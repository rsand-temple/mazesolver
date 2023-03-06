package com.richardsand.stuff.mazesolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import com.richardsand.stuff.mazesolver.Maze.Coordinate;
import com.richardsand.stuff.mazesolver.Maze.PositionState;
import com.richardsand.stuff.mazesolver.Maze.WallState;

import lombok.Getter;

public class MazeSolver {
    Maze maze;

    @Getter
    LinkedList<Coordinate> solution = new LinkedList<>();
    int                    tries    = 0;

    public MazeSolver(Maze maze) {
        this.maze = maze;
    }

    public boolean solve(Coordinate coord) {
        PositionState eval = maze.getPositionState(coord);

        if (!coord.equals(maze.getStart())) {
            if ((coord.getX() == 0) && (eval.getWest() == Maze.WallState.OOB)) {
                System.out.println("Exiting west!");
                return true;
            }
            if ((coord.getY() == 0) && (eval.getNorth() == Maze.WallState.OOB)) {
                System.out.println("Exiting north!");
                return true;
            }
            if ((coord.getX() == maze.getSizeX() - 1) && (eval.getEast() == Maze.WallState.OOB)) {
                System.out.println("Exiting east!");
                return true;
            }
            if ((coord.getY() == maze.getSizeY() - 1) && (eval.getSouth() == Maze.WallState.OOB)) {
                System.out.println("Exiting south!");
                return true;
            }
        }

        System.out.println(eval);
        if (++tries > maze.getSizeX() * maze.getSizeY()) {
            System.out.println("We're clearly going in circles after " + tries + " tries, ending");
            return false;
        }

        if (eval.getWest() == Maze.WallState.OPEN) {
            solution.push(coord.clone());
            coord.west();
            System.out.println("    Going WEST to " + coord);
            maze.getPositionState(coord).east = Maze.WallState.PATH;
            return solve(coord);
        }

        if (eval.getNorth() == Maze.WallState.OPEN) {
            solution.push(coord.clone());
            coord.north();
            System.out.println("    Going NORTH to " + coord);
            maze.getPositionState(coord).south = Maze.WallState.PATH;
            return solve(coord);
        }

        if (eval.getEast() == Maze.WallState.OPEN) {
            solution.push(coord.clone());
            coord.east();
            System.out.println("    Going EAST to " + coord);
            maze.getPositionState(coord).west = Maze.WallState.PATH;
            return solve(coord);
        }

        if (eval.getSouth() == Maze.WallState.OPEN) {
            solution.push(coord.clone());
            coord.south();
            System.out.println("    Going SOUTH to " + coord);
            maze.getPositionState(coord).north = Maze.WallState.PATH;
            return solve(coord);
        }

        // We're blocked, go back
        if (solution.isEmpty()) {
            System.out.println("We're back to where we started, ending");
            return false;
        }

        solution.pop();
        Coordinate previous = solution.pop();
        System.out.println("Dead end, going back to " + previous);

        // Cordon off the last path in reverse order
        if (eval.getSouth() == WallState.PATH)
            maze.getPositionState(previous).setNorth(WallState.DEADEND);
        else if (eval.getEast() == WallState.PATH)
            maze.getPositionState(previous).setWest(WallState.DEADEND);
        else if (eval.getNorth() == WallState.PATH)
            maze.getPositionState(previous).setSouth(WallState.DEADEND);
        else if (eval.getWest() == WallState.PATH)
            maze.getPositionState(previous).setEast(WallState.DEADEND);
        else
            return false;

        return solve(previous);
    }

    public static void main(String[] args) throws IOException {
        InputStream is   = Maze.class.getResourceAsStream("/sample.maze");
        Maze        maze = MazeReader.readMaze(is);

        System.out.println("Maze dimension: " + maze.getSizeX() + "x" + maze.getSizeY());
        System.out.println("Current position: " + maze.getCurrent());

        MazeSolver solver = new MazeSolver(maze);
        if (solver.solve(maze.getStart())) {
            System.out.print("Solved!!");
            System.out.println("Solution: " + solver);
        }
    }
}
