package net.alloyggp.griddle.validator;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.alloyggp.griddle.generated.ParserHelper;
import net.alloyggp.griddle.grammar.GdlVisitor;
import net.alloyggp.griddle.grammar.Literal;
import net.alloyggp.griddle.grammar.Rule;
import net.alloyggp.griddle.grammar.Sentence;
import net.alloyggp.griddle.grammar.TopLevelGdl;

public class AnalyzedGame {
    private final List<TopLevelGdl> topLevelGdl;
    private final List<Rule> rules; //Just the rules from the top-level GDL
    /**
     * If the 'gdl' keyword is used, this contains the arguments to that keyword.
     * All strings stored are lowercase.
     */
    private final Set<String> gdlVariants; //Which variants are set with the 'gdl' keyword
    /**
     * Each name that is a key depends on each name that is in its associated set.
     * This includes indirect relations through multiple rules.
     */
    private final Map<String, Set<String>> sentenceNameAncestorGraph;
    private final Set<String> stateDependentNames;
    private final Set<String> actionDependentNames;

    private AnalyzedGame(List<TopLevelGdl> rules,
            Map<String, Set<String>> sentenceNameAncestorGraph,
            Set<String> gdlVariants,
            Set<String> stateDependentNames,
            Set<String> actionDependentNames) {
        this.topLevelGdl = Collections.unmodifiableList(rules);
        this.rules = collectRules(topLevelGdl);
        this.sentenceNameAncestorGraph = Collections.unmodifiableMap(sentenceNameAncestorGraph);
        this.gdlVariants = Collections.unmodifiableSet(gdlVariants);
        this.stateDependentNames = Collections.unmodifiableSet(stateDependentNames);
        this.actionDependentNames = Collections.unmodifiableSet(actionDependentNames);
    }

    private static List<Rule> collectRules(List<TopLevelGdl> gdls) {
        List<Rule> results = new ArrayList<Rule>();
        for (TopLevelGdl gdl : gdls) {
            if (gdl.isRule()) {
                results.add(gdl.getRule());
            }
        }
        return Collections.unmodifiableList(results);
    }

    public static AnalyzedGame parseAndAnalyze(String gdlFile) throws Exception {
        List<TopLevelGdl> rules = ParserHelper.parse(new StringReader(gdlFile));

        return analyze(rules);
    }

    private static AnalyzedGame analyze(List<TopLevelGdl> rules) {
        Map<String, Set<String>> sentenceNameDependencyGraph = generateSentenceNameDependencyGraph(rules);
        Map<String, Set<String>> sentenceNameAncestorGraph = toAncestorGraph(sentenceNameDependencyGraph);

        Set<String> gdlVariants = pickOutGdlVariants(rules);
        Set<String> stateDependentNames = getNamesMatchingOrDownstreamOf("true", sentenceNameAncestorGraph);
        Set<String> actionDependentNames = getNamesMatchingOrDownstreamOf("does", sentenceNameAncestorGraph);

        return new AnalyzedGame(rules,
                sentenceNameAncestorGraph,
                gdlVariants,
                stateDependentNames,
                actionDependentNames);
    }

    private static Set<String> pickOutGdlVariants(List<TopLevelGdl> rules) {
        Set<String> variants = new HashSet<String>();
        for (TopLevelGdl gdl : rules) {
            if (gdl.isSentence()) {
                Sentence sentence = gdl.getSentence();
                if (sentence.getName().equalsIgnoreCase("gdl")
                        && sentence.getBody().size() == 1) {
                    String variantName = sentence.getBody().get(0).toString();
                    variants.add(variantName.toLowerCase());
                }
            }
        }
        return variants;
    }

    private static Set<String> getNamesMatchingOrDownstreamOf(String target,
            Map<String, Set<String>> ancestorGraph) {
        Set<String> results = new HashSet<String>();
        //Always add target, for cases like "true" or "does" that are not
        //made true by sentences or rules within the GDL. (See GitHub issue #18)
        results.add(target);
        for (String name : ancestorGraph.keySet()) {
            if (name.equalsIgnoreCase(target)) {
                results.add(name);
            } else {
                for (String candidate : ancestorGraph.get(name)) {
                    if (candidate.equalsIgnoreCase(target)) {
                        results.add(name);
                        break;
                    }
                }
            }
        }
        return results;
    }

    private static Map<String, Set<String>> toAncestorGraph(
            Map<String, Set<String>> dependencyGraph) {
        Map<String, Set<String>> ancestorGraph = new HashMap<String, Set<String>>();

        //Initially populate the ancestor graph: deep copy
        for (Entry<String, Set<String>> entry : dependencyGraph.entrySet()) {
            ancestorGraph.put(entry.getKey(), new HashSet<String>(entry.getValue()));
        }
        boolean addedSomething = true;
        while (addedSomething) {
            addedSomething = false;
            for (String key : ancestorGraph.keySet()) {
                for (String value : new ArrayList<String>(ancestorGraph.get(key))) {
                    Set<String> nextLevelDependencies = ancestorGraph.get(value);
                    if (nextLevelDependencies != null) {
                        addedSomething |= ancestorGraph.get(key).addAll(nextLevelDependencies);
                    }
                }
            }
        }
        return ancestorGraph;
    }

    private static Map<String, Set<String>> generateSentenceNameDependencyGraph(
            List<TopLevelGdl> rules) {
        final Map<String, Set<String>> ruleDependencyGraph = new HashMap<String, Set<String>>();
        for (TopLevelGdl gdl : rules) {
            if (gdl.isRule()) {
                Rule rule = gdl.getRule();
                final String headName = rule.getHead().getName();
                if (!ruleDependencyGraph.containsKey(headName)) {
                    ruleDependencyGraph.put(headName, new HashSet<String>());
                }
                for (Literal literal : rule.getConjuncts()) {
                    literal.accept(new GdlVisitor() {
                        @Override
                        public void visitSentence(Sentence sentence) {
                            ruleDependencyGraph.get(headName).add(sentence.getName());
                        }
                    });
                }
            }
        }
        return ruleDependencyGraph;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public List<TopLevelGdl> getTopLevelComponents() {
        return topLevelGdl;
    }

    public void visitAll(GdlVisitor gdlVisitor) {
        for (TopLevelGdl gdl : topLevelGdl) {
            gdl.accept(gdlVisitor);
        }
    }

    public Set<String> getSentenceNameAncestors(String name) {
        Set<String> result = sentenceNameAncestorGraph.get(name);
        if (result == null) {
            return Collections.emptySet();
        } else {
            return Collections.unmodifiableSet(result);
        }
    }

    /**
     * If the 'gdl' keyword is used, this contains the arguments to that keyword.
     * All strings stored are lowercase.
     */
    public Set<String> getGdlVariants() {
        return gdlVariants;
    }

    public boolean isStateDependent(String sentenceName) {
        return stateDependentNames.contains(sentenceName);
    }

    public boolean isActionDependent(String sentenceName) {
        return actionDependentNames.contains(sentenceName);
    }
}
