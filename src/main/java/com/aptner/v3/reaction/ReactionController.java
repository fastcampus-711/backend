package com.aptner.v3.reaction;

import com.aptner.v3.reaction.dto.ReactionDto;
import com.aptner.v3.reaction.dto.ReactionTarget;
import com.aptner.v3.reaction.service.CommentReactionService;
import com.aptner.v3.reaction.service.PostReactionService;
import com.aptner.v3.global.error.SuccessCode;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="공감")
@RequestMapping("/reactions")
@RequiredArgsConstructor
public class ReactionController {
    private final PostReactionService postReactionService;
    private final CommentReactionService commentReactionService;

    @PostMapping
    @Operation(summary = "공감 등록")
    public ApiResponse<?> saveReaction(@RequestBody ReactionDto.Request reactionDto) {
        if (reactionDto.getReactionTarget() == ReactionTarget.POST)
            postReactionService.acceptReaction(reactionDto);
        else //(likeDto.getLikeTarget() == LikeTarget.COMMENT)
            commentReactionService.acceptReaction(reactionDto);

        return ResponseUtil.ok(SuccessCode.REACTION_APPLY_SUCCESS);
    }
}
