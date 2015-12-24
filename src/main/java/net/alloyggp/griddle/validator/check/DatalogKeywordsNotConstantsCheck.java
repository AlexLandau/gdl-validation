package net.alloyggp.griddle.validator.check;

import net.alloyggp.griddle.Position;
import net.alloyggp.griddle.grammar.GdlVisitor;
import net.alloyggp.griddle.validator.AnalyzedGame;

//Note: The current parser probably makes this irrelevant
public class DatalogKeywordsNotConstantsCheck implements Check {
    public static final DatalogKeywordsNotConstantsCheck INSTANCE = new DatalogKeywordsNotConstantsCheck();

    private DatalogKeywordsNotConstantsCheck() {
        //Singleton
    }

    @Override
    public void findProblems(AnalyzedGame game, final ProblemReporter reporter) {
        game.visitAll(new GdlVisitor() {
            @Override
            public void visitConstant(String constant, Position position) {
                if (constant.equalsIgnoreCase("<=")
                        || constant.equalsIgnoreCase("distinct")
                        || constant.equalsIgnoreCase("or")
                        || constant.equalsIgnoreCase("not")) {
                    reporter.report(constant + " is a reserved Datalog keyword, and appears to be"
                            + " used as a constant here. Check your syntax.", position);
                }
            }
        });
    }

}
