package com.richardsand.stuff.mazesolver;

import java.io.IOException;
import java.io.InputStream;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a maze
 * 
 * @author rsand
 */
public class Maze {
    public static enum WallState {
        WALL, OPEN, UNKNOWN, DEADEND, LOOP, PATH, OOB
    }

    @Data
    public static class PositionState {
        public static PositionState createWall(Coordinate position) {
            PositionState wall = new PositionState(position);
            wall.self = Maze.WallState.WALL;
            return wall;
        }

        Coordinate     position;
        Maze.WallState self = Maze.WallState.OPEN;
        Maze.WallState east = Maze.WallState.UNKNOWN, west = Maze.WallState.UNKNOWN, north = Maze.WallState.UNKNOWN, south = Maze.WallState.UNKNOWN;

        public PositionState(Coordinate position) {
            this.position = position;
        }

        public void fillState(Maze maze) {
            this.east = maze.getWallState(position.getX() + 1, position.getY());
            this.west = maze.getWallState(position.getX() - 1, position.getY());
            this.north = maze.getWallState(position.getX(), position.getY() - 1);
            this.south = maze.getWallState(position.getX(), position.getY() + 1);
        }
    }

    /**
     * Runtime exception class for moving out of bounds
     * 
     * @author rsand
     */
    public static class MovementException extends RuntimeException {
        static final long serialVersionUID = -7327575238889487645L;

        public MovementException(String msg) {
            super(msg);
        }
    }

    /**
     * Represents an x,y coordinate pair
     * 
     * @author rsand
     */
    @Getter
    @EqualsAndHashCode
    public static class Coordinate implements Cloneable {
        private int x = 0;
        private int y = 0;
        private int minX, minY, maxX, maxY;

        public Coordinate(int minx, int miny, int maxx, int maxy) {
            this.minX = minx;
            this.minY = miny;
            this.maxX = maxx;
            this.maxY = maxy;
        }

        public int east() {
            if (maxX != 0 && x >= maxX)
                throw new MovementException("Cannot go further east");
            return x++;

        }

        public int west() {
            if (minX != 0 && x <= minX)
                throw new MovementException("Cannot go further west");
            return x--;
        }

        public int south() {
            if (maxY != 0 && y >= maxY)
                throw new MovementException("Cannot go further south");
            return y++;
        }

        public int north() {
            if (minY != 0 && y <= minY)
                throw new MovementException("Cannot go further north");
            return y--;
        }

        void setX(int x) {
            if (maxX != 0 && x > maxX)
                throw new MovementException("X is out of bounds");
            if (minX != 0 && x < minX)
                throw new MovementException("X is out of bounds");
            this.x = x;
        }

        void setY(int y) {
            if (maxY != 0 && y > maxY)
                throw new MovementException("Y is out of bounds");
            if (minY != 0 && y < minY)
                throw new MovementException("Y is out of bounds");
            this.y = y;
        }

        void setPosition(int x, int y) {
            setX(x);
            setY(y);
        }

        @Override
        protected Coordinate clone() {
            Coordinate nc = new Coordinate(this.minX, this.minY, this.maxX, this.maxY);
            nc.x = this.x;
            nc.y = this.y;
            return nc;
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }

    @Getter
    int                       sizeX, sizeY;
    @Getter
    @Setter
    private Coordinate        current, start;
    private PositionState[][] walls;

    public Maze(int x, int y) {
        this(x, y, 0, 0);
    }

    public Maze(int x, int y, int startx, int starty) {
        this.sizeX = x;
        this.sizeY = y;
        walls = new PositionState[x][y];
        current = new Coordinate(0, 0, sizeX, sizeY);
        current.setPosition(startx, starty);
        start = new Coordinate(startx, starty, sizeX, sizeY);
    }

    public boolean isWall(int x, int y) {
        return (walls[x][y].self == Maze.WallState.WALL);
    }

    public WallState getWallState(int x, int y) {
        try {
            return walls[x][y].self;
        } catch (IndexOutOfBoundsException oobe) {
            return WallState.OOB;
        }
    }

    public PositionState getPositionState(int x, int y) {
        return walls[x][y];
    }

    public PositionState getPositionState(Coordinate coord) {
        return walls[coord.getX()][coord.getY()];
    }

    public void addWall(int x, int y) {
        walls[x][y] = PositionState.createWall(newCoordinate(x, y));
    }
    
    public void addOpen(int x, int y) {
        walls[x][y] = new PositionState(newCoordinate(x, y));
    }
    
    public Coordinate newCoordinate(int x, int y) {
        Coordinate c = new Coordinate(0, 0, this.sizeX, this.sizeY);
        c.setX(x);
        c.setY(y);
        return c;
    }

    public static void main(String args[]) throws IOException {
        InputStream is   = Maze.class.getResourceAsStream("/sample.maze");
        Maze        maze = MazeReader.readMaze(is);

        System.out.println("Maze dimension: " + maze.getSizeX() + "x" + maze.getSizeY());
        System.out.println("Current position: " + maze.getCurrent());
    }
}
