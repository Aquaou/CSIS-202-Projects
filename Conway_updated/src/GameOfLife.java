import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;


public class GameOfLife {

    static boolean[][] grid;

    private static final int[][] DIRS_8 = {
            {-1, 0}, // top
            {1, 0}, // bottom
            {0, -1}, // left
            {0, 1}, // right
            {-1, -1}, // top-left
            {-1, 1}, // top-right
            {1, -1}, // bottom-left
            {1, 1} // bottom-right
    };


    public static boolean isFilled(boolean[][] grid, int r, int c) {
        if(grid == null || grid.length == 0) return false;

        // Wrap around logic
        int rows = grid.length;
        int cols = grid[0].length;
        int wr = ((r % rows) + rows) % rows;
        int wc = ((c % cols) + cols) % cols;
        return grid[wr][wc];
        /*
        * I am using something called toroidal indexing
        * EXAMPLE:
          Using r = -1, rows = 10:
          1. r % rows: -1(mod 10) = -1
          2. + rows: -1 + 10 = 9
          3. % rows: 9 (mod 10) = 9
        * As you can see from the example the index -1 correctly wraps around to index 9
        */
    }


    public static int countFilledNeighbors(boolean[][] grid, int r, int c) {
        int count = 0;
        for (int[] d : DIRS_8) {
            if (isFilled(grid, r + d[0], c + d[1])) count++;
        }
        return count;
    }


    public static boolean[][] nextGen() {
        boolean[][] newgen = new boolean[grid.length][grid[0].length];

        for(int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                int neighbors = countFilledNeighbors(grid, r, c);
                if (grid[r][c]) {
                    newgen[r][c] = (neighbors == 2 || neighbors == 3);
                } else {
                    newgen[r][c] = (neighbors == 3);
                }
            }
        }
        return newgen;
    }


    public static void printGrid() {
        for (boolean[] row : grid) {
            for (boolean cell : row) {
                System.out.print(cell ? "O" : "."); // No spaces between cells
            }
            System.out.println();
        }
    }


    public static void main(String[] args) {
        try {
            GridLoader.Grid loaded;
            if (args.length >= 1) {
                // Accepts a plain filename or a full path
                loaded = GridLoader.loadGrid(Path.of(args[0]));
            } else {
                // No agrument so read grid from standard input
                System.out.println("Please paste or type your grid below.");
                System.out.println("When finished, press Enter, then CTRL+Z, and press Enter again to start.");
                loaded = GridLoader.loadGrid(System.in);
            }
            grid = loaded.getAlive();

        } catch (IllegalArgumentException e) {
            System.err.println("Invalid grid file: " + e.getMessage());
            System.exit(2);
        } catch (IOException e) {
            System.err.println("Could not read file: " + e.getMessage());
            System.exit(3);
        }

        for (int generation = 1; generation < 320; generation++) {
            // Advance first and then print the next generation
            grid = nextGen();
            System.out.println("Generation: " + generation);
            System.out.println();
            printGrid();
            System.out.flush();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}