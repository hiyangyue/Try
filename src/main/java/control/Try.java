package control;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Try<T> {

    /**
     * Check if is success
     *
     * @return true if is success, otherwise if false
     */
    public abstract boolean isSuccess();

    /**
     * Check is failure
     *
     * @return true if is failure, otherwise if false
     */
    public abstract boolean isFailure();

    /**
     * Return value if this is a {@link Success}` or throw exceptions if this is a {@link Failure}
     */
    public abstract T get();

    protected abstract Throwable getCause();

    /**
     * Runs the given function applied to the value if this is a {@link Success},
     * or return this if this is a {@link Failure}
     *
     * <code>
     * control.Try.of(() -> 1).map(x -> x * 2)
     * </code>
     *
     * @param <R> new component type
     * @throws NullPointerException if {@code mapper} is null
     */
    public abstract <R> Try<R> map(Function<? super T, ? extends R> mapper);

    /**
     * Returns the give function applied to the value from {@link Success},
     * or return this if this is a {@link Failure}
     *
     * @param <R> new component type
     * @throws NullPointerException if {@code mapper} is null
     */
    public abstract <R> Try<R> flatMap(Function<? super T, ? extends Try<R>> mapper);

    /**
     * Create a instance of control.Try
     *
     * @param <T> Component type
     * @return {@link Success} if no exception occurs, else returns {@link Failure}
     */
    public static <T> Try<T> of(final CheckedSupplier<T> value) {
        Objects.requireNonNull(value);
        try {
            return new Success<>(value.get());
        } catch (Throwable throwable) {
            return new Failure<>(throwable);
        }
    }

    /**
     * Passes the result to the given {@code consumer} if this is a {@code control.Success}.
     * The main purpose is to chain the process which make code clean
     *
     * <code>
     * control.Try.of(() -> 1)
     * .andThen(() -> doAction())
     * .andThen(() -> doAction())
     * .andThen(() -> doAction())
     * </code>
     *
     * @param consumer A Consumer
     * @return {@link Success} if is not failure, else return {@link Failure}
     */
    public final Try<T> andThen(Consumer<? super T> consumer) {
        Objects.requireNonNull(consumer);
        if (isFailure()) {
            return this;
        }

        try {
            consumer.accept(get());
            return this;
        } catch (Throwable throwable) {
            return new Failure<>(throwable);
        }
    }

    /**
     * Consumes the value if this is a {@link Success}
     *
     * <code>
     * control.Try.of(()-> getUser()).onSuccess(System.out::println)
     * </code>
     *
     * @param action a value consume
     * @return this
     * @throws NullPointerException if {@code action} is null
     */
    public final Try<T> onSuccess(final Consumer<T> action) {
        Objects.requireNonNull(action);
        if (isSuccess()) {
            action.accept(get());
        }

        return this;
    }

    /**
     * Consumes the cause if this is a {@link Failure}
     *
     * <code>
     * control.Try.of(() -> mayThrowExceptionMethod()).onFailure(System.out::println)
     * </code>
     *
     * @param action a exception consume
     * @return this
     * @throws NullPointerException if {@code action} is null
     */
    public final Try<T> onFailure(Consumer<? super Throwable> action) {
        Objects.requireNonNull(action);
        if (isFailure()) {
            action.accept((Throwable) getCause());
        }

        return this;
    }

    /**
     * Return the value if this is a {@link Success}, else return the elseValue in a {@link Failure}
     */
    public final T getOrElse(final T elseValue) {
        if (isSuccess()) {
            return this.get();
        } else {
            return elseValue;
        }
    }

    /**
     * Return this if this is a {@link Success}, else return the other control.Try
     */
    public final Try<T> orElse(final Try<T> other) {
        Objects.requireNonNull(other);
        if (isSuccess()) {
            return this;
        } else {
            return other;
        }
    }

    /**
     * Recover exception of the failure
     *
     * <code>
     * control.Try.of(() -> 1 / 0).recover(ArithmeticException.class, arithmeticException -> doSomething())
     * </code>
     *
     * @param exceptExceptionType The specify Exception should handle
     * @param f                   a callback when the specify exception happened
     */
    @SuppressWarnings("unchecked")
    public final <R extends Throwable> Try<T> recover(Class<R> exceptExceptionType,
                                                      Function<? super R, ? extends T> f) {
        Objects.requireNonNull(exceptExceptionType);
        Objects.requireNonNull(f);
        if (isFailure()) {
            Throwable cause = getCause();
            if (exceptExceptionType.isAssignableFrom(cause.getClass())) {
                return Try.of(() -> f.apply((R) cause));
            }
        }

        return this;
    }

    /**
     * A shortcuts for creating a {@link Failure} instance
     */
    <T> Try<T> failure(final Throwable throwable) {
        return new Failure<>(throwable);
    }
}