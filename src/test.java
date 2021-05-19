import bplustree.BPlusTree;
import bplustree.TNode;
import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;

public class test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Input degree for Tree:");
        String line = stdin.readLine();
        BPlusTree myTree = new BPlusTree(Integer.parseInt(line), 4096);
        while (true){
            System.out.println("Input index, int, int");
            line = stdin.readLine();
            if (line.compareTo("\n\n")==0 || line.compareTo("\n")==0 || line.compareTo(" ")==0)
                break;
            String[] arr = line.split("\n")[0].split(" ");
            myTree.insert(arr[0], 0,0);
            myTree.bfs_debug();
        }

        System.out.println("Query:");
        while (true){
            System.out.println("Input index, index");
            line = stdin.readLine();
            if (line.compareTo("\n\n")==0 || line.compareTo("\n")==0 || line.compareTo(" ")==0)
                break;
            String[] arr = line.split("\n")[0].split(" ");
            ArrayList<Pair<Integer,Integer>> result = myTree.query(arr[0],arr[1]);
            result.forEach(p->{
                System.out.println(p.toString());
            });
        }

//        FileOutputStream fileOut = new FileOutputStream("tree.ser");
//        ObjectOutputStream out = new ObjectOutputStream(fileOut);
//        out.writeObject(myTree);
//        out.close();
//        fileOut.close();

//        FileInputStream fileIn = new FileInputStream("tree.ser");
//        ObjectInputStream in = new ObjectInputStream(fileIn);
//        BPlusTree myTree = (BPlusTree) in.readObject();
//        in.close();
//        fileIn.close();
//        myTree.bfs_debug();
    }
}
