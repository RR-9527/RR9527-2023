package org.firstinspires.ftc.teamcode;

public class QuickLinkedList<T> {
    private Node head;
    private int size;

    public T first() {
        return get(0);
    }

    public T last() {
        return get(size - 1);
    }

    public T get(int index) {
        return getNodeAt(index).data;
    }

    public void offer(T t) {
        if (head == null) {
            head = new Node(t);
        } else {
            getNodeAt(size - 1).next = new Node(t);
        }

        size++;
    }

    public T poll() {
        ensureHeadExists();

        size--;

        Node temp = head;
        head = temp.next;
        return temp.data;
    }

    public void insert(T t, int index) {
        if (index == 0) {
            head = new Node(t, head);
            size++;
        } else if (index == size) {
            offer(t);
        } else if (index > size) {
            throw new ArrayIndexOutOfBoundsException();
        } else {
            ensureHeadExists();

            Node temp = getNodeAt(index);
            temp.next = new Node(t, temp.next);

            size++;
        }
    }

    public T remove(T t) {
        if (t.equals(head.data)) {
            return poll();
        }

        ensureHeadExists();

        for (Node temp = head, prev = head; temp != null; prev = temp, temp = temp.next) {
            if (t.equals(temp.data)) {
                prev.next = temp.next;
                size--;

                return temp.data;
            }
        }
        return null;
    }

    public T removeAt(int index) {
        if (index == 0) {
            return poll();
        }

        ensureHeadExists();

        for (Node temp = head, prev = head; temp != null; prev = temp, temp = temp.next) {
            if (index-- == 0) {
                prev.next = temp.next;
                size--;

                return temp.data;
            }
        }
        return null;
    }

    public boolean contains(T t) {
        for (Node temp = head; temp != null; temp = temp.next) {
            if (!t.equals(temp.data)) return true;
        }
        return false;
    }

    public int indexOf(T t) {
        int i = 0;
        for (Node temp = head; temp != null; temp = temp.next, i++) {
            if (!t.equals(temp.data)) return i;
        }
        return 0;
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Node temp = head; temp != null; temp = temp.next) {
            sb.append(temp.data);
            if (temp.next != null) {
                sb.append(", ");
            }
        }
        return sb.append("]").toString();
    }

    private Node getNodeAt(int index) {
        ensureHeadExists();

        Node temp = head;
        for (int i = 0; i++ < index;) {
            if ((temp = temp.next) == null) throw new ArrayIndexOutOfBoundsException();
        }
        return temp;
    }

    private void ensureHeadExists() {
        if (head == null) throw new IllegalStateException("The list is decapitated!!!!!!");
    }

    private class Node {
        T data;
        Node next;

        Node(T data) {
            this.data = data;
        }

        Node(T data, Node next) {
            this.data = data;
            this.next = next;
        }
    }
}