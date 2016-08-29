package net.alloyggp.griddle.validator.check.variant.random;

import net.alloyggp.griddle.grammar.Rule;
import net.alloyggp.griddle.grammar.Sentence;
import net.alloyggp.griddle.grammar.TopLevelGdl;
import net.alloyggp.griddle.validator.AnalyzedGame;
import net.alloyggp.griddle.validator.check.Check;
import net.alloyggp.griddle.validator.check.ProblemReporter;

public class GdlRandomGoalCheck implements Check {
    public static final GdlRandomGoalCheck INSTANCE = new GdlRandomGoalCheck();
    private GdlRandomGoalCheck() {
        //Singleton
    }

    @Override
    public void findProblems(AnalyzedGame game, ProblemReporter reporter) {
        if (game.getGdlVariants().contains("random")) {
            //Look for goal definitions that set to something other than 100, or are in rules
            boolean goalDefinitionFound = false;
            for (TopLevelGdl gdl : game.getTopLevelComponents()) {
                if (gdl.isSentence()) {
                    Sentence sentence = gdl.getSentence();
                    if (sentence.getName().equalsIgnoreCase("goal")
                            && sentence.getBody().size() == 2
                            && sentence.getBody().get(0).toString().equalsIgnoreCase("random")) {
                        if (sentence.getBody().get(1).toString().equalsIgnoreCase("100")) {
                        } else {
                            reporter.report("Goal values for the random player should be 100", sentence.getPosition());
                        }
                    }
                } else if (gdl.isRule()) {
                    Rule rule = gdl.getRule();
                    if (rule.getHead().getName().equalsIgnoreCase("goal")
                            && rule.getHead().getBody().size() == 2
                            && rule.getHead().getBody().get(0).toString().equalsIgnoreCase("random")) {
                        reporter.report("Goal values for the random player should always be 100 and defined in a standalone sentence, not a rule", rule.getPosition());
                    }
                }
            }

            if (!goalDefinitionFound) {
                reporter.report("The random player's goal value should be set to 100", null);
            }
        }
    }
}
