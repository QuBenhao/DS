import bplustree.BPlusTree;

import java.io.IOException;

public class treeload {
    public static void main(String[] args) throws IOException {
        // check for correct number of arguments
        if (args.length != constants.TREELOAD_ARG_COUNT) {
            System.out.println("Error: Incorrect number of arguments were input");
            return;
        }
        int pageSize = Integer.parseInt(args[constants.TREELOAD_PAGE_SIZE_ARG]);
        String datafile = "heap." + pageSize;
        long startTime = 0;
        long finishTime = 0;

        BPlusTree tree = new BPlusTree();
    }
}
