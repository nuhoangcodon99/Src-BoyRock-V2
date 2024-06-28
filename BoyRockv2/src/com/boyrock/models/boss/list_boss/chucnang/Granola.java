package com.boyrock.models.boss.list_boss.chucnang;

import java.util.Calendar;
import java.util.Random;

import com.boyrock.models.boss.Boss;
import com.boyrock.models.boss.BossID;
import com.boyrock.models.boss.BossStatus;
import com.boyrock.models.boss.BossesData;
import com.boyrock.models.map.ItemMap;
import com.boyrock.models.player.Player;
import com.boyrock.services.EffectSkillService;
import com.boyrock.services.Service;
import com.boyrock.services.TaskService;
import com.boyrock.utils.Util;

public class Granola extends Boss {

    public Granola() throws Exception {
        super(BossID.GRANOLA, BossesData.GRANOLA);
    }

    @Override
    public void reward(Player plKill) {
       int[] itemDos = new int[]{1443};
int[] NRs = new int[]{20};
int randomDo = new Random().nextInt(itemDos.length);
int randomNR = new Random().nextInt(NRs.length);
if (Util.isTrue(20, 100)) { // Changed probability to 20% for itemDos
    Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, itemDos[randomDo], 1, this.location.x, this.location.y, plKill.id));
} else { // 80% chance for NRs
    Service.getInstance().dropItemMap(this.zone, new ItemMap(zone, NRs[randomNR], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
}

plKill.pointSukien += 3;
Service.gI().sendThongBao(plKill, "Bạn nhận được 3 điểm lấp lánh");
TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }
    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }

    private long st;
@Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        if (Util.canDoWithTime(st, 400000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }
    

    @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
       if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
             damage = 1; // Đặt sát thương là 1
        if (!piercing && effectSkill.isShielding) {
            if (damage > nPoint.hpMax) {
                EffectSkillService.gI().breakShield(this);
            }
            damage = 1; // Đặt sát thương là 1
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
    }



/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - Lucy
 */
