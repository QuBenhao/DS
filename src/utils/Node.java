package utils;

import bplustree.TNode;
import javafx.util.Pair;

public class Node {
    public String index;
    public TNode child;
    public Pair<Integer, Integer> value;
    public Node next;

    public Node(String index, TNode child){
        this.index = index;
        this.child = child;
        this.value = null;
        this.next = null;
    }

    public Node(String index, Pair<Integer, Integer> value){
        this.index = index;
        this.child = null;
        this.value = value;
        this.next = null;
    }
}
