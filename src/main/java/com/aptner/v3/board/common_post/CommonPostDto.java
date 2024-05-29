package com.aptner.v3.board.common_post;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.CategoryCode;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.comment.CommentDto;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.dto.ReactionColumnsDto;
import com.aptner.v3.global.domain.BaseTimeDto;
import com.aptner.v3.global.util.MemberUtil;
import com.aptner.v3.global.util.ModelMapperUtil;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.dto.MemberDto;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class CommonPostDto extends BaseTimeDto {
    Long id;
    MemberDto memberDto;
    String title;
    String content;
    List<String> imageUrls;
    Long hits;
    ReactionColumnsDto reactionColumnsDto;
    Long countOfComments;
    boolean visible;
    BoardGroup boardGroup;
    CategoryDto categoryDto;

    public CommonPostDto(Long id
            , MemberDto memberDto
            , String title
            , String content
            , List<String> imageUrls
            , Long hits
            , ReactionColumnsDto reactionColumnsDto
            , Long countOfComments
            , boolean visible
            , BoardGroup boardGroup
            , CategoryDto categoryDto
            , String createdBy
            , LocalDateTime createdAt
            , String modifiedBy
            , LocalDateTime modifiedAt) {
        super(createdBy, createdAt, modifiedBy, modifiedAt);
        this.id = id;
        this.memberDto = memberDto;
        this.title = title;
        this.content = content;
        this.imageUrls = imageUrls;
        this.hits = hits;
        this.reactionColumnsDto = reactionColumnsDto;
        this.countOfComments = countOfComments;
        this.visible = visible;
        this.boardGroup = boardGroup;
        this.categoryDto = categoryDto;
    }

    public static CommonPostDto of(BoardGroup boardGroup, MemberDto memberDto, CommonPostRequest commonPostRequestDto) {

        return new CommonPostDto(
                commonPostRequestDto.getId(),
                memberDto,
                commonPostRequestDto.getTitle(),
                commonPostRequestDto.getContent(),
                commonPostRequestDto.getImageUrls(),
                null,
                null,
                null,
                commonPostRequestDto.isVisible(),
                boardGroup,
                CategoryDto.of(commonPostRequestDto.getCategoryId()),
                null,
                null,
                null,
                null
        );
    }

    public static CommonPostDto from(CommonPost entity) {

        CommonPostDto build = CommonPostDto.builder()
                .id(entity.getId())
                .memberDto(MemberDto.from(entity.getMember()))
                .title(entity.getTitle())
                .content(entity.getContent())
                .imageUrls(entity.getImageUrls())
                .reactionColumnsDto(ReactionColumnsDto.from(entity.getReactionColumns()))
                .countOfComments(entity.getCountOfComments())
                .visible(MemberUtil.getMemberId() != entity.getMember().getId())
                .boardGroup(BoardGroup.getByTable(entity.getDtype()))
                .categoryDto(CategoryDto.from(entity.getCategory()))
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .modifiedAt(entity.getModifiedAt())
                .modifiedBy(entity.getModifiedBy())
                .build();
        log.debug("commonPostDto.from : {}", build);
        return build;
    }

    public CommonPost toEntity(Member member, Category category) {
        CommonPost commonPost = CommonPost.of(
                member,
                category,
                title,
                content,
                boardGroup.getTable(),
                visible
        );
        log.debug("toEntity: {}", commonPost);
        return commonPost;
    }


    @Getter
    @Setter
    @ToString
    @SuperBuilder
    @NoArgsConstructor
    public static class CommonPostRequest {
        protected Long id;
        @NotBlank
        @Min(1L)
        protected Long categoryId;
        @NotBlank
        protected String title;
        @NotBlank
        protected String content;

        protected boolean visible;

        protected List<String> imageUrls;

        public CommonPostRequest(Long id, Long categoryId, String title, String content, boolean visible, List<String> imageUrls) {
            this.id = id;
            this.categoryId = categoryId;
            this.title = title;
            this.content = content;
            this.visible = visible;
            this.imageUrls = imageUrls;
        }

        public static CommonPostRequest of(Long id, Long categoryId) {
            return new CommonPostRequest(id, categoryId, null, null, true, null);
        }

        protected Class<? extends CommonPost> getEntityClassType() {
            return Arrays.stream(CategoryCode.values())
                    .filter(s -> s.getDtoForRequest().equals(this.getClass()))
                    .findFirst()
                    .orElseThrow()
                    .getDomain();
        }
    }

    @Getter
    @Setter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @SuperBuilder
    @JsonPropertyOrder({"id", "user_id", "userNickname", "userImage", "categoryName", "title", "content", "visible", "reactionColumns", "countOfComments", "hits", "comments"})
    public static class CommonPostResponse extends BaseTimeDto.BaseResponse {
        protected long id;
        protected long userId;
        protected String userNickname;
        protected String userImage;
        protected long postUserId;
        protected String categoryName;
        protected String title;
        protected String content;
        protected boolean visible;
        protected Long hits;
        protected ReactionColumnsDto reactionColumns;
        protected long countOfComments;
        protected BoardGroup boardGroup;
        protected List<CommentDto.Response> comments;

        public <E extends CommonPost> CommonPostResponse(E entity) {
            ModelMapper modelMapper = ModelMapperUtil.getModelMapper();

            modelMapper.map(entity, this);
        }

        public CommonPostResponse blindPostAlgorithm() {
            if (!visible && MemberUtil.getMemberId() != userId) {
                this.title = "비밀 게시글입니다.";
                this.content = "비밀 게시글입니다.";
//                this.reactionColumns.blindColumns();
                this.comments = new ArrayList<>();
            }
            blindCommentAlgorithm(comments);

            return this;
        }

        protected void blindCommentAlgorithm(List<CommentDto.Response> comments) {
            if (comments == null)
                return;

            for (int i = 0; i < comments.size(); i++) {
                CommentDto.Response comment = comments.get(i);
                if (comment.isAdmin()) {
                    comments.add(0, comments.remove(i));
                }

                if (!comment.isVisible() &&
                        MemberUtil.getMemberId() != userId &&
                        MemberUtil.getMemberId() != postUserId) {
                    comment.setContent("비밀 댓글입니다.");
//                    comment.getReactionColumns().blindColumns();
                }
                blindCommentAlgorithm(comment.getChildComments());
            }
        }

        public static CommonPostResponse from(CommonPostDto dto) {

            String blindTitle = "비밀 게시글입니다.";
            String blindContent = "비밀 게시글입니다.";
            boolean isSecret = hasSecret(dto);

            CommonPostResponse build = CommonPostResponse.builder()
                    .id(dto.getId())
                    .userId(dto.getMemberDto().getId())
                    .userNickname(dto.getMemberDto().getNickname())
                    .userImage(dto.getMemberDto().getImage())
                    .visible(dto.isVisible())
                    .title(isSecret ? blindTitle : dto.getTitle())
                    .content(isSecret ? blindContent : dto.getContent())
                    .hits(dto.getHits())
                    .reactionColumns(isSecret ? null : dto.getReactionColumnsDto())
                    .countOfComments(dto.getCountOfComments())
                    .boardGroup(dto.getBoardGroup())
                    .categoryName(dto.getCategoryDto().getName())
                    .createdAt(dto.getCreatedAt())
                    .createdBy(dto.getCreatedBy())
                    .modifiedAt(dto.getModifiedAt())
                    .modifiedBy(dto.getModifiedBy())
                    .build();

            log.debug("CommonPostResponse:from : {}", build);
            return build;

        }

        protected static boolean hasSecret(CommonPostDto dto) {
            return (!dto.isVisible()
                    && MemberUtil.getMemberId() != dto.getMemberDto().getId());
        }
    }
}
