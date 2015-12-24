package net.alloyggp.griddle.validator;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.alloyggp.griddle.GdlProblem;
import net.alloyggp.griddle.Position;

public class ParenthesesValidator implements Validator {
    public static final ParenthesesValidator INSTANCE = new ParenthesesValidator();

    @Override
    public Set<GdlProblem> findProblems(String gdlFile) {
        Deque<Integer> openingIndices = new ArrayDeque<Integer>();
        Deque<Integer> openingLineNumbers = new ArrayDeque<Integer>();
        boolean inComment = false;
        boolean lastNonWhitespaceWasOpenParens = false;
        int lineNumber = 1;
        for (int i = 0; i < gdlFile.length(); i++) {
            char curChar = gdlFile.charAt(i);
            if (inComment) {
                //Behavior when in comment:
                //end the comment if we're at EOL
                //otherwise, ignore the character
                if (curChar == '\n') {
                    inComment = false;
                    lineNumber++;
                }
            } else {
                //Behavior when not in comment
                if (curChar == ';') {
                    inComment = true;
                } else if (curChar == '(') {
                    if (lastNonWhitespaceWasOpenParens) {
                        return consecutiveOpenParens(i, lineNumber);
                    }
                    openingIndices.addLast(i);
                    openingLineNumbers.addLast(lineNumber);
                    lastNonWhitespaceWasOpenParens = true;
                } else if (curChar == ')') {
                    if (openingIndices.isEmpty()) {
                        return unmatchedCloseParens(i, lineNumber);
                    }
                    openingIndices.removeLast();
                    openingLineNumbers.removeLast();
                    lastNonWhitespaceWasOpenParens = false;
                } else if (curChar == '\n') {
                    lineNumber++;
                } else if (!Character.isWhitespace(curChar)) {
                    lastNonWhitespaceWasOpenParens = false;
                }
            }
        }

        //We've reached the end of the file
        if (!openingIndices.isEmpty()) {
            return unmatchedOpenParens(openingIndices, openingLineNumbers);
        }

        return Collections.emptySet();
    }

    private Set<GdlProblem> consecutiveOpenParens(int i, int lineNumber) {
        return Collections.singleton(GdlProblem.createError("Consecutive opening parentheses",
                new Position(i, i+1, lineNumber)));
    }

    private Set<GdlProblem> unmatchedCloseParens(int i, int lineNumber) {
        return Collections.singleton(GdlProblem.createError("Unmatched closing parenthesis",
                new Position(i, i+1, lineNumber)));
    }

    private Set<GdlProblem> unmatchedOpenParens(Iterable<Integer> indices, Iterable<Integer> lineNumbers) {
        Set<GdlProblem> problems = new HashSet<GdlProblem>();
        Iterator<Integer> indexItr = indices.iterator();
        Iterator<Integer> lineNumberItr = lineNumbers.iterator();
        while (indexItr.hasNext()) {
            int i = indexItr.next();
            int lineNumber = lineNumberItr.next();
            problems.add(GdlProblem.createError("Unmatched opening parenthesis",
                    new Position(i, i+1, lineNumber)));
        }
        return problems;
    }
}
