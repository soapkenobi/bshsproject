import java.io.*;

class testsandbox {
    public static void main(String[] args) throws IOException {
        File fl = new File("quiz.soap");
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(fl));
        dos.writeUTF("Hello world");
        dos.close();
        DataInputStream dis = new DataInputStream(new FileInputStream(fl));
        String str = dis.readUTF();
        dis.close();
        System.out.println(str);
    }
}