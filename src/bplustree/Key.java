package bplustree;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Key {
    public String sensorId;
    public String dateTime;
    public long timestamp;

    public Key(String index) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        Date date;
        try {
            sensorId = index.substring(0,1);
            this.dateTime = index.substring(1);
            date = dateFormat.parse(index.substring(1));
        }
        catch (ParseException e) {
            this.sensorId = index.substring(0,2);
            this.dateTime = index.substring(2);
            date = dateFormat.parse(index.substring(2));
        }
        this.timestamp = date.getTime();

    }
}
