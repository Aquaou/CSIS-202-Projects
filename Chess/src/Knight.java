import java.util.ArrayList;
import java.util.List;

public class Knight extends ChessPiece {
    public Knight(ChessBoard board, Color color) {
        super(board, color);
    }

    @Override
    public List<String> legalMoves() throws IllegalPositionException {
        List<String> legalMoves = new ArrayList<>();

        int[] dr = {-2, -2, -1, -1, 1, 1, 2, 2};
        int[] dc = {-1, 1, -2, 2, -2, 2, -1, 1};
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

        for (int i = 0; i < 8; i++) {
            int targetR = startR + dr[i];
            int targetC = startC + dc[i]; // was dr[i] — bug fix

            if (!board.isWithinBounds(targetR, targetC)) {
                continue;
            }

            String targetPos = getCoordsString(targetR, targetC);
            ChessPiece destinationPiece = board.getPiece(targetPos);

            if (destinationPiece != null && destinationPiece.getColor() == this.getColor()) {
                continue;
            }

            legalMoves.add(targetPos);
        }
        return legalMoves;
    }

    private String getCoordsString(int r, int c) {
        char fileChar = (char) ('a' + c);
        char rankChar = (char) ('1' + (8 - r));
        return String.valueOf(fileChar) + rankChar;
    }
}