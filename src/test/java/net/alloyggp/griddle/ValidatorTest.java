package net.alloyggp.griddle;

import java.util.HashSet;
import java.util.Set;

import net.alloyggp.griddle.validator.AnalyzedGame;
import net.alloyggp.griddle.validator.ParenthesesValidator;
import net.alloyggp.griddle.validator.check.Check;
import net.alloyggp.griddle.validator.check.DatalogKeywordsNotConstantsCheck;
import net.alloyggp.griddle.validator.check.ErrorStringCheck;
import net.alloyggp.griddle.validator.check.ProblemReporter;

import org.junit.Assert;
import org.junit.Test;

public class ValidatorTest extends Assert {
    @Test
    public void testParensMatching() throws Exception {
        String gameString = TestGames.getGameString("ticTacToe");
        Set<GdlProblem> problems = ParenthesesValidator.INSTANCE.findProblems(gameString);
        assertTrue(problems.isEmpty());
    }

    @Test
    public void testParensMatching1() throws Exception {
        String gameString = TestGames.getGameString("badparens1");
        Set<GdlProblem> problems = ParenthesesValidator.INSTANCE.findProblems(gameString);
        assertTrue(problems.size() == 1);
        GdlProblem problem = problems.iterator().next();
        assertTrue(problem.isError());
        assertEquals(4, problem.getPosition().getStart());
        assertEquals(5, problem.getPosition().getEnd());
    }

    @Test
    public void testParensMatching2() throws Exception {
        String gameString = TestGames.getGameString("badparens2");
        Set<GdlProblem> problems = ParenthesesValidator.INSTANCE.findProblems(gameString);
        assertTrue(problems.size() == 1);
        GdlProblem problem = problems.iterator().next();
        assertTrue(problem.isError());
        assertEquals(8, problem.getPosition().getStart());
        assertEquals(9, problem.getPosition().getEnd());
    }

    @Test
    public void testParensMatching3() throws Exception {
        String gameString = TestGames.getGameString("badparens3");
        Set<GdlProblem> problems = ParenthesesValidator.INSTANCE.findProblems(gameString);
        assertTrue(problems.size() == 1);
        GdlProblem problem = problems.iterator().next();
        assertTrue(problem.isError());
        //Unfortunately, Git repo checkouts may vary in local line endings
        if (gameString.contains("\r")) {
            //Windows-style whitespace
            assertEquals(17, problem.getPosition().getStart());
            assertEquals(18, problem.getPosition().getEnd());
        } else {
            //Unix-style whitespace
            assertEquals(16, problem.getPosition().getStart());
            assertEquals(17, problem.getPosition().getEnd());
        }
    }

    @Test
    public void testConsecutiveOpenParens4() throws Exception {
        String gameString = TestGames.getGameString("badparens4");
        Set<GdlProblem> problems = ParenthesesValidator.INSTANCE.findProblems(gameString);
        assertTrue(problems.size() == 1);
        GdlProblem problem = problems.iterator().next();
        assertTrue(problem.isError());
        assertEquals(3, problem.getPosition().getStart());
        assertEquals(4, problem.getPosition().getEnd());
    }

    @Test
    public void testConsecutiveOpenParens5() throws Exception {
        String gameString = TestGames.getGameString("badparens5");
        Set<GdlProblem> problems = ParenthesesValidator.INSTANCE.findProblems(gameString);
        assertTrue(problems.size() == 1);
        GdlProblem problem = problems.iterator().next();
        assertTrue(problem.isError());
        assertEquals(2, problem.getPosition().getStart());
        assertEquals(3, problem.getPosition().getEnd());
    }

    @Test
    public void testMisplacedDatalogKeyword1() throws Exception {
        String gameString = TestGames.getGameString("misplacedDatalogKeyword1");
        Set<GdlProblem> problems = findProblems(ErrorStringCheck.INSTANCE, gameString);
        assertTrue(problems.size() == 1);
        GdlProblem problem = problems.iterator().next();
        assertTrue(problem.isError());
        assertEquals(0, problem.getPosition().getStart());
        assertEquals(18, problem.getPosition().getEnd());
    }

    @Test
    public void testMisplacedDatalogKeyword2() throws Exception {
        String gameString = TestGames.getGameString("misplacedDatalogKeyword2");
        Set<GdlProblem> problems = findProblems(DatalogKeywordsNotConstantsCheck.INSTANCE, gameString);
        assertTrue(problems.size() == 1);
        GdlProblem problem = problems.iterator().next();
        assertTrue(problem.isError());
        assertEquals(5, problem.getPosition().getStart());
        assertEquals(8, problem.getPosition().getEnd());
    }

    @Test
    public void testMisplacedDatalogKeyword3() throws Exception {
        String gameString = TestGames.getGameString("misplacedDatalogKeyword3");
        Set<GdlProblem> problems = findProblems(DatalogKeywordsNotConstantsCheck.INSTANCE, gameString);
        assertTrue(problems.size() == 1);
        GdlProblem problem = problems.iterator().next();
        assertTrue(problem.isError());
        assertEquals(6, problem.getPosition().getStart());
        assertEquals(9, problem.getPosition().getEnd());
    }

    private Set<GdlProblem> findProblems(Check check, String gameString) throws Exception {
        AnalyzedGame game = AnalyzedGame.parseAndAnalyze(gameString);
        final Set<GdlProblem> problems = new HashSet<GdlProblem>();
        check.findProblems(game, new ProblemReporter() {
            @Override
            public void report(String message, Position position) {
                problems.add(GdlProblem.createError(message, position));
            }
        });
        return problems;
    }


}
