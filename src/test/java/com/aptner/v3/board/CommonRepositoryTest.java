package com.aptner.v3.board;

import com.aptner.v3.board.commons.CommonPostRepository;
import com.aptner.v3.board.complains.Complain;
import com.aptner.v3.board.frees.domain.FreePost;
import com.aptner.v3.board.markets.Market;
import com.aptner.v3.board.notices.domain.NoticePost;
import com.aptner.v3.board.qnas.Qna;
import com.aptner.v3.global.config.JpaConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
@Import(JpaConfig.class)
@AutoConfigureTestDatabase
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class CommonRepositoryTest {
    private static final Logger log = LoggerFactory.getLogger(CommonRepositoryTest.class);

    @Autowired
    private CommonPostRepository<Complain> complainPostRepository;

    @Autowired
    private CommonPostRepository<FreePost> freePostRepository;

    @Autowired
    private CommonPostRepository<Market> marketPostRepository;

    @Autowired
    private CommonPostRepository<NoticePost> noticePostRepository;

    @Autowired
    private CommonPostRepository<Qna> qnaPostRepository;

    @BeforeEach
    public void setUp() {

        for (int i = 0; i < 1; i++) {
            Complain post1 = new Complain();
            post1.setTitle("Spring Boot Test");
            post1.setContent("Content 1");
            post1.setCategoryId(1L);
            post1.setBoardGroupId(1L);
            complainPostRepository.save(post1);
        }

        for (int i = 0; i < 1; i++) {
            FreePost post1 = new FreePost();
            post1.setTitle("Spring Boot Test");
            post1.setContent("Content 1");
            post1.setCategoryId(1L);
            post1.setBoardGroupId(1L);
            freePostRepository.save(post1);
        }

        for (int i = 0; i < 1; i++) {
            Market post1 = new Market();
            post1.setTitle("Spring Boot Test");
            post1.setContent("Content 1");
            post1.setCategoryId(1L);
            post1.setBoardGroupId(1L);
            marketPostRepository.save(post1);
        }

        for (int i = 0; i < 1; i++) {
            NoticePost post1 = new NoticePost();
            post1.setTitle("Spring Boot Test");
            post1.setContent("Content 1");
            post1.setCategoryId(1L);
            post1.setBoardGroupId(1L);
            noticePostRepository.save(post1);
        }

        for (int i = 0; i < 1; i++) {
            Qna post1 = new Qna();
            post1.setTitle("Spring Boot Test");
            post1.setContent("Content 1");
            post1.setCategoryId(1L);
            post1.setBoardGroupId(1L);
            qnaPostRepository.save(post1);
        }

    }

    @Test
    @DisplayName("save 테스트")
    public void testComplainSaveAndFindById() {

        // when
        Complain post = new Complain();
        post.setTitle(post.getClass().getSimpleName() + " Title");
        post.setContent(post.getClass().getSimpleName() + " Content");
        post.setCategoryId(1L);
        post.setBoardGroupId(1L);
        complainPostRepository.save(post);

        // given
        Optional<Complain> foundPost = complainPostRepository.findById(post.getId());

        // then
        assertTrue(foundPost.isPresent());
        assertEquals(post.getTitle(), foundPost.get().getTitle());
    }

    @Test
    @DisplayName("조회 함수 테스트")
    public void testFindByDtype() {

        // when
        Complain post = new Complain();
        post.setTitle(post.getClass().getSimpleName() + " Title");
        post.setContent(post.getClass().getSimpleName() + " Content");
        post.setCategoryId(1L);
        post.setBoardGroupId(1L);
        complainPostRepository.save(post);

        // then
        List<Complain> all = complainPostRepository.findAll();
        assertTrue(!all.isEmpty());

        List<Complain> complain = complainPostRepository.findByDtype("COMPLAIN");
        assertTrue(!complain.isEmpty());
    }

    @Test
    public void testFindByTitleContaining() {
        // given
        Pageable pageable = PageRequest.of(0, 3);
        Page<Complain> result = complainPostRepository.findByTitleContaining("Test", pageable);

        // then
        assertTrue(result.getTotalElements() > 3);
    }


    @Test
    @DisplayName("댓글의 PostId 조회")
    public void testFindByTitleContainingAndDtype() {

        // when
        Market post = new Market();
        post.setTitle(post.getClass().getSimpleName() + " Title");
        post.setContent(post.getClass().getSimpleName() + " Content");
        post.setCategoryId(1L);
        post.setBoardGroupId(1L);
        marketPostRepository.save(post);

        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Market> result = marketPostRepository.findByTitleContainingAndDtype("Test", "MARKETS", pageable); // Adjust the dtype value based on your entities

        // then
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("댓글의 PostId 조회")
    public void testFindByComments_CommonPostId() {

        // when
        Qna post = new Qna();
        post.setTitle(post.getClass().getSimpleName() + " Title");
        post.setContent(post.getClass().getSimpleName() + " Content");
        post.setCategoryId(1L);
        post.setBoardGroupId(1L);
        qnaPostRepository.save(post);

        Optional<Qna> result = qnaPostRepository.findByComments_CommonPostId(post.getId());
        assertTrue(result.isPresent());
    }


    @Test
    @DisplayName("카테고리ID 조회")
    public void testFindByCategoryId() {
        Complain post = new Complain();
        post.setTitle(post.getClass().getSimpleName() + " Title");
        post.setContent(post.getClass().getSimpleName() + " Content");
        post.setCategoryId(1L);
        post.setBoardGroupId(1L);
        complainPostRepository.save(post);

        Complain post1 = new Complain();
        post1.setTitle(post.getClass().getSimpleName() + " Title");
        post1.setContent(post.getClass().getSimpleName() + " Content");
        post1.setCategoryId(2L);
        post1.setBoardGroupId(1L);
        complainPostRepository.save(post1);

        List<Complain> foundPosts = complainPostRepository.findByCategoryId(1L);
        assertThat(foundPosts.size()).isGreaterThan(1);
    }
}
