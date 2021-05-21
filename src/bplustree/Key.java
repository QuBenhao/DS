package bplustree;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class Key implements Comparable<Key> {
    public String sensorId;
    public String dateTime;
    public long timestamp;

    public Key(String index){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        Date date;
        try {
            sensorId = index.substring(0,1);
            this.dateTime = index.substring(1);
            date = dateFormat.parse(index.substring(1));
        }
        catch (ParseException e) {
            try {
                this.sensorId = index.substring(0,2);
                this.dateTime = index.substring(2);
                date = dateFormat.parse(index.substring(2));
            }catch (ParseException ex){
                // should not happen
                ex.printStackTrace();
                date = new Date();
            }
        }
        this.timestamp = date.getTime();
    }

    @Override
    public int compareTo(Key o) {
        return Comparator.comparing((Key k) -> k.sensorId).thenComparing((Key k) -> k.timestamp,Comparator.nullsLast(Comparator.naturalOrder())).compare(this,o);
    }
}
