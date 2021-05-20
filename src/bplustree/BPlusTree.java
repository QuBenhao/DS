package bplustree;

import constant.constants;
import javafx.util.Pair;
import utils.LinkedList;
import utils.Node;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

// currently implement non-clustered index
public class BPlusTree {
    // the order of the tree
    public static int degree, pageSize, pageNeeded, num_record_per_page;
    public TNode root;

    public BPlusTree(int degree, int pageSize){
        BPlusTree.degree = degree;
        BPlusTree.pageSize = pageSize;
        BPlusTree.num_record_per_page = BPlusTree.pageSize / constants.LEAF_TOTAL_SIZE;
        BPlusTree.pageNeeded = (int) Math.ceil((double) (degree-1) / num_record_per_page);
        this.root = null;
    }

    // Bottom-up: bulk loading
    public BPlusTree(int degree, int pageSize, FileInputStream inputStream) throws IOException {
        this(degree, pageSize);

        byte[] page = new byte[pageSize];
        byte[] sdtnameBytes = new byte[constants.STD_NAME_SIZE];
        byte[] pageIndexBytes = new byte[constants.PAGE_INDEX_SIZE];
        byte[] slotsBytes = new byte[constants.SLOTS_SIZE];

        int node_page_count = 0, numBytesRead = 0;
        int numBytesInOneRecord = constants.LEAF_TOTAL_SIZE;
        int numRecordsPerPage = pageSize/numBytesInOneRecord;

        while ((numBytesRead = inputStream.read(page)) != -1) {
            for (int i = 0; i < numRecordsPerPage; i++) {
                // Copy record's SdtName (field is located at multiples of the total record byte length)
                System.arraycopy(page, (i*numBytesInOneRecord), sdtnameBytes, 0, constants.STD_NAME_SIZE);
                // Check if field is empty; if so, end of all records found (packed organisation)
                if (sdtnameBytes[0] == 0) {
                    // can stop checking records
                    break;
                }
                String sdtNameString = new String(sdtnameBytes);
                System.arraycopy(page, ((i*numBytesInOneRecord) + constants.PAGE_INDEX_OFFSET), pageIndexBytes, 0, Integer.BYTES);
                System.arraycopy(page, ((i*numBytesInOneRecord) + constants.SLOTS_OFFSET), slotsBytes, 0, Integer.BYTES);
                int pageIndex = ByteBuffer.wrap(pageIndexBytes).getInt();
                int slots = ByteBuffer.wrap(slotsBytes).getInt();

                // TODO: load tree
                this.insert(sdtNameString, pageIndex, slots);
            }
            node_page_count ++;
        }
    }


    public void insert(String index, int pageIndex, int slots){
        if(this.root == null)
            this.root = new LeafNode();
        this.root.insert(index,new Pair<Integer,Integer>(pageIndex, slots));
        if(this.root.parent != null)
            this.root = this.root.parent;
    }

//    public Pair<Integer, Integer> query(String index){
//        Pair<Integer,Integer> result = this.root.query(index);
//        if(result == null)
//            System.out.printf("Index: %s does not exists!\n", index);
//        return result;
//    }
//
//    public ArrayList<Pair<Integer, Integer>> query(String start_index, String end_index){
//        // in case end_index is smaller than start index
//        if (start_index.compareTo(end_index) > 0){
//            String temp = end_index;
//            end_index = start_index;
//            start_index = temp;
//        }
//        ArrayList<Pair<Integer, Integer>> result = this.root.query(start_index, end_index);
//        if(result.isEmpty())
//            System.out.printf("Index: between %s and %s does not exists!\n", start_index, end_index);
//        return result;
//    }

    public void bfs_debug(){
        ArrayList<TNode> nodes = new ArrayList<>();
        System.out.println("root:");
        System.out.println(this.root.lists.root.index);
        nodes.add(this.root);
        int level = 0;
        while (!nodes.isEmpty()){
            System.out.printf("Current level at: %d%n",level);
            ArrayList<TNode> next = new ArrayList<>();
            nodes.forEach(node->{
                if(node!=null) {
                    next.add(node.leftmost_child);
                    Node start = node.lists.root;
                    node.print();
                    while (start!=null){
                        if(start.child!=null && !(start.child instanceof LeafNode))
                            next.add(start.child);
                        start = start.next;
                    }
                }
            });
            nodes = next;
            System.out.println();
            level++;
        }

        TNode node = this.root;
        while (node.leftmost_child!=null)
            node = node.leftmost_child;
        int data = 0;
        while (node!=null) {
            node.print();
            data += node.lists.capacity;
            node = ((LeafNode)node).right;

        }
        System.out.printf("LeafNode data: %d\n", data);

    }
}
