package com.aptner.v3.board.common_post.domain;

import com.aptner.v3.board.category.Category;
import com.aptner.v3.board.category.CategoryCode;
import com.aptner.v3.board.comment.Comment;
import com.aptner.v3.board.common.reaction.domain.ReactionColumns;
import com.aptner.v3.board.common.reaction.service.ReactionAndCommentCalculator;
import com.aptner.v3.board.common_post.CommonPostDto;
import com.aptner.v3.global.domain.BaseTimeEntity;
import com.aptner.v3.global.util.MemberUtil;
import com.aptner.v3.global.util.ModelMapperUtil;
import com.aptner.v3.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@ToString(callSuper = true)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@SQLDelete(sql = "UPDATE common_post SET deleted = true where id = ?")
@SQLRestriction("deleted is false")
public class CommonPost extends BaseTimeEntity
        implements ReactionAndCommentCalculator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(optional = false)
    private Member member;

    @Column(nullable = true)
    private Long memberId;

    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(optional = false)
    private Category category;

    @Setter
    private String title;

    @Setter
    @Column(length = 500)
    private String content;

    @Setter
    @ElementCollection
    @Column(name = "post_images")
    List<String> imageUrls;

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
    private List<Comment> comments;

    private boolean visible = true;

    private boolean deleted;

    public CommonPost() {
    }

    public CommonPost(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public <Q extends CommonPostDto.CommonPostRequest> CommonPost updateByUpdateRequest(Q updateRequest) {
        ModelMapper modelMapper = ModelMapperUtil.getModelMapper();

        modelMapper.map(updateRequest, this);
        return this;
    }

    public CommonPostDto.CommonPostResponse toResponseDtoWithoutComments() {
        ModelMapper modelMapper = (ModelMapperUtil.getModelMapper());

        Class<? extends CommonPostDto.CommonPostResponse> responseDtoClass = getResponseDtoClassType();

        CommonPostDto.CommonPostResponse commonPostResponseDto =
                modelMapper.map(this, responseDtoClass, "skipComments");

        return commonPostResponseDto.blindPostAlgorithm();
    }

    public CommonPostDto.CommonPostResponse toResponseDtoWithComments() {
        ModelMapper modelMapper = ModelMapperUtil.getModelMapper();

        Class<? extends CommonPostDto.CommonPostResponse> responseDtoClass = getResponseDtoClassType();

        CommonPostDto.CommonPostResponse commonPostResponseDto = modelMapper.map(this, responseDtoClass);

        return commonPostResponseDto.blindPostAlgorithm();
    }

    private Class<? extends CommonPostDto.CommonPostResponse> getResponseDtoClassType() {
        return Arrays.stream(CategoryCode.values())
                .filter(s -> s.getDomain().equals(this.getClass()))
                .findFirst()
                .orElseThrow()
                .getDtoForResponse();
    }

    public CommonPost updateCountOfComments(long countOfComments) {
        this.countOfComments = countOfComments;
        return this;
    }

    public CommonPost plusHits() {
        this.hits++;

        return this;
    }

    public boolean checkIsDtypeIsEquals(String dtype) {
        return this.getClass().getSimpleName().equals(dtype);
    }

    public boolean validUpdateOrDeleteAuthority() {
        return this.memberId == MemberUtil.getMemberId();
    }

    public CommonPost setMemberId() {
        this.memberId = MemberUtil.getMemberId();
        return this;
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
}
