package net.alloyggp.griddle.validator;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.alloyggp.griddle.GdlProblem;

public class CascadingValidator implements Validator {
    private final List<Validator> validators;

    private CascadingValidator(List<Validator> validators) {
        this.validators = new ArrayList<Validator>(validators);
    }

    public static CascadingValidator create(Validator... validators) {
        return new CascadingValidator(Arrays.asList(validators));
    }

    @Override
    public Set<GdlProblem> findProblems(String gdlFile) {
        Set<GdlProblem> problems = new HashSet<GdlProblem>();
        for (Validator validator : validators) {
            problems.addAll(validator.findProblems(gdlFile));
            if (containsError(problems)) {
                break;
            }
        }
        return problems;
    }

    private boolean containsError(Set<GdlProblem> problems) {
        for (GdlProblem problem : problems) {
            if (problem.isError()) {
                return true;
            }
        }
        return false;
    }
}
