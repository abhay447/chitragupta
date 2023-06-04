package com.chitragupta.commons.druid;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;

public class DruidClient {
    private final String druidCoordinatorUrl;

    public DruidClient(String druidCoordinatorUrl) {
        this.druidCoordinatorUrl = druidCoordinatorUrl;
    }

    public void submitIngestionSpec(String ingestionSpec) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final String druidSupervisorUrl = druidCoordinatorUrl + "/druid/indexer/v1/supervisor";
            System.out.println(druidSupervisorUrl);
            // Create an HttpPost request with the URL
            HttpPost httpPost = new HttpPost(druidSupervisorUrl);

            // Set the request body
            StringEntity requestEntity = new StringEntity(ingestionSpec);
            httpPost.setEntity(requestEntity);
            httpPost.setHeader("Content-Type", "application/json");

            // Execute the request
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                // Check the response status code
                int statusCode = response.getCode();

                if (statusCode == HttpStatus.SC_OK) {
                    // Get the response body
                    HttpEntity responseEntity = response.getEntity();
                    String responseBody = EntityUtils.toString(responseEntity);

                    // Handle the response
                    System.out.println("Response Body: " + responseBody);
                } else {
                    System.err.println("Request failed with status code: " + statusCode);
                }
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

}
