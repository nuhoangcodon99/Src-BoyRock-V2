/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boyrock.models.boss.list_boss.chucnang;

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
import java.util.Random;

/**
 *
 * @author Administrator
 */
public class Tieudoitruong extends Boss {
    private long dropTime; // Biến đếm thời gian

    public Tieudoitruong() throws Exception {
        super(BossID.TDTNM, BossesData.TDTNM);
        this.dropTime = 0; // Khởi tạo thời gian drop ban đầu là 0
    }

    @Override
    public void reward(Player plKill) {
        int[] itemDosIds = {1387, 1388, 1389, 1390, 1391, 1392, 1393};

        // Rơi các item trong mảng itemDosIds với số lượng 1, cách nhau
        for (int i = 0; i < itemDosIds.length; i++) {
            int xPos = this.location.x + (i * 25); // Tăng vị trí x lên 25 đơn vị cho mỗi lần lặp
            int yPos = this.location.y;
            Service.getInstance().dropItemMap(this.zone, Util.nrnm(zone, itemDosIds[i], 1, xPos, yPos, plKill.id));
        }

        // Cập nhật thời gian drop
        this.dropTime = System.currentTimeMillis();

        // Gọi hàm checkDoneTaskKillBoss
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

    @Override
    public void active() {
        super.active();
        if (Util.canDoWithTime(st, 1800000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }

        // Kiểm tra xem đã đủ 5 phút chờ chưa
        if (System.currentTimeMillis() - this.dropTime >= 300000) { // 300000 milliseconds = 5 minutes
            // Người chơi có thể nhặt vật phẩm
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
