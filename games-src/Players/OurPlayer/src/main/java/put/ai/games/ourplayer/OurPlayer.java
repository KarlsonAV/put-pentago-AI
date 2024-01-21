package put.ai.games.ourplayer;

import java.util.*;

import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;

public class OurPlayer extends Player {

    class PentagoMove {
        Move move;
        Double value;

        PentagoMove(Move move, Double value) {
            this.move = move;
            this.value = value;
        }
    }

    static Random random = new Random(0xdeadbeef);
    private static final int DEFAULT_BOARD_SIZE = 6;
    private static final int DEFAULT_DEPTH_LIMIT = 20;
    private static final long TIME_BUFFER = 500;

    private int boardSize = DEFAULT_BOARD_SIZE;
    private long startTime = 0;
    private long timeLimit = 10 * 1000;

    public final static float valueOf2Pawns = 100.0f;
    public final static float valueOf3Pawns = 200.0f;
    public final static float valueOf4Pawns = 2000.0f;
    public final static float valueOf5Pawns = 9999999.0f;
    public final static float valueOfBlockedOpponent = 1000.0f;


    boolean timeout() {
        return System.currentTimeMillis() - startTime >= timeLimit;
    }

    PentagoMove randomMove(Board board, Color color) {
        List<Move> moves = board.getMovesFor(color);
        Move move = moves.get(random.nextInt(moves.size()));
        return new PentagoMove(move, score(board, color));
    }

    private float pawnsInRow(Board board, Color color) {
        float sum = 0.0f;
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (board.getState(i, j) == color
                        && board.getState(i, j + 1) == color) { //2 in a row
                    sum += valueOf2Pawns;
                    if (board.getState(i, j + 2) == color) {
                        sum += valueOf3Pawns;
                        if (board.getState(i, j + 3) == color) {
                            sum += valueOf4Pawns;
                            if (board.getState(i, j + 4) == color) sum += valueOf5Pawns;
                        }
                    }
                }
            }
        }
        return sum;
    }

    private float pawnsInColumn(Board board, Color color) {
        float sum = 0.0f;
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (board.getState(i, j) == color
                        && board.getState(i + 1, j) == color) { //2 in a row
                    sum += valueOf2Pawns;
                    if (board.getState(i + 2, j) == color) {
                        sum += valueOf3Pawns;
                        if (board.getState(i + 3, j) == color) {
                            sum += valueOf4Pawns;
                            if (board.getState(i + 4, j) == color) sum += valueOf5Pawns;
                        }
                    }
                }
            }
        }
        return sum;
    }

    private float blocksOpponentLine(Board board, Color color) {
        float sum = 0.0f;

        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (board.getState(i, j) == color) {
                    // Check if placing a pawn at this position blocks opponent's line
                    if (blocksLine(board, getOpponent(color), i, j)) {
                        sum += valueOfBlockedOpponent;
                    }
                }
            }
        }
        return sum;
    }

    private boolean blocksLine(Board board, Color opponentColor, int x, int y) {
        // Check if placing a pawn at (x, y) blocks opponent's line
        // This function checks for 3 or 4 in a row for the opponent

        // Horizontal
        int countHorizontal = countOpponentPawns(board, opponentColor, x, y, 1, 0)
                + countOpponentPawns(board, opponentColor, x, y, -1, 0);
        if (countHorizontal == 3 || countHorizontal == 4) {
            if (!isEmptySpace(board, x + 2, y) && !isEmptySpace(board, x - 2, y)) {
                return false;  // Jeśli obie strony są zajęte, to nie jest blokujące
            }
            return true;
        }

        // Vertical
        int countVertical = countOpponentPawns(board, opponentColor, x, y, 0, 1)
                + countOpponentPawns(board, opponentColor, x, y, 0, -1);
        if (countVertical == 3 || countVertical == 4) {
            if (!isEmptySpace(board, x, y + 2) && !isEmptySpace(board, x, y - 2)) {
                return false;  // Jeśli obie strony są zajęte, to nie jest blokujące
            }
            return true;
        }

        return false;
    }

    private boolean isEmptySpace(Board board, int x, int y) {
        // Sprawdza, czy dane pole na planszy jest puste
        return x >= 0 && x < board.getSize() && y >= 0 && y < board.getSize()
                && board.getState(x, y) == Color.EMPTY;
    }

    private int countOpponentPawns(Board board, Color opponentColor, int x, int y, int directionX, int directionY) {
        int count = 0;
        for (int i = 1; i <= 4; i++) {
            int currentX = x + i * directionX;
            int currentY = y + i * directionY;

            if (currentX >= 0 && currentX < board.getSize() && currentY >= 0 && currentY < board.getSize()) {
                if (board.getState(currentX, currentY) == opponentColor) {
                    count++;
                } else {
                    break; // Zatrzymaj liczenie w momencie, gdy natrafisz na pole gracza lub puste
                }
            } else {
                break; // Zatrzymaj liczenie, jeśli wychodzisz poza zakres planszy
            }
        }
        return count;
    }

