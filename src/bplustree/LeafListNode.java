package bplustree;

import javafx.util.Pair;

public class LeafListNode extends ListNode {
    public Pair<Integer, Integer> value;

    public LeafListNode(String index, int pageIndex, int slots) {
        super(index);
        value = new Pair<>(pageIndex, slots);
    }
}
