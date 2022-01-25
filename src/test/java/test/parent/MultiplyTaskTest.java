package test.parent;


import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import test.annotations.Points;
import test.annotations.test_base.ArraySource;
import test.annotations.test_base.ArraySources;
import test.extensions.CustomTestExtension;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@ExtendWith(CustomTestExtension.class)
public class MultiplyTaskTest {

    public static final String MULTIPLY_TIMEOUT = "Multiply Timeout";

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
                () -> Assert.assertEquals(expected, MultiplyTask.multiply(args[0], args[1]))
        );
//         //Arrange
//         int expected = args[2];
//         int actual = 0;
//         ExecutorService executor = Executors.newSingleThreadExecutor();
//         Callable<Integer> divisionTask = () -> MultiplyTask.multiply(args[0], args[1]);
//         Future<Integer> multiplyFuture = executor.submit(divisionTask);

//         //Act
//         try {
//             actual = multiplyFuture.get(2, TimeUnit.SECONDS);
//         } catch (TimeoutException ex) {
//             multiplyFuture.cancel(true);
//             Assertions.fail(MULTIPLY_TIMEOUT);
//         } catch (ExecutionException | InterruptedException e) {
//             e.printStackTrace();
//         }
//         executor.shutdownNow();
//         //Assert
//         Assert.assertEquals(expected, actual);
    }
}
