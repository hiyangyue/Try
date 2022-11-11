package control;

import java.util.Objects;
import java.util.function.Function;

/**
 * a failed control.Try
 *
 * @param <T> component type of this control.Failure
 */
final class Failure<T> extends Try<T> {

    private final Throwable cause;

    /**
     * Constructs for control.Failure
     *
     * @throws NullPointerException if case is null
     * @throws Throwable            if cause is a fatal, in design, fatal exception will be thrown.
     */
    Failure(Throwable cause) {
        Objects.requireNonNull(cause);
        rethrowIfFatal(cause);
        this.cause = cause;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> Try<R> map(Function<? super T, ? extends R> mapper) {
        return (Try<R>) this;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public boolean isFailure() {
        return true;
    }

    @Override
    public T get() {
        throw new RuntimeException(this.cause);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> Try<R> flatMap(Function<? super T, ? extends Try<R>> mapper) {
        return (Try<R>) this;
    }

    @Override
    protected Throwable getCause() {
        return this.cause;
    }

    @Override
    public String toString() {
        return String.format("control.Failure(%s)", this.cause);
    }

    private void rethrowIfFatal(final Throwable throwable) {
        if (isFatal(throwable)) {
            throw new RuntimeException(throwable);
        }
    }

    private boolean isFatal(Throwable throwable) {
        if (throwable == null) {
            return false;
        }

        return throwable instanceof InterruptedException
                || throwable instanceof LinkageError
                || throwable instanceof ThreadDeath
                || throwable instanceof VirtualMachineError;
    }

}
