package com.boyrock.models.boss.list_boss;

import com.boyrock.models.boss.*;
import com.boyrock.models.map.ItemMap;
import com.boyrock.models.map.Zone;
import com.boyrock.models.player.Player;
import com.boyrock.services.Service;
import com.boyrock.utils.Util;

/**
 * @Stole By Lucy#0800
 */
public class NhanBan extends Boss {

    public NhanBan(int bossID, BossData bossData, Zone zone) throws Exception {
        super(bossID, bossData);
        this.zone = zone;
    }

    @Override
    public void reward(Player plKill) {
        //vật phẩm rơi khi diệt boss nhân bản
        ItemMap it = new ItemMap(this.zone, 861, Util.nextInt(30, 40), this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                this.location.y - 24), plKill.id);
        Service.gI().dropItemMap(this.zone, it);
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
