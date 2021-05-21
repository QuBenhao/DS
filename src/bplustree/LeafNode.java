package bplustree;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

public class LeafNode extends TreeNode{
    public LinkedList<LeafData> values;
    public LeafNode left, right;

    public LeafNode(){
        super();
        this.values = new LinkedList<>();
        this.left = this.right = null;
    }

    @Override
    public int binary_search(Key key){
        // same as TreeNode, as keys are stored in LeafData, LeafNode no longer needs keys
        if(this.capacity > 0 && this.values.get(0).key.compareTo(key) > 0)
            return -1;
        int l = 0, r = this.capacity - 1;
        while(l<r){
            int mid = (l+r+1)/2;
            int cmp = this.values.get(mid).key.compareTo(key);
            if(cmp <= 0)
                l = mid;
            else
                r = mid - 1;
        }
        return r;
    }

    @Override
    public void insert(LeafData data){
        int pos = binary_search(data.key) + 1;
        this.values.add(pos, data);
        this.capacity++;
        // LeafNode is Full: has to split
        if(this.capacity == BPlusTree.degree){
            LeafNode sep = new LeafNode();
            sep.parent = this.parent;
            // Doubly link LeafNodes
            sep.left = this;
            sep.right = this.right;
            if(this.right != null)
                this.right.left = sep;
            this.right = sep;

            ListIterator<LeafData> listIterator = this.values.listIterator((this.capacity-1)/2);
            while(listIterator.hasNext())
                sep.values.add(listIterator.next());
            sep.capacity = (this.capacity+1)/2;

            this.values.removeAll(sep.values);
            this.capacity -= sep.capacity;

            // similar to TreeNode, but should not remove first key in sep node
            // insert sep first Key into TreeNode
            if(this.parent == null){
                this.parent = new TreeNode();
                this.parent.leftmost_child = this;
                this.parent.keys.add(sep.values.get(0).key);
                this.parent.children.add(sep);
                this.parent.capacity++;
                sep.parent = this.parent;
            }else{
                // insert new entry into parent node
                this.parent.insert(sep.values.get(0).key, sep);
            }
        }
    }

    // equal query
    @Override
    public Pair<Integer, Integer> query(Key key){
        int pos = this.binary_search(key);
        if(pos == -1)
            return null;
        LeafData res = this.values.get(pos);
        return new Pair<>(res.pageIndex,res.slots);
    }

    // range query
    @Override
    public ArrayList<Pair<Integer, Integer>> query(Key start_key, Key end_key){
        ArrayList<Pair<Integer, Integer>> result = new ArrayList<>();
        LeafNode node = this;
        boolean stop = false;

        while (node!=null){
            // the first node can binary search start key,
            // the other will always start at the first key (which is greater than start_key)
            ListIterator<LeafData> listIterator = node.values.listIterator();
            if(node == this) {
                int pos = node.binary_search(start_key);
                if (pos == -1)
                    pos = 0;
                listIterator = node.values.listIterator(pos);
            }
            while (listIterator.hasNext()){
                LeafData d = listIterator.next();
                // if reaches end_key, stop
                if(d.key.compareTo(end_key) > 0) {
                    stop = true;
                    break;
                }
                result.add(new Pair<>(d.pageIndex, d.slots));
            }
            if (stop)
                break;
            node = node.right;
        }
        return result;
    }

    public LeafNode construct(LeafData data){
        // construct from file, leave 20% space for later insertion
        if(this.capacity == ((BPlusTree.degree-1)*4)/5){
            this.right = new LeafNode();
            this.right.parent = this.parent;
            if(this.parent == null){
                this.parent = new TreeNode();
                this.parent.leftmost_child = this;
                this.parent.keys.add(data.key);
                this.parent.children.add(this.right);
                this.parent.capacity++;
                this.right.parent = this.parent;
            }else {
                this.parent.insert(data.key, this.right);
            }
            this.right.left = this;
            this.right.values.add(data);
            this.right.capacity++;
            return this.right;
        }
        this.values.add(data);
        this.capacity++;
        return this;
    }


    // same as TreeNode
    @Override
    public void print(){
        System.out.printf("Node capacity: %d\n", this.capacity);
        for(LeafData d:this.values)
            System.out.printf("%s%s\n", d.key.sensorId, d.key.dateTime);
        System.out.println();
    }
}
