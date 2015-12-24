package net.alloyggp.griddle.validator.check;

import java.util.HashMap;
import java.util.Map;

import net.alloyggp.griddle.Position;
import net.alloyggp.griddle.grammar.GdlVisitor;
import net.alloyggp.griddle.grammar.Rule;
import net.alloyggp.griddle.validator.AnalyzedGame;

public class InconsistentCapitalizationCheck implements Check {
    public static final InconsistentCapitalizationCheck INSTANCE = new InconsistentCapitalizationCheck();

    private InconsistentCapitalizationCheck() {
        //Singleton
    }

    @Override
    public void findProblems(AnalyzedGame game, final ProblemReporter reporter) {
        final Map<String, String> preferredCapitalization = new HashMap<String, String>();
        game.visitAll(new GdlVisitor() {
            @Override
            public void visitConstant(String constant, Position position) {
                String lowercase = constant.toLowerCase();
                if (!preferredCapitalization.containsKey(lowercase)) {
                    preferredCapitalization.put(lowercase, constant);
                } else {
                    String existingCase = preferredCapitalization.get(lowercase);
                    if (!constant.equals(existingCase)) {
                        reporter.report("Inconsistent capitalization used for constant: "
                                + existingCase + " vs. " + constant + ". This "
                                + "will create inconsistencies between case-sensitive and "
                                + "case-insensitive gamers.", position);
                    }
                }
            }
            @Override
            public void visitRule(Rule rule) {
                checkVariablesInRule(rule, reporter);
            }
        });
    }

    protected void checkVariablesInRule(Rule rule, final ProblemReporter reporter) {
        final Map<String, String> preferredCapitalization = new HashMap<String, String>();
        rule.accept(new GdlVisitor() {
            @Override
            public void visitVariable(String variable, Position position) {
                String lowercase = variable.toLowerCase();
                if (!preferredCapitalization.containsKey(lowercase)) {
                    preferredCapitalization.put(lowercase, variable);
                } else {
                    String existingCase = preferredCapitalization.get(lowercase);
                    if (!variable.equals(existingCase)) {
                        reporter.report("Inconsistent capitalization used for variable within rule: "
                                + existingCase + " vs. " + variable + ". This "
                                + "will create inconsistencies between case-sensitive and "
                                + "case-insensitive gamers.", position);
                    }
                }
            }
        });
    }

}
