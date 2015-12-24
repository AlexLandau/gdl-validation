package net.alloyggp.griddle.validator.check;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.alloyggp.griddle.grammar.GdlVisitor;
import net.alloyggp.griddle.grammar.Sentence;
import net.alloyggp.griddle.validator.AnalyzedGame;

public class KeywordArityCheck implements Check {
    public static final KeywordArityCheck INSTANCE = new KeywordArityCheck();
    private KeywordArityCheck() {
        //Singleton
    }

    private static final Map<String, Integer> KEYWORD_ARITIES;
    static {
        Map<String, Integer> keywordArities = new HashMap<String, Integer>();
        keywordArities.put("true", 1);
        keywordArities.put("init", 1);
        keywordArities.put("next", 1);
        keywordArities.put("legal", 2);
        keywordArities.put("does", 2);
        keywordArities.put("role", 1);
        keywordArities.put("terminal", 0);
        keywordArities.put("goal", 2);
        keywordArities.put("base", 1);
        keywordArities.put("input", 2);
        KEYWORD_ARITIES = Collections.unmodifiableMap(keywordArities);
    }

    @Override
    public void findProblems(AnalyzedGame game, final ProblemReporter reporter) {
        game.visitAll(new GdlVisitor() {
            @Override
            public void visitSentence(Sentence sentence) {
                String lowerCaseName = sentence.getName().toLowerCase();
                if (KEYWORD_ARITIES.containsKey(lowerCaseName)) {
                    int expectedArity = KEYWORD_ARITIES.get(lowerCaseName);
                    int arity = sentence.getBody().size();
                    if (expectedArity != arity) {
                        reporter.report("Sentences that begin with the keyword " + lowerCaseName
                                + " must contain exactly " + expectedArity + " terms.", sentence.getPosition());
                    }
                }
            }
        });
    }
}
