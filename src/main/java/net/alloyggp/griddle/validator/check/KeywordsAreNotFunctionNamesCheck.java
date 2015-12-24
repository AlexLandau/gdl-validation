package net.alloyggp.griddle.validator.check;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.alloyggp.griddle.grammar.Function;
import net.alloyggp.griddle.grammar.GdlVisitor;
import net.alloyggp.griddle.validator.AnalyzedGame;

public class KeywordsAreNotFunctionNamesCheck implements Check {
    public static final KeywordsAreNotFunctionNamesCheck INSTANCE = new KeywordsAreNotFunctionNamesCheck();
    private KeywordsAreNotFunctionNamesCheck() {
        //Singleton
    }

    private static final Set<String> SENTENCE_NAME_KEYWORDS = Collections.unmodifiableSet(
            new HashSet<String>(Arrays.asList(
                    "true",
                    "init",
                    "next",
                    "legal",
                    "does",
                    "role",
                    "terminal",
                    "goal",
                    "base",
                    "input"
                    )));

    @Override
    public void findProblems(AnalyzedGame game, final ProblemReporter reporter) {
        game.visitAll(new GdlVisitor() {
            @Override
            public void visitFunction(Function function) {
                if (isKeyword(function.getName())) {
                    reporter.report("The keyword " + function.getName() + " should only be used "
                            + "as a sentence name.",
                            function.getNamePosition());
                }
            }

            private boolean isKeyword(String constant) {
                return SENTENCE_NAME_KEYWORDS.contains(constant.toLowerCase());
            }
        });
    }
}
