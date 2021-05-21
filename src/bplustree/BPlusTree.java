package bplustree;

import javafx.util.Pair;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import constant.constants;

public class BPlusTree {
    public static int degree, pageSize;
    public TreeNode root;
    public BPlusTree(int degree, int pageSize){
        BPlusTree.degree = degree;
        BPlusTree.pageSize = pageSize;
        this.root = new LeafNode();
    }

    public void insert(LeafData data){
        this.root.insert(data);
        if(this.root.parent!=null)
            this.root = this.root.parent;
    }

    public void print(){
        ArrayList<TreeNode> nodes = new ArrayList<>();
        nodes.add(this.root);
        int level = 0;
        while(!nodes.isEmpty()){
            System.out.printf("Current level: %d\n", level);
            ArrayList<TreeNode> next = new ArrayList<TreeNode>();
            for(TreeNode node:nodes){
                if(node == null)
                    continue;
                node.print();
                System.out.println();
                if(node.leftmost_child instanceof LeafNode)
                    continue;
                next.add(node.leftmost_child);
                next.addAll(node.children);
            }
            nodes = next;
            level++;
        }

        System.out.println("LeafNodes:");
        TreeNode node = this.root;
        while(node.leftmost_child!=null)
            node = node.leftmost_child;
        int count = 0;
        while (node !=null){
            node.print();
            count += node.capacity;
            node = ((LeafNode)node).right;
        }
        System.out.printf("LeafNode data: %d\n", count);
    }

    public ArrayList<Pair<Integer, Integer>> query(Key start_key, Key end_key) {
        return this.root.query(start_key, end_key);
    }

    public Pair<Integer, Integer> query(Key key) {
        return this.root.query(key);
    }

    public void construct(FileInputStream inputStream) throws IOException {
        byte[] page = new byte[pageSize];
        byte[] sdtnameBytes = new byte[constants.STD_NAME_SIZE];
        byte[] pageIndexBytes = new byte[constants.PAGE_INDEX_SIZE];
        byte[] slotsBytes = new byte[constants.SLOTS_SIZE];

        int node_page_count = 0, numBytesRead = 0;
        int numBytesInOneRecord = constants.LEAF_TOTAL_SIZE;
        int numRecordsPerPage = pageSize/numBytesInOneRecord;

        LeafNode node = (LeafNode) this.root;
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
                node = node.construct(new LeafData(sdtNameString, pageIndex, slots));
                if(this.root.parent !=null)
                    this.root = this.root.parent;
            }
            node_page_count ++;
        }
        System.out.printf("Total page loaded: %d\n",node_page_count);
    }
}
