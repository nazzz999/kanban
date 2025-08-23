package ru.kanban.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.kanban.model.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class InMemoryTaskManagerTest {

    private InMemoryTaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    private Task newTask(String name) {
        Task task = new Task();
        task.setName(name);
        task.setDescription(name);
        task.setStatus(TaskStatus.NEW);
        task.setTaskType(TaskType.TASK);
        return task;
    }

    private EpicTask newEpic(String name) {
        EpicTask epicTask = new EpicTask();
        epicTask.setName(name);
        epicTask.setDescription(name);
        epicTask.setTaskType(TaskType.EPIC_TASK);
        return epicTask;
    }

    private SubTask newSubTask(String name, EpicTask epicTask) {
        SubTask subTask = new SubTask();
        subTask.setName(name);
        subTask.setDescription(name);
        subTask.setStatus(TaskStatus.NEW);
        subTask.setTaskType(TaskType.SUB_TASK);
        subTask.setEpic(epicTask);
        return subTask;
    }

    @Test
    void createSimpleTask() {
        Optional<Task> createdTask = taskManager.createTask(newTask("Task number one"));
        assertThat(createdTask).isPresent();
        Task task = createdTask.get();
        assertThat(task.getId()).isPositive();
        assertThat(task.getName()).isEqualTo("Task number one");
        assertThat(task.getType()).isEqualTo(TaskType.TASK);
    }

    @Test
    void createEpicTask() {
        Optional<Task> createdEpic = taskManager.createTask(newEpic("Epic number one"));
        assertThat(createdEpic).isPresent();
        assertThat(createdEpic.get().getType()).isEqualTo(TaskType.EPIC_TASK);
    }

    @Test
    void createSubTaskWhenEpicExist() {
        EpicTask epicTask = (EpicTask) taskManager.createTask(newEpic("Epic number one")).get();
        Optional<Task> createdSub = taskManager.createTask(newSubTask("SubTask number one", epicTask));
        assertThat(createdSub).isPresent();
        assertThat(createdSub.get().getType()).isEqualTo(TaskType.SUB_TASK);
        List<Task> subTask = taskManager.getAllTasksByType(TaskType.SUB_TASK);
        assertThat(subTask).hasSize(1);
    }

    @Test
    void createSubTaskWhenEpicTaskReturnEmpty() {
        EpicTask epicTask = newEpic("Epic");
        epicTask.setId(179);
        Optional<Task> subTask = taskManager.createTask(newSubTask("SubTask", epicTask));
        assertThat(subTask).isEmpty();
    }

    @Test
    void getTaskByIdAndType() {
        Task task = (Task) taskManager.createTask(newTask("Task")).get();
        Optional<Task> theFoundTask = taskManager.getTaskByIdAndType(task);
        assertThat(theFoundTask).isPresent();
        assertThat(theFoundTask.get().getId()).isEqualTo(task.getId());
        assertThat(theFoundTask.get().getTaskType()).isEqualTo(task.getType());
    }

    @Test
    void getEpicByIdAndType() {
        EpicTask epicTask = (EpicTask) taskManager.createTask(newEpic("Epic")).get();
        Optional<Task> theFoundEpic = taskManager.getTaskByIdAndType(epicTask);
        assertThat(theFoundEpic).isPresent();
        assertThat(theFoundEpic.get().getId()).isEqualTo(epicTask.getId());
        assertThat(theFoundEpic.get().getTaskType()).isEqualTo(epicTask.getType());
    }

    @Test
    void getSubTaskByIdAndType() {
        EpicTask epicTask = (EpicTask) taskManager.createTask(newEpic("Epic")).get();
        SubTask subTask = (SubTask) taskManager.createTask(newSubTask("SubTask", epicTask)).get();
        Optional<Task> theFoundSubTask = taskManager.getTaskByIdAndType(subTask);
        assertThat(theFoundSubTask).isPresent();
        assertThat(theFoundSubTask.get().getId()).isEqualTo(subTask.getId());
        assertThat(theFoundSubTask.get().getTaskType()).isEqualTo(subTask.getType());
    }
}
