package com.boyrock.models.boss;

import com.girlkun.network.io.Message;
import com.boyrock.models.boss.list_boss.Mabu;
import com.boyrock.models.boss.list_boss.SuperXen;
import com.boyrock.models.boss.list_boss.BLACK.*;


import com.boyrock.models.boss.list_boss.Doraemon.Doraemon;

import com.boyrock.models.boss.list_boss.HuyDiet.Champa;
import com.boyrock.models.boss.list_boss.HuyDiet.ThanHuyDiet;
import com.boyrock.models.boss.list_boss.HuyDiet.ThienSuWhis;
import com.boyrock.models.boss.list_boss.HuyDiet.Vados;
import com.boyrock.models.boss.list_boss.Mabu12h.BuiBui;
import com.boyrock.models.boss.list_boss.Mabu12h.BuiBui2;
import com.boyrock.models.boss.list_boss.Mabu12h.Drabura;
import com.boyrock.models.boss.list_boss.Mabu12h.Drabura2;
import com.boyrock.models.boss.list_boss.Mabu12h.MabuBoss;
import com.boyrock.models.boss.list_boss.Mabu12h.Yacon;

import com.boyrock.models.boss.list_boss.android.*;


import com.boyrock.models.boss.list_boss.cell.SieuBoHung;
import com.boyrock.models.boss.list_boss.cell.XenBoHung;
import com.boyrock.models.boss.list_boss.cell.Xencon;



import com.boyrock.models.boss.list_boss.fide.Fide;

import com.boyrock.models.boss.list_boss.ginyu.TDST;

import com.boyrock.models.boss.list_boss.Mabu13h.*;




import com.boyrock.models.boss.list_boss.nappa.*;


import com.boyrock.models.boss.list_boss.chucnang.Granola;
import com.boyrock.models.boss.list_boss.chucnang.Tieudoitruong;
import com.boyrock.models.boss.list_boss.chucnang.bugay;

import com.boyrock.models.boss.list_boss.doanh_trai.*;

import com.boyrock.models.map.Zone;
import com.boyrock.models.player.Player;
import com.boyrock.server.Manager;
import com.boyrock.server.ServerManager;
import com.boyrock.services.ItemMapService;
import com.boyrock.services.MapService;
import com.boyrock.services.Service;
import com.boyrock.services.func.ChangeMapService;
import com.boyrock.utils.Logger;
import com.boyrock.utils.Util;
import com.boyrock.models.boss.list_boss.Mabu13h.BuBung;

import java.util.ArrayList;
import java.util.List;

public class BossManager implements Runnable {

    private static BossManager I;
    public static final byte ratioReward = 30;

    public static BossManager gI() {
        if (BossManager.I == null) {
            BossManager.I = new BossManager();
        }
        return BossManager.I;
    }

    private BossManager() {
        this.bosses = new ArrayList<>();
    }

    private boolean loadedBoss;
    private final List<Boss> bosses;

    public void addBoss(Boss boss) {
        this.bosses.add(boss);
    }

    public void removeBoss(Boss boss) {
        this.bosses.remove(boss);
    }

