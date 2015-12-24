package net.alloyggp.griddle.validator.check;

import java.util.HashMap;
import java.util.Map;

import net.alloyggp.griddle.grammar.Function;
import net.alloyggp.griddle.grammar.GdlVisitor;
import net.alloyggp.griddle.grammar.Sentence;
import net.alloyggp.griddle.validator.AnalyzedGame;

public class FixedArityCheck implements Check {
    public static final FixedArityCheck INSTANCE = new FixedArityCheck();
    private FixedArityCheck() {
        //Singleton
    }

    @Override
    public void findProblems(AnalyzedGame game, final ProblemReporter reporter) {
        final Map<String, Integer> sentenceArities = new HashMap<String, Integer>();
        final Map<String, Integer> functionArities = new HashMap<String, Integer>();
        game.visitAll(new GdlVisitor() {
            @Override
            public void visitFunction(Function function) {
                String name = function.getName();
                if (!functionArities.containsKey(name)) {
                    functionArities.put(name, function.getBody().size());
                } else {
                    int expectedArity = functionArities.get(name);
                    int arity = function.getBody().size();
                    if (arity != expectedArity) {
                        reporter.report("Functions with the same name should have the same "
                                + "arity. The name " + name + " was previously used with "
                                + "arity " + expectedArity + " but here has arity " + arity + ".",
                                function.getPosition());
                    }
                }
            }
            @Override
            public void visitSentence(Sentence sentence) {
                String name = sentence.getName();
                if (!sentenceArities.containsKey(name)) {
                    sentenceArities.put(name, sentence.getBody().size());
                } else {
                    int expectedArity = sentenceArities.get(name);
                    int arity = sentence.getBody().size();
                    if (arity != expectedArity) {
                        reporter.report("Sentences with the same name should have the same "
                                + "arity. The name " + name + " was previously used with "
                                + "arity " + expectedArity + " but here has arity " + arity + ".",
                                sentence.getPosition());
                    }
                }
            }
        });
    }

}
