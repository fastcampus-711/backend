package com.aptner.v3.board.notice_post;

import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.notice_post.domain.NoticePost;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Qualifier("noticePostRepository")
public interface NoticePostRepository extends CommonPostRepository<NoticePost> {

    @Query("SELECT np FROM NoticePost np WHERE np.dtype = :dtype AND" +
            "(np.scheduleStartAt >= :startOfDay AND np.scheduleStartAt <= :endOfNextDay) OR " +
            "(np.scheduleEndAt >= :startOfDay AND np.scheduleEndAt <= :endOfNextDay)" +
            "AND deleted = false")
    List<NoticePost> findByScheduleRangeAndDtype(
            LocalDateTime startOfDay,
            LocalDateTime endOfNextDay,
            String dtype
    );

//    Page<NoticePost> findByIsDuptyAndDtype(String dtype);
}
