package bplustree;

public abstract class TNode {
    public ListNode root;
    private final int degree;
    private int capacity;
    public TNode(int degree){
        this.degree = degree;
        this.capacity = 0;
    }

    public abstract void insert(String index);

    public abstract void delete(String index);
}
