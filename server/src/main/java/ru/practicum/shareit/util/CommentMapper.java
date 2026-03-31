package ru.practicum.shareit.util;

import org.mapstruct.Mapper;
import ru.practicum.shareit.dto.commentDto.CommentInputDto;
import ru.practicum.shareit.dto.commentDto.CommentOutputDto;
import ru.practicum.shareit.model.Comment;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CommentMapper {
    Comment toEntity(CommentInputDto dto);

    CommentOutputDto toDto(Comment comment);
}
