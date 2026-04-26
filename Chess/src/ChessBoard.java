import java.util.List;

public class ChessBoard {

    private ChessPiece[][] board;

    public ChessBoard() {
        this.board = new ChessPiece[8][8];
    }

    int[] getCoords(String position) throws IllegalPositionException {
        if (position == null || position.length() != 2) {
            throw new IllegalPositionException("Position must be exactly two characters (e.g., 'a1')");
        }

        char fileChar = Character.toLowerCase(position.charAt(0));
        char rankChar = Character.toLowerCase(position.charAt(1));

        if (fileChar < 'a' || fileChar > 'h') {
            throw new IllegalPositionException("Invalid file coordinate: " + fileChar);
        }

        if (rankChar < '1' || rankChar > '8') {
            throw new IllegalPositionException("Invalid rank coordinate: " + rankChar);
        }

        int col = fileChar - 'a';

        int row = 8 - (rankChar - '0');

        return new int[]{
                row, col
        };
    }

    boolean isWithinBounds(int r, int c) {
        return r >= 0 && r < 8 && c >= 0 && c < 8;
    }

    public void initialize() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.board[i][j] = null;
            }
        }

        // Setups for White

        // Back row
        ChessPiece whiteRook = new Rook(this, ChessPiece.Color.WHITE);
        ChessPiece whiteKnight = new Knight(this, ChessPiece.Color.WHITE);
        ChessPiece whiteBishop = new Bishop(this, ChessPiece.Color.WHITE);
        ChessPiece whiteQueen = new Queen(this, ChessPiece.Color.WHITE);
        ChessPiece whiteKing = new King(this, ChessPiece.Color.WHITE);

        placePiece(whiteRook, "a8");
        placePiece(whiteKnight, "b8");
        placePiece(whiteBishop, "c8");
        placePiece(whiteQueen, "d8");
        placePiece(whiteKing, "e8");
        placePiece(whiteBishop, "f8");
        placePiece(whiteRook, "h8");

        for (int i = 0; i < 8; i++) {
            String pos = String.valueOf((char) ('a' + i)) + "7";
            placePiece(new Pawn(this, ChessPiece.Color.WHITE), pos);
        }

        // Setups for Black

        // Back row
        ChessPiece blackRook = new Rook(this, ChessPiece.Color.BLACK);
        ChessPiece blackKnight = new Knight(this, ChessPiece.Color.BLACK);
        ChessPiece blackBishop = new Bishop(this, ChessPiece.Color.BLACK);
        ChessPiece blackQueen = new Queen(this, ChessPiece.Color.BLACK);
        ChessPiece blackKing = new King(this, ChessPiece.Color.BLACK);

        placePiece(blackRook, "a1");
        placePiece(blackKnight, "b1");
        placePiece(blackBishop, "c1");
        placePiece(blackQueen, "d1");
        placePiece(blackKing, "e1");
        placePiece(blackBishop, "f1");
        placePiece(blackRook, "h1");

        // Pawns

        for (int i = 0; i < 8; i++) {
            String pos = String.valueOf((char) ('a' + i)) + "2";
            placePiece(new Pawn(this, ChessPiece.Color.BLACK), pos);
        }
    }

    public ChessPiece getPiece(String position) throws IllegalPositionException {
        int[] coords = getCoords(position);
        int r = coords[0];
        int c = coords[1];
        return this.board[r][c];
    }

    public boolean placePiece(ChessPiece piece, String position) {
        try {
            int[] coords = getCoords(position);
            int r = coords[0];
            int c = coords[1];

            if (isWithinBounds(r, c)) {
                this.board[r][c] = piece;

                piece.setPosition(position);
                return true;
            }
            return false;
        } catch (IllegalPositionException e) {
            return false;
        }
    }

    public void move(String from, String to) throws IllegalMoveException, IllegalPositionException {
        ChessPiece movingPiece = getPiece(from);

        if (movingPiece == null) {
            throw new IllegalMoveException("Cannot move: No piece found at source position: " + from);
        }

        List<String> legalMoves = movingPiece.getLegalMoves();
        if (!legalMoves.contains(to)) {
            throw new IllegalMoveException("Illegal move attempt: " + movingPiece.getClass().getSimpleName() + " cannot move from " + from + " to " + to);
        }

        int[] fromCoords = getCoords(from);
        int fromR = fromCoords[0];
        int fromC = fromCoords[1];
        this.board[fromR][fromC] = null;

        int[] toCoords = getCoords(to);
        int toR = toCoords[0];
        int toC = toCoords[1];
        this.board[toR][toC] = movingPiece;

        movingPiece.setPosition(to);
    }
}