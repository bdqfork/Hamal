package cn.hamal.rpc;

import cn.hamal.core.exception.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author bdq
 * @since 2020/2/27
 */
public class DefaultFuture extends CompletableFuture<Object> {
    private static final Logger log = LoggerFactory.getLogger(DefaultFuture.class);
    private static final Map<Long, DefaultFuture> FUTURE_MAP = new ConcurrentHashMap<>(256);
    private static final Timer TIMER = new Timer();
    private TimerTask timerTask;

    private DefaultFuture() {
    }

    public static DefaultFuture newDefaultFuture(Long requestId, long timeout) {
        DefaultFuture defaultFuture = new DefaultFuture();
        defaultFuture.timerTask = new TimerTask() {
            @Override
            public void run() {
                FUTURE_MAP.remove(requestId);
                defaultFuture.completeExceptionally(new TimeoutException());
            }
        };
        TIMER.schedule(defaultFuture.timerTask, timeout);
        FUTURE_MAP.put(requestId, defaultFuture);
        return defaultFuture;
    }

    public static void received(Response response) {
        DefaultFuture future = FUTURE_MAP.get(response.getId());
        if (future == null) {
            if (log.isWarnEnabled()) {
                log.warn("receive response for id {}, but it is expired!", response.getId());
            }
            return;
        }
        future.timerTask.cancel();
        if (response.getStatus() == Request.ERROR) {
            future.completeExceptionally((Throwable) response.getPayload());
        } else {
            future.complete(response.getPayload());
        }
    }
}
