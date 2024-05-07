package com.aptner.v3.board.common_post.domain;

import com.aptner.v3.board.comment.domain.Comment;
import com.aptner.v3.board.common_post.dto.CommonPostDto;
import com.aptner.v3.global.domain.CreatedInfo;
import jakarta.persistence.*;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@DiscriminatorColumn
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
public class CommonPost extends CreatedInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private int hits = 0;
    //상속 관계를 표현하기 위한 Column ex."NoticePost", "FreeBoardPost"
    @Column(insertable = false, updatable = false)
    private String dtype;

    @OneToMany
    @JoinColumn(name = "comment_id")
    private List<Comment> comments;
    private Boolean visible = true;

    public CommonPost() {}

    public CommonPost(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public <T extends CommonPostDto.UpdateRequest, U extends CommonPost> U update(T updateRequest) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true);
        modelMapper.map(updateRequest, this);
        return (U)this;
    }
}
