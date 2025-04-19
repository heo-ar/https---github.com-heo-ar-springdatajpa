package ex001.ex001.service;

import ex001.ex001.domain.MemberEntity;
import ex001.ex001.dto.MemberDTO;
import ex001.ex001.repo.MemberRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberService {

    private final MemberRepo repo;
    private final BCryptPasswordEncoder passwordEncoder; // 🔐 암호화기 주입

    public int insert(MemberDTO dto){
        int result = 0;
        try {
            // 🔐 비밀번호 암호화
            String encodedPassword = passwordEncoder.encode(dto.getPassword());
            dto.setPassword(encodedPassword);

            MemberEntity entity = repo.save(new MemberEntity(dto));
            log.info("service entity : {}", entity);
        } catch (Exception e) {
            result = 1;
            e.printStackTrace();
        }
        return result;
    }

    public List<MemberDTO> getList(){
        List<MemberDTO> list = null;
        List<MemberEntity> listE = repo.findAll();

        if(listE.size() != 0){
            list = listE.stream()
                    .map(m -> new MemberDTO(m))
                    .toList();
        }

        log.info("list entity : {}", listE);
        return list;
    }

    public MemberDTO getData(long number){
        Optional<MemberEntity> opM = repo.findById(number);
        MemberEntity entity = opM.orElse(null);
        if(entity != null)
            return new MemberDTO(entity);
        return null;
    }

    public int deleteData(long num){
        int result = 0;
        MemberDTO dto = getData(num);
        if(dto != null){
            repo.deleteById(num);
            result = 1;
        }
        return result;
    }

    public List<MemberDTO> getListPage(int start, int page){
        Pageable pageable = PageRequest.of(start, page,
                Sort.by(Sort.Order.desc("number")));
        Page<MemberEntity> pageEntity = repo.findAll(pageable);
        List<MemberEntity> listE = pageEntity.getContent();
        List<MemberDTO> list = listE.stream()
                .map(m -> new MemberDTO(m))
                .toList();
        return list;
    }

    public MemberDTO getContent(long number){
        MemberEntity entity = repo.findByContent(number);
        log.info("entity : {}", entity);
        if(entity != null)
            return new MemberDTO(entity);
        return null;
    }

    public int insertContent(MemberDTO dto){
        int result = 0;
        try {
            result = repo.insertContent(
                    dto.getUserId(),
                    dto.getUserName(),
                    dto.getAge(),
                    dto.getPassword() // 이 부분은 DTO 단에서 이미 암호화된 상태라고 가정
            );
            log.info("service result : {}", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // 🔐 로그인 메서드 (암호화 검증)
    public MemberDTO login(String userId, String rawPassword) {
        MemberEntity entity = repo.findByUserId(userId);
        log.info("login service entity : {}", entity);
        if (entity != null && passwordEncoder.matches(rawPassword, entity.getPassword())) {
            return new MemberDTO(entity);
        }
        return null;
    }

    public Page<MemberDTO> getPagedList(Pageable pageable) {
        Page<MemberEntity> pageEntity = repo.findAll(pageable);
        Page<MemberDTO> pageDTO = pageEntity.map(MemberDTO::new);
        return pageDTO;
    }

    public List<MemberDTO> searchByName(String keyword) {
        List<MemberEntity> list = repo.findByUserNameContaining(keyword);
        return list.stream().map(MemberDTO::new).toList();
    }

    public int updateData(String userId, MemberDTO dto){
        return repo.updateMemberNative(userId, dto.getUserName(), dto.getAge());
    }
}
