package com.boyrock.models.npc;

import com.boy.MaQuaTang.MaQuaTangManager;
import com.boyrock.consts.ConstMap;
import com.boyrock.consts.ConstNpc;
import com.boyrock.consts.ConstPlayer;
import com.boyrock.consts.ConstTask;
import com.boyrock.jdbc.daos.PlayerDAO;
import com.boyrock.models.boss.Boss;
import com.boyrock.models.boss.BossData;
import com.boyrock.models.boss.BossID;
import com.boyrock.models.boss.BossManager;
import com.boyrock.models.boss.BossesData;
import com.boyrock.models.boss.dhvt.PonPut;
import com.boyrock.models.boss.list_boss.NhanBan;

import com.boyrock.models.clan.Clan;
import com.boyrock.models.clan.ClanMember;
import com.boyrock.models.item.Item;
import com.boyrock.models.item.Item.ItemOption;
import com.boyrock.models.map.Map;
import com.boyrock.models.map.Zone;
import com.boyrock.models.map.KhiGasHuyDiet.KhiGasHuyDiet;
import com.boyrock.models.map.KhiGasHuyDiet.KhiGasHuyDietService;
import com.boyrock.models.map.MapMaBu.MapMaBu;
import com.boyrock.models.map.MapSatan.MapSatan;
import com.boyrock.models.map.MapVoDai.MapVodai;
import com.boyrock.models.map.bando.BanDoKhoBau;
import com.boyrock.models.map.bando.BanDoKhoBauService;
import com.boyrock.models.map.blackball.BlackBallWar;
import com.boyrock.models.map.challenge.MartialCongressService;

import com.boyrock.models.map.doanhtrai.DoanhTrai;
import com.boyrock.models.map.doanhtrai.DoanhTraiService;
import com.boyrock.models.map.mapMabu13h.MapMaBu13h;
////import com.boyrock.models.map.minuong.MiNuong;
////import com.boyrock.models.map.minuong.MiNuongService;
import com.boyrock.models.matches.PVPService;
import com.boyrock.models.matches.pvp.DaiHoiVoThuat;
import com.boyrock.models.matches.pvp.DaiHoiVoThuatService;
import com.boyrock.models.mob.Mob;
import com.boyrock.models.player.Inventory;
import com.boyrock.models.player.NPoint;
import com.boyrock.models.player.Player;
import com.boyrock.models.shop.ShopServiceNew;
import com.boyrock.models.skill.Skill;
import com.boyrock.server.Client;
import com.boyrock.server.Maintenance;
import com.boyrock.server.Manager;
import com.boyrock.services.*;
import com.boyrock.services.func.ChangeMapService;
import com.boyrock.services.func.ChonAiDay;
import com.boyrock.services.func.CombineServiceNew;
import com.boyrock.services.func.Input;
import com.boyrock.services.func.LuckyRound;
import com.boyrock.services.func.SummonDragon;
import com.boyrock.services.func.TopService;
import com.boyrock.utils.Logger;
import com.boyrock.utils.SkillUtil;
import com.boyrock.utils.TimeUtil;
import com.boyrock.utils.Util;
import com.girlkun.database.GirlkunDB;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.girlkun.network.io.Message;

import static com.boyrock.services.func.CombineServiceNew.CHE_TAO_TRANG_BI_TS;
import static com.boyrock.services.func.SummonDragon.SHENRON_1_STAR_WISHES_1;
import static com.boyrock.services.func.SummonDragon.SHENRON_1_STAR_WISHES_2;
import static com.boyrock.services.func.SummonDragon.SHENRON_SAY;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

import com.kygui.ItemKyGui;
import com.kygui.ShopKyGuiService;
import com.kygui.ShopKyGuiManager;
import java.io.IOException;
import java.util.logging.Level;

import java.util.logging.Level;

public class NpcFactory {

    private static final int COST_HD = 50000000;
private static int vodaiBallNumber = 0;
    private static boolean nhanVang = false;
    private static boolean nhanDeTu = false;

    // playerid - object
    public static final java.util.Map<Long, Object> PLAYERID_OBJECT = new HashMap<Long, Object>();

    private NpcFactory() {

    }

    public static Npc bardock(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Cậu có thể mang tới cho tôi 99 nho xanh hoặc nho tím, tôi sẽ tặng cậu những quả trứng mà tôi thu thập được!",
                                "Oke");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:// Shop
                                ShopServiceNew.gI().opendShop(player, "BARDOCK", true);
                                break;
                        }
                    }
                }
            }
        };

    }

