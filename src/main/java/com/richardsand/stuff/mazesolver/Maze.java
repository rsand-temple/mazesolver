package com.richardsand.stuff.mazesolver;

import java.io.IOException;
import java.io.InputStream;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a maze
 * 
 * @author rsand
 */
public class Maze {
    public static enum Directions {
        NORTH, SOUTH, EAST, WEST
    };

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
    @ToString
    @EqualsAndHashCode
    public static class Coordinate implements Cloneable {
        private int x = 0;
        private int y = 0;
        private int minX, minY, maxX, maxY;

        public Coordinate(int maxx, int maxy) {
            this(0, 0, maxx, maxy);
        }

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

        public int north() {
            if (maxY != 0 && y >= maxY)
                throw new MovementException("Cannot go further north");
            return y++;
        }

        public int south() {
            if (minY != 0 && y <= minY)
                throw new MovementException("Cannot go further south");
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
    }

    public static enum WallState {
        WALL, OPEN, UNKNOWN, DEADEND, LOOP, PATH, OOB
    }

    @Getter
    int         sizeX, sizeY;
    @Getter
    @Setter
    Coordinate  current, start;
    boolean[][] walls;

    public Maze(int x, int y) {
        this(x, y, 0, 0);
    }

    public Maze(int x, int y, int startx, int starty) {
        this.sizeX = x;
        this.sizeY = y;
        walls = new boolean[x][y];
        current = new Coordinate(sizeX, sizeY);
        current.setPosition(startx, starty);
        start = new Coordinate(startx, starty);
    }

    public boolean isWall(int x, int y) {
        return walls[x][y];
    }
    
    public WallState getWallState(int x, int y) {
        
    }

    public void addWall(int x, int y) {
        setWall(x, y, true);
    }

    public void setWall(int x, int y, boolean value) {
        walls[x][y] = value;
    }

    public static void main(String args[]) throws IOException {
        InputStream is   = Maze.class.getResourceAsStream("/sample.maze");
        Maze        maze = MazeReader.readMaze(is);

        System.out.println("Maze dimension: " + maze.getSizeX() + "x" + maze.getSizeY());
        System.out.println("Current position: " + maze.getCurrent());
    }
}
