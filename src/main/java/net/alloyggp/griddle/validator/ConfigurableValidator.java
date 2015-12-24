package net.alloyggp.griddle.validator;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.alloyggp.griddle.GdlProblem;
import net.alloyggp.griddle.GdlProblem.Level;
import net.alloyggp.griddle.Position;
import net.alloyggp.griddle.validator.check.Check;
import net.alloyggp.griddle.validator.check.ProblemReporter;

public class ConfigurableValidator implements Validator {
    private final ValidatorConfiguration config;

    private ConfigurableValidator(ValidatorConfiguration config) {
        this.config = config;
    }

    public static ConfigurableValidator create(ValidatorConfiguration config) {
        return new ConfigurableValidator(config);
    }

    @Override
    public Set<GdlProblem> findProblems(String gdlFile) {
        Set<GdlProblem> problems = new HashSet<GdlProblem>();
        AnalyzedGame game;
        try {
            game = AnalyzedGame.parseAndAnalyze(gdlFile);
        } catch (Exception e) {
            return Collections.singleton(GdlProblem.createError(
                    "Problem parsing the game: " + e.getMessage(), null));
        }

        Map<Level, ProblemReporter> reporters = new HashMap<Level, ProblemReporter>();
        addReporter(reporters, problems, Level.WARNING);
        addReporter(reporters, problems, Level.ERROR);

        for (Entry<Check, Level> entry : config.getChecks().entrySet()) {
            Check check = entry.getKey();
            Level level = entry.getValue();
            check.findProblems(game, reporters.get(level));
        }
        return problems;
    }

    private void addReporter(Map<Level, ProblemReporter> reporters,
            final Set<GdlProblem> problems, final Level level) {
        reporters.put(level, new ProblemReporter() {
            @Override
            public void report(String message, Position position) {
                problems.add(GdlProblem.create(level, message, position));
            }
        });
    }
}
