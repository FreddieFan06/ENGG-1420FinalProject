// Created by Shivom Bhanot (1319202)

package validation.rules;

import java.util.List;

// Engine that applies a list of validation rules to a given context
public class ValidatorEngine<T> {
    private final List<ValidationRule<T>> rules; // rules to be applied

    public ValidatorEngine(List<ValidationRule<T>> rules) {
        this.rules = rules;
    }

    // Applies all rules to the provided context
    public void validate(T context) {
        for (ValidationRule<T> rule : rules) {
            rule.validate(context);
        }
    }
}