package ru.kanban.service;

import ru.kanban.model.Task;
import ru.kanban.model.SubTask;
import ru.kanban.model.TaskType;

import java.util.List;
import java.util.Optional;

public interface TaskManager {

    Optional<Task> createTask(Task task);

    List<Task> getAllTasksByType(TaskType type);

    Optional<Task> getTaskByIdAndType(Task task);

    void deleteAllTasksByType(TaskType type);

    void deleteTaskByIdAndType(int id,TaskType type);

    void updateTask(Task task);
}
