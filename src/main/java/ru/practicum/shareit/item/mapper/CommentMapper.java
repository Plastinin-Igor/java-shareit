package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.mapper.UserMapper;

public final class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setItem(comment.getItem() != null ? ItemMapper.toItemDto(comment.getItem()) : null);
        commentDto.setAuthor(comment.getAuthor() != null ? UserMapper.toUserDto(comment.getAuthor()) : null);
        commentDto.setCreated(comment.getCreated());
        return commentDto;
    }

    public static Comment toComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setText(commentDto.getText());
        comment.setItem(commentDto.getItem() != null ? ItemMapper.toItem(commentDto.getItem()) : null);
        comment.setAuthor(commentDto.getAuthor() != null ? UserMapper.toUser(commentDto.getAuthor()) : null);
        comment.setCreated(commentDto.getCreated());
        return comment;
    }

    public static Comment toCommentFromCreateDto(CommentCreateDto commentCreateDto) {
        Comment comment = new Comment();
        comment.setText(commentCreateDto.getText());
        return comment;
    }

}
