import java.util.*;

public class BasicStack <E> implements List<E> {
    private BasicArrayList<E> storage;

    public BasicStack(){
        storage = new BasicArrayList<>();
    }

    @Override
    public String toString(){
        return storage.toString();
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public boolean isEmpty() {
        return storage.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return storage.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return storage.iterator();
    }

    @Override
    public Object[] toArray() {
        return storage.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return storage.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return storage.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return storage.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return storage.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return storage.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return storage.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return storage.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return storage.retainAll(c);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public E get(int index) {
        return storage.get(index);
    }

    @Override
    public E set(int index, E element) {
        return storage.set(index, element);
    }

    @Override
    public void add(int index, E element) {
        storage.add(index, element);
    }

    @Override
    public E remove(int index) {
        return storage.remove(index);
    }

    public E peek(){
        return storage.get(storage.size() - 1);
    }

    public E pop(){
        return storage.remove(storage.size() - 1);
    }

    public E push(E data){
        storage.add(data);
        return data;
    }

    @Override
    public int indexOf(Object o) {
        return storage.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return storage.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return storage.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return storage.listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return storage.subList(fromIndex, toIndex);
    }
}
