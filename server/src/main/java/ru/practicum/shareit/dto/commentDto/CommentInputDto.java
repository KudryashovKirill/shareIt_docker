package ru.practicum.shareit.dto.commentDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentInputDto {
    @NotNull(message = "text of comment must be not null")
    @NotBlank(message = "text of comment must be not blank")
    private String text;
}
