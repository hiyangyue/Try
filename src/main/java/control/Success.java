package control;

import java.util.Objects;
import java.util.function.Function;

/**
 * a succeeded control.Try
 *
 * @param <T> component type of this control.Success
 */
final class Success<T> extends Try<T> {
    private final T value;

    Success(final T value) {
        this.value = value;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public boolean isFailure() {
        return false;
    }

    @Override
    public T get() {
        return this.value;
    }

    @Override
    public <R> Try<R> map(Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper);
        try {
            return new Success<>(mapper.apply(this.value));
        } catch (Throwable throwable) {
            return failure(throwable);
        }
    }

    @Override
    public <R> Try<R> flatMap(Function<? super T, ? extends Try<R>> mapper) {
        Objects.requireNonNull(mapper);
        try {
            return mapper.apply(this.value);
        } catch (Throwable throwable) {
            return failure(throwable);
        }
    }

    @Override
    protected Throwable getCause() {
        return new UnsupportedOperationException("success is not supported");
    }

    @Override
    public String toString() {
        return String.format("control.Success(%s)", this.value);
    }
}

