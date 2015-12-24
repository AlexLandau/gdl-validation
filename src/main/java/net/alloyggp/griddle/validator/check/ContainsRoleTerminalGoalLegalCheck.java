package net.alloyggp.griddle.validator.check;

import net.alloyggp.griddle.grammar.TopLevelGdl;
import net.alloyggp.griddle.validator.AnalyzedGame;

public class ContainsRoleTerminalGoalLegalCheck implements Check {
    public static final ContainsRoleTerminalGoalLegalCheck INSTANCE = new ContainsRoleTerminalGoalLegalCheck();
    private ContainsRoleTerminalGoalLegalCheck() {
        //Singleton
    }

    @Override
    public void findProblems(AnalyzedGame game, ProblemReporter reporter) {
        boolean containsTerminal = false;
        boolean containsRole = false;
        boolean containsGoal = false;
        boolean containsLegal = false;
        for (TopLevelGdl gdl : game.getTopLevelComponents()) {
            if (gdl.isSentence()) {
                String sentenceName = gdl.getSentence().getName();
                if (sentenceName.equalsIgnoreCase("terminal")) {
                    containsTerminal = true;
                } else if (sentenceName.equalsIgnoreCase("role")) {
                    containsRole = true;
                } else if (sentenceName.equalsIgnoreCase("goal")) {
                    containsGoal = true;
                } else if (sentenceName.equalsIgnoreCase("legal")) {
                    containsLegal = true;
                }
            } else if (gdl.isRule()) {
                String headName = gdl.getRule().getHead().getName();
                if (headName.equalsIgnoreCase("terminal")) {
                    containsTerminal = true;
                } else if (headName.equalsIgnoreCase("role")) {
                    containsRole = true;
                } else if (headName.equalsIgnoreCase("goal")) {
                    containsGoal = true;
                } else if (headName.equalsIgnoreCase("legal")) {
                    containsLegal = true;
                }
            }
        }
        if (!containsTerminal) {
            reporter.report("The game does not have any rules for termination.", null);
        }
        if (!containsRole) {
            reporter.report("The game does not define any roles.", null);
        }
        if (!containsGoal) {
            reporter.report("The game does not define any goals for any players.", null);
        }
        if (!containsLegal) {
            reporter.report("The game does not define any legal moves.", null);
        }
    }
}
