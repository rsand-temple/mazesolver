package com.richardsand.stuff.mazesolver;

import java.util.LinkedList;

import com.richardsand.stuff.mazesolver.Maze.Coordinate;

import lombok.Data;

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
    LinkedList<PositionEval> solution = new LinkedList<>();

    public MazeSolver(Maze maze) {
        this.maze = maze;
    }

    public boolean solve(Coordinate coord, PositionEval eval) {
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
        }

        if (eval.getWest() == Maze.WallState.OPEN) {
            System.out.println("Going WEST " + coord);
            coord.west();
            PositionEval next = new PositionEval(coord.clone());
            next.east = Maze.WallState.PATH;
            next.south = (maze.walls[next.getPosition().getX()][next.getPosition().getY() + 1]) ? Maze.WallState.WALL : Maze.WallState.OPEN;
            next.west = (maze.walls[next.getPosition().getX() - 1][next.getPosition().getY()]) ? Maze.WallState.WALL : Maze.WallState.OPEN;
            next.north = (maze.walls[next.getPosition().getX()][next.getPosition().getY() - 1]) ? Maze.WallState.WALL : Maze.WallState.OPEN;
            return solve(coord, next);
        } 
        
        if (eval.getNorth() == Maze.WallState.OPEN) {
            System.out.println("Going NORTH " + coord);
            coord.north();
            PositionEval next = new PositionEval(coord.clone());
            next.south = Maze.WallState.PATH;
            next.west = (maze.walls[next.getPosition().getX() - 1][next.getPosition().getY()]) ? Maze.WallState.WALL : Maze.WallState.OPEN;
            next.north = (maze.walls[next.getPosition().getX()][next.getPosition().getY() - 1]) ? Maze.WallState.WALL : Maze.WallState.OPEN;
            next.east = (maze.walls[next.getPosition().getX() + 1][next.getPosition().getY()]) ? Maze.WallState.WALL : Maze.WallState.OPEN;
            return solve(coord, next);
        } 
        
        if (eval.getEast() == Maze.WallState.OPEN) {
            System.out.println("Going EAST " + coord);
            coord.east();
            PositionEval next = new PositionEval(coord.clone());
            next.west = Maze.WallState.PATH;
            next.north = (maze.walls[next.getPosition().getX()][next.getPosition().getY() - 1]) ? Maze.WallState.WALL : Maze.WallState.OPEN;
            next.east = (maze.walls[next.getPosition().getX() + 1][next.getPosition().getY()]) ? Maze.WallState.WALL : Maze.WallState.OPEN;
            next.south = (maze.walls[next.getPosition().getX()][next.getPosition().getY() + 1]) ? Maze.WallState.WALL : Maze.WallState.OPEN;
            return solve(coord, next);
        }
        
        if (eval.getSouth() == Maze.WallState.OPEN) {
            System.out.println("Going SOUTH " + coord);
            coord.south();
            PositionEval next = new PositionEval(coord.clone());
            next.north = Maze.WallState.PATH;
            next.east = (maze.walls[next.getPosition().getX() + 1][next.getPosition().getY()]) ? Maze.WallState.WALL : Maze.WallState.OPEN;
            next.south = (maze.walls[next.getPosition().getX()][next.getPosition().getY() + 1]) ? Maze.WallState.WALL : Maze.WallState.OPEN;
            next.west = (maze.walls[next.getPosition().getX() - 1][next.getPosition().getY()]) ? Maze.WallState.WALL : Maze.WallState.OPEN;
            return solve(coord, next);
        }
        
        // We're blocked, go back
        System.out.println("Dead end, going back " + coord);
        return false;
    }
}
