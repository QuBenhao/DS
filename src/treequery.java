import java.io.IOException;
import java.io.RandomAccessFile;

public class treequery {

    // Reads in a binary file of the argument-specified pagesize, prints out matching records
    public static void main(String[] args) throws IOException {

        // check for correct number of arguments
        if (args.length != constants.TREEQUERY_ARG_COUNT) {
            System.out.println("Error: Incorrect number of arguments were input");
            return;
        }

        String text = args[0];
        // match text format, as this won't change, create only once
        String sFormat = String.format("(.*)%s(.*)", text);

        int pageSize = Integer.parseInt(args[constants.TREEQUERY_PAGE_SIZE_ARG]);

        String datafile = "heap." + pageSize;
        long startTime = 0;
        long finishTime = 0;

        /*
        TODO: example read byte file with byte position
        RandomAccessFile raf = new RandomAccessFile(datafile, "r");
        raf.seek(byte position);
        byte[] temp = new byte[byte to read];
        raf.read(temp);
        convert temp to output
        */
    }
}