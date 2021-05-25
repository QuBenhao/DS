package bplustree;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import constant.constants;

public class BPlusTree {
    public static int degree, pageSize;
    public TreeNode root;
    public BPlusTree(int degree, int pageSize){
        // maximum child pointer a TreeNode can have (or: max_capacity+1)
        BPlusTree.degree = degree;
        // pageSize: used for reading tree file to construct tree
        BPlusTree.pageSize = pageSize;
        // empty root start from a LeafNode
        root = new LeafNode();
    }

    public void insert(LeafData data){
        root.insert(data);
        // if after the insertion, root has spliced, assign root to new root (root.parent)
        if(root.parent!=null)
            root = root.parent;
    }

    // range Query
    public ArrayList<Integer> query(Key start_key, Key end_key) {
        // if end_key happens to be smaller, switch them
        if(end_key.compareTo(start_key) < 0) {
            System.err.println("End Index is smaller than start index, switching...");
            Key temp = start_key;
            start_key = end_key;
            end_key = temp;
        }
        return root.query(start_key, end_key);
    }

    // equal Query, could replace this function with range Query input same key
    public int query(Key key) {
        return root.query(key);
    }

    // build B+Tree from sorted index pointer file
    public void construct(FileInputStream inputStream) throws IOException {
        byte[] page = new byte[pageSize];
        byte[] sdtnameBytes = new byte[constants.STD_NAME_SIZE];
        byte[] pageIndexBytes = new byte[constants.PAGE_INDEX_SIZE];
        byte[] slotsBytes = new byte[constants.SLOTS_SIZE];

        int node_page_count = 0, numBytesRead = 0;
        int numBytesInOneRecord = constants.LEAF_TOTAL_SIZE;
        int numRecordsPerPage = pageSize/numBytesInOneRecord;

        LeafNode node = (LeafNode) root;
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
                if(root.parent !=null)
                    root = root.parent;
            }
            node_page_count ++;
        }
        System.out.printf("Total page loaded: %d\n",node_page_count);
    }

    // print the tree
    public void print(){
        ArrayList<TreeNode> nodes = new ArrayList<>();
        nodes.add(root);
        int level = 0;
        while(!nodes.isEmpty()){
            System.out.printf("Current level: %d\n", level);
            ArrayList<TreeNode> next = new ArrayList<>();
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

        System.out.printf("LeafNodes level: %d\n", level);
        TreeNode node = root;
        while(node.leftmost_child!=null)
            node = node.leftmost_child;
        int count = 0;
        while (node !=null){
            node.print();
            count += node.capacity;
            node = ((LeafNode)node).right;
        }
        System.out.printf("LeafNode total data: %d\n", count);
    }
}
