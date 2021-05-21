package bplustree;

import constant.constants;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

public class TreeNode {
    public LinkedList<Key> keys;
    public LinkedList<TreeNode> children;
    public TreeNode leftmost_child, parent;
    public int capacity;

    public TreeNode(){
        this.keys = new LinkedList<>();
        this.children = new LinkedList<>();
        this.leftmost_child = this.parent = null;
        this.capacity = 0;
    }

    public int binary_search(Key key){
        if(this.capacity > 0 && this.keys.get(0).compareTo(key) > 0)
            return -1;
        int l = 0, r = this.capacity - 1;
        while(l<r){
            int mid = (l+r+1)/2;
            int cmp = this.keys.get(mid).compareTo(key);
            if(cmp <= 0)
                l = mid;
            else
                r = mid - 1;
        }
        return r;
    }

    public void insert(Key key, TreeNode child){
        int pos = binary_search(key)+1;
        this.keys.add(pos, key);
        this.children.add(pos, child);
        this.capacity++;
        child.parent = this;
        // split
        if(this.capacity == BPlusTree.degree){
            TreeNode sep = new TreeNode();
            sep.parent = this.parent;
            // child parent is the sep Node if it's inserted in right half
            if(pos >= (this.capacity-1)/2)
                child.parent = sep;
            ListIterator<Key> listIterator1 = this.keys.listIterator((this.capacity-1)/2);
            ListIterator<TreeNode> listIterator2 = this.children.listIterator((this.capacity-1)/2);
            while(listIterator1.hasNext()) {
                sep.keys.add(listIterator1.next());
                sep.children.add(listIterator2.next());
            }
            sep.capacity = (this.capacity+1)/2;
            this.keys.removeAll(sep.keys);
            this.children.removeAll(sep.children);
            this.capacity -= sep.capacity;

            // insert sep first Key into TreeNode
            if(this.parent == null){
                this.parent = new TreeNode();
                this.parent.leftmost_child = this;
                // TODO
                assert sep.keys.peekFirst() != null;

                // put mid key into parent
                this.parent.keys.add(sep.keys.removeFirst());
                this.parent.children.add(sep);
                this.parent.capacity++;
                sep.parent = this.parent;
            }else{
                // insert new entry into parent node
                this.parent.insert(sep.keys.removeFirst(), sep);
            }
            // sep node no longer has the first key, assign its child to leftmost_child
            sep.leftmost_child = sep.children.removeFirst();
            sep.leftmost_child.parent = sep;
            sep.capacity--;
        }
    }

    public void insert(LeafData data){
        int pos = this.binary_search(data.key);
        if(pos == -1)
            this.leftmost_child.insert(data);
        else
            this.children.get(pos).insert(data);
    }

    public Pair<Integer, Integer> query(Key key){
        int pos = this.binary_search(key);
        if(pos == -1)
            return this.leftmost_child.query(key);
        return this.children.get(pos).query(key);
    }

    public ArrayList<Pair<Integer, Integer>> query(Key start_key, Key end_key){
        int pos = this.binary_search(start_key);
        if(pos == -1)
            return this.leftmost_child.query(start_key, end_key);
        return this.children.get(pos).query(start_key, end_key);
    }

    public void print(){
        System.out.printf("Node capacity: %d\n", this.capacity);
        for(Key key:this.keys)
            System.out.printf("%s%s; ", key.sensorId, key.dateTime);
        System.out.println();
    }
}
