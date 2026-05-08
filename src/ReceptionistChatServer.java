import java.io.*;
import java.net.*;

public class ReceptionistChatServer implements Runnable {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private final int PORT = 9090;

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Receptionist Chat Server started on port " + PORT);

            // Wait for a Guest to connect
            clientSocket = serverSocket.accept();
            System.out.println("Guest connected to chat!");

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            // Listen for messages from Guest
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Guest says: " + inputLine);
                // Here you would use Platform.runLater to update the Receptionist Chat UI
            }
        } catch (IOException e) {
            System.out.println("Chat Server Error: " + e.getMessage());
        }
    }

    // Method to send message back to Guest
    public void sendMessageToGuest(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public void stopServer() throws IOException {
        if (in != null) in.close();
        if (out != null) out.close();
        if (clientSocket != null) clientSocket.close();
        if (serverSocket != null) serverSocket.close();
    }
}
