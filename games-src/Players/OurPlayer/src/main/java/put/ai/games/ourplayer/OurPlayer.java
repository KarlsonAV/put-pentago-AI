/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.ourplayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;

public class OurPlayer extends Player {

    public final static int DEPTH = 3;
    public final static float valueOf2Pawns = 100.0f;
    public final static float valueOf3Pawns = 200.0f;
    public final static float valueOf4Pawns = 2000.0f;
    public final static float valueOf5Pawns = 9999999.0f;
    public final static float valueOfBlockedOpponent = 1000.0f;

    private Random random = new Random(0xdeadbeef);


    //board and tree of boards
    public static class Tree {
        static public class Heuristic {
            public float heuristicValue;
            public Move move;

            public Heuristic() {}

            public Heuristic(float heuristicValue, Move move) {
                this.heuristicValue = heuristicValue;
                this.move = move;
            }
        }


        private Board board;
        private ArrayList<Tree> children;
        private Color color;
        public Heuristic boardHeuristicValue;

        //creates actual game
        public Tree(Board board, Color color) {
            children = new ArrayList<>();
            this.board = board;
            this.color = color;
        }

        //creates actual game with move that made it
        public Tree(Board board, Color color, Move move) {
            children = new ArrayList<>();
            this.board = board;
            this.color = color;
            boardHeuristicValue = new Heuristic();
            boardHeuristicValue.move = move;
        }

        public ArrayList<Tree> getChildren() {
            return children;
        }

        public void makeChildren() {
            List <Move> possibleMoves = board.getMovesFor(color); //all possible moves
            for (Move move: possibleMoves) {
                board.doMove(move);
                children.add(new Tree(board.clone(), getOpponent(color), move)); //created new board with move
                board.undoMove(move); //don't do this move yet
            }
        }

        public boolean hasChildren() {
            return children.size() != 0;
        }

        //Adding heuristic value if something happens
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



        private Heuristic getBoardHeuristicValue() {
            boardHeuristicValue.heuristicValue = 0.0f;

            boardHeuristicValue.heuristicValue += pawnsInRow(board, Color.PLAYER1);
            boardHeuristicValue.heuristicValue += pawnsInColumn(board, Color.PLAYER1);

            boardHeuristicValue.heuristicValue -= pawnsInRow(board, Color.PLAYER2);
            boardHeuristicValue.heuristicValue -= pawnsInColumn(board, Color.PLAYER2);

            boardHeuristicValue.heuristicValue += blocksOpponentLine(board, Color.PLAYER1);

            return boardHeuristicValue;
        }

        public static Heuristic max(Tree.Heuristic a, Tree.Heuristic b) {
            if (a.heuristicValue >= b.heuristicValue) {
                return a;
            }
            return b;
        }

        public static Heuristic min(Tree.Heuristic a, Tree.Heuristic b) {
            if (a.heuristicValue <= b.heuristicValue) {
                return a;
            }
            return b;
        }

    }

    public Tree.Heuristic MinMaxAlfaBeta(Tree gameTree, int depth, Tree.Heuristic alpha, Tree.Heuristic beta, boolean maximizingPlayer) {
        if (depth == 0) {
            return gameTree.getBoardHeuristicValue();
        }

        gameTree.makeChildren();
        if (!gameTree.hasChildren()) {
            return gameTree.getBoardHeuristicValue();
        }

        ArrayList<Tree> children = gameTree.getChildren();

        // MAX
        if (maximizingPlayer) {
            for (Tree child: children) {
                Tree.Heuristic valueOfChildren = MinMaxAlfaBeta(child, depth-1, alpha, beta, false);
                //System.out.println("Player max: " + maximizingPlayer + " Depth: " + depth + " Value: " + valueOfChildren.heuristicValue);
                alpha = Tree.max(valueOfChildren, alpha);
                System.out.println("Alpha: " + alpha.heuristicValue);
                if (alpha.heuristicValue >= beta.heuristicValue) {
                    return beta;
                }
            }
            return alpha;
        }
        // MIN
        else {
            for (Tree child: children) {
                Tree.Heuristic valueOfChildren = MinMaxAlfaBeta(child, depth - 1, alpha, beta, true);
                System.out.println("Player max: " + maximizingPlayer + " Depth: " + depth + " Value: " + valueOfChildren.heuristicValue);
                beta = Tree.min(valueOfChildren, beta);
                System.out.println("Beta: " + beta.heuristicValue);
                if (alpha.heuristicValue >= beta.heuristicValue) {
                    return alpha;
                }
            }
            return beta;
        }

    }

    public Move CheckWin(Board board, Color playerColor) {
        List<Move> possibleMoves = board.getMovesFor(playerColor);
        for (Move move: possibleMoves) {
            board.doMove(move);
            if (playerColor.equals(board.getWinner(playerColor))) {
                board.undoMove(move);
                return move;
            }
            board.undoMove(move);
        }
        System.out.println("Nie ma wygranej!");
        return null;
    }

    @Override
    public String getName() {
        return "Andrei Kartavik 153925 Cezary Szwedek 151920";
    }

    @Override
    public Move nextMove(Board b) {
        boolean maximizingPlayer;
        Color playerColor = getColor();

        Move checkWinMove = CheckWin(b.clone(), playerColor);
        if (checkWinMove != null) {
            System.out.println("Win with CheckWinMove!");
            return checkWinMove;
        }

        // MinMax with Alpha-Beta pruning
        maximizingPlayer = true;
        if (playerColor.equals(Color.PLAYER1)) {
            maximizingPlayer = true;
        }

        Tree.Heuristic alpha = new Tree.Heuristic(Float.NEGATIVE_INFINITY, null);
        Tree.Heuristic beta = new Tree.Heuristic(Float.POSITIVE_INFINITY, null);

        Tree currentGameTree = new Tree(b, playerColor);

        Tree.Heuristic result = null;
        try {
            result = MinMaxAlfaBeta(currentGameTree, DEPTH, alpha, beta, maximizingPlayer);
        } catch (Exception e) {
            result = null;
            System.out.println("GOT EXCEPTION " + e);
        }

        System.out.println(result.heuristicValue);
        System.out.println(playerColor);
        System.out.println(result.move);
        System.out.println(result.move.getColor());
        return result.move;
    }
}
