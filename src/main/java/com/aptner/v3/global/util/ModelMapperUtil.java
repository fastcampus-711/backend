package com.aptner.v3.global.util;

import com.aptner.v3.board.commons.domain.CommonPost;
import com.aptner.v3.board.commons.CommonPostDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;

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

        modelMapper.createTypeMap(CommonPost.class, CommonPostDto.Response.class, "skipComments")
                .addMappings(mapping -> mapping.skip(CommonPostDto.Response::setComments));
    }
}