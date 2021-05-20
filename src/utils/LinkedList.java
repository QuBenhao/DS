package utils;

import bplustree.BPlusTree;
import bplustree.TNode;
import javafx.util.Pair;

public class LinkedList {
    public Node root, last;
    public int capacity;

    public LinkedList(){
        this.capacity = 0;
        this.root = this.last = null;
    }

    public LinkedList(String index, TNode child){
        this.capacity = 0;
        this.root = this.last = new Node(index, child);
        this.capacity++;
    }

    public LinkedList(String index, Pair<Integer, Integer> value){
        this.capacity = 0;
        this.root = this.last = new Node(index, value);
        this.capacity++;
    }

    public LinkedList insert(String index, TNode child){
        Node newNode = new Node(index, child);
        if(this.root==null){
            this.root = this.last = newNode;
        }else {
            Node i = this.find_index(index);
            if(i == null){
                newNode.next = this.root;
            }else {
                newNode.next = i.next;
                i.next = newNode;
            }
        }
        this.capacity++;
        if(this.capacity== BPlusTree.degree){
            return this.separate();
        }
        return null;
    }

    public LinkedList insert(String index, Pair<Integer, Integer> value){
        Node newNode = new Node(index, value);
        if(this.root==null){
            this.root = this.last = newNode;
        }else {
            Node i = this.find_index(index);
            if(i == null){
                newNode.next = this.root;
            }else {
                newNode.next = i.next;
                i.next = newNode;
            }
        }
        this.capacity++;
        if(this.capacity== BPlusTree.degree){
            return this.separate();
        }
        return null;
    }

    public LinkedList separate(){
        LinkedList sep_list = new LinkedList();
        Node curr = this.root;
        // 0 1 2 3 4 -> 0 1, 2 3 4
        for(int i=0;i<(this.capacity-1)/2-1;i++){
            curr = curr.next;
        }
        sep_list.root = curr.next;
        curr.next = null;
        sep_list.last = this.last;
        sep_list.capacity = (this.capacity+1)/2;
        this.last = curr;
        this.capacity = (this.capacity-1)/2;
        return sep_list;
    }

    public Node find_index(String index){
        Node res = null;
        if(index.compareTo(this.last.index) >=0)
            res = this.last;
        else if(index.compareTo(this.root.index) >= 0){
            res = this.root;
            while (res.next!=null){
                if(index.compareTo(res.next.index) < 0){
                    return res;
                }
                res = res.next;
            }
        }
        // return null means the leftmost_child
        return res;
    }

    public Node pop_first(){
        Node temp = this.root;
        if(this.last == this.root)
            this.last = temp.next;
        this.root = temp.next;
        temp.next = null;
        this.capacity--;
        return temp;
    }

    public void print(){
        Node node = root;
        while (node!=null){
            System.out.printf("%s ", node.index);
            node = node.next;
        }
        System.out.println();
    }
}
