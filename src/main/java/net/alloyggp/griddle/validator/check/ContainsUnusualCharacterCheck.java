package net.alloyggp.griddle.validator.check;

import java.util.HashSet;
import java.util.Set;

import net.alloyggp.griddle.Position;
import net.alloyggp.griddle.grammar.GdlVisitor;
import net.alloyggp.griddle.validator.AnalyzedGame;

public class ContainsUnusualCharacterCheck implements Check {
    public static final ContainsUnusualCharacterCheck INSTANCE = new ContainsUnusualCharacterCheck();
    private ContainsUnusualCharacterCheck() {
        //Singleton
    }

    @Override
    public void findProblems(AnalyzedGame game, final ProblemReporter reporter) {
        game.visitAll(new GdlVisitor() {
            @Override
            public void visitConstant(String constant, Position position) {
                Set<Character> unusualCharacters = getUnusualCharacters(constant);
                if (!unusualCharacters.isEmpty()) {
                    reporter.report("These characters are unusual in GDL and may not be handled correctly by all players: " + unusualCharacters, position);
                }
            }
            @Override
            public void visitVariable(String variable, Position position) {
                //Remove the '?'
                String namePart = variable.substring(1);
                Set<Character> unusualCharacters = getUnusualCharacters(namePart);
                if (!unusualCharacters.isEmpty()) {
                    reporter.report("These characters are unusual in GDL and may not be handled correctly by all players: " + unusualCharacters, position);
                }
            }
        });
    }

    protected static Set<Character> getUnusualCharacters(String string) {
        Set<Character> characters = new HashSet<Character>();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (isUnusual(c)) {
                characters.add(c);
            }
        }
        return characters;
    }

    private static boolean isUnusual(char c) {
        if ((c >= 'a' && c <= 'z')
                || (c >= 'A' && c <= 'Z')
                || (c >= '0' && c <= '9')
                || c == '_'
                || c == '-') {
            return false;
        }
        return true;
    }
}
