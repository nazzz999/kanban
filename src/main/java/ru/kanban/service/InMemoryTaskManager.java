package ru.kanban.service;

import ru.kanban.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Task> tasks;
    private Map<Integer, EpicTask> epics;
    private Map<Integer, SubTask> subTasks;
    private int generateId = 0;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<EpicTask> getAllEpicTasks() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        subTasks.clear();
    }

    @Override
    public Task createTask(Task task) {
       if (task.getTaskType() == TaskType.TASK) {
            return addTask(task);
       }
       return null;
    }

    public EpicTask createEpicTask(EpicTask epicTask) {
        if (epicTask == null) {
            return null;
        }
        epics.put(epicTask.getId(), epicTask);
        return epicTask;
    }

    public SubTask createSubTask(SubTask subTask) {
        if (subTask == null) {
            return null;
        }
        subTasks.put(subTask.getId(), subTask);
        return subTask;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            return null;
        }
        return task;
    }

    public EpicTask getEpicById(int id) {
        EpicTask epic = epics.get(id);
        if (epic == null) {
            return null;
        }
        return epic;
    }

    public SubTask getSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        if (subTask == null) {
            return null;
        }
        return subTask;
    }

    @Override
    public void updateTask(Task task) {
        if (task == null) {
            System.out.println("Task is null");
            return;
        }
        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.NEW);
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public void deleteTaskById(int id) {

    }

    private Task addTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    private int generateId() {
        return ++generateId;
    }
}