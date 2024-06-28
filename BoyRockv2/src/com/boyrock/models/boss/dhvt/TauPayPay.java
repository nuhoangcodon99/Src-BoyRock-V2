package com.boyrock.models.boss.dhvt;

import com.boyrock.models.boss.BossData;
import com.boyrock.models.boss.BossID;
import com.boyrock.models.boss.BossesData;
import com.boyrock.models.player.Player;

/**
 * @author BTH sieu cap vippr0 
 */
public class TauPayPay extends BossDHVT {

    public TauPayPay(Player player) throws Exception {
        super(BossID.TAU_PAY_PAY, BossesData.TAU_PAY_PAY);
        this.playerAtt = player;
    }
}