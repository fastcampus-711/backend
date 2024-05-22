package com.aptner.v3.board.complain;

import com.aptner.v3.board.common_post.CommonPostRepository;
import com.aptner.v3.board.common_post.CommonPostService;
import com.aptner.v3.board.complain.dto.ComplainDto;
import org.springframework.stereotype.Service;

@Service
public class ComplainService extends CommonPostService<Complain, ComplainDto.Request, ComplainDto.Response> {
    public ComplainService(CommonPostRepository<Complain> commonPostRepository) {
        super(commonPostRepository);
    }
}
