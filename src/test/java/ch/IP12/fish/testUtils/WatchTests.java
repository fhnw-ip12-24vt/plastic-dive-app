package ch.IP12.fish.testUtils;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})  // Can be used on classes
@Retention(RetentionPolicy.RUNTIME)  // Available at runtime via reflection
@ExtendWith(GlobalTestWatcher.class)  // The actual extension being applied
public @interface WatchTests {
    /**
     * Configures whether to log failures to external files
     * @default false
     */
    boolean logToFile() default true;

    /**
     * Configures whether to print full stack traces
     * @default true
     */
    boolean logStackTraces() default true;
}