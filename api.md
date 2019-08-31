# API

## USERS

| 메소드 | 경로                        | 설명            | 인증 |
| ------ | --------------------------- | --------------- | ---- |
| POST   | /login                      | 로그인          | X    |
| POST   | /users                      | 회원가입        | X    |
| GET    | /users/check?email={email}  | 이메일 체크     | X    |
| GET    | ~~/users/{userIdx}~~/mypage | 마이페이지 조회 | O    |
|        |                             |                 |      |
|        |                             |                 |      |
|        |                             |                 |      |
|        |                             |                 |      |
|        |                             |                 |      |

## ALARMS

| 메소드 | 경로                        | 설명         | 인증 |
| ------ | --------------------------- | ------------ | ---- |
| GET    | ~~/users/{userIdx~~}/alarms | 내 알람 조회 | O    |

## BOARDS

| 메소드 | 경로                        | 설명           | 인증 |
| ------ | --------------------------- | -------------- | ---- |
| GET    | /boards                     | 램덤 피드 조회 | X    |
| GET    | /boards/{boardIdx}          | 기록하기 조회  | X    |
| POST   | ~~/users/{userIdx}~~/boards | 기록하기 작성  | O    |
| PUT    | /boards/{boardIdx}/open     | 공개, 비공개   | O    |
|        |                             |                |      |

## LIKES

| 메소드 | 경로                      | 설명            | 인증 |
| ------ | ------------------------- | --------------- | ---- |
| POST   | boards/{boardIdx}/likes   | 기록하기 좋아요 | O    |
| POST   | courses/{courseIdx}/likes | 코스 좋아요     | O    |

## COMMENTS

| 메소드 | 경로                          | 설명                 | 인증 |                           |
| ------ | ----------------------------- | -------------------- | ---- | ------------------------- |
| GET    | /boards/{boardIdx}/comments   | 기록하기 댓글들 조회 | X    | 기록하기 조회시 같이 조회 |
| GET    | /courses/{courseIdx}/comments | 코스 댓글들 조회     | X    | 코스 조회시 같이 조회     |
| POST   | /boards/{boardIdx}/comments   | 기록하기 댓글 작성   | O    |                           |
| POST   | /courses/{courseIdx}/comments | 코스 댓글 작성       | O    |                           |

## ADDRESSES

| 메소드 | 경로                         | 설명           | 인증 |
| ------ | ---------------------------- | -------------- | ---- |
| GET    | /addresses?address={address} | 주소 검색      | X    |
| POST   | /addresses                   | 신규 주소 등록 | ?    |
|        |                              |                |      |

## SCRAPS

| 메소드 | 경로                                          | 설명                      | 인증 |
| ------ | --------------------------------------------- | ------------------------- | ---- |
| GET    | ~~/users/{userIdx}~~/scraps                   | 사용자 스크랩 게시글 조회 | O    |
| POST   | ~~/users/{userIdx}~~/scraps/boards/{boardIdx} | 게시글 스크랩             | O    |
| DELETE | ~~/users/{userIdx}~~/scraps/boards/{boardIdx} | 게시글 스크랩 삭제        | O    |
|        |                                               |                           |      |

## SHARES

| 메소드 | 경로 | 설명      | 인증 |
| ------ | ---- | --------- | ---- |
| POST   |      | 공유      | O    |
| DELETE |      | 공유 해제 | O    |
|        |      |           |      |

## SEARCHES

| 메소드 | 경로                        | 설명 | 인증 |
| :----- | --------------------------- | ---- | ---- |
| /GET   | /searches?keyword={keyword} | 검색 | X    |
|        |                             |      |      |
|        |                             |      |      |

## HASHTAGS

| 메소드 | 경로                | 설명          | 인증 |
| ------ | ------------------- | ------------- | ---- |
| GET    | /hashtags?tag={tag} | 해시태그 조회 | ?    |
| POST   | /hashtags           | 해시태그 등록 | ?    |
|        |                     |               |      |

