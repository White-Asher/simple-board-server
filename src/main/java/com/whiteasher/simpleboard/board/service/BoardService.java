package com.whiteasher.simpleboard.board.service;

import com.whiteasher.simpleboard.auth.entity.UserPrincipal;
import com.whiteasher.simpleboard.board.dto.BoardDto;
import com.whiteasher.simpleboard.board.entity.Board;

public interface BoardService {
    BoardDto.Response retrieveBoardByNo(Long no, UserPrincipal userPrincipal);
    BoardDto.Response insertBoard(BoardDto.Request request, UserPrincipal userPrincipal);
    void deleteBoard(Long no, UserPrincipal userPrincipal);
}
