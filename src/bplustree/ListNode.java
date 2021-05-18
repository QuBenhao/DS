package bplustree;

public class ListNode{
    public ListNode next;
    // index key
    public String index;
    // implement a pointer to child treeNode
    public TNode child;

    public ListNode(int degree){
        this.next = null;
        this.child = null;
    }
}
