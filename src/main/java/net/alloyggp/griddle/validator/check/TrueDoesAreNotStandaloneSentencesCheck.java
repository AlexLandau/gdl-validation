package net.alloyggp.griddle.validator.check;

import net.alloyggp.griddle.grammar.Sentence;
import net.alloyggp.griddle.grammar.TopLevelGdl;
import net.alloyggp.griddle.validator.AnalyzedGame;

public class TrueDoesAreNotStandaloneSentencesCheck implements Check {
    public static final TrueDoesAreNotStandaloneSentencesCheck INSTANCE = new TrueDoesAreNotStandaloneSentencesCheck();

    private TrueDoesAreNotStandaloneSentencesCheck() {
        //Singleton
    }

    @Override
    public void findProblems(AnalyzedGame game, final ProblemReporter reporter) {
        for (TopLevelGdl gdl : game.getTopLevelComponents()) {
            if (gdl.isSentence()) {
                Sentence sentence = gdl.getSentence();
                if (sentence.getName().equalsIgnoreCase("true")) {
                    reporter.report("Sentences starting with 'true' are part of the game state and cannot be defined directly."
                            + " They are determined each turn according to the 'init' and 'next' sentences.", sentence.getPosition());
                } else if (sentence.getName().equalsIgnoreCase("does")) {
                    reporter.report("Sentences starting with 'does' are the moves chosen by each player and cannot be defined directly.",
                            sentence.getPosition());
                }
            }
        }
    }

}
