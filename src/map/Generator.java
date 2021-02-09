package map;

import java.io.*;
import java.util.*;

public class Generator {
    StringBuilder map;
    int size = 12;
    int mapSize = size - 2;
    Map<Integer, List<Point>> shipList;
    List<List<Point>> ships;

    public Generator() {
        this.map = new StringBuilder("-------------..........--..........-" +
                "-..........--..........--..........-" +
                "-..........--..........--..........-" +
                "-..........--..........-------------");
    }

    void setPoint(Point point, char sign) {
        map.setCharAt((point.row + 1) * size + point.col + 1, sign);
    }

    char getPoint(Point point) {
        return map.charAt((point.row + 1) * size + point.col + 1);
    }

    char getPoint(int x, int y) {
        return map.charAt((x + 1) * size + y + 1);
    }

    List<Integer> getShipSizes() {
        List<Integer> ships = new ArrayList<>(List.of(4, 3, 3, 2, 2, 2, 1, 1, 1, 1));
        Collections.shuffle(ships);
        return ships;
    }

    Point getRandomPoint(int range) {
        return new Point(new Random().nextInt(range), new Random().nextInt(range));
    }

    boolean isNeighborhoodCorrect(Point point) {
        if (point.row < 0 || point.col < 0 ||
                point.row > (mapSize - 1) || point.col > (mapSize - 1) ||
                getPoint(point) == '-' || getPoint(point) == '#' ||
                getPoint(point.row - 1, point.col - 1) == '#' ||
                getPoint(point.row - 1, point.col) == '#' ||
                getPoint(point.row, point.col - 1) == '#' ||
                getPoint(point.row + 1, point.col - 1) == '#' ||
                getPoint(point.row - 1, point.col + 1) == '#' ||
                getPoint(point.row + 1, point.col + 1) == '#' ||
                getPoint(point.row + 1, point.col) == '#' ||
                getPoint(point.row, point.col + 1) == '#') {
            return false;
        } else {
            return true;
        }
    }

    Point getDirectionPoint(Point point) {
        int direction = new Random().nextInt(4);
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

    public static void main(String[] args)  {
        StringBuilder s = new StringBuilder("");
        s.append("a").append("b").append('c');
        System.out.println(s);
    }

    boolean genrateShip(Integer shipSize) {
        List<Point> shipPoints = new ArrayList<>();
        Point point = getRandomPoint(mapSize);
        if (isNeighborhoodCorrect(point)) {
            shipPoints.add(point);
        } else {
            return false;
        }
        Point directionPoint = point;
        while (shipPoints.size() != shipSize) {
            Point newDirectionPoint = getDirectionPoint(point);
            while (directionPoint == newDirectionPoint || shipPoints.contains(newDirectionPoint)) {
                newDirectionPoint = getDirectionPoint(point);
            }
            directionPoint = newDirectionPoint;
            if (isNeighborhoodCorrect(directionPoint)) {
                shipPoints.add(directionPoint);
            } else {
                return false;
            }
            point = directionPoint;
        }
        for (Point shipPoint : shipPoints) {
            setPoint(shipPoint, '#');
        }
        shipList.put(shipList.size(), shipPoints);
        ships.add(shipPoints);
        return true;
    }


    public String generateMap() {
        this.shipList = new HashMap<>();
        this.ships = new ArrayList<>();
        List<Integer> shipSizes = getShipSizes();
        boolean generated;
        for (Integer shipSize : shipSizes) {
            do {
                generated = genrateShip(shipSize);
            } while (!generated);
        }
        return map.toString().replace("-", "");
    }

    public void generateMapToFile(File file) {
        try(FileWriter fileWriter = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fileWriter)) {
            String str = generateMap();
            writer.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readMapFromFile(File file) {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultStringBuilder.toString();
    }

    public Map<Integer, List<Point>> getShipList() {
        if(shipList != null) {
            return shipList;
        } else {
            return Map.of();
        }
    }

    public List<List<Point>> getShipPoints() {
        return ships;
    }
}

