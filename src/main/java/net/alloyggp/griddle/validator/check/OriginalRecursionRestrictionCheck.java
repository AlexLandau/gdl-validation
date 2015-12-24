package net.alloyggp.griddle.validator.check;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.alloyggp.griddle.grammar.GdlVisitor;
import net.alloyggp.griddle.grammar.Literal;
import net.alloyggp.griddle.grammar.Rule;
import net.alloyggp.griddle.grammar.Sentence;
import net.alloyggp.griddle.grammar.Term;
import net.alloyggp.griddle.validator.AnalyzedGame;

public class OriginalRecursionRestrictionCheck implements Check {
    public static final OriginalRecursionRestrictionCheck INSTANCE = new OriginalRecursionRestrictionCheck();

    private OriginalRecursionRestrictionCheck() {
        //Singleton
    }

    @Override
    public void findProblems(final AnalyzedGame game, final ProblemReporter reporter) {
        game.visitAll(new GdlVisitor() {
            @Override
            public void visitRule(Rule rule) {
                // Look at all the terms in each positive relation in the rule that
                // is in a cycle with the head.
                Set<Term> termsInNonRecursivePositiveConjuncts = getTermsInNonRecursivePositiveConjuncts(rule, game);

                for (Literal conjunct : rule.getConjuncts()) {
                    if (conjunct.isSentence()) {
                        reportViolationsIfCyclic(game, reporter, rule,
                                termsInNonRecursivePositiveConjuncts,
                                conjunct.getSentence());
                    } else if (conjunct.isDisjunction()) {
                        //Check sentences in disjunctions
                        for (Literal disjunct : conjunct.getDisjunction()) {
                            if (disjunct.isSentence()) {
                                reportViolationsIfCyclic(game, reporter, rule,
                                        termsInNonRecursivePositiveConjuncts,
                                        disjunct.getSentence());
                            }
                        }
                    }
                }
            }

            private void reportViolationsIfCyclic(final AnalyzedGame game,
                    final ProblemReporter reporter, Rule rule,
                    Set<Term> termsInNonRecursivePositiveConjuncts,
                    Sentence sentence) {
                String headName = rule.getHead().getName();
                if (sentence.getName().equals(headName)
                        || game.getSentenceNameAncestors(sentence.getName())
                        .contains(headName)) {
                    //This is in a cycle with the head.
                    Set<Term> problemTerms = findProblemTerms(sentence, rule,
                            termsInNonRecursivePositiveConjuncts);
                    for (Term term : problemTerms) {
                        //TODO: More informative error message?
                        reporter.report("The term " + term.getUserFriendlyString() + " in this rule violates the Recursion Restriction.", term.getPosition());
                    }
                }
            }
        });
    }

    protected static List<Sentence> toSentences(List<Literal> literals) {
        List<Sentence> sentences = new ArrayList<Sentence>(literals.size());
        for (Literal literal : literals) {
            if (!literal.isSentence()) {
                throw new IllegalArgumentException();
            }
            sentences.add(literal.getSentence());
        }
        return sentences;
    }

    protected static boolean allSentences(List<Literal> literals) {
        for (Literal literal : literals) {
            if (!literal.isSentence()) {
                return false;
            }
        }
        return true;
    }

    protected static Set<Term> getTermsInNonRecursivePositiveConjuncts(Rule rule,
            AnalyzedGame game) {
        Set<Term> results = new HashSet<Term>();
        String headName = rule.getHead().getName();

        for (Literal conjunct : rule.getConjuncts()) {
            if (conjunct.isSentence()) {
                if (!conjunct.getSentence().getName().equals(headName)
                        && !game.getSentenceNameAncestors(conjunct.getSentence().getName())
                        .contains(headName)) {
                    //Sentence is not in a cycle with the head; add its terms
                    results.addAll(conjunct.getSentence().getBody());
                }
            } else if (conjunct.isDisjunction()) {
                List<Literal> innerLiterals = conjunct.getDisjunction();
                if (!allSentences(innerLiterals)) {
                    //Can't add any terms from this set
                    continue;
                }
                List<Sentence> innerSentences = toSentences(innerLiterals);
                //We want the terms that are in the intersection of all these sentences...
                //but only if every sentence is not in a cycle with the head
                List<List<Term>> termsToIntersect = new ArrayList<List<Term>>();
                for (Sentence sentence : innerSentences) {
                    if (game.getSentenceNameAncestors(sentence.getName())
                            .contains(headName)) {
                        //Sentence is in a cycle with the head; can't add any terms
                        termsToIntersect.add(Collections.<Term>emptyList());
                    } else {
                        termsToIntersect.add(sentence.getBody());
                    }
                }

                results.addAll(intersectIgnorePosition(termsToIntersect));
            }
        }
        return results;
    }

    //TODO: Make this more efficient?
    private static List<Term> intersectIgnorePosition(
            List<List<Term>> termsToIntersect) {
        if (termsToIntersect.isEmpty()) {
            return Collections.emptyList();
        }
        List<Term> termsUnderConsideration = new ArrayList<Term>(termsToIntersect.get(0));
        for (int i = 1; i < termsToIntersect.size(); i++) {
            List<Term> currentRoundTerms = termsToIntersect.get(i);
            List<Term> termsStillJustified = new ArrayList<Term>();
            for (Term term : termsUnderConsideration) {
                if (containsIgnorePosition(currentRoundTerms, term)) {
                    termsStillJustified.add(term);
                }
            }
            termsUnderConsideration = termsStillJustified;
        }
        return termsUnderConsideration;
    }

    private static boolean containsIgnorePosition(List<Term> set,
            Term termToFind) {
        for (Term candidate : set) {
            if (candidate.equalsIgnorePosition(termToFind)) {
                return true;
            }
        }
        return false;
    }

    protected static Set<Term> findProblemTerms(Sentence conjunct, Rule rule, Set<Term> termsInNonRecursivePositiveConjuncts) {
        Set<Term> problems = new HashSet<Term>();
        //Every term in the sentence body needs to be ok.
        conjunctTermLoop : for (Term term : conjunct.getBody()) {
            //There are three ways this can be okay:
            //1) If it's ground, it's okay.
            if (term.isGround()) {
                continue conjunctTermLoop;
            }
            //2) If it appears as-is in the head relation, it's okay.
            for (Term headTerm : rule.getHead().getBody()) {
                if (term.equalsIgnorePosition(headTerm)) {
                    continue conjunctTermLoop;
                }
            }
            //3) If it's in some other positive conjunct not in a cycle with the head, it's okay.
            for (Term termInNonRecursiveConjunct : termsInNonRecursivePositiveConjuncts) {
                if (term.equalsIgnorePosition(termInNonRecursiveConjunct)) {
                    continue conjunctTermLoop;
                }
            }

            //Otherwise, we have a problem
            problems.add(term);
        }
        return problems;
    }
}
