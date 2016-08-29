package net.alloyggp.griddle.validator.check.variant;

import net.alloyggp.griddle.grammar.Sentence;
import net.alloyggp.griddle.grammar.TopLevelGdl;
import net.alloyggp.griddle.validator.AnalyzedGame;
import net.alloyggp.griddle.validator.check.Check;
import net.alloyggp.griddle.validator.check.ProblemReporter;

public class GdlArityCheck implements Check {
    public static final GdlArityCheck INSTANCE = new GdlArityCheck();
    private GdlArityCheck() {
        //Singleton
    }

    @Override
    public void findProblems(AnalyzedGame game, ProblemReporter reporter) {
        for (TopLevelGdl gdl : game.getTopLevelComponents()) {
            if (gdl.isSentence()) {
                Sentence sentence = gdl.getSentence();
                if (sentence.getBody().size() != 1) {
                    reporter.report("A GDL sentence should have exactly one term.", sentence.getPosition());
                }
            }
        }
    }

}
