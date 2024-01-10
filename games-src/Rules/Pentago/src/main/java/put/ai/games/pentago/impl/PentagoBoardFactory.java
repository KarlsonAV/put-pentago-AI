package put.ai.games.pentago.impl;

import put.ai.games.engine.UniversalBoardFactory;
import put.ai.games.game.Board;

public class PentagoBoardFactory extends UniversalBoardFactory {
    public PentagoBoardFactory() throws NoSuchMethodException {
        super(PentagoBoard.class, "Pentago", "https://pl.wikipedia.org/w/index.php?title=Pentago&oldid=54278761");
    }
}
