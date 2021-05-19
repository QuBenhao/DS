package bplustree;

import javafx.util.Pair;

import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.ListIterator;

public abstract class TNode {
    public LinkedList<String> keys;
    public LinkedList<TNode> pointers;
    public TNode leftmost_child, parent;
    protected int capacity;
    public TNode(){
        this.keys = new LinkedList<>();
        this.pointers = new LinkedList<>();
        this.leftmost_child = this.parent = null;
        this.capacity = 0;
    }

    public TNode(Pair<LinkedList<String>,LinkedList<TNode>> lists){
        this.keys = lists.getKey();
        this.pointers = lists.getValue();
        this.leftmost_child = this.parent = null;
        this.capacity = keys.size();
    }

    public int binary_search(String index){
        if(this.capacity==0)
            return -1;
        if(index.compareTo(this.keys.getLast()) >= 0)
            return this.capacity-1;
        if(index.compareTo(this.keys.getFirst()) < 0){
            return -1;
        }
        int l = 0, r = this.capacity - 2;
        while (l < r){
            int mid = (l + r) / 2;
            int cmp_left = index.compareTo(this.keys.get(mid));
            int cmp_right = index.compareTo(this.keys.get(mid+1));
            if(cmp_right >= 0)
                l = mid + 1;
            else if(cmp_left < 0)
                r = mid - 1;
            else
                return mid;
        }
        return l;
    }


    /*
    * Insert has three situation:
    * 1. Both LeafNode and TreeNode are not full, insert into LeafNode directly
    * 2. Only LeafNode is full, separate LeafNode into two, insert the middle index into TreeNode
    * 3. Both LeafNode and TreeNode are full: separate LeafNode into two, separate TreeNode into Two,
    *  insert the middle index of LeafNode into TreeNode, insert middle index of TreeNode into parent TreeNode
    */
    public void insert(String index, TNode child){
        int pos = this.binary_search(index) + 1;
        if(pos == this.capacity){
            this.keys.addLast(index);
            this.pointers.addLast(child);
        }else {
            this.keys.add(pos, index);
            this.pointers.add(pos, child);
        }
        this.capacity++;

        // after the insertion, Node is full
        if(this.capacity > BPlusTree.degree - 1){
            // Separate TreeNode from mid
            TreeNode sep_node = new TreeNode(((TreeNode)this).split());
            sep_node.parent = this.parent;
            this.capacity = this.keys.size();

            if(this.parent == null){
                this.parent = new TreeNode();
                this.parent.leftmost_child = this;
                this.parent.keys.add(sep_node.keys.getFirst());
                this.parent.pointers.add(sep_node);
                sep_node.parent = this.parent;
                this.parent.capacity++;
            }else {
                this.parent.insert(index, this);
            }
            // TreeNode separate is different, as it needs to move middle index from sep_node.root to parent Node
            sep_node.leftmost_child = sep_node.pointers.getFirst();
            sep_node.keys.removeFirst();
            sep_node.pointers.removeFirst();
            sep_node.capacity--;
        }
    }

    public void insert(String index, int pageIndex, int slots){
        int pos = this.binary_search(index);
        if(pos == -1)
            this.leftmost_child.insert(index, pageIndex, slots);
        else
            this.pointers.get(pos).insert(index, pageIndex, slots);
    }

    public abstract void delete(String index);

    public void debug_print(){
        System.out.printf("Capacity: %d\n", this.capacity);
        for (String key : this.keys) System.out.printf("%s ", key);
        System.out.println();
    }
}
