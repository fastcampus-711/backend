package com.aptner.v3.board.frees;

import com.aptner.v3.board.commons.CommonPostRepository;
import com.aptner.v3.board.commons.CommonPostService;
import com.aptner.v3.board.frees.domain.FreePost;
import com.aptner.v3.board.frees.dto.FreePostDto;
import com.aptner.v3.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class FreePostService extends CommonPostService<
        FreePost,
        FreePostDto.FreeCommonRequest,
        FreePostDto.FreeCommonResponse,
        FreePostDto> {

    private final CommonPostRepository<FreePost> commonPostRepository;

    public FreePostService(CommonPostRepository<FreePost> commonPostRepository, MemberRepository memberRepository, CommonPostRepository<FreePost> commonPostRepository1) {
        super(commonPostRepository, memberRepository);
        this.commonPostRepository = commonPostRepository1;
    }


    public FreePostDto.FreeCommonResponse createPost(FreePostDto.FreeCommonRequest requestDto) {
//        if (!requestDto.getImageUrls().isEmpty()) {
//            FreePost freePost = requestDto.toEntity(requestDto.getImageUrls());
//            return new FreePostDto.FreeCommonResponse(commonPostRepository.save(freePost));
//        }

        FreePost request = requestDto.toEntity();
        FreePost saved = (FreePost) commonPostRepository.save(request);
        return new FreePostDto.FreeCommonResponse(saved);
    }
}
