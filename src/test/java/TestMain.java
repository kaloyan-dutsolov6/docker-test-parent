import model.FailedTestCase;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import service.HttpService;
import service.TestFailService;
import test.extensions.CustomTestExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

public class TestMain {

    private static final String HOSTNAME = "HOSTNAME";
    private static final String SANDBOX_TEST = "test.sandbox";
    private static final String POINT_TESTS = "test.parent";


    public static SummaryGeneratingListener runTests(String testPackage) {
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectPackage(testPackage))
                .build();
        Launcher launcher = LauncherFactory.create();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);
        return listener;
    }

    public static void main(String[] args) {
        TestFailService failService = new TestFailService();
        SummaryGeneratingListener listener = runTests(POINT_TESTS);
        TestExecutionSummary summary = listener.getSummary();
        List<FailedTestCase> fails = failService.formatFailures(summary.getFailures());
        failService.printFails(fails);

        System.out.println("Points won:");
        System.out.println(CustomTestExtension.pointsSum);

        HttpService httpService = new HttpService();
        try {
            String containerId = System.getenv().get(HOSTNAME);
            if (fails.isEmpty()) {
                List<FailedTestCase> maliciousTests = isNotMalicious();
                if (!maliciousTests.isEmpty()) {
                    httpService.sendTestResult(args[0], 0, maliciousTests, containerId);
                }
            } else {
                httpService.sendTestResult(args[0], CustomTestExtension.pointsSum, fails, containerId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<FailedTestCase> isNotMalicious() {
        TestFailService failService = new TestFailService();
        SummaryGeneratingListener listener = runTests(SANDBOX_TEST);
        TestExecutionSummary summary = listener.getSummary();
        List<FailedTestCase> fails = failService.formatFailures(summary.getFailures());
        failService.printFails(fails);
        return fails;
    }
}
