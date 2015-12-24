package net.alloyggp.griddle.validator.check;

import net.alloyggp.griddle.Position;

public interface ProblemReporter {
    void report(String message, Position position);
}
