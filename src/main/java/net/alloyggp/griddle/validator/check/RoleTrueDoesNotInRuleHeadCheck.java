package net.alloyggp.griddle.validator.check;

import net.alloyggp.griddle.grammar.GdlVisitor;
import net.alloyggp.griddle.grammar.Rule;
import net.alloyggp.griddle.validator.AnalyzedGame;

public class RoleTrueDoesNotInRuleHeadCheck implements Check {
    public static final RoleTrueDoesNotInRuleHeadCheck INSTANCE = new RoleTrueDoesNotInRuleHeadCheck();

    private RoleTrueDoesNotInRuleHeadCheck() {
        //Singleton
    }

    @Override
    public void findProblems(AnalyzedGame game, final ProblemReporter reporter) {
        game.visitAll(new GdlVisitor() {
            @Override
            public void visitRule(Rule rule) {
                if (rule.getHead().getName().equalsIgnoreCase("role")) {
                    reporter.report("Roles cannot be defined in rules. They must be defined in standalone sentences.",
                            rule.getHead().getPosition());
                }
                if (rule.getHead().getName().equalsIgnoreCase("true")) {
                    reporter.report("Sentences starting with 'true' are part of the game state and cannot be defined directly."
                            + " They are determined each turn according to the 'init' and 'next' sentences.",
                            rule.getHead().getPosition());
                }
                if (rule.getHead().getName().equalsIgnoreCase("does")) {
                    reporter.report("Sentences starting with 'does' are the moves chosen by each player and cannot be defined directly.",
                            rule.getHead().getPosition());
                }
            }
        });
    }

}
