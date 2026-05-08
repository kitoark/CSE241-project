import java.io.*;
import java.net.*;
import javafx.application.Platform;

public class GuestChatClient implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String serverAddress = "localhost"; // Assuming local network
    private final int PORT = 9090;

    @Override
    public void run() {
        try {
            socket = new Socket(serverAddress, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String incomingMessage;
            // Listen for messages from Receptionist
            while ((incomingMessage = in.readLine()) != null) {
                final String msg = incomingMessage;
                // Update Guest UI
                Platform.runLater(() -> {
                    System.out.println("Receptionist: " + msg);

                });
            }
        } catch (IOException e) {
            System.out.println("Chat Client Error: " + e.getMessage());
        }
    }

    // Call this method when the guest clicks "Send"
    public void sendMessageToReceptionist(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public void disconnect() throws IOException {
        if (in != null) in.close();
        if (out != null) out.close();
        if (socket != null) socket.close();
    }
}