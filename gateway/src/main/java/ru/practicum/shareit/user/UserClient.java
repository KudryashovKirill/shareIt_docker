package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

@Component
public class UserClient extends BaseClient {
    private final static String API_PREFIX = "/users";

    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<Object> create(UserInputDto userInputDto) {
        return post("", userInputDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@RequestBody UserInputDto userInputDto, @PathVariable Long userId) {
        return patch("/" + userId, userId, userInputDto);
    }

    @GetMapping("{userId}")
    public ResponseEntity<Object> getById(@PathVariable Long userId) {
        return get("/" + userId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAll() {
        return get("");
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Object> delete(@PathVariable Long userId) {
        return delete("/" + userId, userId);
    }
}
