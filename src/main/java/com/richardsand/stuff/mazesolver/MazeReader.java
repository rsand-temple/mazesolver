package com.richardsand.stuff.mazesolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MazeReader {
    public static Maze readMaze(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String nextline;
        int    maxx = 0, startx = 0, starty = 0;

        List<boolean[]> lines = new ArrayList<>();

        int linectr = 0;
        while ((nextline = br.readLine()) != null) {
            if (nextline.startsWith("//"))
                continue;
            if (nextline.length() == 0)
                continue;
            boolean[] mazeline = new boolean[nextline.length()];
            for (int q = 0; q < nextline.length(); q++) {
                if (nextline.charAt(q) == '#')
                    mazeline[q] = true;
                if (nextline.charAt(q) == '@') {
                    startx = linectr;
                    starty = q;
                }
            }
            lines.add(mazeline);
            if (nextline.length() > maxx)
                maxx = nextline.length();
            linectr++;
        }

        Maze maze = new Maze(maxx, linectr, startx, starty);

        linectr = 0;
        for (boolean[] line : lines) {
            for (int j = 0; j < line.length; j++)
                maze.setWall(linectr, j, line[j]);
            linectr++;
        }

        return maze;
    }
}
