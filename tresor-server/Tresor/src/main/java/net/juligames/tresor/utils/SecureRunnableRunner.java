package net.juligames.tresor.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SecureRunnableRunner<E extends Exception> implements Runnable {

    private static final @NotNull Logger log = LoggerFactory.getLogger(SecureRunnableRunner.class);

    private final @NotNull Runnable<E> runnable;
    private final @Nullable java.lang.Runnable finallyRunnable;
    @NotNull
    private final Class<E> exceptionClass;

    public SecureRunnableRunner(@NotNull Runnable<E> runnable, @NotNull Class<E> exceptionClass) {
        this(runnable, exceptionClass, null);
    }

    public SecureRunnableRunner(@NotNull Runnable<E> runnable, @NotNull Class<E> exceptionClass, @Nullable java.lang.Runnable finallyRunnable) {
        this.runnable = runnable;
        this.finallyRunnable = finallyRunnable;
        this.exceptionClass = exceptionClass;
    }


    @Override
    public void run() {
        try {
            runnable.run();
        } catch (Exception e) {
            if (exceptionClass.isInstance(e)) {
                @SuppressWarnings("unchecked") E capture = (E) e;
                log.error("Error while running secure runnable", capture);
            } else {
                throw new RuntimeException("Unexpected exception while executing SecureRunnableRunner!", e);
            }
        } finally {
            if (finallyRunnable != null) {
                finallyRunnable.run();
            }
        }
    }

    public interface Runnable<E extends Exception> {
        void run() throws E;
    }
}
