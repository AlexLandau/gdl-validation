package net.alloyggp.griddle.validator.check;

import net.alloyggp.griddle.validator.AnalyzedGame;

/**
 * A Check is an aspect of game validation that usually produces a single
 * type of error. These are used in a ValidatorConfiguration and run by
 * the ConfigurableValidator.
 */
public interface Check {

    void findProblems(AnalyzedGame game, ProblemReporter reporter);

}
