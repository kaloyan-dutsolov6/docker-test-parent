package util;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

public class AccessTokenUtil {

    private static final String CLIENT_ID = System.getenv("client_id");
    private static final String CLIENT_SECRET = System.getenv("client_secret");

    private final WebClient webClient = WebClient.builder().build();

    public String getAccessToken() {
        String encodedClientData =
                Base64Utils.encodeToString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes());

        return Objects.requireNonNull(webClient.post()
                        .uri("host.docker.internal:8180/auth/realms/dojo-realm/protocol/openid-connect/token")
                        .header("Authorization", "Basic " + encodedClientData)
                        .body(BodyInserters.fromFormData("grant_type", "client_credentials"))
                        .retrieve()
                        .bodyToMono(JsonNode.class)
                        .map(json -> json.get("access_token"))
                        .block())
                .toString().replace("\"", "");
    }
}