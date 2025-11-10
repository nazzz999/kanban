package ru.kanban.service;
import ru.kanban.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private CustomLinkedList<Task> historyList = new CustomLinkedList<>();
    private Map<Integer, Node<Task>> historyMap = new HashMap<>();
    private static final int HISTORY_SIZE = 10;

    @Override
    public void add(Task task) {
        if (historyMap.containsKey(task.getId())) {
            Node<Task> nodeToRemove = historyMap.get(task.getId());
            historyList.removeNode(nodeToRemove);
        }
        Node<Task> newNode = historyList.linkLast(task);
        historyMap.put(task.getId(), newNode);
        if (historyMap.size() > HISTORY_SIZE) {
            Node<Task> firstNode = historyList.removeFirst();
            historyMap.remove(firstNode.task.getId());
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }

    @Override
    public void remove(int id) {
        if (historyMap.containsKey(id)) {
            Node<Task> nodeToRemove = historyMap.get(id);
            historyList.removeNode(nodeToRemove);
            historyMap.remove(id);
        }
    }
}

    class CustomLinkedList<T> {
        private Node<T> head;
        private Node<T> tail;

        public Node<T> linkLast(T task) {
            Node<T> newNode = new Node<>(task);
            if (tail == null) {
                head = newNode;
                tail = newNode;
            } else {
                tail.next = newNode;
                newNode.prev = tail;
                tail = newNode;
            }
            return newNode;
        }

        public List<T> getTasks() {
            List<T> tasks = new ArrayList<>();
            Node<T> current = head;
            while (current != null) {
                tasks.add(current.task);
                current = current.next;
            }
            return tasks;
        }

        public void removeNode(Node<T> node) {
            if (node == null) {
                return;
            }
            if (node.prev != null) {
                node.prev.next = node.next;
            }
            if (node == head) {
                head = node.next;
            }
            if (node == tail) {
                tail = node.prev;
            }
            node.next = null;
            node.prev = null;
        }

        public Node<T> removeFirst() {
            if (head == null) {
                return null;
            }
            Node<T> firstNode = head;
            removeNode(firstNode);
            return firstNode;
        }
    }



