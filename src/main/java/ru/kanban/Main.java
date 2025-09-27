package ru.kanban;

import ru.kanban.model.Task;
import ru.kanban.model.TaskStatus;
import ru.kanban.model.TaskType;
import ru.kanban.service.InMemoryTaskManager;
import ru.kanban.service.TaskManager;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Integer> sum = new ArrayList<>();
        TaskManager taskManager = new InMemoryTaskManager();
        Task task = new Task("Programming tasks", "Solve 10 tasks", TaskStatus.NEW, TaskType.TASK);
        System.out.println(taskManager.createTask(task));
        System.out.println(taskManager.getAllTasksByType(null));
    }
}