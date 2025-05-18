package ru.kanban.service;

import ru.kanban.model.EpicTask;
import ru.kanban.model.Task;
import ru.kanban.model.SubTask;

import java.util.List;

public interface TaskManager {

    List<Task> getAllTasks();
    List<EpicTask> getAllEpicTasks();
    List<SubTask> getAllSubTasks();

    void deleteAllTasks();
    void deleteAllEpics();
    void deleteAllSubTasks();

    Task getTaskById(int id);

    Task createTask(Task task);

    void updateTask(Task task);

    void deleteTaskById(int id);
}
