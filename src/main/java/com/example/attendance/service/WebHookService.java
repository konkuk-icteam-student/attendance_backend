package com.example.attendance.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WebHookService {
    private final WebClient webClient;

    public WebHookService(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.baseUrl("https://teamroom.nate.com").build();
    }

//     String webhookUrl = "/api/webhook/4e71dbbb/8KG8Vhn3dJ9zZG1Y0j9qX0hs"; // 팀룸 준형
    String webhookUrl = "/api/webhook/2573721c/vlWBbePJ6k7kZ9rJMAvYapQe"; // 팀룸 동현쌤

    @Async
    public void sendWebhookMessage(String userName, String status){
        if(status == "1") status = "출근";
        else status = "퇴근";
        String message = userName + "님이 " + status + "하였습니다.";
        String payload = "{\"text\": \"" + message + "\"}";

//        for(int i=0; i<2000; i++)
//        {
//            System.out.println("delay\n");
//        }

        webClient.post()
                .uri(uriBuilder -> uriBuilder.path(webhookUrl).build())
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
