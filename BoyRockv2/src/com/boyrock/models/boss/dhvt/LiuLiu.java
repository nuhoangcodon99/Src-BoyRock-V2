package com.boyrock.models.boss.dhvt;

import com.boyrock.models.boss.BossData;
import com.boyrock.models.boss.BossID;
import com.boyrock.models.boss.BossesData;
import com.boyrock.models.player.Player;
/**
 * @author BTH sieu cap vippr0 
 */
public class LiuLiu extends BossDHVT {

    public LiuLiu(Player player) throws Exception {
        super(BossID.LIU_LIU, BossesData.LIU_LIU);
        this.playerAtt = player;
    }
}