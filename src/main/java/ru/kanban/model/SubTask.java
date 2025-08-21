package ru.kanban.model;

public class SubTask extends Task {
    private EpicTask epic;

    public SubTask(int id, String name, String description, TaskStatus status, TaskType taskType, EpicTask epic) {
        super(id, name, description, status, taskType);
        this.epic = epic;
    }

    public SubTask() {

    }

    public EpicTask getEpic() {
        return epic;
    }

    public void setEpic(EpicTask epic) {
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
