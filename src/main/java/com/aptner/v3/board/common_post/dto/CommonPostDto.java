package com.aptner.v3.board.common_post.dto;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.comment.CommentDto;
import com.aptner.v3.board.common.reaction.dto.ReactionType;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.global.domain.BaseTimeDto;
import com.aptner.v3.global.util.MemberUtil;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.dto.MemberDto;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.aptner.v3.board.common_post.dto.CommonPostDto.CommonPostResponse.toMiddleSizeImageUrl;
import static com.aptner.v3.board.common_post.dto.CommonPostDto.CommonPostResponse.toThumbSizeImageUrl;

@Slf4j
@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CommonPostDto extends BaseTimeDto {

    Long id;
    // user
    MemberDto memberDto;
    // post
    String title;
    String content;
    List<String> imageUrls;
    boolean visible;
    Long parentCommentId;
    // post info
    Long hits;
    ReactionColumnsDto reactionColumnsDto;
    Long countOfComments;
    Set<CommentDto> commentDto;
    // category
    String boardGroup;
    CategoryDto categoryDto;
    // icon
    @Setter
    ReactionType reactionType;
    @Setter
    private boolean isHot;

    public static CommonPostDto of(BoardGroup boardGroup, MemberDto memberDto, CommonPostRequest request) {

        return CommonPostDto.builder()
                .id(request.getId())
                .memberDto(memberDto)
                .title(request.getTitle())
                .content(request.getContent())
                .imageUrls(request.getImageUrls())
                .visible(request.isVisible())
                .boardGroup(boardGroup.getTable())
                .categoryDto(CategoryDto.of(request.getCategoryId()))
                .build();
    }

    public CommonPost toEntity(Member member, Category category) {
        return CommonPost.of(
                member,
                category,
                title,
                content,
                imageUrls,
                visible
        );
    }

    public CommonPostResponse toResponse() {
        CommonPostDto dto = this;

        String blindTitle = "비밀 게시글입니다.";
        String blindContent = "비밀 게시글입니다.";
        boolean isSecret = CommonPostResponse.hasSecret(dto);

        return CommonPostResponse.builder()
                .id(dto.getId())
                // user
                .userId(dto.getMemberDto().getId())
                .userNickname(dto.getMemberDto().getNickname())
                .userImage(dto.getMemberDto().getImage())
                // post
                .title(isSecret ? blindTitle : dto.getTitle())
                .content(isSecret ? blindContent : dto.getContent())
                .imageUrls(isSecret ? null : toThumbSizeImageUrl(dto))
                .visible(dto.isVisible())
                // post info
                .hits(dto.getHits())                                            // 조회수
                .reactionColumns(isSecret ? null : dto.getReactionColumnsDto()) // 공감
                .reactionType(isSecret || dto.getReactionType() == null ? ReactionType.DEFAULT : dto.getReactionType())
                .countOfComments(dto.getCountOfComments())                      // 댓글 수
                // category
                .boardGroup(dto.getBoardGroup())
                .categoryId(dto.getCategoryDto().getId())
                .categoryName(dto.getCategoryDto().getName())
                // base
                .createdAt(dto.getCreatedAt())
                .createdBy(dto.getCreatedBy())
                .modifiedAt(dto.getModifiedAt())
                .modifiedBy(dto.getModifiedBy())
                // icon
                .isOwner(CommonPostResponse.isOwner(dto))
                .isNew(CommonPostResponse.isNew(dto))
                .isHot(dto.isHot())
                .build();
    }

    public CommonPostResponse toResponseWithComment() {
        CommonPostDto dto = this;

        String blindTitle = "비밀 게시글입니다.";
        String blindContent = "비밀 게시글입니다.";
        boolean isSecret = CommonPostResponse.hasSecret(dto);

        return CommonPostResponse.builder()
                .id(dto.getId())
                // user
                .userId(dto.getMemberDto().getId())
                .userNickname(dto.getMemberDto().getNickname())
                .userImage(dto.getMemberDto().getImage())
                // post
                .title(dto.getTitle())
                .content(isSecret ? blindContent : dto.getContent())
                .imageUrls(isSecret ? null : toMiddleSizeImageUrl(dto))
                .visible(dto.isVisible())
//                .comments(organizeChildComments(dto.getCommentDto()))
                // post info
                .hits(dto.getHits())                                            // 조회수
                .reactionColumns(isSecret ? null : dto.getReactionColumnsDto()) // 공감
                .reactionType(isSecret || dto.getReactionType() == null ? ReactionType.DEFAULT : dto.getReactionType())
                .countOfComments(dto.getCountOfComments())                      // 댓글 수
                // category
                .boardGroup(dto.getBoardGroup())
                .categoryId(dto.getCategoryDto().getId())
                .categoryName(dto.getCategoryDto().getName())
                // base
                .createdAt(dto.getCreatedAt())
                .createdBy(dto.getCreatedBy())
                .modifiedAt(dto.getModifiedAt())
                .modifiedBy(dto.getModifiedBy())
                // icon
                .isOwner(CommonPostResponse.isOwner(dto))
                .isNew(CommonPostResponse.isNew(dto))
                .isHot(dto.isHot())
                .build();
    }

    @Getter
    @Setter
    @ToString
    @SuperBuilder
    @NoArgsConstructor
    public static class CommonPostRequest {
        protected Long id;
        @NotNull
        @Min(1L)
        protected Long categoryId;
        @NotBlank
        @Size(max = 200, message = "제목은 200자 이내로 작성해주세요.")
        protected String title;
        @NotBlank
        @Size(max = 500, message = "제목은 500자 이내로 작성해주세요.")
        protected String content;
        protected boolean visible;
        protected List<String> imageUrls;

        public static CommonPostDto.CommonPostRequest of(Long id, Long categoryId) {
            return CommonPostDto.CommonPostRequest.builder()
                    .id(id)
                    .categoryId(categoryId)
                    .build();
        }

        public CommonPostDto toDto(BoardGroup boardGroup, CustomUserDetails user, CommonPostRequest request) {
            return CommonPostDto.of(
                    boardGroup,
                    user.toDto(),
                    request
            );
        }

    }

    @Getter
    @Setter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @SuperBuilder
    @JsonPropertyOrder({"id", "user_id", "userNickname", "userImage", "categoryName", "title", "content", "imageUrls", "visible", "reactionColumns", "countOfComments", "hits", "comments"})
    public static class CommonPostResponse extends BaseTimeDto.BaseResponse {
        protected long id;
        // user
        protected long userId;
        protected String userNickname;
        protected String userImage;
        // post
        protected String title;
        protected String content;
        protected List<String> imageUrls;
        protected boolean visible;
        // post info
        protected Long hits;
        protected ReactionColumnsDto reactionColumns;
        private ReactionType reactionType;
        protected long countOfComments;
        Set<CommentDto.CommentResponse> comments;
        // category
        protected String boardGroup;
        protected long categoryId;
        protected String categoryName;
        // icon
        protected boolean isOwner;
        protected boolean isNew;
        protected boolean isHot;

        public static boolean hasSecret(CommonPostDto dto) {
            // isVisible: false && (user != writer)
            return (!dto.isVisible()
                    && !MemberUtil.getMember().getId().equals(dto.getMemberDto().getId()));
        }

        public static boolean isOwner(CommonPostDto dto) {
            return MemberUtil.getMember().getId().equals(dto.getMemberDto().getId());
        }

        public static boolean isNew(CommonPostDto dto) {
            LocalDateTime fortyEightHoursAgo = LocalDateTime.now().minusHours(48);
            return dto.getCreatedAt().isAfter(fortyEightHoursAgo);
        }

        public static List<String> toMiddleSizeImageUrl(CommonPostDto dto) {
            if (dto.getImageUrls() == null) return null;

            List<String> resizedUrls = new ArrayList<>();
            for (String url : dto.getImageUrls()) {
                if (containsValidImageExtension(url)) {
                    String baseUrl = url.substring(0, url.lastIndexOf("."));
                    String extension = url.substring(url.lastIndexOf("."));
                    resizedUrls.add(baseUrl + "/600" + extension);
                } else {
                    resizedUrls.add(url); // 원본 데이터 반환
                }
            }
            return resizedUrls;
        }

        public static List<String> toThumbSizeImageUrl(CommonPostDto dto) {
            if (dto.getImageUrls() == null) return null;

            List<String> resizedUrls = new ArrayList<>();
            for (String url : dto.getImageUrls()) {
                if (containsValidImageExtension(url)) {
                    String baseUrl = url.substring(0, url.lastIndexOf("."));
                    String extension = url.substring(url.lastIndexOf("."));
                    resizedUrls.add(baseUrl + "/300" + extension);
                } else {
                    resizedUrls.add(url); // 원본 데이터 반환
                }
            }
            return resizedUrls;
        }

        private static boolean containsValidImageExtension(String url) {
            String lowerCaseUrl = url.toLowerCase();
            return lowerCaseUrl.contains(".jpeg")
                    || lowerCaseUrl.contains(".jpg")
                    || lowerCaseUrl.contains(".png")
                    || lowerCaseUrl.contains(".webp")
                    ;
        }
    }
}
