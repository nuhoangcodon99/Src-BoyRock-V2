package com.boyrock.services;

import com.girlkun.network.io.Message;
import com.boyrock.models.mob.Mob;
import com.boyrock.models.player.Player;
import com.boyrock.models.skill.Skill;
import com.boyrock.utils.SkillUtil;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EffectSkillService {

    public static final byte TURN_ON_EFFECT = 1;
    public static final byte TURN_OFF_EFFECT = 0;
    public static final byte TURN_OFF_ALL_EFFECT = 2;
public static final byte VO_HIEU_CHUONG = 45;
    public static final byte HOLD_EFFECT = 32;
    public static final byte SHIELD_EFFECT = 33;
    public static final byte HUYT_SAO_EFFECT = 39;
    public static final byte BLIND_EFFECT = 40;
    public static final byte SLEEP_EFFECT = 41;
    public static final byte STONE_EFFECT = 42;
    public static final byte CUONGCHIEN_EFFECT = 43;
    private static EffectSkillService i;

    private EffectSkillService() {

    }

    public static EffectSkillService gI() {
        if (i == null) {
            i = new EffectSkillService();
        }
        return i;
    }
 public void setMaPhongBa(Player player, long lastTimeMaPhongBa, int timeMaPhongBa) {
        player.effectSkill.lastTimeMaPhongBa = lastTimeMaPhongBa;
        player.effectSkill.timeMaPhongBa = timeMaPhongBa;
        player.effectSkill.isMaPhongBa = true;
    }

    public void removeMaPhongBa(Player player) {
        player.effectSkill.isMaPhongBa = false;
        Service.getInstance().Send_Caitrang(player);
    }
    public void sendMobToMaPhongBa(Player player, Mob mob, int timeMaPhongBaMOB) {
        Message msg;
        try {
            msg = new Message(-112);
            msg.writer().writeByte(1);
            msg.writer().writeByte(mob.id); //mob id
            msg.writer().writeShort(11175); //icon socola
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
            mob.effectSkill.setMaPhongBaMOB(System.currentTimeMillis(), timeMaPhongBaMOB);
        } catch (Exception e) {
            com.boyrock.utils.Logger.logException(EffectSkillService.class, e);
        }
    }
    // hiệu ứng player dùng skill
    public void sendEffectUseSkill(Player player, byte skillId) {
        Skill skill = SkillUtil.getSkillbyId(player, skillId);
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(8);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(skill.skillId);
            Service.gI().sendMessAnotherNotMeInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            com.boyrock.utils.Logger.logException(EffectSkillService.class, e);
        }
    }
