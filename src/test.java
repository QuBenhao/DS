import bplustree.BPlusTree;

import java.io.*;

public class test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Input degree for Tree:");
        String line = stdin.readLine();
        BPlusTree myTree = new BPlusTree(Integer.parseInt(line));
        while (true){
            System.out.println("Input index, int, int");
            line = stdin.readLine();
            if (line.compareTo("\n\n")==0 || line.compareTo("\n")==0 || line.compareTo(" ")==0)
                break;
            myTree.insert(line.split("\n")[0], 0,0);
            myTree.bfs_debug();
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
