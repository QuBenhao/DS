package bplustree;

import java.util.ArrayList;

public class BPlusTree {
    public static int degree;
    public TreeNode root;
    public BPlusTree(int degree){
        BPlusTree.degree = degree;
        this.root = new LeafNode();
    }

    public void insert(LeafData data){
        this.root.insert(data);
        if(this.root.parent!=null)
            this.root = this.root.parent;
    }

    public void print(){
        ArrayList<TreeNode> nodes = new ArrayList<>();
        nodes.add(this.root);
        int level = 0;
        while(!nodes.isEmpty()){
            System.out.printf("Current level: %d\n", level);
            ArrayList<TreeNode> next = new ArrayList<TreeNode>();
            for(TreeNode node:nodes){
                if(node == null)
                    continue;
                node.print();
                System.out.println();
                if(node.leftmost_child instanceof LeafNode)
                    continue;
                next.add(node.leftmost_child);
                next.addAll(node.children);
            }
            nodes = next;
            level++;
        }

        System.out.println("LeafNodes:");
        TreeNode node = this.root;
        while(node.leftmost_child!=null)
            node = node.leftmost_child;
        int count = 0;
        while (node !=null){
            node.print();
            count += node.capacity;
            node = ((LeafNode)node).right;
        }
        System.out.printf("LeafNode data: %d\n", count);
    }
}
