package ru.kanban.service;

import ru.kanban.model.EpicTask;
import ru.kanban.model.Task;
import ru.kanban.model.SubTask;
import ru.kanban.model.TaskStatus;
import ru.kanban.service.TaskManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Manager implements TaskManager {
    protected HashMap<Integer, Task> tasks;
    protected HashMap<Integer, EpicTask> epics;
    protected HashMap<Integer, SubTask> subTasks;

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
    public Task createTask(Task task) {
        if (task == null) {
            return null;
        }
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public EpicTask createEpicTask(EpicTask epicTask) {
        if (epicTask == null) {
            return null;
        }
        epics.put(epicTask.getId(), epicTask);
        return epicTask;
    }

    @Override
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

    @Override
    public EpicTask getEpicById(int id) {
        EpicTask epic = epics.get(id);
        if (epic == null) {
            return null;
        }
        return epic;
    }

    @Override
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
}