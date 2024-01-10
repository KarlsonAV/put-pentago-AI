package put.ai.games.pentago.impl;

import org.junit.jupiter.api.Test;
import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;
import put.ai.games.game.moves.RotateMove;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PentagoBoardTest {

    @Test
    public void getMovesNoUsed() {
        PentagoBoard board = new PentagoBoard(6);
        board.doMove(new PentagoMove(0, 0, 3, 3, 5, 3, Player.Color.PLAYER1));
        List<Move> moves = board.getMovesFor(Player.Color.PLAYER1);
        for (Move _m : moves) {
            PentagoMove m = (PentagoMove) _m;
            assertTrue(m.getPlaceX() != 0 || m.getPlaceY() != 0);
        }
    }

    @Test
    public void getMovesOnEmptyBoard() {
        PentagoBoard board = new PentagoBoard(6);
        List<Move> moves = board.getMovesFor(Player.Color.PLAYER1);
        assertEquals(6 * 6 * 4 * 2, moves.size());
    }

    @Test
    public void rotateULcw() {
        PentagoBoard board = new PentagoBoard(6);
        board.doMove(new PentagoMove(0, 0, 3, 3, 5, 3, Player.Color.PLAYER1));
        check(board,
                "1_____\n" +
                        "______\n" +
                        "______\n" +
                        "______\n" +
                        "______\n" +
                        "______\n"
        );
        board.doMove(new PentagoMove(2, 0, 3, 3, 5, 3, Player.Color.PLAYER2));
        check(board,
                "1_2___\n" +
                        "______\n" +
                        "______\n" +
                        "______\n" +
                        "______\n" +
                        "______\n"
        );
        board.doMove(new PentagoMove(5, 5, 0, 0, 2, 0, Player.Color.PLAYER1));
        check(board,
                "__1___\n" +
                        "______\n" +
                        "__2___\n" +
                        "______\n" +
                        "______\n" +
                        "_____1\n"
        );
        board.doMove(new PentagoMove(2, 1, 0, 0, 2, 0, Player.Color.PLAYER2));
        check(board,
                "______\n" +
                        "______\n" +
                        "221___\n" +
                        "______\n" +
                        "______\n" +
                        "_____1\n"
        );
        board.doMove(new PentagoMove(0, 1, 0, 0, 2, 0, Player.Color.PLAYER1));
        check(board,
                "21____\n" +
                        "2_____\n" +
                        "1_____\n" +
                        "______\n" +
                        "______\n" +
                        "_____1\n"
        );
    }

    @Test
    public void rotateULccw() {
        PentagoBoard board = new PentagoBoard(6);
        board.doMove(new PentagoMove(0, 0, 5, 5, 3, 5, Player.Color.PLAYER1));
        check(board,
                "1_____\n" +
                        "______\n" +
                        "______\n" +
                        "______\n" +
                        "______\n" +
                        "______\n"
        );
        board.doMove(new PentagoMove(2, 0, 5, 5, 3, 5, Player.Color.PLAYER2));
        check(board,
                "1_2___\n" +
                        "______\n" +
                        "______\n" +
                        "______\n" +
                        "______\n" +
                        "______\n"
        );
        board.doMove(new PentagoMove(5, 5, 2, 0, 0, 0, Player.Color.PLAYER1));
        check(board,
                "2_____\n" +
                        "______\n" +
                        "1_____\n" +
                        "______\n" +
                        "______\n" +
                        "_____1\n"
        );
        board.doMove(new PentagoMove(2, 1, 2, 0, 0, 0, Player.Color.PLAYER2));
        check(board,
                "_2____\n" +
                        "______\n" +
                        "2_1___\n" +
                        "______\n" +
                        "______\n" +
                        "_____1\n"
        );
        board.doMove(new PentagoMove(0, 1, 2, 0, 0, 0, Player.Color.PLAYER1));
        check(board,
                "__1___\n" +
                        "2_____\n" +
                        "_12___\n" +
                        "______\n" +
                        "______\n" +
                        "_____1\n"
        );
    }

    @Test
    public void rotateURcw() {
        PentagoBoard board = new PentagoBoard(6);
        board.doMove(new PentagoMove(3, 0, 3, 5, 5, 5, Player.Color.PLAYER1));
        check(board,
                "___1__\n" +
                        "______\n" +
                        "______\n" +
                        "______\n" +
                        "______\n" +
                        "______\n"
        );
        board.doMove(new PentagoMove(5, 0, 3, 5, 5, 5, Player.Color.PLAYER2));
        check(board,
                "___1_2\n" +
                        "______\n" +
                        "______\n" +
                        "______\n" +
                        "______\n" +
                        "______\n"
        );
        board.doMove(new PentagoMove(5, 5, 3, 1, 4, 1, Player.Color.PLAYER1));
        check(board,
                "_____1\n" +
                        "______\n" +
                        "_____2\n" +
                        "______\n" +
                        "______\n" +
                        "_____1\n"
        );
        board.doMove(new PentagoMove(5, 1, 3, 1, 4, 1, Player.Color.PLAYER2));
        check(board,
                "______\n" +
                        "______\n" +
                        "___221\n" +
                        "______\n" +
                        "______\n" +
                        "_____1\n"
        );
        board.doMove(new PentagoMove(3, 1, 3, 1, 4, 1, Player.Color.PLAYER1));
        check(board,
                "___21_\n" +
                        "___2__\n" +
                        "___1__\n" +
                        "______\n" +
                        "______\n" +
                        "_____1\n"
        );
    }

    @Test
    public void rotateURccw() {
        PentagoBoard board = new PentagoBoard(6);
        board.doMove(new PentagoMove(3, 0, 5, 5, 3, 5, Player.Color.PLAYER1));
        check(board,
                "___1__\n" +
                        "______\n" +
                        "______\n" +
                        "______\n" +
                        "______\n" +
                        "______\n"
        );
        board.doMove(new PentagoMove(5, 0, 5, 5, 3, 5, Player.Color.PLAYER2));
        check(board,
                "___1_2\n" +
                        "______\n" +
                        "______\n" +
                        "______\n" +
                        "______\n" +
                        "______\n"
        );
        board.doMove(new PentagoMove(5, 5, 4, 1, 3, 1, Player.Color.PLAYER1));
        check(board,
                "___2__\n" +
                        "______\n" +
                        "___1__\n" +
                        "______\n" +
                        "______\n" +
                        "_____1\n"
        );
        board.doMove(new PentagoMove(5, 1, 4, 1, 3, 1, Player.Color.PLAYER2));
        check(board,
                "____2_\n" +
                        "______\n" +
                        "___2_1\n" +
                        "______\n" +
                        "______\n" +
                        "_____1\n"
        );
        board.doMove(new PentagoMove(3, 1, 4, 1, 3, 1, Player.Color.PLAYER1));
        check(board,
                "_____1\n" +
                        "___2__\n" +
                        "____12\n" +
                        "______\n" +
                        "______\n" +
                        "_____1\n"
        );
    }

    @Test
    public void rotateLLcw() {
        PentagoBoard board = new PentagoBoard(6);
        board.doMove(new PentagoMove(0, 3, 3, 5, 5, 5, Player.Color.PLAYER1));
        check(board,
                "______\n" +
                        "______\n" +
                        "______\n" +
                        "1_____\n" +
                        "______\n" +
                        "______\n"

        );
        board.doMove(new PentagoMove(2, 3, 3, 5, 5, 5, Player.Color.PLAYER2));
        check(board,
                "______\n" +
                        "______\n" +
                        "______\n" +
                        "1_2___\n" +
                        "______\n" +
                        "______\n"
        );
        board.doMove(new PentagoMove(5, 5, 1, 4, 2, 4, Player.Color.PLAYER1));
        check(board,
                "______\n" +
                        "______\n" +
                        "______\n" +
                        "__1___\n" +
                        "______\n" +
                        "__2__1\n"
        );
        board.doMove(new PentagoMove(2, 4, 1, 4, 2, 4, Player.Color.PLAYER2));
        check(board,
                "______\n" +
                        "______\n" +
                        "______\n" +
                        "______\n" +
                        "______\n" +
                        "221__1\n"
        );
        board.doMove(new PentagoMove(0, 4, 1, 4, 2, 4, Player.Color.PLAYER1));
        check(board,
                "______\n" +
                        "______\n" +
                        "______\n" +
                        "21____\n" +
                        "2_____\n" +
                        "1____1\n"
        );
    }

    @Test
    public void rotateLLccw() {
        PentagoBoard board = new PentagoBoard(6);
        board.doMove(new PentagoMove(0, 3, 5, 5, 3, 5, Player.Color.PLAYER1));
        check(board,
                "______\n" +
                        "______\n" +
                        "______\n" +
                        "1_____\n" +
                        "______\n" +
                        "______\n"
        );
        board.doMove(new PentagoMove(2, 3, 5, 5, 3, 5, Player.Color.PLAYER2));
        check(board,
                "______\n" +
                        "______\n" +
                        "______\n" +
                        "1_2___\n" +
                        "______\n" +
                        "______\n"
        );
        board.doMove(new PentagoMove(5, 5, 1, 4, 0, 4, Player.Color.PLAYER1));
        check(board,
                "______\n" +
                        "______\n" +
                        "______\n" +
                        "2_____\n" +
                        "______\n" +
                        "1____1\n"
        );
        board.doMove(new PentagoMove(2, 4, 1, 4, 0, 4, Player.Color.PLAYER2));
        check(board,
                "______\n" +
                        "______\n" +
                        "______\n" +
                        "_2____\n" +
                        "______\n" +
                        "2_1__1\n"
        );
        board.doMove(new PentagoMove(0, 4, 1, 4, 0, 4, Player.Color.PLAYER1));
        check(board,
                "______\n" +
                        "______\n" +
                        "______\n" +
                        "__1___\n" +
                        "2_____\n" +
                        "_12__1\n"
        );
    }

    @Test
    public void rotateLRcw() {
        PentagoBoard board = new PentagoBoard(6);
        board.doMove(new PentagoMove(3, 3, 3, 2, 5, 2, Player.Color.PLAYER1));
        check(board,
                "______\n" +
                        "______\n" +
                        "______\n" +
                        "___1__\n" +
                        "______\n" +
                        "______\n"
        );
        board.doMove(new PentagoMove(5, 3, 3, 2, 5, 2, Player.Color.PLAYER2));
        check(board,
                "______\n" +
                        "______\n" +
                        "______\n" +
                        "___1_2\n" +
                        "______\n" +
                        "______\n"
        );
        board.doMove(new PentagoMove(5, 2, 3, 4, 4, 4, Player.Color.PLAYER1));
        check(board,
                "______\n" +
                        "______\n" +
                        "_____1\n" +
                        "_____1\n" +
                        "______\n" +
                        "_____2\n"
        );
        board.doMove(new PentagoMove(5, 4, 3, 4, 4, 4, Player.Color.PLAYER2));
        check(board,
                "______\n" +
                        "______\n" +
                        "_____1\n" +
                        "______\n" +
                        "______\n" +
                        "___221\n"
        );
        board.doMove(new PentagoMove(3, 4, 3, 4, 4, 4, Player.Color.PLAYER1));
        check(board,
                "______\n" +
                        "______\n" +
                        "_____1\n" +
                        "___21_\n" +
                        "___2__\n" +
                        "___1__\n"
        );
    }

    @Test
    public void rotateLRccw() {
        PentagoBoard board = new PentagoBoard(6);
        board.doMove(new PentagoMove(3, 3, 5, 2, 3, 2, Player.Color.PLAYER1));
        check(board,
                "______\n" +
                        "______\n" +
                        "______\n" +
                        "___1__\n" +
                        "______\n" +
                        "______\n"
        );
        board.doMove(new PentagoMove(5, 3, 5, 2, 3, 2, Player.Color.PLAYER2));
        check(board,
                "______\n" +
                        "______\n" +
                        "______\n" +
                        "___1_2\n" +
                        "______\n" +
                        "______\n"
        );
        board.doMove(new PentagoMove(5, 2, 4, 4, 3, 4, Player.Color.PLAYER1));
        check(board,
                "______\n" +
                        "______\n" +
                        "_____1\n" +
                        "___2__\n" +
                        "______\n" +
                        "___1__\n"
        );
        board.doMove(new PentagoMove(5, 4, 4, 4, 3, 4, Player.Color.PLAYER2));
        check(board,
                "______\n" +
                        "______\n" +
                        "_____1\n" +
                        "____2_\n" +
                        "______\n" +
                        "___2_1\n"
        );
        board.doMove(new PentagoMove(3, 4, 4, 4, 3, 4, Player.Color.PLAYER1));
        check(board,
                "______\n" +
                        "______\n" +
                        "_____1\n" +
                        "_____1\n" +
                        "___2__\n" +
                        "____12\n"
        );
    }

    @Test
    public void undoMove() {
        PentagoBoard board = new PentagoBoard(6);
        board.doMove(new PentagoMove(3, 3, 5, 2, 3, 2, Player.Color.PLAYER1));
        check(board,
                "______\n" +
                        "______\n" +
                        "______\n" +
                        "___1__\n" +
                        "______\n" +
                        "______\n"
        );
        PentagoMove m = new PentagoMove(5, 3, 5, 2, 3, 2, Player.Color.PLAYER2);
        board.doMove(m);
        check(board,
                "______\n" +
                        "______\n" +
                        "______\n" +
                        "___1_2\n" +
                        "______\n" +
                        "______\n"
        );
        board.undoMove(m);
        check(board,
                "______\n" +
                        "______\n" +
                        "______\n" +
                        "___1__\n" +
                        "______\n" +
                        "______\n"
        );
    }

    @Test
    public void cloneTest() {
        PentagoBoard board1 = new PentagoBoard(6);
        board1.doMove(new PentagoMove(3, 3, 5, 2, 3, 2, Player.Color.PLAYER1));
        check(board1,
                "______\n" +
                        "______\n" +
                        "______\n" +
                        "___1__\n" +
                        "______\n" +
                        "______\n"
        );
        PentagoBoard board2 = board1.clone();
        board1.doMove(new PentagoMove(5, 3, 5, 2, 3, 2, Player.Color.PLAYER2));
        check(board1,
                "______\n" +
                        "______\n" +
                        "______\n" +
                        "___1_2\n" +
                        "______\n" +
                        "______\n"
        );
        check(board2,
                "______\n" +
                        "______\n" +
                        "______\n" +
                        "___1__\n" +
                        "______\n" +
                        "______\n"
        );
    }

    @Test
    public void noWinner() {
        PentagoBoard board = new PentagoBoard(6);
        assertNull(board.getWinner(Player.Color.PLAYER1));
        assertNull(board.getWinner(Player.Color.PLAYER2));
    }

    @Test
    public void winner1() {
        PentagoBoard board = new PentagoBoard(6);
        for (int x = 0; x < 5; ++x) {
            assertNull(board.getWinner(Player.Color.PLAYER1));
            assertNull(board.getWinner(Player.Color.PLAYER2));
            board.doMove(new PentagoMove(x, 0, 3, 3, 5, 3, Player.Color.PLAYER1));
            board.doMove(new PentagoMove(x % 3, 4 + x / 3, 3, 3, 5, 3, Player.Color.PLAYER2));
        }
        assertEquals(Player.Color.PLAYER1, board.getWinner(Player.Color.PLAYER1));
        assertEquals(Player.Color.PLAYER1, board.getWinner(Player.Color.PLAYER2));
    }

    @Test
    public void winner2() {
        PentagoBoard board = new PentagoBoard(6);
        for (int x = 0; x < 5; ++x) {
            assertNull(board.getWinner(Player.Color.PLAYER1));
            assertNull(board.getWinner(Player.Color.PLAYER2));
            board.doMove(new PentagoMove(x % 3, 4 + x / 3, 3, 3, 5, 3, Player.Color.PLAYER1));
            board.doMove(new PentagoMove(x, 0, 3, 3, 5, 3, Player.Color.PLAYER2));
        }
        assertEquals(Player.Color.PLAYER2, board.getWinner(Player.Color.PLAYER1));
        assertEquals(Player.Color.PLAYER2, board.getWinner(Player.Color.PLAYER2));
    }

    @Test
    public void winnerVertical() {
        PentagoBoard board = new PentagoBoard(6);
        for (int y = 0; y < 5; ++y) {
            assertNull(board.getWinner(Player.Color.PLAYER1));
            assertNull(board.getWinner(Player.Color.PLAYER2));
            board.doMove(new PentagoMove(4 + y / 3, y % 3, 3, 3, 5, 3, Player.Color.PLAYER1));
            board.doMove(new PentagoMove(0, y, 3, 3, 5, 3, Player.Color.PLAYER2));
        }
        assertEquals(Player.Color.PLAYER2, board.getWinner(Player.Color.PLAYER1));
        assertEquals(Player.Color.PLAYER2, board.getWinner(Player.Color.PLAYER2));
    }

    @Test
    public void noWinnerDiagonal() {
        PentagoBoard board = new PentagoBoard(6);
        for (int y = 0; y < 5; ++y) {
            assertNull(board.getWinner(Player.Color.PLAYER1));
            assertNull(board.getWinner(Player.Color.PLAYER2));
            board.doMove(new PentagoMove(y, y, 3, 0, 5, 0, Player.Color.PLAYER1));
        }
        assertNull(board.getWinner(Player.Color.PLAYER1));
        assertNull(board.getWinner(Player.Color.PLAYER2));
    }

    @Test
    public void winnerNonConsecutive() {
        PentagoBoard board = new PentagoBoard(6);
        for (int x = 0; x < 5; ++x) {
            assertNull(board.getWinner(Player.Color.PLAYER1));
            assertNull(board.getWinner(Player.Color.PLAYER2));
            board.doMove(new PentagoMove(x + x / 3, 0, 3, 3, 5, 3, Player.Color.PLAYER1));
        }
        assertNull(board.getWinner(Player.Color.PLAYER1));
        assertNull(board.getWinner(Player.Color.PLAYER2));
    }

    private void check(Board board, String expected) {
        assertEquals(expected, board.toString());
    }

    @Test
    public void nRequiredTest() {
        assertEquals(5, new PentagoBoard(6).nRequired());
        assertEquals(8, new PentagoBoard(10).nRequired());
        assertEquals(11 + 6, new PentagoBoard(22).nRequired());
    }

    @Test
    public void winnerNonConsecutive2() {
        PentagoBoard board = new PentagoBoard(6);
        board.doMove(new PentagoMove(0, 0, 3, 3, 5, 3, Player.Color.PLAYER1));
        board.doMove(new PentagoMove(1, 0, 3, 3, 5, 3, Player.Color.PLAYER1));
        board.doMove(new PentagoMove(2, 0, 3, 3, 5, 3, Player.Color.PLAYER1));
        board.doMove(new PentagoMove(4, 0, 3, 3, 5, 3, Player.Color.PLAYER1));
        board.doMove(new PentagoMove(5, 0, 3, 3, 5, 3, Player.Color.PLAYER1));
        assertNull(board.getWinner(Player.Color.PLAYER1));
        assertNull(board.getWinner(Player.Color.PLAYER2));
    }

}