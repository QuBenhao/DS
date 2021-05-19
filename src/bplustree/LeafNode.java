package bplustree;

import javafx.util.Pair;

import java.util.ArrayList;

public class LeafNode extends TNode{
    // Doubly LinkedList
    public LeafNode left, right;

    public LeafNode(){
        super();
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
            // insert index with ordering
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
        if(this.capacity > BPlusTree.degree-1){
            curr = this.root;
            // find the middle LeafNode to separate
            for(int i=0;i<(BPlusTree.degree-1)/2;i++){
                curr = curr.next;
            }
            // create separated LeafNode
            LeafNode sep_leaf = new LeafNode();
            sep_leaf.capacity = this.capacity - BPlusTree.degree/2;
            sep_leaf.root = curr;
            sep_leaf.last = this.last;
            sep_leaf.left = this;
            sep_leaf.right = this.right;
            sep_leaf.parent = this.parent;

            // break the link of ListNode between separate LeafNode and resign capacity
            this.last = curr.prev;
            this.last.next = null;
            curr.prev = null;
            this.capacity = BPlusTree.degree/2;
            this.right = sep_leaf;

            // insert middle index into parent TreeNode
            if(this.parent == null){
                this.parent = new TreeNode();
                this.parent.leftmost_child = this;
                this.parent.root = new ListNode();
                this.parent.root.index = sep_leaf.root.index;
                this.parent.root.child = sep_leaf;
                this.parent.last = this.parent.root;
                this.parent.capacity++;
                sep_leaf.parent = this.parent;
            }else {
                this.parent.insert(sep_leaf.root.index, sep_leaf);
            }
        }
    }

    @Override
    public Pair<Integer, Integer> query(String index){
        ListNode curr = this.root;
        while (curr !=null){
            if(index.compareTo(curr.index) == 0)
                return ((LeafListNode)curr).value;
            curr = curr.next;
        }
        return null;
    }

    // Range query
    @Override
    public ArrayList<Pair<Integer, Integer>> query(String start_index, String end_index) {
        ArrayList<Pair<Integer, Integer>> result = new ArrayList<>();
        LeafNode node = this;
        while (node!=null){
            ListNode curr = node.root;
            while (curr!=null){
                if(curr.index.compareTo(end_index) > 0)
                    break;
                if(curr.index.compareTo(start_index) >=0)
                    result.add(((LeafListNode)curr).value);
                curr = curr.next;
            }
            if(curr!=null && curr.index.compareTo(end_index) > 0)
                break;
            node = node.right;
        }
        return result;
    }


    @Override
    public void delete(String index) {

    }

}
