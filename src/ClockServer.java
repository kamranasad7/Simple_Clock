import java.io.*;
import java.net.*;
import java.time.LocalDateTime;

public class ClockServer {
    DatagramSocket socket;

    public ClockServer(int PORT) throws SocketException {
        socket = new DatagramSocket(PORT);
    }
    public static void main(String[] args) {
        try {
            int PORT=8000;
            ClockServer server = new ClockServer(PORT);
            System.out.println("Server created on port" +PORT);
            server.service();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    private void service() throws IOException {
        System.out.println("Server Listening...");
        while (true) {
            byte[] reqBody = new byte[256];

            DatagramPacket request = new DatagramPacket(reqBody, reqBody.length);
            socket.receive(request);    //block exec until a request is received

            InetAddress clientAddress = request.getAddress();
            int clientPort = request.getPort();

            String time = LocalDateTime.now().toString();

            DatagramPacket response = new DatagramPacket(time.getBytes(), time.length(), clientAddress, clientPort);
            socket.send(response);
        }
    }
}
