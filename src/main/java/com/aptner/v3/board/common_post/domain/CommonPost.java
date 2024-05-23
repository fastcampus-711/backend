package com.aptner.v3.board.common_post.domain;

import com.aptner.v3.board.category.CategoryCode;
import com.aptner.v3.board.comment.Comment;
import com.aptner.v3.board.common.reaction.domain.ReactionColumns;
import com.aptner.v3.board.common.reaction.service.ReactionAndCommentCalculator;
import com.aptner.v3.board.common_post.CommonPostDto;
import com.aptner.v3.global.domain.BaseTimeEntity;
import com.aptner.v3.global.util.MemberUtil;
import com.aptner.v3.global.util.ModelMapperUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
@SQLDelete(sql = "UPDATE common_post SET deleted = true where id = ?")
@Where(clause = "deleted is false")
public class CommonPost extends BaseTimeEntity
implements ReactionAndCommentCalculator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long userId;

    private long categoryId;

    private String title;

    @Column(length = 500)
    private String content;

    private long hits;

    @Embedded
    private ReactionColumns reactionColumns = new ReactionColumns();

    private long countOfComments;
    //상속 관계를 표현하기 위한 Column ex."NoticePost", "FreePost"
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

    public <Q extends CommonPostDto.Request> CommonPost updateByUpdateRequest(Q updateRequest) {
        ModelMapper modelMapper = ModelMapperUtil.getModelMapper();

        modelMapper.map(updateRequest, this);
        return this;
    }

    public CommonPostDto.Response toResponseDtoWithoutComments() {
        ModelMapper modelMapper = (ModelMapperUtil.getModelMapper());

        Class<? extends CommonPostDto.Response> responseDtoClass = getResponseDtoClassType();

        CommonPostDto.Response responseDto =
                modelMapper.map(this, responseDtoClass, "skipComments");

        return responseDto.blindPostAlgorithm();
    }

    public CommonPostDto.Response toResponseDtoWithComments() {
        ModelMapper modelMapper = ModelMapperUtil.getModelMapper();

        Class<? extends CommonPostDto.Response> responseDtoClass = getResponseDtoClassType();

        CommonPostDto.Response responseDto =  modelMapper.map(this, responseDtoClass);

        return responseDto.blindPostAlgorithm();
    }

    private Class<? extends CommonPostDto.Response> getResponseDtoClassType() {
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
        return this.userId == MemberUtil.getMemberId();
    }

    public CommonPost setUserId() {
        this.userId = MemberUtil.getMemberId();
        return this;
    }
}
