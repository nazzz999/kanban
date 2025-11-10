package ru.kanban.service;

public class Node<T> {

    T task;
    Node<T> next;
    Node<T> prev;

    public Node(T task) {
        this.task = task;
    }
}