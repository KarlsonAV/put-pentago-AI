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

    boolean timeout() {
        return System.currentTimeMillis() - startTime >= timeLimit;
    }

    PentagoMove randomMove(Board board, Color color) {
        List<Move> moves = board.getMovesFor(color);
        Move move = moves.get(random.nextInt(moves.size()));
        return new PentagoMove(move, score(board, color));
    }

    double score(Board board, Color color) {
        if (board.getWinner(color) == color) return Double.MAX_VALUE;
        int[] rows = new int[boardSize];
        int[] cols = new int[boardSize];
        int[] opprows = new int[boardSize];
        int[] oppcols = new int[boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board.getState(i, j) == color) {
                    rows[i] += 1;
                    cols[i] += 1;
                } else if (board.getState(i, j) != Color.EMPTY) {
                    opprows[i] += 1;
                    oppcols[j] += 1;
                }
            }
        }

        double res = 0.0;
        for (int i = 0; i < boardSize; i++) {
            res += Math.pow(2, Math.min(rows[i], 5)) - 1;
            res += Math.pow(2, Math.min(cols[i], 5)) - 1;
            res -= Math.pow(2, opprows[i]) - 1;
            res -= Math.pow(2, oppcols[i]) - 1;
        }

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