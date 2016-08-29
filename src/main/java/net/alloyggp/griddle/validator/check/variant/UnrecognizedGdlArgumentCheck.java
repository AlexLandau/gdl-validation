package net.alloyggp.griddle.validator.check.variant;

import java.util.Arrays;
import java.util.List;

import net.alloyggp.griddle.grammar.Sentence;
import net.alloyggp.griddle.grammar.TopLevelGdl;
import net.alloyggp.griddle.validator.AnalyzedGame;
import net.alloyggp.griddle.validator.check.Check;
import net.alloyggp.griddle.validator.check.ProblemReporter;

public class UnrecognizedGdlArgumentCheck implements Check {
    public static final UnrecognizedGdlArgumentCheck INSTANCE = new UnrecognizedGdlArgumentCheck();
    private UnrecognizedGdlArgumentCheck() {
        //Singleton
    }

    private static final List<String> RECOGNIZED_ARGUMENTS = Arrays.asList(
            "random");

    @Override
    public void findProblems(AnalyzedGame game, ProblemReporter reporter) {
        gdlLoop: for (TopLevelGdl gdl : game.getTopLevelComponents()) {
            if (gdl.isSentence()) {
                Sentence sentence = gdl.getSentence();
                if (sentence.getBody().size() == 1) {
                    for (String possibleArgument : RECOGNIZED_ARGUMENTS) {
                        if (possibleArgument.equalsIgnoreCase(sentence.getBody().get(0).toString())) {
                            continue gdlLoop;
                        }
                    }
                    // Not found
                    reporter.report("Argument to the gdl keyword is not recognized by this validator. May not be able to determine if game rules are valid", sentence.getBody().get(0).getPosition());
                }
            }
        }
    }

}
