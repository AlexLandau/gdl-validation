package net.alloyggp.griddle.validator.check;

import java.util.Set;

import net.alloyggp.griddle.grammar.GdlVisitor;
import net.alloyggp.griddle.grammar.Literal;
import net.alloyggp.griddle.grammar.Rule;
import net.alloyggp.griddle.grammar.Sentence;
import net.alloyggp.griddle.validator.AnalyzedGame;

public class InitBaseInputAreConstantCheck implements Check {
    public static final InitBaseInputAreConstantCheck INSTANCE = new InitBaseInputAreConstantCheck();
    private InitBaseInputAreConstantCheck() {
        //Singleton
    }

    @Override
    public void findProblems(final AnalyzedGame game, final ProblemReporter reporter) {
        for (final Rule rule : game.getRules()) {
            if (isConstantKeyword(rule.getHead().getName())) {
                for (Literal conjunct : rule.getConjuncts()) {
                    conjunct.accept(new GdlVisitor() {
                        @Override
                        public void visitSentence(Sentence sentence) {
                            if (game.isStateDependent(sentence.getName())) {
                                reporter.report("Sentences starting with the " + rule.getHead().getName()
                                        + " keyword should not depend on the current state of the game.", sentence.getPosition());
                            }
                            if (game.isActionDependent(sentence.getName())) {
                                reporter.report("Sentences starting with the " + rule.getHead().getName()
                                        + " keyword should not depend on the moves being made.", sentence.getPosition());
                            }
                        }
                    });
                }
            }
        }
    }

    protected boolean containsIgnoreCase(Set<String> set, String string) {
        for (String candidate : set) {
            if (candidate.equalsIgnoreCase(string)) {
                return true;
            }
        }
        return false;
    }

    private boolean isConstantKeyword(String name) {
        //Role is handled elsewhere
        return name.equalsIgnoreCase("init")
                || name.equalsIgnoreCase("base")
                || name.equalsIgnoreCase("input");
    }

}
