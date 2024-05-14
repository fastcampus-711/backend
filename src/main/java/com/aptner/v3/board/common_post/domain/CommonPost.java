package com.aptner.v3.board.common_post.domain;

import com.aptner.v3.board.category.CategoryName;
import com.aptner.v3.board.comment.domain.Comment;
import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.global.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;

import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
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

    @OneToMany(mappedBy = "commonPost")
    private List<Comment> comments;
    private Boolean visible = true;


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

    public CommonPostDto.Response toResponseDto() {
        ModelMapper modelMapper = new ModelMapper();
        Class<?> responseDto = Arrays.stream(CategoryName.values())
                .filter(s -> s.getDomain().equals(this.getClass()))
                .findFirst()
                .orElseThrow()
                .getDtoForResponse();

        modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true);

        return (CommonPostDto.Response) modelMapper.map(this, responseDto);
    }
}
