package ru.kanban.model;

import java.util.Objects;

public class SubTask extends Task {
    private int epic;

    public SubTask(int id, String name, String description, TaskStatus status, TaskType taskType, int epic) {
        super(id, name, description, status, taskType);
        this.epic = epic;
    }

    public int getEpic() {
        return epic;
    }

    public void setEpic(int epic) {
        this.epic = epic;
    }

    public TaskType getType() {
        return TaskType.SUB_TASK;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epic=" + epic +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", taskType=" + taskType +
                '}';
    }
}
