package com.aptner.v3.category;

//@DisplayName("비즈니스 로직 - 분류")
//@ExtendWith(MockitoExtension.class)
//class CategoryServiceTest {
//
//    @InjectMocks
//    private CategoryService sut;
//
//    @Mock
//    private CategoryRepository categoryRepository;
//
//    @Mock
//    private MenuService menuService;
//
//    @Test
//    @DisplayName("새로운 카테고리 생성 - 메뉴 존재")
//    void whenCreateCategory_success() {
//        // Given
//        CategoryDtoRequest request = CategoryDtoRequest.of("code", "value", BoardGroup.MARKETS);
//        Category expectedCategory = Category.of("code", "value", BoardGroup.MARKETS.getId());
//
//        given(menuService.isExistsMenu(1L)).willReturn(true); // Exists.
//        given(categoryRepository.save(any(Category.class))).willReturn(expectedCategory);
//
//        // When
//        Category result = sut.createCategory(request);
//
//        // Then
//        assertNotNull(result);
//        assertEquals(expectedCategory.getId(), result.getId());
//        verify(categoryRepository).save(any(Category.class));
//    }
//
//    @Test
//    @DisplayName("새로운 카테고리 생성 - 메뉴 존재 안함")
//    void whenCreateCategory_thenThrowException() {
//        // Given
//        CategoryDtoRequest request = CategoryDtoRequest.of("code", "value", BoardGroup.MARKETS);
//        // when
//        when(menuService.isExistsMenu(1L))
//                .thenThrow(new MenuException(ErrorCode._NOT_FOUND)); // Not Exists.
//        // Then
//        assertThrows(MenuException.class, () -> {
//            sut.createCategory(request);
//        });
//    }
//
//    @Test
//    @DisplayName("카테고리 생성 실패 - 중복 데이터")
//    void createCategory_throwsExceptionWhenDuplicateData() {
//        // 준비
//        CategoryDtoRequest request = CategoryDtoRequest.of("code", "value", BoardGroup.MARKETS);
//        given(menuService.isExistsMenu(1L)).willReturn(true); // Exists.
//        when(categoryRepository.save(any(Category.class)))
//                .thenThrow(new DataIntegrityViolationException("Already exists"));
//
//        // 실행 & 검증
//        assertThrows(CategoryException.class, () -> sut.createCategory(request));
//        verify(categoryRepository).save(any(Category.class));
//    }
//}