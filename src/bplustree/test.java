package bplustree;

import java.util.Comparator;

public class test {
    public static void main(String[] args) {
        // a simple test to see the structure of tree is right or not
        BPlusTree tree = new BPlusTree(3, 4096);

        tree.insert(new LeafData("101/01/1970 00:00:00 AM",0,0));
        tree.insert(new LeafData("201/01/1970 00:00:00 AM",0,0));
        tree.insert(new LeafData("301/01/1970 00:00:00 AM",0,0));
        tree.insert(new LeafData("401/01/1970 00:00:00 AM",0,0));
        tree.insert(new LeafData("501/01/1970 00:00:00 AM",0,0));
        tree.insert(new LeafData("601/01/1970 00:00:00 AM",0,0));
        tree.insert(new LeafData("701/01/1970 00:00:00 AM",0,0));
        tree.insert(new LeafData("801/01/1970 00:00:00 AM",0,0));
        tree.insert(new LeafData("901/01/1970 00:00:00 AM",0,0));
        tree.insert(new LeafData("4301/01/1970 00:00:00 AM",0,0));
        tree.insert(new LeafData("4501/01/1970 00:00:00 AM",0,0));
        /*
        After insertion,
        this tree should be:
                          5
                   /            \
                  3              7
                 / \           / \
                2  4,43       6   8
               /|  /|\       /|  /|
              1-2-3-4-43,45-5-6-7-8,9
         Note: As these are String, so 4 < 43 < 45 < 5 applies
         */

        tree.print();

        System.out.println("Key test:");
        // Similar key test
        Key a = new Key("501/23/2021 09:00:00 AM"), b = new Key("5001/23/2021 08:00:00 AM");
        // should print -1, as "5" smaller than "50"
        System.out.printf("Compare key %s with key %s", a.sensorId+a.dateTime, b.sensorId+b.dateTime);
        System.out.println(a.compareTo(b));
    }
}
