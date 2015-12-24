package net.alloyggp.griddle.validator.check;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.alloyggp.griddle.Position;
import net.alloyggp.griddle.grammar.GdlVisitable;
import net.alloyggp.griddle.grammar.GdlVisitor;
import net.alloyggp.griddle.grammar.Literal;
import net.alloyggp.griddle.grammar.Rule;
import net.alloyggp.griddle.grammar.Sentence;
import net.alloyggp.griddle.validator.AnalyzedGame;

public class RuleSafetyCheck implements Check {
    public static final RuleSafetyCheck INSTANCE = new RuleSafetyCheck();
    private RuleSafetyCheck() {
        //Singleton
    }

    @Override
    public void findProblems(AnalyzedGame game, final ProblemReporter reporter) {
        for (Rule rule : game.getRules()) {
            final Set<String> supportedVariables = getSupportedVariables(rule);
            //Also the head!
            Sentence head = rule.getHead();
            Set<String> varsInHead = getVars(head);
            varsInHead.removeAll(supportedVariables);
            if (!varsInHead.isEmpty()) {
                reporter.report("Unsafe rule: The following variables are used in the head "
                        + "but not defined in a positive conjunct of the rule: " + varsInHead,
                        head.getPosition());
            }
            rule.accept(new GdlVisitor() {
                @Override
                public void visitNegation(Literal negation) {
                    Set<String> varsInNegation = getVars(negation);
                    varsInNegation.removeAll(supportedVariables);
                    if (!varsInNegation.isEmpty()) {
                        reporter.report("Unsafe rule: The following variables are used in a negated sentence "
                                + "but not defined in a positive conjunct of the rule: " + varsInNegation,
                                negation.getPosition());
                    }
                }
                @Override
                public void visitDistinct(Literal distinct) {
                    Set<String> varsInDistinct = new HashSet<String>();
                    varsInDistinct.addAll(getVars(distinct.getDistinctTerm1()));
                    varsInDistinct.addAll(getVars(distinct.getDistinctTerm2()));
                    varsInDistinct.removeAll(supportedVariables);
                    if (!varsInDistinct.isEmpty()) {
                        reporter.report("Unsafe rule: The following variables are used in a distinct clause "
                                + "but not defined in a positive conjunct of the rule: " + varsInDistinct,
                                distinct.getPosition());
                    }
                }
            });
        }
    }

    private Set<String> getSupportedVariables(Rule rule) {
        Set<String> supportedVars = new HashSet<String>();
        for (Literal literal : rule.getConjuncts()) {
            supportedVars.addAll(getSupportedVariables(literal));
        }
        return supportedVars;
    }

    private Set<String> getSupportedVariables(Literal literal) {
        if (literal.isSentence()) {
            return getVars(literal.getSentence());
        } else if (literal.isDisjunction()) {
            //Get the intersection of variables found
            List<Set<String>> setsToIntersect = new ArrayList<Set<String>>();
            for (Literal inner : literal.getDisjunction()) {
                setsToIntersect.add(getSupportedVariables(inner));
            }
            return intersect(setsToIntersect);
        } else {
            return Collections.emptySet();
        }
    }

    private Set<String> intersect(List<Set<String>> setsToIntersect) {
        if (setsToIntersect.isEmpty()) {
            return Collections.emptySet();
        }
        Set<String> candidates = new HashSet<String>(setsToIntersect.get(0));
        for (int i = 1; i < setsToIntersect.size(); i++) {
            candidates.retainAll(setsToIntersect.get(i));
        }
        return candidates;
    }

    private Set<String> getVars(GdlVisitable gdl) {
        final Set<String> variables = new HashSet<String>();
        gdl.accept(new GdlVisitor() {
            @Override
            public void visitVariable(String variable, Position position) {
                variables.add(variable);
            }
        });
        return variables;
    }
}
