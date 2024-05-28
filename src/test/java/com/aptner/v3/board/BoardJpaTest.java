//package com.aptner.v3.board;
//
//import com.aptner.v3.board.category.BoardGroup;
//import com.aptner.v3.board.common_post.CommonPostRepository;
//import com.aptner.v3.board.complain.Complain;
//import com.aptner.v3.board.free_post.domain.FreePost;
//import com.aptner.v3.board.notice_post.domain.NoticePost;
//import com.aptner.v3.global.config.JpaConfig;
//import com.aptner.v3.member.Member;
//import com.aptner.v3.member.MemberRole;
//import com.aptner.v3.member.repository.MemberRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.then;
//
//@DataJpaTest
//@Import(JpaConfig.class)
//@AutoConfigureTestDatabase
//public class BoardJpaTest {
//    @Autowired
//    CommonPostRepository<NoticePost> noticePostRepository;
//    @Autowired
//    CommonPostRepository<FreePost> freePostRepository;
//    @Autowired
//    CommonPostRepository<Complain> complainPostRepository;
//
//    @Autowired
//    MemberRepository memberRepository;
//
//    @Test
//    @DisplayName("게시판 별 조회 테스트 - DType = BoardGroup")
//    void findByDtype() {
//
//        // Given
//        String Dtype = BoardGroup.NOTICES.name();
//        String keyword = "title";
//
//        Pageable pageable = Pageable.ofSize(10);
//        FreePost post1 = createFreePost("title1", "no content1");
//        NoticePost post2 = createNoticePost("title2", "no content2");
//        Complain post3 = createComplainPost("title3", "no content3");
//
//        given(noticePostRepository.findByDtype(Dtype, pageable))
//                .willReturn(new PageImpl<>(List.of(post2), pageable, 1));
//
//        then(noticePostRepository).should().findByDtype(Dtype, pageable);
//    }
//
//
//    private NoticePost createNoticePost(String title, String content) {
//        createUser("user");
//        NoticePost noticePost = new NoticePost(title, content, LocalDateTime.now());
//        return noticePostRepository.save(noticePost);
//    }
//
//    private FreePost createFreePost(String title, String content) {
//        createUser("user");
//        FreePost freePost = new FreePost(title, content, LocalDateTime.now(), "system");
//        return freePostRepository.save(freePost);
//    }
//
//    private Complain createComplainPost(String title, String content) {
//        createUser("user");
//        Complain complainPost = new Complain(title, content);
//        return complainPostRepository.save(complainPost);
//    }
//
//    public Member createUser(String username) {
//        return memberRepository.findByUsername(username).orElseGet(() -> {
//            Member user = Member.of(username, "p@ssword", List.of(MemberRole.ROLE_USER));
//            return memberRepository.save(user);
//        });
//    }
//}