//    public static Npc minuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {
//                    if (player.clan == null) {
//                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai", "Đóng");
//                        return;
//                    }
//                    if (player.clan.doanhTrai_haveGone) {
//                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                "Ta đã thả ngọc rồng ở tất cả các map,mau đi nhặt đi. Hẹn ngươi quay lại vào ngày mai",
//                                "OK");
//                        return;
//                    }
//
//                    boolean flag = true;
//                    for (Mob mob : player.zone.mobs) {
//                        if (!mob.isDie()) {
//                            flag = false;
//                        }
//                    }
//                    for (Player boss : player.zone.getBosses()) {
//                        if (!boss.isDie()) {
//                            flag = false;
//                        }
//                    }
//
//                    if (flag) {
//                        if (!player.clan.miNuong.timePickDragonBall) {
//                            player.clan.miNuong_haveGone = true;
//                            player.clan.miNuong.setLastTimeOpen(System.currentTimeMillis() + 290_000);
//                            player.clan.miNuong.DropNgocRong();
//                            for (Player pl : player.clan.membersInGame) {
//                                ItemTimeService.gI().sendTextTime(pl, (byte) 0, "Phó bản Mị nương sắp kết thúc : ",
//                                        300);
//                            }
//                            player.clan.miNuong.timePickDragonBall = true;
//                            createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                    "Ta đã thả bánh trưng ở tất cả các map,mau đi nhặt đi. Hẹn ngươi quay lại vào ngày mai",
//                                    "OK");
//                        } else {
//                            createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                    "Đi nhặt bánh trưng mau lên", "OK");
//                        }
//                    } else {
//                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                "Hãy tiêu diệt hết quái và boss trong map", "OK");
//                    }
//
//                }
//            }
//
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    switch (player.iDMark.getIndexMenu()) {
//                        case ConstNpc.MENU_JOIN_DOANH_TRAI:
//                            if (select == 0) {
//                                DoanhTraiService.gI().joinDoanhTrai(player);
//                            } else if (select == 2) {
//                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
//                            }
//                            break;
//                        case ConstNpc.IGNORE_MENU:
//                            if (select == 1) {
//                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
//                            }
//                            break;
//                    }
//                }
//            }
//        };
//
//    }

//    public static Npc vuaHung(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {
//                    if (player.clan == null) {
//                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai", "Đóng");
//                        return;
//                    }
//                    if (player.clan.getMembers().size() < MiNuong.N_PLAYER_CLAN) {
//                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                "Bang hội phải có ít nhất 5 thành viên mới có thể mở", "Đóng");
//                        return;
//                    }
//                    if (player.clan.miNuong != null) {
//                        createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
//                                "Bang hội của ngươi đang đánh phó bản mị nương\n"
//                                        + "Thời gian còn lại là "
//                                        + TimeUtil.getMinLeft(player.clan.miNuong.getLastTimeOpen(),
//                                                MiNuong.TIME_MI_NUONG / 1000)
//                                        + " phút . Ngươi có muốn tham gia không?",
//                                "Tham gia", "Không", "Hướng\ndẫn\nthêm");
//                        return;
//                    }
//                    int nPlSameClan = 0;
//                    for (Player pl : player.zone.getPlayers()) {
//                        if (!pl.equals(player) && pl.clan != null
//                                && pl.clan.equals(player.clan) && pl.location.x >= 35
//                                && pl.location.x <= 500) {
//                            nPlSameClan++;
//                        }
//                    }
//                    if (nPlSameClan < MiNuong.N_PLAYER_MAP) {
//                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                "Ngươi phải có ít nhất " + MiNuong.N_PLAYER_MAP
//                                        + " đồng đội cùng bang đứng gần mới có thể\nvào\n"
//                                        + "tuy nhiên ta khuyên ngươi nên đi cùng với 3-4 người để khỏi chết.\n"
//                                        + "Hahaha.",
//                                "OK", "Hướng\ndẫn\nthêm", "Cửa hàng");
//                        return;
//                    }
//                    if (player.clanMember.getNumDateFromJoinTimeToToday() < 1) {
//                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                "Phó bản Mị Nương chỉ cho phép những người ở trong bang trên 1 ngày. Hẹn ngươi quay lại vào lúc khác",
//                                "OK", "Hướng\ndẫn\nthêm", "Cửa hàng");
//                        return;
//                    }
//                    if (player.clan.haveGoneMiNuong) {
//                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                "Bang hội của ngươi đã đi phó bản lúc "
//                                        + TimeUtil.formatTime(player.clan.lastTimeOpenMiNuong, "HH:mm:ss")
//                                        + " hôm nay. Người mở\n"
//                                        + "(" + player.clan.playerOpenMiNuong + "). Hẹn ngươi quay lại vào ngày mai",
//                                "OK", "Hướng\ndẫn\nthêm", "Cửa hàng");
//                        return;
//                    }
//                    createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
//                            "Hôm nay bang hội của ngươi chưa vào phó bản lần nào. Ngươi có muốn vào\n"
//                                    + "không?\nĐể vào, ta khuyên ngươi nên có 3-4 người cùng bang đi cùng",
//                            "Vào\n(miễn phí)", "Không", "Hướng\ndẫn\nthêm", "Cửa hàng");
//                }
//            }
//
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    switch (player.iDMark.getIndexMenu()) {
//                        case ConstNpc.MENU_JOIN_DOANH_TRAI:
//                            if (select == 0) {
//                                MiNuongService.gI().joinMiNuong(player);
//                            } else if (select == 2) {
//                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
//                            } else if (select == 3) {
//                                ShopServiceNew.gI().opendShop(player, "HUNGVUONG", true);
//                            }
//                            break;
//                        case ConstNpc.IGNORE_MENU:
//                            if (select == 1) {
//                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
//                            }
//                            if (select == 2) {
//                                ShopServiceNew.gI().opendShop(player, "HUNGVUONG", true);
//                            }
//                            break;
//                    }
//                }
//            }
//        };
//
//    }

    public static Npc nezuko(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "|1|Chào anh, anh muốn chơi Gacha trước? Tắm trước? Ăn trước ? Hay là em ??\n" +
                                            "|2|Mối duyên tương ngộ: Cải trang, Pet, Cánh, Trứng linh thú,\n Công thức, đá may mắn, đồ kích hoạt ...  ! \n"
                                            +
                                            "|7|Mối duyên vương vấn: Hiện tại chưa mở! \n ",
                                    "Mối duyên tương ngộ", "Mối duyên vương vấn", "Shop");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                Item TuongNgo = null;

                                try {
                                    TuongNgo = InventoryServiceNew.gI().findItemBag(player, 342);

                                } catch (Exception e) {
                                    // throw new RuntimeException(e);
                                }
                                if (TuongNgo == null) {
                                    this.npcChat(player, "|7|Bạn không đủ mối duyên tương ngộ");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "|7|Hành trang của cưng không đủ chỗ trống");
                                } else {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, TuongNgo, 1);
                                    Service.gI().sendMoney(player);
                                    if (Util.isTrue(3, 100)) {
                                        int[] itemDos = new int[] { 463, 525, 578, 577, 591, 606, 711, 2045, 632, 609,
                                                623, 1091, 1093, 575, 724, 576, 458, 461, 455 };
                                        int randomDo = new Random().nextInt(itemDos.length);
                                        Item ct = ItemService.gI().createNewItem((short) itemDos[randomDo]);
                                        
                                        ct.itemOptions.add(new ItemOption(77, 10));
                                        ct.itemOptions.add(new ItemOption(103, 10));
                                        ct.itemOptions.add(new ItemOption(50, 20));
                                        ct.itemOptions.add(new ItemOption(93, 14));
                                        switch (ct.template.id) {
                                            case 463:
                                                ct.itemOptions.add(new ItemOption(116, 40));
                                                ct.itemOptions.add(new ItemOption(115, 40));
                                                break;
                                            case 525:
                                                ct.itemOptions.add(new ItemOption(3, 40));
                                                break;
                                            case 578:
                                                ct.itemOptions.add(new ItemOption(29, 0));
                                                break;
                                            case 577:
                                                ct.itemOptions.add(new ItemOption(26, 0));
                                                break;
                                            case 591:
                                            case 606:
                                                ct.itemOptions.add(new ItemOption(33, 0));
                                                ct.itemOptions.add(new ItemOption(156, 50));
                                                break;
                                            case 711:
                                                ct.itemOptions.add(new ItemOption(159, 4));
                                                break;
                                            case 2045:
                                                ct.itemOptions.add(new ItemOption(108, 30));
                                                break;
                                            case 632:
                                                ct.itemOptions.add(new ItemOption(167, 0));
                                                ct.itemOptions.add(new ItemOption(106, 0));
                                                break;
                                            case 609:
                                                ct.itemOptions.add(new ItemOption(97, 33));
                                                break;
                                            case 623:
                                                ct.itemOptions.add(new ItemOption(169, 0));
                                                break;
                                            case 1091:
                                                ct.itemOptions.add(new ItemOption(213, 0));
                                                break;
                                            case 1093:
                                                ct.itemOptions.add(new ItemOption(5, 10));
                                                break;
                                            case 575:
                                                ct.itemOptions.add(new ItemOption(24, 0));
                                                break;
                                            case 724:
                                                ct.itemOptions.add(new ItemOption(162, 10));
                                                break;
                                            case 576:
                                                ct.itemOptions.add(new ItemOption(25, 10));
                                                break;
                                            case 458:
                                                ct.itemOptions.add(new ItemOption(111, 10));
                                                break;
                                            case 461:
                                                ct.itemOptions.add(new ItemOption(231, 10));
                                                break;
                                            case 455:
                                                ct.itemOptions.add(new ItemOption(109, 10));
                                                break;

                                        }
                                        InventoryServiceNew.gI().addItemBag(player, ct);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "|7|Bạn nhận được cải trang");
                                    } else if (Util.isTrue(3, 100)) {
                                        int[] itemDos = new int[] { 1028, 1030, 1031, 800, 801, 803, 804, 805, 814, 822,
                                                823, 852, 865, 1129, 1130, 1151, 1152, 954, 955, 966, 467, 468, 469,
                                                470, 982, 471, 740, 741, 966, 996, 997, 998, 999, 1000, 745, 1001, 1007,
                                                1013, 1021, 1022, 1023 };
                                        int randomDo = new Random().nextInt(itemDos.length);
                                        Item ct = ItemService.gI().createNewItem((short) itemDos[randomDo]);
                                        ct.itemOptions.add(new ItemOption(50, 15));
                                        ct.itemOptions.add(new ItemOption(77, 10));
                                        ct.itemOptions.add(new ItemOption(103, 10));
                                        ct.itemOptions.add(new ItemOption(108, 10));
                                        ct.itemOptions.add(new ItemOption(93, 14));
                                        InventoryServiceNew.gI().addItemBag(player, ct);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "|8|Bạn nhận được cánh");
                                    } else if (Util.isTrue(3, 100)) {
                                        int[] itemDos = new int[] { 2095, 2090, 2089, 2088, 1046, 1191, 2087, 2086,
                                                2085, 942, 943, 944, 967, 1107, 1307, 1306 };
                                        int randomDo = new Random().nextInt(itemDos.length);
                                        Item ct = ItemService.gI().createNewItem((short) itemDos[randomDo]);
                                        ct.itemOptions.add(new ItemOption(5, 10));
                                      
                                        ct.itemOptions.add(new ItemOption(50, 15));
                                        ct.itemOptions.add(new ItemOption(77, 10));
                                        ct.itemOptions.add(new ItemOption(103, 10));
                                        ct.itemOptions.add(new ItemOption(93, 10));

                                        InventoryServiceNew.gI().addItemBag(player, ct);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "|2|Bạn nhận được pet");

                                    } else if (Util.isTrue(25, 100)) {
                                        Item hkh = ItemService.gI().createNewItem((short) (2000 + player.gender));
                                        InventoryServiceNew.gI().addItemBag(player, hkh);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "|4|Bạn nhận được 1 hòm tiếp tế");
                                    } else {
                                        int[] itemDos = new int[] { 663, 664, 665, 666, 667, 465, 466, 890, 891, 14, 15,
                                                16, 1066, 1067, 1068, 1069, 1070, 1076, 1077, 1078,
                                                1082,
                                                1083, 1084, 1085, 1086, 1100, 1101, 1102, 1103, 1082, 1083, 1084, 1085,
                                                1086, 1131, 2122, 925, 926, 927, 928, 929, 930, 931, 2091, 2092, 2093,
                                                807, 808, 809, 810, 811, 812, 813, 1183, 1184, 2030, 457, 344, 674 };
                                        int randomDo = new Random().nextInt(itemDos.length);
                                        Item ct = ItemService.gI().createNewItem((short) itemDos[randomDo]);
                                        InventoryServiceNew.gI().addItemBag(player, ct);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "|1|Bạn nhận được item");
                                    }

                                }
                            } else if (select == 1) {
                                Item VuongVan = null;

                                try {
                                    VuongVan = InventoryServiceNew.gI().findItemBag(player, 343);

                                } catch (Exception e) {
                                    // throw new RuntimeException(e);
                                }
                                if (VuongVan == null) {
                                    this.npcChat(player, "Bạn không đủ mối duyên vương vấn");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành trang của cưng không đủ chỗ trống");
                                } else {
                                    this.npcChat(player, "|2|Em chưa mở anh ơi");
                                    // InventoryServiceNew.gI().subQuantityItemsBag(player, VuongVan, 1);
                                    // Service.gI().sendMoney(player);

                                }
                            }

                            else if (select == 2) {
                                if (player.nPoint.power >= 2000000000) {

                                    ShopServiceNew.gI().opendShop(player, "POC", false);
                                } else {

                                    Service.gI().sendThongBaoOK(player,
                                            "Anh zai yếu sinh lý quá, đủ 2tỉ sức mạnh quay lại gặp em nhé");

                                }
                            }

                        }

                    }

                }
            }
        };
    }

    public static Npc noibanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Chế tạo bánh trung thu", "Bánh trung thu 1 trứng", "Bánh trung thu 2 trứng",
                                    "Bánh trung thu gà quay", "Bánh trung thu thập cẩm(cái này ăn như cặc)");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                Item Nuoc = null;
                                Item Mi = null;
                                Item Thit = null;
                                Item Duong = null;
                                Item Trung1 = null;
                                try {
                                    Nuoc = InventoryServiceNew.gI().findItemBag(player, 2126);
                                    Mi = InventoryServiceNew.gI().findItemBag(player, 2128);
                                    Thit = InventoryServiceNew.gI().findItemBag(player, 2127);
                                    Duong = InventoryServiceNew.gI().findItemBag(player, 2129);
                                    Trung1 = InventoryServiceNew.gI().findItemBag(player, 2130);
                                } catch (Exception e) {
                                    // throw new RuntimeException(e);
                                }
                                if (Nuoc == null || Mi == null || Thit == null || Duong == null || Trung1 == null
                                        || Nuoc.quantity < 20 || Mi.quantity < 20 || Thit.quantity < 20
                                        || Duong.quantity < 20 || Trung1.quantity < 20) {
                                    this.npcChat(player, "Bạn không đủ nguyên liệu");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành trang của cưng không đủ chỗ trống");
                                } else {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, Nuoc, 20);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, Mi, 20);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, Thit, 20);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, Duong, 20);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, Trung1, 20);
                                    Service.gI().sendMoney(player);

                                    Item ct = ItemService.gI().createNewItem((short) 465);
                                    InventoryServiceNew.gI().addItemBag(player, ct);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn nhận được bánh trung thu 1 trứng");
                                }

                            } else if (select == 1) {
                                Item Nuoc = null;
                                Item Mi = null;
                                Item Thit = null;
                                Item Duong = null;
                                Item Trung2 = null;
                                try {
                                    Nuoc = InventoryServiceNew.gI().findItemBag(player, 2126);
                                    Mi = InventoryServiceNew.gI().findItemBag(player, 2128);
                                    Thit = InventoryServiceNew.gI().findItemBag(player, 2127);
                                    Duong = InventoryServiceNew.gI().findItemBag(player, 2129);
                                    Trung2 = InventoryServiceNew.gI().findItemBag(player, 2131);
                                } catch (Exception e) {
                                    // throw new RuntimeException(e);
                                }
                                if (Nuoc == null || Mi == null || Thit == null || Duong == null || Trung2 == null
                                        || Nuoc.quantity < 20 || Mi.quantity < 20 || Thit.quantity < 20
                                        || Duong.quantity < 20 || Trung2.quantity < 20) {
                                    this.npcChat(player, "Bạn không đủ nguyên liệu");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành trang của cưng không đủ chỗ trống");
                                } else {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, Nuoc, 20);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, Mi, 20);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, Thit, 20);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, Duong, 20);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, Trung2, 20);

                                    Service.gI().sendMoney(player);
                                    Item ct = ItemService.gI().createNewItem((short) 466);
                                    InventoryServiceNew.gI().addItemBag(player, ct);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn nhận được bánh trung thu 2 trứng");
                                }

                            } else if (select == 2) {
                                Item Nuoc = null;
                                Item Mi = null;
                                Item Thit = null;
                                Item Duong = null;
                                Item GaQuay = null;
                                try {
                                    Nuoc = InventoryServiceNew.gI().findItemBag(player, 2126);
                                    Mi = InventoryServiceNew.gI().findItemBag(player, 2128);
                                    Thit = InventoryServiceNew.gI().findItemBag(player, 2127);
                                    Duong = InventoryServiceNew.gI().findItemBag(player, 2129);
                                    GaQuay = InventoryServiceNew.gI().findItemBag(player, 2132);
                                } catch (Exception e) {
                                    // throw new RuntimeException(e);
                                }
                                if (Nuoc == null || Mi == null || Thit == null || Duong == null || GaQuay == null
                                        || Nuoc.quantity < 20 || Mi.quantity < 20 || Thit.quantity < 20
                                        || Duong.quantity < 20 || GaQuay.quantity < 20) {
                                    this.npcChat(player, "Bạn không đủ nguyên liệu");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành trang của cưng không đủ chỗ trống");
                                } else {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, Nuoc, 20);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, Mi, 20);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, Thit, 20);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, Duong, 20);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, GaQuay, 20);
                                    Service.gI().sendMoney(player);
                                    Item ct = ItemService.gI().createNewItem((short) 890);
                                    InventoryServiceNew.gI().addItemBag(player, ct);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn nhận được bánh trung thu gà quay");
                                }

                            } else if (select == 3) {
                                Item Nuoc = null;
                                Item Mi = null;
                                Item Thit = null;
                                Item Duong = null;
                                Item Mut = null;

                                try {
                                    Nuoc = InventoryServiceNew.gI().findItemBag(player, 2126);
                                    Mi = InventoryServiceNew.gI().findItemBag(player, 2128);
                                    Thit = InventoryServiceNew.gI().findItemBag(player, 2127);
                                    Duong = InventoryServiceNew.gI().findItemBag(player, 2129);
                                    Mut = InventoryServiceNew.gI().findItemBag(player, 2133);
                                } catch (Exception e) {
                                    // throw new RuntimeException(e);
                                }
                                if (Nuoc == null || Mi == null || Thit == null || Duong == null || Mut == null
                                        || Nuoc.quantity < 20 || Mi.quantity < 20 || Thit.quantity < 20
                                        || Duong.quantity < 20 || Mut.quantity < 20) {
                                    this.npcChat(player, "Bạn không đủ nguyên liệu");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành trang của cưng không đủ chỗ trống");
                                } else {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, Nuoc, 20);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, Mi, 20);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, Thit, 20);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, Duong, 20);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, Mut, 20);
                                    Service.gI().sendMoney(player);
                                    Item ct = ItemService.gI().createNewItem((short) 891);
                                    InventoryServiceNew.gI().addItemBag(player, ct);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn nhận được bánh trung thu thập cẩm");
                                }

                            }

                        }

                    }

                }
            }
        };
    }
 public static Npc gapThu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 5) {
                    this.createOtherMenu(player, 1234, "|0|- •⊹٭Ngọc Rồng Heaven⊹• -\n" + "|2|MÁY GẮP TRỨNG\n" + "|1|GẮP X1 : 500tr Vàng\nGẮP X10 : 5 Tỷ Vàng\nGẮP X100 : 50 Tỷ Vàng\n" + "|7|NẾU MUỐN NGƯNG AUTO GẤP CHỈ CẦN THOÁT GAME VÀ VÀO LẠI!"
                            +"\nTrứng sẽ có 3 cấp độ, cấp độ càng cao chỉ số càng vip"
                            +"\nSẽ có tỷ lệ ra linh thú vĩnh viễn",
                            "Gắp X1", "Gắp X10", "Gắp X100", "Rương Đồ");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.getIndexMenu() == 1234) {
                            switch (select) {
                                case 0:
                                    if (player.inventory.gold < 500000000) {
                                        Service.gI().sendThongBao(player, "không đủ 500tr vàng");
                                        return;
                                    }
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        Service.gI().sendThongBao(player, "Hết chỗ trống rồi");
                                        return;
                                    }
                                    player.inventory.gold -= 500000000;
                                    Service.gI().sendMoney(player);
                                    Item gapt = Util.petrandom(Util.nextInt(1239, 1241));
                                    if (Util.isTrue(40, 100)) {
                                        player.inventory.itemsBoxCrackBall.add(gapt);
                                        this.createOtherMenu(player, 1234, "|2|Bạn vừa gắp được : " + gapt.template.name + "\nSố vàng Trừ : 500000000" + "\n|7|Chiến tiếp ngay!",
                                                "Gắp X1", "Gắp X10", "Gắp X100", "Rương Đồ");
                                    } else {
                                        this.createOtherMenu(player, 1234, "|6|Gắp hụt rồi, bạn bỏ cuộc sao?" + "\nSố Vàng Trừ : 500000000" + "\n|7|Chiến tiếp ngay!",
                                                "Gắp X1", "Gắp X10", "Gắp X100", "Rương Đồ");
                                    }
                                    break;
                                case 1:
                                    if (player.inventory.gold < 5000000000L) {
                                        Service.gI().sendThongBao(player, "không đủ 5 tỷ vàng");
                                        return;
                                    }
                                    try {
                                        Service.gI().sendThongBao(player, "Tiến hành auto gắp x10 lần");
                                        int timex10 = 10;
                                        int hn = 0;
                                        while (timex10 > 0) {
                                            timex10--;
                                            hn += 500000000;
                                            Thread.sleep(100);
                                            if (1 + player.inventory.itemsBoxCrackBall.size() > 100) {
                                                this.createOtherMenu(player, 12345, "|7|DỪNG AUTO GẮP, RƯƠNG PHỤ ĐÃ ĐẦY!\n" + "|2|TỔNG LƯỢT GẮP : " + (10 - timex10) + " LƯỢT" + "\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
                                                        "Đóng");
                                                break;
                                            }
                                            player.inventory.gold -= 500000000;
                                            Service.gI().sendMoney(player);
                                            Item gapx10 = Util.petrandom(Util.nextInt(1239, 1241));
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                                if (Util.isTrue(40, 100)) {
//                                    InventoryServiceNew.gI().addItemBag(player, gapx10);
//                                    InventoryServiceNew.gI().sendItemBags(player);
                                                    player.inventory.itemsBoxCrackBall.add(gapx10);
                                                    this.createOtherMenu(player, 12345, "|7|ĐANG TIẾN HÀNH GẮP AUTO X10\nSỐ LƯỢT CÒN : " + timex10 + " LƯỢT\n" + "|2|Đã gắp được : " + gapx10.template.name + "\nSố vàng đã trừ : " + hn + "\n" + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                                            "Đóng");
                                                } else {
                                                    this.createOtherMenu(player, 12345, "|7|ĐANG TIẾN HÀNH GẮP AUTO X10\nSỐ LƯỢT CÒN : " + timex10 + " LƯỢT\n" + "|2|Gắp hụt rồi!" + "\nSố vàng đã trừ : " + hn + "\n" + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                                            "Đóng");
                                                }
                                            }
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                                if (Util.isTrue(10, 100)) {
                                                    player.inventory.itemsBoxCrackBall.add(gapx10);
                                                    this.createOtherMenu(player, 12345, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : " + timex10 + " LƯỢT\n" + "|2|Đã gắp được : " + gapx10.template.name + "\nSố vàng đã trừ : " + hn + "\n",
                                                            "Đóng");
                                                }
//                                    else {
//                                    this.createOtherMenu(player, 12345, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Gắp hụt rồi!"+"\nSố hồng ngọc đã trừ : "+hn+"\n : ",
//                                    "Đóng");
//                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                    break;
                                case 2:
                                    if (player.inventory.gold < 50000000000L) {
                                        Service.gI().sendThongBao(player, "không đủ 50 tỷ vàng ");
                                        return;
                                    }
                                    try {
                                        Service.gI().sendThongBao(player, "Tiến hành auto gắp x100 lần");
                                        int timex100 = 100;
                                        int hn = 0;
                                        while (timex100 > 0) {
                                            timex100--;
                                            hn += 500000000;
                                            Thread.sleep(100);
                                            if (1 + player.inventory.itemsBoxCrackBall.size() > 100) {
                                                this.createOtherMenu(player, 12345, "|7|DỪNG AUTO GẮP, RƯƠNG PHỤ ĐÃ ĐẦY!\n" + "|2|TỔNG LƯỢT GẮP : " + (10 - timex100) + " LƯỢT" + "\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
                                                        "Đóng");
                                                break;
                                            }
                                            player.inventory.gold -= 500000000;
                                            Service.gI().sendMoney(player);
                                            Item gapx100 = Util.petrandom(Util.nextInt(1239, 1241));
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                                if (Util.isTrue(10, 100)) {
//                                    InventoryServiceNew.gI().addItemBag(player, gapx100);
//                                    InventoryServiceNew.gI().sendItemBags(player);
                                                    player.inventory.itemsBoxCrackBall.add(gapx100);
                                                    this.createOtherMenu(player, 12345, "|7|ĐANG TIẾN HÀNH GẮP AUTO X100\nSỐ LƯỢT CÒN : " + timex100 + " LƯỢT\n" + "|2|Đã gắp được : " + gapx100.template.name + "\nSố vàng đã trừ : " + hn + "\n" + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                                            "Đóng");
                                                }
//                                    else {
//                                    this.createOtherMenu(player, 12345, "|7|ĐANG TIẾN HÀNH GẮP AUTO X100\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Gắp hụt rồi!"+"\nSố hồng ngọc đã trừ : "+hn+"\n"+"\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
//                                    "Đóng");
//                                    }
                                            }
                                            if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                                if (Util.isTrue(10, 100)) {
                                                    player.inventory.itemsBoxCrackBall.add(gapx100);
                                                    this.createOtherMenu(player, 12345, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X100 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : " + timex100 + " LƯỢT\n" + "|2|Đã gắp được : " + gapx100.template.name + "\nSố vàng đã trừ : " + hn + "\n",
                                                            "Đóng");
                                                } else {
                                                    this.createOtherMenu(player, 12345, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X100 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : " + timex100 + " LƯỢT\n" + "|2|Gắp hụt rồi!" + "\nSố vàng đã trừ : " + hn + "\n",
                                                            "Đóng");
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                    break;
                                case 3:
                                    this.createOtherMenu(player, ConstNpc.RUONG_PHU,
                                            "|1|Tình yêu như một dây đàn\n"
                                            + "Tình vừa được thì đàn đứt dây\n"
                                            + "Đứt dây này anh thay dây khác\n"
                                            + "Mất em rồi anh biết thay ai?",
                                            "Rương Phụ\n(" + (player.inventory.itemsBoxCrackBall.size()
                                            - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
                                            + " món)",
                                            "Xóa Hết\nRương Phụ", "Đóng");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.RUONG_PHU) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "ITEMS_LUCKY_ROUND", true);
                                    break;
                                case 1:
                                    NpcService.gI().createMenuConMeo(player,
                                            ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND, this.avartar,
                                            "|3|Bạn chắc muốn xóa hết vật phẩm trong rương phụ?\n"
                                            + "|7|Sau khi xóa sẽ không thể khôi phục!",
                                            "Đồng ý", "Hủy bỏ");
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }
    public static Npc poc(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Cưng muốn tặng bánh hả? Thêm 5 thỏi vàng nhé! Chụy đây hông dễ dãi !",
                                    "Tặng bánh trung thu 1 trứng", "Tặng bánh trung thu 2 trứng",
                                    "Làm hộp bánh trung thu đặc biệt", "Đổi cải trang thỏ đại ca");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                Item Banh1Trung = null;
                                Item ThoiVang = null;
                                try {
                                    Banh1Trung = InventoryServiceNew.gI().findItemBag(player, 465);
                                    ThoiVang = InventoryServiceNew.gI().findItemBag(player, 457);
                                } catch (Exception e) {
                                    // throw new RuntimeException(e);
                                }
                                if (Banh1Trung == null || Banh1Trung.quantity < 5) {
                                    this.npcChat(player, "Cưng có đủ 5 bánh 1 trứng");
                                } else if (ThoiVang == null || ThoiVang.quantity < 5) {
                                    this.npcChat(player, "Cưng không đủ 5 thỏi vàng");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành trang của cưng không đủ chỗ trống");
                                } else {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, Banh1Trung, 5);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, ThoiVang, 5);
                                    Service.gI().sendMoney(player);

                                    short[] itemDos = new short[] { 1041, 1042, 1043, 584 };
                                    int randomDo = new Random().nextInt(itemDos.length);
                                    Item ct = ItemService.gI().createNewItem(itemDos[randomDo]);
                                    ct.itemOptions.add(new ItemOption(147, Util.nextInt(15, 25)));
                                    ct.itemOptions.add(new ItemOption(77, Util.nextInt(15, 25)));
                                    ct.itemOptions.add(new ItemOption(103, Util.nextInt(15, 25)));
                                    ct.itemOptions.add(new ItemOption(93, Util.nextInt(7, 10)));
                                    ct.itemOptions.add(new ItemOption(88, Util.nextInt(30, 50)));
                                    ct.itemOptions.add(new ItemOption(117, Util.nextInt(10, 20)));
                                    InventoryServiceNew.gI().addItemBag(player, ct);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn nhận được cải trang");
                                }
                            }
                            if (select == 1) {
                                Item Banh2Trung = null;
                                Item ThoiVang = null;
                                try {
                                    Banh2Trung = InventoryServiceNew.gI().findItemBag(player, 466);
                                    ThoiVang = InventoryServiceNew.gI().findItemBag(player, 457);
                                } catch (Exception e) {
                                    // throw new RuntimeException(e);
                                }
                                if (Banh2Trung == null || Banh2Trung.quantity < 5) {
                                    this.npcChat(player, "Cưng không có đủ 5 bánh 2 trứng");
                                } else if (ThoiVang == null || ThoiVang.quantity < 5) {
                                    this.npcChat(player, "Cưng không đủ 5 thỏi vàng");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành trang của cưng không đủ chỗ trống");
                                } else {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, Banh2Trung, 5);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, ThoiVang, 5);
                                    Service.gI().sendMoney(player);
                                    short[] itemDos = new short[] { 467, 468, 469, 470, 471 };
                                    int randomDo = new Random().nextInt(itemDos.length);
                                    Item ct = ItemService.gI().createNewItem(itemDos[randomDo]);
                                    ct.itemOptions.add(new ItemOption(147, Util.nextInt(15, 30)));
                                    ct.itemOptions.add(new ItemOption(77, Util.nextInt(15, 25)));
                                    ct.itemOptions.add(new ItemOption(103, Util.nextInt(15, 25)));
                                    ct.itemOptions.add(new ItemOption(93, Util.nextInt(7, 10)));
                                    ct.itemOptions.add(new ItemOption(88, Util.nextInt(10, 30)));

                                    InventoryServiceNew.gI().addItemBag(player, ct);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn nhận được lồng đèn");
                                }
                            }
                            if (select == 2) {
                                Item Banh1Trung = null;
                                Item Banh2Trung = null;
                                Item BanhGaQuay = null;
                                Item BanhThapCam = null;
                                Item Banhdacbiet = null;
                                Item ThoiVang = null;
                                try {
                                    Banh1Trung = InventoryServiceNew.gI().findItemBag(player, 465);
                                    Banh2Trung = InventoryServiceNew.gI().findItemBag(player, 466);
                                    BanhGaQuay = InventoryServiceNew.gI().findItemBag(player, 890);
                                    BanhThapCam = InventoryServiceNew.gI().findItemBag(player, 891);

                                    Banhdacbiet = InventoryServiceNew.gI().findItemBag(player, 472);
                                    ThoiVang = InventoryServiceNew.gI().findItemBag(player, 457);
                                } catch (Exception e) {
                                    // throw new RuntimeException(e);
                                }
                                if (Banh1Trung == null || Banh1Trung.quantity < 20) {
                                    this.npcChat(player, "Cưng không có đủ 20 bánh trung thu 1 trứng");
                                } else if (Banh2Trung == null || Banh2Trung.quantity < 20) {
                                    this.npcChat(player, "Cưng không có đủ 20 bánh trung thu 2 trứng");
                                } else if (Banhdacbiet == null || Banhdacbiet.quantity < 20) {
                                    this.npcChat(player, "Cưng không đủ 20 có bánh trung thu đặc biệt");
                                } else if (BanhGaQuay == null || BanhGaQuay.quantity < 20) {
                                    this.npcChat(player, "Cưng không đủ 20 có bánh trung thu gà quay");
                                } else if (BanhThapCam == null || BanhThapCam.quantity < 20) {
                                    this.npcChat(player, "Cưng không đủ 20 có bánh trung thu thập cẩm");
                                } else if (ThoiVang == null || ThoiVang.quantity < 10) {
                                    this.npcChat(player, "Cưng không đủ 10 thỏi vàng");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành trang của cưng không đủ chỗ trống");
                                } else {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, Banh1Trung, 20);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, Banh2Trung, 20);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, BanhGaQuay, 20);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, BanhThapCam, 20);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, Banhdacbiet, 20);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, ThoiVang, 10);
                                    Service.gI().sendMoney(player);
                                    Item ct = ItemService.gI().createNewItem((short) 473);
                                    InventoryServiceNew.gI().addItemBag(player, ct);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn nhận được hộp bánh đặc biệt");
                                }
                            }
                            if (select == 3) {
                                Item Carot = null;
                                Item ThoiVang = null;
                                try {
                                    Carot = InventoryServiceNew.gI().findItemBag(player, 462);

                                    ThoiVang = InventoryServiceNew.gI().findItemBag(player, 457);
                                } catch (Exception e) {
                                    // throw new RuntimeException(e);
                                }
                                if (Carot == null || Carot.quantity < 99) {
                                    this.npcChat(player, "Cưng không có đủ 99 cà rốt");
                                } else if (ThoiVang == null || ThoiVang.quantity < 5) {
                                    this.npcChat(player, "Cưng không đủ 5 thỏi vàng");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành trang của cưng không đủ chỗ trống");
                                } else {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, Carot, 99);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, ThoiVang, 5);
                                    Service.gI().sendMoney(player);
                                    Item ct = ItemService.gI().createNewItem((short) 463);
                                    ct.itemOptions.add(new ItemOption(147, Util.nextInt(15, 25)));
                                    ct.itemOptions.add(new ItemOption(77, Util.nextInt(10, 20)));
                                    ct.itemOptions.add(new ItemOption(103, Util.nextInt(10, 20)));
                                    ct.itemOptions.add(new ItemOption(93, Util.nextInt(4, 7)));
                                    ct.itemOptions.add(new ItemOption(88, Util.nextInt(30, 50)));
                                    ct.itemOptions.add(new ItemOption(116, 0));
                                    ct.itemOptions.add(new ItemOption(115, 0));
                                    InventoryServiceNew.gI().addItemBag(player, ct);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn nhận được cải trang Thỏ đại ca");
                                }
                            }

                        }

                    }

                }
            }
        };
    }

    private static Npc trongTai(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
public void openBaseMenu(Player player) {
    try {
        MapVodai.gI().setTimeJoinMapVodai();
        if (this.mapId == 13) {
            long now = System.currentTimeMillis();
            if (now > MapVodai.TIME_OPEN_VODAI && now < MapVodai.TIME_CLOSE_VODAI) {
                int numRegistered = MapVodai.gI().getNumRegistered();
                this.createOtherMenu(player, ConstNpc.MENU_OPEN_VODAI, 
                    "Đại chiến ngọc rồng namec đã mở, " + 
                    "ngươi có muốn tham gia không?\n\n" +
                    "|7|Hiện tại đã có " + numRegistered + " người đăng ký tham gia.",
                    "Hướng dẫn\nthêm", "Đăng ký", "Tham gia", "Từ chối");
            } else {
                this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_VODAI,
                    "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
            }
        }  else if (this.mapId == 166) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ta có thể giúp gì cho ngươi?", "Nhận thưởng", "Quay về");
                    }
                } catch (Exception ex) {
                    Logger.error("Lỗi mở menu trọng tài");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 13) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.MENU_REWARD_VODAI:
                                break;
                            case ConstNpc.MENU_OPEN_VODAI:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_VODAI);
                                }   if (select == 1) {
                                   MapVodai.gI().registerPlayer(player);
                               }
                            else  if (select == 2) {
                                
                            
                                   MapVodai.gI().joinBattle(player);
                            }
                            
                            
                                break;
                          
                           
                            case ConstNpc.MENU_NOT_OPEN_BDW:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_VODAI);
                                }
                                break;
                        }
                    } 
                   else if (this.mapId == 166) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                            if (select == 1) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            } else {
                                this.npcChat(player, "Hãy chiến đấu vì ");
                            }
                        }
                    }
                }

            };
        };
    }

   private static Npc trungLinhThu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
    return new Npc(mapId, status, cx, cy, tempId, avartar) {
        @Override
        public void openBaseMenu(Player player) {
            if (canOpenNpc(player)) {
                if (this.mapId == 219) {
                    Item longLinhThu = null;
                    Item honLinhThu = null;
                    Item thangTinhThach = null;
                    try {
                        longLinhThu = InventoryServiceNew.gI().findItemBag(player, 1462);
                        honLinhThu = InventoryServiceNew.gI().findItemBag(player, 2029);
                        thangTinhThach = InventoryServiceNew.gI().findItemBag(player, 2031);
                    } catch (Exception e) {
                        // throw new RuntimeException(e);
                    }
                    int longLinhThuQuantity = longLinhThu != null ? longLinhThu.quantity : 0;
                    int honLinhThuQuantity = honLinhThu != null ? honLinhThu.quantity : 0;
                    int thangTinhThachQuantity = thangTinhThach != null ? thangTinhThach.quantity : 0;
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|2|Bạn đang có: " + honLinhThuQuantity + "/5 Hồn Linh thú" +
                            "\nBạn đang có: " + longLinhThuQuantity + "/5 Lông Linh thú" +
                           "\nBạn đang có: " + thangTinhThachQuantity + "/5 Thăng tinh thạch" 
                            
                         , "Ấp trứng linh thú",
                            "Từ chối");
                }
            }
        }

        @Override
        public void confirmMenu(Player player, int select) {
            if (canOpenNpc(player)) {
                if (this.mapId == 219) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                          
                              case 0: {
    Item honLinhThu = InventoryServiceNew.gI().findItemBag(player, 2029);
    Item longLinhThu = InventoryServiceNew.gI().findItemBag(player, 1462);
    Item thangTinhThach = InventoryServiceNew.gI().findItemBag(player, 2031);

    if (honLinhThu == null || honLinhThu.quantity < 5) {
        this.npcChat(player, "Bạn không đủ 5 Hồn Linh thú");
    } else if (longLinhThu == null || longLinhThu.quantity < 5) {
        this.npcChat(player, "Bạn không đủ 5 Lông Linh thú");
    } else if (thangTinhThach == null || thangTinhThach.quantity < 5) {
        this.npcChat(player, "Bạn không đủ 1 Thăng Tinh Thạch");
    } else if (player.inventory.gold < 1_000_000_000) {
        this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
    } else {
        // Thực hiện các hoạt động khác
    

                                    player.inventory.gold -= 1_000_000_000;
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, honLinhThu, 5);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, longLinhThu, 5);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, thangTinhThach, 5);
                                    Service.gI().sendMoney(player);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    int randomNumber = new Random().nextInt(100);
if (randomNumber < 60) {
    Item trungLinhThu = ItemService.gI().createNewItem((short) 1239);
    InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
    InventoryServiceNew.gI().sendItemBags(player);
    this.npcChat(player, "Bạn nhận được 1 Trứng Linh thú sơ cấp");
} else if (randomNumber < 90) {
    Item trungLinhThu = ItemService.gI().createNewItem((short) 1240);
    InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
    InventoryServiceNew.gI().sendItemBags(player);
    this.npcChat(player, "Bạn nhận được 1 Trứng Linh thú trung cấp");
} else {
    Item trungLinhThu = ItemService.gI().createNewItem((short) 1241);
    InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
    InventoryServiceNew.gI().sendItemBags(player);
    this.npcChat(player, "Bạn nhận được 1 Trứng Linh thú cao cấp");
}
                                }
                                break;
                              
                                   }
                            
                        }
                    }
                }
            }
        }
    };
}

    private static Npc kyGui(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, 0,
                            "Tại đây mua bán trao đổi ký gửi",
                            "Hướng dẫn","Cửa Hàng \n ký gửi","Đóng");
                }
            }

            @Override
            public void confirmMenu(Player pl, int select) {
                if (canOpenNpc(pl)) {
                    switch (select) {
                        case 0:
                            Service.getInstance().sendPopUpMultiLine(pl, tempId, avartar,
                                    "Cửa hàng chuyên nhận ký gửi mua bán vật phẩm\bChỉ với 5 hồng ngọc\bGiá trị ký gửi 10k-200Tr vàng hoặc 2-2k ngọc\bMột người bán, vạn người mua, mại dô, mại dô");
                            break;
                        case 1:
                            if (pl.getSession().actived) {
                                ShopKyGuiService.gI().openShopKyGui(pl);
                            } else {
                                Service.gI().sendThongBaoOK(pl, "Chỉ dành cho thành viên");
                            }

                            break;

                    }
                }
            }
        };
    }

    private static Npc poTaGe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 140) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Đa vũ trụ song song \b|7|Con muốn gọi con trong đa vũ trụ \b|1|Với giá 200tr vàng không?",
                                "Gọi Boss\nNhân bản", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 140) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {
                                    Boss oldBossClone = BossManager.gI()
                                            .getBossById(Util.createIdBossClone((int) player.id));
                                    if (oldBossClone != null) {
                                        this.npcChat(player,
                                                "Nhà ngươi hãy tiêu diệt Boss lúc trước gọi ra đã, con boss đó đang ở khu "
                                                        + oldBossClone.zone.zoneId);
                                    } else if (player.inventory.gold < 200_000_000) {
                                        this.npcChat(player, "Nhà ngươi không đủ 200 Triệu vàng ");
                                    } else {
                                        List<Skill> skillList = new ArrayList<>();
                                        for (byte i = 0; i < player.playerSkill.skills.size(); i++) {
                                            Skill skill = player.playerSkill.skills.get(i);
                                            if (skill.point > 0) {
                                                skillList.add(skill);
                                            }
                                        }
                                        int[][] skillTemp = new int[skillList.size()][3];
                                        for (byte i = 0; i < skillList.size(); i++) {
                                            Skill skill = skillList.get(i);
                                            if (skill.point > 0) {
                                                skillTemp[i][0] = skill.template.id;
                                                skillTemp[i][1] = skill.point;
                                                skillTemp[i][2] = skill.coolDown;
                                            }
                                        }
                                        BossData bossDataClone = new BossData(
                                                "Nhân Bản " + player.name,
                                                player.gender,
                                                new short[] { player.getHead(), player.getBody(), player.getLeg(),
                                                        player.getFlagBag(), player.idAura, player.getEffFront() },
                                                player.nPoint.hpMax / 200,
                                                new int[] { player.nPoint.dame * 1000 },
                                                new int[] { 140 },
                                                skillTemp,
                                                new String[] { "|-2|Boss nhân bản đã xuất hiện rồi" }, // text chat 1
                                                new String[] { "|-1|Ta sẽ chiếm lấy thân xác của ngươi hahaha!" }, // text
                                                                                                                   // chat
                                                                                                                   // 2
                                                new String[] { "|-1|Lần khác ta sẽ xử đẹp ngươi" }, // text chat 3
                                                60);

                                        try {
                                            new NhanBan(Util.createIdBossClone((int) player.id), bossDataClone,
                                                    player.zone);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Logger.logException(Manager.class, e, "Lỗi tạo nhân bản");
                                        }
                                        // trừ vàng khi gọi boss
                                        player.inventory.gold -= 200_000_000;
                                        Service.gI().sendMoney(player);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        };
    }

    private static Npc quyLaoKame(int mapId, int status, int cx, int cy, int tempId, int avartar) {

        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {

                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.getSession().is_gift_box) {
                            // this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào con, con muốn ta giúp
                            // gì nào?", "Giải tán bang hội", "Nhận quà\nđền bù");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào con, con muốn ta giúp gì nào?\n" ,
                                  
                                    "Hỗ trợ nhiệm vụ","Kho báu dưới biển", "Giải tán bang hội", "Lãnh địa Bang Hội", 
                                    "Tới không gian xanh");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                             case 0: //shop
                                this.createOtherMenu(player, 12131, "Ta sẽ hỗ trợ con bỏ qua nhiệm vụ với giá 1 thỏi vàng:",
                                         "Bỏ qua nhiệm\n vụ bang hội", "Bỏ qua nhiệm\n vụ kết bạn", "Hủy");

                                break;
                            case 1:
                                if (player.clan != null) {
                                    if (player.clan.banDoKhoBau != null) {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPENED_DBKB,
                                                "Bang hội của con đang đi tìm kho báu dưới biển cấp độ "
                                                        + player.clan.banDoKhoBau.level
                                                        + "\nCon có muốn đi theo không?",
                                                "Đồng ý", "Từ chối");
                                    } else {

                                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_DBKB,
                                                "Đây là bản đồ kho báu \nCác con cứ yên tâm lên đường\n"
                                                        + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                                "Chọn\ncấp độ", "Từ chối");
                                    }
                                } else {
                                    this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                                }
                                break;
                            case 2:
                                Clan clan = player.clan;
                                if (clan != null) {
                                    ClanMember cm = clan.getClanMember((int) player.id);
                                    if (cm != null) {
                                        if (clan.members.size() > 1) {
                                            Service.gI().sendThongBao(player, "Bang phải còn một người");
                                            break;
                                        }
                                        if (!clan.isLeader(player)) {
                                            Service.gI().sendThongBao(player, "Phải là bảng chủ");
                                            break;
                                        }
                                        //
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DISSOLUTION_CLAN,
                                                -1,
                                                "Con có chắc chắn muốn giải tán bang hội không? Ta cho con 2 lựa chọn...",
                                                "Yes you do!", "Từ chối!");
                                    }
                                    break;
                                }
                                Service.gI().sendThongBao(player, "Chức năng không hoạt động!");
                                break;
                            case 3:
                                if (player.getSession().player.nPoint.power >= 80000000000L) {

                                    ChangeMapService.gI().changeMapBySpaceShip(player, 153, -1, 432);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                }
                                break; // qua lanh dia
                           
                            case 4:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 218, -1, 76);
                                break;
                            default:
                                this.npcChat(player, "Đang cập nhật...");
                                break;

                        }
                    } 
                    else if (player.iDMark.getIndexMenu() == 12131) {
                        switch (select) {
                            case 0:
                                if (player.playerTask.taskMain.id == 13) {
                                    if (player.playerTask.taskMain.index == 0) {
                                        TaskService.gI().DoneTask(player, ConstTask.TASK_13_0);
                                    } else {
                                        Service.gI().sendThongBao(player, "Ta đã giúp con hoàn thành nhiệm vụ");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Nhiệm vụ ko phù hợp");

                                }
                                break;
                            case 1:
                                if (player.playerTask.taskMain.id == 11) {
                                    if (player.playerTask.taskMain.index == 0) {
                                        TaskService.gI().DoneTask(player, ConstTask.TASK_11_0);
                                    if (player.playerTask.taskMain.index == 1) {
                                        TaskService.gI().DoneTask(player, ConstTask.TASK_11_1);}
                                    if (player.playerTask.taskMain.index == 2) {
                                        TaskService.gI().DoneTask(player, ConstTask.TASK_11_2);}
                                    } else {
                                        Service.gI().sendThongBao(player, "Ta đã giúp con hoàn thành nhiệm vụ");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Nhiệm vụ ko phù hợp");

                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_DBKB) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                                    ChangeMapService.gI().goToDBKB(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                                }
                                break;

                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_DBKB) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                                    Input.gI().createFormChooseLevelBDKB(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                                }
                                break;
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_BDKB) {
                        switch (select) {
                            case 0:
                                BanDoKhoBauService.gI().openBanDoKhoBau(player,
                                        Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                break;
                        }
                    }
                }

            }
            // }
        };
    }

    public static Npc ongGohan_ongMoori_ongParagus(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                String isActive = player.getSession().actived ? "Đã kích hoạt tài khoản !" : "Chưa kich hoạt tài khoản";
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Chào con ! \n" + isActive
                                        .replaceAll("%1", player.gender == ConstPlayer.TRAI_DAT ? "Quy lão Kamê"
                                                : player.gender == ConstPlayer.NAMEC ? "Trưởng lão Guru"
                                                        : "Vua Vegeta"),
                                "Đổi mật khẩu", "Nhận Ngọc", "Hướng dẫn", 
                                "Gíp Cốt","Nhận đệ tử");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                Input.gI().createFormChangePassword(player);
                                break;
                        
                            case 1:
                                if (player.inventory.gem == 200000) {
                                    this.npcChat(player, "Con đã nhận rồi");
                                    break;
                                }
                                player.inventory.gem = 200000;
                                Service.gI().sendMoney(player);
                                Service.gI().sendThongBao(player, "Bạn vừa nhận được 200K ngọc xanh");
                                break;
                          
                            case 3:
                                Input.gI().createFormGiftCode(player);
                                break;
                          
                            case 4:
                               if (player.pet == null) {
                                    PetService.gI().createNormalPet(player);
                                    Service.getInstance().sendThongBao(player,
                                            "Con vừa nhận được đệ tử! Hãy chăm sóc nó nhé");
                                } else {
                                    this.npcChat(player, "Đã có đệ tử rồi mà!");

                                }
                                break;
                                  case 2: //shop
                                createOtherMenu(player, ConstNpc.MENU_THONGTIN,
                                        "Ngươi muốn biết những thông tin gì hãy chọn bên dưới!",
                                        "Cơ chế set kích hoạt", "Chân mệnh", "Đá nâng cấp", "Pet công đức", "Cơ chế đệ tử");
                                break;
                        }
                    }
                                else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_THONGTIN) {
                        switch (select) {
                            case 0: //shop
                                createOtherMenu(player, ConstNpc.MENU_KH,
                                        "|0|Ngươi đã có cho mình 1 set kích hoạt V1 miễn phí trên người!\n"
                                        + "|1|Ngươi hãy thu thập thêm các món thần linh để làm phôi"
                                       + "Làm nhiệm vụ tại bò mộng để tích năng lượng kích hoạt\n"                                       
                                        + "|2|Sau đó tới gặp bà hạt mít để nâng lên sét kích hoạt V2",
                                        "Đóng");
                                break;
                            case 1: //shop
                                createOtherMenu(player, ConstNpc.MENU_KYNANG,
                                        "Hãy tới gặp whis trên kaio để đến map thiên tử"
                                        + "\n|7|Đang hoàn thiện chức năng",
                                        "Đóng");
                                break;
                            case 2: //shop
                                createOtherMenu(player, ConstNpc.MENU_HONHOAN,
                                        "Hãy Dùng những món đồ thần để chuyển nó thành đá nâng cấp tại bà hạt mít"
                                        + "\n Mỗi cấp độ của đồ chỉ tốn 1 viên đá.",
                                        "Đóng");
                                break;
                            case 3: //shop
                                createOtherMenu(player, ConstNpc.MENU_NANGCAPDT,
                                        "Hãy tới gặp đường tăng tại làng aru để thỉnh kinh"
                                        + "\nKhi làm hết các nhiệm vụ đường tăng giao cho"
                                        + "\nSẽ nhận được các pet công đức",
                                        "Đóng");
                                break;
                            case 4: //shop
                                createOtherMenu(player, ConstNpc.MENU_DETU,
                                        "|7|Có 2 loại đệ tử: Đệ thường và đệ Kid Bư\n"
                                        + "|2|Hãy tìm tên mabu mập xuất hiển ở khắp mọi nơi để đấu với hắn"
                                        + "\nCàng đấu lâu hắn sẽ trở lên càng mạnh"
                                        + "\nKhi hắn đạt mốc 16tr HP và tiêu diệt hắn Kid Bư sẽ xuất hiện"
                                        + "\n Sau đó tiêu diệt tên ác nhân Kid Bư để nhận đệ Kid bư",
                                        "Đóng");

                                break;
                                
                                        
                                        

                        }
                    } 
                      
                }

            }

        };
    }

    public static Npc bulmaQK(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Cậu cần trang bị gì cứ đến chỗ tôi nhé", "Cửa\nhàng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:// Shop
                                if (player.gender == ConstPlayer.TRAI_DAT) {
                                    ShopServiceNew.gI().opendShop(player, "BUNMA", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Xin lỗi cưng, chị chỉ bán đồ cho người Trái Đất", "Đóng");
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc bearry(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "|7|Cậu đang có " + player.pointSukien + " điểm sự kiện! Cậu muốn ?\n" +
                                        "|2|Hộp quà thường: Bọ hung lục, vàng, đỏ, lam x20 mỗi loại!\n" +
                                        "|1|Hộp quà đặc biệt: Bọ hung lục, vàng, đỏ, lam x20 mỗi loại và 1 bướm đêm!",
                                "Điểm danh\nhằng ngày", "Đổi hộp thường", "Đổi hộp VIP", "Top sự kiện", "Xem Boss");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        Item bohungluc = null;
                        Item bohungdo = null;
                        Item bohungvang = null;
                        Item bohunglam = null;
                        Item buomdem = null;
                        try {
                            bohungluc = InventoryServiceNew.gI().findItemBag(player, 1350);
                            bohungdo = InventoryServiceNew.gI().findItemBag(player, 1352);
                            bohungvang = InventoryServiceNew.gI().findItemBag(player, 1351);
                            bohunglam = InventoryServiceNew.gI().findItemBag(player, 1353);
                            buomdem = InventoryServiceNew.gI().findItemBag(player, 1354);
                        } catch (Exception e) {
                            // throw new RuntimeException(e);
                        }
                        switch (select) {
                            case 0:// Shop
                                LocalDate now = LocalDate.now();
                                // Chuyển đổi giá trị mili giây thành LocalDate
                                LocalDate lastDate = LocalDate.ofEpochDay(player.last_time_dd / (24 * 60 * 60 * 1000));
                                long caichogi = now.toEpochDay() * (24 * 60 * 60 * 1000);
                                boolean isAttended = now.isEqual(lastDate);
                                if (!isAttended && caichogi != player.last_time_dd) {
                                    player.inventory.ruby += 1000;
                                    player.inventory.gold += 500000000;
                                    player.countOpenBox = 0;
                                    player.pointSukien++;
                                    Item kitSuaChua = ItemService.gI().createNewItem((short) 1425, 1);
                                    kitSuaChua.itemOptions.add(new ItemOption(30,0));
                                    InventoryServiceNew.gI().addItemBag(player, kitSuaChua);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    this.npcChat(player,
                                            "|2|Điểm danh thành công nhận 1000 hồng ngọc và 500tr vàng 1 kit sửa chữa 1 điểm sự kiện và 10 lượt mở quà! ");
                                    player.last_time_dd = now.toEpochDay() * (24 * 60 * 60 * 1000);
                                } else {
                                    this.npcChat(player, "Thời gian đến lần điểm danh kế tiếp còn "
                                            + Util.msToTime(player.last_time_dd));
                                }
                                break;
                            case 1:
                                if (bohungluc == null || bohungluc.quantity < 20 ||
                                        bohungdo == null || bohungdo.quantity < 20 ||
                                        bohungvang == null || bohungvang.quantity < 20 ||
                                        bohunglam == null || bohunglam.quantity < 20) {
                                    this.npcChat(player, "Bạn không đủ nguyên liệu");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành trang của cưng không đủ chỗ trống");
                                } else {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, bohungluc, 20);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, bohunglam, 20);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, bohungvang, 20);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, bohungdo, 20);

                                    Item ct = ItemService.gI().createNewItem((short) 1345);
                                    ct.itemOptions.add(new ItemOption(30, 1));
                                    InventoryServiceNew.gI().addItemBag(player, ct);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn nhận được 1 hộp quà thường");
                                }
                                break;
                            case 2:
                                if (bohungluc == null || bohungluc.quantity < 20 ||
                                        bohungdo == null || bohungdo.quantity < 20 ||
                                        bohungvang == null || bohungvang.quantity < 20 ||
                                        bohunglam == null || bohunglam.quantity < 20 ||
                                        buomdem == null) {
                                    this.npcChat(player, "Bạn không đủ nguyên liệu");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành trang của cưng không đủ chỗ trống");
                                } else {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, bohungluc, 20);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, bohunglam, 20);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, bohungvang, 20);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, bohungdo, 20);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, buomdem, 1);

                                    Item ct = ItemService.gI().createNewItem((short) 1346);
                                    ct.itemOptions.add(new ItemOption(30, 1));
                                    InventoryServiceNew.gI().addItemBag(player, ct);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn nhận được 1 hộp quà đặc biệt");
                                }
                                break;
                            case 3:
                                Service.gI().showListTop(player, Manager.topSuKien);
                                break;
                            case 4:
                                BossManager.gI().showListBossNormal(player);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc dende(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.idNRNM != -1) {
                            if (player.zone.map.mapId == 7) {
                                this.createOtherMenu(player, 1,
                                        "Ồ, ngọc rồng namếc, bạn thật là may mắn\nnếu tìm đủ 7 viên sẽ được Rồng Thiêng Namếc ban cho điều ước",
                                        "Hướng\ndẫn\nGọi Rồng", "Gọi rồng", "Từ chối");
                            }
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Anh cần trang bị gì cứ đến chỗ em nhé", "Cửa\nhàng");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:// Shop
                                if (player.gender == ConstPlayer.NAMEC) {
                                    ShopServiceNew.gI().opendShop(player, "DENDE", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Xin lỗi anh, em chỉ bán đồ cho dân tộc Namếc", "Đóng");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 1) {

                        if (player.clan == null) {
                            this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Anh hãy gia nhập 1 bang hội", "Đóng");
                            return;
                        }
                        if (player.idNRNM != 353) {
                            this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Anh phải có viên ngọc rồng Namếc 1 sao",
                                    "Đóng");
                            return;
                        }

                        byte numChar = 0;
                        for (Player pl : player.zone.getPlayers()) {
                            if (pl.clan.id == player.clan.id && pl.id != player.id) {
                                if (pl.idNRNM != -1) {
                                    numChar++;
                                }
                            }
                        }
                        if (numChar < 6) {
                            this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Anh hãy tập hợp đủ 7 viên ngọc rồng nameck đi", "Đóng");
                            return;
                        }

                        if (player.zone.map.mapId == 7 && player.idNRNM != -1) {
                            if (player.idNRNM == 353) {
                                NgocRongNamecService.gI().tOpenNrNamec = System.currentTimeMillis() + 86400000;
                                NgocRongNamecService.gI().firstNrNamec = true;
                                NgocRongNamecService.gI().timeNrNamec = 0;
                                NgocRongNamecService.gI().doneDragonNamec();
                                NgocRongNamecService.gI().initNgocRongNamec((byte) 1);
                                NgocRongNamecService.gI().reInitNrNamec((long) 86399000);
                                SummonDragon.gI().summonNamec(player);
                            } else {
                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        "Anh hãy tập hợp đủ 7 viên ngọc rồng nameck đi", "Đóng");
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc appule(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {

                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi cần trang bị gì cứ đến chỗ ta nhé", "Cửa\nhàng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:// Shop
                                if (player.gender == ConstPlayer.XAYDA) {
                                    ShopServiceNew.gI().opendShop(player, "APPULE", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Về hành tinh hạ đẳng của ngươi mà mua đồ cùi nhé. Tại đây ta chỉ bán đồ cho người Xayda thôi",
                                            "Đóng");
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc drDrief(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {

                if (canOpenNpc(pl)) {
                    if (this.mapId == 84) {
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                pl.gender == ConstPlayer.TRAI_DAT ? "Đến\nTrái Đất"
                                        : pl.gender == ConstPlayer.NAMEC ? "Đến\nNamếc" : "Đến\nXayda");
                    } else if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                    "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                    "Đến\nNamếc", "Đến\nXayda", "Siêu thị");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 84) {
                        ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 24, -1, -1);
                    } else if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc cargo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {

                if (canOpenNpc(pl)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                    "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                    "Đến\nTrái Đất", "Đến\nXayda", "Siêu thị");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc cui(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            private final int COST_FIND_BOSS = 50000000;

            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang bị bắt cóc rồi");
                        } else {
                            if (this.mapId == 19) {

                                int taskId = TaskService.gI().getIdTask(pl);
                                switch (taskId) {
                                    case ConstTask.TASK_19_0:
                                        this.createOtherMenu(pl, ConstNpc.MENU_FIND_KUKU,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nKuku\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)",
                                                "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    case ConstTask.TASK_19_1:
                                        this.createOtherMenu(pl, ConstNpc.MENU_FIND_MAP_DAU_DINH,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nMập đầu đinh\n(" + Util.numberToMoney(COST_FIND_BOSS)
                                                        + " vàng)",
                                                "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    case ConstTask.TASK_19_2:
                                        this.createOtherMenu(pl, ConstNpc.MENU_FIND_RAMBO,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nRambo\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)",
                                                "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    default:
                                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến Cold", "Đến\nNappa", "Từ chối");

                                        break;
                                }
                            } else if (this.mapId == 68) {
                                this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                        "Ngươi muốn về Thành Phố Vegeta", "Đồng ý", "Từ chối");
                            } else {
                                this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                        "Tàu vũ trụ Xayda sử dụng công nghệ mới nhất, "
                                                + "có thể đưa ngươi đi bất kỳ đâu, chỉ cần trả tiền là được.",
                                        "Đến\nTrái Đất", "Đến\nNamếc", "Siêu thị");
                            }
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 26) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                    break;
                            }
                        }
                    }
                    if (this.mapId == 19) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (player.getSession().player.nPoint.power >= 41000000000L
                                            && player.playerTask.taskMain.id > 26) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                    }
                                    // break;
                                    else {
                                        Service.gI().sendThongBaoOK(player, "Làm Nhiệm Vụ 26 Và Đạt 41 Tỷ Sức Mạnh");
                                    }
                                    break;
                                case 1:
                                    if (player.playerTask.taskMain.id >= 17) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                    } else {
                                        Service.gI().sendThongBaoOK(player, "Làm Nhiệm Vụ Đi");
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_KUKU) {
                            switch (select) {
                                case 0:
                                    Boss boss = BossManager.gI().getBossById(BossID.KUKU);
                                    if (boss != null && !boss.isDie()) {
                                        if (boss.zone != null && player.inventory.gold >= COST_FIND_BOSS) {
                                            Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId,
                                                    boss.zone.zoneId);
                                            if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                                player.inventory.gold -= COST_FIND_BOSS;
                                                ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x,
                                                        boss.location.y);
                                                Service.gI().sendMoney(player);
                                            } else {
                                                Service.gI().sendThongBao(player, "Khu vực đang full.");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold)
                                                    + " vàng");
                                        }
                                        break;
                                    }
                                    Service.gI().sendThongBao(player, "Chết rồi ba...");
                                    break;
                                case 1:
                                    if (player.getSession().player.nPoint.power >= 41000000000L
                                            && player.playerTask.taskMain.id > 26) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                    }
                                    // break;
                                    else {
                                        Service.gI().sendThongBaoOK(player, "Làm Nhiệm Vụ 26 Và Đạt 41 Tỷ Sức Mạnh");
                                    }
                                    break;
                                case 2:
                                    if (player.playerTask.taskMain.id >= 17) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                    } else {
                                        Service.gI().sendThongBaoOK(player, "Làm Nhiệm Vụ Đi");
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_MAP_DAU_DINH) {
                            switch (select) {
                                case 0:
                                    Boss boss = BossManager.gI().getBossById(BossID.MAP_DAU_DINH);
                                    if (boss != null && !boss.isDie()) {
                                        if (player.inventory.gold >= COST_FIND_BOSS) {
                                            Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId,
                                                    boss.zone.zoneId);
                                            if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                                player.inventory.gold -= COST_FIND_BOSS;
                                                ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x,
                                                        boss.location.y);
                                                Service.gI().sendMoney(player);
                                            } else {
                                                Service.gI().sendThongBao(player, "Khu vực đang full.");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold)
                                                    + " vàng");
                                        }
                                        break;
                                    }
                                    Service.gI().sendThongBao(player, "Chết rồi ba...");
                                    break;
                                case 1:
                                    if (player.getSession().player.nPoint.power >= 41000000000L
                                            && player.playerTask.taskMain.id > 26) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                    }
                                    // break;
                                    else {
                                        Service.gI().sendThongBaoOK(player, "Làm Nhiệm Vụ 26 Và Đạt 41 Tỷ Sức Mạnh");
                                    }
                                    break;
                                case 2:
                                    if (player.playerTask.taskMain.id >= 17) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                    } else {
                                        Service.gI().sendThongBaoOK(player, "Làm Nhiệm Vụ Đi");
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_RAMBO) {
                            switch (select) {
                                case 0:
                                    Boss boss = BossManager.gI().getBossById(BossID.RAMBO);
                                    if (boss != null && !boss.isDie()) {
                                        if (player.inventory.gold >= COST_FIND_BOSS) {
                                            Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId,
                                                    boss.zone.zoneId);
                                            if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                                player.inventory.gold -= COST_FIND_BOSS;
                                                ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x,
                                                        boss.location.y);
                                                Service.gI().sendMoney(player);
                                            } else {
                                                Service.gI().sendThongBao(player, "Khu vực đang full.");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold)
                                                    + " vàng");
                                        }
                                        break;
                                    }
                                    Service.gI().sendThongBao(player, "Chết rồi ba...");
                                    break;
                                case 1:
                                    if (player.getSession().player.nPoint.power >= 41000000000L
                                            && player.playerTask.taskMain.id > 26) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                    }
                                    // break;
                                    else {
                                        Service.gI().sendThongBaoOK(player, "Làm Nhiệm Vụ 26 Và Đạt 41 Tỷ Sức Mạnh");
                                    }
                                    break;
                                case 2:
                                    if (player.playerTask.taskMain.id >= 17) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                    } else {
                                        Service.gI().sendThongBaoOK(player, "Làm Nhiệm Vụ Đi");
                                    }
                                    break;
                            }
                        }
                    }
                    if (this.mapId == 68) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 19, -1, 1100);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc santa(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {

                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xin chào, ta có một số vật phẩm đặt biệt cậu có muốn xem không?",
                            "Cửa hàng", 
                            "Cửa Hàng \nCải Trang");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                    if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: // shop
                                    ShopServiceNew.gI().opendShop(player, "SANTA", false);
                                    break;
                               
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "CAI_TRANG_SANTA", true);
                                    break;

                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc thodaika(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {

                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Sàn đầu tư nhị phân hót nhất thị trường\nRủi ro cực thấp\nLợi nhuận cực cao\nTỉ lệ thành công lên đến 50%",
                            "Tài", "Xỉu");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        // Service.gI().sendThongBao(player, "Tệ nạn? ");
                        if (!player.getSession().actived) {
                            Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                        } else if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    Input.gI().TAI(player);
                                    break;
                                case 1:
                                    Input.gI().XIU(player);
                                    break;

                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc uron(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    ShopServiceNew.gI().opendShop(pl, "URON", false);
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc baHatMit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Ép sao\ntrang bị", "Pha lê\nhóa trang bị",
  "Nâng cấp SKH","Chế tác sao pha lê lấp lánh","Kích hoạt cải trang","Khảm hóa đá nâng cấp"
                             );
                    } else if (this.mapId == 121) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Về đảo\nrùa");

                    } else {

                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?, Muốn có bùa x4 thì chăm làm sự kiện con nhé !",
                                "Cửa hàng\nBùa", "Nâng cấp\nVật phẩm",
                                "Nâng cấp\nBông tai\nPorata", "Mở chỉ số\nBông tai\nPorata",
                                "Nhập\nNgọc Rồng", "Phân Rã\nĐồ Thần Linh");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.EP_SAO_TRANG_BI);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.PHA_LE_HOA_TRANG_BI);
                                    break;
                              

                               
                                    case 2:
                                    createOtherMenu(player, ConstNpc.SKH,
                                            "|7|Ngươi đang còn: " + player.pointKarin + "Năng lượng kích hoạt" +
                                            "\n|2|Mỗi món đồ kích hoạt V2 cần" +
                                            "\n|4|1 điểm năng lượng kích hoạt" +
                                            "\n Đồ thần linh tương ứng"        ,
                                            "Nâng SKH V2");
                                    break;
                                   case 3: //nâng cấp Chân mệnh
                                    this.createOtherMenu(player, 1513,
                                            "|7|Sao pha lê lấp lánh"
                                            + "\n|1|Hãy tới gặp quy lão để ông ta đưa con đến không gian xanh"
                                            + "\nTại đó con có thể kiếm được nguyên liệu "
                                                    + "\n(Hãy quay lại tìm ta khi đủ nguyên liệu",
                                                 
                                          
                                            "Nâng cấp");
                                    break;
                                     case 4: //nâng cấp Chân mệnh
                                    this.createOtherMenu(player, 1511,
                                            "|7|Kích hoạt cải trang"
                                            + "\n|1|Bạn cần có 1 cải trang, x10 đá ma thuật, x20 thỏi vàng"
                                            + "\n|5| Khi kích hoạt sẽ có tỷ lệ chia làm 3 cấp độ: "
                                                    + "\n(Sơ cấp) chỉ số từ 10 đến 15%"
                                                    + "\n(Trung cấp) chỉ số từ 15 đến 20%"
                                                    + "\n(Cao cấp) chỉ số từ 20 đến 25%",
                                          
                                            "Nâng cấp", "Hướng dẫn kiếm đá ma thuật");
                                    break;
                                    case 5:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_DA_NC);
                                    break;
                             
                            }
                        }
                         else if (player.iDMark.getIndexMenu() == 1513) {
                            switch (select) {
                                             
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_SPL);
                                    break;
                            }
                         }
                         else if (player.iDMark.getIndexMenu() == 1511) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_CAI_TRANG);
                                    break;
                               case 1: //nâng cấp Chân mệnh
                                    this.createOtherMenu(player, 1811,
                                            "|7|Cách kiếm đá ma thuật"
                                            + "\n|5| Làm nhiệm vụ bò mộng để có cơ hội nhận đá ma thuật"
                                            + "\n Săn boss bư gầy"
                                            + "\n Có thể mua tại shop santa",
                                           
                                            "Đóng");
                                    break;
                               

                            }
                        } 
                         else if (player.iDMark.getIndexMenu() == ConstNpc.SKH) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_SKH);
                                    break;
                              
                              
                            }
                        }else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.EP_SAO_TRANG_BI:
                                case CombineServiceNew.CHUYEN_HOA_TRANG_BI:
                               
                                case CombineServiceNew.LUYEN_HOA_CHIEN_LINH:
                                case CombineServiceNew.NANG_CAP_SKH:
                                     case CombineServiceNew.NANG_CAP_SPL:
                                case CombineServiceNew.MO_CHI_SO_CAI_TRANG:
                               case CombineServiceNew.NANG_CAP_DA_NC:
                              
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                                case CombineServiceNew.PHA_LE_HOA_TRANG_BI:
                                    switch (select) {
                                        case 0:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 1;
                                            }
                                            break;
                                        case 1:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 10;
                                            }
                                            break;
                                        case 2:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 20;
                                            }
                                            break;
                                        case 3:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 50;
                                            }
                                            break;
                                        case 4:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 100;
                                            }
                                            break;

                                    }
                                    CombineServiceNew.gI().startCombine(player);

                                    break;

                            }
                        }
                    } else if (this.mapId == 112) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1156);
                                    break;
                            }
                        }
                    } else if (this.mapId == 42 || this.mapId == 43 || this.mapId == 44 || this.mapId == 84) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: // shop bùa
                                    createOtherMenu(player, ConstNpc.MENU_OPTION_SHOP_BUA,
                                            "Bùa của ta rất lợi hại, nhìn ngươi yếu đuối thế này, chắc muốn mua bùa để "
                                                    + "mạnh mẽ à, mua không ta bán cho, xài rồi lại thích cho mà xem.",
                                            "Bùa\n1 giờ", "Bùa\n8 giờ", "Bùa\n1 tháng", "Đóng");
                                    break;
                                case 1:

                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_VAT_PHAM);
                                    break;
                                case 2: // nâng cấp bông tai
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI);
                                    break;
                                case 3: // làm phép nhập đá
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI);
                                    break;
                                case 4:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NHAP_NGOC_RONG);
                                    break;
                                case 5: // phân rã đồ thần linh
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.PHAN_RA_DO_THAN_LINH);
                                    break;
                                case 6:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_SKH_VIP);
                                    break;
                                case 7:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.OPEN_SKH_MA_SU);
                                    break;
                                case 8:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.OPEN_SKH_THAN_SU);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_SHOP_BUA) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1H", true);
                                    break;
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "BUA_8H", true);
                                    break;
                                case 2:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1M", true);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.NANG_CAP_VAT_PHAM:
                                case CombineServiceNew.NANG_CAP_BONG_TAI:
                                case CombineServiceNew.MO_CHI_SO_BONG_TAI:
                                case CombineServiceNew.NHAP_NGOC_RONG:
                                case CombineServiceNew.PHAN_RA_DO_THAN_LINH:
                                case CombineServiceNew.CHE_TAO_TRANG_BI_TS:
                                case CombineServiceNew.NANG_CAP_SKH_VIP:
                                case CombineServiceNew.OPEN_SKH_MA_SU:
                                case CombineServiceNew.OPEN_SKH_THAN_SU:

                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHAN_RA_DO_THAN_LINH) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_CAP_DO_TS) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_DOI_SKH_VIP) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc ruongDo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    InventoryServiceNew.gI().sendItemBox(player);
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc duongtank(int mapId, int status, int cx, int cy, int tempId, int avartar) {
    return new Npc(mapId, status, cx, cy, tempId, avartar) {

        @Override
        public void openBaseMenu(Player player) {
            if (canOpenNpc(player)) {
                if (this.mapId == 123) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Hãy hộ tống ta đến Đông Karin, ta sẽ tặng con quả hồng đào !", "Về làng aru",
                            "Hộ tống thỉnh kinh", "Shop hồng đào", "Luyện công đức");
                } else if (mapId == 0) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Bạn Muốn tới ngũ hành sơn?", "OK", "Từ chối");

                } else if (mapId == 122) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xia xia Ngươi còn: " + player.NguHanhSonPoint + "Điểm ngũ hành sơn",
                            "Đổi cải trang");
                }

            }
        }

        @Override
        public void confirmMenu(Player player, int select) {
            if (this.mapId == 0) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {

                        case 0:
                            if (player.nPoint.limitPower <= 1500000) {
                                ChangeMapService.gI().changeMapInYard(player, 123, -1, 469);
                            }
                            break;

                    }
                }
            } else if (this.mapId == 122) {
                if (select == 0) {
                    if (player.NguHanhSonPoint >= 500) {
                        player.NguHanhSonPoint -= 500;
                        Item item = ItemService.gI().createNewItem((short) (711));
                        item.itemOptions.add(new Item.ItemOption(50, 20));
                        item.itemOptions.add(new Item.ItemOption(77, 20));
                        item.itemOptions.add(new Item.ItemOption(103, 20));
                        item.itemOptions.add(new Item.ItemOption(33, 0));
                        item.itemOptions.add(new Item.ItemOption(159, 4));
                        item.itemOptions.add(new Item.ItemOption(93, 14));
                        //
                        InventoryServiceNew.gI().addItemBag(player, item);
                        Service.gI().sendThongBao(player, "Chúc Mừng Bạn Đổi Vật Phẩm Thành Công !");
                    } else {
                        Service.gI().sendThongBao(player,
                                "Không đủ điểm, bạn còn " + (500 - player.NguHanhSonPoint) + " điểm nữa");
                    }

                }
            } else if (this.mapId == 123) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0:
                            ChangeMapService.gI().changeMapInYard(player, 0, -1, 469);
                            break;
                      
                        case 2:
                            ShopServiceNew.gI().opendShop(player, "DUONGTANK", true);
                            break;
                        case 3:
                            CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.REN_CONG_DUC);
                            break;
                    }
                
            } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                    case CombineServiceNew.REN_CONG_DUC:
                        if (select == 0) {
                            CombineServiceNew.gI().startCombine(player);
                        }
                        break;
                }
            }
            
            }
        
        }
    };
}
    public static Npc minuong2(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (mapId == 179) {
                        this.createOtherMenu(player, 0,
                                "Hãy hộ tống ta đến thung lũng cầu treo, em sẽ tặng anh zai 1 phần thưởng đặc biệt !",
                                "Oke",
                                "Éo");
                    }

                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (select) {

                    

                    }
                }
            }
        };
    }

    public static Npc dauThan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    player.magicTree.openMenuTree();
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_LEFT_PEA:
                            if (select == 0) {
                                player.magicTree.harvestPea();
                            } else if (select == 1) {
                                if (player.magicTree.level == 10) {
                                    player.magicTree.fastRespawnPea();
                                } else {
                                    player.magicTree.showConfirmUpgradeMagicTree();
                                }
                            } else if (select == 2) {
                                player.magicTree.fastRespawnPea();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_FULL_PEA:
                            if (select == 0) {
                                player.magicTree.harvestPea();
                            } else if (select == 1) {
                                player.magicTree.showConfirmUpgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_CONFIRM_UPGRADE:
                            if (select == 0) {
                                player.magicTree.upgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_UPGRADE:
                            if (select == 0) {
                                player.magicTree.fastUpgradeMagicTree();
                            } else if (select == 1) {
                                player.magicTree.showConfirmUnuppgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_CONFIRM_UNUPGRADE:
                            if (select == 0) {
                                player.magicTree.unupgradeMagicTree();
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc calick(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            private final byte COUNT_CHANGE = 50;
            private int count;

            private void changeMap() {
                if (this.mapId != 102) {
                    count++;
                    if (this.count >= COUNT_CHANGE) {
                        count = 0;
                        this.map.npcs.remove(this);
                        Map map = MapService.gI().getMapForCalich();
                        this.mapId = map.mapId;
                        this.cx = Util.nextInt(100, map.mapWidth - 100);
                        this.cy = map.yPhysicInTop(this.cx, 0);
                        this.map = map;
                        this.map.npcs.add(this);
                    }
                }
            }

            @Override
            public void openBaseMenu(Player player) {
                player.iDMark.setIndexMenu(ConstNpc.BASE_MENU);
                if (TaskService.gI().getIdTask(player) < ConstTask.TASK_20_0) {
                    Service.gI().hideWaitDialog(player);
                    Service.gI().sendThongBao(player, "Không thể thực hiện");
                    return;
                }
                if (this.mapId != player.zone.map.mapId) {
                    Service.gI().sendThongBao(player, "Calích đã rời khỏi map!");
                    Service.gI().hideWaitDialog(player);
                    return;
                }

                if (this.mapId == 102) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào chú, cháu có thể giúp gì?",
                            "Kể\nChuyện", "Quay về\nQuá khứ");
                } else {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào chú, cháu có thể giúp gì?", "Kể\nChuyện", "Đi đến\nTương lai", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (this.mapId == 102) {
                    if (player.iDMark.isBaseMenu()) {
                        if (select == 0) {
                            // kể chuyện
                            NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                        } else if (select == 1) {
                            // về quá khứ
                            ChangeMapService.gI().goToQuaKhu(player);
                        }
                    }
                } else if (player.iDMark.isBaseMenu()) {
                    if (select == 0) {
                        // kể chuyện
                        NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                    } else if (select == 1) {
                        // đến tương lai
                        // changeMap();
                        if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_20_0) {
                            ChangeMapService.gI().goToTuongLai(player);
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Không thể thực hiện");
                    }
                }
            }
        };
    }

    public static Npc jaco(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Gô Tên, Calich và Monaka đang gặp chuyện ở hành tinh Potaufeu \n Hãy đến đó ngay",
                                    "Đến \nPotaufeu");
                        } else if (this.mapId == 139) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Người muốn trở về?", "Quay về", "Từ chối");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        if (player.getSession().player.nPoint.power >= 800000000L) {

                            ChangeMapService.gI().goToPotaufeu(player);
                        } else {
                            this.npcChat(player, "Bạn chưa đủ 800tr sức mạnh để vào!");
                        }
                    } else if (this.mapId == 139) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                // về trạm vũ trụ
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24 + player.gender, -1, -1);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc bulmaNm(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (this.mapId == 206) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Hãy gia nhập phe chúng tôi để chống lại Fide",
                                    "Đổi phe", "Giao ngọc");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Cadic và Fide đang kéo cả hành tinh Namec đến bờ vực diệt vong, hãy đến giải cứu hành tinh Namec",
                                    "Đến chiến trường", "Bảng xếp hạng");
                        }

                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        if (this.mapId == 206) {
                            switch (select) {
                                // về trạm vũ trụ
                                case 0:
                                    if (!player.haveChangeFlagNamec) {
                                        Service.gI().changeFlag(player, 9);
                                        player.haveChangeFlagNamec = true;
                                    } else {
                                        this.npcChat(player, "Cậu đã có phe của mình rồi ?");
                                    }
                                    break;
                                case 1:
                                    if (player.getSession().actived && player.cFlag == 9) {
                                        List<Item> listNro = new ArrayList<>();
                                        Item nroNM1s = InventoryServiceNew.gI().findItemBag(player, 1387);
                                        if (nroNM1s != null)
                                            listNro.add(nroNM1s);
                                        Item nroNM2s = InventoryServiceNew.gI().findItemBag(player, 1388);
                                        if (nroNM2s != null)
                                            listNro.add(nroNM2s);
                                        Item nroNM3s = InventoryServiceNew.gI().findItemBag(player, 1389);
                                        if (nroNM3s != null)
                                            listNro.add(nroNM3s);
                                        Item nroNM4s = InventoryServiceNew.gI().findItemBag(player, 1390);
                                        if (nroNM4s != null)
                                            listNro.add(nroNM4s);
                                        Item nroNM5s = InventoryServiceNew.gI().findItemBag(player, 1391);
                                        if (nroNM5s != null)
                                            listNro.add(nroNM5s);
                                        Item nroNM6s = InventoryServiceNew.gI().findItemBag(player, 1392);
                                        if (nroNM6s != null)
                                            listNro.add(nroNM6s);
                                        Item nroNM7s = InventoryServiceNew.gI().findItemBag(player, 1393);
                                        if (nroNM7s != null)
                                            listNro.add(nroNM7s);
                                        if (listNro.size() > 0) {
                                            for (Item nro : listNro) {
                                                Service.gI().sendThongBao(player, "Bạn vừa giao " + nro.template.name
                                                        + " cho Bulma và nhận 1 điểm!");
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, nro, 1);
                                                player.pointNroNamec++;
                                                InventoryServiceNew.gI().sendItemBags(player);
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player,
                                                    "Cậu không có ngọc rồng !");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Cậu phải cùng phe mới có thể giao ngọc rồng !");
                                    }
                                    break;
                            }
                        } else {

                            switch (select) {
                                // về trạm vũ trụ
                                case 0:
                                    final Calendar rightNow = Calendar.getInstance();
                                    int hour = rightNow.get(11);
                                    if (hour >= 14 && hour < 17) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 206, -1, 1542);
                                    } else {
                                        Service.gI().sendThongBaoFromAdmin(player,
                                                "Chỉ mở từ 14h đến 17h !");
                                    }
                                    break;
                                case 1:
                                    Service.gI().showListTop(player, Manager.topNroNamec);
                                    break;
                            }
                        }
                    }

                }
            }
        };
    }

   

