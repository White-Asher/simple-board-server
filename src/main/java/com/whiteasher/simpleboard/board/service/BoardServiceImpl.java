package com.whiteasher.simpleboard.board.service;

import com.whiteasher.simpleboard.auth.entity.UserPrincipal;
import com.whiteasher.simpleboard.board.Repository.BoardRepository;
import com.whiteasher.simpleboard.board.dto.BoardDto;
import com.whiteasher.simpleboard.board.entity.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;

    @Override
    public BoardDto.Response retrieveBoardByNo(Long no, UserPrincipal userPrincipal) {
        Board retrieveResult = boardRepository.findByNo(no);
        return new BoardDto.Response(retrieveResult, userPrincipal);
    }

    @Override
    public BoardDto.Response insertBoard(BoardDto.Request request, UserPrincipal userPrincipal) {
        Board board = request.toEntity(userPrincipal);
        Board result = boardRepository.save(board);
        return new BoardDto.Response(result, userPrincipal);
    }

    @Override
    public void deleteBoard(Long no, UserPrincipal userPrincipal) {
        boardRepository.deleteById(no);
        return;
    }

}
