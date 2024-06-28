



















package com.boyrock.models.boss.iboss;

import com.boyrock.models.player.Player;


public interface IBossDie {
    
    void doSomeThing(Player playerKill);

    void notifyDie(Player playerKill);

    void rewards(Player playerKill);

    void leaveMap();

}






















