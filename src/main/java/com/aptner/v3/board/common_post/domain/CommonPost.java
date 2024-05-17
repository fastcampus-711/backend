package com.aptner.v3.board.common_post.domain;

import com.aptner.v3.board.category.CategoryCode;
import com.aptner.v3.board.comment.domain.Comment;
import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.global.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;

import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@SQLDelete(sql = "UPDATE commonpost SET deleted = true where id = ?")
@Where(clause = "deleted is false")
public class CommonPost extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Column(length = 500)
    private String content;
    private int hits = 0;
    //상속 관계를 표현하기 위한 Column ex."NoticePost", "FreePost"
    @Column(insertable = false, updatable = false)
    private String dtype;

    @OneToMany(mappedBy = "commonPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
    private Boolean visible = true;
    private Boolean deleted = false;

    public CommonPost() {
    }

    public CommonPost(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public <Q extends CommonPostDto.Request> CommonPost update(Q updateRequest) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true);

        modelMapper.map(updateRequest, this);
        return this;
    }

    public CommonPostDto.Response toResponseDtoWithoutComments() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true);

        Class<? extends CommonPostDto.Response> responseDto = Arrays.stream(CategoryCode.values())
                .filter(s -> s.getDomain().equals(this.getClass()))
                .findFirst()
                .orElseThrow()
                .getDtoForResponse();

        modelMapper.createTypeMap(this, responseDto)
                .addMappings(mapping -> mapping.skip(CommonPostDto.Response::setComments));

        return modelMapper.map(this, responseDto);
    }

    public CommonPostDto.Response toResponseDtoWithComments() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true);

        Class<? extends CommonPostDto.Response> responseDto = Arrays.stream(CategoryCode.values())
                .filter(s -> s.getDomain().equals(this.getClass()))
                .findFirst()
                .orElseThrow()
                .getDtoForResponse();

        return modelMapper.map(this, responseDto);
    }
}
