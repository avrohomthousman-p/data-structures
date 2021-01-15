import java.util.*;


public class BasicArrayList<E> implements List<E> {

    private E[] storage;
    private int insertionPoint;

    public BasicArrayList(){
        storage = (E[]) new Object[10];
        insertionPoint = 0;
    }

    public BasicArrayList(Collection<? extends E> startingData){
        nullCheck(startingData, "cannot initialize arrayList using data from null collection.");
        storage = (E[]) new Object[2 * startingData.size() + 1];
        insertionPoint = 0;
        this.addAll(startingData);
    }

    public BasicArrayList(BasicArrayList template){
        this.storage = (E[]) new Object[template.size()];
        this.insertionPoint = template.size();
        System.arraycopy(template.storage, 0, this.storage, 0, template.size());
    }

    @Override
    public int size() {
        return insertionPoint;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        for(int i = 0; i < size(); i++){
            if(Objects.equals(o, storage[i])){
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<E>{
        private int indexPointer = 0;
        private boolean calledRemove = false;

        @Override
        public boolean hasNext(){
            return !(indexPointer >= insertionPoint);
        }

        @Override
        public E next(){
            calledRemove = false;
            return storage[indexPointer++];
        }

        @Override
        public void remove(){
            if (calledRemove)
                throw new IllegalStateException();
            BasicArrayList.this.remove(--indexPointer); //need to call the remove() of MyArrayList
            calledRemove = true;
        }
    }


    @Override
    public E[] toArray() {
        E[] output = (E[]) new Object[insertionPoint];
        System.arraycopy(storage, 0, output, 0, insertionPoint);
        return output;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size()){
            a = (T[]) new Object[size()];
        }
        else if(a.length > size()){
            for(int i = size(); i < a.length; i++){
                a[i] = null;
            }
        }
        for(int i = 0; i < size(); i++){
            a[i] = (T) get(i);
        }
        return a;
    }

    @Override
    public boolean add(E e) {
        if(size() == storage.length)
            ensureCapacity(1);
        storage[insertionPoint++] = e;
        return true;
    }

    private void ensureCapacity(int additionalSpaceNeeded){
        int requiredSize = size() + additionalSpaceNeeded;
        int newSize = storage.length;
        while(newSize <= requiredSize){
            newSize = requiredSize * 2 + 1;
        }
        E[] newStorage = (E[]) new Object[newSize];
        System.arraycopy(storage, 0, newStorage, 0, size());
        storage = newStorage;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index == -1)
            return false;
        this.remove(index);
        return true;
    }

    private static void fill(Object[] o, int startIndex, Collection<?> data){
        for (Object item : data) {
            o[startIndex++] = item;
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        nullCheck(c, "arrayList cannot be compared to null collection");
        HashSet<E> contents = new HashSet<>(this);

        for(Object item : c){
            if(!contents.contains(item)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        nullCheck(c, "cannot add data from null collection to arrayList");
        if (c.isEmpty())
            return false;
        int additionalSpaceNeeded = c.size() + this.size() - storage.length;
        if(additionalSpaceNeeded >= 0)
            ensureCapacity(additionalSpaceNeeded);

        fill(storage, insertionPoint, c);
        insertionPoint += c.size();
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        nullCheck(c, "cannot add data from null collection to arraylist");
        if(index >= size())
            throw new IndexOutOfBoundsException(String.format("index %d out of bounds for arraylist of size %d", index, size()));

        if(c.isEmpty())
            return false;

        int additionalSpaceNeeded = c.size() + this.size() - storage.length;
        if(additionalSpaceNeeded >= 0){
            E[] newStorage = (E[]) new Object[(storage.length + c.size()) * 2 + 1];
            System.arraycopy(storage, 0, newStorage, 0, index);
            fill(newStorage, index, c);
            System.arraycopy(storage, index, newStorage, index + c.size(), this.size() - index);
        }
        else{
            System.arraycopy(storage, index, storage, index + c.size(), this.size() - index);
            fill(storage, index, c);
        }
        insertionPoint += c.size();
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        nullCheck(c, "cannot use null collection to remove data from arrayList");
        if(c.isEmpty())
            return false;
        HashSet<E> itemsToRemove = new HashSet<>((Collection<? extends E>) c);
        E[] newStorage = (E[]) new Object[storage.length];
        int newInsertionPoint = 0;
        for (int i = 0; i < insertionPoint; i++){
            //if item is not in the collection, add it to newBackingStore
            if(!itemsToRemove.contains(storage[i])){
                newStorage[newInsertionPoint++] = storage[i];
            }
        }
        boolean changesMade = !(insertionPoint == newInsertionPoint);
        storage = newStorage;
        insertionPoint = newInsertionPoint;
        return changesMade;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        nullCheck(c, "cannot use null collection to retain data from arrayList");
        if(c.isEmpty())
            return false;
        HashSet<E> itemsToKeep = new HashSet<>((Collection<? extends E>) c);
        E[] newStorage = (E[]) new Object[storage.length];
        int newInsertionPoint = 0;
        for (int i = 0; i < insertionPoint; i++){
            if(itemsToKeep.contains(storage[i])){
                newStorage[newInsertionPoint++] = storage[i];
            }
        }
        boolean changesMade = !(insertionPoint == newInsertionPoint);
        storage = newStorage;
        insertionPoint = newInsertionPoint;
        return changesMade;
    }

    @Override
    public void clear() {
        storage = (E[]) new Object[storage.length];
        insertionPoint = 0;
    }

    @Override
    public boolean equals(Object o){
        if(o == null) return false;
        if(this == o) return true;

        if(this.getClass() == o.getClass()){
            BasicArrayList<E> otherList = (BasicArrayList<E>) o;
            if(this.size() == otherList.size()){
                return Arrays.equals(this.storage, otherList.storage);
            }
        }
        return false;
    }

    @Override
    public E get(int index) {
        if (index >= size())
            throw new IndexOutOfBoundsException(String.format("index %d out of bounds for arraylist length %d",
                    index, size()));
        return storage[index];
    }

    @Override
    public E set(int index, E element) {
        if (index >= size())
            throw new IndexOutOfBoundsException(String.format("index %d out of bounds for arraylist length %d",
                    index, size()));
        E oldElement = storage[index];
        storage[index] = element;
        return oldElement;
    }

    @Override
    public void add(int index, E element) {
        if (index >= size())
            throw new IndexOutOfBoundsException(String.format("index %d out of bounds for arraylist length %d",
                    index, size()));

        if (insertionPoint == storage.length)
            ensureCapacity(1);
        System.arraycopy(storage, index, storage, index+1, (size() - index));
        storage[index] = element;
        insertionPoint++;
    }

    @Override
    public E remove(int index) {
        if (index >= insertionPoint || index < 0)
            throw new IndexOutOfBoundsException(String.format("index %d is out of bounds for length %d.",
                    index, size()));

        E item = storage[index];
        if (index == insertionPoint-1 && storage.length == insertionPoint){
            storage[index] = null;
        }
        else{
            System.arraycopy(storage, index+1, storage, index, (size() - index)-1);
        }
        insertionPoint--;
        return item;
    }

    @Override
    public int indexOf(Object o) {
        for (int i=0; i < size(); i++){
            if (o == null && storage[i] == null || o != null && storage[i].equals(o)){
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for(int i = size()-1; i >= 0; i--){
            if (o == null && storage[i] == null ||
                    o != null && o.equals(storage[i])){

                return i;
            }
        }
        return -1;
    }

    @Override
    public ListIterator<E> listIterator() {
        return new MyListIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new MyListIterator(index);
    }

    private class MyListIterator implements ListIterator<E>{
        private int indexPointer = 0;
        private int lastReturned;
        private boolean canCallRemove = false;

        private MyListIterator(){}

        private MyListIterator(int index){
            indexPointer = index;
        }

        @Override
        public boolean hasNext() {
            return !(indexPointer >= insertionPoint);
        }

        @Override
        public E next() {
            if (indexPointer >= insertionPoint)
                throw new NoSuchElementException();

            canCallRemove = true;
            lastReturned = indexPointer;
            return storage[indexPointer++];
        }

        @Override
        public boolean hasPrevious() {
            return indexPointer > 0;
        }

        @Override
        public E previous() {
            if (indexPointer <= 0)
                throw new NoSuchElementException();

            canCallRemove = true;
            lastReturned = indexPointer - 1;
            return storage[--indexPointer];//could have decremented on previous line but this is clearer.
        }

        @Override
        public int nextIndex() {
            return indexPointer;
        }

        @Override
        public int previousIndex() {
            return indexPointer - 1; //because previous() uses pre increment
        }

        @Override
        public void remove() {
            if (!canCallRemove)
                throw new IllegalStateException();

            BasicArrayList.this.remove(lastReturned);
            indexPointer--;
            canCallRemove = false;
        }


        @Override
        public void set(E item) {
            if (!canCallRemove)
                throw new IllegalStateException();

            BasicArrayList.this.set(lastReturned, item);
        }

        @Override
        public void add(E item) {
            BasicArrayList.this.add(nextIndex(), item);
            canCallRemove = false;
        }
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if(toIndex > size())
            throw new IndexOutOfBoundsException(String.format("Index %d out of bounds for arrayList size %d. ",
                    toIndex, size()));
        BasicArrayList theSubList = new BasicArrayList();
        int sizeToCopy = toIndex - fromIndex;
        if(sizeToCopy >= theSubList.storage.length)
            theSubList.ensureCapacity(sizeToCopy - theSubList.storage.length);
        System.arraycopy(this.storage, fromIndex, theSubList.storage, 0, sizeToCopy);
        theSubList.insertionPoint += sizeToCopy;
        return theSubList;
    }

    @Override
    public String toString(){
        StringBuilder output = new StringBuilder();
        output.append('[');
        for (int i = 0; i < this.size(); i++) {
            output.append(storage[i]); output.append(','); output.append(' ');
        }
        //remove the trailing comma and space
        output.deleteCharAt(output.length() - 1);
        output.deleteCharAt(output.length() - 1);
        output.append(']');
        return output.toString();
    }

    private void nullCheck(Object o, String errorMsg){
        if(o == null)
            throw new NullPointerException(errorMsg);
    }

}