public static Npc fideNm(int mapId, int status, int cx, int cy, int tempId, int avartar) {
    return new Npc(mapId, status, cx, cy, tempId, avartar) {
        @Override
        public void openBaseMenu(Player player) {
            if (canOpenNpc(player)) {
                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                    String vodaiBallMessage = "|7|Các ngươi đã giao " + vodaiBallNumber + " viên ngọc rồng."+
                            "\nHãy tìm đủ 7 viên để có thể nhận thưởng ";
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            vodaiBallMessage + "\n Hãy gia nhập với bọn ta, ta sẽ chia cho ngươi 1 nửa hành tinh ",
                            "Đổi phe", "Giao ngọc","Nhận thưởng");
                }
            }
        }

        @Override
        public void confirmMenu(Player player, int select) {
            if (canOpenNpc(player)) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                          case 0:
                                    if (!player.haveChangeFlagNamec) {
                                        Service.gI().changeFlag(player, 10);
                                        player.haveChangeFlagNamec = true;
                                    } else {
                                        this.npcChat(player, "Cậu đã có phe của mình rồi ?");
                                    }
                                    break;
                        case 1:
                            if (player.getSession().actived && player.cFlag == 10 && player.iDMark.isHoldVodaiBall()) {
                                Service.gI().sendThongBao(player, "Ngươi đã giao ngọc thành công hãy tìm thêm những viên còn lại");
                                // Thực hiện logic cấp phần thưởng cho người chơi ở đây
                                player.iDMark.setHoldVodaiBall(false);
                                player.iDMark.setTempIdVodaiBallHold(-1);
                                vodaiBallNumber++;
                                Service.gI().sendFlagBag(player);
                            } else {
                                Service.gI().sendThongBao(player, "Ngươi phải cùng phe mới có thể giao ngọc rồng !");
                            }
                            break;
                  case 2:
    if (player.getSession().actived && player.cFlag == 10 && vodaiBallNumber >= 7) {
        int[] itemDos = new int[] { 457, 2030, 933, 225, 1303, 1304, 1461,224,225,226,227,228 };
        for (int itemId : itemDos) {
            Item item = ItemService.gI().createNewItem((short) itemId);
            item.quantity = 5; // Đặt số lượng là 10 cho mỗi vật phẩm
            InventoryServiceNew.gI().addItemBag(player, item);
        }
        InventoryServiceNew.gI().sendItemBags(player);
        
        // Reset số lượng ngọc về 0
        vodaiBallNumber = 0;
        
        Service.gI().sendThongBao(player, "|7|Bạn nhận được các vật phẩm đặc biệt với số lượng 10 cho mỗi vật phẩm!");
        
        // Thêm thông báo để người chơi quay lại ngày hôm sau
        Service.gI().sendThongBao(player, "Ngày mai hãy quay lại để nhận thưởng tiếp!");
    } else {
        Service.gI().sendThongBao(player, "Các ngươi chưa giao đủ 7 viên ngọc cho ta");
    }
    break;
                    }
                }
            }
        }
    };
}
    public static Npc diemVuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Tên Fide với Xên bọ hung đang làm loạn ở dưới này, cậu có thể giúp tôi trừng trị bọn chúng được không ?",
                                "Nâng cấp Ngọc bội", "Shop đá địa ngục", " Sống lại");

                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            // về trạm vũ trụ
                            case 0:
                                this.npcChat("Đang update...");
                                break;
                            case 1:
                                this.npcChat("Đang update...");
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 21 + player.gender, -1, 100);
                                break;
                        }
                    }

                }
            }
        };
    }

    public static Npc cayLongDen(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                Item longDenCheck = null;
                Integer longDenCheckQuantity = 0;
                try {
                    longDenCheck = InventoryServiceNew.gI().findItemBag(player, 1237);
                    if (longDenCheck != null) {
                        longDenCheckQuantity = longDenCheck.quantity;
                    }
                } catch (Exception e) {
                    // throw new RuntimeException(e);
                }
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "|2|Bạn đang có " + longDenCheckQuantity
                                        + " lồng đèn, bạn có muốn sử dụng 50 lồng đèn để trang trí cây không ?",
                                "Đồng ý", "Thôi khỏi");

                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                Item longDenCheck = null;
                Integer longDenCheckQuantity = 0;
                try {
                    longDenCheck = InventoryServiceNew.gI().findItemBag(player, 1237);
                    if (longDenCheck != null) {
                        longDenCheckQuantity = longDenCheck.quantity;
                    }
                } catch (Exception e) {
                    // throw new RuntimeException(e);
                }
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            // về trạm vũ trụ
                            case 0:
                                if (longDenCheckQuantity < 50) {
                                    Service.gI().sendThongBao(player,
                                            "|7|Bạn còn thiếu " + (50 - longDenCheckQuantity) + " lồng đèn nữa!");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                } else {
                                    int[] itemDos = new int[] { 1243, 1244, 1245, 1246, 1249, 1306 };
                                    int randomDo = new Random().nextInt(itemDos.length);
                                    Item ct = ItemService.gI().createNewItem((short) itemDos[randomDo]);
                                    ct.itemOptions.add(new ItemOption(5, 10));
                                    ct.itemOptions.add(new ItemOption(14, 5));
                                    ct.itemOptions.add(new ItemOption(50, 25));
                                    ct.itemOptions.add(new ItemOption(77, 25));
                                    ct.itemOptions.add(new ItemOption(103, 25));
                                    ct.itemOptions.add(new ItemOption(101, 25));
                                    ct.itemOptions.add(new ItemOption(93, 14));
                                    ct.itemOptions.add(new ItemOption(98, 25));
                                    ct.itemOptions.add(new ItemOption(108, 25));

                                    switch (ct.template.id) {
                                        case 1243:
                                        case 1244:
                                        case 1245:
                                        case 1246:
                                            ct.itemOptions.add(new ItemOption(29, 0));
                                            break;
                                        case 1249:
                                            ct.itemOptions.add(new ItemOption(117, 25));
                                            break;
                                    }
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, longDenCheck, 50);
                                    InventoryServiceNew.gI().addItemBag(player, ct);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "|7|Bạn nhận được " + ct.template.name);
                                }
                                break;
                            case 1:
                                break;

                        }
                    }

                }
            }
        };
    }

    public static Npc npclytieunuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                createOtherMenu(player, 0,
                        "Trò chơi Chọn ai đây đang được diễn ra, nếu bạn tin tưởng mình đang tràn đầy may mắn thì có thể tham gia thử",
                        "Thể lệ", "Chọn\nThỏi vàng");
            }

            @Override
            public void confirmMenu(Player pl, int select) {
                if (canOpenNpc(pl)) {
                    String time = ((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                    if (pl.iDMark.getIndexMenu() == 0) {
                        if (select == 0) {
                            createOtherMenu(pl, ConstNpc.IGNORE_MENU,
                                    "Thời gian giữa các giải là 5 phút\nKhi hết giờ, hệ thống sẽ ngẫu nhiên chọn ra 1 người may mắn.\nLưu ý: Số thỏi vàng nhận được sẽ bị nhà cái lụm đi 5%!Trong quá trình diễn ra khi đặt cược nếu thoát game mọi phần đặt đều sẽ bị hủy",
                                    "Ok");
                        } else if (select == 1) {
                            if (pl.getSession().actived) {
                                createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar
                                        + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0)
                                        + "%\nTổng giải VIP: "
                                        + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: "
                                        + pl.percentGold(1) + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar
                                        + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time,
                                        "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
                            } else {
                                Service.gI().sendThongBaoOK(pl, "Yêu cầu kích hoạt tài khoản!");
                            }
                        }
                    } else if (pl.iDMark.getIndexMenu() == 1) {
                        if (((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar
                                            + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0)
                                            + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip
                                            + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1)
                                            + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar
                                            + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time,
                                            "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
                                    break;
                                case 1: {
                                    if (InventoryServiceNew.gI().findItemBag(pl, 457).isNotNullItem()
                                            && InventoryServiceNew.gI().findItemBag(pl, 457).quantity >= 20) {
                                        InventoryServiceNew.gI().subQuantityItemsBag(pl,
                                                InventoryServiceNew.gI().findItemBag(pl, 457), 20);
                                        InventoryServiceNew.gI().sendItemBags(pl);
                                        pl.goldNormar += 20;
                                        ChonAiDay.gI().goldNormar += 20;
                                        ChonAiDay.gI().addPlayerNormar(pl);
                                        createOtherMenu(pl, 1,
                                                "Tổng giải thường: " + ChonAiDay.gI().goldNormar
                                                        + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0)
                                                        + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip
                                                        + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1)
                                                        + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar
                                                        + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP
                                                        + "\n Thời gian còn lại: " + time,
                                                "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
                                    } else {
                                        Service.getInstance().sendThongBao(pl, "Bạn không đủ thỏi vàng");
                                    }
                                }
                                    break;
                                case 2: {
                                    if (InventoryServiceNew.gI().findItemBag(pl, 457).isNotNullItem()
                                            && InventoryServiceNew.gI().findItemBag(pl, 457).quantity >= 200) {
                                        InventoryServiceNew.gI().subQuantityItemsBag(pl,
                                                InventoryServiceNew.gI().findItemBag(pl, 457), 200);
                                        InventoryServiceNew.gI().sendItemBags(pl);
                                        pl.goldVIP += 200;
                                        ChonAiDay.gI().goldVip += 200;
                                        ChonAiDay.gI().addPlayerVIP(pl);
                                        createOtherMenu(pl, 1,
                                                "Tổng giải thường: " + ChonAiDay.gI().goldNormar
                                                        + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0)
                                                        + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip
                                                        + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1)
                                                        + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar
                                                        + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP
                                                        + "\n Thời gian còn lại: " + time,
                                                "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
                                    } else {
                                        Service.getInstance().sendThongBao(pl, "Bạn không đủ thỏi vàng");
                                    }
                                }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc npclytieunuong54(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {

                if (canOpenNpc(player)) {
                    Item daSangTheCheck = null;
                    Integer daSangTheCheckQuantity = 0;
                    try {
                        daSangTheCheck = InventoryServiceNew.gI().findItemBag(player, 345);
                        if (daSangTheCheck != null) {
                            daSangTheCheckQuantity = daSangTheCheck.quantity;
                        }
                    } catch (Exception e) {
                        // throw new RuntimeException(e);
                    }
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xin chào, cậu đang có " + player.getSession().vnd + " số dư và " + daSangTheCheckQuantity
                                    + " đá sáng thế, cậu muốn quy đổi chứ ?",
                            "Donate", "Quy đổi");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    Service.getInstance().sendThongBaoOK(player,
                                            "Truy cập ngocrongrone.online -> Đăng nhập -> Donate hoặc\n" +
                                                    "MOMO: 0344870442 | MB Bank: 8870101072003 với lời nhắn: \n" +
                                                    "donate " + player.getSession().userId

                                                    + "\n Tối thiểu 20000VND \n 20K VND sẽ đổi được 20 đá sáng thế\n");
                                    break;
                                case 1:
                                    Item daSangThe = null;
                                    Integer daSangTheQuantity = 0;
                                    try {
                                        daSangThe = InventoryServiceNew.gI().findItemBag(player, 345);
                                        if (daSangThe != null) {
                                            daSangTheQuantity = daSangThe.quantity;
                                        }

                                    } catch (Exception e) {
                                        // throw new RuntimeException(e);
                                    }
                                    this.createOtherMenu(player, ConstNpc.QUY_DOI, "Bạn đang có : "
                                            + daSangTheQuantity + " Đá sáng thế \n"
                                            + "Tỉ lệ quy đổi là 1 đá sáng thế = 4 thỏi vàng\n"
                                            + "1 đá sáng thế = 2000 hồng ngọc\n"
                                            + "1000 số dư = " + Manager.QUY_DOI_SERVER + " đá sáng thế\n"
                                            + "|7|Sau khi quy đổi vui lòng đăng nhập lại, nếu không có sự cố xảy ra bạn sẽ chịu hoàn toàn trách nhiệm!",
                                            "Quy đổi\n Thỏi vàng", "Quy Đổi\nHồng Ngọc", "Quy đổi\nĐá sáng thế");
                                    break;
                            }
                            // }

                        } else if (player.iDMark.getIndexMenu() == ConstNpc.QUY_DOI) {
                            switch (select) {
                                case 0:
                                    Input.gI().createFormQDTV(player);
                                    break;
                                case 1:
                                    Input.gI().createFormQDHN(player);
                                    break;
                                case 2:
                                    Input.gI().createFormQDDST(player);
                                    break;
                            }
                        }
                    }
                }
            }

        };
    }

    public static Npc thuongDe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 45) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con muốn làm gì nào", "Con đường rắn độc", "Quay số\nmay mắn","Đến Kaio "
                                );
                    }
                    if (this.mapId == 49) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con muốn làm gì nào", "Quay về", "Từ chối");
                    }
                    if (this.mapId == 141) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con muốn làm gì nào", "Quay về", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 45) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 141, -1, 354);
                                    break;
                                case 1:
                                    this.createOtherMenu(player, ConstNpc.MENU_CHOOSE_LUCKY_ROUND,
                                            "Con muốn làm gì nào?", "Quay bằng\nvàng",
                                            "Rương phụ\n("
                                                    + (player.inventory.itemsBoxCrackBall.size()
                                                            - InventoryServiceNew.gI().getCountEmptyListItem(
                                                                    player.inventory.itemsBoxCrackBall))
                                                    + " món)",
                                            "Xóa hết\ntrong rương", "Đóng");
                                    break;
                                  case 2:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 48, -1, 354);
                                    break;
                               

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHOOSE_LUCKY_ROUND) {
                            switch (select) {
                                case 0:
                                    LuckyRound.gI().openCrackBallUI(player, LuckyRound.USING_GOLD);
                                    break;
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "ITEMS_LUCKY_ROUND", true);
                                    break;
                                case 2:
                                    NpcService.gI().createMenuConMeo(player,
                                            ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND, this.avartar,
                                            "Con có chắc muốn xóa hết vật phẩm trong rương phụ? Sau khi xóa "
                                                    + "sẽ không thể khôi phục!",
                                            "Đồng ý", "Hủy bỏ");
                                    break;
                            }
                        }
                    }

                }
                if (this.mapId == 49) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 45, -1, 354);
                                break;
                            case 1:

                                break;
                        }
                    }
                }
                if (this.mapId == 141) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 45, -1, 354);
                                break;
                            case 1:

                                break;
                        }
                    }
                }

            }

        };
    }

    public static Npc thanVuTru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con muốn làm gì nào", "Di chuyển");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, ConstNpc.MENU_DI_CHUYEN,
                                            "Con muốn đi đâu?", "Về\ncon đường rắn độc", "Thánh địa\nKaio",
                                            "Xuống địa ngục",
                                            "Từ chối");
                                    break;
                              
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DI_CHUYEN) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 143, -1, 1137, 336);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                    break;
                               
                            }
                        }
                    }
                }
            }

        };
    }

    public static Npc kibit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {

                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Hãy giúp chúng tôi chống lại Babiday ?",
                            "Đổi phe", "Từ chối");

                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                Service.gI().changeFlag(player, 9);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc babiday(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {

                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi có muốn gia nhập với bọn ta ?",
                            "Đổi phe", "Từ chối");

                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                Service.gI().changeFlag(player, 10);
                                break;
                        }
                    }

                }
            }
        };
    }

    public static Npc osin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Đến\nKaio", "Đến\nhành tinh\nBill", "Từ chối");
                    } else if (this.mapId == 154) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Về thánh địa", "Đến\nhành tinh\nngục tù", "Từ chối");
                    } else if (this.mapId == 155) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    } else if (this.mapId == 52) {
                        try {
                            MapMaBu.gI().setTimeJoinMapMaBu();
                            if (this.mapId == 52) {
                                long now = System.currentTimeMillis();
                                if (now > MapMaBu.TIME_OPEN_MABU && now < MapMaBu.TIME_CLOSE_MABU) {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_MMB, "Đại chiến Ma Bư đã mở, "
                                            + "ngươi có muốn tham gia không?",
                                            "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                                } else if (now > MapMaBu13h.TIME_OPEN_MABU_13H
                                        && now < MapMaBu13h.TIME_CLOSE_MABU_13H) {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_MABU_13H, "Đại chiến Ma Bư đã mở, "
                                            + "ngươi có muốn tham gia không?",
                                            "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_MMB,
                                            "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                }

                            }
                        } catch (Exception ex) {
                            Logger.error("Lỗi mở menu osin");
                        }

                    } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
                        if (player.fightMabu.pointMabu >= player.fightMabu.POINT_MAX) {
                            this.createOtherMenu(player, ConstNpc.GO_UPSTAIRS_MENU, "Ta có thể giúp gì cho ngươi ?",
                                    "Lên Tầng!", "Quay về", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                    "Quay về", "Từ chối");
                        }
                    } else if (this.mapId == 120) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                                    break;
                            }
                        }
                    } else if (this.mapId == 154) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (player.nPoint.power >= 80000000000L) {
                                        ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                    } else {
                                        Service.gI().sendThongBaoOK(player, "Yêu Cầu 80 Tỷ Sức Mạnh");
                                    }
                                    break;
                                case 1:
                                    if (player.nPoint.power >= 80000000000L) {
                                        ChangeMapService.gI().changeMap(player, 155, -1, 111, 792);
                                    } else {
                                        Service.gI().sendThongBaoOK(player, "Yêu Cầu 80 Tỷ Sức Mạnh");
                                    }
                                    break;
                            }
                        }
                    } else if (this.mapId == 155) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                if (player.nPoint.power >= 80000000000L) {
                                    ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                                } else {
                                    Service.gI().sendThongBaoOK(player, "Yêu Cầu 80 Tỷ Sức Mạnh");
                                }

                            }
                        }
                    } else if (this.mapId == 52) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.MENU_REWARD_MMB:
                                break;
                            case ConstNpc.MENU_OPEN_MMB:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                                } else if (select == 1) {
                                    if (!player.getSession().actived) {
                                        Service.gI().sendThongBao(player,
                                                "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                                    } else
                                        ChangeMapService.gI().changeMap(player, 114, Util.nextInt(0, 8), 318, 336);
                                }
                                break;
                            case ConstNpc.MENU_OPEN_MABU_13H:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                                } else if (select == 1) {
                                    if (!player.getSession().actived) {
                                        Service.gI().sendThongBao(player,
                                                "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                                    } else
                                        ChangeMapService.gI().changeMap(player, 144, Util.nextInt(0, 8), 181, 312);
                                }
                                break;
                            case ConstNpc.MENU_NOT_OPEN_BDW:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                                }
                                break;
                        }
                    } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.GO_UPSTAIRS_MENU) {
                            if (select == 0) {
                                player.fightMabu.clear();
                                ChangeMapService.gI().changeMap(player, this.map.mapIdNextMabu((short) this.mapId),
                                        Util.nextInt(0, 8),
                                        this.cx, this.cy);
                            } else if (select == 1) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        } else {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        }
                    } else if (this.mapId == 120) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc docNhan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.clan == null) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai", "Đóng");
                        return;
                    }
                    if (player.clan.doanhTrai_haveGone) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ta đã thả ngọc rồng ở tất cả các map,mau đi nhặt đi. Hẹn ngươi quay lại vào ngày mai",
                                "OK");
                        return;
                    }

                    boolean flag = true;
                    for (Mob mob : player.zone.mobs) {
                        if (!mob.isDie()) {
                            flag = false;
                        }
                    }
                    for (Player boss : player.zone.getBosses()) {
                        if (!boss.isDie()) {
                            flag = false;
                        }
                    }

                    if (flag) {
                        if (!player.clan.doanhTrai.timePickDragonBall) {
                            player.clan.doanhTrai_haveGone = true;
                            player.clan.doanhTrai.setLastTimeOpen(System.currentTimeMillis() + 290_000);
                            player.clan.doanhTrai.DropNgocRong();
                            for (Player pl : player.clan.membersInGame) {
                                ItemTimeService.gI().sendTextTime(pl, (byte) 0, "Doanh trại độc nhãn sắp kết thúc : ",
                                        300);
                            }
                            player.clan.doanhTrai.timePickDragonBall = true;
                            createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Ta đã thả ngọc rồng ở tất cả các map,mau đi nhặt đi. Hẹn ngươi quay lại vào ngày mai",
                                    "OK");
                        } else {
                            createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Đi nhặt ngọc mau lên", "OK");
                        }
                    } else {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy tiêu diệt hết quái và boss trong map", "OK");
                    }

                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_JOIN_DOANH_TRAI:
                            if (select == 0) {
                                DoanhTraiService.gI().joinDoanhTrai(player);
                            } else if (select == 2) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                        case ConstNpc.IGNORE_MENU:
                            if (select == 1) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc linhCanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.clan == null) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai", "Đóng");
                        return;
                    }
                    if (player.clan.getMembers().size() < DoanhTrai.N_PLAYER_CLAN) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bang hội phải có ít nhất 5 thành viên mới có thể mở", "Đóng");
                        return;
                    }
                    if (player.clan.doanhTrai != null) {
                        createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
                                "Bang hội của ngươi đang đánh trại độc nhãn\n"
                                        + "Thời gian còn lại là "
                                        + TimeUtil.getMinLeft(player.clan.doanhTrai.getLastTimeOpen(),
                                                DoanhTrai.TIME_DOANH_TRAI / 1000)
                                        + " phút . Ngươi có muốn tham gia không?",
                                "Tham gia", "Không", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    int nPlSameClan = 0;
                    for (Player pl : player.zone.getPlayers()) {
                        if (!pl.equals(player) && pl.clan != null
                                && pl.clan.equals(player.clan) && pl.location.x >= 1285
                                && pl.location.x <= 1645) {
                            nPlSameClan++;
                        }
                    }
                    if (nPlSameClan < DoanhTrai.N_PLAYER_MAP) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ngươi phải có ít nhất " + DoanhTrai.N_PLAYER_MAP
                                        + " đồng đội cùng bang đứng gần mới có thể\nvào\n"
                                        + "tuy nhiên ta khuyên ngươi nên đi cùng với 3-4 người để khỏi chết.\n"
                                        + "Hahaha.",
                                "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    if (player.clanMember.getNumDateFromJoinTimeToToday() < 0) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Doanh trại chỉ cho phép những người ở trong bang trên 1 ngày. Hẹn ngươi quay lại vào lúc khác",
                                "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    if (player.clan.haveGoneDoanhTrai) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bang hội của ngươi đã đi trại lúc "
                                        + TimeUtil.formatTime(player.clan.lastTimeOpenDoanhTrai, "HH:mm:ss")
                                        + " hôm nay. Người mở\n"
                                        + "(" + player.clan.playerOpenDoanhTrai + "). Hẹn ngươi quay lại vào ngày mai",
                                "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
                            "Hôm nay bang hội của ngươi chưa vào trại lần nào. Ngươi có muốn vào\n"
                                    + "không?\nĐể vào, ta khuyên ngươi nên có 3-4 người cùng bang đi cùng",
                            "Vào\n(miễn phí)", "Không", "Hướng\ndẫn\nthêm");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_JOIN_DOANH_TRAI:
                            if (select == 0) {
                                DoanhTraiService.gI().joinDoanhTrai(player);
                            } else if (select == 2) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                        case ConstNpc.IGNORE_MENU:
                            if (select == 1) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                    }
                }
            }
        };
    }

    //
    
    //

   

    public static Npc quocVuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "Con muốn nâng giới hạn sức mạnh cho bản thân hay đệ tử?",
                        "Bản thân", "Đệ tử", "Từ chối");
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                    this.createOtherMenu(player, ConstNpc.OPEN_POWER_MYSEFT,
                                            "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của bản thân lên "
                                                    + Util.numberToMoney(player.nPoint.getPowerNextLimit()),
                                            "Nâng\ngiới hạn\nsức mạnh",
                                            "Nâng ngay\n"
                                                    + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER)
                                                    + " vàng",
                                            "Đóng");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Sức mạnh của con đã đạt tới giới hạn",
                                            "Đóng");
                                }
                                break;
                            case 1:
                                if (player.pet != null) {
                                    if (player.pet.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                        this.createOtherMenu(player, ConstNpc.OPEN_POWER_PET,
                                                "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của đệ tử lên "
                                                        + Util.numberToMoney(player.pet.nPoint.getPowerNextLimit()),
                                                "Nâng ngay\n" + Util.numberToMoney(
                                                        OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " vàng",
                                                "Đóng");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Sức mạnh của đệ con đã đạt tới giới hạn",
                                                "Đóng");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Không thể thực hiện");
                                }
                                // giới hạn đệ tử
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_MYSEFT) {
                        switch (select) {
                            case 0:
                                OpenPowerService.gI().openPowerBasic(player);
                                break;
                            case 1:
                                if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                    if (OpenPowerService.gI().openPowerSpeed(player)) {
                                        player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                        Service.gI().sendMoney(player);
                                    }
                                } else {
                                    Service.gI().sendThongBao(player,
                                            "Bạn không đủ vàng để mở, còn thiếu "
                                                    + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER
                                                            - player.inventory.gold))
                                                    + " vàng");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_PET) {
                        if (select == 0) {
                            if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                if (OpenPowerService.gI().openPowerSpeed(player.pet)) {
                                    player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                    Service.gI().sendMoney(player);
                                }
                            } else {
                                Service.gI().sendThongBao(player,
                                        "Bạn không đủ vàng để mở, còn thiếu "
                                                + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER
                                                        - player.inventory.gold))
                                                + " vàng");
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc bulmaTL(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 102) {
                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu bé muốn mua gì nào?", "Cửa hàng",
                                    "Đóng");
                        }
                    } else if (this.mapId == 104) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Kính chào Ngài Linh thú sư!\nHãy tới Hành tinh thực vật để tìm trứng linh thú!",
                                "Cửa hàng",
                                "Ấp Trứng thiên thần", "Ấp Trứng hắc ám");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 102) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_FUTURE", true);
                            }
                        }
                    } else if (this.mapId == 104) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_LINHTHU", true);
                            }
                            if (select == 1) {
                                CombineServiceNew.gI().openTabCombine(player,
                                        CombineServiceNew.CREATE_TRUNG_THIEN_THAN);

                            }
                            if (select == 2) {
                                CombineServiceNew.gI().openTabCombine(player,
                                        CombineServiceNew.CREATE_TRUNG_HAC_AM);

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.CREATE_TRUNG_THIEN_THAN:
                                case CombineServiceNew.CREATE_TRUNG_HAC_AM:
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc rongOmega(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    BlackBallWar.gI().setTime();
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        try {
                            long now = System.currentTimeMillis();
                            if (now > BlackBallWar.TIME_OPEN && now < BlackBallWar.TIME_CLOSE) {
                                this.createOtherMenu(player, ConstNpc.MENU_OPEN_BDW,
                                        "Đường đến với ngọc rồng sao đen đã mở, "
                                                + "ngươi có muốn tham gia không?",
                                        "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                            } else {
                                String[] optionRewards = new String[7];
                                int index = 0;
                                for (int i = 0; i < 7; i++) {
                                    if (player.rewardBlackBall.timeOutOfDateReward[i] > System.currentTimeMillis()) {
                                        String quantily = player.rewardBlackBall.quantilyBlackBall[i] > 1
                                                ? "x" + player.rewardBlackBall.quantilyBlackBall[i] + " "
                                                : "";
                                        optionRewards[index] = quantily + (i + 1) + " sao";
                                        index++;
                                    }
                                }
                                if (index != 0) {
                                    String[] options = new String[index + 1];
                                    for (int i = 0; i < index; i++) {
                                        options[i] = optionRewards[i];
                                    }
                                    options[options.length - 1] = "Từ chối";
                                    this.createOtherMenu(player, ConstNpc.MENU_REWARD_BDW,
                                            "Ngươi có một vài phần thưởng ngọc "
                                                    + "rồng sao đen đây!",
                                            options);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_BDW,
                                            "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                }
                            }
                        } catch (Exception ex) {
                            Logger.error("Lỗi mở menu rồng Omega");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_REWARD_BDW:
                            player.rewardBlackBall.getRewardSelect((byte) select);
                            break;
                        case ConstNpc.MENU_OPEN_BDW:
                            if (select == 0) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                            } else if (select == 1) {
                                if (!player.getSession().actived) {
                                    Service.gI().sendThongBao(player,
                                            "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");

                                } else
                                    player.iDMark.setTypeChangeMap(ConstMap.CHANGE_BLACK_BALL);
                                ChangeMapService.gI().openChangeMapTab(player);
                            }
                            break;
                        case ConstNpc.MENU_NOT_OPEN_BDW:
                            if (select == 0) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                            }
                            break;
                    }
                }
            }

        };
    }

    public static Npc rong1_to_7s(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isHoldBlackBall()) {
                        this.createOtherMenu(player, ConstNpc.MENU_PHU_HP, "Ta có thể giúp gì cho ngươi?", "Phù hộ",
                                "Từ chối");
                    } else {
                        if (BossManager.gI().existBossOnPlayer(player)
                                || player.zone.items.stream()
                                        .anyMatch(itemMap -> ItemMapService.gI().isBlackBall(itemMap.itemTemplate.id))
                                || player.zone.getPlayers().stream().anyMatch(p -> p.iDMark.isHoldBlackBall())) {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME, "Ta có thể giúp gì cho ngươi?",
                                    "Về nhà", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME, "Ta có thể giúp gì cho ngươi?",
                                    "Về nhà", "Từ chối", "Gọi BOSS");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHU_HP) {
                        if (select == 0) {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_PHU_HP,
                                    "Ta sẽ giúp ngươi tăng HP lên mức kinh hoàng, ngươi chọn đi",
                                    "x3 HP\n" + Util.numberToMoney(BlackBallWar.COST_X3) + " vàng",
                                    "x5 HP\n" + Util.numberToMoney(BlackBallWar.COST_X5) + " vàng",
                                    "x7 HP\n" + Util.numberToMoney(BlackBallWar.COST_X7) + " vàng",
                                    "Từ chối");
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_GO_HOME) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                        } else if (select == 2) {
                            BossManager.gI().callBoss(player, mapId);
                        } else if (select == 1) {
                            this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PHU_HP) {
                        if (player.effectSkin.xHPKI > 1) {
                            Service.gI().sendThongBao(player, "Bạn đã được phù hộ rồi!");
                            return;
                        }
                        switch (select) {
                            case 0:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X3);
                                break;
                            case 1:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X5);
                                break;
                            case 2:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X7);
                                break;
                            case 3:
                                this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc npcThienSu64(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 14) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                                    + "\n 3. chi phí vào cổng  50 triệu vàng" +
                                    "\n 4. xong nhiệm vụ 36",
                            "Tới ngay", "Từ chối");
                }
                if (this.mapId == 7) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                                    + "\n 3. chi phí vào cổng  50 triệu vàng" +
                                    "\n 4. xong nhiệm vụ 36",
                            "Tới ngay", "Từ chối");
                }
                if (this.mapId == 0) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                                    + "\n 3. chi phí vào cổng  50 triệu vàng" +
                                    "\n 4. xong nhiệm vụ 36" + "\n Cậu có thể kiếm được bí kíp tuyệt kỹ tại đây.",
                            "Tới ngay", "Từ chối");
                }
                if (this.mapId == 146) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 147) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 148) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 48) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Đã tìm đủ nguyên liệu cho tôi chưa?\n Tôi sẽ giúp cậu mạnh lên kha khá đấy!", "Hướng Dẫn",
                            "Đổi Thức Ăn\nLấy Điểm", "Từ Chối","sss");
                }
                if (this.mapId == 154) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Đã tìm đủ nguyên liệu cho tôi chưa?\n Tôi sẽ giúp cậu mạnh lên kha khá đấy!",
                              "Nâng cấp tuyệt kỹ", 
                            "Luyện tập");
                }
            }

            // if (player.inventory.gold < 500000000) {
            // this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo
            // ít thôi con", "Đóng");
            // return;
            // }
            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu() && this.mapId == 7) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 80000000000L
                                    && player.inventory.gold > COST_HD && player.playerTask.taskMain.id > 36) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 146, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào,Yêu cầu 80 tỉ và xong nhiệm vụ 28");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 14) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 80000000000L
                                    && player.inventory.gold > COST_HD && player.playerTask.taskMain.id > 36) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 148, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 0) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 80000000000L
                                    && player.inventory.gold > COST_HD && player.playerTask.taskMain.id > 36) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 147, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 147) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 450);
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 148) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 14, -1, 450);
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 146) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 7, -1, 450);
                        }
                        if (select == 1) {
                        }

                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 48) {
                        if (select == 0) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "x99 Thức Ăn Được 1 Điểm");
                        }
                        if (select == 1) {
                            CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.DOI_DIEM);
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                        switch (player.combineNew.typeCombine) {
                            case CombineServiceNew.DOI_DIEM:

                                if (select == 0) {
                                    CombineServiceNew.gI().startCombine(player);
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHAN_RA_DO_THAN_LINH) {
                        if (select == 0) {
                            CombineServiceNew.gI().startCombine(player);
                        }

                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 154) {
                      
                       
                        if (select == 0) {
                            Message msg;
                            Item honLinhThu = null;
                            try {
                                honLinhThu = InventoryServiceNew.gI().findItemBag(player, 1215);
                            } catch (Exception e) {
                                // throw new RuntimeException(e);
                            }
                            try {
                                if (player.gender == 0) {
                                    Skill curSkill = SkillUtil.getSkillbyId(player, Skill.SUPER_KAME);
                                    if (curSkill.point == 0) {

                                        if (player.nPoint.power >= 60000000000L) {
                                            if (honLinhThu == null || honLinhThu.quantity < 99) {
                                                this.npcChat(player, "Bạn không đủ 99 Bí Kíp Tuyệt Kỹ");
                                            } else if (player.inventory.ruby > 100_000 && honLinhThu.quantity >= 99) {
                                                player.inventory.ruby -= 100_000;
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, honLinhThu, 99);
                                                Service.getInstance().sendMoney(player);
                                                curSkill = SkillUtil.createSkill(Skill.SUPER_KAME, 1);
                                                SkillUtil.setSkill(player, curSkill);
                                                msg = Service.getInstance().messageSubCommand((byte) 23);
                                                msg.writer().writeShort(curSkill.skillId);
                                                player.sendMessage(msg);
                                                msg.cleanup();
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Không đủ Hồng ngọc");
                                            }
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Yêu cầu sức mạnh trên 60 Tỷ");
                                        }
                                    } else if (curSkill.point > 0 && curSkill.point < 9) {
                                        if (player.inventory.ruby < 50_000) {
                                            Service.getInstance().sendThongBao(player, "Không đủ Hồng ngọc");
                                        } else if (honLinhThu != null && honLinhThu.quantity >= 99
                                                && player.inventory.ruby > 50_000) {
                                            player.inventory.ruby -= 50_000;
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, honLinhThu, 99);
                                            Service.getInstance().sendMoney(player);
                                            curSkill = SkillUtil.createSkill(Skill.SUPER_KAME, curSkill.point + 1);
                                            SkillUtil.setSkill(player, curSkill);
                                            msg = Service.getInstance().messageSubCommand((byte) 62);
                                            msg.writer().writeShort(curSkill.skillId);
                                            player.sendMessage(msg);
                                            msg.cleanup();
                                        } else {
                                            Service.getInstance().sendThongBao(player,
                                                    "Không đủ điều kiện, cần 99 bí kíp tuyệt kĩ");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player,
                                                "Tuyệt kĩ của bạn đã đạt cấp tối đa");
                                    }
                                }
                                 if (player.gender == 3) {
                                    Skill curSkill = SkillUtil.getSkillbyId(player, Skill.BIEN_BROLY);
                                    if (curSkill.point == 0) {

                                        if (player.nPoint.power >= 6000000000L) {
                                            if (honLinhThu == null || honLinhThu.quantity < 99) {
                                                this.npcChat(player, "Bạn không đủ 99 Bí Kíp Tuyệt Kỹ");
                                            } else if (player.inventory.ruby > 100_000 && honLinhThu.quantity >= 99) {
                                                player.inventory.ruby -= 100_000;
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, honLinhThu, 99);
                                                Service.getInstance().sendMoney(player);
                                                curSkill = SkillUtil.createSkill(Skill.BIEN_BROLY, 1);
                                                SkillUtil.setSkill(player, curSkill);
                                                msg = Service.getInstance().messageSubCommand((byte) 23);
                                                msg.writer().writeShort(curSkill.skillId);
                                                player.sendMessage(msg);
                                                msg.cleanup();
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Không đủ Hồng ngọc");
                                            }
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Yêu cầu sức mạnh trên 60 Tỷ");
                                        }
                                    } else if (curSkill.point > 0 && curSkill.point < 9) {
                                        if (player.inventory.ruby < 50_000) {
                                            Service.getInstance().sendThongBao(player, "Không đủ Hồng ngọc");
                                        } else if (honLinhThu != null && honLinhThu.quantity >= 99
                                                && player.inventory.ruby > 50_000) {
                                            player.inventory.ruby -= 50_000;
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, honLinhThu, 99);
                                            Service.getInstance().sendMoney(player);
                                            curSkill = SkillUtil.createSkill(Skill.BIEN_BROLY, curSkill.point + 1);
                                            SkillUtil.setSkill(player, curSkill);
                                            msg = Service.getInstance().messageSubCommand((byte) 62);
                                            msg.writer().writeShort(curSkill.skillId);
                                            player.sendMessage(msg);
                                            msg.cleanup();
                                        } else {
                                            Service.getInstance().sendThongBao(player,
                                                    "Không đủ điều kiện, cần 99 bí kíp tuyệt kĩ");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player,
                                                "Tuyệt kĩ của bạn đã đạt cấp tối đa");
                                    }
                                }
                                if (player.gender == 1) {
                                    Skill curSkill = SkillUtil.getSkillbyId(player, Skill.MA_PHONG_BA);
                                    if (curSkill.point == 0) {

                                        if (player.nPoint.power >= 60000000000L) {
                                            if (honLinhThu == null || honLinhThu.quantity < 99) {
                                                this.npcChat(player, "Bạn không đủ 99 Bí Kíp Tuyệt Kỹ");
                                            } else if (player.inventory.ruby > 100_000 && honLinhThu.quantity >= 99) {
                                                player.inventory.ruby -= 100_000;
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, honLinhThu, 99);
                                                Service.getInstance().sendMoney(player);
                                                curSkill = SkillUtil.createSkill(Skill.MA_PHONG_BA, 1);
                                                SkillUtil.setSkill(player, curSkill);
                                                msg = Service.getInstance().messageSubCommand((byte) 23);
                                                msg.writer().writeShort(curSkill.skillId);
                                                player.sendMessage(msg);
                                                msg.cleanup();
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Không đủ Hồng ngọc");
                                            }
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Yêu cầu sức mạnh trên 60 Tỷ");
                                        }
                                    } else if (curSkill.point > 0 && curSkill.point < 9) {
                                        if (player.inventory.ruby < 50_000) {
                                            Service.getInstance().sendThongBao(player, "Không đủ Hồng ngọc");
                                        } else if (honLinhThu != null && honLinhThu.quantity >= 99
                                                && player.inventory.ruby > 50_000) {
                                            player.inventory.ruby -= 50_000;
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, honLinhThu, 99);
                                            Service.getInstance().sendMoney(player);
                                            curSkill = SkillUtil.createSkill(Skill.MA_PHONG_BA, curSkill.point + 1);
                                            SkillUtil.setSkill(player, curSkill);
                                            msg = Service.getInstance().messageSubCommand((byte) 62);
                                            msg.writer().writeShort(curSkill.skillId);
                                            player.sendMessage(msg);
                                            msg.cleanup();
                                        } else {
                                            Service.getInstance().sendThongBao(player,
                                                    "Không đủ điều kiện, cần 99 bí kíp tuyệt kĩ");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player,
                                                "Tuyệt kĩ của bạn đã đạt cấp tối đa");
                                    }
                                }
                                if (player.gender == 2) {
                                    Skill curSkill = SkillUtil.getSkillbyId(player, Skill.LIEN_HOAN_CHUONG);
                                    if (curSkill.point == 0) {

                                        if (player.nPoint.power >= 60000000000L) {
                                            if (honLinhThu == null || honLinhThu.quantity < 99) {
                                                this.npcChat(player, "Bạn không đủ 99 Bí Kíp Tuyệt Kỹ");
                                            } else if (player.inventory.ruby > 100_000 && honLinhThu.quantity >= 99) {
                                                player.inventory.ruby -= 100_000;
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, honLinhThu, 99);
                                                Service.getInstance().sendMoney(player);
                                                curSkill = SkillUtil.createSkill(Skill.LIEN_HOAN_CHUONG, 1);
                                                SkillUtil.setSkill(player, curSkill);
                                                msg = Service.getInstance().messageSubCommand((byte) 23);
                                                msg.writer().writeShort(curSkill.skillId);
                                                player.sendMessage(msg);
                                                msg.cleanup();
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Không đủ Hồng ngọc");
                                            }
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Yêu cầu sức mạnh trên 60 Tỷ");
                                        }
                                    } else if (curSkill.point > 0 && curSkill.point < 9) {
                                        if (player.inventory.ruby < 50_000) {
                                            Service.getInstance().sendThongBao(player, "Không đủ Hồng ngọc");
                                        } else if (honLinhThu != null && honLinhThu.quantity >= 99
                                                && player.inventory.ruby > 50_000) {
                                            player.inventory.ruby -= 50_000;
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, honLinhThu, 99);
                                            Service.getInstance().sendMoney(player);
                                            curSkill = SkillUtil.createSkill(Skill.LIEN_HOAN_CHUONG,
                                                    curSkill.point + 1);
                                            SkillUtil.setSkill(player, curSkill);
                                            msg = Service.getInstance().messageSubCommand((byte) 62);
                                            msg.writer().writeShort(curSkill.skillId);
                                            player.sendMessage(msg);
                                            msg.cleanup();
                                        } else {
                                            Service.getInstance().sendThongBao(player,
                                                    "Không đủ điều kiện, cần 99 bí kíp tuyệt kĩ");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player,
                                                "Tuyệt kĩ của bạn đã đạt cấp tối đa");
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                        switch (player.combineNew.typeCombine) {
                            case CombineServiceNew.CHE_TAO_TRANG_BI_TS:
                            case CombineServiceNew.NANG_CAP_GAY_THIEN_SU:

                                if (select == 0) {
                                    CombineServiceNew.gI().startCombine(player);
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_CAP_DO_TS) {
                        if (select == 0) {
                            CombineServiceNew.gI().startCombine(player);
                        }

                    }
                }
            }

        };
    }

    public static Npc tapion(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 19) {
                        try {
                            MapSatan.gI().setTimeJoinMapSatan();
                            if (this.mapId == 19) {
                                long now = System.currentTimeMillis();
                                if (now > MapSatan.TIME_OPEN_SATAN && now < MapSatan.TIME_CLOSE_SATAN) {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_SANTA, "Đại chiến Hirudegan đã mở, "
                                            + "ngươi có muốn tham gia không?",
                                            "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_SANTA,
                                            "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Đổi huy hiệu", "Từ chối");
                                }

                            }
                        } catch (Exception ex) {
                            Logger.error("Lỗi mở menu tapion");
                        }

                    } else if (this.mapId == 149) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 19) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.MENU_REWARD_SANTA:
                                break;
                            case ConstNpc.MENU_OPEN_SANTA:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_SATAN);
                                } else if (select == 1) {
                                    if (!player.getSession().actived) {
                                        Service.gI().sendThongBao(player,
                                                "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                                    } else
                                        ChangeMapService.gI().changeMap(player, 149, Util.nextInt(0, 8), 163, 336);
                                }
                                break;
                            case ConstNpc.MENU_NOT_OPEN_BDW:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_SATAN);
                                }
                                if (select == 1) {
                                    if (player.nPoint.power >= 2000000000) {

                                        ShopServiceNew.gI().opendShop(player, "TAPION", false);
                                    } else {

                                        Service.gI().sendThongBaoOK(player,
                                                "Yêu cầu 2 tỉ sức mạnh");

                                    }
                                }
                                break;
                        }
                    } else if (this.mapId == 149) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        }
                    }
                }

            };

        };

    }

    public static Npc bill(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Gặp Whis Để Đổi Thức Ăn Lấy Điểm Sau Đó Gặp Ta Để Mua Trang Bị Hủy Diệt",
                                "Điểm",
                                "Shop Hủy Diệt", "Đóng"," sss");
                    } else if (this.mapId == 154) {
                        createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Gặp Whis Để Đổi Thức Ăn Lấy Điểm Sau Đó Gặp Ta Để Mua Trang Bị Hủy Diệt",
                                "Điểm",
                                "Shop Hủy Diệt");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (this.mapId) {
                        case 48:
                            switch (player.iDMark.getIndexMenu()) {
                                case ConstNpc.BASE_MENU:
                                    if (select == 0) {
                                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Mày Có " + player.inventory.coupon + " Điểm", "Đóng");
                                    }
                                    
                                    if (select == 1) {
                                        for (int id = 663; id <= 667; id++) {
                                   Item thucan = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, id);
     List<Item> itemsBody = player.inventory.itemsBody;
    int size = itemsBody.size();
    
    for (int i = 0; i < size; i++) {
        Item item = itemsBody.get(i);
        if (item.isNotNullItem()) {
            if (item.template.id >= 555 && item.template.id <= 567) {
                i++;
            }
      if (thucan == null && i < 5 ) {
    createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ngươi Không Đủ Thức Ăn Hoặc Chưa Mặc đồ thần", "Đóng");
      } else if (thucan != null&&thucan.quantity >= 99 && i == 5) {
    ShopServiceNew.gI().opendShop(player, "BILL", false);
    break;
}  
}}
}   }     }
                        case 154:
                       
                        if (select == 0) {
                            Message msg;
                            Item honLinhThu = null;
                            try {
                                honLinhThu = InventoryServiceNew.gI().findItemBag(player, 1215);
                            } catch (Exception e) {
                                // throw new RuntimeException(e);
                            }
                            try {
                             
                                 if (player.gender == 2) {
                                    Skill curSkill = SkillUtil.getSkillbyId(player, Skill.BIEN_BROLY);
                                   if (curSkill.point == 0) {

                                        if (player.nPoint.power >= 6000000000L) {
                                            if (honLinhThu == null || honLinhThu.quantity < 99) {
                                                this.npcChat(player, "Bạn không đủ 99 Bí Kíp Tuyệt Kỹ");
                                            } else if (player.inventory.ruby > 100_000 && honLinhThu.quantity >= 99) {
                                                player.inventory.ruby -= 100_000;
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, honLinhThu, 99);
                                                Service.getInstance().sendMoney(player);
                                                curSkill = SkillUtil.createSkill(Skill.BIEN_BROLY, 1);
                                                SkillUtil.setSkill(player, curSkill);
                                                msg = Service.getInstance().messageSubCommand((byte) 23);
                                                msg.writer().writeShort(curSkill.skillId);
                                                player.sendMessage(msg);
                                                msg.cleanup();
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Không đủ Hồng ngọc");
                                            }
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Yêu cầu sức mạnh trên 60 Tỷ");
                                        }
                                    } else if (curSkill.point > 0 && curSkill.point < 7) {
                                        if (player.inventory.ruby < 50_000) {
                                            Service.getInstance().sendThongBao(player, "Không đủ Hồng ngọc");
                                        } else if (honLinhThu != null && honLinhThu.quantity >= 99
                                                && player.inventory.ruby > 50_000) {
                                            player.inventory.ruby -= 50_000;
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, honLinhThu, 99);
                                            Service.getInstance().sendMoney(player);
                                            curSkill = SkillUtil.createSkill(Skill.BIEN_BROLY, curSkill.point + 1);
                                            SkillUtil.setSkill(player, curSkill);
                                            msg = Service.getInstance().messageSubCommand((byte) 62);
                                            msg.writer().writeShort(curSkill.skillId);
                                            player.sendMessage(msg);
                                            msg.cleanup();
                                        } else {
                                            Service.getInstance().sendThongBao(player,
                                                    "Không đủ điều kiện, cần 99 bí kíp tuyệt kĩ");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player,
                                                "Tuyệt kĩ của bạn đã đạt cấp tối đa");
                                    }
                                
                               
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        }    
                    
                }
                
            }
        };
    }

    public static Npc boMong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 47 || this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                             "|2|Làm Nhiệm Vụ Đi\b|7|Nhiệm Vụ Càng Khó Thưởng Càng Nhìu"
                                +"\n|1|Nhiệm vụ dễ + 10tr vàng và 1 điểm năng lượng kích hoạt"
                                 +"\nNhiệm vụ bình thường + 20tr vàng và 1 điểm năng lượng kích hoạt"
                                +"\nNhiệm vụ khó + 30tr vàng và 2 điểm năng lượng kích hoạt"
                                 +"\nNhiệm vụ siêu khó + 40tr vàng và 2 điểm năng lượng kích hoạt"
                                         +"\n Nhiệm vụ địa ngục + 50tr vàng và 3 điểm năng lượng kích hoạt"
                                +"\n|7|Lưu ý khi làm nhiệm vụ càng khó sẽ có tỷ lệ nhận đá ma thuật cao hơn", "Nhiệm vụ\nhàng ngày",
                                "Thành tựu",
                                "Từ chối");
                    }
                    // if (this.mapId == 47) {
                    // this.createOtherMenu(player, ConstNpc.BASE_MENU,
                    // "Xin chào, cậu muốn tôi giúp gì?", "Từ chối");
                    // }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 47 || this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (player.playerTask.sideTask.template != null) {
                                        String npcSay = "Nhiệm vụ hiện tại: " + player.playerTask.sideTask.getName()
                                                + " ("
                                                + player.playerTask.sideTask.getLevel() + ")"
                                                + "\nHiện tại đã hoàn thành: " + player.playerTask.sideTask.count + "/"
                                                + player.playerTask.sideTask.maxCount + " ("
                                                + player.playerTask.sideTask.getPercentProcess()
                                                + "%)\nSố nhiệm vụ còn lại trong ngày: "
                                                + player.playerTask.sideTask.leftTask + "/" + ConstTask.MAX_SIDE_TASK;
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_PAY_SIDE_TASK,
                                                npcSay, "Trả nhiệm\nvụ", "Hủy nhiệm\nvụ");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK,
                                                "Tôi có vài nhiệm vụ theo cấp bậc, "
                                                        + "sức cậu có thể làm được cái nào?",
                                                "Dễ", "Bình thường", "Khó", "Siêu khó", "Địa ngục", "Từ chối");
                                    }
                                    break;
                                case 1:
                                    Service.gI().sendThongBao(player, "Tích cực quay tay, vận may sẽ đến");
                                    player.achievement.Show();
                                    break;
                              
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK) {
                            switch (select) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                    TaskService.gI().changeSideTask(player, (byte) select);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PAY_SIDE_TASK) {
                            switch (select) {
                                case 0:
                                    TaskService.gI().paySideTask(player);
                                    break;
                                case 1:
                                    TaskService.gI().removeSideTask(player);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.DANH_HIEU) {
                            if (select == 0) {
                                if (player.lastTimeTitle > 0) {
                                    Service.gI().removeTitle(player);
                                    player.isTitleUse = !player.isTitleUse;
                                    Service.gI().point(player);
                                    Service.gI().sendThongBao(player,
                                            "Đã " + (player.isTitleUse == true ? "Bật" : "Tắt") + " Danh Hiệu!");
                                }
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.BAT_TAT_HOP_THE) {
                            if (select == 0) {
                                player.isShowCTHT = !player.isShowCTHT;
                                Service.gI().Send_Caitrang(player);
                                Service.gI().sendThongBao(player, "Thành công!");
                            }
                        }
                    }
                }
            }
        };
    }
