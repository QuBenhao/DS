package bplustree;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class TNode implements Serializable {
    // the first and last ListNode
    public ListNode root, last;
    public TNode leftmost_child, parent;
    // maximum capacity: degree - 1
    protected final int max_capacity;
    // how many ListNodes belong to left when full
    protected final int sep_mid;
    protected int capacity;
    public TNode(int max_capacity){
        this.root = this.last = null;
        this.leftmost_child = this.parent = null;
        this.max_capacity = max_capacity;
        if(this.max_capacity % 2 == 0)
            this.sep_mid = this.max_capacity / 2;
        else
            this.sep_mid = (this.max_capacity - 1) / 2;
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
        if(this.capacity > this.max_capacity){
            curr = this.root;
            for(int i=0;i<this.sep_mid-1;i++){
                curr = curr.next;
            }
            // Separate TreeNode from mid
            TreeNode sep_node = new TreeNode(this.max_capacity);
            sep_node.capacity = this.capacity - this.sep_mid;
            sep_node.root = curr.next;
            sep_node.last = this.last;
            sep_node.parent = this.parent;

            curr.next = null;
            this.capacity = this.sep_mid;
            this.last = curr;

            if(this.parent == null){
                this.parent = new TreeNode(this.max_capacity);
                this.parent.leftmost_child = this;
                this.parent.root = new ListNode();
                this.parent.root.index = sep_node.root.index;
                this.parent.root.child = sep_node;
                sep_node.parent = this.parent;
                this.parent.capacity++;
            }else {
                this.parent.insert(sep_node.root.index, this);
            }
            // TreeNode separate is different, as it needs to move middle index from sep_node.root to parent Node
            sep_node.leftmost_child = sep_node.root.child;
            sep_node.root = sep_node.root.next;
            sep_node.capacity--;
        }
    }

    public void insert(String index, int pageIndex, int slots){
        // insert into leftmost_child if index < root.index
        // insert into last if index > last.index
        if(leftmost_child != null && index.compareTo(this.root.index) < 0){
            leftmost_child.insert(index, pageIndex, slots);
        }else if(this.last != null && index.compareTo(this.last.index) > 0){
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

    public Pair<Integer, Integer> query(String index){
        if(leftmost_child != null && index.compareTo(this.root.index) < 0){
            return leftmost_child.query(index);
        }else if(this.last != null && index.compareTo(this.last.index) >= 0){
            return this.last.child.query(index);
        }else{
            ListNode curr = this.root;
            while (curr !=null){
                if(index.compareTo(curr.index) >= 0)
                    return curr.child.query(index);
                curr = curr.next;
            }

        }
        return null;
    }

    // Range query
    public ArrayList<Pair<Integer, Integer>> query(String start_index, String end_index){
        if(leftmost_child != null && start_index.compareTo(this.root.index) < 0){
            return leftmost_child.query(start_index, end_index);
        }else if(this.last != null && start_index.compareTo(this.last.index) >= 0){
            return this.last.child.query(start_index, end_index);
        }else{
            ListNode curr = this.root;
            while (curr !=null){
                if(start_index.compareTo(curr.index) >= 0)
                    return curr.child.query(start_index, end_index);
                curr = curr.next;
            }

        }
        return null;
    }

    public abstract void delete(String index);

    public void debug_print(){
        System.out.printf("Capacity: %d, Max: %d\n", this.capacity, this.max_capacity);
        ListNode curr = this.root;
        while (curr !=null){
            System.out.println(curr.index);
            curr = curr.next;
        }
        System.out.println();
    }
}
