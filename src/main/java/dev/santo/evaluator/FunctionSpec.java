package dev.santo.evaluator;
import java.util.function.ToDoubleFunction;
record FunctionSpec(int minimum, int maximum, ToDoubleFunction<double[]> implementation) {
    double apply(double[] arguments) {
        if (arguments.length < minimum || arguments.length > maximum)
            throw new IllegalArgumentException("Expected " + minimum + ".." + maximum + " arguments, received " + arguments.length);
        return implementation.applyAsDouble(arguments);
    }
}
