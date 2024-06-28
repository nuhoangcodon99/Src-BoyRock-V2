package com.boyrock.models.boss.dhvt;

import com.boyrock.models.boss.BossData;
import com.boyrock.models.boss.BossID;
import com.boyrock.models.boss.BossesData;
import com.boyrock.models.player.Player;

/**
 * @author BTH sieu cap vippr0 
 */
public class SoiHecQuyn extends BossDHVT {
    public SoiHecQuyn(Player player) throws Exception {
        super(BossID.SOI_HEC_QUYN, BossesData.SOI_HEC_QUYN);
        this.playerAtt = player;
    }
}
