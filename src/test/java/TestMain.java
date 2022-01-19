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

//    public static SummaryGeneratingListener runGlobalTests() {
//        SummaryGeneratingListener listener = new SummaryGeneratingListener();
//        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
//                .selectors(selectPackage("test.global"))
//                .build();
//        Launcher launcher = LauncherFactory.create();
//        launcher.registerTestExecutionListeners(listener);
//        launcher.execute(request);
//        return listener;
//    }


    public static SummaryGeneratingListener runTests() {
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectPackage("test.parent"))
                .build();
        Launcher launcher = LauncherFactory.create();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);
        return listener;
    }


//    public static void main(String[] args) {
//        TestFailService failService = new TestFailService();
//        SummaryGeneratingListener globalListener = runGlobalTests();
//        TestExecutionSummary globalSummary = globalListener.getSummary();
//        List<FailedTestCase> globalFails = failService.formatFailures(globalSummary.getFailures());
//
//        if (globalFails.isEmpty()) {
//            SummaryGeneratingListener listener = runTests();
//            TestExecutionSummary summary = listener.getSummary();
//            List<FailedTestCase> fails = failService.formatFailures(summary.getFailures());
//            failService.printFails(fails);
//
//            System.out.println("Points won:");
//            System.out.println(CustomTestExtension.pointsSum);
//
//            HttpService httpService = new HttpService();
//            try {
//                httpService.sendTestResult(args[0], CustomTestExtension.pointsSum, fails);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            failService.printFails(globalFails);
//            System.out.println("Points won:");
//            System.out.println(CustomTestExtension.pointsSum);
//
//            HttpService httpService = new HttpService();
//            try {
//                httpService.sendTestResult(args[0], CustomTestExtension.pointsSum, globalFails);
////            httpService.sendTestResult("kaloyan_dutsolov6", CustomTestExtension.pointsSum, fails);
////            httpService.sendTestResult("kaloyan_dutsolov6", CustomTestExtension.pointsSum, globalFails);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
//}


    public static void main(String[] args) {
        TestFailService failService = new TestFailService();
        SummaryGeneratingListener listener = runTests();
        TestExecutionSummary summary = listener.getSummary();
        List<FailedTestCase> fails = failService.formatFailures(summary.getFailures());
        failService.printFails(fails);

        System.out.println("Points won:");
        System.out.println(CustomTestExtension.pointsSum);

        HttpService httpService = new HttpService();
        try {
            httpService.sendTestResult(args[0], CustomTestExtension.pointsSum, fails);
//            httpService.sendTestResult(args[0], CustomTestExtension.pointsSum, globalFails);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
