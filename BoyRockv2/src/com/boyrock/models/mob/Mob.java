package com.boyrock.models.mob;

import java.util.List;
import java.util.Random;

import com.girlkun.network.io.Message;
import com.boyrock.consts.ConstMap;
import com.boyrock.consts.ConstMob;
import com.boyrock.consts.ConstPlayer;
import com.boyrock.consts.ConstTask;
import com.boyrock.models.item.Item;
import com.boyrock.models.map.ItemMap;
import com.boyrock.models.map.Zone;
import com.boyrock.models.player.Location;
import com.boyrock.models.player.Pet;
import com.boyrock.models.player.Player;
import com.boyrock.models.reward.ItemMobReward;
import com.boyrock.models.reward.MobReward;
import com.boyrock.server.Maintenance;
import com.boyrock.server.Manager;
import com.boyrock.server.ServerManager;
import com.boyrock.services.*;
import com.boyrock.utils.Logger;
import com.boyrock.utils.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class Mob {

    public int id;
    public Zone zone;
    public int tempId;
    public String name;
    public byte level;

    public MobPoint point;
    public MobEffectSkill effectSkill;
    public Location location;

    public byte pDame;
    public int pTiemNang;
    private long maxTiemNang;
    public boolean isMobMe;
    public long lastTimeDie;
    public int lvMob = 0;
    public int status = 5;
    private int action = 0;
    public short cx;
    public short cy;
    public int Gio;

    public Mob(Mob mob) {
        this.point = new MobPoint(this);
        this.effectSkill = new MobEffectSkill(this);
        this.location = new Location();
        this.id = mob.id;
        this.tempId = mob.tempId;
        this.level = mob.level;
        this.point.setHpFull(mob.point.getHpFull());
        this.point.sethp(this.point.getHpFull());
        this.location.x = mob.location.x;
        this.location.y = mob.location.y;
        this.pDame = mob.pDame;
        this.pTiemNang = mob.pTiemNang;
        this.setTiemNang();
    }

    public Mob() {
        this.point = new MobPoint(this);
        this.effectSkill = new MobEffectSkill(this);
        this.location = new Location();
    }

    public static void initMobBanDoKhoBau(Mob mob, byte level) {
        mob.level = level;
        mob.point.dame = level * 4250 * mob.level * 3;
        mob.point.maxHp = level * 20000 * mob.level * 2 + level * 107263 * mob.tempId;
    }

    public static void initMobKhiGaHuyDiet(Mob mob, byte level) {
        mob.level = level;
        mob.point.dame = level * 4250 * mob.level * 3;
        mob.point.maxHp = level * 22472 * mob.level * 2 + level * 107263 * mob.tempId;
    }

    public static void hoiSinhMob(Mob mob) {
        mob.point.hp = mob.point.maxHp;
        mob.setTiemNang();
        Message msg;
        try {
            msg = new Message(-13);
            msg.writer().writeByte(mob.id);
            msg.writer().writeByte(mob.tempId);
            msg.writer().writeByte(0); // level mob
            msg.writer().writeInt((mob.point.hp));
            Service.getInstance().sendMessAllPlayerInMap(mob.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void setTiemNang() {
        this.maxTiemNang = (long) this.point.getHpFull() * (this.pTiemNang + Util.nextInt(-2, 2)) / 100;
    }

    private long lastTimeAttackPlayer;

    public boolean isDie() {
        return this.point.gethp() <= 0;
    }

    public boolean isSieuQuai() {
        return this.lvMob > 0;
    }

    public void sendMobDieAfterMobMeAttacked(Player plKill, int dameHit) {
        this.status = 0;
        Message msg;
        try {
            if (this.id == 13) {
                this.zone.isbulon13Alive = false;
            }
            if (this.id == 14) {
                this.zone.isbulon14Alive = false;
            }
            msg = new Message(-12);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt(dameHit);
            msg.writer().writeBoolean(false); // crit

            List<ItemMap> items = mobReward(plKill, this.dropItemTask(plKill), msg);
            Service.getInstance().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
            hutItem(plKill, items);
        } catch (IOException e) {
            Logger.logException(Mob.class, e);
        }
        // if (plKill.isPl()) {
        // if (TaskService.gI().IsTaskDoWithMemClan(plKill.playerTask.taskMain.id)) {
        // TaskService.gI().checkDoneTaskKillMob(plKill, this, true);
        // } else {
        // TaskService.gI().checkDoneTaskKillMob(plKill, this, false);
        // }
        //
        // }
        this.lastTimeDie = System.currentTimeMillis();
    }

    public void randomSieuQuai() {
        if (this.tempId != 0 && this.point.maxHp >= 3000 && MapService.gI().isMapKhongCoSieuQuai(this.zone)
                && Util.isTrue(1, 10)) {
            this.lvMob = 1;
        }
    }

    public synchronized void injured(Player plAtt, int damage, boolean dieWhenHpFull) {
        if (!this.isDie()) {
            if (damage >= this.point.hp) {
                damage = this.point.hp;
            }
            if (!dieWhenHpFull) {
                if (this.point.hp == this.point.maxHp && damage >= this.point.hp) {
                    damage = this.point.hp;
                }
                if (this.tempId == 0) {
                    damage = 10;
                }
                 if (this.tempId == 70) {
                    damage = 5000000;
                }
            }

            this.point.hp -= damage;
            if (this.isDie()) {
                this.status = 0;
                this.sendMobDieAffterAttacked(plAtt, damage);
                TaskService.gI().checkDoneTaskKillMob(plAtt, this);
                TaskService.gI().checkDoneSideTaskKillMob(plAtt, this);
                TaskService.gI().checkDoneSideTaskKillMob1(plAtt, this);
//                TaskService.gI().checkDoneClanTaskKillMob(plAtt, this);

                this.lastTimeDie = System.currentTimeMillis();
                if (this.id == 13) {
                    this.zone.isbulon13Alive = false;
                }
                if (this.id == 14) {
                    this.zone.isbulon14Alive = false;
                }
                if (this.isSieuQuai()) {
                    plAtt.achievement.plusCount(12);
                }
                if (this.tempId == 0) {
                    plAtt.achievement.plusCount(7);
                }
            } else {
                this.sendMobStillAliveAffterAttacked(damage, plAtt != null ? plAtt.nPoint.isCrit : false);
            }

            Service.gI().addSMTN(plAtt, (byte) 2, getTiemNangForPlayer(plAtt, damage), true);

        }
    }

    private boolean isHaveEffectSkill() {
        return effectSkill.isAnTroi || effectSkill.isBlindDCTT || effectSkill.isStun || effectSkill.isThoiMien
                || effectSkill.isSocola  || effectSkill.isHoaLanh;
    }

    private void BigbossAttack() {
        if (!isDie() && !isHaveEffectSkill() && Util.canDoWithTime(lastTimeAttackPlayer, 1000)) {
            Message msg = null;
            try {
                switch (tempId) {
                    case 70: // Hirudegarn
                        if (!Util.canDoWithTime(lastTimeAttackPlayer, 2000)) {
                            return;
                        }
                        if (this.isDie()) {
                            return;
                        }
                        // 0: bắn - 1: Quật đuôi - 2: dậm chân - 3: Bay - 4: tấn công - 5: Biến hình -
                        // 6: Biến hình lên cấp
                        // 7: vận chiêu - 8: Di chuyển - 9: Die
                        int[] idAction = new int[] { 1, 2, 3, 7 };
                        if (this.level >= 2) {

                            idAction = new int[] { 1, 2 };
                        }
                        action = action == 7 ? 0 : idAction[Util.nextInt(0, idAction.length - 1)];
                        int index = Util.nextInt(0, zone.getPlayers().size() - 1);
                        Player player = zone.getPlayers().get(index);
                        if (player == null || player.isDie()) {
                            return;
                        }
                        if (action == 1) {
                            location.x = (short) player.location.x;
                            Service.getInstance().sendBigBoss2(zone, 8, this);
                        }
                        msg = new Message(101);
                        msg.writer().writeByte(action);
                        if (action >= 0 && action <= 4) {
                            if (action == 1) {
                                msg.writer().writeByte(1);
                                int dame = (int) player.injured(player, (int) this.point.getDameAttack(), false, true);
                                if (dame <= 0) {
                                    dame = 1;
                                }

                                msg.writer().writeInt((int) player.id);
                                msg.writer().writeInt(dame);
                            } else if (action == 3) {
                                location.x = (short) player.location.x;
                                msg.writer().writeShort(location.x);
                                msg.writer().writeShort(location.y);
                            } else {
                                msg.writer().writeByte(zone.getHumanoids().size());
                                for (int i = 0; i < zone.getHumanoids().size(); i++) {
                                    Player pl = zone.getHumanoids().get(i);
                                    int dame = (int) player.injured(player, (int) this.point.getDameAttack(), false,
                                            true);
                                    if (dame <= 0) {
                                        dame = 1;
                                    }

                                    msg.writer().writeInt((int) pl.id);
                                    msg.writer().writeInt(dame);
                                }
                            }
                        } else {
                            if (action == 6 || action == 8) {
                                location.x = (short) player.location.x;
                                msg.writer().writeShort(location.x);
                                msg.writer().writeShort(location.y);
                            }
                        }
                        Service.getInstance().sendMessAllPlayerInMap(zone, msg);
                        lastTimeAttackPlayer = System.currentTimeMillis();
                        break;
                    case 71: // Vua Bạch Tuộc
                        int[] idAction2 = new int[] { 3, 4, 5 };
                        action = action == 7 ? 0 : idAction2[Util.nextInt(0, idAction2.length - 1)];
                        int index2 = Util.nextInt(0, zone.getPlayers().size() - 1);
                        Player player2 = zone.getPlayers().get(index2);
                        if (player2 == null || player2.isDie()) {
                            return;
                        }
                        msg = new Message(102);
                        msg.writer().writeByte(action);
                        if (action >= 0 && action <= 5) {
                            if (action != 5) {
                                msg.writer().writeByte(1);
                                int dame = (int) player2.injured(player2, (int) this.point.getDameAttack(), false,
                                        true);
                                if (dame <= 0) {
                                    dame = 1;
                                }
                                msg.writer().writeInt((int) player2.id);
                                msg.writer().writeInt(dame);
                            }
                            if (action == 5) {
                                location.x = (short) player2.location.x;
                                msg.writer().writeShort(location.x);
                            }
                        } else {

                        }
                        Service.getInstance().sendMessAllPlayerInMap(zone, msg);
                        lastTimeAttackPlayer = System.currentTimeMillis();
                        break;
                    case 72: // Rôbốt bảo vệ
                        int[] idAction3 = new int[] { 0, 1, 2,7 };
                        action = action == 7 ? 0 : idAction3[Util.nextInt(0, idAction3.length - 1)];
                        int index3 = Util.nextInt(0, zone.getPlayers().size() - 1);
                        Player player3 = zone.getPlayers().get(index3);
                        if (player3 == null || player3.isDie()) {
                            return;
                        }
                        msg = new Message(102);
                        msg.writer().writeByte(action);
                        if (action >= 0 && action <= 2) {
                            msg.writer().writeByte(1);
                            int dame = (int) player3.injured(player3, (int) this.point.getDameAttack(), false, true);
                            if (dame <= 0) {
                                dame = 1;
                            }
                            msg.writer().writeInt((int) player3.id);
                            msg.writer().writeInt(dame);
                        }
                        Service.getInstance().sendMessAllPlayerInMap(zone, msg);
                        lastTimeAttackPlayer = System.currentTimeMillis();
                        break;
                }
            } catch (Exception e) {
                // Util.debug("ERROR BIG BOSS");
            } finally {
                if (msg != null) {
                    msg.cleanup();
                    msg = null;
                }
            }
        }
    }

    public long getTiemNangForPlayer(Player pl, long dame) {
        int levelPlayer = Service.gI().getCurrLevel(pl);
        int n = levelPlayer - this.level;
        long pDameHit = dame * 100 / point.getHpFull();
        long tiemNang = pDameHit * maxTiemNang / 100;
        if (tiemNang <= 0) {
            tiemNang = 1;
        }
        if (n >= 0) {
            for (int i = 0; i < n; i++) {
                long sub = tiemNang * 10 / 100;
                if (sub <= 0) {
                    sub = 1;
                }
                tiemNang -= sub;
            }
        } else {
            for (int i = 0; i < -n; i++) {
                long add = tiemNang * 10 / 100;
                if (add <= 0) {
                    add = 1;
                }
                tiemNang += add;
            }
        }

        // dia nguc
        if (pl.zone.map.mapId > 197 && pl.zone.map.mapId <= 203) {
            tiemNang *= 3;
        }
        // mn
        if (pl.zone.map.mapId > 179 && pl.zone.map.mapId <= 183) {
            tiemNang *= 10;
        }
        // dt
        if (pl.zone.map.mapId > 53 && pl.zone.map.mapId <= 62) {
            tiemNang *= 10;
        }
        //
        if (pl.zone.map.mapId > 157 && pl.zone.map.mapId <= 159) {
            tiemNang /= 20000;
        }
        if ((pl.zone.map.mapId == 174 || pl.zone.map.mapId == 175 || pl.zone.map.mapId == 176)) {
            tiemNang *= 2.5;
        }
        if (pl.zone.map.mapId == 146 || pl.zone.map.mapId == 147 || pl.zone.map.mapId == 148) {
            tiemNang *= 2;
        }
        if (pl.zone.map.mapId >= 92 && pl.zone.map.mapId <= 100) {
            tiemNang *= 1.5;
        }
        if (pl.zone.map.mapId >= 105 && pl.zone.map.mapId <= 110) {
            tiemNang *= 2;
        }
        tiemNang = (int) pl.nPoint.calSucManhTiemNang(tiemNang);
        return tiemNang;
    }

    public void update() {
        if (this.tempId == 71) {
            try {
                Message msg = new Message(102);
                msg.writer().writeByte(5);
                msg.writer().writeShort(this.zone.getPlayers().get(0).location.x);
                Service.gI().sendMessAllPlayerInMap(zone, msg);
                msg.cleanup();
            } catch (Exception e) {
            }
        }

        if (isDie() && (tempId == 71 || tempId == 72)) {
            Service.getInstance().sendBigBoss(this.zone, tempId == 71 ? 7 : 6, 0, -1, -1);
        }

        if (isDie() && this.tempId == 70 && (System.currentTimeMillis() - lastTimeDie) > 3000 && level <= 2) {
            if (level == 0) {
                level = 1;
                action = 6;
                this.point.hp = this.point.maxHp;
            } else if (level == 1) {
                level = 2;
                action = 5;
                this.point.hp = this.point.maxHp;
            } else if (level == 2) {
                level = 3;
                action = 9;
            }
            int trai = 0;
            int phai = 1;
            int next = 0;

            for (int i = 0; i < 30; i++) {
                int X = next == 0 ? -7 * trai : 7 * phai;
                if (next == 0) {
                    trai++;
                } else {
                    phai++;
                }
                next = next == 0 ? 1 : 0;
                if (trai > 10) {
                    trai = 0;
                }
                if (phai > 10) {
                    phai = 0;
                }
                
                ItemMap itemMap = new ItemMap(zone, 1184, Util.nextInt(1, 2),
                        location.x + X + Util.nextInt(10, 20), location.y, -1);
                Service.getInstance().dropItemMap(zone, itemMap);
            }
            if (Util.isTrue(40, 100)) {
                for (int i = 0; i < Util.nextInt(1, 3); i++) {
                    ItemMap itemMap2 = new ItemMap(zone, 568, 1, location.x + Util.nextInt(-100, 100), location.y, -1);
                    Service.getInstance().dropItemMap(zone, itemMap2);
                }
                Random random = new Random();
                if (Util.isTrue(50, 100)) {
                    for (int i = 0; i < Util.nextInt(3, 6); i++) {
                        byte randomItemIndexDoTl = (byte) random.nextInt(Manager.itemIds_TL.length);
                        ItemMap itemMap3 = Util.ratiItem(zone, Manager.itemIds_TL[randomItemIndexDoTl], 1,
                                location.x + Util.nextInt(-100, 100), location.y, -1);
                        Service.getInstance().dropItemMap(zone, itemMap3);
                    }
                }
            }
            Service.gI().sendBigBoss2(zone, action, this);
            if (level <= 2) {
                Message msg = null;
                try {
                    msg = new Message(-9);
                    msg.writer().writeByte(this.id);
                    msg.writer().writeInt((int) this.point.hp);
                    msg.writer().writeInt(1);
                    Service.gI().sendMessAllPlayerInMap(zone, msg);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (msg != null) {
                        msg.cleanup();
                        msg = null;
                    }
                }
            } else {
                location.x = -1000;
                location.y = -1000;
            }
        }

        if (this.isDie() && !Maintenance.isRuning) {
            switch (zone.map.type) {

                case ConstMap.MAP_DOANH_TRAI:
                    if (this.zone.isTrungUyTrangAlive && this.tempId == 22 && this.zone.map.mapId == 59) {
                        if (Util.canDoWithTime(lastTimeDie, 5000)) {
                            if (this.id == 13) {
                                this.zone.isbulon13Alive = true;
                            }
                            if (this.id == 14) {
                                this.zone.isbulon14Alive = true;
                            }
                            this.hoiSinh();
                            this.sendMobHoiSinh();
                        }
                    }
                    break;
                case ConstMap.MAP_MI_NUONG:
                    if (this.zone.isTrungUyTrangAlive && this.tempId == 22 && this.zone.map.mapId == 59) {
                        if (Util.canDoWithTime(lastTimeDie, 5000)) {
                            if (this.id == 13) {
                                this.zone.isbulon13Alive = true;
                            }
                            if (this.id == 14) {
                                this.zone.isbulon14Alive = true;
                            }
                            this.hoiSinh();
                            this.sendMobHoiSinh();

                        }
                    }
                    break;
                case ConstMap.MAP_BAN_DO_KHO_BAU:
                    break;
                case ConstMap.MAP_KHI_GA_HUY_DIET:
                    break;
                default:
                    if (Util.canDoWithTime(lastTimeDie, 7000)) {
                        if (this.tempId == 77) {
                            long currentTime = System.currentTimeMillis();
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(currentTime);
                            cal.set(Calendar.HOUR_OF_DAY, 20); // Đặt giờ hồi sinh là 20:00
                            cal.set(Calendar.MINUTE, 0);
                            cal.set(Calendar.SECOND, 0);
                            long respawnTime = cal.getTimeInMillis();

                            // Kiểm tra nếu đã đến thời gian hồi sinh
                            if (currentTime >= respawnTime) {
                                this.sendMobHoiSinh();
                            }
                        } else {
                            this.hoiSinh();
                            this.sendMobHoiSinh();
                            this.randomSieuQuai();
                        }
                    }
            }
        }
        effectSkill.update();
        if (tempId >= 70 && tempId <= 72) {
            BigbossAttack();
        } else {
            attackPlayer();
        }
    }

    private void attackPlayer() {
        if (!isDie() && !effectSkill.isHaveEffectSkill() && !(tempId == 0)
                && Util.canDoWithTime(lastTimeAttackPlayer, 2000)) {
            Player pl = getPlayerCanAttack();
            if (pl != null) {
                this.mobAttackPlayer(pl);
            }
            this.lastTimeAttackPlayer = System.currentTimeMillis();
        }
    }

    private Player getPlayerCanAttack() {
        int distance = 150;
        Player plAttack = null;
        try {
            List<Player> players = this.zone.getNotBosses();
            for (Player pl : players) {
                if (!pl.isDie() && !pl.isBoss && !pl.effectSkin.isVoHinh) {
                    int dis = Util.getDistance(pl, this);
                    if (dis <= distance) {
                        plAttack = pl;
                        distance = dis;
                    }
                }
            }
        } catch (Exception e) {

        }
        return plAttack;
    }

    // **************************************************************************
    private void mobAttackPlayer(Player player) {
        int dameMob = this.point.getDameAttack();
        if (player.charms.tdDaTrau > System.currentTimeMillis()) {
            dameMob /= 2;
        }
        if (this.isSieuQuai() && !(player.charms.tdOaiHung > System.currentTimeMillis())) {
            dameMob = player.nPoint.hpMax / 10;
        }
        int dame = player.injured(null, dameMob, false, true);
        this.sendMobAttackMe(player, dame);
        this.sendMobAttackPlayer(player);
    }

    private void sendMobAttackMe(Player player, int dame) {
        if (!player.isPet && !player.isNewPet) {
            Message msg;
            try {
                msg = new Message(-11);
                msg.writer().writeByte(this.id);
                msg.writer().writeInt(dame); // dame
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
            }
        }
    }

    private void sendMobAttackPlayer(Player player) {
        Message msg;
        try {
            msg = new Message(-10);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeInt(player.nPoint.hp);
            Service.gI().sendMessAnotherNotMeInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void hoiSinh() {
        this.status = 5;
        this.point.hp = this.isSieuQuai() ? this.point.maxHp * 10 : this.point.maxHp;
        this.setTiemNang();
    }

    public void sendMobHoiSinh() {
        Message msg;
        try {
            msg = new Message(-13);
            msg.writer().writeByte(this.id);
            msg.writer().writeByte(this.tempId);
            msg.writer().writeByte(lvMob);
            msg.writer().writeInt(this.point.hp);
            Service.gI().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    // **************************************************************************
    private void sendMobDieAffterAttacked(Player plKill, int dameHit) {
        Message msg;
        try {
            msg = new Message(-12);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt(dameHit);
            msg.writer().writeBoolean(plKill.nPoint.isCrit); // crit
            List<ItemMap> items = mobReward(plKill, this.dropItemTask(plKill), msg);
            Service.gI().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
            hutItem(plKill, items);
        } catch (Exception e) {
        }
    }

    private void hutItem(Player player, List<ItemMap> items) {
        if (!player.isPet && !player.isNewPet) {
            if (player.charms.tdThuHut > System.currentTimeMillis()) {
                for (ItemMap item : items) {
                    if (item.itemTemplate.id != 590) {
                        ItemMapService.gI().pickItem(player, item.itemMapId, true);
                    }
                }
            }
        } else {
            if (((Pet) player).master.charms.tdThuHut > System.currentTimeMillis()) {
                for (ItemMap item : items) {
                    if (item.itemTemplate.id != 590) {
                        ItemMapService.gI().pickItem(((Pet) player).master, item.itemMapId, true);
                    }
                }
            }
        }
    }

    private List<ItemMap> mobReward(Player player, ItemMap itemTask, Message msg) {
        // nplayer
        List<ItemMap> itemReward = new ArrayList<>();
        try {

            itemReward = this.getItemMobReward(player, this.location.x + Util.nextInt(-10, 10),
                    this.zone.map.yPhysicInTop(this.location.x, this.location.y));
            if (itemTask != null) {
                itemReward.add(itemTask);
            }
            msg.writer().writeByte(itemReward.size()); // sl item roi
            for (ItemMap itemMap : itemReward) {
                msg.writer().writeShort(itemMap.itemMapId);// itemmapid
                msg.writer().writeShort(itemMap.itemTemplate.id); // id item
                msg.writer().writeShort(itemMap.x); // xend item
                msg.writer().writeShort(itemMap.y); // yend item
                msg.writer().writeInt((int) itemMap.playerId); // id nhan nat
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(Manager.class, e, "Lỗi mobReward");
        }
        return itemReward;
    }

    public List<ItemMap> getItemMobReward(Player player, int x, int yEnd) {
        List<ItemMap> list = new ArrayList<>();
        MobReward mobReward = Manager.MOB_REWARDS.get(this.tempId);
        if (mobReward == null) {
            return list;

        }
        final Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(11);

        List<ItemMobReward> items = mobReward.getItemReward();
        List<ItemMobReward> golds = mobReward.getGoldReward();
        if (!items.isEmpty()) {
            ItemMobReward item = items.get(Util.nextInt(0, items.size() - 1));
            ItemMap itemMap = item.getItemMap(zone, player, x, yEnd);
            if (itemMap != null) {
                list.add(itemMap);
            }
        }
        if (!golds.isEmpty()) {
            ItemMobReward gold = golds.get(Util.nextInt(0, golds.size() - 1));
            ItemMap itemMap = gold.getItemMap(zone, player, x, yEnd);
            if (itemMap != null) {
                list.add(itemMap);
            }
        }
        if (player.itemTime.isUseMayDo && Util.isTrue(10, 100) && this.tempId > 57 && this.tempId < 66) {
            list.add(new ItemMap(zone, 380, 1, x, player.location.y, player.id));
            if (Util.isTrue(1, 100) && this.tempId > 57 && this.tempId < 66) { // up bí kíp
                list.add(new ItemMap(zone, Util.nextInt(1099, 1102), 1, x, player.location.y, player.id));
            }
        } // vat phẩm rơi khi user maaáy dò adu hoa r o day ti code choa
      
       
        if (player.setClothes.setHuyDiet() && (this.zone.map.mapId >= 174 && this.zone.map.mapId <= 176)) {
            if (Util.isTrue(1, 400)) { // up bí kíp
                list.add(new ItemMap(zone, Util.nextInt(1066, 1070), 1, x, player.location.y, player.id));
            }
        }

        try {
            Item itemHe = player.inventory.itemsBody.get(1);
            if (this.zone.map.mapId > 0) {
                if (itemHe.isNotNullItem()) {
                    if (itemHe.template.id == 691) {
                        if (Util.isTrue(5, 100)) { // up bí kíp
                            list.add(new ItemMap(zone, Util.nextInt(695, 698), 1, x, player.location.y,
                                    player.id));
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
        // /////
       
        
        ///
        // bi kip
        if (this.zone.map.mapId >= 131 && this.zone.map.mapId <= 133) {
            if (Util.isTrue(1, 2)) {
                list.add(new ItemMap(zone, 590, 1, x, player.location.y, player.id));
            }
        }
        // manh vo bong tai
       
        if (this.zone.map.mapId >= 0 && player != null && player.itemTime.isEatCaiVot && this.zone.map.mapId >= 27
                && this.zone.map.mapId <= 30) {
            if (Util.isTrue(18, 100)) {
                list.add(new ItemMap(zone, Util.nextInt(1350, 1353), 1, x, player.location.y, player.id));
            }
        }
        // thoi vang
       
        /*
         * // Roi Do Than Cold
         * if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
         * if (Util.isTrue(1, 5000)) {
         * Item Quanthanlinh = ItemService.gI().createNewItem((short) (556));
         * Quanthanlinh.itemOptions.add(new Item.ItemOption(22, Util.nextInt(55, 65)));
         * Quanthanlinh.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
         * if (Util.isTrue(2, 10)) {
         * Quanthanlinh.itemOptions.add(new Item.ItemOption(107, Util.nextInt(0, 6)));
         * }
         * InventoryServiceNew.gI().addItemBag(player, Quanthanlinh);
         * InventoryServiceNew.gI().sendItemBags(player);
         * Service.gI().sendThongBao(player, "Bạn vừa nhận được " +
         * Quanthanlinh.template.name);
         * }
         * }
         * if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
         * if (Util.isTrue(1, 5000)) {
         * Item Quanthanlinhxd = ItemService.gI().createNewItem((short) (560));
         * Quanthanlinhxd.itemOptions.add(new Item.ItemOption(22, Util.nextInt(45,
         * 55)));
         * Quanthanlinhxd.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15,
         * 17)));
         * if (Util.isTrue(2, 10)) {
         * Quanthanlinhxd.itemOptions.add(new Item.ItemOption(107, Util.nextInt(0, 6)));
         * }
         * InventoryServiceNew.gI().addItemBag(player, Quanthanlinhxd);
         * InventoryServiceNew.gI().sendItemBags(player);
         * Service.gI().sendThongBao(player, "Bạn vừa nhận được " +
         * Quanthanlinhxd.template.name);
         * }
         * }
         * if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
         * if (Util.isTrue(1, 5000)) {
         * Item Quanthanlinhnm = ItemService.gI().createNewItem((short) (558));
         * Quanthanlinhnm.itemOptions.add(new Item.ItemOption(22, Util.nextInt(50,
         * 60)));
         * Quanthanlinhnm.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15,
         * 17)));
         * if (Util.isTrue(2, 10)) {
         * Quanthanlinhnm.itemOptions.add(new Item.ItemOption(107, Util.nextInt(0, 6)));
         * }
         * InventoryServiceNew.gI().addItemBag(player, Quanthanlinhnm);
         * InventoryServiceNew.gI().sendItemBags(player);
         * Service.gI().sendThongBao(player, "Bạn vừa nhận được " +
         * Quanthanlinhnm.template.name);
         * }
         * }
         * if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
         * if (Util.isTrue(1, 5000)) {
         * Item Aothanlinh = ItemService.gI().createNewItem((short) (555));
         * Aothanlinh.itemOptions.add(new Item.ItemOption(47, Util.nextInt(500, 600)));
         * Aothanlinh.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
         * if (Util.isTrue(2, 10)) {
         * Aothanlinh.itemOptions.add(new Item.ItemOption(107, Util.nextInt(0, 6)));
         * }
         * InventoryServiceNew.gI().addItemBag(player, Aothanlinh);
         * InventoryServiceNew.gI().sendItemBags(player);
         * Service.gI().sendThongBao(player, "Bạn vừa nhận được " +
         * Aothanlinh.template.name);
         * }
         * }
         * if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
         * if (Util.isTrue(1, 5000)) {
         * Item Aothanlinhxd = ItemService.gI().createNewItem((short) (559));
         * Aothanlinhxd.itemOptions.add(new Item.ItemOption(47, Util.nextInt(600,
         * 700)));
         * Aothanlinhxd.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
         * if (Util.isTrue(2, 10)) {
         * Aothanlinhxd.itemOptions.add(new Item.ItemOption(107, Util.nextInt(0, 6)));
         * }
         * InventoryServiceNew.gI().addItemBag(player, Aothanlinhxd);
         * InventoryServiceNew.gI().sendItemBags(player);
         * Service.gI().sendThongBao(player, "Bạn vừa nhận được " +
         * Aothanlinhxd.template.name);
         * }
         * }
         * if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
         * if (Util.isTrue(1, 5000)) {
         * Item Aothanlinhnm = ItemService.gI().createNewItem((short) (557));
         * Aothanlinhnm.itemOptions.add(new Item.ItemOption(47, Util.nextInt(400,
         * 550)));
         * Aothanlinhnm.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
         * if (Util.isTrue(2, 10)) {
         * Aothanlinhnm.itemOptions.add(new Item.ItemOption(107, Util.nextInt(0, 6)));
         * }
         * InventoryServiceNew.gI().addItemBag(player, Aothanlinhnm);
         * InventoryServiceNew.gI().sendItemBags(player);
         * Service.gI().sendThongBao(player, "Bạn vừa nhận được " +
         * Aothanlinhnm.template.name);
         * }
         * }
         * if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
         * if (Util.isTrue(1, 5000)) {
         * Item Gangthanlinh = ItemService.gI().createNewItem((short) (562));
         * Gangthanlinh.itemOptions.add(new Item.ItemOption(0, Util.nextInt(6000,
         * 7000)));
         * Gangthanlinh.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
         * if (Util.isTrue(2, 10)) {
         * Gangthanlinh.itemOptions.add(new Item.ItemOption(107, Util.nextInt(0, 6)));
         * }
         * InventoryServiceNew.gI().addItemBag(player, Gangthanlinh);
         * InventoryServiceNew.gI().sendItemBags(player);
         * Service.gI().sendThongBao(player, "Bạn vừa nhận được " +
         * Gangthanlinh.template.name);
         * }
         * }
         * if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
         * if (Util.isTrue(1, 5000)) {
         * Item Gangthanlinhxd = ItemService.gI().createNewItem((short) (566));
         * Gangthanlinhxd.itemOptions.add(new Item.ItemOption(0, Util.nextInt(6500,
         * 7500)));
         * Gangthanlinhxd.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15,
         * 17)));
         * if (Util.isTrue(2, 10)) {
         * Gangthanlinhxd.itemOptions.add(new Item.ItemOption(107, Util.nextInt(0, 6)));
         * }
         * InventoryServiceNew.gI().addItemBag(player, Gangthanlinhxd);
         * InventoryServiceNew.gI().sendItemBags(player);
         * Service.gI().sendThongBao(player, "Bạn vừa nhận được " +
         * Gangthanlinhxd.template.name);
         * }
         * }
         * if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
         * if (Util.isTrue(1, 5000)) {
         * Item Gangthanlinhnm = ItemService.gI().createNewItem((short) (564));
         * Gangthanlinhnm.itemOptions.add(new Item.ItemOption(0, Util.nextInt(5500,
         * 6500)));
         * Gangthanlinhnm.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15,
         * 17)));
         * if (Util.isTrue(2, 10)) {
         * Gangthanlinhnm.itemOptions.add(new Item.ItemOption(107, Util.nextInt(0, 6)));
         * }
         * InventoryServiceNew.gI().addItemBag(player, Gangthanlinhnm);
         * InventoryServiceNew.gI().sendItemBags(player);
         * Service.gI().sendThongBao(player, "Bạn vừa nhận được " +
         * Gangthanlinhnm.template.name);
         * }
         * }
         * if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
         * if (Util.isTrue(1, 5000)) {
         * Item Giaythanlinh = ItemService.gI().createNewItem((short) (563));
         * Giaythanlinh.itemOptions.add(new Item.ItemOption(23, Util.nextInt(50, 60)));
         * Giaythanlinh.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15, 17)));
         * if (Util.isTrue(2, 10)) {
         * Giaythanlinh.itemOptions.add(new Item.ItemOption(107, Util.nextInt(0, 6)));
         * }
         * InventoryServiceNew.gI().addItemBag(player, Giaythanlinh);
         * InventoryServiceNew.gI().sendItemBags(player);
         * Service.gI().sendThongBao(player, "Bạn vừa nhận được " +
         * Giaythanlinh.template.name);
         * }
         * }
         * if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
         * if (Util.isTrue(1, 5000)) {
         * Item Giaythanlinhxd = ItemService.gI().createNewItem((short) (567));
         * Giaythanlinhxd.itemOptions.add(new Item.ItemOption(23, Util.nextInt(55,
         * 65)));
         * Giaythanlinhxd.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15,
         * 17)));
         * if (Util.isTrue(2, 10)) {
         * Giaythanlinhxd.itemOptions.add(new Item.ItemOption(107, Util.nextInt(0, 6)));
         * }
         * InventoryServiceNew.gI().addItemBag(player, Giaythanlinhxd);
         * InventoryServiceNew.gI().sendItemBags(player);
         * Service.gI().sendThongBao(player, "Bạn vừa nhận được " +
         * Giaythanlinhxd.template.name);
         * }
         * }
         * if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
         * if (Util.isTrue(1, 5000)) {
         * Item Giaythanlinhnm = ItemService.gI().createNewItem((short) (565));
         * Giaythanlinhnm.itemOptions.add(new Item.ItemOption(23, Util.nextInt(65,
         * 75)));
         * Giaythanlinhnm.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15,
         * 17)));
         * if (Util.isTrue(2, 10)) {
         * Giaythanlinhnm.itemOptions.add(new Item.ItemOption(107, Util.nextInt(0, 6)));
         * }
         * InventoryServiceNew.gI().addItemBag(player, Giaythanlinhnm);
         * InventoryServiceNew.gI().sendItemBags(player);
         * Service.gI().sendThongBao(player, "Bạn vừa nhận được " +
         * Giaythanlinhnm.template.name);
         * }
         * }
         * if (this.zone.map.mapId >= 105 && this.zone.map.mapId <= 110) {
         * if (Util.isTrue(1, 5000)) {
         * Item Giaythanlinhnm = ItemService.gI().createNewItem((short) (561));
         * Giaythanlinhnm.itemOptions.add(new Item.ItemOption(14, Util.nextInt(13,
         * 16)));
         * Giaythanlinhnm.itemOptions.add(new Item.ItemOption(21, Util.nextInt(15,
         * 17)));
         * if (Util.isTrue(2, 10)) {
         * Giaythanlinhnm.itemOptions.add(new Item.ItemOption(107, Util.nextInt(0, 6)));
         * }
         * InventoryServiceNew.gI().addItemBag(player, Giaythanlinhnm);
         * InventoryServiceNew.gI().sendItemBags(player);
         * Service.gI().sendThongBao(player, "Bạn vừa nhận được " +
         * Giaythanlinhnm.template.name);
         * }
         * 
         * }
         */
        return list;
    }

    private ItemMap dropItemTask(Player player) {
        ItemMap itemMap = null;
        switch (this.tempId) {
            case ConstMob.KHUNG_LONG:
            case ConstMob.LON_LOI:
            case ConstMob.QUY_DAT:
                if (TaskService.gI().getIdTask(player) == ConstTask.TASK_2_0) {
                    itemMap = new ItemMap(this.zone, 73, 1, this.location.x, this.location.y, player.id);
                }
                break;
        }
        if (itemMap != null) {
            return itemMap;
        }
        return null;
    }

    private void sendMobStillAliveAffterAttacked(int dameHit, boolean crit) {
        Message msg;
        try {
            msg = new Message(-9);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt(this.point.gethp());
            msg.writer().writeInt(dameHit);
            msg.writer().writeBoolean(crit); // chí mạng
            msg.writer().writeInt(-1);
            Service.gI().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }
}
