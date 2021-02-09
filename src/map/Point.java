package map;

import java.util.Objects;
import java.util.Random;

public class Point {
    public int row, col;

    public Point(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Point(String coordinates) {
        this.col = getColumn(coordinates.substring(0, 1));
        this.row = getRow(coordinates.substring(1));
    }

    int getColumn (String col) {
        return col.charAt(0) - 'A';
    }

    int getRow (String row) {
        return Integer.parseInt(row) - 1;
    }

    public static Point getRandomly() {
        Random random = new Random();
        int row = random.nextInt(10);
        int col = random.nextInt(10);
        return new Point(row, col);
    }

    @Override
    public String toString() {
        return String.valueOf((char)('A' + col)) + (row + 1);
        //return "(" + row + ", " + col + ")";
    }

    public String toAlphaNumericString() {
        return String.valueOf((char)('A' + col)) + (row + 1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return row == point.row &&
                col == point.col;
    }
}