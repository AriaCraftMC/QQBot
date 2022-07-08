package cn.nuym.qqbot;

import cn.nuym.qqbot.api.litebans.LitebansAPI;
import cn.nuym.qqbot.api.litebans.listeners.LiteBansListener;
import cn.nuym.qqbot.api.litebans.manager.HistoryManager;
import cn.nuym.qqbot.api.litebans.punishment.Punishment;
import cn.nuym.qqbot.listeners.onGroupMessage;
import litebans.api.Database;
import me.dreamvoid.miraimc.api.MiraiBot;
import me.dreamvoid.miraimc.httpapi.MiraiHttpAPI;
import me.dreamvoid.miraimc.httpapi.exception.AbnormalStatusException;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public final class QQBot extends JavaPlugin {

    private LiteBansListener liteBansListener;

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
        this.liteBansListener = new LiteBansListener(this);
        liteBansListener.registerEvent();
    }


    public static String sayings() {
    String[] slist = {"“四月樱花，洇染成殇。”",
            "Dimmed by April,the sakura of memory is covered by the dust of time.",
            "君子赠人以言，庶人赠人以财。",
            "使意志获得自由的唯一途径，就是让意志摆脱任性。",
            "如烟往事俱忘却，心底无私天地宽。",
            "执着追求并从中得到最大快乐的人，才是成功者。",
            "有百折不挠的信念的所支持的人的意志，比那些似乎是无敌的物质力量有更强大的威力。",
            "生活在我们这个世界里，不读书就完全不可能了解人。",
            "一个人即使已登上顶峰，也仍要自强不息。",
            "对具有高度自觉与深邃透彻的心灵的人来说，痛苦与烦恼是他必备的气质。",
            "先相信你自己，然后别人才会相信你。",
            "上天赋予的生命，就是要为人类的繁荣和平和幸福而奉献。",
            "猝然死去本无甚苦痛，长期累死倒真难以忍受。",
            "人需要真理，就象瞎子需要明眼的引路人一样。",
            "我这个人走得很慢，但是我从不后退。",
            "我是炎黄子孙，理所当然地要把学到的知识全部奉献给我亲爱的祖国。",
            "你若要喜爱你自己的价值，你就得给世界创造价值。",
            "意志是一个强壮的盲人，倚靠在明眼的跛子肩上。",
            "永远不要因承认错误而感到羞耻，因为承认错误也可以解释作你今天更聪敏。",
            "我死国生，我死犹荣，身虽死精神长生，成功成仁，实现大同。",
            "我怀着比对我自己的生命更大的尊敬、神圣和严肃，去爱国家的利益。",
            "预测未来最好的方法就是把它创造出来",
            "但愿每次回忆，对生活都不感到负疚",
            "我们若已接受最坏的，就再没有什么损失。",
            "只要你具备了精神气质的美，只要你有这样的自信，你就会拥有风度的自然之美。",
            "要在这个世界上获得成功，就必须坚持到底：至死都不能放手。",
            "有了坚定的意志，就等于给双脚添了一对翅膀。",
            "恢弘志士之气，不宜妄自菲薄。",
            "我重视祖国的利益，甚于自己的生命和我所珍爱的儿女。",
            "想不付出任何代价而得到幸福，那是神话。",
            "宿命论是那些缺乏意志力的弱者的借口。",
            "只有永远躺在泥坑里的人，才不会再掉进坑里。",
            "世间的活动，缺点虽多，但仍是美好的。",
            "今天应做的事没有做，明天再早也是耽误了。",
            "忍耐和坚持虽是痛苦的事情，但却能渐渐地为你带来好处。",
            "自古奇人伟士，不屈折于忧患，则不足以其学。",

    };
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
