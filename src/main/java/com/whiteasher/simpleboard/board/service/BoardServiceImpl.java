package com.whiteasher.simpleboard.board.service;

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
    public BoardDto.Response retrieveBoardByNo(Long no) {
        Board retrieveResult = boardRepository.findByNo(no);
        return new BoardDto.Response(retrieveResult);
    }

    @Override
    public BoardDto.Response insertBoard(BoardDto.Request request) {
        Board board = request.toEntity();
        Board result = boardRepository.save(board);
        return new BoardDto.Response(result);
    }

    @Override
    public void deleteBoard(Long no) {
        boardRepository.deleteById(no);
        return;
    }

}
