package com.aptner.v3.board.frees;

import com.aptner.v3.board.commons.CommonPostRepository;
import com.aptner.v3.board.commons.CommonPostService;
import com.aptner.v3.board.frees.domain.FreePost;
import com.aptner.v3.board.frees.dto.FreePostDto;
import org.springframework.stereotype.Service;

@Service
public class FreePostService extends CommonPostService<FreePost, FreePostDto.Request, FreePostDto.Response> {
    public FreePostService(CommonPostRepository<FreePost> commonPostRepository) {
        super(commonPostRepository);
    }


}
