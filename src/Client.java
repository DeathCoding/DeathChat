import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {

    private int port;
    private String address;
    private String name;
    ChatSaver saver;

    public Client(String address, int port, String name) {
        this.port = port;
        this.address = address;
        this.name = name;
        saver = new ChatSaver("C:\\Users\\Daniel\\IdeaProjects\\DeathChat\\chat.txt");
    }

    public void start() {
        Socket s = new Socket();
        try {
            s.setSoTimeout(15000);
            SocketAddress address = new InetSocketAddress(this.address, port);
            s.connect(address);

            List<String> chat = saver.read();

            for(int i = 0; i < chat.size(); i++) {
                System.out.println(chat.get(i));
            }

            Thread receiver = new Thread(() -> {
                    while (!s.isClosed()) {
                        try {
                            InputStream is = s.getInputStream();

                            Scanner sc = new Scanner(is);

                            String line;

                            while (sc.hasNextLine()) {
                                line = sc.nextLine();
                                System.out.println(line);
                                break;
                            }

                            Thread.sleep(100);
                        } catch(IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            });
            receiver.start();

            Thread sender = new Thread(() -> {
                while (!s.isClosed()) {
                    try {
                        OutputStream os = s.getOutputStream();

                        PrintWriter pw = new PrintWriter(os);
                        Scanner cs = new Scanner(System.in);

                        String message = cs.nextLine();
                        pw.println(name + " | " + message);
                        pw.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            sender.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        String username;
        System.out.println("Please tell me your username");
        username = sc.nextLine();

        Client c = new Client("localhost", 2222, username);
        c.start();
    }

}
