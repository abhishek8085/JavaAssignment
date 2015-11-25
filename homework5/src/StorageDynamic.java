
/**
 * This Program is Storage Dynamic, which implements storage and EnhancedStorage
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : Storage.java, 2015/09/27
 */
public class StorageDynamic<E extends Comparable<E>, V> implements Storage<E, V>, EnhancedStorage<E> {

    private int size = 0; // Linked list size
    private Node<E> front = null; // First node in the list

    /**
     * This method adds a new node to the end of the list. Returns true after
     * adding.
     */
    @Override
    public boolean add(E e) {
        if (size == 0) {
            front = new Node<E>(e);
            size++;
            return true;
        }
        Node<E> currentNode = front;
        while (currentNode.getLink() != null) {
            currentNode = currentNode.getLink();
        }
        currentNode.setLink(new Node<E>(e));
        size++;
        return true;
    }

    /**
     * This method adds the node at the specified index.
     */
    @Override
    public void add(int index, E element) {

        if (size == 0) {
            add(element);
        }

        if ((index < 0) && index > (size - 1)) {
            System.err.println("Index OutOfBound :Ignoring element");
        }

        Node<E> currentNode = front;
        Node<E> tempNode = new Node<E>(element);

        if (index==0)
        {
            tempNode.setLink(front.getLink());
            front=tempNode;
            return;
        }



        for (int i = 0; i < index - 1; i++) {
            currentNode = currentNode.getLink();
        }

        tempNode.setLink(currentNode.getLink());
        currentNode.setLink(tempNode);
        size++;
    }

    /**
     * This method calls the method add in which node is added in the end.
     */
    @Override
    public void addElement(E obj) {
        add(obj);
    }

    /**
     * Adds the specified component to the end of this storage, increasing its
     * size by one.
     */
    @Override
    public void addElement(E obj, V elem) {

    }

    /**
     * This method returns the size of the linked list.
     */
    @Override
    public int capacity() {
        return size;
    }

    /**
     * This method clear the entire linked list and sets the first node to null.
     */
    @Override
    public void clear() {
        front = null;
        size = 0;
    }

    /**
     * This method returns the first element data.
     */
    @Override
    public E firstElement() {
        return front.getData();
    }

    /**
     * This method returns the node for the given index.
     */
    @Override
    public E get(int index) {

        if ((index < 0) && index > (size - 1)) {
            System.err.println("Index OutOfBound");
            return null;
        }

        Node<E> currentNode = front;
        for (int i = 0; i < index; i++) {
            currentNode = currentNode.getLink();
        }

        return currentNode.getData();
    }

    /**
     * This methos returns the last node in the linked list.
     */
    @Override
    public E lastElement() {
        Node<E> current = front;
        while (current.getLink() != null) {
            current = current.getLink();
        }
        return current.getData();
    }

    /**
     * This method clones the linked list to a new dynamic storage.
     */
    public Object clone() {
        StorageDynamic<E, V> evStorageDynamic = new StorageDynamic<E, V>();
        Node<E> currentElement = front;
        while (currentElement.getLink() != null) {
            evStorageDynamic.add(currentElement.getData());
            currentElement = currentElement.getLink();
        }
        return evStorageDynamic;
    }

    /**
     * This method prints the storage dynamic list elements.
     */
    @Override
    public String toString() {
        String string = "";

        Node<E> currentElement = front;

        while (currentElement.getLink() != null) {
            string += currentElement.getData().toString() + "=";
            currentElement = currentElement.getLink();
        }

        return string;
    }

    /**
     * This method checks if the given oblect is in the list or not.
     */
    @Override
    public boolean contains(Object o) {

        Node<E> currentNode = front;

        while (currentNode.getLink() != null) {
            if (currentNode.getData().equals(o)) {
                return true;
            }
            currentNode = currentNode.getLink();
        }

        return false;
    }

    /**
     * This method removes the node.
     */
    @Override
    public boolean remove(Object o) {

        if (size == 1) {
            front = null;
            size = 0;
            return true;
        }

        if (size == 0) {
            return false;
        }

        Node<E> currentNode = front;
        Node<E> previousNode = null;

        while (currentNode.getLink() != null) {
            if (currentNode.getData().equals(o)) {
                if (previousNode != null) {
                    previousNode.setLink(currentNode.getLink());
                } else {
                    front = currentNode.getLink();
                }
                size--;
                return true;
            }
            previousNode = currentNode;
            currentNode = currentNode.getLink();
        }

        if (currentNode.getData().equals(o))
        {
            previousNode.setLink(currentNode.getLink());
            size--;
            return true;
        }

        return false;
    }

    @Override
    public void setElementAt(int index, E e) {

        if (size == 0) {
            add(e);
        }

        if ((index < 0) && index > (size - 1)) {
            System.err.println("Index OutOfBound :Ignoring element");
        }

        Node<E> currentNode = front;
        Node<E> tempNode = new Node<E>(e);




        for (int i = 0; i < index - 1; i++) {
            currentNode = currentNode.getLink();
        }
        tempNode.setLink(currentNode.getLink().getLink());
        currentNode.setLink(tempNode);
    }
}

/**
 * This is a Node class used for by the storage dynamic class.
 *
 * @param <E>
 */
class Node<E> {
    private E data;
    private Node<E> link;

    public Node(E data, Node<E> link) {
        this.data = data;
        this.link = link;
    }

    public Node(E data) {
        this.data = data;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public Node<E> getLink() {
        return link;
    }

    public void setLink(Node<E> link) {
        this.link = link;
    }

}