package org.sonar.plugins.crowd.integration;

import okhttp3.*;
import org.hamcrest.core.StringContains;
import org.junit.Test;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;

import static org.hamcrest.MatcherAssert.assertThat;

public class CrowdIT {
    private final CookieJar cookieJar = new JavaNetCookieJar(getCookieManager());
    private final OkHttpClient client = new OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .build();

    private CookieManager getCookieManager() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        return cookieManager;
    }

    private HttpUrl getUrl(String pathSegments) {
        return new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(9000)
                .addPathSegment("api")
                .addPathSegments(pathSegments)
                .build();
    }

    public String post(String pathSegments, RequestBody formBody) throws IOException {
        HttpUrl url = getUrl(pathSegments);

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    public String get(String pathSegments) throws IOException {
        HttpUrl url = getUrl(pathSegments);

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                if (response.code() == 401) throw new AuthenticationException();
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }

    public String get(String pathSegments, String queryParam, String value) throws IOException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(9000)
                .addPathSegment("api")
                .addPathSegments(pathSegments)
                .addQueryParameter(queryParam, value)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                if (response.code() == 401) throw new AuthenticationException();
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }

    public void login(String username, String password) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("login", username)
                .add("password", password)
                .build();

        post("authentication/login", formBody);
    }

    public void logout() throws Exception {
        RequestBody formBody = new FormBody.Builder().build();
        post("authentication/logout", formBody);
    }

    @Test
    public void testCrowdGroupSync() throws Exception {
        login("crowdadmin", "admin");
        String groups = get("users/groups", "login", "crowdadmin");
        assertThat(groups, new StringContains("\"name\":\"sonar-administrators\""));
    }
}
