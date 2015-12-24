package net.alloyggp.griddle.validator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.alloyggp.griddle.GdlProblem.Level;
import net.alloyggp.griddle.validator.check.Check;
import net.alloyggp.griddle.validator.check.ContainsRoleTerminalGoalLegalCheck;
import net.alloyggp.griddle.validator.check.ContainsUnusualCharacterCheck;
import net.alloyggp.griddle.validator.check.DatalogKeywordsNotConstantsCheck;
import net.alloyggp.griddle.validator.check.DisjunctionNotNestedCheck;
import net.alloyggp.griddle.validator.check.EmptyBodyCheck;
import net.alloyggp.griddle.validator.check.ErrorStringCheck;
import net.alloyggp.griddle.validator.check.FixedArityCheck;
import net.alloyggp.griddle.validator.check.InconsistentCapitalizationCheck;
import net.alloyggp.griddle.validator.check.InitBaseInputAreConstantCheck;
import net.alloyggp.griddle.validator.check.InitNextBaseInputNotInRuleBodiesCheck;
import net.alloyggp.griddle.validator.check.KeywordArityCheck;
import net.alloyggp.griddle.validator.check.KeywordsAreNotFunctionNamesCheck;
import net.alloyggp.griddle.validator.check.KeywordsAreNotObjectConstantsCheck;
import net.alloyggp.griddle.validator.check.NegationContainsSentenceCheck;
import net.alloyggp.griddle.validator.check.NoNegativeEdgesInCyclesCheck;
import net.alloyggp.griddle.validator.check.OriginalRecursionRestrictionCheck;
import net.alloyggp.griddle.validator.check.RoleTrueDoesNotInRuleHeadCheck;
import net.alloyggp.griddle.validator.check.RuleSafetyCheck;
import net.alloyggp.griddle.validator.check.SentenceAndFunctionNamesDifferCheck;
import net.alloyggp.griddle.validator.check.TerminalLegalGoalAreNotActionDependentCheck;
import net.alloyggp.griddle.validator.check.TrueDoesAreNotStandaloneSentencesCheck;
import net.alloyggp.griddle.validator.check.UnproducedSentenceNamesCheck;
import net.alloyggp.griddle.validator.check.UnusedSentenceNamesCheck;

public class ValidatorConfiguration {
    private final Map<Check, Level> checks;

    private ValidatorConfiguration(Map<Check, Level> checks) {
        this.checks = Collections.unmodifiableMap(new HashMap<Check, Level>(checks));
    }

    public static ValidatorConfiguration createStandard() {
        Map<Check, Level> checks = new HashMap<Check, Level>();

        checks.put(ContainsRoleTerminalGoalLegalCheck.INSTANCE, Level.ERROR);
        checks.put(ContainsUnusualCharacterCheck.INSTANCE, Level.WARNING);
        checks.put(DatalogKeywordsNotConstantsCheck.INSTANCE, Level.ERROR);
        checks.put(DisjunctionNotNestedCheck.INSTANCE, Level.WARNING);
        checks.put(ErrorStringCheck.INSTANCE, Level.ERROR);
        checks.put(EmptyBodyCheck.INSTANCE, Level.WARNING);
        checks.put(FixedArityCheck.INSTANCE, Level.WARNING);
        checks.put(InconsistentCapitalizationCheck.INSTANCE, Level.ERROR);
        checks.put(InitBaseInputAreConstantCheck.INSTANCE, Level.ERROR);
        checks.put(InitNextBaseInputNotInRuleBodiesCheck.INSTANCE, Level.ERROR);
        checks.put(KeywordArityCheck.INSTANCE, Level.ERROR);
        checks.put(KeywordsAreNotFunctionNamesCheck.INSTANCE, Level.ERROR);
        checks.put(KeywordsAreNotObjectConstantsCheck.INSTANCE, Level.WARNING);
        checks.put(NegationContainsSentenceCheck.INSTANCE, Level.ERROR);
        checks.put(NoNegativeEdgesInCyclesCheck.INSTANCE, Level.ERROR);
        checks.put(OriginalRecursionRestrictionCheck.INSTANCE, Level.ERROR);
        checks.put(RoleTrueDoesNotInRuleHeadCheck.INSTANCE, Level.ERROR);
        checks.put(RuleSafetyCheck.INSTANCE, Level.ERROR);
        checks.put(SentenceAndFunctionNamesDifferCheck.INSTANCE, Level.WARNING);
        checks.put(TerminalLegalGoalAreNotActionDependentCheck.INSTANCE, Level.ERROR);
        checks.put(TrueDoesAreNotStandaloneSentencesCheck.INSTANCE, Level.ERROR);
        checks.put(UnproducedSentenceNamesCheck.INSTANCE, Level.WARNING);
        checks.put(UnusedSentenceNamesCheck.INSTANCE, Level.WARNING);

        return new ValidatorConfiguration(checks);
    }

    public Map<Check, Level> getChecks() {
        return checks;
    }

}
