package bplustree;

public abstract class TNode {
    // the first and last ListNode
    public ListNode root, last;
    public TNode leftmost_child, parent;
    // maximum capacity: degree
    protected final int degree;
    // how many ListNodes belong to left when full
    protected final int sep_mid;
    protected int capacity;
    public TNode(int degree){
        this.root = this.last = null;
        this.leftmost_child = this.parent = null;
        this.degree = degree;
        if(this.degree % 2 == 0)
            this.sep_mid = this.degree / 2;
        else
            this.sep_mid = (this.degree - 1) / 2;
        this.capacity = 0;
    }

    /*
    * Insert has three situation:
    * 1. Both LeafNode and TreeNode are not full, insert into LeafNode directly
    * 2. Only LeafNode is full, separate LeafNode into two, insert the middle index into TreeNode
    * 3. Both LeafNode and TreeNode are full: separate LeafNode into two, separate TreeNode into Two,
    *  insert the middle index of LeafNode into TreeNode, insert middle index of TreeNode into parent TreeNode
    */
    public void insert(String index, TNode child){
        ListNode curr = this.root, prev = null;
        // insert index into the LinkedList
        while (curr!=null){
            int cmp = curr.index.compareTo(index);
            if(cmp > 0){
                // this should never happen (as previous root is smaller than insert index)
                if(prev == null){
                    System.err.println("Insert in TreeNode as root should never occur!");
                    this.root = new ListNode(index);
                    this.root.child = child;
                    this.root.next = curr;
                }else {
                    prev.next = new ListNode(index);
                    prev.next.child = child;
                    prev = prev.next;
                    prev.next = curr;
                }
                this.capacity++;
                break;
            }
            prev = curr;
            curr = curr.next;
        }
        // insert at back
        if(curr == null){
            curr = new ListNode(index);
            curr.child = child;
            // this should never happen (as previous root is smaller than insert index)
            if(prev == null){
                System.err.println("Insert in TreeNode as root should never occur!");
                this.root = this.last = curr;
            }else {
                prev.next = curr;
                this.last = curr;
            }
            this.capacity++;
        }

        // after the insertion, Node is full
        if(this.capacity > this.degree){
            curr = this.root;
            for(int i=0;i<this.sep_mid-1;i++){
                curr = curr.next;
            }
            TreeNode sep_node = new TreeNode(this.degree);
            sep_node.capacity = this.capacity - this.sep_mid;
            sep_node.root = curr.next;
            sep_node.last = this.last;

            curr.next = null;
            this.last = curr;

            if(this.parent == null){
                this.parent = new TreeNode(this.degree);
                this.parent.leftmost_child = this;
                this.parent.root = new ListNode();
                this.parent.root.index = sep_node.root.index;
                this.parent.root.child = sep_node;
            }else {
                this.parent.insert(index, this);
            }
        }
    }

    public void insert(String index, int pageIndex, int slots){
        if(index.compareTo(this.root.index) < 0){
            leftmost_child.insert(index, pageIndex, slots);
        }else if(index.compareTo(this.last.index) > 0){
            this.last.child.insert(index, pageIndex, slots);
        }else{
            ListNode curr = this.root;
            while (curr !=null){
                if(index.compareTo(curr.index) > 0) {
                    curr.child.insert(index, pageIndex, slots);
                    break;
                }
                curr = curr.next;
            }
        }
    }

    public abstract void delete(String index);

    public void debug_print(){
        ListNode curr = this.root;
        while (curr !=null){
            System.out.println(curr.index);
            curr = curr.next;
        }
        System.out.println();
    }
}
