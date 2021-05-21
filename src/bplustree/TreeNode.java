package bplustree;

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
        keys = new LinkedList<>();
        children = new LinkedList<>();
        // one more pointer to save keys that are smaller than the first key,
        // other keys will made by their first key in child nodes
        leftmost_child = parent = null;
        capacity = 0;
    }

    // binary search to find the position of key belongs to in current keys list
    // return -1 if it's smaller than the minimum key in keys
    public int binary_search(Key key){
        if(capacity > 0 && keys.get(0).compareTo(key) > 0)
            return -1;
        int l = 0, r = capacity - 1;
        while(l<r){
            int mid = (l+r+1)/2;
            int cmp = keys.get(mid).compareTo(key);
            if(cmp <= 0)
                l = mid;
            else
                r = mid - 1;
        }
        return r;
    }

    // this method is called by child Node when child node splits
    public void insert(Key key, TreeNode child){
        // pos: the first position of key that are greater than insert key
        int pos = binary_search(key)+1;
        keys.add(pos, key);
        children.add(pos, child);
        capacity++;

        child.parent = this;
        // split current Node if it's full
        if(capacity == BPlusTree.degree){
            TreeNode sep = new TreeNode();
            sep.parent = parent;
            // child parent is the sep Node if it's inserted in right half
            if(pos >= (capacity-1)/2)
                child.parent = sep;
            // split lists into two
            ListIterator<Key> listIterator1 = keys.listIterator((capacity-1)/2);
            ListIterator<TreeNode> listIterator2 = children.listIterator((capacity-1)/2);
            while(listIterator1.hasNext()) {
                sep.keys.add(listIterator1.next());
                sep.children.add(listIterator2.next());
            }
            sep.capacity = (capacity+1)/2;
            // break the chain of left and right part of lists
            keys.removeAll(sep.keys);
            children.removeAll(sep.children);
            capacity -= sep.capacity;

            // insert sep first Key into TreeNode
            if(parent == null){
                parent = new TreeNode();
                parent.leftmost_child = this;
                // move mid key to parent
                parent.keys.add(sep.keys.removeFirst());
                parent.children.add(sep);
                parent.capacity++;
                sep.parent = parent;
            }else{
                // insert new sep Node in parent if parent already exists
                parent.insert(sep.keys.removeFirst(), sep);
            }
            // As this is TreeNode: sep node no longer has the first key, assign its child to leftmost_child
            sep.leftmost_child = sep.children.removeFirst();
            sep.leftmost_child.parent = sep;
            sep.capacity--;
        }
    }

    public void insert(LeafData data){
        int pos = binary_search(data.key);
        // insert key is smaller than minimum key in keys, insert into leftmost_child
        if(pos == -1)
            leftmost_child.insert(data);
        else
            children.get(pos).insert(data);
    }

    // equal query
    public Pair<Integer, Integer> query(Key key){
        int pos = binary_search(key);
        // query key is smaller than minimum key in keys, query leftmost_child
        if(pos == -1)
            return leftmost_child.query(key);
        return children.get(pos).query(key);
    }

    // range query
    public ArrayList<Pair<Integer, Integer>> query(Key start_key, Key end_key){
        int pos = binary_search(start_key);
        // same as equal query
        if(pos == -1)
            return leftmost_child.query(start_key, end_key);
        return children.get(pos).query(start_key, end_key);
    }

    // for debug purpose, print out node capacity and keys
    public void print(){
        System.out.printf("Node capacity: %d\n", capacity);
        for(Key key:keys)
            System.out.printf("%s%s; ", key.sensorId, key.dateTime);
        System.out.println();
    }
}
