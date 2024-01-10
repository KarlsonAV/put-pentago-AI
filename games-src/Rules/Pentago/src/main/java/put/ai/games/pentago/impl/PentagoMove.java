package put.ai.games.pentago.impl;

import put.ai.games.game.Player;
import put.ai.games.game.moves.PlaceAndRotateMove;
import put.ai.games.game.moves.RotateMove;

import java.util.Objects;

public class PentagoMove implements PlaceAndRotateMove {


    private final int placeX;
    private final int placeY;
    private final int rotateSrcX;
    private final int rotateSrcY;
    private final int rotateDstX;
    private final int rotateDstY;
    private final Player.Color color;
    private final RotateMove.Direction direction;

    public PentagoMove(int placeX, int placeY, int rotateSrcX, int rotateSrcY, int rotateDstX, int rotateDstY, Player.Color color) {
        if (color == null)
            throw new NullPointerException();
        this.placeX = placeX;
        this.placeY = placeY;
        this.rotateSrcX = rotateSrcX;
        this.rotateSrcY = rotateSrcY;
        this.rotateDstX = rotateDstX;
        this.rotateDstY = rotateDstY;

        this.color = color;

        int a, b;
        if (rotateSrcX == rotateDstX) {
            a = rotateSrcY;
            b = rotateDstY;
        } else if (rotateSrcY == rotateDstY) {
            a = rotateSrcX;
            b = rotateDstX;
        } else {
            throw new IllegalArgumentException();
        }
        if (a > b)
            this.direction = RotateMove.Direction.COUNTERCLOCKWISE;
        else if (a < b)
            this.direction = RotateMove.Direction.CLOCKWISE;
        else
            throw new IllegalArgumentException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PentagoMove that = (PentagoMove) o;
        return placeX == that.placeX &&
                placeY == that.placeY &&
                rotateSrcX == that.rotateSrcX &&
                rotateSrcY == that.rotateSrcY &&
                rotateDstX == that.rotateDstX &&
                rotateDstY == that.rotateDstY &&
                color == that.color;
    }

    public RotateMove.Direction getDirection() {
        return direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeX, placeY, rotateSrcX, rotateSrcY, rotateDstX, rotateDstY, color);
    }

    @Override
    public String toString() {
        return String.format("Place@(%d, %d), rotate@(%d, %d)->(%d, %d) %s", getPlaceX(), getPlaceY(), getRotateSrcX(), getRotateSrcY(), getRotateDstX(), getRotateDstY(), getDirection());
    }

    @Override
    public int getPlaceX() {
        return placeX;
    }

    @Override
    public int getPlaceY() {
        return placeY;
    }

    @Override
    public int getRotateSrcX() {
        return rotateSrcX;
    }

    @Override
    public int getRotateSrcY() {
        return rotateSrcY;
    }

    @Override
    public int getRotateDstX() {
        return rotateDstX;
    }

    @Override
    public int getRotateDstY() {
        return rotateDstY;
    }

    @Override
    public Player.Color getColor() {
        return color;
    }
}
