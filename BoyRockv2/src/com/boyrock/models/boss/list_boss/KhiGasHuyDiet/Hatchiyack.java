package com.boyrock.models.boss.list_boss.KhiGasHuyDiet;

import com.boyrock.models.boss.bdkb.*;
import com.boyrock.models.boss.BossData;
import com.boyrock.models.boss.BossManager;
import com.boyrock.models.boss.BossID;
import com.boyrock.models.boss.Boss;
import com.boyrock.consts.ConstPlayer;
import com.boyrock.models.map.bando.*;
import com.boyrock.models.map.ItemMap;
import com.boyrock.models.map.Zone;
import com.boyrock.models.player.Player;
import com.boyrock.models.skill.Skill;
import com.boyrock.services.Service;
import com.boyrock.services.TaskService;
import com.boyrock.utils.Util;
import java.util.Random;

public class Hatchiyack extends Boss {
    private static final int[][] FULL_DEMON = new int[][]{{Skill.DEMON, 1}, {Skill.DEMON, 2}, {Skill.DEMON, 3}, {Skill.DEMON, 4}, {Skill.DEMON, 5}, {Skill.DEMON, 6}, {Skill.DEMON, 7}};

    public Hatchiyack(Zone zone , int level, int dame, int hp) throws Exception {
        super(Util.randomBossId(), new BossData(
                "Hatchiyack",
                ConstPlayer.TRAI_DAT,
                new short[]{639, 640, 641, -1, -1, -1},
                ((10000 + dame) * level),
                new int[]{((500000 + hp) * level)},
                new int[]{103},
                (int[][]) Util.addArray(FULL_DEMON),
                new String[]{},
                new String[]{"|-1|Nhóc con"},
                new String[]{},
                60
        ));
        this.zone = zone;
    }

    @Override
    public void reward(Player plKill) {
        
        int lv = plKill.clan.KhiGaHuyDiet.level;
        if (lv == 110) {
            
        } else if (lv <= 50) {  
        
        }
        if (Util.isTrue(100, 100)) {
            ItemMap it = new ItemMap(this.zone, 14, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.getInstance().dropItemMap(this.zone, it);
        }
        plKill.pointBoss += 0;
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }
    @Override
    public void active() {
        super.active();
    }

    @Override
    public void joinMap() {
        super.joinMap();
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        this.dispose();
    }
}