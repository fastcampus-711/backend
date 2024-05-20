package com.aptner.v3.board.common.reaction;

import com.aptner.v3.board.common.reaction.dto.ReactionDto;
import com.aptner.v3.board.common.reaction.dto.ReactionTarget;
import com.aptner.v3.board.common.reaction.service.CommentReactionService;
import com.aptner.v3.board.common.reaction.service.PostReactionService;
import com.aptner.v3.global.error.SuccessCode;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reactions")
@RequiredArgsConstructor
public class ReactionController {
    private final PostReactionService postReactionService;
    private final CommentReactionService commentReactionService;

    @PostMapping
    public ApiResponse<?> saveLike(@RequestBody ReactionDto.Request reactionDto) {
        if (reactionDto.getReactionTarget() == ReactionTarget.POST)
            postReactionService.acceptReaction(reactionDto);
        else //(likeDto.getLikeTarget() == LikeTarget.COMMENT)
            commentReactionService.acceptReaction(reactionDto);

        return ResponseUtil.ok(SuccessCode.REACTION_APPLY_SUCCESS);
    }
}
