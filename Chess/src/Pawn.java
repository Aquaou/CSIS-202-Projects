import java.util.ArrayList;
import java.util.List;

public class Pawn extends ChessPiece {

    public Pawn(ChessBoard board, Color color) {
        super(board, color);
    }

    @Override
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

        int directionR = (this.getColor() == Color.WHITE) ? -1 : 1;

        int r1 = startR + directionR;
        int c1 = startC;
        String pos1 = getCoordsString(r1, c1);
        if (board.isWithinBounds(r1, c1) && board.getPiece(pos1) == null) {
            legalMoves.add(pos1);

            // Two-square advance from starting rank
            boolean isWhiteStart = (this.getColor() == Color.WHITE && startR == 6);
            boolean isBlackStart = (this.getColor() == Color.BLACK && startR == 1);
            if (isWhiteStart || isBlackStart) {
                int r2 = startR + 2 * directionR;
                String pos2 = getCoordsString(r2, c1);
                if (board.isWithinBounds(r2, c1) && board.getPiece(pos2) == null) {
                    legalMoves.add(pos2);
                }
            }
        }

        // Diagonal captures
        for (int dc_step : new int[]{-1, 1}) {
            int r_capture = startR + directionR;
            int c_capture = startC + dc_step;

            if (board.isWithinBounds(r_capture, c_capture)) {
                String capturePos = getCoordsString(r_capture, c_capture);
                ChessPiece pieceAtCapture = board.getPiece(capturePos);

                if (pieceAtCapture != null && pieceAtCapture.getColor() != this.getColor()) {
                    legalMoves.add(capturePos);
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