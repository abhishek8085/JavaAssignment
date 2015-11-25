/**
 * This is a array based fixed data structure.
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : EnhancedStorage.java, 2015/09/27
 */
public interface EnhancedStorage<E> {

    /**
     * Check if element present in the storage.
     *
     * @param o the object to checked
     * @return true if the object is present in the storage
     *         else false.
     */
    boolean contains(Object o);

    /**
     * Removes the specific element from the storage.
     *
     * @param o object to be removed
     * @return true is the object was successfully removed
     *         else false
     */
    boolean remove(Object o);

    /**
     * set element at specific location
     * @param e element
     */
    void setElementAt( int index, E e );
}