public void sendEffectUseSkill1(Player player, byte skillId) {
        Skill skill = SkillUtil.getSkillbyId(player, skillId);
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(8);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(skill.skillId);
            Service.gI().sendMessAnotherNotMeInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            com.boyrock.utils.Logger.logException(EffectSkillService.class, e);
        }
    }
    public void sendEffectPlayer(Player plUseSkill, Player plTarget, byte toggle, byte effect) {
        Message msg;
        try {
            msg = new Message(-124);
            msg.writer().writeByte(toggle); // 0: hủy hiệu ứng, 1: bắt đầu hiệu ứng
            msg.writer().writeByte(0); // 0: vào phần phayer, 1: vào phần mob
            if (toggle == TURN_OFF_ALL_EFFECT) {
                msg.writer().writeInt((int) plTarget.id);
            } else {
                msg.writer().writeByte(effect); // loại hiệu ứng
                msg.writer().writeInt((int) plTarget.id); // id player dính effect
                msg.writer().writeInt((int) plUseSkill.id); // id player dùng skill
            }
            Service.gI().sendMessAllPlayerInMap(plUseSkill, msg);
            msg.cleanup();
        } catch (Exception e) {
            com.boyrock.utils.Logger.logException(EffectSkillService.class, e);
        }
    }

    public void sendEffectMob(Player plUseSkill, Mob mobTarget, byte toggle, byte effect) {
        Message msg;
        try {
            msg = new Message(-124);
            msg.writer().writeByte(toggle); // 0: hủy hiệu ứng, 1: bắt đầu hiệu ứng
            msg.writer().writeByte(1); // 0: vào phần phayer, 1: vào phần mob
            msg.writer().writeByte(effect); // loại hiệu ứng
            msg.writer().writeByte(mobTarget.id); // id mob dính effect
            msg.writer().writeInt((int) plUseSkill.id); // id player dùng skill
            Service.gI().sendMessAllPlayerInMap(mobTarget.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            com.boyrock.utils.Logger.logException(EffectSkillService.class, e);
        }
    }

    // Cải trang Drabura Hóa Đá
    public void setHoaDa(Player player, long lastTimeHoaDa, int timeHoaDa) {
        player.effectSkill.lastTimeHoaDa = lastTimeHoaDa;
        player.effectSkill.timeHoaDa = timeHoaDa;
        player.effectSkill.isHoaDa = true;
        Service.gI().Send_Caitrang(player);
        Service.gI().point(player);
        Service.gI().Send_Info_NV(player);

    }

    public void setHoaLanh(Player player, long lastTimeHoaLanh, int timeHoaLanh) {
        player.effectSkill.lastTimeHoaLanh = lastTimeHoaLanh;
        player.effectSkill.timeHoaLanh = timeHoaLanh;
        player.effectSkill.isHoaLanh = true;
         player.effectSkill.countPem1hp = 0;
        Service.gI().Send_Caitrang(player);
        Service.gI().point(player);
        Service.gI().Send_Info_NV(player);

    }

    // bui bui
    public void setLamCham(Player player, long lastTimeLamCham, int timeLamCham) {
        player.effectSkill.lastTimeLamCham = lastTimeLamCham;
        player.effectSkill.timeLamCham = timeLamCham;
        player.effectSkill.isLamCham = true;
        Service.gI().Send_Caitrang(player);
        Service.gI().point(player);
        Service.gI().Send_Info_NV(player);

    }

    public void removeLamCham(Player player) {
        player.effectSkill.isLamCham = false;
        Service.gI().chat(player,"Nhẹ quá !");
        Service.gI().Send_Caitrang(player);
        Service.gI().point(player);
        Service.gI().Send_Info_NV(player);

    }
    // sexy
    public void setHorny(Player player, long lastTimeHorny, int timeHorny) {
        player.effectSkill.lastTimeHorny = lastTimeHorny;
        player.effectSkill.timeHorny = timeHorny;
        player.effectSkill.isHorny = true;
        Service.gI().Send_Caitrang(player);
        Service.gI().point(player);
        Service.gI().Send_Info_NV(player);

    }

    public void removeHorny(Player player) {
        player.effectSkill.isHorny = false;
        Service.gI().chat(player,"Mình vừa sửa cái gì thế nhỉ ?");
        Service.gI().Send_Caitrang(player);
        Service.gI().point(player);
        Service.gI().Send_Info_NV(player);

    }
     // danh hoi
    public void setDanhHoi(Player player, long lastTimeDanhHoi, int timeDanhHoi) {
        player.effectSkill.lastTimeDanhHoi = lastTimeDanhHoi;
        player.effectSkill.timeDanhHoi = timeDanhHoi;
        player.effectSkill.isDanhHoi = true;
        Service.gI().Send_Caitrang(player);
        Service.gI().point(player);
        Service.gI().Send_Info_NV(player);

    }

    public void removeDanhHoi(Player player) {
        player.effectSkill.isDanhHoi = false;
        Service.gI().chat(player,"Khịt khịt ");
        Service.gI().Send_Caitrang(player);
        Service.gI().point(player);
        Service.gI().Send_Info_NV(player);

    }
     // phan tam
    public void setPhanTam(Player player, long lastTimePhanTam, int timePhanTam) {
        player.effectSkill.lastTimePhanTam = lastTimePhanTam;
        player.effectSkill.timePhanTam = timePhanTam;
        player.effectSkill.isPhanTam = true;
        Service.gI().Send_Caitrang(player);
        Service.gI().point(player);
        Service.gI().Send_Info_NV(player);

    }

    public void removePhanTam(Player player) {
        player.effectSkill.isPhanTam = false;
        Service.gI().chat(player,"Điếc cả tai");
        Service.gI().Send_Caitrang(player);
        Service.gI().point(player);
        Service.gI().Send_Info_NV(player);

    }
    // ân thân
    public void setAnThan(Player player, long lastTimeAnThan, int timeAnThan) {
        player.effectSkill.lastTimeAnThan = lastTimeAnThan;
        player.effectSkill.timeAnThan = timeAnThan;
        player.effectSkill.isAnThan = true;
        Service.gI().Send_Caitrang(player);
        Service.gI().point(player);
        Service.gI().Send_Info_NV(player);

    }

    public void removeAnThan(Player player) {
        player.effectSkill.isAnThan = false;
        Service.gI().chat(player,"Bất ngờ chưa ?");
        Service.gI().Send_Caitrang(player);
        Service.gI().point(player);
        Service.gI().Send_Info_NV(player);

    }

    public void removeHoaDa(Player player) {
        player.effectSkill.isHoaDa = false;
         Service.gI().chat(player,"Cử động được rồi ?");
        Service.gI().Send_Caitrang(player);
        Service.gI().point(player);
        Service.gI().Send_Info_NV(player);

    }

    public void removeHoaLanh(Player player) {
        player.effectSkill.isHoaLanh = false;
        Service.gI().chat(player,"Ấm quá !");
        Service.gI().Send_Caitrang(player);
        Service.gI().point(player);
        Service.gI().Send_Info_NV(player);

    }

    // **
    public void setHoaSocola(Player player, long lastTimeHoaSocola, int timeHoaSocola) {
        player.effectSkill.lastTimeHoaSocola = lastTimeHoaSocola;
        player.effectSkill.timeHoaSocola = timeHoaSocola;
        player.effectSkill.isHoaSocola = true;
        Service.gI().Send_Caitrang(player);
        Service.gI().point(player);
        Service.gI().Send_Info_NV(player);

    }

    public void removeHoaSocola(Player player) {
        player.effectSkill.isHoaSocola = false;
        Service.gI().chat(player,"Tí cook !");
        Service.gI().Send_Caitrang(player);
        Service.gI().point(player);
        Service.gI().Send_Info_NV(player);
    }
    // **
    public void setThieuDot(Player player, long lastTimeThieuDot, int timeThieuDot) {
        player.effectSkill.lastTimeThieuDot = lastTimeThieuDot;
        player.effectSkill.timeThieuDot = timeThieuDot;
        player.effectSkill.isThieuDot = true;
        Service.gI().Send_Caitrang(player);
        Service.gI().point(player);
        Service.gI().Send_Info_NV(player);
        Service.gI().sendFlagBag(player);

    }

    public void removeThieuDot(Player player) {
        player.effectSkill.isThieuDot = false;
        Service.gI().Send_Caitrang(player);
        Service.gI().point(player);
        Service.gI().Send_Info_NV(player);
        Service.gI().sendFlagBag(player);
    }

    // **
    // Cải trang Thỏ Đại Ca
    public void setHoaCarot(Player player, long lastTimeHoaCarot, int timeHoaCarot) {
        player.effectSkill.lastTimeHoaCarot = lastTimeHoaCarot;
        player.effectSkill.timeHoaCarot = timeHoaCarot;
        player.effectSkill.isHoaCarot = true;
        Service.gI().Send_Caitrang(player);
        Service.gI().point(player);
        Service.gI().Send_Info_NV(player);

    }

    public void removeHoaCarot(Player player) {
        player.effectSkill.isHoaCarot = false;
        Service.gI().chat(player,"Mẹ mày béo ?");
        Service.gI().Send_Caitrang(player);
        Service.gI().point(player);
        Service.gI().Send_Info_NV(player);
    }

    // Trói *********************************************************************
    // dừng sử dụng trói
    public void removeUseTroi(Player player) {
        if (player.effectSkill.mobAnTroi != null) {
            player.effectSkill.mobAnTroi.effectSkill.removeAnTroi();
        }
        if (player.effectSkill.plAnTroi != null) {
            removeAnTroi(player.effectSkill.plAnTroi);
        }
        player.effectSkill.useTroi = false;
        player.effectSkill.mobAnTroi = null;
        player.effectSkill.plAnTroi = null;
        sendEffectPlayer(player, player, TURN_OFF_EFFECT, HOLD_EFFECT);
    }

    // hết thời gian bị trói
    public void removeAnTroi(Player player) {
        if (player != null && player.effectSkill != null) {
            player.effectSkill.anTroi = false;
            player.effectSkill.plTroi = null;
            Service.gI().chat(player,"Không thể trói buộc được tao đâu !");
            sendEffectPlayer(player, player, TURN_OFF_EFFECT, HOLD_EFFECT);
        }
    }

    public void setAnTroi(Player player, Player plTroi, long lastTimeAnTroi, int timeAnTroi) {
        player.effectSkill.anTroi = true;
        // player.effectSkill.lastTimeAnTroi = lastTimeAnTroi;
        // player.effectSkill.timeAnTroi = timeAnTroi;
        player.effectSkill.plTroi = plTroi;
    }

    public void setUseTroi(Player player, long lastTimeTroi, int timeTroi) {
        player.effectSkill.useTroi = true;
        player.effectSkill.lastTimeTroi = lastTimeTroi;
        player.effectSkill.timeTroi = timeTroi;
    }
    // **************************************************************************

    // Thôi miên ****************************************************************
    // thiết lập thời gian bắt đầu bị thôi miên
    public void setThoiMien(Player player, long lastTimeThoiMien, int timeThoiMien) {
        player.effectSkill.isThoiMien = true;
        player.effectSkill.lastTimeThoiMien = lastTimeThoiMien;
        player.effectSkill.timeThoiMien = timeThoiMien;
    }

    // hết hiệu ứng thôi miên
    public void removeThoiMien(Player player) {
        player.effectSkill.isThoiMien = false;
        Service.gI().chat(player,"Đau đầu quá !");
        sendEffectPlayer(player, player, TURN_OFF_EFFECT, SLEEP_EFFECT);
    }

    // **************************************************************************
    // Thái dương hạ san &&&&****************************************************
    // player ăn choáng thái dương hạ san
    public void startStun(Player player, long lastTimeStartBlind, int timeBlind) {
        player.effectSkill.lastTimeStartStun = lastTimeStartBlind;
        player.effectSkill.timeStun = timeBlind;
        player.effectSkill.isStun = true;
        sendEffectPlayer(player, player, TURN_ON_EFFECT, BLIND_EFFECT);
    }

    // kết thúc choáng thái dương hạ san
    public void removeStun(Player player) {
        player.effectSkill.isStun = false;
        Service.gI().chat(player,"Đau mắt quá !");
        sendEffectPlayer(player, player, TURN_OFF_EFFECT, BLIND_EFFECT);
    }
    // **************************************************************************

    // Socola *******************************************************************
    // player biến thành socola
    public void setSocola(Player player, long lastTimeSocola, int timeSocola) {
        player.effectSkill.lastTimeSocola = lastTimeSocola;
        player.effectSkill.timeSocola = timeSocola;
        player.effectSkill.isSocola = true;
        player.effectSkill.countPem1hp = 0;
        Service.gI().Send_Caitrang(player);
        Service.gI().point(player);
        Service.gI().Send_Info_NV(player);

    }

    public void setBinh(Player player, long lastTimeBinh, int timeBinh) {
        player.effectSkill.lastTimeBinh = lastTimeBinh;
        player.effectSkill.timeBinh = timeBinh;
        player.effectSkill.isBinh = true;
        Service.gI().Send_Caitrang(player);
        Service.gI().point(player);
        Service.gI().Send_Info_NV(player);
    }

    // player trở lại thành người
    public void removeSocola(Player player) {
        player.effectSkill.isSocola = false;
        Service.gI().chat(player,"Tí chết !");
        Service.getInstance().Send_Caitrang(player);
        Service.gI().point(player);
        Service.gI().Send_Info_NV(player);
    }

    // player trở lại thành người
    public void removeBinh(Player player) {
        player.effectSkill.isBinh = false;
        Service.gI().chat(player,"Ta đã được tự do !");
        Service.getInstance().Send_Caitrang(player);
        Service.gI().point(player);
        Service.gI().Send_Info_NV(player);
    }

    // quái biến thành socola
    public void sendMobToSocola(Player player, Mob mob, int timeSocola) {
        Message msg;
        try {
            msg = new Message(-112);
            msg.writer().writeByte(1);
            msg.writer().writeByte(mob.id); // mob id
            msg.writer().writeShort(4133); // icon socola
            Service.gI().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
            mob.effectSkill.setSocola(System.currentTimeMillis(), timeSocola);
        } catch (Exception e) {
            com.boyrock.utils.Logger.logException(EffectSkillService.class, e);
        }
    }
  public void sendMobToHoaLanh(Player player, Mob mob, int timeHoaDa) {
        Message msg;
        try {
            msg = new Message(-112);
            msg.writer().writeByte(1);
            msg.writer().writeByte(mob.id); // mob id
            msg.writer().writeShort(741); // icon socola
            Service.gI().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
            mob.effectSkill.setHoaLanh(System.currentTimeMillis(), timeHoaDa);
        } catch (Exception e) {
            com.boyrock.utils.Logger.logException(EffectSkillService.class, e);
        }
    }

    public void sendMobToBinh(Player player, Mob mob, int timeBinh) {
        Message msg;
        try {
            msg = new Message(-112);
            msg.writer().writeByte(1);
            msg.writer().writeByte(mob.id); // mob id
            msg.writer().writeShort(14522); // icon socola
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
            mob.effectSkill.setBinh(System.currentTimeMillis(), timeBinh);
        } catch (Exception e) {
            com.boyrock.utils.Logger.logException(EffectSkillService.class, e);
        }
    }
    // **************************************************************************

    // Dịch chuyển tức thời *****************************************************
    public void setBlindDCTT(Player player, long lastTimeDCTT, int timeBlindDCTT) {
        player.effectSkill.isBlindDCTT = true;
        player.effectSkill.lastTimeBlindDCTT = lastTimeDCTT;
        player.effectSkill.timeBlindDCTT = timeBlindDCTT;
    }

    public void removeBlindDCTT(Player player) {
        player.effectSkill.isBlindDCTT = false;
        sendEffectPlayer(player, player, TURN_OFF_EFFECT, BLIND_EFFECT);
    }
    // **************************************************************************

    // Huýt sáo *****************************************************************
    // Hưởng huýt sáo
    public void setStartHuytSao(Player player, int tiLeHP) {
        player.effectSkill.tiLeHPHuytSao = tiLeHP;
        player.effectSkill.lastTimeHuytSao = System.currentTimeMillis();
    }

    // Hết hiệu ứng huýt sáo
    public void removeHuytSao(Player player) {
        player.effectSkill.tiLeHPHuytSao = 0;
        sendEffectPlayer(player, player, TURN_OFF_EFFECT, HUYT_SAO_EFFECT);
        Service.gI().point(player);
        Service.gI().Send_Info_NV(player);
    }
    /// cuong chien

    public void setStartCuongChien(Player player) {
        player.effectSkill.isCuongChien = true;
        player.effectSkill.lastTimeCuongChien = System.currentTimeMillis();
        player.nPoint.setCrit();
        Service.gI().point(player);
        Service.gI().Send_Info_NV(player);
    }

    public void removeCuongChien(Player player) {
        player.effectSkill.isCuongChien = false;
        sendEffectPlayer(player, player, TURN_OFF_EFFECT, CUONGCHIEN_EFFECT);
        player.nPoint.setCrit();
        Service.gI().point(player);
        Service.gI().Send_Info_NV(player);

        PlayerService.gI().sendInfoHpMpMoney(player);
    }

    // **************************************************************************
    // Biến khỉ *****************************************************************
    // Bắt đầu biến khỉ
    public void setIsMonkey(Player player) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(EffectSkillService.class.getName()).log(Level.SEVERE, null, ex);
        }
        int timeMonkey = SkillUtil.getTimeMonkey(player.playerSkill.skillSelect.point);
        if (player.setClothes.cadic == 5) {
            timeMonkey *= 2;
        }
         if (player.setClothes.cadic2 == 5) {
            timeMonkey *= 3;
        }
        player.effectSkill.isMonkey = true;
        player.effectSkill.timeMonkey = timeMonkey;
        player.effectSkill.lastTimeUpMonkey = System.currentTimeMillis();
        player.effectSkill.levelMonkey = (byte) player.playerSkill.skillSelect.point;
        player.nPoint.setHp(player.nPoint.hp * 2);
    }
 public void setIsBroly(Player player) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(EffectSkillService.class.getName()).log(Level.SEVERE, null, ex);
        }
        int timeBroly = SkillUtil.getTimeBroly(player.playerSkill.skillSelect.point);
      
        player.effectSkill.isBroly = true;
          player.effectSkill.timeBroly= timeBroly;
        player.effectSkill.timeBroly =  SkillUtil.getTimeBroly(player.playerSkill.skillSelect.point);
        player.effectSkill.lastTimeUpBroly = System.currentTimeMillis();
        player.effectSkill.levelBroly = (byte) player.playerSkill.skillSelect.point;
        player.nPoint.setHp(player.nPoint.hp * 2);
    }
 public void setIsVoHieu(Player player) {
      Service.gI().sendTimeSkill( player);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(EffectSkillService.class.getName()).log(Level.SEVERE, null, ex);
        }
        int timeBroly = SkillUtil.getTimeVohieu(player.playerSkill.skillSelect.point);
      
        player.effectSkill.isVoHieuChuong = true;
          player.effectSkill.timeVoHieuChuong= timeBroly;
        player.effectSkill.timeVoHieuChuong =  SkillUtil.getTimeVohieu(player.playerSkill.skillSelect.point);
        player.effectSkill.lastTimeVoHieuChuong = System.currentTimeMillis();
       
    }
