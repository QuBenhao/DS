package bplustree;

import constant.constants;

import java.io.DataOutputStream;
import java.io.IOException;

public class LeafData implements Comparable<LeafData>{
    public Key key;
    public int pageIndex;
    public int slots;

    public LeafData(String index, int pageIndex, int slots){
        key = new Key(index);
        this.pageIndex = pageIndex;
        this.slots = slots;
    }

    public void write(DataOutputStream dataOutput) throws IOException {
        /*
        key: fixed length String 24 bytes
        pageIndex: int 4 bytes
        slots: int 4 bytes
        Total: 32 bytes
         */
        dataOutput.writeBytes(getStringOfLength(key.sensorId+key.dateTime));
        dataOutput.writeInt(pageIndex);
        dataOutput.writeInt(slots);
    }

    // Returns a whitespace padded string of the same length as parameter int length
    private String getStringOfLength(String original) {

        int lengthDiff = constants.STD_NAME_SIZE - original.length();

        // Check difference in string lengths
        if (lengthDiff == 0) {
            return original;
        }
        else if (lengthDiff > 0) {
            // if original string is too short, pad end with whitespace
            StringBuilder string = new StringBuilder(original);
            for (int i = 0; i < lengthDiff; i++) {
                string.append(" ");
            }
            return string.toString();
        }
        else {
            // if original string is too long, shorten to required length
            return original.substring(0, constants.STD_NAME_SIZE);
        }
    }

    @Override
    public int compareTo(LeafData o) {
        return key.compareTo(o.key);
    }
}
