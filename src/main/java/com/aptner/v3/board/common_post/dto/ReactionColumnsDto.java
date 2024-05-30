package com.aptner.v3.board.common_post.dto;

import com.aptner.v3.board.common.reaction.domain.ReactionColumns;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class ReactionColumnsDto {

    Long countReactionTypeGood;
    Long countReactionTypeBad;

    public ReactionColumnsDto(Long countReactionTypeGood, Long countReactionTypeBad) {
        this.countReactionTypeGood = countReactionTypeGood;
        this.countReactionTypeBad = countReactionTypeBad;
    }

    public static ReactionColumnsDto from(ReactionColumns entity) {
        return new ReactionColumnsDto(
                entity.getCountReactionTypeGood(),
                entity.getCountReactionTypeBad()
        );
    }

}
