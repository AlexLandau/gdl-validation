package net.alloyggp.griddle.validator.check;

import net.alloyggp.griddle.grammar.GdlVisitor;
import net.alloyggp.griddle.grammar.Literal;
import net.alloyggp.griddle.validator.AnalyzedGame;

public class EmptyDisjunctionCheck implements Check {
    public static final EmptyDisjunctionCheck INSTANCE = new EmptyDisjunctionCheck();
    private EmptyDisjunctionCheck() {
        //Singleton
    }

    @Override
    public void findProblems(AnalyzedGame game, final ProblemReporter reporter) {
        game.visitAll(new GdlVisitor() {
            @Override
            public void visitDisjunction(Literal disjunction) {
                if (disjunction.getDisjunction().isEmpty()) {
                    reporter.report("Disjunctions should not be empty.", disjunction.getPosition());
                }
            }
        });
    }
}
