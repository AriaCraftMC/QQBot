package cn.nuym.qqbot.api.litebans;

import cn.nuym.qqbot.api.litebans.manager.HistoryManager;
import litebans.api.Database;

public class LitebansAPI {

    private final HistoryManager historyManager;
    public LitebansAPI(Database database) {
        historyManager = new HistoryManager(database);
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }
}
