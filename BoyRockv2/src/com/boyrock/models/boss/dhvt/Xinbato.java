package com.boyrock.models.boss.dhvt;

import com.boyrock.models.boss.BossID;
import com.boyrock.models.boss.BossesData;
import com.boyrock.models.player.Player;

/**
 * @author BTH sieu cap vippr0 
 */
public class Xinbato extends BossDHVT {

    public Xinbato(Player player) throws Exception {
        super(BossID.XINBATO, BossesData.XINBATO);
        this.playerAtt = player;
    }
}