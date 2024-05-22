package com.aptner.v3.board.complains;

import com.aptner.v3.board.commons.CommonPostRepository;
import com.aptner.v3.board.commons.CommonPostService;
import com.aptner.v3.board.complains.dto.ComplainDto;
import org.springframework.stereotype.Service;

@Service
public class ComplainService extends CommonPostService<Complain, ComplainDto.ComplainRequest, ComplainDto.ComplainResponse> {
    public ComplainService(CommonPostRepository<Complain> commonPostRepository) {
        super(commonPostRepository);
    }
}
