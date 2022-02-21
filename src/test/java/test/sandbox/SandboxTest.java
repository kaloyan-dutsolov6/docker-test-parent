package test.sandbox;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import test.annotations.Points;
import test.annotations.test_base.ArraySource;
import test.annotations.test_base.ArraySources;
import test.extensions.CustomTestExtension;
import test.parent.DivisionTask;
import test.parent.MultiplyTask;

import java.security.Policy;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

@ExtendWith(CustomTestExtension.class)
public class SandboxTest {

    @BeforeAll
    static void beforeAll() {
        Policy.setPolicy(new SandboxSecurityPolicy());
        System.setSecurityManager(new SecurityManager());
    }

    @Points()
    @ParameterizedTest
    @ArraySources(arrays = {
            @ArraySource(array = {15, 3, 5}),
            @ArraySource(array = {20, 5, 4}),
            @ArraySource(array = {355, 5, 71}),
            @ArraySource(array = {25, 5, 5}),
            @ArraySource(array = {67, -1, -67})
    })
    void divisionSandbox(int[] args) {
        assertTimeoutPreemptively(
                Duration.ofSeconds(2),
                () -> DivisionTask.divide(args[0], args[1])
        );
    }

    @Points()
    @ParameterizedTest
    @ArraySources(arrays = {
            @ArraySource(array = {3, 5, 15}),
            @ArraySource(array = {4, 5, 20}),
            @ArraySource(array = {5, 71, 355}),
            @ArraySource(array = {5, 5, 25}),
            @ArraySource(array = {-1, -67, 67})
    })
    void multiplySandbox(int[] args) {
        assertTimeoutPreemptively(
                Duration.ofSeconds(2),
                () -> MultiplyTask.multiply(args[0], args[1])
        );
    }
}
