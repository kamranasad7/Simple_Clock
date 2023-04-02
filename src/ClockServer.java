import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Vector;
import java.util.TimerTask;
import javax.swing.Timer;

public class ClockServer {
    DatagramSocket socket;
    Vector<Timer> timers;
    public ClockServer(int PORT) throws SocketException {
        socket = new DatagramSocket(PORT);
    }
    public static void main(String[] args) {
        try {
            int PORT=8000;
            ClockServer server = new ClockServer(PORT);
            System.out.println("Server created on port " +PORT);
            System.out.println(server.getTimeofZoneID(server.getZoneId("Asia/Kolkata")));
            //server.service();
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

    ZonedDateTime getTimeofZoneID(ZoneId id){
        try {
            LocalDateTime currentTime = LocalDateTime.now();
            ZonedDateTime zonedDateTime = currentTime.atZone(id);
            return zonedDateTime;
        }
        catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    ZoneId getZoneId(String Area){
        return ZoneId.of(Area);
    }

    void StartNewTImer(int countdown, ActionListener listener){

        Timer t = new Timer(countdown, listener);

        t.start();
        timers.add(t);
    }

}
