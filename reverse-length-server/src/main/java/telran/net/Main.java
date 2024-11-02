package telran.net;

import java.net.*;
import java.io.*;
import org.json.JSONObject;

public class Main {
    private static final int PORT = 4000;

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(PORT);
        while (true) {
            Socket socket = serverSocket.accept();
            runSession(socket);
        }
    }

    private static void runSession(Socket socket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintStream writer = new PrintStream(socket.getOutputStream())) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                JSONObject json = new JSONObject(line);
                String string = json.getString("string");
                String type = json.getString("type");
                String answer = getAnswerByType(string, type);
                writer.printf("Server on %s, port: %d sends back answer %s by type %s \n",
                        socket.getLocalAddress().getHostAddress(),
                        socket.getLocalPort(),
                        answer,
                        type);
            }
        } catch (Exception e) {
            System.out.println("Server is not working");
        }
    }

    private static String getAnswerByType(String string, String type) {
        return switch (type) {
            case "length" -> String.valueOf(string.length());
            case "reverse" -> new StringBuilder(string).reverse().toString();
            default -> "No answer";
        };
    }
}