package bplustree;

public abstract class TNode {
    // the first and last ListNode
    public ListNode root, last;
    public TNode leftmost_child;
    // maximum capacity: degree
    private final int degree;
    private int capacity;
    public TNode(int degree){
        this.root = this.last = null;
        this.leftmost_child = null;
        this.degree = degree;
        this.capacity = 0;
    }

    /*
    * Insert has three situation:
    * 1. Both LeafNode and TreeNode are not full, insert into LeafNode directly
    * 2. Only LeafNode is full, separate LeafNode into two, insert the middle index into TreeNode
    * 3. Both LeafNode and TreeNode are full: separate LeafNode into two, separate TreeNode into Two,
    *  insert the middle index of LeafNode into TreeNode, insert middle index of TreeNode into parent TreeNode
    */
    public abstract void insert(String index);

    public abstract void delete(String index);
}
