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
}


class LeafListNode extends ListNode{
    public int pageIndex, slots;
    public LeafListNode(String index, int pageIndex, int slots){
        super(index);
        this.pageIndex = pageIndex;
        this.slots = slots;
    }
}
