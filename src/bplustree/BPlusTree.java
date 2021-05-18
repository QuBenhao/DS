package bplustree;

public class BPlusTree {
    // the order of the tree
    private final int d;
    // whether or not saving the data in the LeafNode, currently implement non-clustered index
    private final boolean clustered;

    public BPlusTree() {
        // default order of the tree set to 4
        this.d = 4;
        this.clustered = false;
    }

    public BPlusTree(int d){
        this.d = d;
        this.clustered = false;
    }
}
