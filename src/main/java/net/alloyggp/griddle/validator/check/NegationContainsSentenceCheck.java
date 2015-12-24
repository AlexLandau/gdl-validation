package net.alloyggp.griddle.validator.check;

import net.alloyggp.griddle.grammar.GdlVisitor;
import net.alloyggp.griddle.grammar.Literal;
import net.alloyggp.griddle.validator.AnalyzedGame;

public class NegationContainsSentenceCheck implements Check {
    public static final NegationContainsSentenceCheck INSTANCE = new NegationContainsSentenceCheck();
    private NegationContainsSentenceCheck() {
        //Singleton
    }

    @Override
    public void findProblems(AnalyzedGame game, final ProblemReporter reporter) {
        game.visitAll(new GdlVisitor() {
            @Override
            public void visitNegation(Literal negation) {
                Literal negatedLiteral = negation.getNegation();
                if (!negatedLiteral.isSentence()) {
                    reporter.report("Only sentences may be negated. Negation may not be applied to disjunctions, distinct clauses, or other negations.",
                            negation.getPosition());
                }
            }
        });
    }
}
