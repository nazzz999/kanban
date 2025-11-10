package ru.kanban.service;

import ru.kanban.model.Task;

public class Node<T> {
    T task;
    Node<T> next;
    Node<T> prev;

    public Node (T task) {
        this.task = task;
    }


}
