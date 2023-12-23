## 회원가입, 로그인 기능이 있는 투두앱 백엔드 서버 만들기

#### AWS S3 이용한 이미지 저장 기
![img_1.png](img_1.png)
#### AWS 내의 mysql 설정
![img_2.png](img_2.png)
#### AWS EC2를 이용한 동작 성공
![img_3.png](img_3.png)

### 📌기능 요구사항
- [X]  **🆕 회원 가입 API**
    - username, password를 Client에서 전달받기
    - username은  `최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)`로 구성되어야 한다.
    - password는  `최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9)`로 구성되어야 한다.
    - DB에 중복된 username이 없다면 회원을 저장하고 Client 로 성공했다는 메시지, 상태코드 반환하기

- [X]  **🆕 로그인 API**
    - username, password를 Client에서 전달받기
    - DB에서 username을 사용하여 저장된 회원의 유무를 확인하고 있다면 password 비교하기
    - 로그인 성공 시, 로그인에 성공한 유저의 정보와 JWT를 활용하여 토큰을 발급하고,
      발급한 토큰을 Header에 추가하고 성공했다는 메시지, 상태코드 와 함께 Client에 반환하기
- [X]  **할일 카드 작성 기능 API**
    - 토큰을 검사하여, 유효한 토큰일 경우에만 할일 작성 가능
    - `할일 제목`,`할일 내용`, `작성일`을 저장할 수 있습니다
    - 할일 제목, 할일 내용을 저장하고
    - 저장된 할일을 Client 로 반환하기(username은 로그인 된 사용자)
- [X]  **선택한 할일카드  조회 기능 API**
    - 선택한 할일 의 정보를 조회할 수 있습니다.
        - 반환 받은 할일 정보에는 `할일 제목`,`할일 내용`, `작성자` , `작성일`정보가 들어있습니다.
        - ~~반환 받은 할일 정보에 비밀번호는 제외 되어있습니다.~~
- [X]  **할일카드 목록 조회 기능 API**
    - 등록된 할일 전체를 조회할 수 있습니다.
        - 회원별로 각각 나누어서 할일 목록이 조회됩니다.
        - 반환 받은 할일 정보에는 `할일 제목`, `작성자` , `작성일`, `완료 여부`정보가 들어있습니다.
        - ~~반환 받은 할일 정보에 비밀번호는 제외 되어있습니다.~~
    - 조회된 할일 목록은 `작성일` 기준 내림차순으로 정렬 되어있습니다.
- [X]  **선택한 할일카드 수정 기능 API**
    - 선택한 할일카드의 `제목`, `작성 내용`을 수정할 수 있습니다. (~~작성자명~~)
        - 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 게시글만 수정 가능
        - 할일 제목, 할일 내용을 수정하고 수정된 할일 정보는 Client 로 반환됩니다.
        - ~~서버에 게시글 수정을 요청할 때 비밀번호를 함께 전달합니다.~~
        - ~~선택한 게시글의 비밀번호와 요청할 때 함께 보낸 비밀번호가 일치할 경우에만 수정이 가능합니다.~~
    - 수정된  할일의 정보를 반환 받아 확인할 수 있습니다.
        - 반환 받은 할일 정보에는 `할일 제목`,`할일 내용`, `작성자` , `작성일`정보가 들어있습니다.
        - ~~반환 받은 게시글의 정보에 비밀번호는 제외 되어있습니다.~~
- [X]  **🆕 할일카드 완료 기능 API**
    - 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 할일카드 만 완료 가능
    - 완료처리 한 할일카드는 목록조회시 `완료 여부`필드가 TRUE 로 내려갑니다.
    - `완료 여부` 기본값은 FALSE
- [X]  **🆕 댓글 작성 API**
    - 토큰을 검사하여, 유효한 토큰일 경우에만 댓글 작성 가능
    - 선택한 할일의 DB 저장 유무를 확인하기
    - 선택한 할일이 있다면 댓글을 등록하고 등록된 댓글 반환하기
- [X]  **🆕 댓글 수정 API**
    - 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 댓글만 수정 가능
    - 선택한 댓글의 DB 저장 유무를 확인하기
    - 선택한 댓글이 있다면 댓글 수정하고 수정된 댓글 반환하기
- [X]  **🆕 댓글 삭제 API**
    - 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 댓글만 삭제 가능
    - 선택한 댓글의 DB 저장 유무를 확인하기
    - 선택한 댓글이 있다면 댓글 삭제하고 Client 로 성공했다는 메시지, 상태코드 반환하기
