package bplustree;

import constant.constants;
import javafx.util.Pair;

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
        BPlusTree.pageNeeded = Math.max(1,(int) (Math.ceil((double) (BPlusTree.degree-1) * constants.LEAF_TOTAL_SIZE)/BPlusTree.pageSize));
        BPlusTree.num_record_per_page = BPlusTree.pageSize / constants.LEAF_TOTAL_SIZE;
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
        LeafNode currNode = new LeafNode(), tempNode = null;
        TreeNode parent = new TreeNode();
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
                if(currNode.root==null){
                    currNode.root = new LeafListNode(sdtNameString, pageIndex, slots);
                    currNode.last = currNode.root;
                    currNode.capacity++;
                }else {
                    currNode.last.next = new LeafListNode(sdtNameString, pageIndex, slots);
                    currNode.last = currNode.last.next;
                    currNode.capacity++;
                }
            }
            node_page_count ++;
            if (node_page_count % BPlusTree.pageNeeded == 0) {
                // TODO: save leafNode in BPlusTree
                if(parent.leftmost_child == null) {
                    parent.leftmost_child = currNode;
                    currNode.parent = parent;
                }
                else if(parent.root == null){
                    parent.root = new ListNode(currNode.root.index);
                    parent.last = parent.root;
                    parent.root.child = currNode;
                    currNode.parent = parent;
                    parent.capacity++;
                }else {
                    if(parent.capacity == BPlusTree.degree-1){
                        if(this.root == null) {
                            this.root = new TreeNode();
                            this.root.root = new ListNode(currNode.root.index);
                            this.root.last = this.root.root;
                            this.root.leftmost_child = parent;
                            parent.parent = this.root;
                            this.root.capacity++;
                        }else {
                            this.root.last.next = new ListNode(currNode.root.index);
                            this.root.last = this.root.last.next;
                            this.root.last.child = parent;
                            parent.parent = this.root;
                            this.root.capacity++;
                        }
                        parent.root = new ListNode(currNode.root.index);
                        parent.last = parent.root;
                        parent.root.child = currNode;
                        currNode.parent = parent;
                        parent.capacity++;
                    }else {
                        parent.last.next = new ListNode(currNode.root.index);
                        parent.last = parent.last.next;
                        parent.last.child = currNode;
                        currNode.parent = parent;
                        parent.capacity++;
                    }
                }
                tempNode = new LeafNode();
                currNode.right = tempNode;
                tempNode.left = currNode;
                currNode = tempNode;
            }
        }
        if(this.root == null){
            this.root = parent;
        }
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
