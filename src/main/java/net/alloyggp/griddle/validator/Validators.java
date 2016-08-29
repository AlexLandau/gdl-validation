package net.alloyggp.griddle.validator;

public class Validators {
    private Validators() {
        //Not instantiable
    }

    public static Validator getStandardValidator() {
        return CascadingValidator.create(
                ParenthesesValidator.INSTANCE,
                ConfigurableValidator.create(getStandardConfiguration()));
    }

    /**
     * This validator includes support for the experimental 'gdl' keyword and associated
     * GDL variants.
     */
    public static Validator getExperimentalValidator() {
        return CascadingValidator.create(
                ParenthesesValidator.INSTANCE,
                ConfigurableValidator.create(ValidatorConfiguration.createExperimental()));
    }

    public static ValidatorConfiguration getStandardConfiguration() {
        return ValidatorConfiguration.createStandard();
    }
}
