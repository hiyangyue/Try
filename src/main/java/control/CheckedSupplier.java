package control;

/**
 * A Supplier that may throw, equivalent to {@link java.util.function.Supplier}.
 * It's used in {@link Try}
 *
 * @param <T> the value type supplied to current supplier
 */

@FunctionalInterface
public interface CheckedSupplier<T> {

    T get() throws Throwable;
}

