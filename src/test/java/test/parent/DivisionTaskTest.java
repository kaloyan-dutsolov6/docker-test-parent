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
public class DivisionTaskTest {

    @Points(value = 2)
    @ParameterizedTest
    @ArraySources(arrays = {
            @ArraySource(array = {15, 3, 5}),
            @ArraySource(array = {20, 5, 4}),
            @ArraySource(array = {355, 5, 71}),
            @ArraySource(array = {25, 5, 5}),
            @ArraySource(array = {67, -1, -67})
    })
    void divisionTest(int[] args) {
        //Arrange
        int expected = args[2];

        //Act and Assert
        assertTimeoutPreemptively(
                Duration.ofSeconds(2),
                () -> Assertions.assertEquals(expected, DivisionTask.divide(args[0], args[1]))
        );
    }
}
