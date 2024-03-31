package com.whiteasher.simpleboard.board.service;

import com.whiteasher.simpleboard.board.dto.BoardDto;
import com.whiteasher.simpleboard.board.entity.Board;

public interface BoardService {
    BoardDto.Response retrieveBoardByNo(Long no);
    BoardDto.Response insertBoard(BoardDto.Request request);
    void deleteBoard(Long no);
}
