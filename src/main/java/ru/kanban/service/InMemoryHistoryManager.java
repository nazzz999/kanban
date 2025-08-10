package ru.kanban.service;

import ru.kanban.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> historyTask = new LinkedList<>();
    private static final int HISTORY_SIZE = 10;
    private static final int FIRST_INDEX_IN_HISTORY = 0;

    @Override
    public void add(Task task) {
        if (historyTask.size() >= HISTORY_SIZE) {
            historyTask.remove(FIRST_INDEX_IN_HISTORY);
        }
        historyTask.add(task);

    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyTask);
    }
}
