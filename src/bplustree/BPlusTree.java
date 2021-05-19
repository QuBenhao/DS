package bplustree;

import java.util.ArrayList;

// currently implement non-clustered index
public class BPlusTree {
    // the order of the tree
    public final int degree;
    public TNode root;

    public BPlusTree(int degree){
        this.degree = degree;
        this.root = null;
    }

    public void insert(String index, int pageIndex, int slots){
        if(this.root == null)
            this.root = new LeafNode(this.degree-1);
        this.root.insert(index, pageIndex, slots);
        if(this.root.parent != null)
            this.root = this.root.parent;
        this.root.debug_print();
    }

    public void bfs_debug(){
        ArrayList<TNode> nodes = new ArrayList<>();
        System.out.println("root:");
        System.out.println(this.root.root.index);
        nodes.add(this.root);
        int level = 0;
        while (!nodes.isEmpty()){
            System.out.printf("Current level at: %d%n",level);
            ArrayList<TNode> next = new ArrayList<>();
            nodes.forEach(node->{
                if(node!=null) {
                    next.add(node.leftmost_child);
                    ListNode start = node.root;
                    node.debug_print();
                    while (start!=null){
                        next.add(start.child);
                        start = start.next;
                    }
                }
            });
            nodes = next;
            System.out.println();
            level++;
        }
    }
}
