import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.List;
import java.util.ArrayList;

public class GridLoader {

    // Nested Static Class (does not need an instance of GridLoader)
    public static class Grid {
        private final boolean[][] alive;
        // Is a 2D array of true/false and only accessible inside grid
        // Final makes it so that it cannot be re-assigned

        public boolean[][] getAlive() {
            return alive;
        }

        // This is a constructor
        // assigns the passed in 2D array to the internal field
        public Grid(boolean[][] alive) {
            this.alive = alive;
            // this.alive refers to the class field
            // alive refers to the parameter
        }

        public int rows() {
            return alive.length;
            // returns how many row arrays exist
        }

        public int cols() {
            return alive[0].length;
            // returns the number of columns
        }

        public boolean isAlive(int r, int c) {
            return alive[r][c];
            // This is a getter method for a specific cell
        }


        // Basically replaces the default toString() from Object
        // Automatically calls grid.toString()
        @Override
        public String toString() {

            StringBuilder sb = new StringBuilder();
            // Creates a string builder

            // Iterates over every cell in the grid
            for (int r = 0; r < rows(); r++) {
                for (int c = 0; c < cols(); c++) {
                    sb.append(alive[r][c] ? 'O' : '.');
                    // If the cell is alive (true) append 'O'
                    // If dead (false) append '.'
                }
                if (r < rows() - 1) sb.append('\n');
                // This adds a newline after each row except the last one
            }
            return sb.toString();
            // Converts the built string into an actual String
        }
    }


    public static Grid loadGrid(InputStream in) throws IOException {
        List<String> lines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        return parseLines(lines);
    }


    public static Grid loadGrid(Path file) throws IOException {
        List<String> lines = Files.readAllLines(file);
        return parseLines(lines);
    }


    private static Grid parseLines(List<String> lines) {

        if (lines.size() < 2) {
            throw new IllegalArgumentException("Grid must have at least 2 rows, found " + lines.size() + ".");
        }

        String firstLine = lines.get(0);
        int cols = firstLine.length();

        if (cols < 2) {
            throw new IllegalArgumentException("Grid must have at least 2 columns, found " + cols + ".");
        }

        int rows = lines.size();
        boolean[][] alive = new boolean[rows][cols];

        for (int r = 0; r < rows; r++) {
            String line = lines.get(r);

            if (line.isEmpty()) {
                throw new IllegalArgumentException("Row " + (r + 1) + " is empty. Blank lines are not allowed.");
            }

            if (line.length() != cols) {
                throw new IllegalArgumentException(
                        "Non rectangular grid at row " + (r + 1) +
                                ": expected " + cols + " columsn but found " + line.length() + "."
                );
            }

            for (int c = 0; c < cols; c++) {
                char ch = line.charAt(c);

                if (ch == 'O') {
                    alive[r][c] = true;
                } else if (ch == '.') {
                    alive[r][c] = false;
                } else {
                    throw new IllegalArgumentException(
                            "Invalid character " + ch + " at row " + (r + 1) + ", col " + (c + 1) +
                                    ". Only 'O' and '.' are allowed."
                    );
                }
            }
        }

        if (rows < 2 || cols < 2) {
            throw new IllegalArgumentException("Grid must be at least 2x2.");
        }

        return new Grid(alive);
    }


    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java GridLoader <path_to_grid_file>");
            System.exit(1);
        }

        Path file = Path.of(args[0]);

        try {
            Grid grid = loadGrid(file);

            System.out.println("Loaded Grid: " + grid.rows() + " x " + grid.cols());
            System.out.println(grid);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid grid file: " + e.getMessage());
            System.exit(2);
        } catch (IOException e) {
            System.err.println("Could not read file: " + e.getMessage());
            System.exit(3);
        }
    }
}