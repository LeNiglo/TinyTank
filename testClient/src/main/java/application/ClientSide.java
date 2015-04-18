package application;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import communication.ReceiveFile;
import utils.MD5;

import java.util.List;

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
            public void connected(Connection connection) {
                Network.MessageConnect request = new Network.MessageConnect("Switi", "123456789");
                client.sendTCP(request);
            }

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
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }.start();
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (Exception ignored) {}
        }
    }

    public void isConnectAnswer(Network.MessageConnect response) {
        String encodedJson = response.getEncodedJson();
        String encodedMap = response.getEncodedMap();
        String mapName = response.getMapName();
        String name = response.getMapFileName();
        List<String> players = response.getUsers();
        try {
            if (MD5.getMD5Checksum(System.getProperty("user.dir") + "/maps/" + name + ".jpg").equals(encodedMap) &&
                    MD5.getMD5Checksum(System.getProperty("user.dir") + "/maps/" + name + ".json").equals(encodedJson))  {
                System.out.println("Pas besoin de la map");
                // TODO: TankChoice
                return;
            }
        } catch (Exception e) {
            Log.error("MD5: " + e.getMessage());
        }
        System.out.println("Besoin de la map");
        Network.MessageNeedMap request = new Network.MessageNeedMap(true);
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
