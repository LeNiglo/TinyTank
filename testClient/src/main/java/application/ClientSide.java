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
                if (object instanceof Network.MessageConnect) {
                    ClientSide.this.isConnectAnswer((Network.MessageConnect) object);
                } else if (object instanceof Network.MessageDownload) {
                    ClientSide.this.isDownloadAnswer((Network.MessageDownload) object);
                }
            }
        });
        new Thread("connect") {
            public void run() {
                try {
                    client.connect(5000, ClientSide.this.ip, 13333, 13444);
                    Network.MessageConnect request = new Network.MessageConnect("Switi", "123456789");
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

    public void isConnectAnswer(Network.MessageConnect response) {
        Network.MessageNeedMap request = new Network.MessageNeedMap(false);
        client.sendTCP(request);
    }

    public void isDownloadAnswer(final Network.MessageDownload response) {
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
