package com.richardsand.stuff.mazesolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
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
        // Retrieve the current state for the coordinate
        PositionState eval = maze.getPositionState(coord);

        // First check for win conditions
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

        // Push the current coordinates onto the stack
        System.out.println(eval);
        solution.push(coord.clone()); // we use a clone here because the variable coord keeps changing
        
        // This was added for troubleshooting to prevent stack overflow errors
        if (++tries > maze.getSizeX() * maze.getSizeY()) {
            System.out.println("We're clearly going in circles after " + tries + " tries, ending");
            return false;
        }

        // Go WEST if we can
        if (eval.getWest() == Maze.WallState.OPEN) {
            coord.west();
            maze.getPositionState(coord).east = Maze.WallState.PATH;
            System.out.println("    Going WEST to " + coord);
            return solve(coord);
        }

        // Go NORTH if we can
        if (eval.getNorth() == Maze.WallState.OPEN) {
            coord.north();
            maze.getPositionState(coord).south = Maze.WallState.PATH;
            System.out.println("    Going NORTH to " + coord);
            return solve(coord);
        }

        // Go EAST if we can
        if (eval.getEast() == Maze.WallState.OPEN) {
            coord.east();
            maze.getPositionState(coord).west = Maze.WallState.PATH;
            System.out.println("    Going EAST to " + coord);
            return solve(coord);
        }

        // Go SOUTH if we can
        if (eval.getSouth() == Maze.WallState.OPEN) {
            coord.south();
            maze.getPositionState(coord).north = Maze.WallState.PATH;
            System.out.println("    Going SOUTH to " + coord);
            return solve(coord);
        }

        // If we reach here, we are blocked, go back
        // Another sanity check - if the solution is empty, something went wrong and we've gone in a circle
        if (solution.isEmpty()) {
            System.out.println("We're back to where we started, ending");
            return false;
        }

        // Going back
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
            Collections.reverse(solver.solution);
            System.out.println("Solution: " + solver.solution);
        }
    }
}
