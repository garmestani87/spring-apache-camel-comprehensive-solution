package com.garm.servicegateway.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.garm.servicegateway.base.Response;
import com.garm.servicegateway.base.ResponseCode;
import com.garm.servicegateway.exception.GatewayExceptionModel;
import lombok.SneakyThrows;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class GatewayUtils {
    public static <T> Response<T, T> buildBody(T res) {
        Response<T, T> response = new Response<>();
        response.setResponse(res);
        return response;
    }

    public static <T> void buildBody(Exchange exchange, T res) {
        Response<T, T> response = new Response<>();
        response.setResponse(res);
        exchange.getIn().setBody(buildBody(res));
    }

    public static void trustSelfSignedSSL() {
        try {
            SSLContext ctx = SSLContext.getInstance("SSL");
            X509TrustManager tm = new X509TrustManager() {

                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static <O> O getObjectFromResource(String resource, String prefix, Class<O> clazz) throws IOException {
        Properties p = new Properties();
        p.load(GatewayUtils.class.getResourceAsStream(resource));
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        p.forEach((k, v) -> {
            String propKey = k.toString();
            if (propKey.startsWith(prefix)) {
                node.set(propKey.replaceFirst(prefix, StringUtils.EMPTY), JsonNodeFactory.instance.textNode(v.toString()));
            }
        });
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(node.toString(), clazz);
    }

    public static <O> O getObjectFromResource(String resource, Class<O> clazz) throws IOException {
        Properties p = new Properties();
        p.load(GatewayUtils.class.getResourceAsStream(resource));
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        p.forEach((k, v) -> {
            String propKey = k.toString();
            node.set(propKey, JsonNodeFactory.instance.textNode(v.toString()));
        });
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(node.toString(), clazz);
    }

    public static void handleException(Exchange exchange, GatewayExceptionModel exceptionModel) {
        Response<Object, Object> response = new Response<>();
        response.setErrorDetail(exceptionModel);
        response.setResponseCode(ResponseCode.EXCEPTION);
        exchange.getIn().setBody(response);
    }


    public static void extractToken(Exchange exchange) throws java.io.IOException {
        Response response = new ObjectMapper()
                .readValue(new ObjectMapper().writeValueAsBytes(exchange.getIn().getBody()), Response.class);
        if (Objects.nonNull(response) && response.getResponseCode().equals(ResponseCode.GENERAL)) {
            String removeQuotationsFromResponse = ((String) response.getResponse()).replaceAll("\"", "");
            buildBody(exchange, removeQuotationsFromResponse);
        }
    }

    @SneakyThrows
    public static <O> String convertToFormUrlEncoded(O inputObject) {
        final Map<String, String> input =
                new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(inputObject), Map.class);
        StringBuilder stringBuilder = new StringBuilder();
        if (CollectionUtils.isEmpty(input))
            return stringBuilder.toString();
        input.entrySet().stream().filter(map -> !StringUtils.isEmpty(map.getValue())).forEach(map -> {
            stringBuilder.append(map.getKey());
            stringBuilder.append(SpecialCharacter.EQUALS);
            try {
                stringBuilder.append(URLEncoder.encode(map.getValue(), StandardCharsets.UTF_8.toString()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            stringBuilder.append(SpecialCharacter.AND);
        });
        stringBuilder.replace(stringBuilder.lastIndexOf(SpecialCharacter.AND), stringBuilder.length(), "");
        return stringBuilder.toString();
    }

}
