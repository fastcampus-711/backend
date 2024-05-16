package com.aptner.v3.board.free_post;

import com.aptner.v3.board.common_post.repository.CommonPostRepository;
import com.aptner.v3.board.common_post.service.CommonPostService;
import com.aptner.v3.board.free_post.domain.FreePost;
import com.aptner.v3.board.free_post.dto.FreePostDto;
import org.springframework.stereotype.Service;

@Service
public class FreePostService extends CommonPostService<FreePost, FreePostDto.Request, FreePostDto.Response> {
    public FreePostService(CommonPostRepository<FreePost> commonPostRepository) {
        super(commonPostRepository);
    }

    public FreePostDto.Response createPost(FreePostDto.Request requestDto) {
        if (!requestDto.getImageUrls().isEmpty()) {
            FreePost freePost = requestDto.toEntity(requestDto.getImageUrls());
            return new FreePostDto.Response(commonPostRepository.save(freePost));
        }
        return new FreePostDto.Response(commonPostRepository.save(requestDto.toEntity()));
    }
}
