package ru.practicum.shareit.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentInputDto {
//    @NotNull(message = "text of comment must be not null")
//    @NotBlank(message = "text of comment must be not blank")
    String text;
}
