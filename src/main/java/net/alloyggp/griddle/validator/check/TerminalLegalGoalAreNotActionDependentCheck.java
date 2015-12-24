package net.alloyggp.griddle.validator.check;

import net.alloyggp.griddle.grammar.GdlVisitor;
import net.alloyggp.griddle.grammar.Literal;
import net.alloyggp.griddle.grammar.Rule;
import net.alloyggp.griddle.grammar.Sentence;
import net.alloyggp.griddle.validator.AnalyzedGame;

public class TerminalLegalGoalAreNotActionDependentCheck implements Check {
    public static final TerminalLegalGoalAreNotActionDependentCheck INSTANCE = new TerminalLegalGoalAreNotActionDependentCheck();
    private TerminalLegalGoalAreNotActionDependentCheck() {
        //Singleton
    }

    @Override
    public void findProblems(final AnalyzedGame game, final ProblemReporter reporter) {
        for (final Rule rule : game.getRules()) {
            if (isRelevantKeyword(rule.getHead().getName())) {
                for (Literal conjunct : rule.getConjuncts()) {
                    conjunct.accept(new GdlVisitor() {
                        @Override
                        public void visitSentence(Sentence sentence) {
                            if (game.isActionDependent(sentence.getName())) {
                                reporter.report("Sentences starting with the keyword " + rule.getHead().getName()
                                        + " should not depend on 'does' sentences or other sentences derived from them.",
                                        sentence.getPosition());
                            }
                        }
                    });
                }
            }
        }
    }

    private boolean isRelevantKeyword(String name) {
        return name.equalsIgnoreCase("terminal")
                || name.equalsIgnoreCase("legal")
                || name.equalsIgnoreCase("goal");
    }
}
