package ru.kanban.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EpicTask extends Task {
    private final List<Integer> subTasks = new ArrayList<>();

    public EpicTask(int id, String name, String description, TaskStatus status, TaskType taskType) {
        super(id, name, description, status, taskType);
    }

    public List<Integer> getSubTasks() {
        return subTasks;
    }

    public void addSubTask(Integer subTaskId) {
        subTasks.add(subTaskId);
    }

    public void deleteSubTask(Integer subTaskId) {
        subTasks.remove(subTaskId);
    }

    public void removeSubTasksList() {
        subTasks.clear();
    }

    public TaskType getType() {
        return TaskType.EPIC_TASK;
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "subTasks=" + subTasks +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", taskType=" + taskType +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        EpicTask epicTask = (EpicTask) object;
        return Objects.equals(subTasks, epicTask.subTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasks);
    }
}
