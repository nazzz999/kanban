package ru.kanban.service;

import ru.kanban.exception.ValidationException;
import ru.kanban.model.*;

import java.util.*;

import static ru.kanban.util.Constants.INCORRECT_TASK_TYPE_MESSAGE;

public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Task> tasks;
    private Map<Integer, EpicTask> epics;
    private Map<Integer, SubTask> subTasks;
    private int generateId = 1;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
    }

    @Override
    public List<Task> getAllTasksByType(TaskType type) {
        switch (type) {
            case TASK -> {
                return new ArrayList<>(tasks.values());
            }
            case SUB_TASK -> {
                return new ArrayList<>(subTasks.values());
            }
            case EPIC_TASK -> {
                return new ArrayList<>(epics.values());
            }
            default -> throw new ValidationException(INCORRECT_TASK_TYPE_MESSAGE);
        }
    }

    @Override
    public void deleteAllTasksByType(TaskType type) {
        switch (type) {
            case TASK -> {
                tasks.clear();
            }
            case EPIC_TASK -> {
                epics.clear();
                subTasks.clear();
            }
            case SUB_TASK -> {
                subTasks.clear();
                for (EpicTask epicTask : epics.values()) {
                    epicTask.updateStatus();
                }
            }
            default -> throw new ValidationException(INCORRECT_TASK_TYPE_MESSAGE);
        }
    }

    @Override
    public Optional<Task> createTask(Task task) {
        TaskType type = task.getType();
        switch (type) {
            case TASK -> {
                return addTask(task);
            }
            case SUB_TASK -> {
                return createSubTask(task);
            }
            case EPIC_TASK -> {
                return createEpicTask(task);
            }
            default -> throw new ValidationException(INCORRECT_TASK_TYPE_MESSAGE);
        }
    }

    @Override
    public Task getTaskByIdAndType(Task task) {
        TaskType type = task.getType();
        switch (type) {
            case TASK -> {
                return getTaskById(task.getId());
            }
            case SUB_TASK -> {
                return getSubTaskById(task.getId());
            }
            case EPIC_TASK -> {
                return getEpicById(task.getId());
            }
            default -> throw new ValidationException(INCORRECT_TASK_TYPE_MESSAGE);
        }
    }

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
    public Task deleteTaskByIdAndType(Task task) {
        TaskType type = task.getType();
        switch (type) {
            case TASK -> {
                return deleteTaskById(task.getId());
            }
            case SUB_TASK -> {
                return deleteSubTaskId(task.getId());
            }
            case EPIC_TASK -> {
                return deleteEpicTaskId(task.getId());
            }
            default -> throw new ValidationException(INCORRECT_TASK_TYPE_MESSAGE);
        }
    }

    public Task deleteTaskById(int id) {
        return tasks.remove(id);
    }

    public SubTask deleteSubTaskId(int id) {
        return subTasks.remove(id);
    }

    public EpicTask deleteEpicTaskId(int id) {
        return epics.remove(id);
    }

    private Optional<Task> addTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return Optional.of(task);
    }

    private Optional<Task> createEpicTask(Task task) {
        EpicTask epicTask = (EpicTask) task;
        epicTask.setId(generateId++);
        epics.put(epicTask.getId(), epicTask);
        epicTask.updateStatus();
        return Optional.of(epicTask);
    }

    private Optional<Task> createSubTask(Task task) {
        SubTask subTask = (SubTask) task;
        EpicTask epicTask = subTask.getEpic();
        if (epics.containsKey(epicTask.getId())) {
            subTask.setId(generateId++);
            epicTask.addSubTask(subTask);
            subTasks.put(subTask.getId(), subTask);
            epicTask.updateStatus();
            return Optional.of(subTask);
        }
        return Optional.empty();
    }

    private int generateId() {
        return generateId++;
    }
}