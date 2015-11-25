/**
 * All dictionary should extend this.
 *
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : Dictionary.java, 2015/10/11
 */
interface Dictionary<K, V> {

    /**
     * Puts key value in dictinary
     *
     * @param key key
     * @param value value
     */
     void put(K key, V value);

    /**
     * Gets value for the key
     *
     * @param key key
     * @return value
     */
     V get(K key);

    /**
     * Clears the dictionary
     */
     void clear();

}