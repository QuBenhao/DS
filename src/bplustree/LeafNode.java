package bplustree;

public class LeafNode extends TNode{
    // Doubly LinkedList
    public LeafNode left, right;

    public LeafNode(int degree){
        super(degree);
        this.left = this.right = null;
    }

    @Override
    public void insert(String index) {

    }

    @Override
    public void delete(String index) {

    }

}
