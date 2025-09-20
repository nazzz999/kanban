package ru.kanban.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TaskTest {

    @Test
    void TaskGetter() {
        Task task = new Task(1, "Name", "Desc", TaskStatus.NEW, TaskType.TASK);
        assertThat(task.getId()).isEqualTo(1);
        assertThat(task.getName()).isEqualTo("Name");
        assertThat(task.getDescription()).isEqualTo("Desc");
        assertThat(task.getStatus()).isEqualTo(TaskStatus.NEW);
        assertThat(task.getTaskType()).isEqualTo(TaskType.TASK);
    }

    @Test
    void TaskGetterIdZero() {
        Task task = new Task("HomeWork", "Tomorrow", TaskStatus.NEW, TaskType.TASK);
        assertThat(task.getId()).isZero();
        assertThat(task.getName()).isEqualTo("HomeWork");
        assertThat(task.getDescription()).isEqualTo("Tomorrow");
        assertThat(task.getStatus()).isEqualTo(TaskStatus.NEW);
        assertThat(task.getTaskType()).isEqualTo(TaskType.TASK);
    }

    @Test
    void TaskSetter() {
        Task task = new Task();
        task.setId(1);
        task.setName("Cleaning");
        task.setDescription("Today");
        task.setStatus(TaskStatus.NEW);
        task.setTaskType(TaskType.TASK);

        assertThat(task.getId()).isEqualTo(1);
        assertThat(task.getName()).isEqualTo("Cleaning");
        assertThat(task.getDescription()).isEqualTo("Today");
        assertThat(task.getStatus()).isEqualTo(TaskStatus.NEW);
        assertThat(task.getTaskType()).isEqualTo(TaskType.TASK);
    }
}