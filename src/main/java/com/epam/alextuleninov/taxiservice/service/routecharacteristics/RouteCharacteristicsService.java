package com.epam.alextuleninov.taxiservice.service.routecharacteristics;

import com.epam.alextuleninov.taxiservice.config.properties.PropertiesConfig;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.data.route.RouteCharacteristicsResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Class for determining the characteristics of the route depending on the user's request.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public class RouteCharacteristicsService implements RouteCharacteristics {

    /**
     * Determine the characteristics of the route depending on the user's request.
     *
     * @param request request from user
     */
    @Override
    public RouteCharacteristicsResponse getRouteCharacteristics(OrderRequest request) {
        String requestString = buildQueryString(request);

        HttpClient CLIENT = HttpClient.newHttpClient();

        URI uri = URI.create(requestString);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("accept", "application/json")
                .header("Accept-Charset", "utf-8")
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = null;
        try {
            response = CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        assert response != null;
        JSONObject jsonObject = new JSONObject(response.body());
        JSONArray resourceSets = jsonObject.getJSONArray("resourceSets");
        JSONObject resourceSet = resourceSets.getJSONObject(0);
        JSONObject route = resourceSet.getJSONArray("resources").getJSONObject(0);
        double travelDistance = route.getDouble("travelDistance");
        int travelDuration = route.getInt("travelDuration");

        return new RouteCharacteristicsResponse(travelDistance, travelDuration);
    }

    /**
     * Build query string.
     *
     * @param request request from user
     */
    private static String buildQueryString(OrderRequest request) {
        String start = request.startTravel().replaceAll(" ", "%20");
        String end = request.endTravel().replaceAll(" ", "%20");
        String key = new PropertiesConfig().properties().getProperty("key");
        return String.format(
                "https://dev.virtualearth.net/REST/V1/Routes/Driving?wp.0=%s&wp.1=%s&avoid=minimizeTolls&key=%s",
                start,
                end,
                key);
    }
}
