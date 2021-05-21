package bplustree;

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
        // Full: has to split
        if(this.capacity == BPlusTree.degree){
            LeafNode sep = new LeafNode();
            sep.parent = this.parent;
            sep.left = this;
            this.right = sep;
            ListIterator<LeafData> listIterator = this.values.listIterator((this.capacity-1)/2);
            while(listIterator.hasNext())
                sep.values.add(listIterator.next());
            sep.capacity = (this.capacity+1)/2;

            // TODO: remove assert
            assert sep.capacity == sep.values.size();

            this.values.removeAll(sep.values);
            this.capacity -= sep.capacity;

            //TODO
            assert this.capacity == this.values.size();

            // insert sep first Key into TreeNode
            if(this.parent == null){
                this.parent = new TreeNode();
                this.parent.leftmost_child = this;
                // TODO
                assert sep.values.peekFirst() != null;
                this.parent.keys.add(sep.values.peekFirst().key);
                this.parent.children.add(sep);
                this.parent.capacity++;
                sep.parent = this.parent;
            }else{
                // insert new entry into parent node
                //TODO
                assert sep.values.peekFirst() != null;
                this.parent.insert(sep.values.peekFirst().key, sep);
            }
        }
    }

    @Override
    public void print(){
        System.out.printf("Node capacity: %d\n", this.capacity);
        for(LeafData d:this.values)
            System.out.printf("%s%s; ", d.key.sensorId, d.key.dateTime);
        System.out.println();
    }
}
