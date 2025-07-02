package concurrent.order.service.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

public class DeferredResultUtil {

    public static <T> DeferredResult<ResponseEntity<T>> fromCompletableFuture(
        CompletableFuture<T> future) {
        DeferredResult<ResponseEntity<T>> result = new DeferredResult<>();

        future.thenAccept(data -> result.setResult(ResponseEntity.ok(data)))
            .exceptionally(ex -> {
                result.setErrorResult(resolveCause(ex));
                return null;
            });

        return result;
    }

    public static <T> DeferredResult<ResponseEntity<T>> fromVoidFuture(CompletableFuture<Void> future) {
        DeferredResult<ResponseEntity<T>> result = new DeferredResult<>();

        future.thenRun(() -> result.setResult(ResponseEntity.noContent().build()))
            .exceptionally(ex -> {
                result.setErrorResult(resolveCause(ex));
                return null;
            });

        return result;
    }

    private static Throwable resolveCause(Throwable ex) {
        return (ex instanceof CompletionException && ex.getCause() != null)
            ? ex.getCause()
            : ex;
    }

}
