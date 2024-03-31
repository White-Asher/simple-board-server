package com.whiteasher.simpleboard.board.Repository;

import com.whiteasher.simpleboard.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository  extends JpaRepository<Board, Long> {
    Board findByNo(Long no);
}
