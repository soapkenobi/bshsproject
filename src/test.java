import java.io.*;
import java.util.Scanner;
public class test {
    public static void main(String[] args) throws IOException {
        File fl = new File("userinfo/income.dat");
        DataInputStream dis = new DataInputStream(new FileInputStream(fl));
        System.out.print(dis.readUTF()+dis.readDouble());
    }
}