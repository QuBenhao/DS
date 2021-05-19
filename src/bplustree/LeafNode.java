package bplustree;

public class LeafNode extends TNode{
    // Doubly LinkedList
    public LeafNode left, right;

    public LeafNode(int degree){
        super(degree);
        this.left = this.right = null;
    }

    /*
     * Insert has three situation:
     * 1. Both LeafNode and TreeNode are not full, insert into LeafNode directly
     * 2. Only LeafNode is full, separate LeafNode into two, insert the middle index into TreeNode
     * 3. Both LeafNode and TreeNode are full: separate LeafNode into two, separate TreeNode into Two,
     *  insert the middle index of LeafNode into TreeNode, insert middle index of TreeNode into parent TreeNode
     */
    @Override
    public void insert(String index, int pageIndex, int slots) {
        ListNode curr = this.root, prev = null;
        // insert index into the LinkedList
        while (curr!=null){
            int cmp = curr.index.compareTo(index);
            if(cmp > 0){
                if(prev == null){
                    this.root = new LeafListNode(index, pageIndex, slots);
                    this.root.next = curr;
                    curr.prev = this.root;
                }else {
                    prev.next = new LeafListNode(index, pageIndex, slots);
                    prev.next.prev = prev;
                    prev = prev.next;
                    prev.next = curr;
                    curr.prev = prev;
                }
                this.capacity++;
                break;
            }
            prev = curr;
            curr = curr.next;
        }
        // insert at back
        if(curr == null){
            curr = new LeafListNode(index, pageIndex, slots);
            if(prev == null){
                this.root = this.last = curr;
            }else {
                prev.next = curr;
                curr.prev = prev;
                this.last = curr;
            }
            this.capacity++;
        }

        // after the insertion, Node is full
        if(this.capacity > this.degree){
            curr = this.root;
            for(int i=0;i<this.sep_mid;i++){
                curr = curr.next;
            }
            LeafNode sep_leaf = new LeafNode(this.degree);
            sep_leaf.capacity = this.capacity - this.sep_mid;
            sep_leaf.root = curr;
            sep_leaf.last = this.last;
            sep_leaf.left = this;

            this.last = curr.prev;
            this.last.next = null;
            curr.prev = null;
            this.capacity = this.sep_mid;
            this.right = sep_leaf;

            // insert middle index into parent TreeNode
            if(this.parent == null){
                this.parent = new TreeNode(this.degree);
                this.parent.leftmost_child = this;
                this.parent.root = new ListNode();
                this.parent.root.index = sep_leaf.root.index;
                this.parent.root.child = sep_leaf;
            }else {
                this.parent.insert(sep_leaf.root.index, sep_leaf);
            }
        }
    }

    @Override
    public void delete(String index) {

    }

}
