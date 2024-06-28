package com.boyrock.services.func;

import com.girlkun.database.GirlkunDB;
import com.boyrock.server.Manager;
import com.boyrock.utils.Logger;

import java.sql.Connection;


public class TopService implements Runnable{
    private static TopService i;

    public static TopService gI() {
        if (i == null) {
            i = new TopService();
        }
        return i;
    }
    
    @Override
    public void run() {
        while(true){
            try{
                if (Manager.timeRealTop + (30 * 60 * 1000) < System.currentTimeMillis()) {
                    Manager.timeRealTop = System.currentTimeMillis();
                    try (Connection con = GirlkunDB.getConnection()) {
                        Manager.topNV = Manager.realTop(Manager.queryTopNV, con);
                        Manager.topSM = Manager.realTop(Manager.queryTopSM, con);
                        Manager.topSK = Manager.realTop(Manager.queryTopSK, con);
                        Manager.topPVP = Manager.realTop(Manager.queryTopPVP, con);
                        Manager.topBoss = Manager.realTop(Manager.queryTopBoss, con);
                        Manager.topSD = Manager.realTop(Manager.queryTopSD, con);
                        Manager.topNroNamec = Manager.realTop(Manager.queryTopNroNamec, con);
                        Manager.topSuKien = Manager.realTop(Manager.queryTopSuKien, con);
                        Manager.topCauCa = Manager.realTop(Manager.queryTopCauCa, con);
                    } catch (Exception ignored) {
                        Logger.error("Lỗi đọc top");
                    }
                }
                Thread.sleep(1000);
            }catch (Exception ignored) {
            }
        }
    }

}
