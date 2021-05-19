package bplustree;

import javafx.util.Pair;

import java.util.LinkedList;
import java.util.ListIterator;

public class LeafNode extends TNode{
    // Doubly LinkedList
    public LeafNode left, right;
    public LinkedList<Pair<Integer, Integer>> values;

    public LeafNode(){
        super();
        this.left = this.right = null;
        this.values = new LinkedList<>();
    }

    public LeafNode(Pair<LinkedList<String>,LinkedList<Pair<Integer, Integer>>> lists){
        super(new Pair<>(lists.getKey(),new LinkedList<>()));
        this.keys = lists.getKey();
        this.values = lists.getValue();
        this.left = this.right = null;
    }

    public Pair<LinkedList<String>,LinkedList<Pair<Integer, Integer>>> split(){
        LinkedList<String> keys = new LinkedList<>();
        LinkedList<Pair<Integer,Integer>> values = new LinkedList<>();
        ListIterator<String> iterators = this.keys.listIterator(this.capacity/2);
        ListIterator<Pair<Integer,Integer>> v_iterators = this.values.listIterator(this.capacity/2);
        while (iterators.hasNext()){
            String index = iterators.next();
            Pair<Integer,Integer> value = v_iterators.next();
            keys.add(index);
            values.add(value);
        }
        this.keys.removeAll(keys);
        this.values.removeAll(values);
        return new Pair<>(keys, values);
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
        int pos = this.binary_search(index) + 1;
        if(pos == this.capacity){
            this.keys.addLast(index);
            this.values.addLast(new Pair<>(pageIndex, slots));
        }
        else {
            this.keys.add(pos, index);
            this.values.add(pos, new Pair<>(pageIndex, slots));
        }
        this.capacity++;

        // after the insertion, Node is full
        if(this.capacity > BPlusTree.degree - 1){
            // Separate TreeNode from mid
            LeafNode sep_node = new LeafNode(this.split());
            sep_node.parent = this.parent;
            sep_node.left = this;
            sep_node.right = this.right;
            this.capacity = this.keys.size();
            this.right = sep_node;

            if(this.parent == null){
                this.parent = new TreeNode();
                this.parent.leftmost_child = this;
                this.parent.keys.add(sep_node.keys.getFirst());
                this.parent.pointers.add(sep_node);
                sep_node.parent = this.parent;
                this.parent.capacity++;
            }else {
                this.parent.insert(sep_node.keys.getFirst(), sep_node);
            }
        }
    }

    @Override
    public void delete(String index) {

    }

}
