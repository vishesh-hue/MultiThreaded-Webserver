import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final ExecutorService threadPool;

    public Server(int poolSize) {
        this.threadPool = Executors.newFixedThreadPool(poolSize);
    }

    public void handleClient(Socket clientSocket) {
        try (PrintWriter toSocket = new PrintWriter(clientSocket.getOutputStream(), true)) {
            toSocket.println("Hello from server " + clientSocket.getInetAddress());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 8010;
        int poolSize = 50; // Adjust the pool size as needed
        Server server = new Server(poolSize);

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(70000);
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                // Use the thread pool to handle the client
                server.threadPool.execute(() -> server.handleClient(clientSocket));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // Shutdown the thread pool when the server exits
            server.threadPool.shutdown();
        }
    }
}


//single threaded
//  public class Server{
//      public void run() throws IOException {
//        int port = 8010;
//        ServerSocket socket = new ServerSocket(port);
//        socket.setSoTimeout(10000);
//        while(true){
//            try{
//                System.out.println("server is lsning onport " + port);
//                Socket acceptedConnection = socket.accept();
//                System.out.println("conncetion succesful " + acceptedConnection.getRemoteSocketAddress());
//                PrintWriter toClient = new PrintWriter(acceptedConnection.getOutputStream());
//                BufferedReader fromClient = new BufferedReader( new InputStreamReader(acceptedConnection.getInputStream()));
//                toClient.println("hello from the server");
//                toClient.close();
//                fromClient.close();
//                acceptedConnection.close();
//            }catch(IOException ex){
//                ex.printStackTrace();
//            }
//        }
//    }
//    public static void main(String[] args) {
//        Server server = new Server();
//        try{
//            server.run();
//        }
//        catch(Exception ex){
//            ex.printStackTrace();
//        }
//    }
//
// }

