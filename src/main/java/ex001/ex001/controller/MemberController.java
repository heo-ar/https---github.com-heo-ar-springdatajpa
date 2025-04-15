package ex001.ex001.controller;
import ex001.ex001.dto.MemberDTO;
import ex001.ex001.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class MemberController {
    private final MemberService ms;
    private final MemberService service;

    // 회원가입 처리
    @PostMapping("members")
    public ResponseEntity insert(@RequestBody MemberDTO dto) {
        log.info("ctrl dto: {}", dto);
        int result = ms.insert(dto);    // 인서트 호출 > 회원정보(dto) 넘김
        if(result == 0) // 성공 여부( 0이면 회원가입 처리)
        return ResponseEntity.status(HttpStatus.CREATED).build();
         return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @GetMapping("members")
    public ResponseEntity list(){
        List<MemberDTO> list = ms.getList();
        return ResponseEntity.ok().body( list );
    }

    @GetMapping("/members/{number}")
    public ResponseEntity getData(@PathVariable("number") long number){
        MemberDTO dto = ms.getData(number);
        if (dto != null)
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }





    // 페이징 처리
    @GetMapping("/members/list")
    public ResponseEntity<Page<MemberDTO>> pagedList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {

        Page<MemberDTO> result = ms.getPagedList(PageRequest.of(page, size));
        return ResponseEntity.ok().body(result);
    }


    @GetMapping("/api/content/{number}")
    public ResponseEntity getContent(@PathVariable("number") long number){
        MemberDTO dto = ms.getContent(number);
        if (dto != null)
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // 회원 데이터 등록
    @PostMapping("api/content")
    public ResponseEntity insertContent(@RequestBody MemberDTO dto) {
        log.info("ctrl dto: {}", dto);
        int result = ms.insertContent(dto);
        if(result == 1) // 성공이면 1로 처리
            return ResponseEntity.status(HttpStatus.CREATED).build();
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody MemberDTO dto) {
        log.info("login ctrl dto: {}", dto);
        MemberDTO result = ms.login(dto.getUserId(), dto.getPassword());
        if (result != null)
            return ResponseEntity.status(HttpStatus.OK).body(result);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // 키워드로 검색
    //  포스트맨 링크 검색 :  localhost:8080/members/search?keyword=김
    @GetMapping("/members/search")
    public ResponseEntity search(@RequestParam("keyword") String keyword) {
        List<MemberDTO> list = ms.searchByName(keyword);
        return ResponseEntity.ok().body(list);
    }

    // 멤버 정보 수정
    // 포스트맨 링크 : localhost:8080/members/user02
    @PutMapping("/members/{username}")
    public ResponseEntity update(@PathVariable("username") String userId,
                                 @RequestBody MemberDTO dto) {

        int result = ms.updateData(userId, dto);
        if(result == 1)
            return ResponseEntity.status(HttpStatus.OK).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // 멤버 정보 삭제
    // 포스트맨 링크 : localhost:8080/members/1
    @DeleteMapping("/members/{num}")
    public ResponseEntity deleteData(@PathVariable("num") Long num){
        int result = ms.deleteData(num);
        if(result == 1)
            return ResponseEntity.status(HttpStatus.OK).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //
    @PutMapping("/members/update")
    public ResponseEntity<String> updateMember(@RequestParam String userId, @RequestBody MemberDTO dto) {
        int result = service.updateData(userId, dto);
        return result > 0 ? ResponseEntity.ok("수정 성공") : ResponseEntity.badRequest().body("수정 실패");
    }


}