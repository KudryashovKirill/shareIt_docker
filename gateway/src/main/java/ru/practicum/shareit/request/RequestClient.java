package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

import java.util.HashMap;
import java.util.Map;

@Component
public class RequestClient extends BaseClient {
    private final static String API_PREFIX = "/items";

    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    @PostMapping("/request")
    public ResponseEntity<Object> createItemRequest(ItemRequestInputDto itemRequestInputDto,
                                                    Long requestorId) {
        return post("/request", requestorId, itemRequestInputDto);
    }

    @GetMapping("/request")
    public ResponseEntity<Object> getRequestsWithAnswers(Long requestorId) {
        return get("/request", requestorId);
    }

    @GetMapping("requests/all")
    public ResponseEntity<Object> getRequestsByOtherUsers(Long from,
                                                          Long size,
                                                          Long userId) {
        Map<String, Object> params = new HashMap();
        params.put("from", from);
        params.put("size", size);
        return get("/requests/all", userId, params);
    }

    @GetMapping("requests/{id}")
    public ResponseEntity<Object> getRequest(Long requestorId,
                                             Long id) {
        return get("requests/" + id, requestorId);
    }
}
