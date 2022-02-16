package test.parent;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import test.annotations.Points;
import test.annotations.test_base.ArraySource;
import test.annotations.test_base.ArraySources;
import test.extensions.CustomTestExtension;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

@ExtendWith(CustomTestExtension.class)
public class MultiplyTaskTest {

    @Points(value = 1)
    @ParameterizedTest
    @ArraySources(arrays = {
            @ArraySource(array = {3, 5, 15}),
            @ArraySource(array = {4, 5, 20}),
            @ArraySource(array = {5, 71, 355}),
            @ArraySource(array = {5, 5, 25}),
            @ArraySource(array = {-1, -67, 67})
    })
    void multiplyTest(int[] args) {
        //Arrange
        int expected = args[2];

        //Act and Assert
        assertTimeoutPreemptively(
                Duration.ofSeconds(2),
                () -> Assertions.assertEquals(expected, MultiplyTask.multiply(args[0], args[1]))
        );
    }
}
