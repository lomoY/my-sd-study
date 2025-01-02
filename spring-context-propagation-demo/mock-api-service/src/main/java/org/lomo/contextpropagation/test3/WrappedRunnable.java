package org.lomo.contextpropagation.test3;

import static org.lomo.contextpropagation.test3.ControllerV3.CORRELATION_ID;

public class WrappedRunnable implements Runnable {

    private final Long correlationId;
    private final Runnable wrapped;

    public WrappedRunnable(Runnable wrapped) {
// 为什么 static final ThreadLocal<Long> CORRELATION_ID = new ThreadLocal<>(); 可以在这个地方拿到 ？ (⊙o⊙)…

        this.correlationId = CORRELATION_ID.get();
        this.wrapped = wrapped;
    }

    @Override
    public void run() {
        Long old = CORRELATION_ID.get();
        CORRELATION_ID.set(this.correlationId);
        try {
            wrapped.run();
        } finally {
            CORRELATION_ID.set(old);
        }
    }
}