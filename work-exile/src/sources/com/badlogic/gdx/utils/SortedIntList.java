package com.badlogic.gdx.utils;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class SortedIntList<E> implements Iterable<Node<E>> {
    Node<E> first;
    private SortedIntList<E>.Iterator iterator;
    private NodePool<E> nodePool = new NodePool<>();
    int size = 0;

    public static class Node<E> {
        public int index;
        protected Node<E> n;
        protected Node<E> p;
        public E value;
    }

    public E insert(int index, E value) {
        if (this.first != null) {
            Node<E> c = this.first;
            while (c.n != null && c.n.index <= index) {
                c = c.n;
            }
            if (index > c.index) {
                c.n = this.nodePool.obtain(c, c.n, value, index);
                if (c.n.n != null) {
                    c.n.n.p = c.n;
                }
                this.size++;
            } else if (index < c.index) {
                Node<E> newFirst = this.nodePool.obtain(null, this.first, value, index);
                this.first.p = newFirst;
                this.first = newFirst;
                this.size++;
            } else {
                c.value = value;
            }
        } else {
            this.first = this.nodePool.obtain(null, null, value, index);
            this.size++;
        }
        return null;
    }

    public E get(int index) {
        if (this.first == null) {
            return null;
        }
        Node<E> c = this.first;
        while (c.n != null && c.index < index) {
            c = c.n;
        }
        if (c.index != index) {
            return null;
        }
        E match = c.value;
        return match;
    }

    public void clear() {
        while (this.first != null) {
            this.nodePool.free(this.first);
            this.first = this.first.n;
        }
        this.size = 0;
    }

    public int size() {
        return this.size;
    }

    @Override // java.lang.Iterable
    public java.util.Iterator<Node<E>> iterator() {
        if (this.iterator == null) {
            this.iterator = new Iterator();
        }
        return this.iterator.reset();
    }

    class Iterator implements java.util.Iterator<Node<E>> {
        private Node<E> position;
        private Node<E> previousPosition;

        Iterator() {
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.position != null;
        }

        @Override // java.util.Iterator
        public Node<E> next() {
            this.previousPosition = this.position;
            this.position = this.position.n;
            return this.previousPosition;
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.previousPosition != null) {
                if (this.previousPosition == SortedIntList.this.first) {
                    SortedIntList.this.first = this.position;
                } else {
                    this.previousPosition.p.n = this.position;
                    if (this.position != null) {
                        this.position.p = this.previousPosition.p;
                    }
                }
                SortedIntList.this.size--;
            }
        }

        public SortedIntList<E>.Iterator reset() {
            this.position = SortedIntList.this.first;
            this.previousPosition = null;
            return this;
        }
    }

    static class NodePool<E> extends Pool<Node<E>> {
        NodePool() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.badlogic.gdx.utils.Pool
        public Node<E> newObject() {
            return new Node<>();
        }

        public Node<E> obtain(Node<E> p, Node<E> n, E value, int index) {
            Node<E> newNode = (Node) super.obtain();
            newNode.p = p;
            newNode.n = n;
            newNode.value = value;
            newNode.index = index;
            return newNode;
        }
    }
}
