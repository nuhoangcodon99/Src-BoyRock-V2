package com.boyrock.server;

import java.io.IOException;

import com.boyrock.consts.ConstNpc;
import com.boyrock.models.npc.Npc;
import com.boyrock.models.npc.NpcManager;
import com.boyrock.models.player.Player;
import com.boyrock.server.io.MySession;
import com.boyrock.services.Service;
import com.boyrock.services.func.TransactionService;

public class MenuController {

    private static MenuController instance;

    public static MenuController getInstance() {
        if (instance == null) {
            instance = new MenuController();
        }
        return instance;
    }

    public void openMenuNPC(MySession session, int idnpc, Player player) {
        TransactionService.gI().cancelTrade(player);
        Npc npc = null;
        if (idnpc == ConstNpc.CALICK && player.zone.map.mapId != 102) {
            npc = NpcManager.getNpc(ConstNpc.CALICK);
        } else {
            npc = player.zone.map.getNpc(player, idnpc);
        }
        if (npc != null) {
            npc.openBaseMenu(player);
        } else {
            Service.gI().hideWaitDialog(player);
        }
    }

    public void doSelectMenu(Player player, int npcId, int select) throws IOException {
        TransactionService.gI().cancelTrade(player);
        switch (npcId) {
            case ConstNpc.RONG_THIENG:
            case ConstNpc.CON_MEO:
                NpcManager.getNpc((byte) npcId).confirmMenu(player, select);
                break;
            default:
                Npc npc = null;
                if (npcId == ConstNpc.CALICK && player.zone.map.mapId != 102) {
                    npc = NpcManager.getNpc(ConstNpc.CALICK);
                } else {
                    npc = player.zone.map.getNpc(player, npcId);
                }
                if (npc != null) {
                    npc.confirmMenu(player, select);
                } else {
                    Service.gI().hideWaitDialog(player);
                }
                break;
        }

    }
}
