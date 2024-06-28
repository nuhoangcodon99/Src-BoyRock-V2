package com.boyrock.server;

import com.girlkun.database.GirlkunDB;
import com.girlkun.network.server.GirlkunSessionManager;
import com.girlkun.network.session.ISession;
import com.kygui.ShopKyGuiManager;
import com.boyrock.consts.ConstPlayer;
import com.boyrock.jdbc.daos.PlayerDAO;
import com.boyrock.models.item.Item;
import com.boyrock.models.map.ItemMap;
import com.boyrock.models.matches.pvp.DaiHoiVoThuat;
import com.boyrock.models.matches.pvp.DaiHoiVoThuatService;
import com.boyrock.models.player.Inventory;
import com.boyrock.models.player.Player;
import com.boyrock.models.skill.Skill;
import com.boyrock.server.io.MySession;
import com.boyrock.services.InventoryServiceNew;
import com.boyrock.services.ItemService;
import com.boyrock.services.ItemTimeService;
import com.boyrock.services.MapService;
import com.boyrock.services.NgocRongNamecService;
import com.boyrock.services.Service;
import com.boyrock.services.func.ChangeMapService;
import com.boyrock.services.func.SummonDragon;
import com.boyrock.services.func.TransactionService;
import com.boyrock.utils.Logger;
import com.boyrock.utils.SkillUtil;
import com.boyrock.utils.Util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Client implements Runnable {

    private static Client i;
 private final List<Player> bots = new ArrayList<>();
    private final Map<Long, Player> players_id = new HashMap<Long, Player>();
    private final Map<Integer, Player> players_userId = new HashMap<Integer, Player>();
    private final Map<String, Player> players_name = new HashMap<String, Player>();
    private final List<Player> players = new ArrayList<>();
  private static int id = 10000;
    private boolean running = true;
    

    private Client() {
        new Thread(this).start();
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public static Client gI() {
        if (i == null) {
            i = new Client();
        }
        return i;
    }

    public void put(Player player) {
        if (!players_id.containsKey(player.id)) {
            this.players_id.put(player.id, player);
        }
        if (!players_name.containsValue(player)) {
            this.players_name.put(player.name, player);
        }
        if (!players_userId.containsValue(player)) {
            this.players_userId.put(player.getSession().userId, player);
        }
        if (!players.contains(player)) {
            this.players.add(player);
        }

    }
     private String generateUniqueName(String[] name1, String[] name2, String[] name3) {
        String generatedName;
        do {
            generatedName = name1[Util.nextInt(name1.length)] + name2[Util.nextInt(name2.length)] + name3[Util.nextInt(name3.length)];
        } while (isNameAlreadyUsed(generatedName));
        return generatedName;
    }

    private boolean isNameAlreadyUsed(String name) {
        return false;
    }
     public static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

   
public void createBot(MySession s) throws Exception {
        String[] name1 = {"zeni", "goku", "cadic", "vegeta", "ssj4", "blue", "solo", "yasuo", "go", "red", "make", "mai"};
        String[] name2 = {"trum", "hehe", "black", "gi", "manh", "nro", "kuku", "seri", "gay", "haoem", "quang", "vinh"};
        String[] name3 = {"top1", "zamus", "berrus", "fire", "son", "cuong", "top2", "sever", "gay", "slayer", "kaka", "love"};
        String[] chat = {"top1", "zamus", "berrus", "fire", "son", "cuong", "top2", "sever", "gay", "slayer", "kaka", "love"};

        Player pl = new Player();
        Player temp = Client.gI().getPlayerByUser(1);//GodGK.loadById(2275);

        pl.setSession(s);
        s.userId = id;
        pl.id = id++;

        // Tạo tên duy nhất
        String uniqueName = generateUniqueName(name1, name2, name3);
        pl.name = uniqueName;

        pl.gender = (byte) Util.nextInt(0, 2);
        pl.isBot = true;
        pl.isBoss = false;
        pl.isPet = false;

        pl.nPoint.power = Util.nextInt(2000, 20000000);

        pl.nPoint.hpg = 100000;
        pl.nPoint.hpMax = Math.max(2000, Util.nextInt(2000, 500000));
        pl.nPoint.hp = Math.min(pl.nPoint.hpMax, Math.max(1, pl.nPoint.hpMax / 2));

        pl.nPoint.mpMax = Math.max(2000, Util.nextInt(2000, 500000));
        pl.nPoint.dame = Util.nextInt(500, 2000);
        pl.nPoint.stamina = 32000;

        pl.autodosat = true;
        pl.typePk = ConstPlayer.NON_PK;

        if (pl.nPoint.hp == 0) {
            Service.gI().hsChar(pl, pl.nPoint.hpMax, pl.nPoint.mpMax);
        }

        //skill
        int[] skillsArr = pl.gender == 0 ? new int[]{0, 1, 6, 9}
                : pl.gender == 1 ? new int[]{2, 3, 7}
                : new int[]{4, 5, 8};

        for (int j = 0; j < skillsArr.length; j++) {
            Skill skill = SkillUtil.createSkill(skillsArr[j], Util.nextInt(1, 7));
            pl.playerSkill.skills.add(skill);
        }

        pl.inventory = new Inventory();
        for (int i = 0; i < 13; i++) {
            pl.inventory.itemsBody.add(ItemService.gI().createItemNull());
        }

        pl.inventory.gold = 2000000000;
        pl.inventory.itemsBody.set(5, Manager.CAITRANG.get(Util.nextInt(0, Manager.CAITRANG.size() - 1)));

        pl.location.y = 50;
        pl.zone = MapService.gI().getMapCanJoin(pl, Util.nextInt(150), Util.nextInt(0, 5));

        if (pl.zone != null && pl.zone.map != null
                && pl.zone.map.mapId != 21 && pl.zone.map.mapId != 22 && pl.zone.map.mapId != 23
                && pl.zone.map.mapId != 135 && pl.zone.map.mapId != 136
                && pl.zone.map.mapId != 137 && pl.zone.map.mapId != 57
                && pl.zone.map.mapId != 138 && pl.zone.map.mapId != 58
                && pl.zone.map.mapId != 53 && pl.zone.map.mapId != 59
                && pl.zone.map.mapId != 60 && pl.zone.map.mapId != 61
                && pl.zone.map.mapId != 62 && pl.zone.map.mapId != 85
                && pl.zone.map.mapId != 54 && pl.zone.map.mapId != 86
                && pl.zone.map.mapId != 55 && pl.zone.map.mapId != 87
                && pl.zone.map.mapId != 88 && pl.zone.map.mapId != 89 && pl.zone.map.mapId != 90 && pl.zone.map.mapId != 91
                && pl.zone.map.mapId != 114 && pl.zone.map.mapId != 115 && pl.zone.map.mapId != 116
                && pl.zone.map.mapId != 117 && pl.zone.map.mapId != 118 && pl.zone.map.mapId != 119
                && pl.zone.map.mapId != 120 && pl.zone.map.mapId != 47 && pl.zone.map.mapId != 48
                && pl.zone.map.mapId != 49 && pl.zone.map.mapId != 50 && pl.zone.map.mapId != 51
                && pl.zone.map.mapId != 52
                && pl.zone.map.mapId != 56) {
            pl.location.x = Util.nextInt(20, 1000);
            pl.zone.addPlayer(pl);
            pl.zone.load_Me_To_Another(pl);
            Client.gI().put(pl);

        }
        scheduler.scheduleAtFixedRate(() -> {
            try {
                Service.gI().chat(pl, "");
            } catch (Exception e) {

            }
        }, 0, 60000, TimeUnit.MILLISECONDS);

    }
    private void remove(MySession session) {
        if (session.player != null) {
            this.remove(session.player);
            session.player.dispose();
        }
        if (session.joinedGame) {
            session.joinedGame = false;
            try {
                GirlkunDB.executeUpdate("update account set last_time_logout = ? where id = ?", new Timestamp(System.currentTimeMillis()), session.userId);
            } catch (Exception e) {
                e.printStackTrace();
                Logger.logException(Manager.class, e, "Lỗi remove client");
            }
        }
        ServerManager.gI().disconnect(session);
    }

    private void remove(Player player) {
        this.players_id.remove(player.id);
        this.players_name.remove(player.name);
        this.players_userId.remove(player.getSession().userId);
        this.players.remove(player);
        if (!player.beforeDispose) {
            DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).removePlayerWait(player);  
            DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).removePlayer(player);  
            player.beforeDispose = true;
            player.mapIdBeforeLogout = player.zone.map.mapId;
            if(player.idNRNM != -1){
                ItemMap itemMap = new ItemMap(player.zone, player.idNRNM, 1, player.location.x, player.location.y, -1);
                Service.gI().dropItemMap(player.zone, itemMap);
                NgocRongNamecService.gI().pNrNamec[player.idNRNM - 353] = "";
                NgocRongNamecService.gI().idpNrNamec[player.idNRNM - 353] = -1;
                player.idNRNM = -1;
            }
            ChangeMapService.gI().exitMap(player);
            TransactionService.gI().cancelTrade(player);
            if (player.clan != null) {
                player.clan.removeMemberOnline(null, player);
            }
           if (player.itemTime != null && player.itemTime.isUseTDLT) {
               Item tdlt = null;
               try {
                   tdlt = InventoryServiceNew.gI().findItemBag(player, 521);
               } catch (Exception ex) {
               }
               if (tdlt != null) {
                   ItemTimeService.gI().turnOffTDLT(player, tdlt);
               }
           }
            if (SummonDragon.gI().playerSummonShenron != null
                    && SummonDragon.gI().playerSummonShenron.id == player.id) {
                SummonDragon.gI().isPlayerDisconnect = true;
            }
            if (player.mobMe != null) {
                player.mobMe.mobMeDie();
            }
            if (player.pet != null) {
                if (player.pet.mobMe != null) {
                    player.pet.mobMe.mobMeDie();
                }
                ChangeMapService.gI().exitMap(player.pet);
            }
        }
        PlayerDAO.updatePlayer(player);
    }

    public void kickSession(MySession session) {
        if (session != null) {
            this.remove(session);
            session.disconnect();
        }
    }
