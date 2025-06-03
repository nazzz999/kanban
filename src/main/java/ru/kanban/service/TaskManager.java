package ru.kanban.service;

import ru.kanban.model.EpicTask;
import ru.kanban.model.Task;
import ru.kanban.model.SubTask;
import ru.kanban.model.TaskType;

import java.util.List;

public interface TaskManager {

    List<Task> getAllTasksByType(TaskType type);

    void deleteAllTasksByType(TaskType type);

    Task getTaskById(int id);

    Task createTask(Task task);

    void updateTask(Task task);

    void deleteTaskById(int id);
}
