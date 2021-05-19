package bplustree;

import javafx.util.Pair;

import java.io.File;
import java.util.ArrayList;

// currently implement non-clustered index
public class BPlusTree {
    // the order of the tree
    public static int degree, pageSize, pageNeeded,num_record_per_page;
    public TNode root;

    public BPlusTree(int degree, int pageSize){
        BPlusTree.degree = degree;
        BPlusTree.pageSize = pageSize;
        BPlusTree.pageNeeded = Math.max(1,(int) (Math.ceil((double) BPlusTree.degree * 32 - 28)/BPlusTree.pageSize));
        BPlusTree.num_record_per_page = (BPlusTree.pageSize - 4) / 32;
        this.root = null;
    }

    public BPlusTree(int degree, int pageSize, File file){
        BPlusTree.degree = degree;
        BPlusTree.pageSize = pageSize;
        BPlusTree.pageNeeded = Math.max(1,(int) (Math.ceil((double) BPlusTree.degree * 32 - 28)/BPlusTree.pageSize));
        BPlusTree.num_record_per_page = (BPlusTree.pageSize - 4) / 32;
        this.root = null;
    }


    public void insert(String index, int pageIndex, int slots){
        if(this.root == null)
            this.root = new LeafNode();
        this.root.insert(index, pageIndex, slots);
        if(this.root.parent != null)
            this.root = this.root.parent;
    }

    public Pair<Integer, Integer> query(String index){
        Pair<Integer,Integer> result = this.root.query(index);
        if(result == null)
            System.out.printf("Index: %s does not exists!\n", index);
        return result;
    }

    public ArrayList<Pair<Integer, Integer>> query(String start_index, String end_index){
        // in case end_index is smaller than start index
        if (start_index.compareTo(end_index) > 0){
            String temp = end_index;
            end_index = start_index;
            start_index = temp;
        }
        ArrayList<Pair<Integer, Integer>> result = this.root.query(start_index, end_index);
        if(result.isEmpty())
            System.out.printf("Index: between %s and %s does not exists!\n", start_index, end_index);
        return result;
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
