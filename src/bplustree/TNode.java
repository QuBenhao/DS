package bplustree;

import javafx.util.Pair;
import utils.LinkedList;
import utils.Node;

public class TNode {
    public TNode leftmost_child;
    public LinkedList lists;

    public TNode(){
        this.lists = new LinkedList();
        this.leftmost_child = null;
    }

    public TNode(String index, TNode child){
        this.lists = new LinkedList(index, child);
        this.leftmost_child = null;
    }

    public TNode(String index, Pair<Integer, Integer> value){
        this.lists = new LinkedList(index, value);
        this.leftmost_child = null;
    }

    public TNode insert(String index, Pair<Integer, Integer> value, TNode newchild){
        if(this instanceof LeafNode){
            LinkedList list = this.lists.insert(index, value);
            if(list!=null){
                LeafNode sep_node = new LeafNode();
                sep_node.lists = list;
                sep_node.left = (LeafNode) this;
                ((LeafNode)this).right = sep_node;
            }else
                return null;
        }else{
            Node res = this.lists.find_index(index);
            if(res == null)
                insert(this.leftmost_child, index, value, newchild);
            else{
                insert(res.child, index, value, newchild);
            }
            if(newchild != null){

            }
        }
        return null;
    }
}
