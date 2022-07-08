package cn.nuym.qqbot.api.litebans.manager;

import cn.nuym.qqbot.api.litebans.punishment.Punishment;
import litebans.api.Database;

import javax.xml.crypto.Data;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class HistoryManager {

    private Database database;

    public HistoryManager(Database database) {
        this.database = database;
    }

    public List<Punishment> getBans(UUID uuid) {
        List<Punishment> bans = new ArrayList<>();
        String query = "SELECT * FROM {bans} WHERE uuid=?";
        Database database = this.database;

        CompletableFuture.runAsync(() -> {
            try (PreparedStatement st = database.prepareStatement(query)) {
                st.setString(1, uuid.toString());
                try (ResultSet rs = st.executeQuery()) {
                    while (rs.next()) {
                        bans.add(new Punishment(
                                rs.getLong("id"),
                                rs.getString("uuid"),
                                rs.getString("ip"),
                                rs.getString("reason"),
                                rs.getString("banned_by_uuid"),
                                rs.getString("banned_by_name"),
                                rs.getLong("time"),
                                rs.getLong("until"),
                                rs.getShort("template"),
                                rs.getString("server_scope"),
                                rs.getString("server_origin"),
                                rs.getBoolean("silent"),
                                rs.getBoolean("ipban"),
                                rs.getBoolean("ipban_wildcard"),
                                rs.getBoolean("active")));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        return bans;
    }
}
