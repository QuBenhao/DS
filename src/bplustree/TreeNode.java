package bplustree;

import javafx.util.Pair;

import java.util.LinkedList;
import java.util.ListIterator;

public class TreeNode extends TNode{
    public TreeNode(){
        super();
    }

    public TreeNode(Pair<LinkedList<String>,LinkedList<TNode>> lists){
        super(lists);
    }

    public Pair<LinkedList<String>,LinkedList<TNode>> split(){
        LinkedList<String> keys = new LinkedList<>();
        LinkedList<TNode> pointers = new LinkedList<>();
        // the first element of right linkedlist
        ListIterator<String> iterators = this.keys.listIterator(this.capacity/2);
        ListIterator<TNode> p_iterators = this.pointers.listIterator(this.capacity/2);
        while (iterators.hasNext()){
            String v = iterators.next();
            TNode child = p_iterators.next();
            keys.add(v);
            pointers.add(child);
        }
        this.keys.removeAll(keys);
        this.pointers.removeAll(pointers);
        return new Pair<>(keys, pointers);
    }

    @Override
    public void delete(String index) {

    }
}
