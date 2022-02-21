import model.FailedTestCase;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import service.TestFailService;

import java.util.List;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

public class SandBoxMain {

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
        TestFailService failService = new TestFailService();
        SummaryGeneratingListener listener = runTests(SANDBOX_TEST);
        TestExecutionSummary summary = listener.getSummary();
        List<FailedTestCase> fails = failService.formatFailures(summary.getFailures());
        failService.printFails(fails);
    }
}
