package net.alloyggp.griddle.validator.check;

import net.alloyggp.griddle.grammar.GdlVisitor;
import net.alloyggp.griddle.grammar.Literal;
import net.alloyggp.griddle.validator.AnalyzedGame;

public class DisjunctionNotNestedCheck implements Check {
    public static final DisjunctionNotNestedCheck INSTANCE = new DisjunctionNotNestedCheck();
    private DisjunctionNotNestedCheck() {
        //Singleton
    }

    @Override
    public void findProblems(AnalyzedGame game, final ProblemReporter reporter) {
        game.visitAll(new GdlVisitor() {
            @Override
            public void visitDisjunction(Literal disjunction) {
                for (Literal disjunct : disjunction.getDisjunction()) {
                    if (disjunct.isDisjunction()) {
                        reporter.report("Disjunctions should not be nested. This is unnecessary and "
                                + "can interfere with game analysis, masking other errors in the game rules.",
                                disjunct.getPosition());
                    }
                }
            }
        });
    }

}
