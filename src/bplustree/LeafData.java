package bplustree;

import constant.constants;

import java.io.DataOutputStream;
import java.io.IOException;
import java.text.ParseException;

public class LeafData{
    public Key key;
    public int pageIndex;
    public int slots;

    public LeafData(String index, int pageIndex, int slots) throws ParseException {
        this.key = new Key(index);
        this.pageIndex = pageIndex;
        this.slots = slots;
    }

    public void write(DataOutputStream dataOutput) throws IOException {
        dataOutput.writeBytes(this.getStringOfLength(this.key.sensorId+this.key.dateTime));
        dataOutput.writeInt(this.pageIndex);
        dataOutput.writeInt(this.slots);
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
}
