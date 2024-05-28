package com.aptner.v3.board.common_post;

//@Primary
//@Service
//@Transactional
//public class SearchPostService <E extends CommonPost,
//        Q extends CommonPostDto.Request,
//        S extends CommonPostDto.Response> {
//
//    public List<S> getPostList(HttpServletRequest request, Pageable pageable) {
//
//        String dtype = getDtype(request);
//
//        List<E> list;
//        List<E> topPostsList;
//        if (dtype.equals("CommonPost")) {
//            list = commonPostRepository.findAll(pageable).getContent();
//        }else if (dtype.equals("FreePost")) {
//            //자유게시판의 1페이지일 경우 7일 이내의 조회수+공감수가 가장 높은 3개의 글을 조회
//            if (page == 1) {
//                topPostsList = updateTopPosts();
//                pageable = PageRequest.of(page - 1, limit, Sort.by(sort.getColumnName()).descending());
//                //인기글3개와 나머지 글 7개를 합쳐서 반환해야함
//                /*topPostsList.addAll(commonPostRepository.findByDtype(dtype));
//                return topPostsList.stream()
//                        .map(e -> (S) e.toResponseDtoWithoutComments())
//                        .toList();*/
//                List<E> mergedList = Stream.concat(topPostsList.stream(), commonPostRepository.findByDtype(dtype, pageable).stream()).toList();
//                return mergedList.stream()
//                        .map(e -> (S) e.toResponseDtoWithoutComments())
//                        .toList();
//            } else {
//                list = commonPostRepository.findByDtype(dtype, pageable).getContent();;
//            }
//        } else {
//            list = commonPostRepository.findByDtype(dtype, pageable).getContent();;
//        }
//
//        return list.stream()
//                .map(e -> (S) e.toResponseDtoWithoutComments())
//                .toList();
//    }
//
//    public List<S> getPostListByCategoryId(Long categoryId, HttpServletRequest request, Pageable pageable) {
//
//        String dtype = getDtype(request);
//        List<E> list;
//        if (categoryId == null) {
//            list = commonPostRepository.findByDtype(dtype, pageable).getContent();
//
//            return list.stream()
//                    .map(e -> (S) e.toResponseDtoWithoutComments())
//                    .toList();
//        } else {
//            list = commonPostRepository.findByCategoryId(categoryId);
//
//            return list.stream()
//                    .map(e -> (S) e.toResponseDtoWithoutComments())
//                    .toList();
//        }
//    }
//
//    public List<S> searchPost(HttpServletRequest request, String keyword, Pageable pageable) {
//        String dtype = getDtype(request);
//
//        List<E> list;
//        if (dtype.equals("CommonPost"))
//            list = commonPostRepository.findByTitleContainingIgnoreCaseAndVisible(keyword, pageable, true).getContent();
//        else
//            list = commonPostRepository.findByTitleContainingIgnoreCaseAndDtypeAndVisible(keyword, dtype, pageable, true).getContent();
//
//        return list.stream()
//                .map(e -> (S) e.toResponseDtoWithoutComments())
//                .toList();
//    }
//}
