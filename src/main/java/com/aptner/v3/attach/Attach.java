package com.aptner.v3.attach;

import com.aptner.v3.board.commons.domain.CommonPost;
import com.aptner.v3.global.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Entity
@Builder
@Table(name = "attach", indexes = {
    @Index(columnList = "url"),
})
@AllArgsConstructor
public class Attach extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @Column(nullable = false)
    private String url;
    @Setter
    @Column(nullable = false)
    private String name;
    @Setter
    @Column(nullable = false)
    private String uuid;
    @Setter
    private AttachType type;
    @Setter
    @Column(nullable = false)
    private String contentType;
    @Setter
    @Column(nullable = false)
    private Long size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private CommonPost post;

    //@todo
    // uploadUser;
    protected Attach() {
    }

    public Attach(String url, String name, String uuid, String contentType, Long size, CommonPost post) {
        this.url = url;
        this.name = name;
        this.uuid = uuid;
        this.contentType = contentType;
        this.size = size;
        this.post = post;
    }

    public static Attach of(String url, String name, String uuid, String contentType, Long size, CommonPost post) {
        return new Attach(url, name, uuid, contentType, size, post);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attach attach = (Attach) o;
        return Objects.equals(id, attach.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
