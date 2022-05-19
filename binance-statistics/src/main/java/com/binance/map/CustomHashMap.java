package com.binance.map;

import com.binance.model.SymbolStatistic;

public class CustomHashMap {

    private static final int NUMBER_OF_BUCKETS = 1024;

    private Node[] buckets;

    public CustomHashMap() {
        buckets = new Node[NUMBER_OF_BUCKETS];
    }

    /**
     * Returns the value to which the specified key is mapped, or -1 if this map contains no mapping
     * for the key
     */
    public SymbolStatistic get(String key) {
        int index = index(key);
        int hash = hash(key);

        if (buckets[index] != null) {
            Node current = buckets[index];

            while (current != null) {
                if (current.hash == hash) {
                    return current.value;
                }
                current = current.next;
            }

        }
        return null;
    }

    public void put(String key, SymbolStatistic value) {
        int index = index(key);
        int hash = hash(key);

        if (buckets[index] == null) {
            buckets[index] = new Node(key, value, hash);
        } else {
            Node current = buckets[index];

            while (current != null) {
                if (current.hash == hash) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = new Node(key, value, hash);
                    return;
                }
                current = current.next;
            }
        }
    }

    private int index(String key) {
        return Math.abs(hash(key)) % NUMBER_OF_BUCKETS;
    }

    private int hash(String key) {
        return key.hashCode();
    }

    private static class Node {
        String key;
        SymbolStatistic value;
        int hash;
        Node next;

        Node(String key, SymbolStatistic value, int hash) {
            this.key = key;
            this.value = value;
            this.hash = hash;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "key=" + key +
                    ", value=" + value +
                    ", next=" + next +
                    '}';
        }
    }
}
