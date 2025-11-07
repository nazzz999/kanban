package ru.kanban.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.kanban.model.Task;
import ru.kanban.model.TaskStatus;
import ru.kanban.model.TaskType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryHistoryManagerTest {

    private InMemoryHistoryManager history;

    @BeforeEach
    void setUp() {
        history = new InMemoryHistoryManager();
    }

    @Test
    void addSingleTaskIsHistory() {
        Task task = newTask("Task");
        history.add(task);
        List<Task> historyList = history.getHistory();
        assertThat(historyList).hasSize(1).containsExactly(task);
    }

    @Test
    void addTasksIsHistory() {
        Task taskOne = new Task(1, "Task one");
        Task taskTwo = new Task(2, "Task two");
        Task taskThree = new Task(3, "Task three");
        history.add(taskOne);
        history.add(taskTwo);
        history.add(taskThree);

        List<Task> historyList = history.getHistory();
        assertThat(historyList).containsExactly(taskOne, taskTwo, taskThree);
    }

    @Test
    void addTaskIsHistoryWhenHistorySize10() {
        Task[] task = new Task [11];
        for (int i = 0; i < 11; i++) {
            task[i] = newTask("T" + (i + 1));
            task[i].setId(i + 1);
            history.add(task[i]);
        }
        List<Task> historyList = history.getHistory();
        assertThat(historyList).hasSize(10);
        assertThat(historyList.get(0)).isEqualTo(task[1]);
        assertThat(historyList.get(9)).isEqualTo(task[10]);
    }

    @Test
    void removeTaskFromHistory() {
        Task task1 = newTask("Task 1");
        Task task2 = newTask("Task 2");

        history.add(task1);
        history.add(task2);

        history.remove(1);

        List<Task> historyList = history.getHistory();

        assertThat(historyList).doesNotContain(task1);
        assertThat(historyList).contains(task2);
    }

    @Test
    void addTaskKeepsHistoryWhenLimitIsExceeded() {
        Task[] tasks = new Task[11];
        for (int i = 0; i < 11; i++) {
            tasks[i] = new Task(i + 1, "Task" + (i + 1));
            history.add(tasks[i]);
        }
        List<Task> historyList = history.getHistory();

        assertThat(historyList).hasSize(10);

        assertThat(historyList).doesNotContain(tasks[0]);

        assertThat(historyList.get(9)).isEqualTo(tasks[10]);
    }

    private Task newTask(String name) {
        Task task = new Task();
        task.setName(name);
        task.setDescription(name);
        task.setStatus(TaskStatus.NEW);
        task.setTaskType(TaskType.TASK);
        return task;
    }
}