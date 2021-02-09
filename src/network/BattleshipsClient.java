package network;

import map.Generator;
import main.Mode;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class BattleshipsClient {

    public BattleshipsClient(String host, int port, File map, Generator g) throws IOException {
        Socket s = new Socket(host, port);
        BattleshipsSession session = new BattleshipsSession(s, Mode.Client, map, g);
        new Thread(session, "[Client]").start();
    }

}