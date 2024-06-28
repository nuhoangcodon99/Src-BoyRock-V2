package com.boyrock.models.boss.list_boss.BLACK;

import java.util.Random;

import com.boyrock.models.boss.*;
import com.boyrock.models.item.Item;
import com.boyrock.models.map.ItemMap;
import com.boyrock.models.player.Player;
import com.boyrock.server.Manager;
import com.boyrock.services.EffectSkillService;
import com.boyrock.services.Service;
import com.boyrock.services.TaskService;
import com.boyrock.utils.Util;

public class Black extends Boss {

    public Black() throws Exception {
        super(BossID.BLACK, BossesData.BLACK_GOKU, BossesData.SUPER_BLACK_GOKU);
    }

    @Override
public void reward(Player plKill) {
    int[] itemDosIds = {555,555,555,557, 559, 556, 558, 560, 562, 564, 566, 563, 565, 567};
    int[] NRsIds = { 17};

    // Lấy một ID ngẫu nhiên từ mảng itemDosIds
    int randomItemDosId = itemDosIds[new Random().nextInt(itemDosIds.length)];

    // Lấy một ID ngẫu nhiên từ mảng NRsIds
    int randomNRId = NRsIds[new Random().nextInt(NRsIds.length)];

    // Tạo một ItemOption mới với tỷ lệ option như yêu cầu
   

    // 20% rơi đồ, 80% rơi ngọc rồng
    if (Util.isTrue(1, 5)) {
        // Thả một item ngẫu nhiên từ mảng itemDosIds với số lượng 1
        Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, randomItemDosId, 1, this.location.x, this.location.y, plKill.id));
    } else {
        // Thả một item từ mảng NRsIds với số lượng 1
        Service.getInstance().dropItemMap(this.zone, new ItemMap(zone, randomNRId, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
    }

    // Gọi hàm checkDoneTaskKillBoss
    TaskService.gI().checkDoneTaskKillBoss(plKill, this);
}

    @Override
    public void active() {
        super.active(); // To change body of generated methods, choose Tools | Templates.
        if (Util.canDoWithTime(st, 1800000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

   @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
 this.checkAnThan(plAtt);
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 100)) {
                this.chat("Xí hụt");
                return 0;
            }
           if(plAtt != null && plAtt.nPoint.isSieuThan){
                damage = this.nPoint.subDameInjureWithDeff(damage);
            }else{
                damage = this.nPoint.subDameInjureWithDeff(damage / 2);
            }
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage / 1;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }

    @Override
    public void joinMap() {
        super.joinMap(); // To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }

    private long st;

    // @Override
    // public void moveTo(int x, int y) {
    // if(this.currentLevel == 1){
    // return;
    // }
    // super.moveTo(x, y);
    // }
    //
    // @Override
    // public void reward(Player plKill) {
    // if(this.currentLevel == 1){
    // return;
    // }
    // super.reward(plKill);
    // }
    //
    // @Override
    // protected void notifyJoinMap() {
    // if(this.currentLevel == 1){
    // return;
    // }
    // super.notifyJoinMap();
    // }
}
