package tr.com.oderopay;

import com.google.gson.Gson;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.jwk.JWK;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.DefaultHttpClientConnectionOperator;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.TlsSocketStrategy;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.URIScheme;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import tr.com.oderopay.model.garantipay.request.GarantiPayInitRequest;
import tr.com.oderopay.model.garantipay.request.GarantiPayInitRequestData;
import tr.com.oderopay.model.garantipay.request.GarantiPayInitRequestDataPrice;
import tr.com.oderopay.model.garantipay.request.GarantiPayInitRequestMeta;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.UUID;

@Slf4j
public class Main {
    private static final Gson GSON = new Gson();
    private static final String initUrl = "https://garantipay-test.garantibbva.com.tr/api/garantipay/v0/init";

    public static void main(String[] args) {
        var request = getGarantiPayInitRequest();
        var payload = getPayload(request);
        var jwk = getJwk();
        log.info(jwk.toJSONString());
        var body = jwsHash(payload, jwk);
        log.info(body);
        String response = post(initUrl, body);
        log.info(response);
    }

    private static String jwsHash(Payload payload, JWK jwk) {
        final var jwsHeader = new JWSHeader
                .Builder(JWSAlgorithm.HS256)
                .keyID(jwk.getKeyID())
                .build();

        final var jws = new JWSObject(jwsHeader, payload);

        try {
            jws.sign(new MACSigner(jwk.toOctetSequenceKey()));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

        return jws.serialize();
    }

    private static JWK getJwk() {
        String jwkJson = """
                {
                    "kty": "oct",
                    "kid": "7b76e130-73de-4562-9c20-ad5e983e22d8",
                    "k": "8ut0KLzijwH9m-0L4EUGerwavnYhSUmsn4B5-VNpqig",
                    "alg":"HS256"
                }
                """;
        try {
            return JWK.parse(jwkJson);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static Payload getPayload(GarantiPayInitRequest request) {
        String requestJson = GSON.toJson(request);
        log.info(requestJson);
        return new Payload(requestJson);
    }

    private static GarantiPayInitRequest getGarantiPayInitRequest() {
        return GarantiPayInitRequest.builder()
                .meta(GarantiPayInitRequestMeta.builder()
                        .id(UUID.randomUUID().toString())
                        .timestamp(System.currentTimeMillis())
                        .build())
                .data(GarantiPayInitRequestData.builder()
                        .orderId("467645734901236734")
                        .price(GarantiPayInitRequestDataPrice.builder()
                                .amount("10000")
                                .currency("949")
                                .build())
                        .build())
                .build();
    }

    private static CloseableHttpClient createUnsafeHttpClient() {
        final TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
        final SSLContext sslContext;

        try {
            sslContext = SSLContexts
                    .custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();

        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            throw new RuntimeException("error while creating self-trusted certificates", e);
        }

        final var clientTlsStrategy = new DefaultClientTlsStrategy(sslContext, NoopHostnameVerifier.INSTANCE);
        final var connectionOperator = new DefaultHttpClientConnectionOperator(null, null,
                RegistryBuilder.<TlsSocketStrategy>create()
                        .register(URIScheme.HTTPS.id, clientTlsStrategy)
                        .build());
        final var connectionManager = new BasicHttpClientConnectionManager(connectionOperator, null);

        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();
    }

    public static String post(String url, String body) {
        HttpClient client = createUnsafeHttpClient();
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "text/plain");
        post.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
        try (ClassicHttpResponse response = (ClassicHttpResponse) client.execute(post)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return new String(entity.getContent().readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new RuntimeException("Unsuccessful POST request", e);
        }
        return null;
    }

}
