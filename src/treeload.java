import bplustree.BPlusTree;

import java.io.*;
import java.nio.file.Files;

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

        int numRecordsPerPage = pageSize/constants.TOTAL_SIZE;

        byte[] page = new byte[pageSize];
        FileInputStream inStream = null;
        try{
            File file = new File(datafile);
            inStream = new FileInputStream(file);
            // calculate tree degree
            int degree = (int) Math.sqrt((double) file.length()/pageSize);
            BPlusTree tree = new BPlusTree(degree);
            startTime = System.nanoTime();
            int numBytesRead = 0;
            int pageIndex = 0;
            byte[] sdtnameBytes = new byte[constants.STD_NAME_SIZE];

            // until the end of the binary file is reached
            while ((numBytesRead = inStream.read(page)) != -1) {
                // Process each record in page
                for (int i = 0; i < numRecordsPerPage; i++) {
                    int slots = i * constants.TOTAL_SIZE;

                    // Copy record's SdtName (field is located at multiples of the total record byte length)
                    System.arraycopy(page, slots, sdtnameBytes, 0, constants.STD_NAME_SIZE);

                    // Check if field is empty; if so, end of all records found (packed organisation)
                    if (sdtnameBytes[0] == 0) {
                        // can stop checking records
                        break;
                    }
                    String sdtNameString = new String(sdtnameBytes);
                    System.out.printf("Insert %s, %d, %d\n", sdtNameString, pageIndex, slots);
                    tree.insert(sdtNameString,pageIndex,slots);
                }
                pageIndex++;
            }

            FileOutputStream fileOut = new FileOutputStream(String.format("tree%d.ser",pageSize));
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(tree);
            finishTime = System.nanoTime();

            out.close();
            fileOut.close();
            finishTime = System.nanoTime();

//            tree.bfs_debug();
        }catch (FileNotFoundException e) {
            System.err.println("File not found " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Exception " + e.getMessage());
        }
        finally {

            if (inStream != null) {
                inStream.close();
            }
        }

        long timeInMilliseconds = (finishTime - startTime)/constants.MILLISECONDS_PER_SECOND;
        System.out.println("Time taken: " + timeInMilliseconds + " ms");
    }
}
