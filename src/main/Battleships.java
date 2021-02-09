package main;

import map.Generator;
import network.BattleshipsClient;
import network.BattleshipsServer;

import java.io.File;
import java.io.IOException;
import java.net.*;

public class Battleships {
    private Mode mode;
    private int port;
    private File map;
    //private final static String mapFileName = "main/map.txt";
    private InetAddress address;

    public Battleships(String[] args) throws UnknownHostException {
        setParameters(args);
        address = InetAddress.getLocalHost();
    }

    private void setParameters(String[] args) {
        for(int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-mode":
                    this.mode = Mode.valueOf(args[i + 1]);
                    break;
                case "-port":
                    this.port = Integer.parseInt(args[i + 1]);
                    break;
                case "-map":
                    String path = args[i + 1];// + mapFileName;
                    this.map = new File(path);
                    break;
                default:
                    System.err.println("Wrong parameters");
                    System.exit(1);
            }
            i++;
        }
    }

    void startGame(Generator generator) throws IOException {
        if(mode == Mode.Server) {
            BattleshipsServer battleshipsServer = new BattleshipsServer(findAddress(), port, map, generator);
            new Thread(battleshipsServer, "BattleshipsServer").start();
        } else {
            BattleshipsClient c = new BattleshipsClient(findAddress().getHostAddress(), port, map, generator);
        }
    }

    public static void main(String[] args) throws IOException {
        Battleships battleships = new Battleships(args);
        Generator g = new Generator();
        g.generateMapToFile(battleships.map);
        battleships.startGame(g);
    }

    static InetAddress findAddress() throws SocketException, UnknownHostException {
        var wlan1 = NetworkInterface.getByName("wlan1");
        return wlan1.inetAddresses()
                .filter(a -> a instanceof Inet4Address)
                .findFirst()
                .orElse(InetAddress.getLocalHost());
    }
}
//java -jar out/artifacts/BattleshipsGame_jar/BattleshipsGame.jar -mode Server -port 666 -map "map.txt"
//java -jar out/artifacts/BattleshipsGame_jar/BattleshipsGame.jar -mode Client -port 666 -map "mapc.txt"
