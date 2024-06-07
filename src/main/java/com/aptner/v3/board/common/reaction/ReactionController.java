package com.aptner.v3.board.common.reaction;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.board.common.reaction.dto.ReactionDto;
import com.aptner.v3.board.common.reaction.service.ReactionService;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="공감")
@RequestMapping("/reactions")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;

    @PostMapping("/post")
    @Operation(summary = "게시글 공감")
    public ApiResponse<?> savePostReaction(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody ReactionDto.ReactionRequest reactionDto) {

        reactionDto.setUserId(user.getId());
        return ResponseUtil.ok(reactionService.savePostReaction(reactionDto));
    }

    @PostMapping("/comment")
    @Operation(summary = "댓글 공감")
    public ApiResponse<?> saveCommentReaction(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody ReactionDto.ReactionRequest reactionDto) {

        reactionDto.setUserId(user.getId());
        return ResponseUtil.ok(reactionService.saveCommentReaction(reactionDto));
    }
}
