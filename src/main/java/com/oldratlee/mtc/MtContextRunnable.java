package com.oldratlee.mtc;

import java.util.Map;

/**
 * {@link MtContextRunnable} uses to get @{@link MtContext}
 * and transmit it to the time of {@link Runnable} execution,
 * so as to use {@link Runnable} to thread pool.
 * <p/>
 * Use factory method {@link #get(Runnable)} to create instance.
 *
 * @author ding.lid
 * @see java.util.concurrent.Executor
 * @see java.util.concurrent.ExecutorService
 * @see java.util.concurrent.ThreadPoolExecutor
 * @see java.util.concurrent.ScheduledThreadPoolExecutor
 * @see java.util.concurrent.Executors
 */
public final class MtContextRunnable implements Runnable {
    private final Map<String, Object> context;
    private final Runnable runnable;

    private MtContextRunnable(Runnable runnable) {
        context = MtContext.getContext().get();
        this.runnable = runnable;
    }

    /**
     * wrap method {@link Runnable#run()}.
     */
    @Override
    public void run() {
        MtContext mtContext = MtContext.getContext();
        final Map<String, Object> old = mtContext.get();
        mtContext.set(context);
        runnable.run();
        mtContext.set(old); // restore MtContext
    }

    public Runnable getRunnable() {
        return runnable;
    }

    /**
     * Factory method, wrapper input {@link Runnable} to {@link MtContextRunnable}.
     *
     * @param runnable input {@link Runnable}
     * @return Wrapped {@link Runnable}
     */
    public static MtContextRunnable get(Runnable runnable) {
        if (runnable instanceof MtContextRunnable) {
            return (MtContextRunnable) runnable;
        }
        return new MtContextRunnable(runnable);
    }
}