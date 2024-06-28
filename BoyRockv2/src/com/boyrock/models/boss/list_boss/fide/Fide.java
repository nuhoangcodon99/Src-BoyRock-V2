package com.boyrock.models.boss.list_boss.fide;

import com.boyrock.models.boss.Boss;
import com.boyrock.models.boss.BossID;
import com.boyrock.models.boss.BossStatus;
import com.boyrock.models.boss.BossesData;
import com.boyrock.models.map.ItemMap;
import com.boyrock.models.player.Player;
import com.boyrock.services.Service;
import com.boyrock.services.TaskService;
import com.boyrock.utils.Util;


public class Fide extends Boss {

    public Fide() throws Exception {
        super(BossID.FIDE, BossesData.FIDE_DAI_CA_1, BossesData.FIDE_DAI_CA_2, BossesData.FIDE_DAI_CA_3);
    }
    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(15, 100)) {
            ItemMap it = new ItemMap(this.zone, 861, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
        Service.gI().dropItemMap(this.zone, it);
        if (Util.isTrue(2, 10)) {
            Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 2000+plKill.gender, 1, this.location.x, this.location.y, plKill.id));
        }
        }
        plKill.pointBoss += 0;
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }
      @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
       if(Util.canDoWithTime(st,900000)){
           this.changeStatus(BossStatus.LEAVE_MAP);
       }
    }

    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st= System.currentTimeMillis();
    }
    private long st;

}






















