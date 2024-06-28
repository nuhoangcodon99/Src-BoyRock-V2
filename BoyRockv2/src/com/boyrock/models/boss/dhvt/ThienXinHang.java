package com.boyrock.models.boss.dhvt;

import com.boyrock.models.boss.BossID;
import com.boyrock.models.boss.BossesData;
import com.boyrock.models.player.Player;
import com.boyrock.server.Manager;
import com.boyrock.services.EffectSkillService;
import com.boyrock.utils.Logger;
import com.boyrock.utils.Util;

/**
 * @author BTH sieu cap vippr0
 */
public class ThienXinHang extends BossDHVT {

    private long lastTimePhanThan = System.currentTimeMillis();

    public ThienXinHang(Player player) throws Exception {
        super(BossID.THIEN_XIN_HANG, BossesData.THIEN_XIN_HANG);
        this.playerAtt = player;
        phanThan();
    }

    @Override
    public void attack() {
        super.attack();
        try {
            EffectSkillService.gI().removeStun(this);
            if (Util.canDoWithTime(lastTimePhanThan, 90000)) {
                lastTimePhanThan = System.currentTimeMillis();
                phanThan();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void phanThan() {
        try {
            new ThienXinHangClone(BossID.THIEN_XIN_HANG_CLONE, playerAtt);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(Manager.class, e, "Lỗi phân thân txh");
        }

    }
}