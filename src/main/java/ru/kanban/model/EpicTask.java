package ru.kanban.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EpicTask extends Task {
    private final List<SubTask> subTasks = new ArrayList<>();

    public EpicTask(int id, String name, String description, TaskStatus status, TaskType taskType) {
        super(id, name, description, status, taskType);
    }

    public EpicTask() {

    }

    public void updateStatus() {
        boolean taskNew = true;
        boolean taskDone = true;

        if (subTasks.isEmpty()) {
            this.status = TaskStatus.NEW;
            return;
        }
        for (SubTask subTask : subTasks) {
            TaskStatus s = subTask.getStatus();
            if (s != TaskStatus.NEW)  taskNew  = false;
            if (s != TaskStatus.DONE) taskDone = false;
            if (!taskNew && !taskDone) break;
        }

        if (taskNew) {
            this.status = TaskStatus.NEW;
        } else if (taskDone) {
            this.status = TaskStatus.DONE;
        } else {
            this.status = TaskStatus.IN_PROGRESS;
        }
    }

    public List<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks);
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
    }

    public void deleteSubTask(SubTask subTask) {
        subTasks.remove(subTask);

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
