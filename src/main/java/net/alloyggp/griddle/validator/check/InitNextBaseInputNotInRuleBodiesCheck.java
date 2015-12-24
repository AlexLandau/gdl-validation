package net.alloyggp.griddle.validator.check;

import net.alloyggp.griddle.grammar.GdlVisitor;
import net.alloyggp.griddle.grammar.Literal;
import net.alloyggp.griddle.grammar.Rule;
import net.alloyggp.griddle.grammar.Sentence;
import net.alloyggp.griddle.validator.AnalyzedGame;

public class InitNextBaseInputNotInRuleBodiesCheck implements Check {
    public static final InitNextBaseInputNotInRuleBodiesCheck INSTANCE = new InitNextBaseInputNotInRuleBodiesCheck();
    private InitNextBaseInputNotInRuleBodiesCheck() {
        //Singleton
    }

    @Override
    public void findProblems(AnalyzedGame game, final ProblemReporter reporter) {
        for (Rule rule : game.getRules()) {
            for (Literal conjunct : rule.getConjuncts()) {
                conjunct.accept(new GdlVisitor() {
                    @Override
                    public void visitSentence(Sentence sentence) {
                        if (isRelevantKeyword(sentence.getName())) {
                            reporter.report("Sentences with the keyword " + sentence.getName()
                                    + " should not be in the bodies of rules.", sentence.getPosition());
                        }
                    }
                });
            }
        }
    }

    protected boolean isRelevantKeyword(String name) {
        return name.equalsIgnoreCase("init")
                || name.equalsIgnoreCase("next")
                || name.equalsIgnoreCase("base")
                || name.equalsIgnoreCase("input");
    }
}
