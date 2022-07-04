package cn.nuym.qqbot.listeners;

import cn.nuym.qqbot.QQBot;
import me.dreamvoid.miraimc.api.MiraiBot;
import me.dreamvoid.miraimc.bukkit.BukkitPlugin;
import me.dreamvoid.miraimc.bukkit.event.message.passive.MiraiGroupMessageEvent;
import me.dreamvoid.miraimc.httpapi.MiraiHttpAPI;
import me.dreamvoid.miraimc.httpapi.exception.AbnormalStatusException;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.util.NoSuchElementException;

@SuppressWarnings("all")
public class onGroupMessage implements Listener {
    private final QQBot plugin;

    public onGroupMessage(QQBot plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGroupMessageReceive(MiraiGroupMessageEvent e){
        try {
            if (e.getMessage().startsWith("#")||e.getMessage().startsWith("＃")) {
                String command = e.getMessage().replace("#", "").replace("＃","");
                if (command.startsWith("ban") || command.startsWith("unmute") || command.startsWith("unban") || command.startsWith("mute") || command.startsWith("kick")) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command);
                            success();
                        }
                    }.runTask(plugin);
                }
                if (command.startsWith("sc")){
                   String command_1 = command.replace("sc","litebans broadcast &b[工作人员喊话] &r");
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_1);
                            success();
                        }
                    }.runTask(plugin);
                }
            }
        }catch (Exception exception){

        }

    }

    private void success() {

        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getConfig().getLongList("bot.bot-accounts").forEach(bot -> plugin.getConfig().getLongList("bot.group-ids").forEach(group -> {
                    try {
                        MiraiBot.getBot(bot).getGroup(group).sendMessageMirai("1");
                    } catch (NoSuchElementException e) {
                        if (MiraiHttpAPI.Bots.containsKey(bot)) {
                            try {
                                MiraiHttpAPI.INSTANCE.sendGroupMessage(MiraiHttpAPI.Bots.get(bot), group, "1");
                            } catch (IOException | AbnormalStatusException ex) {
                                plugin.getLogger().warning("使用" + bot + "发送消息时出现异常，原因: " + ex);
                            }
                        } else plugin.getLogger().warning("指定的机器人" + bot + "不存在，是否已经登录了机器人？");
                    }
                }));
            }
        }.runTaskAsynchronously(plugin);

    }
}
