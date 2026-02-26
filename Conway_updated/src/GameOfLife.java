import java.io.IOException;
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
        if (r < 0 || r >= grid.length) return false;
        if (c < 0 || c >= grid[r].length) return false;
        return grid[r][c];
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
                System.out.print(cell ? "O " : ". ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java GameOfLife <path_to_grid_file>");
            System.exit(1);
        }

        try {
            GridLoader.Grid loaded = GridLoader.loadGrid(Path.of(args[0]));
            grid = loaded.getAlive();
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid grid file: " + e.getMessage());
            System.exit(2);
        } catch (IOException e) {
            System.err.println("Could not read file: " + e.getMessage());
            System.exit(3);
        }

        for (int generation = 0; generation < 320; generation++) {
            System.out.println("Generation: " + generation);
            System.out.println();
            printGrid();
            grid = nextGen();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}