/**
 * LZW Dictinary
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : Dictionary.java, 2015/11/11
 */


public class LiteDictionary<K, V> implements Dictionary<K, V> {
    private int size;
    private Entry<K, V>[] entries;

    @SuppressWarnings("unchecked")
    public LiteDictionary(int size) {
        this.size = size;
        entries = new Entry[size];
    }


    @Override
    public void put(K key, V value) {
        int bucket = Math.abs(key.hashCode()) % size;
        Entry<K, V> entry = entries[bucket];

        if (entry != null) {
            if (entry.getKey().equals(key)) {
                entry.setValue(value);
            } else {
                Entry<K, V> currentNode = entry;
                while (currentNode.next != null) {
                    currentNode = currentNode.next;
                }
                currentNode.setNextNode(new Entry<K, V>(key, value));
            }

        } else {
            entries[bucket] = new Entry<K, V>(key, value);
        }

    }

    @SuppressWarnings("unchecked")
    public void clear() {
        entries = new Entry[size];
    }

    @Override
    public V get(K key) {
        Entry<K, V> entry = entries[Math.abs(key.hashCode()) % size];

        if (entry != null) {
            Entry<K, V> currentEntry = entry;

            if (currentEntry.getKey().equals(key)) {
                return currentEntry.getValue();
            }

            while (currentEntry.next != null) {
                if (currentEntry.getKey().equals(key)) {
                    return currentEntry.getValue();
                }
                currentEntry = currentEntry.next;
            }


        }
        return null;
    }

    /**
     * Entry of dictionary
     *
     *
     * @author Abhishek Indukumar
     * 
     * @version 1.0 : Entry.java, 2015/10/11
     */
    class Entry<K, V> {
        private K key;
        private V value;
        public Entry<K, V> next;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Entry<K, V> getNextNode() {
            return next;
        }

        public void setNextNode(Entry<K, V> next) {
            this.next = next;
        }
    }


}