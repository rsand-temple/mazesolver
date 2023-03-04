package com.richardsand.stuff.mazesolver;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a maze
 * 
 * @author rsand
 */
public class Maze {
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
    public static class Coordinate {
        private int x = 0;
        private int y = 0;
        private int minx, miny, maxx, maxy;
        
        public Coordinate(int maxx, int maxy) {
            this(0,0,maxx,maxy);
        }

        public Coordinate(int minx, int miny, int maxx, int maxy) {
            this.minx = minx;
            this.miny = miny;
            this.maxx = maxx;
            this.maxy = maxy;
        }

        int east() {
            if (maxx != 0 && x >= maxx)
                throw new MovementException("Cannot go further east");
            return x++;

        }

        int west() {
            if (minx != 0 && x <= minx)
                throw new MovementException("Cannot go further west");
            return x--;
        }

        int north() {
            if (maxy != 0 && y >= maxy)
                throw new MovementException("Cannot go further north");
            return y++;
        }

        int south() {
            if (miny != 0 && y <= miny)
                throw new MovementException("Cannot go further south");
            return y--;
        }
        
        void setX(int x) {
            if (maxx != 0 && x > maxx)
                throw new MovementException("X is out of bounds");
            if (minx != 0 && x < minx)
                throw new MovementException("X is out of bounds");
            this.x = x;            
        }

        void setY(int y) {
            if (maxy != 0 && y > maxy)
                throw new MovementException("Y is out of bounds");
            if (miny != 0 && y < miny)
                throw new MovementException("Y is out of bounds");
            this.y = y; 
        }

        void setPosition(int x, int y) {
            setX(x);
            setY(y);
        }
    }
    
    @Getter
    int sizex, sizey;
    @Getter @Setter
    Coordinate position;
    boolean[][] walls;
    
    public Maze(int x, int y) {
        this(x,y,0,0);
    }
    
    public Maze(int x, int y, int startx, int starty) {
        this.sizex = x;
        this.sizey = y;
        walls = new boolean[x][y];
        position = new Coordinate(sizex, sizey);
        position.setPosition(startx, starty);
    }
    
    public boolean isWall(int x, int y) {
        return walls[x][y];
    }
    public void addWall(int x, int y) {
        setWall(x,y,true);
    }
    public void setWall(int x, int y, boolean value) {
        walls[x][y] = value;
    }

}
