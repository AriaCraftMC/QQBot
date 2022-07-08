package cn.nuym.qqbot.api.litebans.listeners;

import cn.nuym.qqbot.QQBot;
import litebans.api.Entry;
import litebans.api.Events;
import me.dreamvoid.miraimc.api.MiraiBot;
import me.dreamvoid.miraimc.httpapi.MiraiHttpAPI;
import me.dreamvoid.miraimc.httpapi.exception.AbnormalStatusException;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.UUID;

public class LiteBansListener {
    private QQBot plugin;

    public LiteBansListener(QQBot plugin) {
        this.plugin = plugin;
    }

    public void registerEvent() {
        Events.get().register(new Events.Listener() {
            @Override
            public void entryAdded(Entry entry) {
                String playerName = getPlayerName(entry.getUuid());
                String issuerName = getPlayerName(entry.getExecutorUUID());
                switch (entry.getType()) {
                    case "ban":
                        qqmessage("玩家 "+playerName +" 已被服务器封禁");
                        break;
                    case "mute":
                        qqmessage("玩家 "+playerName +" 已被服务器禁言");
                        break;
                    case "warn":
                        qqmessage("玩家 "+playerName +" 已被服务器警告");
                        break;
                    case "kick":
                        qqmessage("玩家 "+playerName +" 已被服务器踢出");
                        break;

                }

            }


        });
    }

    public String getPlayerName(String uuid) {
        if (uuid.equalsIgnoreCase("console")) {
            return "console";
        }

        return Bukkit.getPlayer(UUID.fromString(uuid)) != null ?
                Bukkit.getPlayer(UUID.fromString(uuid)).getName()
                : Bukkit.getOfflinePlayer((UUID.fromString(uuid))).getName();
    }

    private void qqmessage(String message) {

        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getConfig().getLongList("bot.bot-accounts").forEach(bot -> plugin.getConfig().getLongList("bot.group-ids").forEach(group -> {
                    try {
                        MiraiBot.getBot(bot).getGroup(group).sendMessageMirai(message);
                    } catch (NoSuchElementException e) {
                        if (MiraiHttpAPI.Bots.containsKey(bot)) {
                            try {
                                MiraiHttpAPI.INSTANCE.sendGroupMessage(MiraiHttpAPI.Bots.get(bot), group, "666");
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
