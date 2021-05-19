import bplustree.BPlusTree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class test {
    public static void main(String[] args) throws IOException {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Input degree for Tree:");
        String line = stdin.readLine();
        int temp = 0;
        BPlusTree myTree = new BPlusTree(Integer.parseInt(line));
        while (true){
            System.out.println("Input index, int, int");
            line = stdin.readLine();
            if (line.compareTo("\n\n")==0 || line.compareTo("\n")==0 || line.compareTo(" ")==0)
                break;
            myTree.insert(line.split("\n")[0], temp,temp);
            temp++;
            myTree.bfs_debug();
        }
    }
}
