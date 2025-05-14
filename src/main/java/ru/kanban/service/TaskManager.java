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
    EpicTask getEpicById(int id);
    SubTask getSubTaskById(int id);

    Task createTask(Task task);
    EpicTask createEpicTask(EpicTask epicTask);
    SubTask createSubTask(SubTask subTask);

    void updateTask(Task task);
    void updateEpicTask(EpicTask epicTask);
    void updateSubTask(SubTask subTask);

    void deleteTaskById(int id);
    void deleteEpicById(int id);
    void deleteSubTaskById(int id);
}
