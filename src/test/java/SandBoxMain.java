import model.FailedTestCase;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import service.HttpService;
import service.TestFailService;

import java.io.IOException;
import java.util.List;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

public class SandBoxMain {

    private static final String HOSTNAME = "HOSTNAME";
    private static final String SANDBOX_TEST = "test.sandbox";

    private static SummaryGeneratingListener runTests(String testPackage) {
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
        HttpService httpService = new HttpService();

        TestFailService failService = new TestFailService();
        SummaryGeneratingListener listener = runTests(SANDBOX_TEST);
        TestExecutionSummary summary = listener.getSummary();
        List<FailedTestCase> fails = failService.formatFailures(summary.getFailures());
        failService.printFails(fails);
        try {
            if (!fails.isEmpty()) {
                String containerId = System.getenv().get(HOSTNAME);
                httpService.sendTestResult(args[0], 0, fails, containerId);

                throw new RuntimeException("Your code may be malicious or have an infinite loop!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
