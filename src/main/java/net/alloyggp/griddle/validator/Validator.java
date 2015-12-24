package net.alloyggp.griddle.validator;

import java.util.Set;

import net.alloyggp.griddle.GdlProblem;

public interface Validator {
    Set<GdlProblem> findProblems(String gdlFile);
}
