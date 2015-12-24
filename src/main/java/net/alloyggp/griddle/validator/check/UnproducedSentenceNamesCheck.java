package net.alloyggp.griddle.validator.check;

import java.util.HashSet;
import java.util.Set;

import net.alloyggp.griddle.grammar.GdlVisitor;
import net.alloyggp.griddle.grammar.Rule;
import net.alloyggp.griddle.grammar.Sentence;
import net.alloyggp.griddle.grammar.TopLevelGdl;
import net.alloyggp.griddle.validator.AnalyzedGame;

public class UnproducedSentenceNamesCheck implements Check {
    public static final UnproducedSentenceNamesCheck INSTANCE = new UnproducedSentenceNamesCheck();
    private UnproducedSentenceNamesCheck() {
        //Singleton
    }

    @Override
    public void findProblems(AnalyzedGame game, final ProblemReporter reporter) {
        final Set<String> producedSentenceNames = getProducedSentenceNames(game);
        for (Rule rule : game.getRules()) {
            rule.accept(new GdlVisitor() {
                @Override
                public void visitSentence(Sentence sentence) {
                    //The lower-case check is for keywords that might be capitalized.
                    if (!producedSentenceNames.contains(sentence.getName())
                            && !producedSentenceNames.contains(sentence.getName().toLowerCase())) {
                        reporter.report("Rule references a sentence with a name " + sentence.getName()
                                + " that is not produced by any rule or standalone sentence.",
                                sentence.getPosition());
                    }
                }
            });
        }
    }

    private Set<String> getProducedSentenceNames(AnalyzedGame game) {
        Set<String> results = new HashSet<String>();
        results.add("true");
        results.add("does");
        for (TopLevelGdl gdl : game.getTopLevelComponents()) {
            if (gdl.isSentence()) {
                results.add(gdl.getSentence().getName());
            } else if (gdl.isRule()) {
                results.add(gdl.getRule().getHead().getName());
            }
        }
        return results;
    }
}
