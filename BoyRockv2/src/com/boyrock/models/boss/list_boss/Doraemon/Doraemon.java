package com.boyrock.models.boss.list_boss.Doraemon;

import java.util.Random;

import com.boyrock.models.boss.Boss;
import com.boyrock.models.boss.BossID;
import com.boyrock.models.boss.BossStatus;
import com.boyrock.models.boss.BossesData;
import com.boyrock.models.map.ItemMap;
import com.boyrock.models.player.Player;
import com.boyrock.models.skill.Skill;
import com.boyrock.services.EffectSkillService;
import com.boyrock.services.Service;
import com.boyrock.services.TaskService;
import com.boyrock.utils.Util;


public class Doraemon extends Boss {

    public Doraemon() throws Exception {
        super(BossID.DORAEMON_TEAM,BossesData.DORAEMON,BossesData.NOBITA,BossesData.XUKA,BossesData.CHAIEN,BossesData.XEKO);
    }
    @Override
    public void reward(Player plKill) {
       int[] itemDosIds = {561,561};
    int[] NRsIds = { 17,225,1416,225,225, 457,2030, 1461};

    // Lấy một ID ngẫu nhiên từ mảng itemDosIds
    int randomItemDosId = itemDosIds[new Random().nextInt(itemDosIds.length)];

    // Lấy một ID ngẫu nhiên từ mảng NRsIds
    int randomNRId = NRsIds[new Random().nextInt(NRsIds.length)];

    // Tạo một ItemOption mới với tỷ lệ option như yêu cầu
   

    // 20% rơi đồ, 80% rơi ngọc rồng
    if (Util.isTrue(10, 100)) {
        // Thả một item ngẫu nhiên từ mảng itemDosIds với số lượng 1
        Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, randomItemDosId, 1, this.location.x, this.location.y, plKill.id));
    } else {
        // Thả một item từ mảng NRsIds với số lượng 1
        Service.getInstance().dropItemMap(this.zone, new ItemMap(zone, randomNRId, 2, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
    }

    // Gọi hàm checkDoneTaskKillBoss
    TaskService.gI().checkDoneTaskKillBoss(plKill, this);
}
   @Override
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
 this.checkAnThan(plAtt);
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 100)) {
                this.chat("Xí hụt");
                return 0;
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
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        if(Util.canDoWithTime(st,600000)){
          this.changeStatus(BossStatus.LEAVE_MAP);
        }
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
    