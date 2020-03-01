package com.github.bdqfork.rpc;

import com.github.bdqfork.core.exception.TimeoutException;
import com.github.bdqfork.rpc.protocol.Request;
import com.github.bdqfork.rpc.protocol.Response;
import com.github.bdqfork.rpc.protocol.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 远程调用结果的future，用来记录执行结果
 *
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

    /**
     * 生成一个新的Future
     *
     * @param requestId 请求Id
     * @param timeout   超时时间
     * @return DefaultFuture实例
     */
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

    /**
     * 接收响应信息，完成future
     *
     * @param response 响应信息
     */
    public static void received(Response response) {
        if (log.isDebugEnabled()) {
            log.debug("receive response for id {}!", response.getId());
        }
        DefaultFuture future = FUTURE_MAP.get(response.getId());
        if (future == null) {
            if (log.isWarnEnabled()) {
                log.warn("receive response for id {}, but it is expired!", response.getId());
            }
            return;
        }
        future.timerTask.cancel();
        Result result = (Result) response.getPayload();
        if (response.getStatus() == Request.ERROR) {
            future.completeExceptionally(result.getThrowable());
        } else {
            future.complete(result.getData());
        }
    }
}
