import java.util.*;

class Node<T> {
    T data;
    Node<T> prev, next;

    public Node(){}

    public Node(T t, Node<T> prev, Node<T> next) {
        data= t;
        this.prev = prev;
        this.next = next;
    }
}



public class BasicLinkedList<T> implements List<T> {

    private Node<T> head, tail;
    private int size = 0;


    public BasicLinkedList() {
        head = tail = new Node<>();
    }

    @Override
    public String toString(){
        StringBuilder output = new StringBuilder();
        output.append('[');
        for (T data : this){
            output.append(data); output.append(','); output.append(' ');
        }
        output.deleteCharAt(output.length() - 1);
        output.deleteCharAt(output.length() - 1);
        output.append(']');
        return output.toString();
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null) return false;
        if(o.getClass() == this.getClass()){
            BasicLinkedList<T> template = (BasicLinkedList<T>) o;
            if(template.size() == this.size()){
                Node<T> thisCurrent = this.head, templateCurrent = template.head;
                while (thisCurrent.next != null) {
                    thisCurrent = thisCurrent.next;
                    templateCurrent = templateCurrent.next;
                    if(!Objects.equals(thisCurrent.data, templateCurrent.data)){
                        return false;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        nullCheck(o);
        return getNode(o) != null;
    }

    @Override
    public Iterator<T> iterator() {
        return new MyLinkedListIterator();
    }

    private class MyLinkedListIterator implements Iterator<T>{

        private Node<T> prevPtr = head;
        private boolean calledRemove = true;

        @Override
        public boolean hasNext() {
            return prevPtr.next != null;
        }

        @Override
        public T next() {
            calledRemove = false;
            prevPtr = prevPtr.next;
            return prevPtr.data;
        }

        public void remove(){
            if (calledRemove)
                throw new IllegalStateException("remove() can only be called once per call to next()");
            calledRemove = true;
            removeNode(prevPtr);
        }
    }
    @Override
    public Object[] toArray() {
        T[] output = (T[]) new Object[size];
        int index = 0;
        for (T item : this){
            output[index++] = item;
        }
        return output;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        nullCheck(a);
        if (a.length < size)
            a = (T1[]) new Object[size];
        int index = 0;
        for (T item : this){
            a[index++] = (T1)item;
        }
        return a;
    }

    @Override
    public boolean add(T t) {
        Node<T> newNode = new Node<>(t, tail, null);

        tail.next = newNode;
        tail = newNode;

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        nullCheck(o);
        Node<T> toRemove = getNode(o);
        if (toRemove == null)
            return false;
        removeNode(toRemove);
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        nullCheck(c);
        HashSet<T> linkedListItems = new HashSet<T>(this);
        for(Object data : c){
            if(!linkedListItems.contains(data))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        nullCheck(c);
        for (T item : c){
            Node<T> toAdd = new Node(item, tail, null);
            tail.next = toAdd;
            tail = tail.next;
        }
        size += c.size();
        return c.size() != 0;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        nullCheck(c);
        Node<T> endPoint = getNode(index);
        Node<T> currentNode = endPoint.prev;
        for(T item : c){
            currentNode.next = new Node<T>(item, currentNode, null);
            currentNode = currentNode.next;
        }
        currentNode.next = endPoint;
        endPoint.prev = currentNode;
        size += c.size();
        return c.size() != 0;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        nullCheck(c);
        boolean madeChanges = false;
        HashSet<T> collectionItems = new HashSet<T>((Collection<? extends T>) c);
        for(Node<T> currentNode = head.next; currentNode != null; currentNode = currentNode.next){
            if (collectionItems.contains(currentNode.data)){
                removeNode(currentNode);
                madeChanges = true;
            }
        }
        return madeChanges;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        nullCheck(c);
        boolean madeChanges = false;
        HashSet<T> collectionItems = new HashSet<>((Collection<? extends T>) c);
        for(Node<T> currentNode = head.next; currentNode != null; currentNode = currentNode.next){
            if (!collectionItems.contains(currentNode.data)){
                removeNode(currentNode);
                madeChanges = true;
            }
        }
        return madeChanges;
    }

    @Override
    public void clear() {
        head = tail = new Node<T>();
        size = 0;
    }

    @Override
    public T get(int index) {
        if (index >= size || index < 0)
            throw new IndexOutOfBoundsException(String.format("Index %d out of bounds for linkedList of size %d",
                    index, size));

        return getNode(index).data;
    }

    @Override
    public T set(int index, T element) {
        if (index >= size || index < 0)
            throw new IndexOutOfBoundsException(String.format("Index %d out of bounds for linkedList of size %d",
                    index, size));

        Node<T> item = getNode(index);
        T data = item.data;
        item.data = element;
        return data;
    }

    @Override
    public void add(int index, T element) {
        if (index >= size || index < 0)
            throw new IndexOutOfBoundsException(String.format("Index %d out of bounds for linkedList of size %d",
                    index, size));

        insertNode(element, getNode(index).prev);
    }

    @Override
    public T remove(int index) {
        if (index >= size || index < 0)
            throw new IndexOutOfBoundsException(String.format("Index %d out of bounds for linkedList of size %d",
                    index, size));

        return removeNode(getNode(index));
    }

    @Override
    public int indexOf(Object o) {
        int index = 0;
        for(Node<T> currentNode = head.next; currentNode != null; currentNode = currentNode.next, index++){
            if (o.equals(currentNode.data)){
                return index;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int index = size-1;
        for(Node<T> currentNode = tail; currentNode != null; currentNode = currentNode.prev, index--){
            if (o.equals(currentNode.data)){
                return index;
            }
        }
        return -1;
    }

    @Override
    public ListIterator<T> listIterator() {
        return new MyListIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new MyListIterator(index);
    }

    private class MyListIterator implements ListIterator<T>{

        private Node<T> currentNode = head;
        private int currentIndex = -1;
        private boolean canCallRemove = false;
        private boolean lastCalledNext;

        public MyListIterator(){

        }

        public MyListIterator(int startingIndex){
            currentIndex = startingIndex - 1;
            currentNode = getNode(startingIndex).prev;
        }

        @Override
        public boolean hasNext() {
            return currentIndex < size-1;
        }

        @Override
        public T next() {
            if (currentIndex >= size-1)
                throw new NoSuchElementException();
            lastCalledNext = true;
            canCallRemove = true;
            currentIndex++;
            currentNode = currentNode.next;
            return currentNode.data;
        }

        @Override
        public boolean hasPrevious() {
            return currentIndex > -1;
        }

        @Override
        public T previous() {
            if (currentIndex <= -1)
                throw new NoSuchElementException();
            lastCalledNext = false;
            canCallRemove = true;
            T data = currentNode.data;
            currentIndex--;
            currentNode = currentNode.prev;
            return data;
        }

        @Override
        public int nextIndex() {
            return currentIndex + 1;
        }

        @Override
        public int previousIndex() {
            return currentIndex;
        }

        @Override
        public void remove() {
            if (!canCallRemove)
                throw new IllegalStateException("Next or Previous must be called before another call to remove");
            canCallRemove = false;
            if(lastCalledNext) {
                removeNode(currentNode);
                currentNode = currentNode.prev;
            }
            else
                removeNode(currentNode.next);
            currentIndex--;
        }

        @Override
        public void set(T t) {
            if(!canCallRemove)
                throw new IllegalStateException("Next or Previous must be called before another call to set");

            if(lastCalledNext)
                currentNode.data = t;
            else
                currentNode.next.data = t;
        }

        @Override
        public void add(T t) {
            canCallRemove = false;
            if (!this.hasNext()){
                BasicLinkedList.this.add(t);
            }
            else {
                insertNode(t, currentNode);
            }
            currentIndex++;
            currentNode = currentNode.next;
        }
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0)
            throw new IndexOutOfBoundsException(String.format(
                    "Starting index %d is out of bounds. Negetive indecies are not allowed.", fromIndex));
        else if (toIndex > size)
            throw new IndexOutOfBoundsException(String.format(
                    "Index %d is out of bounds for Linked List of size %d", toIndex, size));

        BasicLinkedList<T> subList = new BasicLinkedList<>();
        Node<T> currentNode = getNode(fromIndex);
        for(int i = fromIndex; i < toIndex; i++){
            subList.add(currentNode.data);
            currentNode = currentNode.next;
        }
        return subList;
    }


    private Node<T> getNode(int index){
        Node<T> currentNode = head;
        for (int counter = 0; counter <= index; currentNode = currentNode.next, counter++ )
            ;//empty loop
        return currentNode;
    }

    private Node<T> getNode(Object o){
        Node<T> currentNode = head;
        while (currentNode.next != null){
            currentNode = currentNode.next;
            if (o.equals(currentNode.data))
                return currentNode;
        }
        return null;
    }

    private T removeNode(Node<T> toRemove){
        T removedData = toRemove.data;
        Node<T> beforeRemove = toRemove.prev;//could do it without this variable but it looks very ugly
        beforeRemove.next = toRemove.next;
        if (toRemove.next != null) {
            toRemove = toRemove.next;
            toRemove.prev = beforeRemove;
        }
        else{
            tail = beforeRemove;
        }
        size--;
        return removedData;
    }

    private void insertNode(T data, Node<T> nodeBefore){
        Node<T> toInsert = new Node(data, nodeBefore, nodeBefore.next);
        nodeBefore.next = toInsert;
        toInsert = toInsert.next;
        toInsert.prev = nodeBefore.next;
        size++;
    }

    private void nullCheck(Object o){
        if (o == null){
            throw new NullPointerException();
        }
    }
}

