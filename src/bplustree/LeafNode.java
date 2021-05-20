package bplustree;

import javafx.util.Pair;

public class LeafNode extends TNode{
    public LeafNode left, right;
    public LeafNode(){
        super();
        this.left = this.right = null;
    }
    public LeafNode(String index, Pair<Integer, Integer> value){
        super(index, value);
        this.left = this.right = null;
    }
}
