package net.alloyggp.griddle.validator.check;

import java.util.HashSet;
import java.util.Set;

import net.alloyggp.griddle.grammar.GdlVisitor;
import net.alloyggp.griddle.grammar.Literal;
import net.alloyggp.griddle.grammar.Rule;
import net.alloyggp.griddle.grammar.Sentence;
import net.alloyggp.griddle.grammar.TopLevelGdl;
import net.alloyggp.griddle.validator.AnalyzedGame;

public class UnusedSentenceNamesCheck implements Check {
    public static final UnusedSentenceNamesCheck INSTANCE = new UnusedSentenceNamesCheck();
    private UnusedSentenceNamesCheck() {
        //Singleton
    }

    @Override
    public void findProblems(AnalyzedGame game, ProblemReporter reporter) {
        Set<String> usedSentenceNames = getUsedSentenceNames(game);
        for (TopLevelGdl gdl : game.getTopLevelComponents()) {
            if (gdl.isSentence()) {
                Sentence sentence = gdl.getSentence();
                //We do the to-lower-case check in case keywords are used in a
                //non-lower-case way.
                if (!usedSentenceNames.contains(sentence.getName())
                        && !usedSentenceNames.contains(sentence.getName().toLowerCase())) {
                    reporter.report("The sentence name " + sentence.getName() + " is defined, "
                            + "but is not used anywhere.", sentence.getPosition());
                }
            } else if (gdl.isRule()) {
                Rule rule = gdl.getRule();
                if (!usedSentenceNames.contains(rule.getHead().getName())
                        && !usedSentenceNames.contains(rule.getHead().getName().toLowerCase())) {
                    reporter.report("The sentence name " + rule.getHead().getName() + " is defined, "
                            + "but is not used anywhere.", rule.getHead().getPosition());
                }
            }
        }
    }

    private Set<String> getUsedSentenceNames(AnalyzedGame game) {
        final Set<String> usedNames = new HashSet<String>();
        usedNames.add("role");
        usedNames.add("true");
        usedNames.add("next");
        usedNames.add("legal");
        usedNames.add("does");
        usedNames.add("terminal");
        usedNames.add("goal");
        usedNames.add("init");
        usedNames.add("base");
        usedNames.add("input");

        for (Rule rule : game.getRules()) {
            for (Literal conjunct : rule.getConjuncts()) {
                conjunct.accept(new GdlVisitor() {
                    @Override
                    public void visitSentence(Sentence sentence) {
                        usedNames.add(sentence.getName());
                    }
                });
            }
        }
        return usedNames;
    }
}
