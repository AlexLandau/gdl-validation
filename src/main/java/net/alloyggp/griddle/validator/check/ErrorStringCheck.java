package net.alloyggp.griddle.validator.check;

import net.alloyggp.griddle.Position;
import net.alloyggp.griddle.grammar.GdlVisitor;
import net.alloyggp.griddle.validator.AnalyzedGame;

public class ErrorStringCheck implements Check {
    public static final ErrorStringCheck INSTANCE = new ErrorStringCheck();
    private ErrorStringCheck() {
        //Singleton
    }

    @Override
    public void findProblems(AnalyzedGame game, final ProblemReporter reporter) {
        game.visitAll(new GdlVisitor() {
            @Override
            public void visitErrorString(String errorString, Position position) {
                reporter.report("Could not parse GDL", position);
            }
        });
    }

}
