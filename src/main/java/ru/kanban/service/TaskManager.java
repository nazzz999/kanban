package ru.kanban.service;

import ru.kanban.model.Task;
import ru.kanban.model.SubTask;
import ru.kanban.model.TaskType;

import java.util.List;
import java.util.Optional;

public interface TaskManager {

    Optional<Task> createTask(Task task);

    List<Task> getAllTasksByType(TaskType type);

    Task getTaskByIdAndType(Task task);

    void deleteAllTasksByType(TaskType type);

    Task deleteTaskByIdAndType(Task task);

    void updateTask(Task task);
}