public static Npc boMong1(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 218 ) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi đang còn: " + player.pointSukien + "Điểm lấp lánh" +
                                "\n|2|Làm Nhiệm Vụ Đi\b|7|Ngươi sẽ nhận được điểm lấp lánh và vàng"
                                +"\nTại đây sẽ xuất hiện tên boss granola hãy tiêu diệt hắn"
                                +"\n Phần quà khi tiêu diệt hắn bao gồm:"
                                +"\n3 điểm lấp lánh và có tỷ lệ nhận được vé lấp lánh và búa chế tác" 
                                , "Nhiệm vụ\nhàng ngày","Cửa hàng","Đổi vé lấp lánh"
                                );
                    }
                 
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 218) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                 case 0:
                                    if (player.playerTask.sideTask.template1 != null) {
                                        String npcSay = "Nhiệm vụ hiện tại: " + player.playerTask.sideTask.getName1() + " ("
                                                + player.playerTask.sideTask.getLevel() + ")"
                                                + "\nHiện tại đã hoàn thành: " + player.playerTask.sideTask.count + "/"
                                                + player.playerTask.sideTask.maxCount + " ("
                                                + player.playerTask.sideTask.getPercentProcess() + "%)\nSố nhiệm vụ còn lại trong ngày: "
                                                + player.playerTask.sideTask.leftTask + "/" + ConstTask.MAX_SIDE_TASK;
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_PAY_SIDE_TASK1,
                                                npcSay, "Trả nhiệm\nvụ", "Hủy nhiệm\nvụ");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK1,
                                                "Tôi có vài nhiệm vụ theo cấp bậc, "
                                                + "sức cậu có thể làm được cái nào?",
                                                "Nhận nhiệm vụ", "Từ chối");
                                    }
                                    break;
                                     case 1: //shop
                                    ShopServiceNew.gI().opendShop(player, "LAP_LANH", false);
                                    break;
                                     case 2: //shop
                                    this.createOtherMenu(player, 1611, "|2|Đổi vé lấp lánh cần:"
                                            + "\n10 điểm lấp lánh"
                                            + "\n10tr vàng"
                                          , "Đổi", "Hủy");

                                    break;
                               
                            }
                        } 
                        else if (player.iDMark.getIndexMenu() == 1611) {
                            switch (select) {
                                case 0:
                                   
                                        if (player.pointSukien >= 10) {
                                            if (player.inventory.gold >= 10000000) {
                                               
                                                player.pointSukien -= 10;
                                                    Item item = ItemService.gI().createNewItem((short) 1472);
                                       
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.getInstance().sendThongBao(player, "Bạn đã đổi thành công vé lấp lánh");
                                            } else {
                                                Service.gI().sendThongBao(player, "Yêu cầu 10 điểm lấp lánh");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Bạn không đủ 10tr vàng");
                                        }
                                    
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK1) {
                            switch (select) {
                                case 0:

                                    TaskService.gI().changeSideTask1(player, (byte) select);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PAY_SIDE_TASK1) {
                            switch (select) {
                                case 0:
                                    TaskService.gI().paySideTask1(player);
                                    break;
                                case 1:
                                    TaskService.gI().removeSideTask(player);
                                    break;
                            }
                        } 
                    }
                }
            }
        };
    }
    /**
     * KI LAN
     */
    public static Npc lucy(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                new Thread(() -> {
                    try {
                        while (true) {
                            String[] textLucy = { "|7|serizawa.store", "|7|Chào mừng đến với NRO RONE !",
                                    "|7|boyrock!" };
                            Thread.sleep(5000);
                            new Thread(() -> {
                                try {
                                    Thread.sleep(3000);
                                    this.npcChat(player, textLucy[Util.nextInt(0, textLucy.length - 1)]);
                                } catch (Exception e) {
                                }
                            }).start();
                        }
                    } catch (Exception e) {
                    }
                }).start();
                if (canOpenNpc(player)) {

                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào mừng đến với NRO RONE ! Cậu muốn tìm hiểu gì?",
                            "Sự kiện", "Vàng", "Hồng ngọc", "Cơ chế trang bị", "Phó bản", "Leo tháp",
                            "Đệ tử", "Cải trang", "Boss khung giờ", "Hộ tống", "Võ đài", "Bông tai", "Gacha",
                            "Đua top");
                }

            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_SU_KIEN);
                                break;

                            case 1:
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_VANG);
                                break;
                            case 2:
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_HONG_NGOC);
                                break;
                            case 3:
                                NpcService.gI().createTutorial(player, this.avartar,
                                        ConstNpc.HUONG_DAN_CO_CHE_TRANG_BI);
                                break;
                            case 4:
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_PHO_BAN);
                                break;
                            case 5:
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_LEO_THAP);
                                break;
                            case 6:
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DE_TU);
                                break;
                            case 7:
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_CAI_TRANG);
                                break;
                            case 8:
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BOSS_KHUNG_GIO);
                                break;
                            case 9:
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_HO_TONG);
                                break;
                            case 10:
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_HO_TONG);
                                break;
                            case 11:
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BONG_TAI);
                                break;
                            case 12:
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_GACHA);
                                break;
                            case 13:
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DUA_TOP);
                                break;

                            default:
                                this.npcChat(player, " Đọc đi mấy cưng ");
                                break;
                        }
                    }

                }
            }
        };
    }

    public static Npc kiLan(int mapId, int status, int cx, int cy, int tempId, int avartar) {

        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                int mapCur = 0;
                switch (this.mapId) {
                    case 6:
                        mapCur = 42;
                        break;
                    case 10:
                        mapCur = 43;
                        break;
                    case 19:
                        mapCur = 44;
                        break;
                    default:
                        mapCur = 0;
                        break;

                }
                if (canOpenNpc(player)) {

                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Mải chơi quá nên bé lạc cmnr đường về rồi, đại ca có thể hộ tống bé về "
                                    + MapService.gI().getMapById(mapCur).mapName + " được không?",
                            "Oke", "Cửa hàng", "Kích hoạt chỉ số",
                            "Không");
                }

            }

            @Override
            public void confirmMenu(Player player, int select) {
                int mapCur = 0;
                switch (this.mapId) {
                    case 6:
                        mapCur = 42;
                        break;
                    case 10:
                        mapCur = 43;
                        break;
                    case 19:
                        mapCur = 44;
                        break;
                    default:
                        mapCur = 0;
                        break;

                }
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                          
                            case 1:
                                this.npcChat(player, "Mại zô mại zô!");
                                ShopServiceNew.gI().opendShop(player, "KI_LAN", true);
                                break;
                            case 2:
                                CombineServiceNew.gI().openTabCombine(player,
                                        CombineServiceNew.MO_CHI_SO_CAI_TRANG);
                                break;
                            case 3:
                                Service.gI().sendThongBao(player, "Hu hu");
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                        switch (player.combineNew.typeCombine) {
                            case CombineServiceNew.MO_CHI_SO_CAI_TRANG:
                                if (select == 0) {
                                    CombineServiceNew.gI().startCombine(player);
                                }
                                break;
                        }
                    }

                }
            }
        };
    }

    public static Npc toSuKaio(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {

                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|2|Con muốn thách đấu với ta ?", 
                            "Không");
                }

            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            
                            case 0:
                                Service.gI().sendThongBao(player, "Luyện tập thêm rồi quay lại gặp ta nhé");
                                // player.achievement.Show();
                                break;
                        }
                    }

                }
            }
        };
    }

    public static Npc karin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        Service.gI().sendThongBaoOK(player, "Mùa hè đã kết thúc, hẹn gặp lại vào năm sau nhé!");
                        // if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        // this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        // "-Sự Kiện Hè-\n Sao Biển: Ngọc Rồng 1-7 Sao Ngẫu Nhiên\nCon Cua: Cải Trang
                        // Chỉ Số 25-30% Có Thể Vĩnh Viễn\nVò Sò: Pet Ngãu Nhiên\n Vỏ Ốc: Cánh ngẫu
                        // nhiên \n Cả 4 loại + 10 thỏi vàng: Cải trang đặc biệt ngẫu nhiên",
                        // "Sao Biển", "Con Cua", "Vỏ Sò", "Vỏ Ốc", "Cả 4 và 10 thỏi vàng");
                        // }
                    } else if (this.mapId == 46) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Con muốn khiêu chiến với ta sao ?",
                                
                                "Thôi sợ");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                Item SaoBien = null;
                                try {
                                    SaoBien = InventoryServiceNew.gI().findItemBag(player, 698);
                                } catch (Exception e) {
                                    // throw new RuntimeException(e);
                                }
                                if (SaoBien == null || SaoBien.quantity < 99) {
                                    this.npcChat(player, "Bạn không đủ 99 sao biển");
                                } else if (player.inventory.gold < 1_000_000_000) {
                                    this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                } else {
                                    player.inventory.gold -= 1_000_000_000;
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, SaoBien, 99);
                                    Service.gI().sendMoney(player);
                                    Item ngocrong = ItemService.gI().createNewItem((short) Util.nextInt(14, 20));
                                    InventoryServiceNew.gI().addItemBag(player, ngocrong);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    this.npcChat(player, "Bạn nhận được ngọc rồng");
                                }
                            }
                            if (select == 1) {
                                Item ConCua = null;
                                try {
                                    ConCua = InventoryServiceNew.gI().findItemBag(player, 697);
                                } catch (Exception e) {
                                    // throw new RuntimeException(e);
                                }
                                if (ConCua == null || ConCua.quantity < 99) {
                                    this.npcChat(player, "Bạn không đủ 99 vật phẩm sự kiện");
                                } else if (player.inventory.gold < 1_000_000_000) {
                                    this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                } else {
                                    player.inventory.gold -= 1_000_000_000;
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, ConCua, 99);
                                    Service.gI().sendMoney(player);
                                    Item caitrang = ItemService.gI().createNewItem((short) (876 + player.gender));
                                    caitrang.itemOptions.add(new Item.ItemOption(50, Util.nextInt(35, 40)));
                                    caitrang.itemOptions.add(new Item.ItemOption(77, Util.nextInt(35, 40)));
                                    caitrang.itemOptions.add(new Item.ItemOption(103, Util.nextInt(35, 40)));
                                    if (Util.isTrue(99, 100)) {
                                        caitrang.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 10)));
                                    }
                                    InventoryServiceNew.gI().addItemBag(player, caitrang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    this.npcChat(player, "Bạn nhận được cải trang");
                                }
                            }
                            if (select == 2) {
                                Item VoSo = null;
                                try {
                                    VoSo = InventoryServiceNew.gI().findItemBag(player, 696);
                                } catch (Exception e) {
                                    // throw new RuntimeException(e);
                                }
                                if (VoSo == null || VoSo.quantity < 99) {
                                    this.npcChat(player, "Bạn không đủ 99 vật phẩm sự kiện");
                                } else if (player.inventory.gold < 1_000_000_000) {
                                    this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                } else {
                                    player.inventory.gold -= 1_000_000_000;
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, VoSo, 99);
                                    Service.gI().sendMoney(player);
                                    Item pet = ItemService.gI().createNewItem((short) Util.nextInt(2085, 2090));
                                    pet.itemOptions.add(new Item.ItemOption(50, Util.nextInt(25, 30)));
                                    pet.itemOptions.add(new Item.ItemOption(77, Util.nextInt(25, 30)));
                                    pet.itemOptions.add(new Item.ItemOption(103, Util.nextInt(25, 30)));
                                    if (Util.isTrue(99, 100)) {
                                        pet.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 10)));
                                    }
                                    InventoryServiceNew.gI().addItemBag(player, pet);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    this.npcChat(player, "Bạn nhận được pet");
                                }
                            }
                            if (select == 3) {
                                Item VoOc = null;
                                try {
                                    VoOc = InventoryServiceNew.gI().findItemBag(player, 695);
                                } catch (Exception e) {
                                    // throw new RuntimeException(e);
                                }
                                if (VoOc == null || VoOc.quantity < 99) {
                                    this.npcChat(player, "Bạn không đủ 99 vật phẩm sự kiện");
                                } else if (player.inventory.gold < 1_000_000_000) {
                                    this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                } else {
                                    player.inventory.gold -= 1_000_000_000;
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, VoOc, 99);
                                    Service.gI().sendMoney(player);
                                    int[] itemDos = new int[] { 2085, 2086, 2087, 2088, 2089, 2090, 1107, 967 };
                                    int randomDo = new Random().nextInt(itemDos.length);
                                    Item canh = ItemService.gI().createNewItem((short) itemDos[randomDo]);
                                    canh.itemOptions.add(new Item.ItemOption(50, Util.nextInt(25, 30)));
                                    canh.itemOptions.add(new Item.ItemOption(77, Util.nextInt(25, 30)));
                                    canh.itemOptions.add(new Item.ItemOption(103, Util.nextInt(25, 30)));
                                    if (Util.isTrue(99, 100)) {
                                        canh.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 10)));
                                    }
                                    InventoryServiceNew.gI().addItemBag(player, canh);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    this.npcChat(player, "Bạn nhận được cánh");
                                }
                            }
                            if (select == 4) {
                                Item VoOc = null;
                                Item VoSo = null;
                                Item SaoBien = null;
                                Item ConCua = null;
                                Item ThoiVang = null;
                                try {
                                    VoOc = InventoryServiceNew.gI().findItemBag(player, 695);
                                    VoSo = InventoryServiceNew.gI().findItemBag(player, 696);
                                    ConCua = InventoryServiceNew.gI().findItemBag(player, 697);
                                    SaoBien = InventoryServiceNew.gI().findItemBag(player, 698);
                                    ThoiVang = InventoryServiceNew.gI().findItemBag(player, 457);
                                } catch (Exception e) {
                                    //
                                }
                                if (VoOc == null || VoOc.quantity < 99) {
                                    this.npcChat(player, "Bạn không đủ 99 vật phẩm vỏ ốc");
                                } else if (VoSo == null || VoSo.quantity < 99) {
                                    this.npcChat(player, "Bạn không đủ 99 vật phẩm vỏ sò");
                                } else if (SaoBien == null || SaoBien.quantity < 99) {
                                    this.npcChat(player, "Bạn không đủ 99 vật phẩm sao biển");
                                } else if (ConCua == null || ConCua.quantity < 99) {
                                    this.npcChat(player, "Bạn không đủ 99 vật phẩm con cua");
                                } else if (ThoiVang == null || ThoiVang.quantity < 10) {
                                    this.npcChat(player, "Bạn không đủ 10 Thỏi vàng");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                } else {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, VoOc, 99);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, VoSo, 99);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, SaoBien, 99);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, ConCua, 99);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, ThoiVang, 99);
                                    Service.gI().sendMoney(player);
                                    int[] itemDos = new int[] { 580, 581, 582, 583 };
                                    int randomDo = new Random().nextInt(itemDos.length);
                                    Item canh = ItemService.gI().createNewItem((short) itemDos[randomDo]);
                                    canh.itemOptions.add(new Item.ItemOption(50, Util.nextInt(25, 30)));
                                    canh.itemOptions.add(new Item.ItemOption(77, Util.nextInt(25, 30)));
                                    canh.itemOptions.add(new Item.ItemOption(103, Util.nextInt(25, 30)));
                                    canh.itemOptions.add(new Item.ItemOption(33, 0));
                                    if (Util.isTrue(99, 100)) {
                                        canh.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 10)));
                                    }
                                    InventoryServiceNew.gI().addItemBag(player, canh);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    this.npcChat(player, "Bạn nhận được cánh");
                                }
                            }
                        }

                    } 
                        }
                    }
                
            
        };
    }

    public static Npc vados(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|2|Ta Vừa Hắc Mắp Xêm Được Tóp Của Toàn Server\b|7|Người Muốn Xem Tóp Gì?",
                            "Tóp Sức Mạnh", "Top Nhiệm Vụ", "Top Tiêu Sài", "Top Ngũ Hành Sơn", "Top săn Boss", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (this.mapId) {
                        case 5:
                            switch (player.iDMark.getIndexMenu()) {
                                case ConstNpc.BASE_MENU:
                                    if (select == 0) {
                                        Service.gI().showListTop(player, Manager.topSM);
                                        break;
                                    }
                                    if (select == 1) {
                                        Service.gI().showListTop(player, Manager.topNV);
                                        break;
                                    }
                                    if (select == 2) {
                                        Service.gI().showListTop(player, Manager.topPVP);
                                        break;
                                    }
                                    if (select == 3) {
                                        Service.gI().showListTop(player, Manager.topNHS);
                                        break;
                                    }
                                    if (select == 4) {
                                        Service.gI().showListTop(player, Manager.topBoss);
                                        break;

                                    }
                                    break;
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc gokuSSJ_1(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {

                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (this.mapId == 80) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, tôi có thể giúp gì cho cậu?",
                                    "Tới hành tinh\nYardart", "Từ chối");
                        } else if (this.mapId == 131) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, tôi có thể giúp gì cho cậu?",
                                    "Quay về", "Từ chối");
                        } else {
                            super.openBaseMenu(player);
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.BASE_MENU:
                            if (this.mapId == 80) {
                                if (select == 0) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 131, -1, 870);
                                }
                            }
                            break;
                    }
                }
            }
        };
    }

