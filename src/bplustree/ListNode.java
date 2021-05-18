package bplustree;

public class ListNode{
    public ListNode prev, next;
    // index key
    public String index;
    // implement a pointer to child treeNode
    public TNode child;

    public ListNode(){
        this.prev = this.next = null;
        this.index = null;
        this.child = null;
    }

    public ListNode(String index){
        this.prev = this.next = null;
        this.index = index;
        this.child = null;
    }

    public ListNode(ListNode last, String index){
        this.prev = last;
        this.next = null;
        this.index = index;
        this.child = null;
    }
}
