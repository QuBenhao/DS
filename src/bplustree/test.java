package bplustree;

public class test {
    public static void main(String[] args) {
        BPlusTree tree = new BPlusTree(3);

        tree.insert(new LeafData("101/01/1970 00:00:00 AM",0,0));
        tree.insert(new LeafData("201/01/1970 00:00:00 AM",0,0));
        tree.insert(new LeafData("301/01/1970 00:00:00 AM",0,0));
        tree.insert(new LeafData("401/01/1970 00:00:00 AM",0,0));
        tree.insert(new LeafData("501/01/1970 00:00:00 AM",0,0));
        tree.insert(new LeafData("601/01/1970 00:00:00 AM",0,0));
        tree.insert(new LeafData("701/01/1970 00:00:00 AM",0,0));
        tree.insert(new LeafData("801/01/1970 00:00:00 AM",0,0));
        tree.insert(new LeafData("901/01/1970 00:00:00 AM",0,0));
        tree.print();

    }
}
