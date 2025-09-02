package ru.kanban.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.kanban.exception.ValidationException;
import ru.kanban.model.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryTaskManagerTest {

    private InMemoryTaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
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
        Optional<Task> theFoundTask = taskManager.getTaskByIdAndType(task.getId(), task.getType());
        assertThat(theFoundTask).isPresent();
        assertThat(theFoundTask.get().getId()).isEqualTo(task.getId());
        assertThat(theFoundTask.get().getTaskType()).isEqualTo(task.getType());
    }

    @Test
    void getEpicByIdAndType() {
        EpicTask epicTask = (EpicTask) taskManager.createTask(newEpic("Epic")).get();
        Optional<Task> theFoundEpic = taskManager.getTaskByIdAndType(epicTask.getId(), epicTask.getTaskType());
        assertThat(theFoundEpic).isPresent();
        assertThat(theFoundEpic.get().getId()).isEqualTo(epicTask.getId());
        assertThat(theFoundEpic.get().getTaskType()).isEqualTo(epicTask.getType());
    }

    @Test
    void getSubTaskByIdAndType() {
        EpicTask epicTask = (EpicTask) taskManager.createTask(newEpic("Epic")).get();
        SubTask subTask = (SubTask) taskManager.createTask(newSubTask("SubTask", epicTask)).get();
        Optional<Task> theFoundSubTask = taskManager.getTaskByIdAndType(subTask.getId(), subTask.getType());
        assertThat(theFoundSubTask).isPresent();
        assertThat(theFoundSubTask.get().getId()).isEqualTo(subTask.getId());
        assertThat(theFoundSubTask.get().getTaskType()).isEqualTo(subTask.getType());
    }

    @Test
    void getAllTasksByTypeTask() {
        Task task = taskManager.createTask(newTask("Task")).get();
        List<Task> theFoundTask = taskManager.getAllTasksByType(task.getType());
        assertThat(theFoundTask.get(0).getId()).isEqualTo(task.getId());
        assertThat(theFoundTask.get(0).getType()).isEqualTo(task.getType());
    }

    @Test
    void getAllTasksByTypeEpic() {
        EpicTask epicTask = (EpicTask) taskManager.createTask(newEpic("Epic")).get();
        List<Task> theFoundEpic = taskManager.getAllTasksByType(epicTask.getType());
        assertThat(theFoundEpic.get(0).getId()).isEqualTo(epicTask.getId());
        assertThat(theFoundEpic.get(0).getType()).isEqualTo(epicTask.getType());
    }

    @Test
    void getAllTasksByTypeSubTask() {
        EpicTask epicTask = (EpicTask) taskManager.createTask(newEpic("Epic")).get();
        SubTask subTask = (SubTask) taskManager.createTask(newSubTask("SubTask", epicTask)).get();
        List<Task> theFoundSubTask = taskManager.getAllTasksByType(subTask.getType());
        assertThat(theFoundSubTask.get(0).getId()).isEqualTo(subTask.getId());
        assertThat(theFoundSubTask.get(0).getType()).isEqualTo(subTask.getType());
    }

    @Test
    void getTaskByIdAndTypeIsEmpty() {
        Task task = newTask("Task");
        task.setId(123);
        Optional<Task> newTask = taskManager.getTaskByIdAndType(task.getId(), task.getType());
        assertThat(newTask).isEmpty();
    }

    @Test
    void getEpicByIdAndTypeIsEmpty() {
        EpicTask epicTask = newEpic("Epic");
        epicTask.setId(123);
        Optional<Task> newEpic = taskManager.getTaskByIdAndType(epicTask.getId(), epicTask.getType());
        assertThat(newEpic).isEmpty();
    }

    @Test
    void getSubTaskByIdAndTypeIsEmpty() {
        EpicTask epicTask = newEpic("Epic");
        SubTask subTask = newSubTask("SubTask", epicTask);
        subTask.setId(123);
        Optional<Task> newSubTask = taskManager.getTaskByIdAndType(subTask.getId(), subTask.getType());
        assertThat(newSubTask).isEmpty();
    }

    @Test
    void deleteAllTasksByTypeTask() {
        Task task = taskManager.createTask(newTask("Task")).get();
        taskManager.deleteAllTasksByType(task.getType());
        assertThat(taskManager.getAllTasksByType(TaskType.TASK)).isEmpty();
    }

    @Test
    void deleteAllTasksByTypeEpicTask() {
        EpicTask epicTask = (EpicTask) taskManager.createTask(newEpic("Epic")).get();
        taskManager.deleteAllTasksByType(epicTask.getType());
        assertThat(taskManager.getAllTasksByType(TaskType.EPIC_TASK)).isEmpty();
    }

    @Test
    void deleteAllTasksByTypeSubTask() {
        EpicTask epicTask = (EpicTask) taskManager.createTask(newEpic("Epic")).get();
        SubTask subTask = (SubTask) taskManager.createTask(newSubTask("SubTask", epicTask)).get();
        taskManager.deleteAllTasksByType(subTask.getType());
        assertThat(taskManager.getAllTasksByType(TaskType.SUB_TASK)).isEmpty();
    }

    @Test
    void deleteTasksByIdAndTypeTask() {
        Task task = taskManager.createTask(newTask("Task")).get();
        taskManager.deleteTaskByIdAndType(task.getId(), task.getType());
        assertThat(taskManager.getTaskByIdAndType(task.getId(), task.getType())).isEmpty();
    }

    @Test
    void deleteTasksByIdAndTypeEpic() {
        EpicTask epicTask = (EpicTask) taskManager.createTask(newEpic("Epic")).get();
        taskManager.deleteTaskByIdAndType(epicTask.getId(), epicTask.getType());
        assertThat(taskManager.getTaskByIdAndType(epicTask.getId(), epicTask.getType())).isEmpty();
    }

    @Test
    void deleteTasksByIdAndTypeSubTask() {
        EpicTask epicTask = (EpicTask) taskManager.createTask(newEpic("Epic")).get();
        SubTask subTask = (SubTask) taskManager.createTask(newSubTask("SubTask", epicTask)).get();
        taskManager.deleteTaskByIdAndType(subTask.getId(), subTask.getType());
        assertThat(taskManager.getTaskByIdAndType(subTask.getId(), subTask.getType())).isEmpty();
    }

    @Test
    void updateTaskByTypeTask() {
        Task task = taskManager.createTask(newTask("Task")).get();
        task.setDescription("Updated");
        Task taskUpdated = taskManager.updateTask(task);
        assertThat(taskUpdated.getDescription()).isEqualTo("Updated");
    }

    @Test
    void updateTasksByTypeEpic() {
        EpicTask epicTask = (EpicTask) taskManager.createTask(newEpic("Epic")).get();
        epicTask.setDescription("Updated Epic");
        EpicTask epicTaskUpdated = (EpicTask) taskManager.updateTask(epicTask);
        assertThat(epicTaskUpdated.getDescription()).isEqualTo("Updated Epic");
    }

    @Test
    void updateTasksByTypeSubTask() {
        EpicTask epicTask = (EpicTask) taskManager.createTask(newEpic("Epic")).get();
        SubTask subTask = (SubTask) taskManager.createTask(newSubTask("SubTask", epicTask)).get();
        subTask.setDescription("Updated SubTask");
        taskManager.updateTask(subTask);
        SubTask subTaskUpdated = (SubTask) taskManager.getTaskByIdAndType(subTask.getId(), subTask.getType()).get();
        assertThat(subTaskUpdated.getDescription()).isEqualTo("Updated SubTask");
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
}