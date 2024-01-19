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
    public final static float valueOf5Pawns = 99999999999999999999.0f;
    public final static float valueOfBlockedOpponent = 1050.0f;

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
                children.add(new Tree(board, getOpponent(color), move)); //created new board with move
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

        private float blockOpponent(Board board, Color opponentColor) {
            float penalty = 0.0f;

            for (int i = 0; i < board.getSize(); i++) {
                for (int j = 0; j < board.getSize() - 4; j++) {
                    if (board.getState(i, j) == opponentColor
                            && board.getState(i, j + 1) == opponentColor
                            && board.getState(i, j + 2) == opponentColor
                            && board.getState(i, j + 3) == opponentColor
                            && board.getState(i, j + 4) == Color.EMPTY) {
                        penalty += valueOfBlockedOpponent * (5 - countOpponentPawns(board, opponentColor, i, j, i, j + 4));
                    }

                    if (board.getState(j, i) == opponentColor
                            && board.getState(j + 1, i) == opponentColor
                            && board.getState(j + 2, i) == opponentColor
                            && board.getState(j + 3, i) == opponentColor
                            && board.getState(j + 4, i) == Color.EMPTY) {
                        penalty += valueOfBlockedOpponent * (5 - countOpponentPawns(board, opponentColor, j, i, j + 4, i));
                    }
                }
            }

            return penalty;
        }

        private int countOpponentPawns(Board board, Color opponentColor, int startX, int startY, int endX, int endY) {
            int count = 0;
            for (int i = startX; i <= endX; i++) {
                for (int j = startY; j <= endY; j++) {
                    if (board.getState(i, j) == opponentColor) {
                        count++;
                    }
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

            boardHeuristicValue.heuristicValue -= blockOpponent(board, Color.PLAYER2);

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
                    System.out.println("Player max: " + maximizingPlayer + " Depth: " + depth + " Value: " + valueOfChildren.heuristicValue);
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

        Move checkWinMove = CheckWin(b, playerColor);
        if (checkWinMove != null) {
            System.out.println("Win with CheckWinMove!");
            return checkWinMove;
        }

        // MinMax with Alpha-Beta pruning
        maximizingPlayer = false;
        if (playerColor.equals(Color.PLAYER1)) {
            maximizingPlayer = true;
        }

        Tree.Heuristic alpha = new Tree.Heuristic(Float.NEGATIVE_INFINITY, null);
        Tree.Heuristic beta = new Tree.Heuristic(Float.POSITIVE_INFINITY, null);

        Tree currentGameTree = new Tree(b, playerColor);

        Tree.Heuristic result = MinMaxAlfaBeta(currentGameTree, DEPTH, alpha, beta, maximizingPlayer);
        System.out.println(result.heuristicValue);
        System.out.println(playerColor);
        return result.move;
    }
}