    public void loadBoss() {
        if (this.loadedBoss) {
            return;
        }
        try {
            this.createBoss(BossID.GRANOLA);
 this.createBoss(BossID.GRANOLA);
  this.createBoss(BossID.GRANOLA);
   this.createBoss(BossID.GRANOLA);
    this.createBoss(BossID.GRANOLA);
            this.createBoss(BossID.FIDE_DIA_NGUC);
            this.createBoss(BossID.XEN_DIA_NGUC);
            this.createBoss(BossID.PAIKUHAN_DIA_NGUC);
            this.createBoss(BossID.DEMON_KING_DIA_NGUC);

          

            this.createBoss(BossID.KHI_DEN);
            this.createBoss(BossID.KHI_BAC);
            this.createBoss(BossID.KHI_VANG);

            this.createBoss(BossID.NHAT_THAN);

            this.createBoss(BossID.TAU_PAY_PAY_ROBOT);
            this.createBoss(BossID.UUB);
              this.createBoss(BossID.MABU);
                this.createBoss(BossID.MABU);
                  this.createBoss(BossID.MABU);
                   this.createBoss(BossID.MABU);
                    this.createBoss(BossID.MABU);
                     this.createBoss(BossID.MABU);
                      this.createBoss(BossID.MABU);
        
            this.createBoss(BossID.CHILL);

            // this.createBoss(BossID.PILAP);
            // this.createBoss(BossID.SU);
            // this.createBoss(BossID.MAI);


            // this.createBoss(BossID.KAMIRIN);
            // this.createBoss(BossID.KAMILOC);
            // this.createBoss(BossID.KAMI_SOOME);

            this.createBoss(BossID.CUMBERBLACK);
            this.createBoss(BossID.CUMBERYELLOW);
            this.createBoss(BossID.CUMBERRED);

            this.createBoss(BossID.SUPER_XEN);

            this.createBoss(BossID.TDST);
             this.createBoss(BossID.TDTNM);

            this.createBoss(BossID.ANDROID_21);
            this.createBoss(BossID.PIC);
            this.createBoss(BossID.POC);
            this.createBoss(BossID.KING_KONG);

            this.createBoss(BossID.SONGOKU_TA_AC);

            this.createBoss(BossID.COOLER_GOLD);

            this.createBoss(BossID.XEN_BO_HUNG);
            this.createBoss(BossID.SIEU_BO_HUNG);

            this.createBoss(BossID.XEN_CON_1);
            // this.createBoss(BossID.XEN_CON_1);
            // this.createBoss(BossID.XEN_CON_1);
            // this.createBoss(BossID.XEN_CON_1);
            // this.createBoss(BossID.XEN_CON_1);

            // bojack

            this.createBoss(BossID.COOLER);

            this.createBoss(BossID.FROST);

            this.createBoss(BossID.THIEN_SU_VADOS);
            this.createBoss(BossID.THIEN_SU_WHIS);

            this.createBoss(BossID.DORAEMON_TEAM);

            // this.createBoss(BossID.Vodaihatmit);

          
            this.createBoss(BossID.GIANGHO);

            // this.createBoss(BossID.SANQUY_TEAM);

            // this.createBoss(BossID.LUFFY_TEAM);

            this.createBoss(BossID.BLACK);
            this.createBoss(BossID.ZAMASZIN);
            this.createBoss(BossID.BLACK2);
            this.createBoss(BossID.ZAMASMAX);
            this.createBoss(BossID.BLACK3);
  
            this.createBoss(BossID.KUKU);
            this.createBoss(BossID.MAP_DAU_DINH);
            this.createBoss(BossID.RAMBO);

            this.createBoss(BossID.FIDE);

            this.createBoss(BossID.FIDE_ROBOT);
            this.createBoss(BossID.VUA_COLD);

            this.createBoss(BossID.DR_KORE);
            this.createBoss(BossID.ANDROID_19);

            this.createBoss(BossID.DUONGTANG);

            this.createBoss(BossID.ANDROID_14);

            this.createBoss(BossID.SUPER_ANDROID_17);
            this.createBoss(BossID.DR_KORE_GT);
            this.createBoss(BossID.DR_MYUU);
            this.createBoss(BossID.CUMBER);
             this.createBoss(BossID.bugay);
               this.createBoss(BossID.bugay);
                 this.createBoss(BossID.bugay);
                   this.createBoss(BossID.bugay);
                     this.createBoss(BossID.bugay);
                     
               

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.loadedBoss = true;
        new Thread(BossManager.I, "Update boss").start();
    }

    public Boss createBossDoanhTrai(int bossID, int dame, int hp, Zone zone) {
        System.out.println("create boss donh trai");
        try {
            switch (bossID) {
                case BossID.TRUNG_UY_TRANG:
                    return new TrungUyTrang(dame, hp, zone);
                case BossID.TRUNG_UY_XANH_LO:
                    return new TrungUyXanhLo(dame, hp, zone);
                case BossID.TRUNG_UY_THEP:
                    return new TrungUyThep(dame, hp, zone);
                case BossID.NINJA_AO_TIM:
                    return new NinjaAoTim(dame, hp, zone);
                case BossID.ROBOT_VE_SI1:
                case BossID.ROBOT_VE_SI2:
                case BossID.ROBOT_VE_SI3:
                case BossID.ROBOT_VE_SI4:
                    return new RobotVeSi(bossID, dame, hp, zone);
                default:
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(Manager.class, e, "Lỗi create boss doanh trại");
            return null;
        }

    }

   
    public Boss createBossDiaNguc(int bossID, int dame, int hp, Zone zone) {
        System.out.println("create boss dia nguc");
        try {
            switch (bossID) {
                // case BossID.SON_TINH:
                // return new Sontinh(dame, hp, zone);
                // case BossID.THUY_TINH:
                // return new Thuytinh(dame, hp, zone);

                default:
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(Manager.class, e, "Lỗi create boss dia nguc");
            return null;
        }
    }

    public Boss createBoss(int bossID) {
        try {
            switch (bossID) {
               
             
                
               
             
                case BossID.KUKU:
                    return new Kuku();
                case BossID.MAP_DAU_DINH:
                    return new MapDauDinh();
                case BossID.RAMBO:
                    return new Rambo();
                case BossID.DRABURA:
                    return new Drabura();
                case BossID.DRABURA_2:
                    return new Drabura2();
                case BossID.BUI_BUI:
                    return new BuiBui();
                case BossID.BUI_BUI_2:
                    return new BuiBui2();
                case BossID.YA_CON:
                    return new Yacon();
                case BossID.MABU_12H:
                    return new MabuBoss();
                      case BossID.MABU:
                    return new Mabu();
//                  case BossID.Rong_1Sao:
//                    return new Rong1Sao();
//                case BossID.Rong_2Sao:
//                    return new Rong2Sao();
//                case BossID.Rong_3Sao:
//                    return new Rong3Sao();
//                case BossID.Rong_4Sao:
//                    return new Rong4Sao();
//                case BossID.Rong_5Sao:
//                    return new Rong5Sao();
//                case BossID.Rong_6Sao:
//                    return new Rong6Sao();
//                case BossID.Rong_7Sao:
//                    return new Rong7Sao();
                case BossID.FIDE:
                    return new Fide();
                case BossID.DR_KORE:
                    return new DrKore();
                case BossID.ANDROID_19:
                    return new Android19();
                case BossID.ANDROID_13:
                    return new Android13();
                case BossID.ANDROID_14:
                    return new Android14();
                case BossID.ANDROID_15:
                    return new Android15();
                case BossID.SUPER_XEN:
                    return new SuperXen();
                  case BossID.ANDROID_21:
                    return new Android21();
                case BossID.PIC:
                    return new Pic();
                case BossID.POC:
                    return new Poc();
                case BossID.KING_KONG:
                    return new KingKong();
                case BossID.XEN_BO_HUNG:
                    return new XenBoHung();
                case BossID.SIEU_BO_HUNG:
                    return new SieuBoHung();
                case BossID.DORAEMON_TEAM:
                    return new Doraemon();
              
               
         
             
              
                case BossID.BLACK:
                    return new Black();
             
                case BossID.XEN_CON_1:
                    return new Xencon();
                     case BossID.TDTNM:
                    return new Tieudoitruong();
                case BossID.TDST:
                    return new TDST();
                 case BossID.THAN_HUY_DIET_CHAMPA:
                    return new Champa();
                case BossID.THIEN_SU_VADOS:
                    return new Vados();
                case BossID.THAN_HUY_DIET:
                    return new ThanHuyDiet();
                case BossID.THIEN_SU_WHIS:
                    return new ThienSuWhis();
                
             
            
                        case BossID.GRANOLA:
                    return new Granola();
             
            
                       case BossID.bugay:
                    return new bugay();
                   
                     
                    
                
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public boolean existBossOnPlayer(Player player) {
        return player.zone.getBosses().size() > 0;
    }

    public void showListBoss(Player player) {
        if (!player.isAdmin()) {
            return;
        }
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Boss");
            msg.writer()
                    .writeByte(
                            (int) bosses.stream()
                                    .filter(boss -> !MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                                            && !MapService.gI().isMapSatan(boss.data[0].getMapJoin()[0])

                                            && !MapService.gI().isMapVodai(boss.data[0].getMapJoin()[0])
                                            && !MapService.gI().isMapBanDoKhoBau(boss.data[0].getMapJoin()[0])
                                            && !MapService.gI().isMapMabu13h(boss.data[0].getMapJoin()[0])
                                            && !MapService.gI().isMapTranhngocnamec(boss.data[0].getMapJoin()[0])
                                            && !MapService.gI().isMapMiNuong(boss.data[0].getMapJoin()[0])
                                            && !MapService.gI().isMapKhiGaHuyDiet(boss.data[0].getMapJoin()[0])
                                            && !MapService.gI().isMapDoanhTrai(boss.data[0].getMapJoin()[0])
                                            && !MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0]))

                                    .count());
            for (int i = 0; i < bosses.size(); i++) {
                Boss boss = this.bosses.get(i);
                if (MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapSatan(boss.data[0].getMapJoin()[0])

                        || MapService.gI().isMapBanDoKhoBau(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapVodai(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapTranhngocnamec(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapMabu13h(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapDoanhTrai(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapMiNuong(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapKhiGaHuyDiet(boss.data[0].getMapJoin()[0]

                        )

                ) {
                    continue;
                }
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeShort(boss.data[0].getOutfit()[0]);
                if (player.getSession().version > 214 || player.getSession().version < 231) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(boss.data[0].getOutfit()[1]);
                msg.writer().writeShort(boss.data[0].getOutfit()[2]);
                msg.writer().writeUTF(boss.data[0].getName());
                if (boss.zone != null) {
                    msg.writer().writeUTF("Sống");
                    msg.writer().writeUTF(
                            boss.zone.map.mapName + "(" + boss.zone.map.mapId + ") khu " + boss.zone.zoneId + "");
                } else {
                    msg.writer().writeUTF("Chết");
                    msg.writer().writeUTF("Chết rồi");
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(Manager.class, e, "Lỗi show list boss");
        }
    }

    public void showListBossNormal(Player player) {
      
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Boss");
            msg.writer()
                    .writeByte(
                            (int) bosses.stream()
                                    .filter(boss -> !MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                                            && !MapService.gI().isMapSatan(boss.data[0].getMapJoin()[0])

                                            && !MapService.gI().isMapVodai(boss.data[0].getMapJoin()[0])
                                            && !MapService.gI().isMapBanDoKhoBau(boss.data[0].getMapJoin()[0])
                                            && !MapService.gI().isMapMabu13h(boss.data[0].getMapJoin()[0])
                                            && !MapService.gI().isMapTranhngocnamec(boss.data[0].getMapJoin()[0])
                                            && !MapService.gI().isMapMiNuong(boss.data[0].getMapJoin()[0])
                                            && !MapService.gI().isMapKhiGaHuyDiet(boss.data[0].getMapJoin()[0])
                                            && !MapService.gI().isMapDoanhTrai(boss.data[0].getMapJoin()[0])
                                            && !MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0]))

                                    .count());
            for (int i = 0; i < bosses.size(); i++) {
                Boss boss = this.bosses.get(i);
                if (MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapSatan(boss.data[0].getMapJoin()[0])

                        || MapService.gI().isMapBanDoKhoBau(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapVodai(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapTranhngocnamec(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapMabu13h(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapDoanhTrai(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapMiNuong(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapKhiGaHuyDiet(boss.data[0].getMapJoin()[0]

                        )

                ) {
                    continue;
                }
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeShort(boss.data[0].getOutfit()[0]);
                if (player.getSession().version > 214 || player.getSession().version < 231) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(boss.data[0].getOutfit()[1]);
                msg.writer().writeShort(boss.data[0].getOutfit()[2]);
                msg.writer().writeUTF(boss.data[0].getName());
                if (boss.zone != null) {
                    msg.writer().writeUTF("Sống");
                    msg.writer().writeUTF(
                             "Tự đi tìm đi");
                } else {
                    msg.writer().writeUTF("Chết");
                    msg.writer().writeUTF("Chết rồi");
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(Manager.class, e, "Lỗi show list boss");
        }
    }

    public void timBoss(Player player, int id) {
        if (!player.isAdmin()) {
            return;
        }
        Boss boss = BossManager.gI().getBossById(id);
        if (boss != null && !boss.isDie()) {

            Zone z = null;
            if (boss.zone != null) {
                z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId,
                        boss.zone.zoneId);
            }
            if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                player.inventory.gold -= 0;
                ChangeMapService.gI().changeMapBySpaceShip(player, boss.zone, boss.location.x);
                Service.gI().sendMoney(player);
            } else {
                Service.gI().sendThongBao(player, "Khu vực đang full.");
            }
        }

    }

    public synchronized void callBoss(Player player, int mapId) {

        try {
            if (BossManager.gI().existBossOnPlayer(player) ||
                    player.zone.items.stream()
                            .anyMatch(itemMap -> ItemMapService.gI().isBlackBall(itemMap.itemTemplate.id))
                    ||
                    player.zone.getPlayers().stream().anyMatch(p -> p.iDMark.isHoldBlackBall())) {
                return;
            }
            Boss k = null;
            switch (mapId) {
                case 85:
                    k = BossManager.gI().createBoss(BossID.Rong_1Sao);
                    break;
                case 86:
                    k = BossManager.gI().createBoss(BossID.Rong_2Sao);
                    break;
                case 87:
                    k = BossManager.gI().createBoss(BossID.Rong_3Sao);
                    break;
                case 88:
                    k = BossManager.gI().createBoss(BossID.Rong_4Sao);
                    break;
                case 89:
                    k = BossManager.gI().createBoss(BossID.Rong_5Sao);
                    break;
                case 90:
                    k = BossManager.gI().createBoss(BossID.Rong_6Sao);
                    break;
                case 91:
                    k = BossManager.gI().createBoss(BossID.Rong_7Sao);
                    break;
            }
            if (k != null) {
                k.currentLevel = 0;
                k.joinMapByZone(player);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(Manager.class, e, "Lỗi callbosss");
        }
    }

    public Boss getBossById(int bossId) {
        return BossManager.gI().bosses.stream().filter(boss -> boss.id == bossId && !boss.isDie()).findFirst()
                .orElse(null);
    }

    @Override
    public void run() {
        while (ServerManager.isRunning) {
            try {
                long st = System.currentTimeMillis();
                for (Boss boss : this.bosses) {
                    boss.update();
                }
                Thread.sleep(150 - (System.currentTimeMillis() - st));
            } catch (Exception ignored) {
            }

        }
    }
}
