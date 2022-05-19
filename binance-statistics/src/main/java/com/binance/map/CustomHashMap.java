package com.binance.map;

import com.binance.model.SymbolStatistic;

/**
 * Quite straight forward hash map implementation using separate chaining and static bucket array
 * Number of buckets in initial array is 2048 in order to avoid collisions.
 * <p>
 * TODO - implements dynamic resizing logic
 */
public class CustomHashMap {

    private static final int NUMBER_OF_BUCKETS = 2048;

    private Node[] buckets;

    public CustomHashMap() {
        buckets = new Node[NUMBER_OF_BUCKETS];
    }


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

    /**
     * If bucket is not empty - put element in the hand made linked list
     */
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
            return "Node{" + "key=" + key + ", value=" + value + ", next=" + next + '}';
        }
    }
}
