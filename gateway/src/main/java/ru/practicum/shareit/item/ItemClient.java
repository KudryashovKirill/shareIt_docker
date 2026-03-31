package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.comment.CommentInputDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ItemClient extends BaseClient {
    private final static String API_PREFIX = "/items";

    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<Object> create(ItemInputDto itemInputDto, Long userId) {
        return post("", userId, itemInputDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(CommentInputDto commentInputDto,
                                             Long itemId,
                                             Long userId) {
        return post("/" + itemId + "/comment", userId, commentInputDto);
    }


    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(ItemInputDto itemInputDto,
                                         Long itemId,
                                         Long userId) {
        return patch("/" + itemId, userId, itemInputDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(Long itemId,
                                          Long userId) {
        return get("/" + itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAll(Long userId, Integer from, Integer size) {
        Map<String, Object> params = new HashMap();
        params.put("from", from);
        params.put("size", size);
        return get("", userId, params);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(
            String text,
            Long userId,
            Integer from,
            Integer size) {

        Map<String, Object> params = new HashMap();
        params.put("text", text);
        params.put("from", from);
        params.put("size", size);

        if (text.isBlank()) {
            return ResponseEntity.ok(List.of());
        }

        return get("/search?text={text}&from={from}&size={size}", userId, params);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> delete(@PathVariable Long itemId) {
        return delete("/" + itemId);
    }
}
