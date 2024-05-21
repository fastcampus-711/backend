package com.aptner.v3.board.common.reaction;

import com.aptner.v3.board.comment.Comment;
import com.aptner.v3.board.comment.CommentService;
import com.aptner.v3.board.common.reaction.domain.CommentReaction;
import com.aptner.v3.board.common.reaction.domain.PostReaction;
import com.aptner.v3.board.common.reaction.dto.ReactionDto;
import com.aptner.v3.board.common.reaction.dto.ReactionType;
import com.aptner.v3.board.common.reaction.service.CommentReactionService;
import com.aptner.v3.board.common.reaction.service.PostReactionService;
import com.aptner.v3.board.common.reaction.service.ReactionService;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import net.minidev.json.JSONObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Rollback(value = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReactionServiceTest {
    private final ReactionRepository<PostReaction> postReactionRepository;
    private final ReactionRepository<CommentReaction> commentReactionRepository;
    private final ReactionService<CommonPost, PostReaction> postReactionService;
    private final ReactionService<Comment, CommentReaction> commentReactionService;

    private ObjectMapper objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    @Autowired
    ReactionServiceTest(ReactionRepository<PostReaction> postReactionRepository,
                        ReactionRepository<CommentReaction> commentReactionRepository,
                        CommonPostService<CommonPost, CommonPostDto.Request, CommonPostDto.Response> commonPostService,
                        CommentService commentService) {
        this.postReactionRepository = postReactionRepository;
        this.commentReactionRepository = commentReactionRepository;
        this.postReactionService = new PostReactionService(commonPostService, postReactionRepository);
        this.commentReactionService = new CommentReactionService(commentService, commentReactionRepository);
    }

    @Test
    void 게시글에_공감_버튼_클릭_시_DB에_저장() throws JsonProcessingException {
        //given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_id", 1);
        jsonObject.put("reaction_target", "POST");
        jsonObject.put("reaction_type", "GOOD");
        jsonObject.put("target_id", 1);

        ReactionDto.Request request = objectMapper.readValue(jsonObject.toString(), ReactionDto.Request.class);

        //when
        postReactionService.acceptReaction(request);

        //then
        Assertions.assertThat(
                        postReactionRepository.findByUserIdAndTargetId(1, 1)
                ).isPresent()
                .get()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("userId", 1L)
                .hasFieldOrPropertyWithValue("reactionType", ReactionType.GOOD)
                .hasFieldOrPropertyWithValue("targetId", 1L)
                .hasFieldOrProperty("createdAt");
    }

    @Test
    void 게시글에_비공감_버튼_클릭_시_DB에_저장() throws JsonProcessingException {
        //given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_id", 1);
        jsonObject.put("reaction_target", "POST");
        jsonObject.put("reaction_type", "BAD");
        jsonObject.put("target_id", 1);

        ReactionDto.Request request = objectMapper.readValue(jsonObject.toString(), ReactionDto.Request.class);

        System.out.println(request.getReactionType());
        //when
        postReactionService.acceptReaction(request);

        //then
        Assertions.assertThat(
                        postReactionRepository.findByUserIdAndTargetId(1, 1)
                ).isPresent()
                .get()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("userId", 1L)
                .hasFieldOrPropertyWithValue("reactionType", ReactionType.BAD)
                .hasFieldOrPropertyWithValue("targetId", 1L)
                .hasFieldOrProperty("createdAt");
    }

    @Test
    void 게시글에_비공감or공감_버튼_재클릭_시_DB에_DEFAULT_저장() throws JsonProcessingException {
        //given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_id", 1);
        jsonObject.put("reaction_target", "POST");
        jsonObject.put("reaction_type", "DEFAULT");
        jsonObject.put("target_id", 1);

        ReactionDto.Request request = objectMapper.readValue(jsonObject.toString(), ReactionDto.Request.class);

        //when
        postReactionService.acceptReaction(request);

        //then
        Assertions.assertThat(
                        postReactionRepository.findByUserIdAndTargetId(1, 1)
                ).isPresent()
                .get()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("userId", 1L)
                .hasFieldOrPropertyWithValue("reactionType", ReactionType.DEFAULT)
                .hasFieldOrPropertyWithValue("targetId", 1L)
                .hasFieldOrProperty("createdAt");
    }

    @Test
    void 댓글에_공감_버튼_클릭_시_DB에_저장() throws JsonProcessingException {
        //given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_id", 1);
        jsonObject.put("reaction_target", "COMMENT");
        jsonObject.put("reaction_type", "GOOD");
        jsonObject.put("target_id", 1);

        ReactionDto.Request request = objectMapper.readValue(jsonObject.toString(), ReactionDto.Request.class);

        //when
        commentReactionService.acceptReaction(request);

        //then
        Assertions.assertThat(
                        commentReactionRepository.findByUserIdAndTargetId(1, 1)
                ).isPresent()
                .get()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("userId", 1L)
                .hasFieldOrPropertyWithValue("reactionType", ReactionType.GOOD)
                .hasFieldOrPropertyWithValue("targetId", 1L)
                .hasFieldOrProperty("createdAt");
    }

    @Test
    void 댓글에_비공감_버튼_클릭_시_DB에_저장() throws JsonProcessingException {
        //given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_id", 1);
        jsonObject.put("reaction_target", "COMMENT");
        jsonObject.put("reaction_type", "BAD");
        jsonObject.put("target_id", 1);

        ReactionDto.Request request = objectMapper.readValue(jsonObject.toString(), ReactionDto.Request.class);

        //when
        commentReactionService.acceptReaction(request);

        //then
        Assertions.assertThat(
                        commentReactionRepository.findByUserIdAndTargetId(1, 1)
                ).isPresent()
                .get()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("userId", 1L)
                .hasFieldOrPropertyWithValue("reactionType", ReactionType.BAD)
                .hasFieldOrPropertyWithValue("targetId", 1L)
                .hasFieldOrProperty("createdAt");
    }

    @Test
    void 댓글에_비공감or공감_버튼_재클릭_시_DB에_DEFAULT_저장() throws JsonProcessingException {
        //given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_id", 1);
        jsonObject.put("reaction_target", "COMMENT");
        jsonObject.put("reaction_type", "DEFAULT");
        jsonObject.put("target_id", 1);

        ReactionDto.Request request = objectMapper.readValue(jsonObject.toString(), ReactionDto.Request.class);

        //when
        commentReactionService.acceptReaction(request);

        //then
        Assertions.assertThat(
                        commentReactionRepository.findByUserIdAndTargetId(1, 1)
                ).isPresent()
                .get()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("userId", 1L)
                .hasFieldOrPropertyWithValue("reactionType", ReactionType.DEFAULT)
                .hasFieldOrPropertyWithValue("targetId", 1L)
                .hasFieldOrProperty("createdAt");
    }
}