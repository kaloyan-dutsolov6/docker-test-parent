package service;

import util.AccessTokenUtil;
import model.FailedTestCase;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.List;

public class HttpService {

    private static final String URL = "http:host.docker.internal:8081/test/result";
    private final OkHttpClient httpClient;
    private final AccessTokenUtil accessTokenUtil;

    public HttpService() {
        this.httpClient = new OkHttpClient();
        this.accessTokenUtil = new AccessTokenUtil();
    }

    public void sendTestResult(String username, int points, List<FailedTestCase> failedTestCases, String containerId) throws IOException {
        JSONObject testsResult = buildJsonTestsResultObject(username, points, failedTestCases, containerId);
        RequestBody body = buildRequestBody(testsResult);
        Request request = buildRequest(URL, body);

        httpClient.newCall(request).execute();
    }

    private Request buildRequest(String url, RequestBody body) {
        return new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + accessTokenUtil.getAccessToken())
                .post(body)
                .build();
    }

    private JSONObject buildJsonTestsResultObject(String username, int points, List<FailedTestCase> failedTestCases, String containerId) {
        JSONObject testsResult = new JSONObject();
        testsResult.putIfAbsent("username", username);
        testsResult.putIfAbsent("points", points);
        JSONArray failedCases = generateFailedTestCasesJsonArray(failedTestCases);
        testsResult.putIfAbsent("failedTestCases", failedCases);
        testsResult.putIfAbsent("containerId", containerId);

        return testsResult;
    }

    private JSONArray generateFailedTestCasesJsonArray(List<FailedTestCase> failedTestCases) {
        JSONArray failedCases = new JSONArray();
        failedTestCases.forEach(failedTestCase -> {
            JSONObject failedCase = new JSONObject();
            failedCase.putIfAbsent("methodName", failedTestCase.getMethodName());
            failedCase.putIfAbsent("expected", failedTestCase.getExpected());
            failedCases.add(failedCase);
        });
        return failedCases;
    }

    private RequestBody buildRequestBody(JSONObject testsResult) {
        MediaType json = MediaType.parse("application/json; charset=utf-8");
        return RequestBody.create(testsResult.toString(), json);
    }
}
