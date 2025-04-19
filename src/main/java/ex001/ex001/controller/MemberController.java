package ex001.ex001.controller;

import ex001.ex001.dto.MemberDTO;
import ex001.ex001.service.MemberService;
import ex001.ex001.utils.JwtUtil; // ✅ JwtUtil import 추가
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value; // ✅ secretKey 주입용 import
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Slf4j
public class MemberController {
    private final MemberService ms;
    private final MemberService service;

    // ✅ JWT secretKey 주입
    @Value("${jwt.secretKey}")
    private String secretKey;

    // 회원가입 처리
    @PostMapping("members")
    public ResponseEntity insert(@RequestBody MemberDTO dto) {
        log.info("ctrl dto: {}", dto);
        int result = ms.insert(dto);
        if(result == 0)
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

    @PostMapping("api/content")
    public ResponseEntity insertContent(@RequestBody MemberDTO dto) {
        log.info("ctrl dto: {}", dto);
        int result = ms.insertContent(dto);
        if(result == 1)
            return ResponseEntity.status(HttpStatus.CREATED).build();
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    // ✅ 로그인 + JWT 토큰 발급
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberDTO dto) {
        log.info("login ctrl dto: {}", dto);
        MemberDTO result = ms.login(dto.getUserId(), dto.getPassword());

        if (result != null) {
            String token = JwtUtil.createToken(dto.getUserId(), secretKey, 60 * 60 * 1000L);

            log.info("✅ 토큰 생성 완료: {}", token); // ← 로그로도 확인 가능

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("message", "Login Success");

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패");
    }


    @GetMapping("/members/search")
    public ResponseEntity search(@RequestParam("keyword") String keyword) {
        List<MemberDTO> list = ms.searchByName(keyword);
        return ResponseEntity.ok().body(list);
    }

    @PutMapping("/members/{username}")
    public ResponseEntity update(@PathVariable("username") String userId,
                                 @RequestBody MemberDTO dto) {
        int result = ms.updateData(userId, dto);
        if(result == 1)
            return ResponseEntity.status(HttpStatus.OK).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/members/{num}")
    public ResponseEntity deleteData(@PathVariable("num") Long num){
        int result = ms.deleteData(num);
        if(result == 1)
            return ResponseEntity.status(HttpStatus.OK).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/members/update")
    public ResponseEntity<String> updateMember(@RequestParam String userId, @RequestBody MemberDTO dto) {
        int result = service.updateData(userId, dto);
        return result > 0 ? ResponseEntity.ok("수정 성공") : ResponseEntity.badRequest().body("수정 실패");
    }
}
