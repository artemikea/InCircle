package com.incircle.service;

import com.incircle.domain.Notification;
import com.incircle.repo.INotifierService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
public class IConsoleNotifierService implements INotifierService {

    @Override
    public void send(Notification notification) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String url = new String("https://zvonok.com/manager/cabapi_external/api/v1/phones/call/");
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("public_key", "{public_key}")
                .queryParam("phone", "{phone}")
                .queryParam("campaign_id", "{campaign_id}")
                .queryParam("text", "{text}")
                .encode()
                .toUriString();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity request = new HttpEntity(headers);
        Map<String, String> params = new HashMap<>();
        params.put("public_key", "02aa0643cfc31ccfba0ed83068306361");
        String phone = notification.getContact().getPhone();
        params.put("phone", "+" + phone);
        params.put("campaign_id", "1226995097");
        params.put("text", notification.getText());
        restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                request,
                String.class,
                params);
        System.out.println("Notification message next: " + notification.getText());
    }
}
