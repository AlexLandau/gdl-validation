package net.alloyggp.griddle.validator.check;

import java.util.HashSet;
import java.util.Set;

import net.alloyggp.griddle.grammar.Function;
import net.alloyggp.griddle.grammar.GdlVisitor;
import net.alloyggp.griddle.grammar.Sentence;
import net.alloyggp.griddle.validator.AnalyzedGame;

public class SentenceAndFunctionNamesDifferCheck implements Check {
    public static final SentenceAndFunctionNamesDifferCheck INSTANCE = new SentenceAndFunctionNamesDifferCheck();
    private SentenceAndFunctionNamesDifferCheck() {
        //Singleton
    }

    @Override
    public void findProblems(AnalyzedGame game, final ProblemReporter reporter) {
        final Set<String> functionNames = new HashSet<String>();
        game.visitAll(new GdlVisitor() {
            @Override
            public void visitFunction(Function function) {
                functionNames.add(function.getName());
            }
        });
        game.visitAll(new GdlVisitor() {
            @Override
            public void visitSentence(Sentence sentence) {
                if (functionNames.contains(sentence.getName())) {
                    reporter.report("The name " + sentence.getName() + " is being used in "
                            + "both functions and sentences, which may not be what you intended. "
                            + "(Are you using 'true' correctly?)", sentence.getNamePosition());
                }
            }
        });
    }

}
