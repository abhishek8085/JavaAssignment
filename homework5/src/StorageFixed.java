
/**
 * This is a array based fixed data structure.
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : StorageFixed.java, 2015/09/27
 */
public class StorageFixed<E extends Comparable<E>, V> implements Storage<E, V>,
        EnhancedStorage<E> {

    @SuppressWarnings("unchecked")
    private Object[] array = new Object[MAX_SIZE];
    private int size = 0;
    private static int MAX_SIZE = 10000;

    public StorageFixed() {
    }

    private StorageFixed(E[] array) {
        this.array = array;
        this.size = array.length;
    }

    /**
     * Appends the specified element to the end of this storage.
     * Returns true, if the element could be added, else false
     * @param e element to be added
     * @return true, if the element could be added, else false
     */
    @Override
    public boolean add(E e) {
        if (size >= MAX_SIZE) {
            return false;
        }
        array[size++] = e;
        return true;
    }

    /**
     * Inserts the specified element at the specified position in this Storage.
     * retursn true, if the element could be added at position index, else false
     * @param index Specified position to insert the specified element
     * @param element true, if the element could be added at position index,
     *                else false
     *
     */
    @Override
    @SuppressWarnings("unchecked")
    public void add(int index, E element) {
        if( index >= MAX_SIZE )
        {
            throw new RuntimeException("Index greater than Max Limit.");
        }
        Object[] tempArray =  new Object[MAX_SIZE];
        copyArray(0, index, 0, array, tempArray);
        tempArray[index] = element;
        copyArray(index, size++, 1, array, tempArray);
        array = tempArray;
    }




    /**
     * Adds the specified component to the end of this storage,
     * increasing its size by one.
     * @param obj element to add
     */
    @Override
    public void addElement(E obj) {
        array[size++] = obj;
    }

    @Override
    public void addElement(E obj, V elem) {

    }

    /**
     * Returns the current capacity of this storage.
     * @return Returns the current capacity of this storage
     */
    @Override
    public int capacity() {
        return size;
    }

    /**
     * Removes all of the elements from this storage.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        array = new Object[MAX_SIZE];
    }

    /**
     * Returns the first component (the item at index 0) of
     * this storage.
     * @return Returns the first component (the item at index 0) of
     * this storage.
     */
    @Override
    @SuppressWarnings("unchecked")
    public E firstElement() {
        return (E) array[0];
    }

    /**
     * Returns the element at the specified position in this storage.
     * @param index Returns the element at the specified position
     *              in this storage.
     * @return Returns the element at the specified position in this storage.
     */
    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        if( index >= MAX_SIZE )
        {
            throw new RuntimeException("Index greater than Max Limit.");
        }
        return (E) array[index];
    }

    /**
     * Returns the last component of the storage.
     * @return Returns the last component of the storage.
     */
    @Override
    @SuppressWarnings("unchecked")
    public E lastElement() {
        return (E) array[size - 1];
    }

    /**
     * Returns a clone of this storage.
     * @return Returns a clone of this storage.
     */
    @SuppressWarnings("unchecked")
    public Object clone() {
        E[] clonedArray = (E[]) new Object[MAX_SIZE];
        copyArray(0, size, 0, array, clonedArray);
        return new StorageFixed<E, V>(clonedArray);
    }

    /**
     *
     * Copies the objects in an array to destination array
     *
     * @param fromIndex start index of source array
     * @param outputOffset offset to copy in destination array
     * @param inputArray source array
     * @param outputArray destination array
     * @return destination array
     */
    private Object[] copyArray(int fromIndex, int toIndex, int outputOffset,
                               Object[] inputArray, Object[] outputArray) {
        for (int i = fromIndex; i < fromIndex + (toIndex - fromIndex); i++) {
            outputArray[outputOffset + i] = inputArray[i];
        }
        return outputArray;
    }

    /**
     * Check if element present in the storage.
     *
     * @param o the object to checked
     * @return true if the object is present in the storage
     *         else false.
     */
    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < array.length; i++) {
            if (o.equals(array[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the specific element from the storage.
     *
     * @param o object to be removed
     * @return true is the object was successfully removed
     *         else false
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        for (int i = 0; i < array.length; i++) {
            if (o.equals(array[i])) {
                Object[] tempArray = new Object[MAX_SIZE];
                copyArray(0, i, 0, array, tempArray);
                copyArray(i + 1, size--, -1, array, tempArray);
                array = tempArray;
                return true;
            }
        }
        return false;
    }

    @Override
    public void setElementAt(int index, E e) {
        if( index >= MAX_SIZE )
        {
            throw new RuntimeException("Index greater than Max Limit.");
        }
        array[index] = e;
    }
}
