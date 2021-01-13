class DynamicArray <E> {//this class is not for public use

    private E[] storage;
    private int insertionPoint;

    public DynamicArray(){
        storage = (E[]) new Object[10];
        insertionPoint = 0;
    }

    public int size(){
        return insertionPoint;
    }

    public void set(int index, E data){//error checking for indexOutOfBounds should be done in the calling class
        storage[index] = data;
    }
}
