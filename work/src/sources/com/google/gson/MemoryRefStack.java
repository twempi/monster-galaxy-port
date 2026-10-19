package com.google.gson;

import com.google.gson.internal.C$Gson$Preconditions;
import java.util.Stack;

/* JADX INFO: loaded from: classes.dex */
final class MemoryRefStack {
    private final Stack<ObjectTypePair> stack = new Stack<>();

    MemoryRefStack() {
    }

    public ObjectTypePair push(ObjectTypePair obj) {
        C$Gson$Preconditions.checkNotNull(obj);
        return this.stack.push(obj);
    }

    public ObjectTypePair pop() {
        return this.stack.pop();
    }

    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

    public ObjectTypePair peek() {
        return this.stack.peek();
    }

    public boolean contains(ObjectTypePair obj) {
        if (obj == null) {
            return false;
        }
        for (ObjectTypePair stackObject : this.stack) {
            if (stackObject.getObject() == obj.getObject() && stackObject.type.equals(obj.type)) {
                return true;
            }
        }
        return false;
    }
}
