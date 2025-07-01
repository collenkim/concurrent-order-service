package concurrent.order.service.application.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {
    String key(); // SpEL 표현식 지원 가능
    long waitTime() default 5;
    long leaseTime() default 3;
}
