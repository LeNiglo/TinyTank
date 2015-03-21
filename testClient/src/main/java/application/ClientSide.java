package application;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import communication.ReceiveFile;

/**
 * Created by Styve on 17/03/2015.
 */
public class ClientSide {
    private final Client client = new Client();
    private String ip;

    public ClientSide(String ip) {
        this.ip = ip;
        this.client.start();
        Network.register(client);
        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof Network.MessageConnectAnswer) {
                    ClientSide.this.isConnectAnswer((Network.MessageConnectAnswer) object);
                } else if (object instanceof Network.MessageDownloadAnswer) {
                    ClientSide.this.isDownloadAnswer((Network.MessageDownloadAnswer) object);
                }
            }
        });
        new Thread("connect") {
            public void run() {
                try {
                    client.connect(5000, ClientSide.this.ip, 13333, 13444);
                    Network.MessageConnect request = new Network.MessageConnect("Switi", 0, "monPass", true);
                    client.sendTCP(request);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(1000);
                    }
                } catch (Exception ignored) {}
            }
        }.start();
    }

    public void isConnectAnswer(Network.MessageConnectAnswer response) {
        System.out.println("The server said that current map is: " + response.getMapName());
        Network.MessageHasMap request = new Network.MessageHasMap(false);
        client.sendTCP(request);
        System.out.println("We are saying that we don't have the map.");
        // else
        // Network.MessageHasMap request = new Network.MessageHasMap(true);
    }

    public void isDownloadAnswer(final Network.MessageDownloadAnswer response) {
        new Thread("Download") {
            public void run() {
                try {
                    new ReceiveFile(ClientSide.this.ip, response.getFileName(), response.getFileSize() + 1024);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }.start();
    }
}
