package put.ai.games.pentago.impl;

import put.ai.games.game.Move;
import put.ai.games.game.Player;
import put.ai.games.game.TypicalBoard;
import put.ai.games.game.moves.RotateMove;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PentagoBoard extends TypicalBoard {
    public PentagoBoard(int boardSize) {
        super(boardSize);
        if (boardSize % 2 != 0)
            throw new IllegalArgumentException("Board size must be even");
        int half = boardSize / 2;
        if (half % 2 != 1)
            throw new IllegalArgumentException("Half of the board size must be odd");
    }

    public PentagoBoard(PentagoBoard other) {
        super(other);
    }

    protected int nRequired() {
        int half = this.getSize() / 2;
        return half + (half + 1) / 2;
    }

    protected void rotate(int cx, int cy, RotateMove.Direction dir) {
        /*
        CLOCKWISE:
        012    630
        345 -> 741
        678    852
        x -> y
        y -> -x
        COUNTERCLOCKWISE:
        012    258
        345 -> 147
        678    036
        x -> -y
        y -> x
         */
        int half = getSize() / 2;
        int dx = (cx < half) ? 0 : half;
        int dy = (cy < half) ? 0 : half;
        Player.Color[][] tmp = new Player.Color[half][];
        for (int x = 0; x < half; ++x) {
            tmp[x] = Arrays.copyOfRange(state[dx + x], dy, dy + half);
        }
        for (int x = 0; x < half; ++x) {
            int sx = (dir == RotateMove.Direction.CLOCKWISE) ? (half - x - 1) : (x);
            for (int y = 0; y < half; ++y) {
                int sy = (dir == RotateMove.Direction.CLOCKWISE) ? (y) : (half - y - 1);
                state[dx + x][dy + y] = tmp[sy][sx];
            }
        }
    }

    @Override
    public void doMove(Move _m) {
        if (!(_m instanceof PentagoMove))
            throw new IllegalArgumentException();
        PentagoMove m = (PentagoMove) _m;
        if (state[m.getPlaceX()][m.getPlaceY()] != Player.Color.EMPTY)
            throw new IllegalArgumentException();
        state[m.getPlaceX()][m.getPlaceY()] = m.getColor();
        rotate(m.getRotateSrcX(), m.getRotateSrcY(), m.getDirection());
    }

    @Override
    public List<Move> getMovesFor(Player.Color color) {
        List<Move> result = new ArrayList<>();
        int half = this.getSize() / 2;
        int d = (half - 1) / 2;
        int[][] centers = new int[][]{
                {0, 0},
                {1, 0},
                {0, 1},
                {1, 1}
        };
        for (int x = 0; x < this.getSize(); ++x)
            for (int y = 0; y < this.getSize(); ++y) {
                if (state[x][y] == Player.Color.EMPTY) {
                    for (int[] c : centers) {
                        result.add(new PentagoMove(x, y, c[0] * half, c[1] * half, c[0] * half + half - 1, c[1] * half, color));
                        result.add(new PentagoMove(x, y, c[0] * half + half - 1, c[1] * half, c[0] * half, c[1] * half, color));
                    }
                }
            }
        return result;
    }

    @Override
    public void undoMove(Move _m) {
        if (!(_m instanceof PentagoMove))
            throw new IllegalArgumentException();
        PentagoMove m = (PentagoMove) _m;
        RotateMove.Direction opp = m.getDirection() == RotateMove.Direction.CLOCKWISE ? RotateMove.Direction.COUNTERCLOCKWISE : RotateMove.Direction.CLOCKWISE;
        rotate(m.getRotateSrcX(), m.getRotateSrcY(), opp);
        if (state[m.getPlaceX()][m.getPlaceY()] != m.getColor())
            throw new IllegalArgumentException();
        state[m.getPlaceX()][m.getPlaceY()] = Player.Color.EMPTY;

    }

    @Override
    public PentagoBoard clone() {
        return new PentagoBoard(this);
    }

    private boolean maybeWinner(Player.Color c) {
        for (int x = 0; x < getSize(); ++x) {
            int p = 0;
            for (int y = 0; y < getSize(); ++y) {
                if (state[x][y] == c) {
                    p++;
                    if (p >= nRequired())
                        return true;
                } else {
                    p = 0;
                    if (y + nRequired() >= getSize())
                        break;
                }
            }
        }
        for (int y = 0; y < getSize(); ++y) {
            int p = 0;
            for (int x = 0; x < getSize(); ++x) {
                if (state[x][y] == c) {
                    p++;
                    if (p >= nRequired())
                        return true;
                } else {
                    p = 0;
                    if (x + nRequired() >= getSize())
                        break;
                }
            }
        }
        return false;
    }

    @Override
    public Player.Color getWinner(Player.Color currentPlayer) {
        boolean w1 = maybeWinner(Player.Color.PLAYER1);
        boolean w2 = maybeWinner(Player.Color.PLAYER2);
        if (w1 && w2)
            return currentPlayer;
        if (w1)
            return Player.Color.PLAYER1;
        if (w2)
            return Player.Color.PLAYER2;
        return hasEmpty() ? null : Player.Color.EMPTY;
    }

    public boolean hasEmpty() {
        for (int x = 0; x < getSize(); ++x) {
            for (int y = 0; y < getSize(); ++y) {
                if (state[x][y] == Player.Color.EMPTY) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected boolean canMove(Player.Color color) {
        throw new UnsupportedOperationException();
    }
}