//    double score(Board board, Color color) {
//        if (board.getWinner(color) == color) return Double.MAX_VALUE;
//        int[] rows = new int[boardSize];
//        int[] cols = new int[boardSize];
//        int[] opprows = new int[boardSize];
//        int[] oppcols = new int[boardSize];
//        for (int i = 0; i < boardSize; i++) {
//            for (int j = 0; j < boardSize; j++) {
//                if (board.getState(i, j) == color) {
//                    rows[i] += 1;
//                    cols[i] += 1;
//                } else if (board.getState(i, j) != Color.EMPTY) {
//                    opprows[i] += 1;
//                    oppcols[j] += 1;
//                }
//            }
//        }
//
//        double res = 0.0;
//        for (int i = 0; i < boardSize; i++) {
//            res += Math.pow(2, Math.min(rows[i], 5)) - 1;
//            res += Math.pow(2, Math.min(cols[i], 5)) - 1;
//            res -= Math.pow(2, opprows[i]) - 1;
//            res -= Math.pow(2, oppcols[i]) - 1;
//        }
//
//        return res;
//    }

    double score(Board board, Color color) {
        double res = 0.0f;

        res += pawnsInRow(board, color);
        res += pawnsInColumn(board, color);

        res -= pawnsInColumn(board, getOpponent(color));
        res -= pawnsInColumn(board, getOpponent(color));

        res += blocksOpponentLine(board, color);

        return res;
    }

    PentagoMove NegMax(Board board, Color color, int depth, double alpha, double beta) {
        Color nextColor = color == Color.PLAYER1 ? Color.PLAYER2 : Color.PLAYER1;
        List<Move> nextMoves = board.getMovesFor(color);
        Collections.shuffle(nextMoves);
        if (depth == 0 || nextMoves.isEmpty() || timeout())
            return new PentagoMove(null, score(board, color));

        PentagoMove best = null;
        for (Move move : nextMoves) {
            board.doMove(move);
            PentagoMove next = NegMax(board, nextColor, depth - 1, -beta, -alpha);
            board.undoMove(move);
            if (next.value == null) continue;
            next.value *= -1.0;
            next.move = move;
            if (best == null || next.value > best.value) best = next;
            alpha = Math.max(best.value, alpha);
            if (best.value >= beta) return best;
        }
        return best;
    }

    PentagoMove FindMove(Board board, Color color, int depthLimit) {
        PentagoMove best = randomMove(board, color);
        for (int depth = 1; depth <= depthLimit && !timeout(); depth++) {
            PentagoMove move = NegMax(board, color, depth, Double.MIN_VALUE, Double.MAX_VALUE);
            if (move.value > best.value) best = move;
        }

        System.out.println("("+best.value+")\t"+best.move);
        return best;
    }

    @Override
    public String getName() {
        return "Andrei Kartavik 153925 Cezary Szwedek 151920";
    }

    @Override
    public Move nextMove(Board board) {
        // give some extra time for returning the move
        boardSize = board.getSize();
        timeLimit = getTime() - TIME_BUFFER;
        startTime = System.currentTimeMillis();
        PentagoMove move = FindMove(board, getColor(), DEFAULT_DEPTH_LIMIT);
        board.doMove(move.move);
        return move.move;
    }

}