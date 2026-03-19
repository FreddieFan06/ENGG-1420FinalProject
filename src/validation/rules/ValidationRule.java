// Created by Shivom Bhanot (1319202)

package validation.rules;

// Generic interface for any business rule that can validate a given context
public interface ValidationRule<T> {

    // Each rule must implement this to validate the context
    void validate(T context);
}