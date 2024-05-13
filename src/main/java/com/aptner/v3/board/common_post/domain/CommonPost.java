package com.aptner.v3.board.common_post.domain;

import com.aptner.v3.board.comment.domain.Comment;
import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.board.free_post.domain.FreePost;
import com.aptner.v3.board.free_post.dto.FreePostDto;
import com.aptner.v3.global.domain.CreatedInfo;
import jakarta.persistence.*;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.convention.NameTokenizers;
import org.modelmapper.spi.NameTokenizer;

import java.util.Arrays;
import java.util.List;

@Entity
@DiscriminatorColumn
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
public class CommonPost extends CreatedInfo {
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

    public <Q extends CommonPostDto.Request, E extends CommonPost> E update(Q updateRequest) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true);

        modelMapper.map(updateRequest, this);
        return (E) this;
    }
}
