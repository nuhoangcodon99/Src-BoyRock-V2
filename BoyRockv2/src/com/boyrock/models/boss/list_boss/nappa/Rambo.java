package com.boyrock.models.boss.list_boss.nappa;

import java.util.Random;

import com.boyrock.models.boss.Boss;
import com.boyrock.models.boss.BossID;
import com.boyrock.models.boss.BossStatus;
import com.boyrock.models.boss.BossesData;
import com.boyrock.models.map.ItemMap;
import com.boyrock.models.player.Player;
import com.boyrock.models.skill.Skill;
import com.boyrock.services.PetService;
import com.boyrock.services.Service;
import com.boyrock.services.TaskService;
import com.boyrock.utils.Util;

/**
 *
 * @Stole By Lucy#0800
 */
public class Rambo extends Boss {

    public Rambo() throws Exception {
        super(BossID.RAMBO, BossesData.RAMBO);
    }

    @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
       if (Util.canDoWithTime(st, 600000)) {
           this.changeStatus(BossStatus.LEAVE_MAP);
       }
    }
    @Override
    public void reward(Player plKill) {
        plKill.pointBoss += 0;
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }
    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }
    private long st;
}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - Lucy
 */
