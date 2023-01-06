package client;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class StatsClient {

    private final WebClient webClient;

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl) {
        webClient = WebClient.builder()
                .baseUrl(serverUrl)
                .build();
    }

    @Value("${app-name}")
    private String appName;

    public void saveStats(HttpServletRequest request) {

        String ip = request.getRemoteAddr();
        String uri = request.getRequestURI();
        Stats stats = new Stats(appName, uri, ip);
        webClient.post()
                .uri("/hit")
                .body(BodyInserters.fromValue(stats))
                .retrieve()
                .bodyToMono(Stats.class)
                .block();
    }

    public List<ViewStats> getViews(String start, String end,
                                    List<String> uris, Boolean unique) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", start.replace("T", " "))
                        .queryParam("end", end.replace("T", " "))
                        .queryParam("uris", String.join(", ", uris).replace("{", "")
                                .replace("}", ""))
                        .queryParam("unique", unique)
                        .build())
                .retrieve()
                .bodyToFlux(ViewStats.class)
                .collectList()
                .block();
    }
}
