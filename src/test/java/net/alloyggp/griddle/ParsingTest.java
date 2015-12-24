package net.alloyggp.griddle;

import java.io.FileNotFoundException;
import java.util.List;

import net.alloyggp.griddle.grammar.TopLevelGdl;
import net.alloyggp.griddle.validator.AnalyzedGame;
import net.alloyggp.griddle.validator.ConfigurableValidator;
import net.alloyggp.griddle.validator.Validators;

import org.junit.Assert;
import org.junit.Test;

public class ParsingTest extends Assert {
    @Test
    public void testParsing() throws Exception {
        //This just tests if these can run without throwing exceptions.
        TestGames.getParsedGame("ticTacToe");

        String gameString = TestGames.getGameString("ticTacToe");
        AnalyzedGame.parseAndAnalyze(gameString);

        ConfigurableValidator validator = ConfigurableValidator.create(Validators.getStandardConfiguration());
        validator.findProblems(gameString);
    }

    //Regression test for bug in 0.1.1
    @Test
    public void testCommentAtEof() throws FileNotFoundException, Exception {
        List<TopLevelGdl> game = TestGames.getParsedGame("commentEof");
        assertEquals(0, game.size());
    }
}
