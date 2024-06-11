package com.aptner.v3.board.complain.dto;

import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.complain.ComplainStatus;
import com.aptner.v3.member.dto.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
    @Slf4j
    @Getter
    @ToString(callSuper = true)
    @SuperBuilder
    @NoArgsConstructor
    public class ComplainStatusDto {

        Long postId;
        ComplainStatus status;
        MemberDto memberDto;

        public static ComplainStatusDto of(BoardGroup boardGroup, MemberDto memberDto, ComplainStatusDto.ComplainStatusRequest request) {
            return ComplainStatusDto.builder()
                    // post
                    .postId(request.getPostId())
                    // member
                    .memberDto(memberDto)
                    // complain
                    .status(request.getStatus())
                    .build();
        }
        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class ComplainStatusRequest {
            Long postId;
            ComplainStatus status;
        }

        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class ComplainStatusResponse {
            private String code;
            private String description;
            public static ComplainStatusResponse from(ComplainStatus status) {
                return new ComplainStatusResponse(status.name(), status.getDescription());
            }

            public static List<ComplainStatusResponse> toList() {
                return Arrays.stream(ComplainStatus.values())
                        .map(ComplainStatusResponse::from)
                        .collect(Collectors.toList());
            }

        }
    }
