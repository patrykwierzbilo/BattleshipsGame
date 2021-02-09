package network;

import map.Game;
import map.Generator;
import main.Mode;

import java.io.*;
import java.net.Socket;

public class BattleshipsSession implements Runnable {
    private final Socket socket;
    private final Mode mode;
    private final BufferedWriter out;
    private final BufferedReader in;
    Game game;

    public BattleshipsSession(Socket socket, Mode mode, File mapFile, Generator g) throws IOException {
        this.socket = socket;
        this.mode = mode;
        out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.game = new Game(mapFile, this, g);
    }

    public void run() {
        game.printMap();
        if (mode == Mode.Client && !game.isGameStarted()){
            game.start();
        }
        String message;
        do {
            String read = read();
            if(read.contains("Wygrana;")) {
                game.printEndingMessage(read);
                game.printMap();
                send(game.getLoserMessage());
                break;
            }
            if(read.contains("Przegrana;")) {
                game.printEndingMessage(read);
                game.printMap();
                break;
            }
            message = game.getResult(read);
            if(message.contains("Przegrana")) {
                send(game.getWinnersMessage());
            } else {
                send(message);
            }
        } while (true);
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String read() {
        try {
            String inputLine = in.readLine();
            System.out.println("[" + Thread.currentThread().getName() + "] got " + inputLine + "!");
            return inputLine;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void send(String message){
        try {
            System.out.println("[" + Thread.currentThread().getName() + "] sending " + message + "...");
            out.write(message);
            out.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }

    }
}