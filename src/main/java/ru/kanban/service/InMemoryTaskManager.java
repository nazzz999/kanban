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
    public Optional<Task> getTaskByIdAndType(Task task) {
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

    @Override
    public void updateTask(Task task) {
        if (task == null) {
            System.out.println("Task is null");
            return;
        }
        switch (task.getType()) {
            case TASK -> updateSimpleTask(task);
            case SUB_TASK -> updateSubTask((SubTask) task);
            case EPIC_TASK -> updateEpicTask((EpicTask) task);
            default -> throw new ValidationException(INCORRECT_TASK_TYPE_MESSAGE);
        }
    }

    @Override
    public void deleteTaskByIdAndType(int id, TaskType type) {
        switch (type) {
            case TASK -> {
                deleteTaskById(id);
            }
            case SUB_TASK -> {
                deleteSubTaskById(id);
            }
            case EPIC_TASK -> {
                deleteEpicTaskById(id);
            }
            default -> throw new ValidationException(INCORRECT_TASK_TYPE_MESSAGE);
        }
    }

    private void deleteTaskById(int id) {
        if (!tasks.containsKey(id)) {
            throw new ValidationException("Task with id " + id + " does not exist.");
        }
        tasks.remove(id);
    }

    private void deleteSubTaskById(int id) {
        SubTask subTask = (SubTask) subTasks.get(id);
        if (!subTasks.containsKey(id)) {
            throw new ValidationException("SubTask with id " + id + " does not exist.");
        }
        EpicTask epicTask = subTask.getEpic();
        subTasks.remove(id);
        epicTask.updateStatus();
    }

    private void deleteEpicTaskById(int id) {
        if (!epics.containsKey(id)) {
            throw new ValidationException("EpicTask with id " + id + " does not exist.");
        }
        epics.remove(id);
        subTasks.values().removeIf(subTask -> subTask.getEpic().getId() == id);
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

    private Optional<Task> getTaskById(int id) {
        return Optional.ofNullable(tasks.get(id));
    }

    private Optional<Task> getEpicById(int id) {
        return Optional.ofNullable(epics.get(id));
    }

    private Optional<Task> getSubTaskById(int id) {
        return Optional.ofNullable(subTasks.get(id));
    }

    private void updateSimpleTask(Task task) {
        if (!tasks.containsKey(task.getId())){
            throw new ValidationException("Task with id " + task.getId() + " does not exist.");
        }
        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.NEW);
        }
        tasks.put(task.getId(), task);
    }

    private void updateSubTask(SubTask subTask) {
        if (subTask == null) {
            throw new ValidationException("SubTask is null.");
        }

        int subTaskId   = subTask.getId();
        if (!subTasks.containsKey(subTaskId)) {
            throw new ValidationException("SubTask with id " + subTaskId + " does not exist.");
        }

        int newEpicId   = subTask.getEpic().getId();
        if (!epics.containsKey(newEpicId)) {
            throw new ValidationException("EpicTask with id " + newEpicId + " does not exist.");
        }

        SubTask  storedSubTask = subTasks.get(subTaskId);
        EpicTask oldEpic = storedSubTask.getEpic();
        EpicTask newEpic = epics.get(newEpicId);

        storedSubTask.setName(subTask.getName());
        storedSubTask.setDescription(subTask.getDescription());
        storedSubTask.setStatus(subTask.getStatus());

        if (oldEpic.getId() != newEpicId) {
            oldEpic.removeSubTasksList(storedSubTask);
            newEpic.addSubTask(storedSubTask);
            storedSubTask.setEpic(newEpic);
            oldEpic.updateStatus();
            newEpic.updateStatus();

        } else {
            oldEpic.updateStatus();
    }
}

    private void updateEpicTask(EpicTask epicTask) {
        if (!epics.containsKey(epicTask.getId())) {
            throw new ValidationException("Task with id " + epicTask.getId() + " does not exist.");
        }
        EpicTask existingEpic = epics.get(epicTask.getId());
        existingEpic.setName(epicTask.getName());
        existingEpic.setDescription(epicTask.getDescription());
        existingEpic.updateStatus();
    }
}
