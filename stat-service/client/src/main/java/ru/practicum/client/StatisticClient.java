package ru.practicum.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.StatisticRequestDto;

import java.util.Map;

@Service
public class StatisticClient extends BaseClient {
    private static final String API_PREFIX = "";

    @Autowired
    public StatisticClient(@Value("${server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createQuery(StatisticRequestDto statisticRequestDto) {
        return post("/hit", statisticRequestDto);
    }

    public ResponseEntity<Object> getStat(String start, String end, String[] uris, boolean unique) {
        String[] urisInternal = uris != null ? uris : new String[0];

        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", urisInternal,
                "unique", unique
        );
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }
}