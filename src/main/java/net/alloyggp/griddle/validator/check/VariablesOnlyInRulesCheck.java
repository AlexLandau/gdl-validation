package net.alloyggp.griddle.validator.check;

import net.alloyggp.griddle.Position;
import net.alloyggp.griddle.grammar.GdlVisitor;
import net.alloyggp.griddle.grammar.Sentence;
import net.alloyggp.griddle.grammar.TopLevelGdl;
import net.alloyggp.griddle.validator.AnalyzedGame;

public class VariablesOnlyInRulesCheck implements Check {
    public static final VariablesOnlyInRulesCheck INSTANCE = new VariablesOnlyInRulesCheck();
    private VariablesOnlyInRulesCheck() {
        //Singleton
    }

    @Override
    public void findProblems(AnalyzedGame game, final ProblemReporter reporter) {
        for (TopLevelGdl gdl : game.getTopLevelComponents()) {
            if (gdl.isSentence()) {
                Sentence sentence = gdl.getSentence();
                sentence.accept(new GdlVisitor() {
                    @Override
                    public void visitVariable(String variable, Position position) {
                        reporter.report("Variables may only appear within rules.", position);
                    }
                });
            }
        }
    }

}
