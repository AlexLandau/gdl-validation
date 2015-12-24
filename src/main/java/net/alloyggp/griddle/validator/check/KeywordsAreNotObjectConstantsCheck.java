package net.alloyggp.griddle.validator.check;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.alloyggp.griddle.grammar.Function;
import net.alloyggp.griddle.grammar.GdlVisitor;
import net.alloyggp.griddle.grammar.Sentence;
import net.alloyggp.griddle.grammar.Term;
import net.alloyggp.griddle.validator.AnalyzedGame;

public class KeywordsAreNotObjectConstantsCheck implements Check {
    public static final KeywordsAreNotObjectConstantsCheck INSTANCE = new KeywordsAreNotObjectConstantsCheck();
    private KeywordsAreNotObjectConstantsCheck() {
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
                for (Term term : function.getBody()) {
                    if (term.isConstant() && isKeyword(term.getConstantName())) {
                        reporter.report("The keyword " + term.getUserFriendlyString() + " should only be used "
                                + "as a sentence name.",
                                term.getPosition());
                    }
                }
            }

            @Override
            public void visitSentence(Sentence sentence) {
                for (Term term : sentence.getBody()) {
                    if (term.isConstant() && isKeyword(term.getConstantName())) {
                        reporter.report("The keyword " + term.getUserFriendlyString() + " should only be used "
                                + "as a sentence name.",
                                term.getPosition());
                    }
                }
            }

            private boolean isKeyword(String constant) {
                return SENTENCE_NAME_KEYWORDS.contains(constant.toLowerCase());
            }
        });
    }
}
