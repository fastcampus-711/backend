package com.aptner.v3.board.free_post;

import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.CommonPostService;
import com.aptner.v3.board.free_post.domain.FreePost;
import com.aptner.v3.board.free_post.dto.FreePostDto;
import org.springframework.stereotype.Service;

@Service
public class FreePostService extends CommonPostService<FreePost, FreePostDto.Request, FreePostDto.Response> {
    public FreePostService(CommonPostRepository<FreePost> commonPostRepository) {
        super(commonPostRepository);
    }


}