- [X]  **🆕 예외 처리 (ResponseEntity 사용)**
    - 토큰이 필요한 API 요청에서 토큰을 전달하지 않았거나 정상 토큰이 아닐 때는 "토큰이 유효하지 않습니다." 라는 에러메시지와 statusCode: 400을 Client에 반환하기
    - 토큰이 있고, 유효한 토큰이지만 해당 사용자가 작성한 게시글/댓글이 아닌 경우에는 “작성자만 삭제/수정할 수 있습니다.”라는 에러메시지와 statusCode: 400을 Client에 반환하기
    - DB에 이미 존재하는 username으로 회원가입을 요청한 경우 "중복된 username 입니다." 라는 에러메시지와 statusCode: 400을 Client에 반환하기
    - 로그인 시, 전달된 username과 password 중 맞지 않는 정보가 있다면 "회원을 찾을 수 없습니다."라는 에러메시지와 statusCode: 400을 Client에 반환하기

### 💎 ERD
![img.png](img.png)

### 🔥API 명세서
|    기능     | Method |        URL        |                    Request                    |                                                                                          Response                                                                                           |
|:---------:|:------:|:-----------------:|:---------------------------------------------:|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
|   회원가입    |  POST  | /api/user/signup  | "username":"String", <br> "password":"String" |                                                                           {"msg":"회원가입 성공", <br>"statusCode":200}                                                                           |
|    로그인    |  POST  |  /api/user/login  |  "username":"String",<br>"password":"String"  |                                                                           {"msg":"로그인 성공", <br> "statusCode":200}                                                                           |
|  할 일 작성   |  POST  |     /api/todo     |    "title":"String",<br>"content":"String"    |                      "id":"Long",<br>"title":"String",<br>"content":"String",<br>"username":"String",<br>"createdAt":"LocalDateTime",<br>"modifiedAt":"LocalDateTime"                       |
|  할 일 수정   |  PUT   |  /api/todo/{id}   |    "title":"String",<br>"content":"String"    |                      "id":"Long",<br>"title":"String",<br>"content":"String",<br>"username":"String",<br>"createdAt":"LocalDateTime",<br>"modifiedAt":"LocalDateTime"                       |
|  할 일 삭제   | DELETE |  /api/todo/{id}   |     {"msg":"삭제 성공",<br>"statusCode":200}      |
| 할 일 목록 조회 |  GET   |    /api/todos     |                                               | List: [<br>"id":"Long",<br>"title":"String",<br>"content":"String",<br>"username":"String",<br>"createdAt":"LocalDateTime",<br>"modifiedAt":"LocalDateTime",<br>"isFinished":"Boolean"<br>] |
| 특정 할 일 조회 |  GET   |  /api/todo/{id}   ||                      "id":"Long",<br>"title":"String",<br>"content":"String",<br>"username":"String",<br>"createdAt":"LocalDateTime",<br>"modifiedAt":"LocalDateTime"                       |
|완료 처리|PUT|/api/todo/finish/{id}|| "id":"Long",<br>"title":"String",<br>"content":"String",<br>"username":"String",<br>"createdAt":"LocalDateTime",<br>"modifiedAt":"LocalDateTime"                       |
|   댓글 작성   |  POST  |   /api/comment    |"content":"String"|                                                                                  "id":"Long",<br>"username":"String",<br>"contnet":"String",<br>"createdAt": LocalDateTime",<br>"modifiedAt":"LocalDateTime"|
|댓글 수정|  PUT   | /api/comment/{id} |"content":"String"|                                                                                  "id":"Long",<br>"username":"String",<br>"contnet":"String",<br>"createdAt": LocalDateTime",<br>"modifiedAt":"LocalDateTime"|
|댓글 삭제| DELETE | /api/comment/{id} |     {"msg":"삭제 성공",<br>"statusCode":200}      |


## 📌 심화과제 📌
### 🙏 요구사항
- [ ]  **🆕 DTO, Entity Test 추가하기**
   - `@Test` 를 사용해서 DTO 와 Entity Test 를 추가합니다.
   - User, Todo, Comment, DTO 에 존재하는 메서드들에 대해서 테스트를 추가합니다.
- [ ]  **🆕 Controller Test 추가하기**
   - `@WebMvcTest` 를 사용하여 Controller Test 를 추가합니다.
   - Todo, Comment Controller 에 대해서 테스트를 추가합니다.
- [ ]  **🆕 Service Test 추가하기**
   - `@RunWith(MockitoJUnitRunner.class)` 를 사용하여 Service Test 를 추가합니다.
   - User, UserDetails, Todo, Comment Service 에 대해서 테스트를 추가합니다.
- [ ]  **🆕 Repository Test 추가하기**
   - `@DataJpaTest` 를 사용하여 Repository Test 를 추가합니다.
   - User, Todo, Comment Repository 에 대해서 테스트를 추가합니다.
