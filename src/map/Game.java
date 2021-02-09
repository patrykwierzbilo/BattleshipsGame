package map;

import network.BattleshipsSession;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Game {
    boolean started;
    StringBuilder map;
    BattleshipsSession session;
    static final int size = 10;
    Map<Integer, List<Point>> shipsMap;
    List<List<Point>> ships;
    List<Point> shots;

    public Game(File mapFile, BattleshipsSession session, Generator g) {
        this.map = new StringBuilder((new Generator()).readMapFromFile(mapFile));
        this.started = false;
        this.session = session;
        this.shipsMap = g.getShipList();
        this.ships = g.getShipPoints();
        this.shots = new ArrayList<Point>();
    }

    Point getRandomPoint() {
        Point result;
        do {
            result = Point.getRandomly();
        } while (shots.contains(result));
        shots.add(result);
        return result;
    }

    char getChar(Point point) {
        return map.charAt(point.row*size + point.col);
    }

    void setChar(Point point, char sign) {
        map.setCharAt(point.row * size + point.col, sign);
    }

    public String getResult(String message) {
        Point shot = new Point(getCoordinates(message));
        if(getChar(shot) == '.') {
            setChar(shot, '~');
            return "pudlo;" + getRandomPoint() + '\n';
        } else if(getChar(shot) == '#') {
            setChar(shot, '@');
            return updateShipsMap(shot);
        } else if(getChar(shot) == '~') {
            return "pudlo;" + getRandomPoint() + '\n';
        } else if(getChar(shot) == '@') {
            if(checkIfShipsIsSunk(shot)) {
                return "trafiony zatopiony;" + getRandomPoint() + '\n';
            }
            return "trafiony;" + getRandomPoint() + '\n';
        } else {
            return "cannot match";
        }
    }

    boolean checkIfShipsIsSunk(Point shot) {
        for (var it : shipsMap.entrySet()) {
            if(it.getValue().contains(shot)) {
                return false;
            }
        }
        return true;
    }

    private String updateShipsMap(Point shot) {
        for (Integer it : shipsMap.keySet()) {
            shipsMap.get(it).removeIf(x -> x.equals(shot));
            if(shipsMap.get(it).isEmpty()) {
                shipsMap.remove(it);
                if (shipsMap.isEmpty()) {
                    return "Przegrana" + mapToPrint();
                }
                return "trafiony zatopiony;"+ getRandomPoint() +"\n";
            }
        }
        if (shipsMap.isEmpty()) {
            return "Przegrana" + mapToPrint();
        } else {
            return "trafiony;"+ getRandomPoint() +"\n";
        }
    }

    Point getDirectionPoint(int direction, Point point) {
        switch (direction) {
            case 0:
                return new Point(point.row + 1, point.col);
            case 1:
                return new Point(point.row, point.col + 1);
            case 2:
                return new Point(point.row - 1, point.col);
            default:
                return new Point(point.row, point.col - 1);
        }
    }

    public void printEndingMessage(String message) {
        System.out.println(message.replaceAll(";", "\n"));
    }

    public String getLoserMessage() {
        StringBuilder loserMap = new StringBuilder(mapToPrint());
        for(int i = 0; i < 100; i++) {
            if( (loserMap.charAt(i) == '.' || loserMap.charAt(i) == '#')) {
                loserMap.setCharAt(i, '?');
            }
        }
        return "Przegrana;" + loserMap.toString() + "\n";
    }
/*
    private String checkCollision(String checkedMap) {
        for(List<Point> shipByPoints: ships) {
            boolean sunk = true;
            for(Point point: shipByPoints) {
                if(getChar(point) != '@') {
                    sunk = false;
                }
            }
            if(sunk) {
                for(Point point: shipByPoints) {

                }
            }
        }
        return false;
    }*/

    public String getWinnersMessage() {
        return "Wygrana;" + mapToPrint() + "\n";
    }

    public void start() {
        session.send("start;" + getRandomPoint() +"\n");
    }

    public String getCommand(String message) {
        if(message.indexOf(';') < 0) {
            return message;
        }
        return message.substring(0, message.indexOf(';'));
    }

    String getCoordinates(String message) {
        if(message.indexOf(';') < 0) {
            return message;
        }
        return message.substring(message.indexOf(';') + 1);
    }

    public boolean isGameStarted() {
        return started;
    }

    public void printMap() {
        for(int i = 0; i < size; i++) {
            System.out.println(map.substring(i * size, i *size + size));
        }
    }

    public String mapToPrint() {
        StringBuilder printableMap = new StringBuilder("");
        for(int i = 0; i < size; i++) {
            printableMap.append(map.substring(i * size, i *size + size)).append(";");
        }
        return printableMap.toString();
    }
}

