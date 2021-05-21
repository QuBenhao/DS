import bplustree.*;
import constant.constants;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

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
        int num_records = 0, num_of_page_used = 0, records_loaded = 0;

        int numRecordsPerPage = pageSize/constants.TOTAL_SIZE;
        int numLeafRecordsPerPage = pageSize/constants.LEAF_TOTAL_SIZE;

        byte[] page = new byte[pageSize];
        FileInputStream inStream = null;
        FileOutputStream outputStream = null;
        ByteArrayOutputStream byteOutputStream = null;
        DataOutputStream dataOutput = null;

        ArrayList<LeafData> data = new ArrayList<>();

        try{
            File file = new File(datafile);
            inStream = new FileInputStream(file);
            outputStream = new FileOutputStream(String.format("bptree.%d",pageSize), true);
            byteOutputStream = new ByteArrayOutputStream();
            dataOutput = new DataOutputStream(byteOutputStream);
            int numBytesRead = 0;
            int pageIndex = 0;
            byte[] sdtnameBytes = new byte[constants.STD_NAME_SIZE];

            startTime = System.nanoTime();

            // until the end of the binary file is reached
            while ((numBytesRead = inStream.read(page)) != -1) {
                // Process each record in page
                for (int i = 0; i < numRecordsPerPage; i++) {
                    records_loaded++;
                    // which byte the data is located in current page
                    int slots = i * constants.TOTAL_SIZE;

                    // Copy record's SdtName (field is located at multiples of the total record byte length)
                    System.arraycopy(page, slots, sdtnameBytes, 0, constants.STD_NAME_SIZE);

                    // Check if field is empty; if so, end of all records found (packed organisation)
                    if (sdtnameBytes[0] == 0) {
                        // can stop checking records
                        break;
                    }
                    String sdtNameString = new String(sdtnameBytes);
                    data.add(new LeafData(sdtNameString, pageIndex, slots));
                }
                pageIndex++;
            }

            // sort index based on sensorId first, then timestamp
            Collections.sort(data);

            // bulk loading: save sorted leafNode indexes and pointer to original heap data
            for(LeafData d: data){
                num_records++;
                // each data needs 32 bytes
                d.write(dataOutput);
                // Write one page
                if(num_records % numLeafRecordsPerPage == 0) {
                    dataOutput.flush();
                    byte[] paget = new byte[pageSize];
                    byte[] records = byteOutputStream.toByteArray();
                    int numberBytesToCopy = byteOutputStream.size();
                    System.arraycopy(records, 0, paget, 0, numberBytesToCopy);
                    outputStream.write(paget);
                    byteOutputStream.reset();
                    num_of_page_used++;
                }
            }

            // At end of ArrayList, check if there are LeafData in the current page to be written out
            if (num_records % numLeafRecordsPerPage != 0) {
                dataOutput.flush();
                byte[] paget = new byte[pageSize];
                byte[] records = byteOutputStream.toByteArray();
                int numberBytesToCopy = byteOutputStream.size();
                System.arraycopy(records, 0, paget, 0, numberBytesToCopy);
                outputStream.write(paget);
                byteOutputStream.reset();
                num_of_page_used++;
            }

            finishTime = System.nanoTime();
        }catch (FileNotFoundException e) {
            System.err.println("File not found " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Exception " + e.getMessage());
        }
        finally {
            if (inStream != null) {
                inStream.close();
            }
            if (dataOutput != null) {
                dataOutput.close();
            }
            if (byteOutputStream != null) {
                byteOutputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }

        System.out.println("The number of records loaded: " + records_loaded);
        System.out.println("The number of indexes inserted: " + num_records);
        System.out.println("The number of pages used: " + num_of_page_used);
        long timeInMilliseconds = (finishTime - startTime)/constants.MILLISECONDS_PER_SECOND;
        System.out.println("Time taken: " + timeInMilliseconds + " ms");
    }
}
