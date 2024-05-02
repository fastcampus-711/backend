[![Docker Build 🚀](https://github.com/fastcampus-711/backend/actions/workflows/build.yaml/badge.svg)](https://github.com/fastcampus-711/backend/actions/workflows/build.yaml)


- [팀 개발 규칙](https://github.com/fastcampus-711/backend/wiki/Spring-Convention)
    1. 변수명, 짧고 간결한 단어 선택이 어렵다면, 길게 변수 및 함수를 쓰고. 코드 리뷰에 언급 한다.
    2. TDD, @Display 한글로 테스트 주제를 쓴다.
    3. TDD, 비지니스 로직은 반드시 TDD를 남긴다.
    4. if(!status.isNormal()) 가능 하다.
    5. Wrapping형 보다, 기본 자료형을 고려하자.
  
- Git Flow
    1. 배포 브랜치  master
    2. 개발 브랜치  feature/#{issue-no}