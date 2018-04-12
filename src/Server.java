import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private int port;
    private static ArrayList<Socket> socket = new ArrayList<>();
    ChatSaver saver;

    public Server(int port) {
        this.port = port;
        saver = new ChatSaver("C:\\Users\\Daniel\\IdeaProjects\\DeathChat\\chat.txt");
    }

    public void start() {
        ExecutorService pool = Executors.newFixedThreadPool(100);
        Socket s = null;

        try (ServerSocket server = new ServerSocket(port)) {
            while (true) {
                s = server.accept();
                socket.add(s);
                pool.submit(new Servermanager(s));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new Server(2222).start();
    }

    private class Servermanager implements Runnable {

        Socket socket;

        Servermanager(Socket server) {
            this.socket = server;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    InputStream is = socket.getInputStream();

                    Scanner sc = new Scanner(is);

                    while (sc.hasNextLine()) {
                        String msg = sc.nextLine();
                        for (int i = 0; i < Server.socket.size(); i++) {
                            OutputStream os = Server.socket.get(i).getOutputStream();
                            PrintWriter pw = new PrintWriter(os);
                            pw.write(msg + "\n");
                            pw.flush();
                        }

                        System.out.println(msg);
                        saver.write(msg);
                        break;
                    }

                    Thread.sleep(100);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
