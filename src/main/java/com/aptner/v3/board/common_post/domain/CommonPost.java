package com.aptner.v3.board.common_post.domain;

import com.aptner.v3.board.category.CategoryCode;
import com.aptner.v3.board.comment.Comment;
import com.aptner.v3.board.common.reaction.domain.ReactionColumns;
import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.global.util.ModelMapperUtil;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;

import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@SQLDelete(sql = "UPDATE common_post SET deleted = true where id = ?")
@Where(clause = "deleted is false")
public class CommonPost extends ReactionColumns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long categoryId;

    private String title;

    @Column(length = 500)
    private String content;

    @ColumnDefault(value = "0")
    private long hits;

    @ColumnDefault(value = "0")
    private long countOfComments;
    //상속 관계를 표현하기 위한 Column ex."NoticePost", "FreePost"
    @Column(insertable = false, updatable = false)
    private String dtype;

    @OneToMany(mappedBy = "commonPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
    @ColumnDefault(value = "true")
    private Boolean visible = true;
    @ColumnDefault(value = "false")
    private Boolean deleted = false;

    public CommonPost() {
    }

    public CommonPost(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public <Q extends CommonPostDto.Request> CommonPost updateByUpdateRequest(Q updateRequest) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true);

        modelMapper.map(updateRequest, this);
        return this;
    }

    public CommonPostDto.Response toResponseDtoWithoutComments() {
        ModelMapper modelMapper = ModelMapperUtil.getModelMapper();

        Class<? extends CommonPostDto.Response> responseDto = getResponseDtoClassType();

        try {
            modelMapper.createTypeMap(this, responseDto)
                    .addMappings(mapping -> mapping.skip(CommonPostDto.Response::setComments));
        } catch (IllegalStateException ignored) {
        }

        return modelMapper.map(this, responseDto);
    }

    public CommonPostDto.Response toResponseDtoWithComments() {
        ModelMapper modelMapper = ModelMapperUtil.getModelMapper();

        Class<? extends CommonPostDto.Response> responseDto = getResponseDtoClassType();

        return modelMapper.map(this, responseDto);
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
}
