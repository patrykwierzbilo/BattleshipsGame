package network;

import map.Generator;
import main.Mode;

import java.io.File;
import java.io.IOException;
import java.net.*;

public class BattleshipsServer implements Runnable {
    private final ServerSocket serverSocket;
    private File map;
    Generator g;

    public BattleshipsServer(InetAddress address, int port, File map, Generator g) throws IOException {
        serverSocket = new ServerSocket(port, 10000, address);
        this.map = map;
        System.out.println("Running PongServer at address: " + address + ", port: " + port);
        this.g = g;
    }

    @Override
    public void run() {
        int sessionId = 0;
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Got request from " + socket.getRemoteSocketAddress() + ", starting session " + sessionId);
                BattleshipsSession session = new BattleshipsSession(socket, Mode.Server, map, g);
                new Thread(session, "ServerSession-" + sessionId).start();
                sessionId++;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}