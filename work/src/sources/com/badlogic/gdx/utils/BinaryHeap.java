package com.badlogic.gdx.utils;

import com.badlogic.gdx.utils.BinaryHeap.Node;

/* JADX INFO: loaded from: classes.dex */
public class BinaryHeap<T extends Node> {
    private final boolean isMaxHeap;
    private Node[] nodes;
    public int size;

    public BinaryHeap() {
        this(16, false);
    }

    public BinaryHeap(int capacity, boolean isMaxHeap) {
        this.size = 0;
        this.isMaxHeap = isMaxHeap;
        this.nodes = new Node[capacity];
    }

    public T add(T node) {
        if (this.size == this.nodes.length) {
            Node[] newNodes = new Node[this.size << 1];
            System.arraycopy(this.nodes, 0, newNodes, 0, this.size);
            this.nodes = newNodes;
        }
        node.index = this.size;
        this.nodes[this.size] = node;
        int i = this.size;
        this.size = i + 1;
        up(i);
        return node;
    }

    public T pop() {
        Node[] nodeArr = this.nodes;
        T t = (T) nodeArr[0];
        int i = this.size - 1;
        this.size = i;
        nodeArr[0] = nodeArr[i];
        nodeArr[this.size] = null;
        if (this.size > 0) {
            down(0);
        }
        return t;
    }

    public void setValue(T node, float value) {
        float oldValue = node.value;
        node.value = value;
        if ((value < oldValue) ^ this.isMaxHeap) {
            up(node.index);
        } else {
            down(node.index);
        }
    }

    private void up(int index) {
        Node[] nodes = this.nodes;
        Node node = nodes[index];
        float value = node.value;
        while (index > 0) {
            int parentIndex = (index - 1) >> 1;
            Node parent = nodes[parentIndex];
            if (!((value < parent.value) ^ this.isMaxHeap)) {
                break;
            }
            nodes[index] = parent;
            parent.index = index;
            index = parentIndex;
        }
        nodes[index] = node;
        node.index = index;
    }

    private void down(int index) {
        Node rightNode;
        float rightValue;
        Node[] nodes = this.nodes;
        int size = this.size;
        Node node = nodes[index];
        float value = node.value;
        while (true) {
            int leftIndex = (index << 1) + 1;
            if (leftIndex < size) {
                int rightIndex = leftIndex + 1;
                Node leftNode = nodes[leftIndex];
                float leftValue = leftNode.value;
                if (rightIndex >= size) {
                    rightNode = null;
                    rightValue = this.isMaxHeap ? Float.MIN_VALUE : Float.MAX_VALUE;
                } else {
                    rightNode = nodes[rightIndex];
                    rightValue = rightNode.value;
                }
                if (!((leftValue < rightValue) ^ this.isMaxHeap)) {
                    if (rightValue != value) {
                        if ((rightValue > value) ^ this.isMaxHeap) {
                            break;
                        }
                        nodes[index] = rightNode;
                        rightNode.index = index;
                        index = rightIndex;
                    } else {
                        break;
                    }
                } else if (leftValue != value) {
                    if ((leftValue > value) ^ this.isMaxHeap) {
                        break;
                    }
                    nodes[index] = leftNode;
                    leftNode.index = index;
                    index = leftIndex;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        nodes[index] = node;
        node.index = index;
    }

    public String toString() {
        if (this.size == 0) {
            return "[]";
        }
        Object[] nodes = this.nodes;
        StringBuilder buffer = new StringBuilder(32);
        buffer.append('[');
        buffer.append(nodes[0]);
        for (int i = 1; i < this.size; i++) {
            buffer.append(", ");
            buffer.append(nodes[i]);
        }
        buffer.append(']');
        return buffer.toString();
    }

    public static class Node {
        int index;
        float value;

        public Node(float value) {
            this.value = value;
        }

        public float getValue() {
            return this.value;
        }
    }
}
