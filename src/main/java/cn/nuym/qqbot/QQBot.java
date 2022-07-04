package cn.nuym.qqbot;

import cn.nuym.qqbot.listeners.onGroupMessage;
import me.dreamvoid.miraimc.api.MiraiBot;
import me.dreamvoid.miraimc.httpapi.MiraiHttpAPI;
import me.dreamvoid.miraimc.httpapi.exception.AbnormalStatusException;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.NoSuchElementException;

public final class QQBot extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(new onGroupMessage(this), this);
        saveDefaultConfig();
        reloadConfig();
        qqmessage(sayings());
        Bukkit.getScheduler().runTaskTimer(this,
                new Runnable() {
                    public void run() {
                        qqmessage(sayings());
                    }
                }, 20 * 60 * 10L, 20 * 60 * 10L);
    }


    public static String sayings() {
    String[] slist = {"“四月樱花，洇染成殇。”",
            "Dimmed by April,the sakura of memory is covered by the dust of time.",
            "我 再 也 沒 有 機 會 與 妳 在 雨 中 穿 行",
            "成功时看得见功成名就的正剧英雄失败时却看不见壮志未酬的悲剧英雄",
            "究竟谁是英雄 谁是狗熊",
             "分明是英雄相惜，棋逢对手，为何还要分出个成王败寇。",
    "英雄往往犹如一道流星划过天际，让我们潸然泪下",
    "多少气吞山河的丰功伟业，在时间的涤荡下却灰飞烟灭。",
    "成功时看得见功成名就的正剧英雄,失败时却看不见壮志未酬的悲剧英雄。",
    "能够和你这样的人相遇，对我毫无疑问来说是奇迹了。",
    "I love three things in this world. Sun, Moon and You. Sun for morning, Moon for night, and You forever.",
    "这个世界虽然不完美,但我们仍可以治愈自己.",
    "我喜欢你，朱云可！",
    "樱花你又在看tips了哦",
    "天上飘过一只鸽子，那是谁？是你爹血风！",
    "Pig Never Dies!",
    "花火が瞬く夜に",
    "四月樱花 洇染成殇",
    "吃花椒的喵酱是我的！",
    "王冰冰是我的！",
    "天好冷，我的心都是冰冰的！",
    "青蛙是什么玩意",
    "咕咕咕~",
    "你是男同吗？",
    "樱花你就是个寄！",
    "害怕那条河最后和女孩还是错过了。她走向现实世界，带着她一生无法忘怀的河流的名字，而他最后也只是在红桥上远远的思念中想起她来。2001年，千与千寻公映，而时至今日，千寻和白龙又怎样活着呢？无论如何，我们都很想念你们。",
    "人生就是一列开往坟墓的列车，路途上会有很多站，很难有人可以自始至终陪着走完。当陪你的人要下车时，即使不舍也该心存感激，然后挥手道别。 ——宫崎骏《千与千寻》",
    "为什么我永远追不上你呢？云可？是我不优秀吗？",
    "为什么每首歌都要扯上你们失败的爱情",
    "我太天真了 我还以为我很会看人呢",
    "看那樱花，正落得惊艳缤纷。",
    "群里的那个安徽人好帅！",
    "你们又在看tips了哦"};
    int oneLength = slist.length;
    int rand1 = (int)(Math.random()*oneLength);
    return slist[rand1];

    }
    private void qqmessage(String message){
    new BukkitRunnable() {
        @Override
        public void run() {
            getConfig().getLongList("bot.bot-accounts").forEach(bot -> getConfig().getLongList("bot.group-ids").forEach(group -> {
                try {
                    MiraiBot.getBot(bot).getGroup(group).sendMessageMirai(sayings());
                } catch (NoSuchElementException e) {
                    if (MiraiHttpAPI.Bots.containsKey(bot)) {
                        try {
                            MiraiHttpAPI.INSTANCE.sendGroupMessage(MiraiHttpAPI.Bots.get(bot), group, "666");
                        } catch (IOException | AbnormalStatusException ex) {
                            getLogger().warning("使用" + bot + "发送消息时出现异常，原因: " + ex);
                        }
                    } else getLogger().warning("指定的机器人" + bot + "不存在，是否已经登录了机器人？");
                }
            }));
        }
    }.runTaskAsynchronously(this);
}

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
