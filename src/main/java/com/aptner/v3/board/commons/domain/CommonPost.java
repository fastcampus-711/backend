package com.aptner.v3.board.commons.domain;

import com.aptner.v3.board.commons.CommonPostDto;
import com.aptner.v3.category.CategoryCode;
import com.aptner.v3.comment.Comment;
import com.aptner.v3.global.domain.BaseTimeEntity;
import com.aptner.v3.member.Member;
import com.aptner.v3.reaction.domain.Reactions;
import com.aptner.v3.reaction.service.ReactionAndCommentCalculator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Arrays;
import java.util.List;

import static com.aptner.v3.CommunityApplication.modelMapper;

@Entity
@Getter
@Setter
@DiscriminatorColumn
@Inheritance(strategy = InheritanceType.JOINED)
@SQLDelete(sql = "UPDATE common_post SET deleted = true where id = ?")
@Where(clause = "deleted is false")
public class CommonPost extends BaseTimeEntity
        implements ReactionAndCommentCalculator {

    /* 게시글 ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* 제목 */
    private String title;

    /* 내용 */
    @Column(length = 500)
    private String content;

    /* 첨부 파일 */
    @ElementCollection
    List<String> imageUrls;

    /* 작성자 */
    @ManyToOne(optional = false)
    private Member member;

    /*=====================*/

    /* 조회 수 */
    @ColumnDefault(value = "0")
    private long hits;

    /* 공감 수 */
    @Embedded
    private Reactions reactions = new Reactions();

    /* 댓글 수 */
    @ColumnDefault(value = "0")
    private long countOfComments;

    /* 노출 여부 */
    @Column(columnDefinition = "boolean default true")
    private Boolean visible;

    /* 삭제 여부 */
    @Column(columnDefinition = "boolean default false")
    private Boolean deleted = false;

    /* 카테고리 ID */
    @Column
    private Long categoryId;

    /* 게시판 구분 ID */
    @Column
    private Long BoardGroupId;

    /*========= 연관 관계 =========*/

    /* JOIN 항목 (COMPLAIN|MARKETS|QNAS) */
    @Column(insertable = false, updatable = false)
    private String dtype;

    /* 댓글 */
    @OneToMany(mappedBy = "commonPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public CommonPost() {
    }

    public CommonPost(String title, String content, List<String> imageUrls, Member member, Boolean visible, Long categoryId, Long boardGroupId) {
        this.title = title;
        this.content = content;
        this.imageUrls = imageUrls;
        this.member = member;
        this.visible = visible;
        this.categoryId = categoryId;
        this.BoardGroupId = boardGroupId;
    }

    public static CommonPost of(Long boardGroupId, Long CategoryId, Member member, String title, String content, List<String> imageUrls, boolean visible) {
        return new CommonPost(title, content, imageUrls, member, visible, CategoryId, boardGroupId);
    }


    public <Q extends CommonPostDto.CommonRequest> CommonPost updateByUpdateRequest(Q updateRequest) {
        modelMapper().map(updateRequest, this);
        return this;
    }

    public CommonPostDto.CommonResponse toResponseDtoWithoutComments() {

        Class<? extends CommonPostDto.CommonResponse> responseDtoClass = getResponseDtoClassType();

        CommonPostDto.CommonResponse commonResponseDto =
                modelMapper().map(this, responseDtoClass, "skipComments");

        return commonResponseDto.blindPostAlgorithm();
    }

    public CommonPostDto.CommonResponse toResponseDtoWithComments() {

        Class<? extends CommonPostDto.CommonResponse> responseDtoClass = getResponseDtoClassType();

        CommonPostDto.CommonResponse commonResponseDto = modelMapper().map(this, responseDtoClass);

        return commonResponseDto.blindPostAlgorithm();
    }

    private Class<? extends CommonPostDto.CommonResponse> getResponseDtoClassType() {
        return Arrays.stream(CategoryCode.values())
                .filter(s -> s.getDomain().equals(this.getClass()))
                .findFirst()
                .orElseThrow()
                .getDtoForResponse();
    }

    public CommonPost updateCountOfComments(long countOfComments) {
        System.out.println("countOfComments = " + countOfComments);
        this.countOfComments = countOfComments;
        return this;
    }

    public CommonPost plusHits() {
        this.hits++;
        return this;
    }
}
