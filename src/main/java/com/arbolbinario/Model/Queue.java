package com.arbolbinario.Model;

public class Queue<T> {
    private class Node {
        private T data;
        private Node next;
    
        public Node(T data) {
            super();
            this.data = data;
            this.next = null;
        }
    }
    
    private Node front;
    private Node rear;
    private int size;
    public Queue() {
        front = rear = null;
        size = 0;
    }
    public boolean isEmpty(){
        return size == 0;
    }
    public void offer(T data) {
        Node newNode = new Node(data);
        if (rear == null) { // Si la cola está vacía
            front = rear = newNode;
        } else {
            rear.next = newNode; // Conectar el último nodo al nuevo
            rear = newNode;      // Mover el fin al nuevo nodo
        }
        size++;
    }
    public T poll() {
        if(isEmpty()) {
            throw new RuntimeException("Queue is empty"); // Manejo de excepción si la lista esta vacia
        }
        T data = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        size--;
        return data;
    }
    @Override
    public Queue<T> clone() {
        Queue<T> clonedQueue = new Queue<>();
        Node current = front;
        while (current != null) {
            clonedQueue.offer(current.data);
            current = current.next;
        }
        return clonedQueue;
    }
    public void clear() {
        front = rear = null;
        size = 0;
    }
    public int size() {
        return size;
    }
    //invertir el orden de una cola recursivamente
    public void reverse() {
        if (isEmpty()) { // Si la cola está vacía, ya no hay nada que invertir
            return;
        }
        T data = poll(); // Sacar el primer elemento
        reverse(); // Llamada recursiva
        offer(data); // Agregar el elemento al final
    }
    //invertir el orden de una cola recursivamente y retorne la cola invertida
    public Queue<T> reverseAndReturn() {
        Queue<T> reversedQueue = new Queue<>();
        if (isEmpty()) {
            return reversedQueue;
        }
        T data = poll(); 
        reversedQueue = reverseAndReturn(); // Llamada recursiva que retorna la cola invertida
        reversedQueue.offer(data); // Agregar el elemento al final
        return reversedQueue; // Retornar la cola invertida finalizada
    }
    // Metodo para toString de la cola
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node current = front;
        while (current != null) {
            sb.append(current.data).append(" ");
            current = current.next;
        }
        return sb.toString().trim(); // Eliminar el espacio final
    }
}

