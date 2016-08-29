package net.alloyggp.griddle.validator.check.variant;

import net.alloyggp.griddle.grammar.Rule;
import net.alloyggp.griddle.validator.AnalyzedGame;
import net.alloyggp.griddle.validator.check.Check;
import net.alloyggp.griddle.validator.check.ProblemReporter;

public class GdlNotInRuleHeadCheck implements Check {
    public static final GdlNotInRuleHeadCheck INSTANCE = new GdlNotInRuleHeadCheck();
    private GdlNotInRuleHeadCheck() {
        //Singleton
    }

    @Override
    public void findProblems(AnalyzedGame game, ProblemReporter reporter) {
        for (Rule rule : game.getRules()) {
            if (rule.getHead().getName().equalsIgnoreCase("gdl")) {
                reporter.report("The gdl keyword should not be used in the head of a rule", rule.getPosition());
            }
        }
    }
}
