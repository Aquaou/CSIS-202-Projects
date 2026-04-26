import java.util.ArrayList;
import java.util.List;

public class Queen extends ChessPiece {

    public Queen(ChessBoard board, Color color) {
        super(board, color);
    }

    @Override
    public List<String> legalMoves() throws IllegalPositionException {
        Rook rook = new Rook(board, this.getColor());
        board.placePiece(rook, this.getPosition());
        List<String> rookMoves = rook.legalMoves();

        Bishop bishop = new Bishop(board, this.getColor());
        board.placePiece(bishop, this.getPosition());
        List<String> bishopMoves = bishop.legalMoves();

        // Restore the queen on the board
        board.placePiece(this, this.getPosition());

        List<String> queenMoves = new ArrayList<>();

        for (String move : rookMoves) {
            if (!queenMoves.contains(move)) {
                queenMoves.add(move);
            }
        }

        for (String move : bishopMoves) {
            if (!queenMoves.contains(move)) {
                queenMoves.add(move);
            }
        }

        return queenMoves;
    }

    private String getCoordsString(int r, int c) {
        char fileChar = (char) ('a' + c);
        char rankChar = (char) ('1' + (8 - r));
        return String.valueOf(fileChar) + rankChar;
    }
}