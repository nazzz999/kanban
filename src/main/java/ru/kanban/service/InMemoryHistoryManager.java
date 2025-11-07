package ru.kanban.service;
import ru.kanban.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private CustomLinkedList historyList = new CustomLinkedList();
    private Map<Integer, Node> historyMap = new HashMap<>();
    private static final int HISTORY_SIZE = 10;

    @Override
    public void add(Task task) {
        if (historyMap.containsKey(task.getId())) {
            Node nodeToRemove = historyMap.get(task.getId());
            historyList.removeNode(nodeToRemove);
        }
        Node newNode = historyList.linkLast(task);
        historyMap.put(task.getId(), newNode);
        if (historyMap.size() > HISTORY_SIZE) {
            Node firstNode = historyList.removeFirst();
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
            Node nodeToRemove = historyMap.get(id);
            historyList.removeNode(nodeToRemove);
            historyMap.remove(id);
        }
    }
}

    class CustomLinkedList {
        private Node head;
        private Node tail;

        public Node linkLast(Task task) {
            Node newNode = new Node(task);
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

        public List<Task> getTasks() {
            List<Task> tasks = new ArrayList<>();
            Node current = head;
            while (current != null) {
                tasks.add(current.task);
                current = current.next;
            }
            return tasks;
        }

        public void removeNode(Node node) {
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

        public Node removeFirst() {
            if (head == null) {
                return null;
            }
            Node firstNode = head;
            removeNode(firstNode);
            return firstNode;
        }
    }

    class Node {
        Task task;
        Node next;
        Node prev;

        public Node(Task task) {
            this.task = task;
        }
    }


