import java.util.ArrayList;
import java.util.List;

public class Rook extends ChessPiece {

    public Rook(ChessBoard board, Color color) {
        super(board, color);
    }

    public List<String> legalMoves() throws IllegalPositionException {
        List<String> legalMoves = new ArrayList<>();

        String startPos = this.getPosition();
        int[] startCoords;
        try {
            startCoords = board.getCoords(startPos);
        } catch (IllegalPositionException e) {
            System.err.println("Error retrieving starting coordinates: " + e.getMessage());
            return legalMoves;
        }

        int startR = startCoords[0];
        int startC = startCoords[1];

        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, 1, -1};

        for (int i = 0; i < 4; i++) {
            int dr_step = dr[i];
            int dc_step = dc[i];

            int currentRow = startR;
            int currentCol = startC;

            while (true) {
                currentRow += dr_step;
                currentCol += dc_step;

                if (!board.isWithinBounds(currentRow, currentCol)) {
                    break;
                }

                String nextPos = getCoordsString(currentRow, currentCol);
                ChessPiece destinationPiece = board.getPiece(nextPos);

                if (destinationPiece == null) {
                    legalMoves.add(nextPos);
                    continue;
                }

                if (destinationPiece.getColor() == this.getColor()) {
                    break;
                } else {
                    legalMoves.add(nextPos);
                    break;
                }
            }
        }

        return legalMoves;
    }

    private String getCoordsString(int r, int c) {
        char fileChar = (char) ('a' + c);
        char rankChar = (char) ('1' + (8 - r));
        return String.valueOf(fileChar) + rankChar;
    }
}