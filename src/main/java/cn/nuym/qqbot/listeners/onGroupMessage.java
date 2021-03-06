package cn.nuym.qqbot.listeners;

import cn.nuym.qqbot.QQBot;
import cn.nuym.qqbot.api.litebans.LitebansAPI;
import cn.nuym.qqbot.api.litebans.manager.HistoryManager;
import cn.nuym.qqbot.api.litebans.punishment.Punishment;
import litebans.api.Database;
import me.dreamvoid.miraimc.api.MiraiBot;
import me.dreamvoid.miraimc.bukkit.event.message.passive.MiraiGroupMessageEvent;
import me.dreamvoid.miraimc.httpapi.MiraiHttpAPI;
import me.dreamvoid.miraimc.httpapi.exception.AbnormalStatusException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.regex.Pattern;

@SuppressWarnings("all")
public class onGroupMessage implements Listener {
    private final QQBot plugin;
    LitebansAPI api = new LitebansAPI(Database.get());
    HistoryManager historyManager = api.getHistoryManager();
    public onGroupMessage(QQBot plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGroupMessageReceive(MiraiGroupMessageEvent e){
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getConfig().getLongList("bot.bot-accounts").forEach(bot -> plugin.getConfig().getLongList("bot.group-ids").forEach(group -> {
                    try {
        try {
            if (e.getMessage().startsWith("#")||e.getMessage().startsWith("＃")) {
                String command = e.getMessage().replaceFirst("#", "").replaceFirst("＃","");
                String msg = e.getMessage();
                String[] args = msg.split(" ");
                if (command.startsWith("help")) {
                    qqmessage(
                            "#help - 使用帮助命令\n" +
                            "#unban [玩家] - 解封\n" +
                            "#ban [玩家] [原因] [时间] - 封禁玩家\n" +
                            "#ipban [玩家] [原因] [时间] - IP封禁玩家\n" +
                            "#kick [玩家] [原因] - 踢出玩家\n" +
                            "#mute [玩家] [原因] [时间] - 禁言玩家\n" +
                            "#unmute [玩家] - 解除玩家禁言\n" +
                            "#sc [内容] - 游戏内STAFF全体消息\n" +
                            "#reload - 重载机器人\n"+
                            "#saying - 每日一言\n"+
                            "#checkban [玩家] - 检查玩家的封禁/禁言\n"+
                            "#badd [玩家] - 将玩家加入解封黑名单\n"+
                             "#bdel [玩家] - 将玩家移除解封黑名单\n"+
                             "#cps [玩家] - 将玩家添加到自证组\n"+
                            "严格执法 规范执法 文明执法 廉洁执法\n" +


                            "");
                }
                if (command.startsWith("reload")&&e.getSenderID()==1006800345){
                    qqmessage("我草，拼了！");
                    qqcommand("ezutils reload qqbot");
                }
                if (command.startsWith("saying")){
                qqmessage(QQBot.sayings());
                }
                if (command.startsWith("cps")){
                    String sender = String.valueOf(e.getSenderID());
                    if (sender.contains("1006800345")|| sender.contains("2768252265")){
                        String name = command.replaceAll("cps ","");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"lp user "+name+" parent add cps");
                        qqmessage("已将玩家 "+name+ " 添加到CPS自证组");
                    }else{
                        qqmessage("你没有执行此命令的权限，如果您有问题，请联系高管。");
                    }
                }
                if (command.startsWith("saying")){
                    qqmessage(QQBot.sayings());
                }

                if (command.contains("badd")){
                    ArrayList<String> list = (ArrayList<String>) plugin.getConfig().getStringList("block");
                    String name = command.replaceAll("badd ","");
                    list.add(name);
                    plugin.getConfig().set("block", list);
                    plugin.saveConfig();
                    plugin.reloadConfig();
                    qqmessage("已将 "+name+" 加入解封黑名单");
                    return;
                }
                if (command.contains("bdel")){
                    ArrayList<String> list = (ArrayList<String>) plugin.getConfig().getStringList("block");
                    String name = command.replaceAll("bdel ","");
                    list.remove(name);
                    plugin.getConfig().set("block", list);
                    plugin.saveConfig();
                    plugin.reloadConfig();
                    qqmessage("已将 "+name+" 移除解封黑名单");
                    return;
                }
                if (command.startsWith("ban") || command.startsWith("unmute") || command.startsWith("unban") || command.startsWith("mute") || command.startsWith("kick")||command.startsWith("ipban")) {
                  if (e.getMessage().contains("[") || e.getMessage().contains("]")||e.getMessage().contains("{")||e.getMessage().contains("}")||panduan(command)||e.getMessage().contains("-")){
                        return;
                    }


                    if (dad(command)&&command.contains("ban")){
                      qqmessage("你爹也敢ban？");
                      return;
                    }
                    if (command.contains("unban")){
                        String name = command.replaceAll("unban ","");
                        if(plugin.getConfig().getStringList("block").contains(name)){
                            qqmessage("玩家 "+name+" 在黑名单列表中，不予解封。");
                           return;
                        }
                    }
                    //qqmessage(String.valueOf(e.getSenderID()));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command);
                            success(e);
                        }
                    }.runTask(plugin);
                    if (args[0].contains("ban")||args[0].contains("ipban")||args[0].contains("banip")) {
                        if (args[0].contains("unban")){
                            return;
                        }
                        String name = args[1];
                        ArrayList<String> list = (ArrayList<String>) plugin.getConfig().getStringList("block");
                        list.add(name);
                        plugin.getConfig().set("block", list);
                        plugin.saveConfig();
                        plugin.reloadConfig();
                        qqmessage("已将 "+name+" 封禁，并加入解封黑名单");

                    }

                }


                if (command.startsWith("sc")){
                   String command_1 = command.replaceFirst("sc","litebans broadcast &b[工作人员喊话] &r");
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_1);
                            success(e);
                        }
                    }.runTask(plugin);
                }
                if (command.startsWith("开摆")&&e.getSenderID()==1006800345){
                    qqmessage("去你妈的，开摆！");
                    String command_1 = command.replaceFirst("开摆","ezutils unload qqbot");
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command_1);
                            success(e);
                        }
                    }.runTask(plugin);
                }
                if (command.startsWith("checkban")){
                    //OfflinePlayer player = Bukkit.getOfflinePlayer(command.replaceAll("checkban ",""));
                    //UUID uuid = player.getUniqueId();
                    String name = command.replaceAll("checkban ","");
                   try (PreparedStatement st =Database.get().prepareStatement("SELECT * FROM {history} WHERE name=?")){
                       st.setString(1,name.toString());
                       try(ResultSet rs = st.executeQuery()){
                           if (rs.next()){
                               /*
                               UUID uuid = UUID.fromString(rs.getString("uuid"));
                               String reason = rs.getString("reason");
                               String bannedByUuid = rs.getString("banned_by_uuid");
                               long time = rs.getLong("time");
                               long until = rs.getLong("until");
                               long id = rs.getLong("id");
                               boolean active = rs.getBoolean("active");
                               qqmessage(uuid.toString()+" "+reason+" "+bannedByUuid+" "+time+" "+"until"+ " "+id);

                                */
                               //qqmessage(rs.getString("uuid"));


                               UUID uuid = UUID.fromString(rs.getString("uuid"));
                               Boolean checkban = Database.get().isPlayerBanned(uuid, null);
                               Boolean checkmute = Database.get().isPlayerMuted(uuid, null);
                               //String reason = rs.getString("reason");

                               qqmessage("玩家 "+name+"\n是否被封禁："+checkban.toString().replaceAll("true","是").replaceAll("false","否")+"\n是否被禁言: "+checkmute.toString().replaceAll("true","是").replaceAll("false","否"));
                           }
                       }

                   }
                   //Boolean checkban = Database.get().isPlayerBanned(uuid, null);
                      //qqmessage(checkban.toString());

                }

                if (command.startsWith("getuuid")){
                    String name = command.replaceAll("getuuid ","");
                    try (PreparedStatement st =Database.get().prepareStatement("SELECT * FROM {history} WHERE name=?")){
                        st.setString(1,name.toString());
                        try(ResultSet rs = st.executeQuery()){
                            if (rs.next()){
                                UUID uuid = UUID.fromString(rs.getString("uuid"));

                                qqmessage("玩家 "+name+" 的UUID是\n"+uuid.toString());
                            }
                        }

                    }
                }
                if(command.startsWith("reason")){
                    String name = command.replaceAll("reason ","");
                    try (PreparedStatement st =Database.get().prepareStatement("SELECT * FROM {history} WHERE name=?")){
                        st.setString(1,name.toString());
                        try(ResultSet rs = st.executeQuery()){
                            if (rs.next()){
                                UUID uuid = UUID.fromString(rs.getString("uuid"));
                                List<Punishment> punishments = historyManager.getBans(uuid);
                                qqmessage(punishments.toString());

                            }
                        }

                    }
                }
            }
        }catch (Exception exception){

        }

    }catch (NoSuchElementException e) {
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
        }.runTaskAsynchronously(plugin);}


    private void success(MiraiGroupMessageEvent e) {

        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getConfig().getLongList("bot.bot-accounts").forEach(bot -> plugin.getConfig().getLongList("bot.group-ids").forEach(group -> {
                    try {
                        MiraiBot.getBot(e.getBotID()).getGroup(e.getGroupID()).sendMessageMirai("[mirai:at:"+e.getSenderID()+"] 666");
                        //MiraiBot.getBot(bot).getGroup(group).sendMessageMirai("666");
                    } catch (NoSuchElementException ebot) {
                        if (MiraiHttpAPI.Bots.containsKey(bot)) {
                            try {
                                MiraiBot.getBot(e.getBotID()).getGroup(e.getGroupID()).sendMessageMirai("[mirai:at:"+e.getSenderID()+"] 你已执行此命令");
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
    private UUID getuuid(String name){

        try (PreparedStatement st =Database.get().prepareStatement("SELECT * FROM {history} WHERE name=?")){
            st.setString(1,name.toString());
            try(ResultSet rs = st.executeQuery()){
                if (rs.next()){
                    UUID uuid = UUID.fromString(rs.getString("uuid"));

                    return uuid;
                }
            }

        } catch (SQLException throwables) {
            return null;
        }
        return null;
    }

    private void getbanreason(String uuid){
        String query = "SELECT * FROM {bans} WHERE uuid=?";
        try (PreparedStatement st = Database.get().prepareStatement(query)) {
            st.setString(1, uuid);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    String reason = rs.getString("reason");
                    qqmessage(reason);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private void qqcommand(String command){
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command);
            }
        }.runTask(plugin);
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
    public boolean panduan (String command){
        String pattern = "/^[a-zA-Z]{1}([a-zA-Z0-9]|[._])";
        return Pattern.compile(pattern).matcher(command).matches();
    }
    public boolean dad (String command){
        String command_1 = command.toLowerCase();
        if (command_1.contains("nuymakstone") || command_1.contains("encryptsp")|| command_1.contains("jiaoshou520")){
            return true;
        }else{
        return false;}
    }
}
