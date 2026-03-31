package ru.practicum.shareit.util.exception;

import java.time.LocalDateTime;

public class ErrorBody {
    private String name;
    private String description;
    private LocalDateTime time;

    public ErrorBody(String name, String description) {
        this.name = name;
        this.description = description;
        this.time = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