public void clear() {
        List<Player> z = players;
        for (Player pl : z) {
            if (pl != null) {
                if (pl.isBot) {
                    remove(pl);
                }
            }
        }
    }
    public Player getPlayer(long playerId) {
        return this.players_id.get(playerId);
    }

    public Player getPlayerByUser(int userId) {
        return this.players_userId.get(userId);
    }

    public Player getPlayer(String name) {
        return this.players_name.get(name);
    }

    public void close() {
        Logger.error("BEGIN KICK OUT SESSION.............................." + players.size() + "\n");
       while(!GirlkunSessionManager.gI().getSessions().isEmpty()){
           Logger.error("LEFT PLAYER: " + this.players.size() + ".........................\n");
           this.kickSession((MySession) GirlkunSessionManager.gI().getSessions().remove(0));
       }
        while (!players.isEmpty()) {
            this.kickSession((MySession) players.remove(0).getSession());
        }
        Logger.error("...........................................SUCCESSFUL\n");
    }

    public void cloneMySessionNotConnect() {
        Logger.error("BEGIN KICK OUT MySession Not Connect...............................\n");
        Logger.error("COUNT: " + GirlkunSessionManager.gI().getSessions().size());
        if (!GirlkunSessionManager.gI().getSessions().isEmpty()) {
            for (int j = 0; j < GirlkunSessionManager.gI().getSessions().size(); j++) {
                MySession m = (MySession) GirlkunSessionManager.gI().getSessions().get(j);
                if (m.player == null) {
                    this.kickSession((MySession) GirlkunSessionManager.gI().getSessions().remove(j));
                }
            }
        }
        Logger.error("..........................................................SUCCESSFUL\n");
    }

    private void update() {
        for (ISession s : GirlkunSessionManager.gI().getSessions()) {
            MySession session = (MySession) s;
            if (session.timeWait > 0) {
                session.timeWait--;
                if (session.timeWait == 0) {
                    kickSession(session);
                }
            }
        }
    }

    @Override
    public void run() {
        while (ServerManager.isRunning) {
            try {
                long st = System.currentTimeMillis();
                update();
                Thread.sleep(800 - (System.currentTimeMillis() - st));
            } catch (Exception e) {
            }
            
            ShopKyGuiManager.gI().save();
        }


    }

    public void show(Player player) {
        String txt = "";
        txt += "sessions: " + GirlkunSessionManager.gI().getSessions().size() + "\n";
        txt += "players_id: " + players_id.size() + "\n";
        txt += "players_userId: " + players_userId.size() + "\n";
        txt += "players_name: " + players_name.size() + "\n";
        txt += "players: " + players.size() + "\n";
        Service.gI().sendThongBao(player, txt);
    }
}
