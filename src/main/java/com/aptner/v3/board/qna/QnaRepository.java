package com.aptner.v3.board.qna;

import com.aptner.v3.board.common_post.CommonPostRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("qnaRepository")
public interface QnaRepository extends CommonPostRepository<Qna> {

    Page<Qna> findByDtypeAndStatus(String dtype, QnaStatus status, Pageable pageable);
    Page<Qna> findByDtypeAndCategoryIdAndStatus(String dtype, Long CategoryId, QnaStatus status, Pageable pageable);

}
