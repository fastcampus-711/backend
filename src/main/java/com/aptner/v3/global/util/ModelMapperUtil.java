package com.aptner.v3.global.util;

import com.aptner.v3.board.category.CategoryCode;
import com.aptner.v3.board.comment.CommentDto;
import com.aptner.v3.board.common_post.domain.CommonPost;
import com.aptner.v3.board.common_post.CommonPostDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;

import java.util.List;

public class ModelMapperUtil {
    private static ModelMapper modelMapper;

    private ModelMapperUtil() {}

    public static ModelMapper getModelMapper() {
        if (modelMapper == null)
            initModelMapper();
        return modelMapper;
    }

    private static void initModelMapper() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setSkipNullEnabled(true)
                .setFieldMatchingEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        for (CategoryCode categoryCode: CategoryCode.values()) {
            Class<? extends CommonPost> commonPostDomain = categoryCode.getDomain();
            Class<? extends CommonPostDto.CommonPostResponse> commonPostDtoResponse = categoryCode.getDtoForResponse();

            modelMapper.createTypeMap(commonPostDomain, commonPostDtoResponse, "skipComments")
                    .addMappings(mapping -> mapping.skip((a, b) -> a.setComments((List<CommentDto.Response>) b)));
        }
    }
}