public void setIstienhoa(Player player) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(EffectSkillService.class.getName()).log(Level.SEVERE, null, ex);
        }
        int timetienhoa = SkillUtil.getTimetienhoa(player.playerSkill.skillSelect.point);
      
        player.effectSkill.istienhoa = true;
          player.effectSkill.timetienhoa= timetienhoa;
        player.effectSkill.timetienhoa =  SkillUtil.getTimetienhoa(player.playerSkill.skillSelect.point);
        player.effectSkill.lastTimeUptienhoa = System.currentTimeMillis();
        player.effectSkill.leveltienhoa = (byte) player.playerSkill.skillSelect.point;
        player.nPoint.setHp(player.nPoint.hp * 2);
    }

    public void monkeyDown(Player player) {
    
        player.effectSkill.isMonkey = false;
        player.effectSkill.levelMonkey = 0;
        if (player.nPoint.hp > player.nPoint.hpMax) {
            player.nPoint.setHp(player.nPoint.hpMax);
        }

        sendEffectEndCharge(player);
        sendEffectMonkey(player);
        Service.gI().setNotMonkey(player);
        Service.gI().Send_Caitrang(player);
        Service.gI().point(player);
        PlayerService.gI().sendInfoHpMp(player);
        Service.gI().Send_Info_NV(player);
        Service.gI().sendInfoPlayerEatPea(player);
    }
  public void brolyDown(Player player) {
          player.ismonkey = 0;
        player.effectSkill.isBroly = false;
        player.effectSkill.levelBroly = 0;
        if (player.nPoint.hp > player.nPoint.hpMax) {
            player.nPoint.setHp(player.nPoint.hpMax);
        }

     sendEffectEndCharge(player);
        sendEffectBroly(player);
         Service.gI().setNotBroly(player);
        Service.gI().Send_Caitrang(player);
        Service.gI().point(player);
        PlayerService.gI().sendInfoHpMp(player);
        Service.gI().Send_Info_NV(player);
        Service.gI().sendInfoPlayerEatPea(player);
    }
  public void vohieuDown(Player player) {
       
        player.effectSkill.isVoHieuChuong = false;
   
        

     
        
        Service.gI().Send_Caitrang(player);
        Service.gI().point(player);
        PlayerService.gI().sendInfoHpMp(player);
        Service.gI().Send_Info_NV(player);
        Service.gI().sendInfoPlayerEatPea(player);
    }
   public void tienhoaDown(Player player) {
        player.isbroly = 0;
        player.effectSkill.istienhoa = false;
        player.effectSkill.leveltienhoa = 0;
        if (player.nPoint.hp > player.nPoint.hpMax) {
            player.nPoint.setHp(player.nPoint.hpMax);
        }

     sendEffectEndCharge(player);
        sendEffecttienhoa(player);
         Service.gI().setNottienhoa(player);
        Service.gI().Send_Caitrang(player);
        Service.gI().point(player);
        PlayerService.gI().sendInfoHpMp(player);
        Service.gI().Send_Info_NV(player);
        Service.gI().sendInfoPlayerEatPea(player);
        
    }
    // **************************************************************************
    // Tái tạo năng lượng *******************************************************

    public void startCharge(Player player) {
        if (!player.effectSkill.isCharging) {
            player.effectSkill.isCharging = true;
            sendEffectCharge(player);
        }
    }

    public void stopCharge(Player player) {
        player.effectSkill.countCharging = 0;
        player.effectSkill.isCharging = false;
        ;
        sendEffectStopCharge(player);

    }

    // **************************************************************************
    // Khiên năng lượng *********************************************************
    public void setStartShield(Player player) {
        player.effectSkill.isShielding = true;
        player.effectSkill.lastTimeShieldUp = System.currentTimeMillis();
        player.effectSkill.timeShield = SkillUtil.getTimeShield(player.playerSkill.skillSelect.point);
    }
 

    public void removeShield(Player player) {
        player.effectSkill.isShielding = false;
        sendEffectPlayer(player, player, TURN_OFF_EFFECT, SHIELD_EFFECT);
    }

    public void breakShield(Player player) {
        removeShield(player);
        Service.gI().sendThongBao(player, "Khiên năng lượng đã bị vỡ!");
        ItemTimeService.gI().removeItemTime(player, 3784);
    }

    // **************************************************************************
    public void sendEffectBlindThaiDuongHaSan(Player plUseSkill, List<Player> players, List<Mob> mobs, int timeStun) {
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(0);
            msg.writer().writeInt((int) plUseSkill.id);
            msg.writer().writeShort(plUseSkill.playerSkill.skillSelect.skillId);
            msg.writer().writeByte(mobs.size());
            for (Mob mob : mobs) {
                msg.writer().writeByte(mob.id);
                msg.writer().writeByte(timeStun / 1000);
            }
            msg.writer().writeByte(players.size());
            for (Player pl : players) {
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeByte(timeStun / 1000);
            }
            Service.gI().sendMessAllPlayerInMap(plUseSkill, msg);
            msg.cleanup();
        } catch (Exception e) {
            com.boyrock.utils.Logger.logException(EffectSkillService.class, e);
        }
    }

    // hiệu ứng bắt đầu gồng
    public void sendEffectStartCharge(Player player) {
        Skill skill = SkillUtil.getSkillbyId(player, Skill.TAI_TAO_NANG_LUONG);

        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(6);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(skill.skillId);
    
            Service.gI().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            com.boyrock.utils.Logger.logException(EffectSkillService.class, e);
        }
    }

    // hiệu ứng đang gồng
    public void sendEffectCharge(Player player) {
        Skill skill = SkillUtil.getSkillbyId(player, Skill.TAI_TAO_NANG_LUONG);
     
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(1);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(skill.skillId);
       
            Service.gI().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            com.boyrock.utils.Logger.logException(EffectSkillService.class, e);
        }
    }

    // dừng gồng
    public void sendEffectStopCharge(Player player) {
        try {
            Message msg = new Message(-45);
            msg.writer().writeByte(3);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(-1);
            Service.gI().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            com.boyrock.utils.Logger.logException(EffectSkillService.class, e);
        }
    }

    // hiệu ứng nổ kết thúc gồng
    public void sendEffectEndCharge(Player player) {
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(5);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(player.playerSkill.skillSelect.skillId);
            Service.gI().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            com.boyrock.utils.Logger.logException(EffectSkillService.class, e);
        }
    }

    // hiệu ứng biến khỉ
    public void sendEffectMonkey(Player player) {
        Skill skill = SkillUtil.getSkillbyId(player, Skill.BIEN_KHI);
        
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(6);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(skill.skillId);
            Service.gI().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            com.boyrock.utils.Logger.logException(EffectSkillService.class, e);
        }
    }
    public void sendEffectBroly(Player player) {
        Skill skill = SkillUtil.getSkillbyId(player, Skill.BIEN_BROLY);
        
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(6);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort((short)97);
            Service.gI().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            com.boyrock.utils.Logger.logException(EffectSkillService.class, e);
        }
    }
    public void sendEffecttienhoa(Player player) {
        Skill skill = SkillUtil.getSkillbyId(player, Skill.TIEN_HOA);
        
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(6);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort((short)97);
            Service.gI().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            com.boyrock.utils.Logger.logException(EffectSkillService.class, e);
        }
    }

}