//    public static Npc mavuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 153) {
//                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                                "Xin chào, tôi có thể giúp gì cho cậu?", "Tây thánh địa", "Nhiệm vụ bang hội",
//                                "Chức năng bang hội", "Từ chối");
//                    } else if (this.mapId == 156) {
//                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                                "Người muốn trở về?", "Quay về", "Từ chối");
//                    }
//                }
//            }
//
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 153) {
//                        if (player.iDMark.isBaseMenu()) {
//                            switch (select) {
//                                case 0:
//                                    ChangeMapService.gI().changeMapBySpaceShip(player, 156, -1, 360);
//                                    break;
//                                case 1:
//                                    Clan clan2 = player.clan;
//                                    if (clan2 != null) {
//                                        if (player.playerTask.clanTask.template != null) {
//                                            String npcSay = "Nhiệm vụ hiện tại: " + player.playerTask.clanTask.getName()
//                                                    + " ("
//                                                    + player.playerTask.clanTask.getLevel() + ")"
//                                                    + "\nHiện tại đã hoàn thành: " + player.playerTask.clanTask.count
//                                                    + "/"
//                                                    + player.playerTask.clanTask.maxCount + " ("
//                                                    + player.playerTask.clanTask.getPercentProcess()
//                                                    + "%)\nSố nhiệm vụ còn lại trong ngày: "
//                                                    + player.playerTask.clanTask.leftTask + "/"
//                                                    + ConstTask.MAX_CLAN_TASK;
//                                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_PAY_SIDE_TASK,
//                                                    npcSay, "Trả nhiệm\nvụ", "Hủy nhiệm\nvụ");
//                                        } else {
//                                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK,
//                                                    "Tôi có vài nhiệm vụ theo cấp bậc, "
//                                                            + "sức cậu có thể làm được cái nào?",
//                                                    "Dễ", "Bình thường", "Khó", "Siêu khó", "Địa ngục", "Từ chối");
//                                        }
//                                    } else {
//                                        Service.gI().sendThongBao(player, "Cậu chưa có bang hội !");
//                                    }
//                                    break;
//                                case 2:
//                                    Clan clan = player.clan;
//                                    if (clan != null) {
//                                        ClanMember cm = clan.getClanMember((int) player.id);
//                                        if (cm != null) {
//                                            if (clan.members.size() == 1) {
//                                                Service.gI().sendThongBao(player, "Bang phải nhiều hơn một người");
//                                                break;
//                                            }
//                                            if (!clan.isLeader(player)) {
//                                                Service.gI().sendThongBao(player, "Phải là bảng chủ");
//                                                break;
//                                            }
//                                            //
//                                            this.createOtherMenu(player, ConstNpc.MENU_CHU_BANG,
//                                                    "Bang hội của cậu đang có " + player.clan.capsuleClan
//                                                            + " capsule bang.\nCậu muốn làm gì?",
//                                                    "Nâng cấp\nbang hội", "Từ chối");
//                                        }
//                                        break;
//                                    }
//                                    Service.gI().sendThongBao(player, "Có bang hội đâu ba!!!");
//                                    break;
//                            }
//                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK) {
//                            switch (select) {
//                                case 0:
//                                case 1:
//                                case 2:
//                                case 3:
//                                case 4:
//                                    TaskService.gI().changeClanTask(player, (byte) select);
//                                    break;
//                            }
//                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PAY_SIDE_TASK) {
//                            switch (select) {
//                                case 0:
//                                    TaskService.gI().payClanTask(player);
//                                    break;
//                                case 1:
//                                    TaskService.gI().removeClanTask(player);
//                                    break;
//                            }
//                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHU_BANG) {
//                            switch (select) {
//                                case 0:
//                                    this.createOtherMenu(player, ConstNpc.MENU_NANG_CAP_BANG,
//                                            "Cậu có muốn sử dụng " + (100 + player.clan.level * 100)
//                                                    + " capsule bang để nâng cấp bang hội?\nSau khi nâng cấp bang, số lượng thành viên sẽ tăng lên 1 và tăng thêm 10% sức mạnh tiềm năng cho thành viên khi đánh quái",
//                                            "Đồng ý", "Từ chối");
//                                    break;
//                                case 1:
//
//                                    break;
//                            }
//                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_CAP_BANG) {
//                            switch (select) {
//                                case 0:
//                                    Clan clan = player.clan;
//                                    if (clan != null) {
//                                        ClanMember cm = clan.getClanMember((int) player.id);
//                                        if (cm != null) {
//                                            if (clan.members.size() == 1) {
//                                                Service.gI().sendThongBao(player, "Bang phải nhiều hơn một người");
//                                                break;
//                                            }
//                                            if (!clan.isLeader(player)) {
//                                                Service.gI().sendThongBao(player, "Phải là bảng chủ");
//                                                break;
//                                            }
//                                            //
//                                            if (player.clan.capsuleClan < (100 + player.clan.level * 100)) {
//                                                Service.gI().sendThongBao(player, "Clan còn thiếu "
//                                                        + ((100 + player.clan.level * 100) - player.clan.capsuleClan)
//                                                        + " capsule bang nữa!");
//                                                break;
//                                            }
//                                            if (player.clan.level >= 10) {
//                                                Service.gI().sendThongBao(player, "Bang hội đã đạt cấp tối đa");
//                                                break;
//                                            }
//                                            player.clan.level++;
//                                            player.clan.maxMember++;
//                                            player.clan.capsuleClan -= 100 + player.clan.level * 100;
//                                            Service.gI().sendThongBao(player, "Bang hội của cậu đã tăng lên 1 cấp!");
//                                            ClanService.gI().sendMyClan(player);
//                                            ClanService.gI().sendClanId(player);
//                                            break;
//
//                                        }
//                                        break;
//                                    }
//                                    Service.gI().sendThongBao(player, "Có bang hội đâu ba!!!");
//                                    break;
//                                case 1:
//                                    break;
//
//                            }
//                        }
//                    } else if (this.mapId == 156) {
//                        if (player.iDMark.isBaseMenu()) {
//                            switch (select) {
//                                // về lanh dia bang hoi
//                                case 0:
//                                    ChangeMapService.gI().changeMapBySpaceShip(player, 153, -1, 432);
//                                    break;
//                            }
//                        }
//                    }
//                }
//            }
//        };
//    }

    public static Npc cauca(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {

                if (canOpenNpc(player)) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào anh, bọn em đang cần 1 số lượng cá rất lớn. Anh có thể giúp bọn em câu cá được không?",
                            "Hướng dẫn", "Cửa hàng\nDụng cụ", "Của hàng\nCá", "Nâng cấp cần câu", "Sửa cần câu");
                }

            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_CAU_CA);
                                break;
                            case 1:
                                ShopServiceNew.gI().opendShop(player, "DUNG_CU_CAU_CA", true);
                                break;
                            case 2:
                                ShopServiceNew.gI().opendShop(player, "BAN_CA", true);
                                break;
                            case 3:
                                CombineServiceNew.gI().openTabCombine(player,
                                        CombineServiceNew.NANG_CAP_CAN_CAU);
                                break;
                            case 4:
                                CombineServiceNew.gI().openTabCombine(player,
                                        CombineServiceNew.SUA_CHUA_CAN_CAU);
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                        switch (player.combineNew.typeCombine) {

                            case CombineServiceNew.SUA_CHUA_CAN_CAU:
                            case CombineServiceNew.NANG_CAP_CAN_CAU:
                                if (select == 0) {
                                    CombineServiceNew.gI().startCombine(player);
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc cauca2(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {

                if (canOpenNpc(player)) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Anh muốn xem bảng xếp hạng câu cá chứ ?\n" +
                            "Anh đang có " + player.pointCauCa + " điểm câu cá!",
                            "Oke", "Thôi");
                }

            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                Service.gI().showListTop(player, Manager.topCauCa);
                                break;
                            case 1:

                                break;

                        }
                    }
                }
            }
        };
    }

    public static Npc gokuSSJ_2(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {

                if (canOpenNpc(player)) {
                    {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Bạn đang có 0 bí kiếp.\n"
                                + "Hãy kiếm đủ 10000 bí kiếp tôi sẽ dạy bạn cách dịch chuyển tức thời của người Yardart\nBí kiếp kiếm tại hành tinh Yadart",
                                "Học dịch\nchuyển", "Đóng");
                    }
                    try {
                        Item biKiep = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 590);
                        if (biKiep != null) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Bạn đang có " + biKiep.quantity
                                    + " bí kiếp.\n"
                                    + "Hãy kiếm đủ 10000 bí kiếp tôi sẽ dạy bạn cách dịch chuyển tức thời của người Yardart\nBí kiếp kiếm tại hành tinh Yadart",
                                    "Học dịch\nchuyển", "Đóng");
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();

                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    try {

                        Item biKiep = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 590);
                        if (biKiep != null) {
                            if (biKiep.quantity >= 10000 && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                Item yardart = ItemService.gI().createNewItem((short) (player.gender + 592));
                                yardart.itemOptions.add(new Item.ItemOption(47, 400));
                                yardart.itemOptions.add(new Item.ItemOption(108, 10));
                                InventoryServiceNew.gI().addItemBag(player, yardart);
                                InventoryServiceNew.gI().subQuantityItemsBag(player, biKiep, 10000);
                                InventoryServiceNew.gI().sendItemBags(player);
                                Service.gI().sendThongBao(player, "Bạn vừa nhận được trang phục tộc Yardart");
                            }
                        }
                    } catch (Exception ex) {

                    }
                }
            }
        };
    }

    public static Npc khidaumoi(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 14) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Bạn muốn nâng cấp khỉ ư?", "Nâng cấp\nkhỉ", "Shop của Khỉ", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 14) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, 1,
                                            "|7|Cần Khỉ Lv1 hoặc 2,4,6 để nâng cấp lên lv8\b|2|Mỗi lần nâng cấp tiếp thì mỗi cấp cần thêm 10 đá ngũ sắc",
                                            "Khỉ\ncấp 2",
                                            "Khỉ\ncấp 4",
                                            "Khỉ\ncấp 6",
                                            "Khỉ\ncấp 8",
                                            "Từ chối");
                                    break;
                                case 1: // shop
                                    ShopServiceNew.gI().opendShop(player, "KHI", false);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 1) { // action đổi dồ húy diệt
                            switch (select) {
                                case 0: // trade
                                    try {
                                        Item dns = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 674);
                                        Item klv1 = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1137);
                                        int soLuong = 0;
                                        if (dns != null) {
                                            soLuong = dns.quantity;
                                        }
                                        for (int i = 0; i < 12; i++) {
                                            Item klv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag,
                                                    1137 + i);

                                            if (InventoryServiceNew.gI().isExistItemBag(player, 1137 + i)
                                                    && soLuong >= 20) {
                                                CombineServiceNew.gI().khilv2(player, 1138 + i);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, dns, 20);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, klv, 1);
                                                this.npcChat(player, "Upgrede Thành Công!");

                                                break;
                                            } else {
                                                this.npcChat(player,
                                                        "Yêu cầu cần cái trang khỉ cấp 1 với 20 đá ngũ sắc");
                                            }

                                        }
                                    } catch (Exception e) {

                                    }
                                    break;
                                case 1: // trade
                                    try {
                                        Item dns = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 674);
                                        Item klv2 = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1138);
                                        int soLuong = 0;
                                        if (dns != null) {
                                            soLuong = dns.quantity;
                                        }
                                        for (int i = 0; i < 12; i++) {
                                            Item klv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag,
                                                    1138 + i);

                                            if (InventoryServiceNew.gI().isExistItemBag(player, 1138 + i)
                                                    && soLuong >= 30) {
                                                CombineServiceNew.gI().khilv3(player, 1139 + i);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, dns, 30);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, klv, 1);
                                                this.npcChat(player, "Upgrede Thành Công!");

                                                break;
                                            } else {
                                                this.npcChat(player,
                                                        "Yêu cầu cần cái trang khỉ cấp 2 với 30 đá ngũ sắc");
                                            }

                                        }
                                    } catch (Exception e) {

                                    }
                                    break;
                                case 2: // trade
                                    try {
                                        Item dns = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 674);
                                        Item klv2 = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1139);
                                        int soLuong = 0;
                                        if (dns != null) {
                                            soLuong = dns.quantity;
                                        }
                                        for (int i = 0; i < 12; i++) {
                                            Item klv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag,
                                                    1139 + i);

                                            if (InventoryServiceNew.gI().isExistItemBag(player, 1139 + i)
                                                    && soLuong >= 40) {
                                                CombineServiceNew.gI().khilv4(player, 1140 + i);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, dns, 40);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, klv, 1);
                                                this.npcChat(player, "Upgrede Thành Công!");

                                                break;
                                            } else {
                                                this.npcChat(player,
                                                        "Yêu cầu cần cái trang khỉ cấp 3 với 40 đá ngũ sắc");
                                            }

                                        }
                                    } catch (Exception e) {

                                    }
                                    break;
                                case 3: // trade
                                    try {
                                        Item dns = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 674);
                                        Item klv2 = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1140);
                                        int soLuong = 0;
                                        if (dns != null) {
                                            soLuong = dns.quantity;
                                        }
                                        for (int i = 0; i < 12; i++) {
                                            Item klv = InventoryServiceNew.gI().findItem(player.inventory.itemsBag,
                                                    1140 + i);

                                            if (InventoryServiceNew.gI().isExistItemBag(player, 1140 + i)
                                                    && soLuong >= 50) {
                                                CombineServiceNew.gI().khilv5(player, 1136 + i);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, dns, 50);
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, klv, 1);
                                                this.npcChat(player, "Upgrede Thành Công!");

                                                break;
                                            } else {
                                                this.npcChat(player,
                                                        "Yêu cầu cần cái trang khỉ cấp 3 với 50 đá ngũ sắc");
                                            }

                                        }
                                    } catch (Exception e) {

                                    }
                                    break;

                                case 5: // canel
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc GhiDanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            String[] menuselect = new String[] {};

            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (this.mapId == 52) {
                        createOtherMenu(pl, 0, DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).Giai(pl),
                                "Thông tin\nChi tiết",
                                DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).CanReg(pl) ? "Đăng ký"
                                        : "OK",
                                "Đại Hội\nVõ Thuật\nLần thứ\n23");
                    } else if (this.mapId == 129) {
                        // int goldchallenge = pl.goldChallenge;
                        if (pl.levelWoodChest == 0) {
                            menuselect = new String[] { "Thi đấu\n" + 200 + " Hồng ngọc", "Về\nĐại Hội\nVõ Thuật" };
                        } else {
                            menuselect = new String[] { "Thi đấu\n" + 200 + " Hồng ngọc",
                                    "Nhận thưởng\nRương cấp\n" + pl.levelWoodChest, "Về\nĐại Hội\nVõ Thuật" };
                        }
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                "Đại hội võ thuật lần thứ 23\nDiễn ra bất kể ngày đêm,ngày nghỉ ngày lễ\nPhần thưởng vô cùng quý giá\nNhanh chóng tham gia nào",
                                menuselect, "Từ chối");

                    } else {
                        super.openBaseMenu(pl);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 52) {
                        switch (select) {
                            case 0:
                                Service.gI().sendPopUpMultiLine(player, tempId, avartar, DaiHoiVoThuat.gI().Info());
                                break;
                            case 1:
                                if (DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).CanReg(player)) {
                                    DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).Reg(player);
                                }
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapNonSpaceship(player, 129, player.location.x, 360);
                                break;
                        }
                    } else if (this.mapId == 129) {
                        // int goldchallenge = player.goldChallenge;
                        if (player.levelWoodChest == 0) {
                            switch (select) {
                                case 0:
                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
                                        if (player.inventory.ruby >= 200) {
                                            MartialCongressService.gI().startChallenge(player);
                                            player.inventory.ruby -= 200;
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.goldChallenge += 2000000;
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(200 - player.inventory.ruby) + " Hồng ngọc");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
                                    break;
                            }
                        } else {
                            switch (select) {
                                case 0:
                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
                                        if (player.inventory.ruby >= 200) {
                                            MartialCongressService.gI().startChallenge(player);
                                            player.inventory.ruby -= (200);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.goldChallenge += 200;
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(200 - player.inventory.ruby) + " vàng");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 1:
                                    if (!player.receivedWoodChest) {
                                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                            Item it = ItemService.gI().createNewItem((short) 570);
                                            it.itemOptions.add(new Item.ItemOption(72, player.levelWoodChest));
                                            it.itemOptions.add(new Item.ItemOption(30, 0));
                                            it.createTime = System.currentTimeMillis();
                                            InventoryServiceNew.gI().addItemBag(player, it);
                                            InventoryServiceNew.gI().sendItemBags(player);

                                            player.receivedWoodChest = true;
                                            player.levelWoodChest = 0;
                                            Service.getInstance().sendThongBao(player, "Bạn nhận được rương gỗ");
                                        } else {
                                            this.npcChat(player, "Hành trang đã đầy");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player,
                                                "Mỗi ngày chỉ có thể nhận rương báu 1 lần");
                                    }
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc unkonw(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, 0,
                                "Éc éc Bạn muốn gì ở tôi :3?", "Đến Võ đài Unknow", "Võ Đài Siêu Cấp");

                    }
                    if (this.mapId == 112) {
                        this.createOtherMenu(player, 0,
                                "Bạn đang còn : " + player.pointPvp + " điểm PvP Point", "Về đảo Kame",
                                "Đổi Cải trang sự kiên", "Top PVP");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.getIndexMenu() == 0) { //
                            switch (select) {
                                case 0:
                                    if (player.getSession().player.nPoint.power >= 10000000000L) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 112, -1, 495);
                                        Service.gI().changeFlag(player, Util.nextInt(8));
                                    } else {
                                        this.npcChat(player, "Bạn cần 10 tỷ sức mạnh mới có thể vào");
                                    }
                                    break; // qua vo dai
                                case 1:
                                    if (player.getSession().player.nPoint.power >= 10000000000L) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 145, -1, 495);
                                        Service.gI().changeFlag(player, Util.nextInt(8));
                                    } else {
                                        this.npcChat(player, "Bạn cần 10 tỷ sức mạnh mới có thể vào");
                                    }
                                    break; // qua vo dai

                            }
                        }
                    }

                    if (this.mapId == 112) {
                        if (player.iDMark.getIndexMenu() == 0) { //
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 319);
                                    break; // ve dao kame
                                case 1: //
                                    this.createOtherMenu(player, 1,
                                            "Bạn có muốn đổi 500 điểm PVP lấy \n|6|Cải trang Mèo Kid Lân với tất cả chỉ số là 80%\n ",
                                            "Ok", "Không");
                                    // bat menu doi item
                                    break;

                                case 2: //
                                    Service.gI().showListTop(player, Manager.topPVP);
                                    // mo top pvp
                                    break;

                            }
                        }
                        if (player.iDMark.getIndexMenu() == 1) { // action doi item
                            switch (select) {
                                case 0: // trade
                                    if (player.pointPvp >= 500) {
                                        player.pointPvp -= 500;
                                        Item item = ItemService.gI().createNewItem((short) (1104));
                                        item.itemOptions.add(new Item.ItemOption(49, 30));
                                        item.itemOptions.add(new Item.ItemOption(77, 15));
                                        item.itemOptions.add(new Item.ItemOption(103, 20));
                                        item.itemOptions.add(new Item.ItemOption(207, 0));
                                        item.itemOptions.add(new Item.ItemOption(33, 0));
                                        //
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        Service.gI().sendThongBao(player, "Chúc Mừng Bạn Đổi Cải Trang Thành Công !");
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Không đủ điểm bạn còn " + (500 - player.pointPvp) + " Điểm nữa");
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

  public static Npc monaito(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 7) {
                        this.createOtherMenu(player, 0,
                                "Chào bạn tôi sẽ đưa bạn đến hành tinh Cereal? \n Với điều kiện hoàn thành nhiệm vụ 30 và đạt 60 tỉ sức mạnh.",
                                "Đồng ý", "Cửa hàng\n flag bag", "Nâng cấp\n chân mệnh", "Linh luyện\nCải trang",
                                "Tẩy tinh luyện", "Từ chối");
                    }
                    if (this.mapId == 10) {
                        this.createOtherMenu(player, 0,
                                "Mặt trăng ở trái đất đã bị Picolo phá hủy rồi, cư dân trên mặt trăng đã di cư sang mặt trăng mới.\n"
                                        +
                                        "Tôi cũng vừa tìm được vị trí hành tinh của Broly mới trong giải ngân hà.\n Cậu muốn di chuyển tới ",
                                "Mặt trăng", "Hành tinh Broly","Halloween", "Từ chối");
                    }
                    if (this.mapId == 170) {
                        this.createOtherMenu(player, 0,
                                "Ta ở đây để đưa con về", "Về Làng Mori", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 7) {
                        if (player.iDMark.getIndexMenu() == 0) { //
                            switch (select) {
                                case 0:
                                    if (player.playerTask.taskMain.id > 30 && player.nPoint.power >= 60000000000L) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 170, -1, 264);
                                    } else {
                                        Service.gI().sendThongBao(player,
                                                "Cậu yếu như thế này sao có thể giải cứu được hành tinh Cereal?");
                                    }
                                    break; // den hanh tinh cereal
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "MONAITO", true);
                                    break;
                                case 2:
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.NANG_CAP_CHAN_MENH);
                                    break;
                                case 3:
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.TINH_LUYEN_CAI_TRANG);
                                    break;
                                case 4:
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.TAY_TINH_LUYEN_CAI_TRANG);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.NANG_CAP_CHAN_MENH:
                                case CombineServiceNew.TINH_LUYEN_CAI_TRANG:
                                case CombineServiceNew.TAY_TINH_LUYEN_CAI_TRANG:
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }
                        }
                    }
                    if (this.mapId == 10) {
                        if (player.iDMark.getIndexMenu() == 0) { //
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 204, -1, 600);
                                    break; // quay ve
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 209, -1, 600);
                                    break; // quay ve
                                case 2:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 208, -1, 600);
                                    break; // quay ve

                            }
                        }
                    }
                    if (this.mapId == 170) {
                        if (player.iDMark.getIndexMenu() == 0) { //
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 7, -1, 432);
                                    break; // quay ve

                            }
                        }
                    }
                }
            }
        };
    }

  public static Npc billbingo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Khu vực nghĩa trang đã mở, cậu có muốn đến không ?",
                                "Vào nghĩa trang", "Chế tạo kẹo", "Cửa hàng sự kiện", "Top sự kiện");

                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            // về trạm vũ trụ
                            case 0:
                                if (player.getSession().actived) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 208, -1, 100);
                                } else {
                                    Service.gI().sendThongBao(player, "Yêu cầu kích hoạt tài khoản!");
                                }
                                break;
                            case 1:
                                this.createOtherMenu(player, 1,
                                        "|7|Bó kẹo: x99 Kẹo Phù thủy, não, bí ngô + x1 giấy gói + x1 dây buộc.\n" +
                                                "|7|Giỏ kẹo: x99 Kẹo Phù thủy, não, bí ngô + x1 giỏ đựng + x1 dây buộc. \n"
                                                +
                                                "|7|Hộp kẹo: 5 Bó kẹo và 5 Giỏ kẹo + x99 kẹo ma trơi. \n",
                                        "Làm bó kẹo",
                                        "Làm giỏ kẹo", "Làm hộp kẹo");
                                break;
                            case 2:
                                ShopServiceNew.gI().opendShop(player, "SHOP_HALLOWEEN", true);
                                break;
                            case 3:
                                Service.gI().showListTop(player, Manager.topSuKien);
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 1) {
                        Item KeoNao = null;
                        Item KeoBi = null;
                        Item KeoPhuThuy = null;
                        Item DayBuoc = null;
                        Item GioKeo = null;
                        Item GiayGoi = null;
                        Item KeoMaTroi = null;
                        Item BoKeoMaQuai = null;
                        Item GioKeoMaQuai = null;
                        try {
                            KeoNao = InventoryServiceNew.gI().findItemBag(player, 1439);
                            KeoBi = InventoryServiceNew.gI().findItemBag(player, 1441);
                            KeoPhuThuy = InventoryServiceNew.gI().findItemBag(player, 1440);
                            DayBuoc = InventoryServiceNew.gI().findItemBag(player, 1442);
                            GioKeo = InventoryServiceNew.gI().findItemBag(player, 1443);
                            GiayGoi = InventoryServiceNew.gI().findItemBag(player, 1444);
                            KeoMaTroi = InventoryServiceNew.gI().findItemBag(player, 1448);
                            BoKeoMaQuai = InventoryServiceNew.gI().findItemBag(player, 1445);
                            GioKeoMaQuai = InventoryServiceNew.gI().findItemBag(player, 1446);
                        } catch (Exception e) {
                            // throw new RuntimeException(e);
                        }
                        switch (select) {
                            // về trạm vũ trụ
                            case 0:
                                if (KeoNao == null || KeoBi == null || KeoPhuThuy == null || DayBuoc == null
                                        || GiayGoi == null || KeoNao.quantity < 99 ||
                                        KeoBi.quantity < 99 ||
                                        KeoPhuThuy.quantity < 99 ||
                                        DayBuoc.quantity < 1 ||
                                        GiayGoi.quantity < 1) {
                                    this.npcChat(player, "Bạn không đủ nguyên liệu");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành trang của cưng không đủ chỗ trống");
                                } else {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, KeoNao, 99);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, KeoBi, 99);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, KeoPhuThuy, 99);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, DayBuoc, 1);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, GiayGoi, 1);
                                    Service.gI().sendMoney(player);

                                    Item ct = ItemService.gI().createNewItem((short) 1445);
                                    ct.itemOptions.add(new ItemOption(30, 0));
                                    InventoryServiceNew.gI().addItemBag(player, ct);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn nhận được bó kẹo ma quái!");
                                }
                                break;
                            case 1:
                                if (KeoNao == null || KeoBi == null || KeoPhuThuy == null || DayBuoc == null
                                        || GioKeo == null || KeoNao.quantity < 99 ||
                                        KeoBi.quantity < 99 ||
                                        KeoPhuThuy.quantity < 99 ||
                                        DayBuoc.quantity < 1 ||
                                        GioKeo.quantity < 1) {
                                    this.npcChat(player, "Bạn không đủ nguyên liệu");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành trang của cưng không đủ chỗ trống");
                                } else {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, KeoNao, 99);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, KeoBi, 99);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, KeoPhuThuy, 99);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, DayBuoc, 1);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, GioKeo, 1);
                                    Service.gI().sendMoney(player);

                                    Item ct = ItemService.gI().createNewItem((short) 1446);
                                    ct.itemOptions.add(new ItemOption(30, 0));
                                    InventoryServiceNew.gI().addItemBag(player, ct);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn nhận được giỏ kẹo ma quái !");
                                }
                                break;

                            case 2:
                                if (BoKeoMaQuai == null || GioKeoMaQuai == null || KeoMaTroi == null
                                        || BoKeoMaQuai.quantity < 5 || GioKeoMaQuai.quantity < 5
                                        || KeoMaTroi.quantity < 99) {
                                    this.npcChat(player, "Bạn không đủ nguyên liệu");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành trang của cưng không đủ chỗ trống");
                                } else {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, BoKeoMaQuai, 5);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, GioKeoMaQuai, 5);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, KeoMaTroi, 99);

                                    Service.gI().sendMoney(player);

                                    Item ct = ItemService.gI().createNewItem((short) 1447);
                                    ct.itemOptions.add(new ItemOption(30, 0));
                                    InventoryServiceNew.gI().addItemBag(player, ct);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn nhận được hộp kẹo đặc biệt !");
                                }
                                break;
                        }
                    }

                }
            }
        };
    }
   public static Npc ultraGoku(int mapId, int status, int cx, int cy, int tempId, int avartar) {
    return new Npc(mapId, status, cx, cy, tempId, avartar) {
        @Override
        public void openBaseMenu(Player player) {
            if (canOpenNpc(player)) {
                if (this.mapId == 14) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào bạn tôi sẽ đưa bạn đến hành tinh Thiên sứ? \n Bạn có thể tìm được mảnh đồ thiên sứ tại đây.",
                            "Đồng ý");
                }
                if (this.mapId == 174) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ta ở đây để đưa cậu về", "Học tuyệt kỹ", "Nâng cấp sách kỹ năng","Mở nội tại sách","Xóa nội tại");
                }
                if (this.mapId == 176) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Cậu đã đủ sẵn sàng chưa?", "Đổi đồ", "Từ chối");
                }
            }
        }

        @Override
        public void confirmMenu(Player player, int select) {
            if (canOpenNpc(player)) {
                if (this.mapId == 14) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.BASE_MENU:
                            if (select == 0) {
                                if (player.getSession().actived && player.nPoint.power > 120000000000L
                                        && player.playerTask.taskMain.id > 40) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 174, 0, 90);
                                } else {
                                    Service.gI().sendThongBao(player,
                                            "Vui lòng kích hoạt tài khoản, hoàn thành nhiệm vụ 40, và đạt sức mạnh trên 120 tỉ để sử dụng chức năng này");
                                }
                            }
                            break;
                    }
                } else if (player.iDMark.isBaseMenu() && this.mapId == 174) {
                   if (select == 0) {
                            if (player.nPoint.power >= 2000000000) {
                                ShopServiceNew.gI().opendShop(player, "ULTRA", false);
                            }
                } else if (this.mapId == 176) {
                    if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                        if (select == 0) {
                            if (player.nPoint.power >= 2000000000) {
                                ShopServiceNew.gI().opendShop(player, "ULTRA", false);
                            } else {
                                Service.gI().sendThongBaoOK(player,
                                        "Anh zai yếu sinh lý quá, đủ 2 tỉ sức mạnh quay lại gặp em nhé");
                            }
                        }
                    }
                }
                }
            }
        }
    
            
        
    };
            
}



    public static Npc granala(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "|1|Xin Chào,Sự Kiện 10/3 Đang Diễn Ra Các Cư Dân Có Thể Đổi Item Vip ở Đây nhé"
                                        + "\n|3|Tách Ngọc Bội Lấy Điểm Sk"
                                        + "\n|3|Đổi Công Thức Chế Tạo Đồ Thiên Sứ"
                                        + "\n|3|Sử Dụng Ngọc Bội Để Đổi Random Item c2"
                                        + "\n|3|Sử Dụng Điểm Sự Kiện Đổi Cải Trang Vip random Có Vĩnh Viễn"
                                        + "\n|3|Thử Vận May Ra NGọc Rồng vip Tỉ Lệ Cao "
                                        + "\n|6|Ngoài Ra Các Bạn Có Thể Trồng Dưa Hấu,Hãy Chat'duahau' để nhận dưa trồng",
                                "Tách Ngọc Bội Lấy Điểm Sk", "Xem Điểm Sk", "Đổi Công Thức", "Đổi item Cấp 2",
                                "Đổi Cải Trang", "Thử Vận May Ngọc Vip", "Tặng Dưa Hấu Cho Vua Hùng", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: // phân rã đồ thần linh
                                    CombineServiceNew.gI().openTabCombine(player,
                                            CombineServiceNew.PHAN_RA_DO_THAN_LINH);
                                    break;
                                case 1:
                                    this.createOtherMenu(player, ConstNpc.NAP_THE,
                                            "|2|Khó Đã Có Mbbank :3 \nNgươi đang có: " + player.inventory.coupon
                                                    + " điểm sự kiện",
                                            "Đóng");
                                    break;
                                case 2:
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DOI_DIEM_DUA, -1,
                                            "Đổi Công Thức Chế Tạo Đồ Thiên Sứ?\nTa Cần 200 điểm sự kiện đấy... ",
                                            "Đồng ý", "Từ chối");
                                    break;
                                case 3:
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DOI_DIEM_ITEMC2, -1,
                                            "Ta Sẽ Cho Con Item siêu cấp ngẫu nhiên?\nTa Cần 100 Điểm Sự Kiện... ",
                                            "Đồng ý", "Từ chối");
                                    break;
                                case 4:
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DOI_DIEM_CT, -1,
                                            "Cần 999 Điểm Sự Kiện Để Lấy Cải Trang Random \nCó Tỉ Lệ May Mắn Được Vĩnh Viễn...Thử Ngay Nào ",
                                            "Đồng ý", "Từ chối");
                                    break;
                                case 5:
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DOI_ITEM_NR, -1,
                                            "Còn Thở Còn Gỡ Còn Điểm Còn Đổi ..?\nPhải giao cho ta 200 điểm sự kiện đấy...\nNếu May Mắn Sẽ Nhận Được Đồ Thiên Sứ jiren Và Nro Víp 1 Sao ",
                                            "Đồng ý", "Từ chối");
                                    break;
                                case 6:
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_GIAO_BONG, -1,
                                            "Dưa Hấu Ngoài Biển Đã Bị Ngươi Cướp ..?\nHãy Giao Dưa Hấu Để Nhận x1 Rương kho Báu của Ta...\nCần 1 Quả Dưa... ",
                                            "Đồng ý", "Từ chối");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.PHAN_RA_DO_THAN_LINH:
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHAN_RA_DO_THAN_LINH) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                        }
                    }
                }
            }
        };
    }

    private static Npc popo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.getSession().is_gift_box) {
                        // this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào con, con muốn ta giúp
                        // gì nào?", "Giải tán bang hội", "Nhận quà\nđền bù");
                    } else {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Thượng Đế vừa phát hiện 1 loại khí đang âm thầm\nhủy diệt mọi mầm sống trên Trái Đất,\nnó được gọi là Destron Gas.\nTa sẽ đưa các cậu đến nơi ấy, các cậu sẵn sàng chưa?",
                                "Thông tin chi tiết", "Top 100 bang hội", "OK", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:

                                break;
                            case 1:
                                break;

                            case 2:
                                if (player.clan != null) {
                                    if (player.clan.KhiGaHuyDiet != null) {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPENED_KGHD,
                                                "Bang hội của con đang đi khí ga hủy diệt cấp độ "
                                                        + player.clan.KhiGaHuyDiet.level
                                                        + "\nCon có muốn đi theo không?",
                                                "Đồng ý", "Từ chối");
                                    } else {

                                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_KGHD,
                                                "Đây là khí ga hủy diệt \nCác con cứ yên tâm lên đường\n"
                                                        + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                                "Chọn\ncấp độ", "Từ chối");
                                    }
                                } else {
                                    this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                                }
                                break;
                            case 3:

                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_KGHD) {
                        switch (select) {
                            case 0:
                                if (player.nPoint.power >= KhiGasHuyDiet.POWER_CAN_GO_TO_KGHD) {
                                    ChangeMapService.gI().goToKGHD(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(KhiGasHuyDiet.POWER_CAN_GO_TO_KGHD));
                                }
                                break;

                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_KGHD) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() && player.clanMember.getNumDateFromJoinTimeToToday() >= 2
                                        || player.nPoint.power >= KhiGasHuyDiet.POWER_CAN_GO_TO_KGHD
                                                && player.clanMember.getNumDateFromJoinTimeToToday() >= 2) {
                                    Input.gI().createFormChooseLevelKGHD(player);
                                }
                                if (player.clanMember.getNumDateFromJoinTimeToToday() < 2) {
                                    Service.gI().sendThongBao(player, "Yêu cầu tham gia bang hội trên 2 ngày!");
                                } else if (player.clan.haveGoneKhiGaHuyDiet) {
                                    createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Bang hội của ngươi đã đi bản đồ khí gas lúc "
                                                    + TimeUtil.formatTime(player.clan.lastTimeOpenKhiGasHuyDiet,
                                                            "HH:mm:ss")
                                                    + " hôm nay. Người mở\n"
                                                    + "(). Hẹn ngươi quay lại vào ngày mai",
                                            "OK", "Hướng\ndẫn\nthêm");
                                    return;

                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(KhiGasHuyDiet.POWER_CAN_GO_TO_KGHD));
                                }
                                break;
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_KGHD) {
                        switch (select) {
                            case 0:
                                KhiGasHuyDietService.gI().openKhiGaHuyDiet(player,
                                        Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                break;
                        }
                    }
                }
            };
        }

        ;
    }

    public static Npc createNPC(int mapId, int status, int cx, int cy, int tempId) {
        int avatar = Manager.NPC_TEMPLATES.get(tempId).avatar;
        try {
            switch (tempId) {
                case ConstNpc.DIEM_VUONG:
                    return diemVuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CAY_LONG_DEN:
                    return cayLongDen(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BULMA_NM:
                    return bulmaNm(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.FIDE:
                    return fideNm(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LY_TIEU_NUONG_CHON_AI_DAY:
                    return npclytieunuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LUCY:
                    return lucy(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.KI_LAN:
                    return kiLan(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TO_SU_KAIO:
                    return toSuKaio(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.MI_NUONG_2:
                    return minuong2(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.UNKOWN:
                    return unkonw(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.POC:
                    return poc(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BARDOCK:
                    return bardock(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NEZUKO:
                    return nezuko(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GHI_DANH:
                    return GhiDanh(mapId, status, cx, cy, tempId, avatar);
                      case ConstNpc.GAP_THU:
                    return gapThu(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRUNG_LINH_THU:
                    return trungLinhThu(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.POTAGE:
                    return poTaGe(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUY_LAO_KAME:
                case ConstNpc.VUA_VEGETA:
                case ConstNpc.TRUONG_LAO_GURU:
                    return quyLaoKame(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.POPO:
                    return popo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THO_DAI_CA:
                    return thodaika(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.ONG_GOHAN:
                case ConstNpc.ONG_MOORI:
                case ConstNpc.ONG_PARAGUS:
                    return ongGohan_ongMoori_ongParagus(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA:
                    return bulmaQK(mapId, status, cx, cy, tempId, avatar);
                // case ConstNpc.DUA_HAU:
                // return duahau(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DENDE:
                    return dende(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.APPULE:
                    return appule(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DR_DRIEF:
                    return drDrief(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CARGO:
                    return cargo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CUI:
                    return cui(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TAPION:
                    return tapion(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRONG_TAI:
                    return trongTai(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.SANTA:
                    return santa(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.URON:
                    return uron(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BA_HAT_MIT:
                    return baHatMit(mapId, status, cx, cy, tempId, avatar);
                     case ConstNpc.THU_TUONG:
                    return boMong1(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RUONG_DO:
                    return ruongDo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DAU_THAN:
                    return dauThan(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CALICK:
                    return calick(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.JACO:
                    return jaco(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THUONG_DE:
                    return thuongDe(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CUA_HANG_KY_GUI:
                    return kyGui(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.Granola:
                    return granala(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.GIUMA_DAU_BO:
//                    return mavuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.ARALE:
                    return cauca(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.OBOTCHAMA:
                    return cauca2(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.Monaito:
                    return monaito(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.ULTRA_GOKU:
                    return ultraGoku(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VADOS:
                    return vados(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.KHI_DAU_MOI:
                    return khidaumoi(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THAN_VU_TRU:
                    return thanVuTru(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.KIBIT:
                    return kibit(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BABIDAY:
                    return babiday(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.OSIN:
                    return osin(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LY_TIEU_NUONG:
                    return npclytieunuong54(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LINH_CANH:
                    return linhCanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DOC_NHAN:
                    return docNhan(mapId, status, cx, cy, tempId, avatar);
              
                case ConstNpc.QUOC_VUONG:
                    return quocVuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NOI_BANH:
                    return noibanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA_TL:
                    return bulmaTL(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_OMEGA:
                    return rongOmega(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_1S:
                case ConstNpc.RONG_2S:
                case ConstNpc.RONG_3S:
                case ConstNpc.RONG_4S:
                case ConstNpc.RONG_5S:
                case ConstNpc.RONG_6S:
                case ConstNpc.RONG_7S:
                    return rong1_to_7s(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NPC_64:
                    return npcThienSu64(mapId, status, cx, cy, tempId, avatar);
               
                case ConstNpc.BILL:
                    return bill(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BEARRY:
                    return bearry(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BO_MONG:
                    return boMong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THAN_MEO_KARIN:
                    return karin(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GOKU_SSJ:
                    return gokuSSJ_1(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GOKU_SSJ_:
                    return gokuSSJ_2(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DUONG_TANG:
                    return duongtank(mapId, status, cx, cy, tempId, avatar);
                default:
                    
                    return new Npc(mapId, status, cx, cy, tempId, avatar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                super.openBaseMenu(player);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                // ShopService.gI().openShopNormal(player, this, ConstNpc.SHOP_BUNMA_TL_0, 0,
                                // player.gender);
                            }
                        }
                    };
            }
        } catch (Exception e) {
            Logger.logException(NpcFactory.class, e, "Lỗi load npc");
            return null;
        }
    }

    // Lucy-mark
    public static void createNpcRongThieng() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.RONG_THIENG, -1) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.SHENRON_CONFIRM:
                        if (select == 0) {
                            SummonDragon.gI().confirmWish();
                        } else if (select == 1) {
                            SummonDragon.gI().reOpenShenronWishes(player);
                        }
                        break;
                    case ConstNpc.SHENRON_1_1:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_1
                                && select == SHENRON_1_STAR_WISHES_1.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_2, SHENRON_SAY,
                                    SHENRON_1_STAR_WISHES_2);
                            break;
                        }
                    case ConstNpc.SHENRON_1_2:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_2
                                && select == SHENRON_1_STAR_WISHES_2.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_1, SHENRON_SAY,
                                    SHENRON_1_STAR_WISHES_1);
                            break;
                        }
                    default:
                        SummonDragon.gI().showConfirmShenron(player, player.iDMark.getIndexMenu(), (byte) select);
                        break;
                }
            }
        };
    }

    public static void createNpcConMeo() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.CON_MEO, 351) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.MAKE_MATCH_PVP:
                        if (player.getSession().actived) {
                            if (Maintenance.isRuning) {
                                break;
                            }
                            PVPService.gI().sendInvitePVP(player, (byte) select);
                            break;
                        } else {
                            Service.gI().sendThongBao(player,
                                    "|5|VUI LÒNG KÍCH HOẠT TÀI KHOẢN ĐỂ MỞ KHÓA TÍNH NĂNG");
                            break;
                        }
                    case ConstNpc.MAKE_FRIEND:
                        if (select == 0) {
                            Object playerId = PLAYERID_OBJECT.get(player.id);
                            if (playerId != null) {
                                FriendAndEnemyService.gI().acceptMakeFriend(player,
                                        Integer.parseInt(String.valueOf(playerId)));
                            }
                        }
                        break;
                    case ConstNpc.REVENGE:
                        if (select == 0) {
                            PVPService.gI().acceptRevenge(player);
                        }
                        break;
                    case ConstNpc.TUTORIAL_SUMMON_DRAGON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        }
                        break;
                    case ConstNpc.SUMMON_SHENRON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        } else if (select == 1) {
                            SummonDragon.gI().summonShenron(player);
                        }
                        break;
                    case ConstNpc.TUTORIAL_SUMMON_DRAGONTRB:// TRB
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TRB);
                        }
                        break;
                    case ConstNpc.SUMMON_SHENRONTRB:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TRB);
                        } else if (select == 1) {
                            SummonDragon.gI().summonShenronTRB(player);
                        }
                        break;
                    // BANG
                    case ConstNpc.TUTORIAL_SUMMON_DRAGONBANG:// BANG
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_BANG);
                        }
                        break;
                    case ConstNpc.SUMMON_SHENRONBANG:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_BANG);
                        } else if (select == 1) {
                            SummonDragon.gI().summonShenronBang(player);
                        }
                        break;
                    // BLACK
                    case ConstNpc.TUTORIAL_SUMMON_DRAGONBLACK:// BLACK
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_BLACK);
                        }
                        break;
                    case ConstNpc.SUMMON_SHENRONBLACK:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_BLACK);
                        } else if (select == 1) {
                            SummonDragon.gI().summonShenronBlack(player);
                        }
                        break;
                    //
                    case ConstNpc.MENU_OPTION_USE_ITEM1105:
                        if (select == 0) {
                            IntrinsicService.gI().sattd(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().satnm(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().setxd(player);
                        }
                        break;
                    case 1431:
                        if (select == 0) {
                            IntrinsicService.gI().sattdtl(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().satnmtl(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().setxdtl(player);
                        }
                        break;
                    case 1432:
                        if (select == 0) {
                            IntrinsicService.gI().sattdhd(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().satnmhd(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().setxdhd(player);
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2000:
                    case ConstNpc.MENU_OPTION_USE_ITEM2001:
                    case ConstNpc.MENU_OPTION_USE_ITEM2002:
                        try {
                            ItemService.gI().OpenSKH(player, player.iDMark.getIndexMenu(), select);
                        } catch (Exception e) {
                            Logger.error("Lỗi mở hộp quà");
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2003:
                    case ConstNpc.MENU_OPTION_USE_ITEM2004:
                    case ConstNpc.MENU_OPTION_USE_ITEM2005:
                        try {
                            ItemService.gI().OpenDHD(player, player.iDMark.getIndexMenu(), select);
                        } catch (Exception e) {
                            Logger.error("Lỗi mở hộp quà");
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM736:
                        try {
                            ItemService.gI().OpenDHD(player, player.iDMark.getIndexMenu(), select);
                        } catch (Exception e) {
                            Logger.error("Lỗi mở hộp quà");
                        }
                        break;
                    case ConstNpc.INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().showAllIntrinsic(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().showConfirmOpen(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().showConfirmOpenVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().open(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC_VIP:
                        if (select == 0) {
                            IntrinsicService.gI().openVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_LEAVE_CLAN:
                        if (select == 0) {
                            ClanService.gI().leaveClan(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_NHUONG_PC:
                        if (select == 0) {
                            ClanService.gI().phongPc(player, (int) PLAYERID_OBJECT.get(player.id));
                        }
                        break;
                    case ConstNpc.BAN_PLAYER:
                        if (select == 0) {
                            PlayerService.gI().banPlayer((Player) PLAYERID_OBJECT.get(player.id));
                            Service.gI().sendThongBao(player,
                                    "Ban người chơi " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                        }
                        break;

                    case ConstNpc.BUFF_PET:
                        if (select == 0) {
                            Player pl = (Player) PLAYERID_OBJECT.get(player.id);
                            if (pl.pet == null) {
                                PetService.gI().createNormalPet(pl);
                                Service.gI().sendThongBao(player, "Phát đệ tử cho "
                                        + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                            }
                        }
                        break;
                    case ConstNpc.ACTIVE_PLAYER:
                        if (select == 0) {
                            PlayerService.gI().ActivePlayer((Player) PLAYERID_OBJECT.get(player.id));
                            Service.getInstance().sendThongBao(player,
                                    "Activated  " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                        }
                        break;
                    case ConstNpc.UP_TOP_ITEM:
                        if (select == 0) {
                            if (player.inventory.ruby >= 1000 && player.iDMark.getIdItemUpTop() != -1) {
                                ItemKyGui it = ShopKyGuiService.gI().getItemBuy(player.iDMark.getIdItemUpTop());
                                if (it == null || it.isBuy) {
                                    Service.gI().sendThongBao(player, "Vật phẩm không tồn tại hoặc đã được bán");
                                    return;
                                }
                                if (it.player_sell != player.id) {
                                    Service.gI().sendThongBao(player, "Vật phẩm không thuộc quyền sở hữu");
                                    ShopKyGuiService.gI().openShopKyGui(player);
                                    return;
                                }
                                player.inventory.ruby -= 1000;
                                Service.gI().sendMoney(player);
                                Service.gI().sendThongBao(player, "Thành công");
                                it.isUpTop += 1;
                                ShopKyGuiService.gI().openShopKyGui(player);
                            } else {
                                Service.gI().sendThongBao(player, "Bạn không đủ hồng ngọc");
                                player.iDMark.setIdItemUpTop(-1);
                            }
                        }
                        break;

                    case ConstNpc.MENU_ADMIN:
                        switch (select) {
                            case 0:
                                for (int i = 14; i <= 20; i++) {
                                    Item item = ItemService.gI().createNewItem((short) i);
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                }
                                InventoryServiceNew.gI().sendItemBags(player);
                                break;
                            case 1:
                                if (player.pet == null) {
                                    PetService.gI().createNormalPet(player);
                                } else {
                                    if (player.pet.typePet == 1) {
                                        PetService.gI().changeGokuPet(player);
                                    } else if (player.pet.typePet == 2) {
                                        PetService.gI().changeMabuPet(player);
                                    } else if (player.pet.typePet == 3) {
                                        PetService.gI().changeNgokhongPet(player);
                                    }
                                    PetService.gI().changeBerusPet(player);
                                }
                                break;
                            case 2:
                                if (player.isAdmin()) {
                                    System.out.println(player.name);
                                    // PlayerService.gI().baoTri();
                                    Maintenance.gI().start(60);
                                }
                                break;
                            case 3:
                                Input.gI().createFormFindPlayer(player);
                                break;
                            case 4:
                                BossManager.gI().showListBoss(player);
                                break;
                            case 5:
                                MaQuaTangManager.gI().checkInfomationGiftCode(player);
                                break;
                            case 6:
                                Input.gI().createFormNapTien(player);
                                break;
                            case 7:
                                Input.gI().createFormThongBao(player);
                                break;
                            case 8:
                                Input.gI().createFormThongBaoRieng(player);
                                break;
                            case 9:
                                Input.gI().createFormTangQuaMocNap(player);
                                break;
                        }
                        break;

                    case ConstNpc.menutd:
                        switch (select) {
                            case 0:
                                try {
                                    ItemService.gI().settaiyoken(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().setgenki(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().setkamejoko(player);
                                } catch (Exception e) {
                                }
                                break;
                        }
                        break;

                    case ConstNpc.menunm:
                        switch (select) {
                            case 0:
                                try {
                                    ItemService.gI().setgodki(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().setgoddam(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().setsummon(player);
                                } catch (Exception e) {
                                }
                                break;
                        }
                        break;

                    case ConstNpc.menuxd:
                        switch (select) {
                            case 0:
                                try {
                                    ItemService.gI().setgodgalick(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().setmonkey(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().setgodhp(player);
                                } catch (Exception e) {
                                }
                                break;
                        }
                        break;
                    ///
                    case ConstNpc.menutdtl:
                        switch (select) {
                            case 0:
                                try {
                                    ItemService.gI().sendKoktl(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().sendTxhtl(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().sendSgktl(player);
                                } catch (Exception e) {
                                }
                                break;
                        }
                        break;

                    case ConstNpc.menunmtl:
                        switch (select) {
                            case 0:
                                try {
                                    ItemService.gI().sendKitl(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().sendLhtl(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().sendTrtl(player);
                                } catch (Exception e) {
                                }
                                break;
                        }
                        break;

                    case ConstNpc.menuxdtl:
                        switch (select) {
                            case 0:
                                try {
                                    ItemService.gI().sendKkrtl(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().sendKhitl(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().sendNaptl(player);
                                } catch (Exception e) {
                                }
                                break;
                        }
                        break;

                    ///
                    case ConstNpc.menutdhd:
                        switch (select) {
                            case 0:
                                try {
                                    ItemService.gI().sendKokhd(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().sendTxhhd(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().sendSgkhd(player);
                                } catch (Exception e) {
                                }
                                break;
                        }
                        break;

                    case ConstNpc.menunmhd:
                        switch (select) {
                            case 0:
                                try {
                                    ItemService.gI().sendKihd(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().sendLhhd(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().sendTrhd(player);
                                } catch (Exception e) {
                                }
                                break;
                        }
                        break;

                    case ConstNpc.menuxdhd:
                        switch (select) {
                            case 0:
                                try {
                                    ItemService.gI().sendKkrhd(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().sendKhihd(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().sendNaphd(player);
                                } catch (Exception e) {
                                }
                                break;
                        }
                        break;
                        //
                    case ConstNpc.CONFIRM_DISSOLUTION_CLAN:
                        switch (select) {
                            case 0:
                                Clan clan = player.clan;
                                clan.deleted = 1;
                                player.clan = null;
                                player.clanMember = null;
                                ClanService.gI().sendMyClan(player);
                                ClanService.gI().sendClanId(player);
                                Service.gI().sendThongBao(player, "Đã giải tán bang hội.");
                                break;
                        }
                        break;
                    case ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND:
                        if (select == 0) {
                            for (int i = 0; i < player.inventory.itemsBoxCrackBall.size(); i++) {
                                player.inventory.itemsBoxCrackBall.set(i, ItemService.gI().createItemNull());
                            }
                            player.inventory.itemsBoxCrackBall.clear();
                            Service.gI().sendThongBao(player, "Đã xóa hết vật phẩm trong rương");
                        }
                        break;
                    case ConstNpc.MENU_FIND_PLAYER:
                        Player p = (Player) PLAYERID_OBJECT.get(player.id);
                        if (p != null) {
                            switch (select) {
                                case 0:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMapYardrat(player, p.zone, p.location.x,
                                                p.location.y);
                                    }
                                    break;
                                case 1:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMap(p, player.zone, player.location.x,
                                                player.location.y);
                                    }
                                    break;
                                case 2:
                                    Input.gI().createFormChangeName(player, p);
                                    break;
                                case 3:
                                    String[] selects = new String[] { "Đồng ý", "Hủy" };
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BAN_PLAYER, -1,
                                            "Bạn có chắc chắn muốn ban " + p.name, selects, p);
                                    break;
                                case 4:
                                    Service.getInstance().sendThongBao(player,
                                            "Kich người chơi " + p.name + " thành công");
                                    Client.gI().getPlayers().remove(p);
                                    Client.gI().kickSession(p.getSession());
                                    break;
                                case 5:
                                    String[] selectss = new String[] { "Đồng ý", "Hủy" };
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.ACTIVE_PLAYER, -1,
                                            "Mở Thành Viên Cho " + p.name + " ?", selectss, p);
                                    break;

                            }
                        }
                    case ConstNpc.MENU_EVENT:
                        switch (select) {
                            case 0:
                                Service.gI().sendThongBaoOK(player,
                                        "Điểm sự kiện: " + player.inventory.event + " ngon ngon...");
                                break;
                            case 1:
                                Service.gI().showListTop(player, Manager.topSK);
                                break;
                            case 2:
                                Service.gI().sendThongBao(player, "Sự kiện đã kết thúc...");
                                // NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_GIAO_BONG, -1, "Người
                                // muốn giao bao nhiêu bông...",
                                // "100 bông", "1000 bông", "10000 bông");
                                break;
                            case 3:
                                Service.gI().sendThongBao(player, "Sự kiện đã kết thúc...");
                                // NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DOI_THUONG_SU_KIEN,
                                // -1, "Con có thực sự muốn đổi thưởng?\nPhải giao cho ta 3000 điểm sự kiện
                                // đấy... ",
                                // "Đồng ý", "Từ chối");
                                break;

                        }
                        break;
                    // case ConstNpc.MENU_GIAO_BONG:
                    // ItemService.gI().giaobong(player, (int) Util.tinhLuyThua(10, select + 2));
                    // break;
                    case ConstNpc.CONFIRM_DOI_THUONG_SU_KIEN:
                        if (select == 0) {
                            ItemService.gI().openBoxVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_DOI_DIEM_DUA:
                        if (select == 0) {
                            ItemService.gI().openBoxCongThuc(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_DOI_DIEM_ITEMC2:
                        if (select == 0) {
                            ItemService.gI().openBoxitemc2(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_DOI_ITEM_NR:
                        if (select == 0) {
                            ItemService.gI().openBoxitemnr(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_DOI_DIEM_CT:
                        if (select == 0) {
                            ItemService.gI().openBoxCt(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_TELE_NAMEC:
                        if (select == 0) {
                            NgocRongNamecService.gI().teleportToNrNamec(player);
                            player.inventory.subGemAndRuby(50);
                            Service.gI().sendMoney(player);
                        }
                        break;
                }
            }
        };
    }

}
