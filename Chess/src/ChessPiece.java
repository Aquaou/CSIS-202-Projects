import java.util.List;

public abstract class ChessPiece {

    public enum Color {
        WHITE, BLACK
    }

    final ChessBoard board;
    private final Color color;
    private String position;

    public ChessPiece(ChessBoard board, Color color) {
        this.board = board;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) throws IllegalPositionException {
        if (!position.matches("[a-h][1-8]")) {
            throw new IllegalPositionException("Invalid position: " + position);
        }
        this.position = position;
    }

    public List<String> getLegalMoves() throws IllegalPositionException {
        return legalMoves();
    }

    public abstract List<String> legalMoves() throws IllegalPositionException;
}