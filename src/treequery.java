//import bplustree.BPlusTree;
//import constant.constants;
//import javafx.util.Pair;
//
//import java.io.*;
//import java.nio.ByteBuffer;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//
//public class treequery {
//
//    // Reads in a binary file of the argument-specified pagesize, prints out matching records
//    public static void main(String[] args) throws IOException, ClassNotFoundException {
//
//        // check for correct number of arguments
//        if (args.length > constants.TREEQUERY_MAX_ARG_COUNT || args.length < constants.TREEQUERY_MIN_ARG_COUNT) {
//            System.out.println("Error: Incorrect number of arguments were input");
//            return;
//        }
//        int pageSize;
//        StringBuilder start_index_builder = new StringBuilder(args[0].replace('_',' '));
//        while (start_index_builder.length() < constants.STD_NAME_SIZE)
//            start_index_builder.append(' ');
//        String start_index = start_index_builder.toString(), end_index = null;
//        StringBuilder end_index_builder = null;
//        if(args.length == constants.TREEQUERY_MAX_ARG_COUNT) {
//            end_index_builder = new StringBuilder(args[1].replace('_', ' '));
//            while (end_index_builder.length() < constants.STD_NAME_SIZE)
//                end_index_builder.append(' ');
//            end_index = end_index_builder.toString();
//            pageSize = Integer.parseInt(args[constants.TREEQUERY_MAX_PAGE_SIZE_ARG]);
//        }else
//            pageSize = Integer.parseInt(args[constants.TREEQUERY_MIN_PAGE_SIZE_ARG]);
//        String datafile = "heap." + pageSize;
//        String treefile = String.format("bptree.%d", pageSize);
//        long startTime = 0;
//        long finishTime = 0;
//
//        int numBytesIntField = Integer.BYTES;
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
//
//        FileInputStream inStream = null;
//        FileInputStream inStream_tree = null;
//        try{
//            File file = new File(datafile);
//            inStream = new FileInputStream(file);
//            inStream_tree = new FileInputStream(treefile);
//
//            // calculate tree degree
//            int degree = (int) Math.sqrt((double) file.length()/pageSize);
//            startTime = System.nanoTime();
//
//            BPlusTree tree = new BPlusTree(degree,pageSize,inStream_tree);
//
//            tree.bfs_debug();
//
//            byte[] sdtnameBytes = new byte[constants.STD_NAME_SIZE];
//            byte[] idBytes = new byte[constants.ID_SIZE];
//            byte[] dateBytes = new byte[constants.DATE_SIZE];
//            byte[] yearBytes = new byte[constants.YEAR_SIZE];
//            byte[] monthBytes = new byte[constants.MONTH_SIZE];
//            byte[] mdateBytes = new byte[constants.MDATE_SIZE];
//            byte[] dayBytes = new byte[constants.DAY_SIZE];
//            byte[] timeBytes = new byte[constants.TIME_SIZE];
//            byte[] sensorIdBytes = new byte[constants.SENSORID_SIZE];
//            byte[] sensorNameBytes = new byte[constants.SENSORNAME_SIZE];
//            byte[] countsBytes = new byte[constants.COUNTS_SIZE];
//            RandomAccessFile raf = new RandomAccessFile(datafile, "r");
//            // range query
//            if(end_index!=null) {
//                ArrayList<Pair<Integer, Integer>> result = tree.query(start_index, end_index.toString());
//                if (!result.isEmpty()){
//                    result.forEach(p -> {
//                        int pageIndex = p.getKey();
//                        int slots = p.getValue();
//                        try {
//                            raf.seek((long) pageIndex * pageSize + slots);
//                            byte[] data = new byte[constants.TOTAL_SIZE];
//                            raf.read(data);
//                            /*
//                             * Fixed Length Records (total size = 112 bytes):
//                             * SDT_NAME field = 24 bytes, offset = 0
//                             * id field = 4 bytes, offset = 24
//                             * date field = 8 bytes, offset = 28
//                             * year field = 4 bytes, offset = 36
//                             * month field = 9 bytes, offset = 40
//                             * mdate field = 4 bytes, offset = 49
//                             * day field = 9 bytes, offset = 53
//                             * time field = 4 bytes, offset = 62
//                             * sensorid field = 4 bytes, offset = 66
//                             * sensorname field = 38 bytes, offset = 70
//                             * counts field = 4 bytes, offset = 108
//                             *
//                             * Copy the corresponding sections of "page" to the individual field byte arrays
//                             */
//                            System.arraycopy(data, 0, sdtnameBytes, 0, constants.STD_NAME_SIZE);
//                            System.arraycopy(data, constants.STD_NAME_SIZE, idBytes, 0, numBytesIntField);
//                            System.arraycopy(data, constants.DATE_OFFSET, dateBytes, 0, constants.DATE_SIZE);
//                            System.arraycopy(data, constants.YEAR_OFFSET, yearBytes, 0, numBytesIntField);
//                            System.arraycopy(data, constants.MONTH_OFFSET, monthBytes, 0, constants.MONTH_SIZE);
//                            System.arraycopy(data, constants.MDATE_OFFSET, mdateBytes, 0, numBytesIntField);
//                            System.arraycopy(data, constants.DAY_OFFSET, dayBytes, 0, constants.DAY_SIZE);
//                            System.arraycopy(data, constants.TIME_OFFSET, timeBytes, 0, numBytesIntField);
//                            System.arraycopy(data, constants.SENSORID_OFFSET, sensorIdBytes, 0, numBytesIntField);
//                            System.arraycopy(data, constants.SENSORNAME_OFFSET, sensorNameBytes, 0, constants.SENSORNAME_SIZE);
//                            System.arraycopy(data, constants.COUNTS_OFFSET, countsBytes, 0, numBytesIntField);
//
//                            // Convert long data into Date object
//                            Date date = new Date(ByteBuffer.wrap(dateBytes).getLong());
//                            String sdtNameString = new String(sdtnameBytes);
//
//                            // Get a string representation of the record for printing to stdout
//                            String record = sdtNameString.trim() + "," + ByteBuffer.wrap(idBytes).getInt()
//                                    + "," + dateFormat.format(date) + "," + ByteBuffer.wrap(yearBytes).getInt() +
//                                    "," + new String(monthBytes).trim() + "," + ByteBuffer.wrap(mdateBytes).getInt()
//                                    + "," + new String(dayBytes).trim() + "," + ByteBuffer.wrap(timeBytes).getInt()
//                                    + "," + ByteBuffer.wrap(sensorIdBytes).getInt() + "," +
//                                    new String(sensorNameBytes).trim() + "," + ByteBuffer.wrap(countsBytes).getInt();
//                            System.out.println(record);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    });
//                }
//            }else {
//                Pair<Integer, Integer> result = tree.query(start_index);
//                if (result!=null) {
//                    int pageIndex = result.getKey();
//                    int slots = result.getValue();
//                    try {
//                        raf.seek((long) pageIndex * pageSize + slots);
//                        byte[] data = new byte[constants.TOTAL_SIZE];
//                        raf.read(data);
//                        /*
//                         * Fixed Length Records (total size = 112 bytes):
//                         * SDT_NAME field = 24 bytes, offset = 0
//                         * id field = 4 bytes, offset = 24
//                         * date field = 8 bytes, offset = 28
//                         * year field = 4 bytes, offset = 36
//                         * month field = 9 bytes, offset = 40
//                         * mdate field = 4 bytes, offset = 49
//                         * day field = 9 bytes, offset = 53
//                         * time field = 4 bytes, offset = 62
//                         * sensorid field = 4 bytes, offset = 66
//                         * sensorname field = 38 bytes, offset = 70
//                         * counts field = 4 bytes, offset = 108
//                         *
//                         * Copy the corresponding sections of "page" to the individual field byte arrays
//                         */
//                        System.arraycopy(data, 0, sdtnameBytes, 0, constants.STD_NAME_SIZE);
//                        System.arraycopy(data, constants.STD_NAME_SIZE, idBytes, 0, numBytesIntField);
//                        System.arraycopy(data, constants.DATE_OFFSET, dateBytes, 0, constants.DATE_SIZE);
//                        System.arraycopy(data, constants.YEAR_OFFSET, yearBytes, 0, numBytesIntField);
//                        System.arraycopy(data, constants.MONTH_OFFSET, monthBytes, 0, constants.MONTH_SIZE);
//                        System.arraycopy(data, constants.MDATE_OFFSET, mdateBytes, 0, numBytesIntField);
//                        System.arraycopy(data, constants.DAY_OFFSET, dayBytes, 0, constants.DAY_SIZE);
//                        System.arraycopy(data, constants.TIME_OFFSET, timeBytes, 0, numBytesIntField);
//                        System.arraycopy(data, constants.SENSORID_OFFSET, sensorIdBytes, 0, numBytesIntField);
//                        System.arraycopy(data, constants.SENSORNAME_OFFSET, sensorNameBytes, 0, constants.SENSORNAME_SIZE);
//                        System.arraycopy(data, constants.COUNTS_OFFSET, countsBytes, 0, numBytesIntField);
//
//                        // Convert long data into Date object
//                        Date date = new Date(ByteBuffer.wrap(dateBytes).getLong());
//                        String sdtNameString = new String(sdtnameBytes);
//
//                        // Get a string representation of the record for printing to stdout
//                        String record = sdtNameString.trim() + "," + ByteBuffer.wrap(idBytes).getInt()
//                                + "," + dateFormat.format(date) + "," + ByteBuffer.wrap(yearBytes).getInt() +
//                                "," + new String(monthBytes).trim() + "," + ByteBuffer.wrap(mdateBytes).getInt()
//                                + "," + new String(dayBytes).trim() + "," + ByteBuffer.wrap(timeBytes).getInt()
//                                + "," + ByteBuffer.wrap(sensorIdBytes).getInt() + "," +
//                                new String(sensorNameBytes).trim() + "," + ByteBuffer.wrap(countsBytes).getInt();
//                        System.out.println(record);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            finishTime = System.nanoTime();
//        }catch (FileNotFoundException e) {
//            System.err.println("File not found " + e.getMessage());
//        }
//        finally {
//
//            if (inStream != null) {
//                inStream.close();
//            }
//            if (inStream_tree != null){
//                inStream_tree.close();
//            }
//        }
//
//        long timeInMilliseconds = (finishTime - startTime)/constants.MILLISECONDS_PER_SECOND;
//        System.out.println("Time taken: " + timeInMilliseconds + " ms");
//    }
//}