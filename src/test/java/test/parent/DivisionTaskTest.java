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
public class DivisionTaskTest {

    public static final String DIVISION_TIMEOUT = "Division Timeout";

    //    @Rule
//    public final TestRule globalTimeout = Timeout.seconds(2);
//
//    @Parameterized.Parameter(0)
//    public int firstNum;
//    @Parameterized.Parameter(1)
//    public int secondNum;
//    @Parameterized.Parameter(2)
//    public int result;
//
//    @Parameterized.Parameters(name = "{0}")
//    public static Iterable<Object[]> data() {
//        return Arrays.asList(new Object[][]{
//                {15, 3, 5},
//                {20, 5, 4},
//                {355, 5, 71},
//                {25, 5, 5},
//                {67, -1, -67}
//        });
//    }

    @Points(value = 2)
    @ParameterizedTest
    @ArraySources(arrays = {
            @ArraySource(array = {15, 3, 5}),
            @ArraySource(array = {20, 5, 4}),
            @ArraySource(array = {355, 5, 71}),
            @ArraySource(array = {25, 5, 5}),
            @ArraySource(array = {67, -1, -67})
    })
    public void divisionTest(int[] args) {
        //Arrange
        int expected = args[2];
        int actual = 0;

        ExecutorService executor = Executors.newCachedThreadPool();
        Callable<Integer> divisionTask = () -> DivisionTask.divide(args[0], args[1]);
        Future<Integer> divisionFuture = executor.submit(divisionTask);

        //Act
        try {
            actual = divisionFuture.get(2, TimeUnit.SECONDS);
        } catch (TimeoutException ex) {
            Assertions.fail(DIVISION_TIMEOUT);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        //Assert
        Assert.assertEquals(expected, actual);
    }
}