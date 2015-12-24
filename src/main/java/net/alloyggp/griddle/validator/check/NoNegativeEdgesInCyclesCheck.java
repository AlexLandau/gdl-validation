package net.alloyggp.griddle.validator.check;

import net.alloyggp.griddle.grammar.GdlVisitor;
import net.alloyggp.griddle.grammar.Literal;
import net.alloyggp.griddle.grammar.Rule;
import net.alloyggp.griddle.grammar.Sentence;
import net.alloyggp.griddle.validator.AnalyzedGame;

public class NoNegativeEdgesInCyclesCheck implements Check {
    public static final NoNegativeEdgesInCyclesCheck INSTANCE = new NoNegativeEdgesInCyclesCheck();
    private NoNegativeEdgesInCyclesCheck() {
        //Singleton
    }

    @Override
    public void findProblems(final AnalyzedGame game, final ProblemReporter reporter) {
        for (Rule rule : game.getRules()) {
            final String ruleHeadName = rule.getHead().getName();
            rule.accept(new GdlVisitor() {
                @Override
                public void visitNegation(Literal negation) {
                    if (!negation.isSentence()) {
                        //This error case will be handled by the NegationContainsSentenceCheck
                        return;
                    }
                    Sentence sentence = negation.getSentence();
                    if (game.getSentenceNameAncestors(sentence.getName()).contains(ruleHeadName)) {
                        reporter.report("A negated sentence is in a cycle with the head of its rule.", negation.getPosition());
                    }
                }
            });
        }
    }

}
