package cz.kec.okrepro;

import io.helidon.common.http.Http;
import io.helidon.logging.common.LogConfig;
import io.helidon.nima.webserver.WebServer;
import io.helidon.nima.webserver.http.HttpRouting;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        LogConfig.configureRuntime();
        WebServer server = WebServer.builder()
                .host("localhost")
                .port(8080)
                .addRouting(() -> HttpRouting.builder()
                        .route(Http.Method.GET, "/test", (req, res) -> {
                            // Force timeout
                            Thread.sleep(10000);
                            res.send("OK");
                        })
                        .build())
                .build();
        server.start();

        OkHttpClient client = new OkHttpClient.Builder()
                .protocols(List.of(Protocol.H2_PRIOR_KNOWLEDGE))
                .build();

        Request.Builder builder = new Request.Builder().url("http://localhost:" + server.port() + "/test");

        try (Response postBookRes = client
                .newCall(builder.get().build())
                .execute()) {
            assert postBookRes.code() == 200;
            assert Objects.requireNonNull(postBookRes.body()).string().equals("OK");
            assert postBookRes.protocol() == Protocol.HTTP_2;
        } finally {
            server.stop();
        }
    }
}
