package com.lulakssoft.activitymanagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryManager {

    private static final HistoryManager INSTANCE = new HistoryManager();

    private final List<String> historyLogs = new ArrayList<>();

    private HistoryManager() {}

    public static HistoryManager getInstance() {
        return INSTANCE;
    }

    public void addLogEntry(String entry) {
        historyLogs.add(entry);
    }

    public List<String> getHistoryLogs() {
        return Collections.unmodifiableList(historyLogs);
    }
}
