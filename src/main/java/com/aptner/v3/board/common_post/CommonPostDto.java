package com.aptner.v3.board.common_post;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.CategoryCode;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.comment.CommentDto;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.dto.ReactionColumnsDto;
import com.aptner.v3.global.util.MemberUtil;
import com.aptner.v3.global.util.ModelMapperUtil;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.dto.MemberDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class CommonPostDto {
    Long id;
    MemberDto memberDto;
    String title;
    String content;
    List<String> imageUrls;
    Long hits;
    ReactionColumnsDto reactionColumnsDto;
    Long countReactionTypeBad;
    Long countOfComments;
    Boolean visible;
    BoardGroup boardGroup;
    CategoryDto categoryDto;

    public CommonPostDto(Long id, MemberDto memberDto, String title, String content, List<String> imageUrls, Long hits, ReactionColumnsDto reactionColumnsDto, Long countOfComments, Boolean visible, BoardGroup boardGroup, CategoryDto categoryDto) {
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
                commonPostRequestDto.getVisible(),
                boardGroup,
                CategoryDto.of(commonPostRequestDto.getCategoryId())
        );
    }

    public static CommonPostDto from(CommonPost entity) {
        return CommonPostDto.builder()
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
                .build();
    }

    public CommonPost toEntity(Member member, Category category) {
        return CommonPost.of(
                member,
                category,
                title,
                content,
                boardGroup.getTable(),
                visible
        );
    }


    @Getter
    @Setter
    @ToString
    @SuperBuilder
    @NoArgsConstructor
    public static class CommonPostRequest {
        private Long id;
        @NotBlank
        @Min(1L)
        private Long categoryId;
        @NotBlank
        private String title;
        @NotBlank
        private String content;

        private Boolean visible;

        private List<String> imageUrls;

        public CommonPostRequest(Long id, Long categoryId, String title, String content, Boolean visible, List<String> imageUrls) {
            this.id = id;
            this.categoryId = categoryId;
            this.title = title;
            this.content = content;
            this.visible = visible;
            this.imageUrls = imageUrls;
        }

        public static CommonPostRequest of(Long id, Long categoryId) {
            return new CommonPostRequest(id, categoryId, null, null, null, null);
        }

        private Class<? extends CommonPost> getEntityClassType() {
            return Arrays.stream(CategoryCode.values())
                    .filter(s -> s.getDtoForRequest().equals(this.getClass()))
                    .findFirst()
                    .orElseThrow()
                    .getDomain();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class CommonPostResponse {
        private long id;
        private long userId;
        private String userNickname;
        private long postUserId;
        private String categoryName;
        private String title;
        private String content;
        private boolean visible;
        private Long hits;
        private ReactionColumnsDto reactionColumns;
        private long countOfComments;
        private BoardGroup boardGroup;
        private List<CommentDto.Response> comments;

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

        private void blindCommentAlgorithm(List<CommentDto.Response> comments) {
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

            return CommonPostResponse.builder()
                    .id(dto.getId())
                    .userId(dto.getMemberDto().getId())
                    .userNickname(dto.getMemberDto().getNickname())
                    .visible(dto.getVisible())
                    .title(isSecret ? blindTitle : dto.getTitle())
                    .content(isSecret ? blindContent : dto.getContent())
                    .hits(dto.getHits())
                    .reactionColumns(isSecret ? null : dto.getReactionColumnsDto())
                    .countOfComments(dto.getCountOfComments())
                    .boardGroup(dto.getBoardGroup())
                    .categoryName(dto.getCategoryDto().getName())
                    .build();
        }

        private static boolean hasSecret(CommonPostDto dto) {
            return !dto.getVisible();
        }
    }
}
