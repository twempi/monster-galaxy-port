package com.badlogic.gdx.utils;

import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;

/* JADX INFO: loaded from: classes.dex */
public class OrderedMap<K, V> extends ObjectMap<K, V> {
    final Array<K> keys;

    public OrderedMap() {
        this.keys = new Array<>();
    }

    public OrderedMap(int initialCapacity) {
        super(initialCapacity);
        this.keys = new Array<>(initialCapacity);
    }

    public OrderedMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        this.keys = new Array<>(initialCapacity);
    }

    @Override // com.badlogic.gdx.utils.ObjectMap
    public V put(K k, V v) {
        if (!containsKey(k)) {
            this.keys.add(k);
        }
        return (V) super.put(k, v);
    }

    @Override // com.badlogic.gdx.utils.ObjectMap
    public V remove(K k) {
        this.keys.removeValue(k, false);
        return (V) super.remove(k);
    }

    @Override // com.badlogic.gdx.utils.ObjectMap
    public void clear() {
        this.keys.clear();
        super.clear();
    }

    public Array<K> orderedKeys() {
        return this.keys;
    }

    @Override // com.badlogic.gdx.utils.ObjectMap
    public ObjectMap.Entries<K, V> entries() {
        return new ObjectMap.Entries(this) { // from class: com.badlogic.gdx.utils.OrderedMap.1
            @Override // com.badlogic.gdx.utils.ObjectMap.MapIterator
            void advance() {
                this.nextIndex++;
                this.hasNext = this.nextIndex < this.map.size;
            }

            @Override // com.badlogic.gdx.utils.ObjectMap.Entries, java.util.Iterator
            public ObjectMap.Entry next() {
                this.entry.key = OrderedMap.this.keys.get(this.nextIndex);
                this.entry.value = this.map.get(this.entry.key);
                advance();
                return this.entry;
            }

            @Override // com.badlogic.gdx.utils.ObjectMap.Entries, com.badlogic.gdx.utils.ObjectMap.MapIterator, java.util.Iterator
            public void remove() {
                this.map.remove(this.entry.key);
            }
        };
    }

    @Override // com.badlogic.gdx.utils.ObjectMap
    public ObjectMap.Keys<K> keys() {
        return new ObjectMap.Keys(this) { // from class: com.badlogic.gdx.utils.OrderedMap.2
            @Override // com.badlogic.gdx.utils.ObjectMap.MapIterator
            void advance() {
                this.nextIndex++;
                this.hasNext = this.nextIndex < this.map.size;
            }

            @Override // com.badlogic.gdx.utils.ObjectMap.Keys, java.util.Iterator
            public K next() {
                K key = OrderedMap.this.keys.get(this.nextIndex);
                advance();
                return key;
            }

            @Override // com.badlogic.gdx.utils.ObjectMap.Keys, com.badlogic.gdx.utils.ObjectMap.MapIterator, java.util.Iterator
            public void remove() {
                this.map.remove(OrderedMap.this.keys.get(this.nextIndex - 1));
            }
        };
    }

    @Override // com.badlogic.gdx.utils.ObjectMap
    public ObjectMap.Values<V> values() {
        return new ObjectMap.Values(this) { // from class: com.badlogic.gdx.utils.OrderedMap.3
            @Override // com.badlogic.gdx.utils.ObjectMap.MapIterator
            void advance() {
                this.nextIndex++;
                this.hasNext = this.nextIndex < this.map.size;
            }

            @Override // com.badlogic.gdx.utils.ObjectMap.Values, java.util.Iterator
            public V next() {
                V v = (V) this.map.get(OrderedMap.this.keys.get(this.nextIndex));
                advance();
                return v;
            }

            @Override // com.badlogic.gdx.utils.ObjectMap.Values, com.badlogic.gdx.utils.ObjectMap.MapIterator, java.util.Iterator
            public void remove() {
                this.map.remove(OrderedMap.this.keys.get(this.nextIndex - 1));
            }
        };
    }

    @Override // com.badlogic.gdx.utils.ObjectMap
    public String toString() {
        if (this.size == 0) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder(32);
        buffer.append('{');
        Array<K> keys = this.keys;
        int n = keys.size;
        for (int i = 0; i < n; i++) {
            K key = keys.get(i);
            if (i > 0) {
                buffer.append(", ");
            }
            buffer.append(key);
            buffer.append(SignatureVisitor.INSTANCEOF);
            buffer.append(get(key));
        }
        buffer.append('}');
        return buffer.toString();
    }
}
