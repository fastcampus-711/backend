package com.aptner.v3.board.common.report;

import com.aptner.v3.board.common.reaction.domain.Reaction;
import com.aptner.v3.board.common.report.domain.Report;
import com.aptner.v3.board.common_post.dto.CommonPostDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportRepository<E extends Report> extends JpaRepository<E, Long> {
    // exists
/*    Optional<E> findByUserIdAndTargetIdAndDtype(long userId, long targetId, String postReport);

    // post get
    List<E> findByUserIdAndDtype(long memberId, String commentReport);*/

    Optional<E> findByUserIdAndTargetId(Long userId, Long targetId);
}
