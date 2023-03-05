package com.richardsand.stuff.mazesolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import com.richardsand.stuff.mazesolver.Maze.Coordinate;

import lombok.Data;
import lombok.Getter;

public class MazeSolver {
    @Data
    static class PositionEval {
        Coordinate position;
        Maze.WallState  east = Maze.WallState.UNKNOWN, west = Maze.WallState.UNKNOWN, north = Maze.WallState.UNKNOWN, south = Maze.WallState.UNKNOWN;
        
        public PositionEval(Coordinate position) {
            this.position = position;
        }
    }

    Maze                     maze;
    @Getter
    LinkedList<PositionEval> solution = new LinkedList<>();

    public MazeSolver(Maze maze) {
        this.maze = maze;
    }

    public boolean solve(Coordinate coord, PositionEval eval) {
        System.out.println("Evaluating coordinates " + coord);
        // First evaluate win conditions
        if (coord != maze.getStart()) {
            if ((coord.getX() == 0) && (eval.getWest() == Maze.WallState.OPEN)) {
                System.out.println("Exiting west!");
                return true;
            }
            if ((coord.getY() == 0) && (eval.getNorth() == Maze.WallState.OPEN)) {
                System.out.println("Exiting north!");
                return true;
            }
            if ((coord.getX() == maze.getSizeX()) && (eval.getEast() == Maze.WallState.OPEN)) {
                System.out.println("Exiting east!");
                return true;
            }
            if ((coord.getY() == maze.getSizeY()) && (eval.getSouth() == Maze.WallState.OPEN)) {
                System.out.println("Exiting south!");
                return true;
            }
        } else {
            // prime the pump
            eval = new PositionEval(coord.clone());
        }

        if (eval.getWest() == Maze.WallState.OPEN) {
            System.out.println("Going WEST " + coord);
            coord.west();
            PositionEval next = new PositionEval(coord.clone());
            next.east = Maze.WallState.PATH;
            next.south = maze.getWallState(next.getPosition().getX(), next.getPosition().getY() + 1);
            next.west = maze.getWallState(next.getPosition().getX() - 1, next.getPosition().getY());
            next.north = maze.getWallState(next.getPosition().getX(), next.getPosition().getY() - 1);
            return solve(coord, next);
        } 
        
        if (eval.getNorth() == Maze.WallState.OPEN) {
            System.out.println("Going NORTH " + coord);
            coord.north();
            PositionEval next = new PositionEval(coord.clone());
            next.south = Maze.WallState.PATH;
            next.west = maze.getWallState(next.getPosition().getX() - 1, next.getPosition().getY());
            next.north = maze.getWallState(next.getPosition().getX(), next.getPosition().getY() - 1);
            next.east = maze.getWallState(next.getPosition().getX() + 1, next.getPosition().getY());
            return solve(coord, next);
        } 
        
        if (eval.getEast() == Maze.WallState.OPEN) {
            System.out.println("Going EAST " + coord);
            coord.east();
            PositionEval next = new PositionEval(coord.clone());
            next.west = Maze.WallState.PATH;
            next.north = maze.getWallState(next.getPosition().getX(), next.getPosition().getY() - 1);
            next.east = maze.getWallState(next.getPosition().getX() + 1, next.getPosition().getY());
            next.south = maze.getWallState(next.getPosition().getX(), next.getPosition().getY() + 1);
            return solve(coord, next);
        }
        
        if (eval.getSouth() == Maze.WallState.OPEN) {
            System.out.println("Going SOUTH " + coord);
            coord.south();
            PositionEval next = new PositionEval(coord.clone());
            next.north = Maze.WallState.PATH;
            next.east = maze.getWallState(next.getPosition().getX() + 1, next.getPosition().getY());
            next.south = maze.getWallState(next.getPosition().getX(), next.getPosition().getY() + 1);
            next.west = maze.getWallState(next.getPosition().getX() - 1, next.getPosition().getY());
            return solve(coord, next);
        }
        
        // We're blocked, go back
        System.out.println("Dead end, going back " + coord);
        return false;
    }
    
    public static void main(String[] args) throws IOException {
        InputStream is = Maze.class.getResourceAsStream("/sample.maze");
        Maze maze = MazeReader.readMaze(is);

        System.out.println("Maze dimension: " + maze.getSizeX() + "x" + maze.getSizeY());
        System.out.println("Current position: " + maze.getCurrent());
        
        MazeSolver solver = new MazeSolver(maze);
        if (solver.solve(maze.getStart(), null)) {
            System.out.print("Solved!!");
            System.out.println(solver.getSolution());
        }
    }
}
