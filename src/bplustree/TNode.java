package bplustree;

import javafx.util.Pair;
import utils.LinkedList;
import utils.Node;

public class TNode {
    public TNode leftmost_child, parent;
    public LinkedList lists;

    public TNode(){
        this.lists = new LinkedList();
        this.leftmost_child = this.parent = null;
    }

    public TNode(String index, TNode child){
        this.lists = new LinkedList(index, child);
        this.leftmost_child = this.parent = null;
    }

    public TNode(String index, Pair<Integer, Integer> value){
        this.lists = new LinkedList(index, value);
        this.leftmost_child = this.parent = null;
    }

    public void insert(String index, TNode child){
        LinkedList list = this.lists.insert(index, child);
        if(list!=null){
            TNode sep_node = new TNode();
            sep_node.lists = list;
            Node n = sep_node.lists.pop_first();
            sep_node.leftmost_child = n.child;
            n.child.parent = sep_node;
            // child belongs to the right
            if(this.lists.find_index(index) == this.lists.last && this.lists.last.child != child){
                child.parent = sep_node;
            }else
                child.parent = this;

            if(this.parent == null){
                this.parent = new TNode(n.index, sep_node);
                this.parent.leftmost_child = this;
                sep_node.parent = this.parent;
            }else
                this.parent.insert(n.index, sep_node);
        }
    }

    public TNode insert(String index, Pair<Integer, Integer> value){
        if(this instanceof LeafNode){
            LinkedList list = this.lists.insert(index, value);
            if(list!=null){
                LeafNode sep_node = new LeafNode();
                sep_node.lists = list;
                sep_node.left = (LeafNode) this;
                ((LeafNode)this).right = sep_node;
                return sep_node;
            }
        }else{
            Node res = this.lists.find_index(index);
            TNode node;
            if(res == null)
                node = this.leftmost_child.insert(index, value);
            else{
                node = res.child.insert(index, value);
            }
            if(node!=null)
                this.insert(node.lists.root.index, node);
        }
        return null;
    }

    public void print() {
        this.lists.print();
    }
}
