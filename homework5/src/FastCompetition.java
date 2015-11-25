/**
 * Created by Abhishek8085 on 26-09-2015.
 */
public class FastCompetition<E extends Comparable<E>> implements Competition<E> {


    StorageDynamic<E,String> eStringStorage = new StorageDynamic<E, String>();

    public FastCompetition(int i) {

    }

    @Override
    public boolean add(E e) {
        return eStringStorage.add(e);
    }

    @Override
    public boolean contains(Object o) {
        for ( int i=0; i< eStringStorage.capacity();i++)
        {
            if (o.equals(eStringStorage.get(i)))
            {
                return  true;
            }
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return eStringStorage.remove(o);
    }

    @Override
    public E elementAt(int index) {
        return eStringStorage.get(index);
    }

    @Override
    public Competition<E> sort() {


        quickSort(  0,  eStringStorage.capacity()-1);

        return null;
    }


    public void quickSort( int low, int high)
    {
    if (eStringStorage == null || eStringStorage.capacity() == 0)
            return ;

    if (low >= high)
            return ;

    int middle = low + (high - low) / 2;
    E  pivot = (E) eStringStorage.get(middle);

    int i = low, j = high;
    while (i <= j) {
        while (eStringStorage.get(i).compareTo(pivot) < 0) {
            i++;
        }

        while (eStringStorage.get(j).compareTo(pivot) > 0) {
            j--;
        }

        if (i <= j) {
            E temp = eStringStorage.get(i);
            eStringStorage.setElementAt(i, eStringStorage.get(j));
            eStringStorage.setElementAt(j,temp);
            i++;
            j--;
        }
    }

    if (low < j)
    quickSort(low, j);

    if (high > i)
    quickSort( i, high);
}

    @Override
    public int size() {
        return eStringStorage.capacity();
    }
}
