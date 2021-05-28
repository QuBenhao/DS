package bplustree;

import constant.constants;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;

public class Key implements Comparable<Key> {
    public String sensorId;
    public String dateTime;
    public long timestamp;

    public Key(String index){
        Date date;
        // some keys like "501/23/2021 09:00:00 AM " will have a space in the end (dbload, tree output and input)
        // some keys like "501/23/2021 09:00:00 AM" will be length smaller than STD_NAME_SIZE (query input)
        if(index.length() < constants.STD_NAME_SIZE || index.charAt(index.length()-1) == ' '){
            try {
                sensorId = index.substring(0,1);
                dateTime = index.substring(1);
                date = constants.dateFormat.parse(index.substring(1));
            }
            catch (ParseException e) {
                e.printStackTrace();
                date = new Date();
            }
        }else {
            try {
                sensorId = index.substring(0,2);
                dateTime = index.substring(2);
                date = constants.dateFormat.parse(index.substring(2));
            }catch (ParseException e){
                e.printStackTrace();
                date = new Date();
            }
        }
        timestamp = date.getTime();
    }

    // compare key based on sensorId, then timestamp
    // Note: if we want to
    // 1. compare sensorId based on converting to int, or
    // 2. compare key based on timestamp first,
    // simply modify the method here and rerun treeload and treequery
    @Override
    public int compareTo(Key o) {
        return Comparator.comparing((Key k) -> k.sensorId).thenComparing((Key k) -> k.timestamp,
                Comparator.nullsLast(Comparator.naturalOrder())).compare(this,o);
    }
}
