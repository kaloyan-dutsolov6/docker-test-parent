package test.parent;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import solutions.DivisionTask;
import solutions.MultiplyTask;
import test.annotations.test_base.ArraySource;
import test.annotations.test_base.ArraySources;
import test.extensions.CustomTestExtension;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


@ExtendWith(CustomTestExtension.class)
public class GlobalTaskTest {

    @ParameterizedTest
    @ArraySources(arrays = {
            @ArraySource(array = {15, 3, 5})
    })

    void infiniteLoopDivisionTest(int[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        Runnable divisionTask = () -> DivisionTask.divide(args[0], args[1]);

        Future<?> divisionFuture = executor.submit(divisionTask);
        try {
            divisionFuture.get(5, TimeUnit.SECONDS);
        } catch (TimeoutException ex) {
            System.out.println("TIMEOUT");
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            divisionFuture.cancel(true); // may or may not desire this
        }
    }

    void infiniteLoopMultiplyTest(int[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        Runnable multiplyTask = () -> MultiplyTask.multiply(args[0], args[1]);

        Future<?> multiplyFuture = executor.submit(multiplyTask);
        try {
            multiplyFuture.get(5, TimeUnit.SECONDS);
        } catch (TimeoutException ex) {
            System.out.println("TIMEOUT");
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            multiplyFuture.cancel(true); // may or may not desire this
        }
    }
}