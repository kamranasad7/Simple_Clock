import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ClockServer {
    DatagramSocket socket;
    public ClockServer(int PORT) throws SocketException {
        socket = new DatagramSocket(PORT);
    }
    public static void main(String[] args) {
        try {
            int PORT=8000;
            ClockServer server = new ClockServer(PORT);
            System.out.println("Server created on port " +PORT);
            server.service();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    private void service() throws IOException {
        System.out.println("Server Listening...");
        while (true) {
            byte[] reqBody = new byte[100];

            DatagramPacket req = new DatagramPacket(reqBody, reqBody.length);
            socket.receive(req);    //block exec until a request is received

            String zoneId = new String(reqBody, 0, req.getLength());
            ZonedDateTime time = getTimeOfZoneID(getZoneId(zoneId));

            InetAddress clientAddress = req.getAddress();
            int clientPort = req.getPort();

            String parsedTime = time.toLocalDateTime().toString();
            DatagramPacket response = new DatagramPacket(parsedTime.getBytes(), parsedTime.length(), clientAddress, clientPort);
            socket.send(response);
        }
    }

    ZonedDateTime getTimeOfZoneID(ZoneId id){
        try {
            LocalDateTime currentTime = LocalDateTime.now();
            return currentTime.atZone(id);
        }
        catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    ZoneId getZoneId(String Area){
        return ZoneId.of(Area);
    }
}
