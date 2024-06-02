package com.aptner.v3.board.common_post.domain;

import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.board.comment.Comment;
import com.aptner.v3.board.common.reaction.domain.ReactionColumns;
import com.aptner.v3.board.common.reaction.service.ReactionAndCommentCalculator;
import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.board.common_post.dto.ReactionColumnsDto;
import com.aptner.v3.global.domain.BaseTimeEntity;
import com.aptner.v3.global.util.MemberUtil;
import com.aptner.v3.global.util.ModelMapperUtil;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.dto.MemberDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@ToString(callSuper = true)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@SQLDelete(sql = "UPDATE common_post SET deleted = true where id = ?")
@SQLRestriction("deleted = false")
public class CommonPost extends BaseTimeEntity
        implements ReactionAndCommentCalculator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(optional = false)
    private Member member;

    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(optional = false)
    private Category category;

    @Setter
    @Column(length = 200)
    private String title;

    @Setter
    @Column(length = 500)
    private String content;

    @Setter
    @ElementCollection
    @Column(name = "post_images")
    List<String> imageUrls;

    @ColumnDefault(value = "true")
    private boolean visible;

    @Setter
    private long hits;

    @Setter
    @Embedded
    private ReactionColumns reactionColumns = new ReactionColumns();

    @Setter
    private long countOfComments;

    @Column(insertable = false, updatable = false)
    private String dtype;

    @OneToMany(mappedBy = "commonPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments;

    private boolean deleted;

    public CommonPost() {
    }

    public CommonPost(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public CommonPost(Member member, Category category, String title, String content, List<String> imageUrls, boolean visible) {
        this.member = member;
        this.category = category;
        this.title = title;
        this.content = content;
        this.imageUrls = imageUrls;
        this.visible = visible;
    }
    public <Q extends CommonPostDto.CommonPostRequest> CommonPost updateByUpdateRequest(Q updateRequest) {
        ModelMapper modelMapper = ModelMapperUtil.getModelMapper();

        modelMapper.map(updateRequest, this);
        return this;
    }

    public static CommonPost of(Member member, Category category, String title, String content, List<String> imageUrls, boolean visible) {
        return new CommonPost(member, category, title, content, imageUrls, visible);
    }

//    public CommonPostDto.CommonPostResponse toResponseDtoWithoutComments() {
//        ModelMapper modelMapper = (ModelMapperUtil.getModelMapper());
//
//        Class<? extends CommonPostDto.CommonPostResponse> responseDtoClass = getResponseDtoClassType();
//
//        CommonPostDto.CommonPostResponse commonPostResponseDto =
//                modelMapper.map(this, responseDtoClass, "skipComments");
//
//        return commonPostResponseDto.blindPostAlgorithm();
//    }

//    public CommonPostDto.CommonPostResponse toResponseDtoWithComments() {
//        ModelMapper modelMapper = ModelMapperUtil.getModelMapper();
//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
//
//        Class<? extends CommonPostDto.CommonPostResponse> responseDtoClass = getResponseDtoClassType();
//
//        CommonPostDto.CommonPostResponse commonPostResponseDto = modelMapper.map(this, responseDtoClass, "memberToCommentResponse");
//
//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
//
//        return commonPostResponseDto.blindPostAlgorithm();
//    }

//    private Class<? extends CommonPostDto.CommonPostResponse> getResponseDtoClassType() {
//        return Arrays.stream(CategoryCode.values())
//                .filter(s -> s.getDomain().equals(this.getClass()))
//                .findFirst()
//                .orElseThrow()
//                .getDtoForResponse();
//    }

    public void plusHits() {
        this.hits++;
    }

    public boolean checkIsDtypeIsEquals(String dtype) {
        return this.getClass().getSimpleName().equals(dtype);
    }

    public boolean validUpdateOrDeleteAuthority() {
        return this.member.getId() == MemberUtil.getMember().getId();
    }

    public CommonPost(Member member, Category category, String title, String content, boolean visible) {
        this.member = member;
        this.category = category;
        this.title = title;
        this.content = content;
        this.visible = visible;
    }

    public static CommonPost of(Member member, Category category, String title, String content, boolean visible) {
        return new CommonPost(member, category, title, content, visible);
    }
    public CommonPostDto toDto() {
        CommonPost entity = this;
        CommonPostDto build = CommonPostDto.builder()
                .id(entity.getId())
                .memberDto(MemberDto.from(entity.getMember()))
                .title(entity.getTitle())
                .content(entity.getContent())
                .imageUrls(entity.getImageUrls())
                .hits(entity.getHits())
                .reactionColumnsDto(ReactionColumnsDto.from(entity.getReactionColumns()))
                .countOfComments(entity.getCountOfComments())
                .visible(entity.isVisible())
                .boardGroup(entity.getDtype())
                .categoryDto(CategoryDto.from(entity.getCategory()))
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .modifiedAt(entity.getModifiedAt())
                .modifiedBy(entity.getModifiedBy())
                .build();

        return build;
    }

    public CommonPostDto toDtoWithComment() {
        CommonPost entity = this;
        CommonPostDto build = CommonPostDto.builder()
                .id(entity.getId())
                .memberDto(MemberDto.from(entity.getMember()))
                .title(entity.getTitle())
                .content(entity.getContent())
                .imageUrls(entity.getImageUrls())
                .hits(entity.getHits())
                .reactionColumnsDto(ReactionColumnsDto.from(entity.getReactionColumns()))
                .countOfComments(entity.getCountOfComments())
                .commentDto(Optional.ofNullable(entity.getComments())
                        .orElse(Collections.emptySet())
                        .stream().map(Comment::toDto)
                        .collect(Collectors.toSet())
                )
                .visible(entity.isVisible())
                .boardGroup(entity.getDtype())
                .categoryDto(CategoryDto.from(entity.getCategory()))
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .modifiedAt(entity.getModifiedAt())
                .modifiedBy(entity.getModifiedBy())
                .build();

        return build;
    }
}
