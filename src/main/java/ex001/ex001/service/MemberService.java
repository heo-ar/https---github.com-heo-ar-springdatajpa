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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberService {
    private final MemberRepo repo;
    public int insert(MemberDTO dto){
        int result =0;
        try {   //insert, update
            MemberEntity entity =repo.save( new MemberEntity(dto) );
            log.info("service entity : {}", entity);
        } catch (Exception e) {
            result = 1;
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
        return result;
    }
    public List<MemberDTO> getList(){
        List<MemberDTO> list = null;
        List<MemberEntity> listE = repo.findAll();

        // 1. 스트림 이용해서 처리
        if(listE.size() != 0){
            list = listE.stream()
                    .map( m -> new MemberDTO(m) )
                    .toList();
            // 2. 반복문 이용해서 처리
            // List<MemberDTO> list = new ArrayList<>();
            // for(MemberEntity e : listE){
            //     list.add( new MemberDTO(e));
        }



        log.info("list entity : {}", listE);
        return  list;
    }
    public MemberDTO getData(long number){
        Optional<MemberEntity> opM = repo.findById(number);
        MemberEntity entity = opM.orElse(null);
        if( entity != null )
            return new MemberDTO( entity );
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





    public List<MemberDTO>getListPage(int start, int page ){
//        int page = 3;
        Pageable pageable = PageRequest.of(start, page,
                                    Sort.by(Sort.Order.desc("number")) );   // desc 내림차순으로 정렬
        Page<MemberEntity> pageEntity = repo.findAll( pageable );
        List<MemberEntity>listE = pageEntity.getContent();
        List<MemberDTO> list = listE.stream()
                .map( m -> new MemberDTO(m))
                .toList();
        return list;
    }

    public MemberDTO getContent(long number){
        MemberEntity entity = repo.findByContent(number);
        log.info("entity : {}", entity);
        if( entity != null )
            return new MemberDTO( entity );
        return null;
    }

    public int insertContent(MemberDTO dto){
        int result =0;  // 0이면 문제발생
        try {   //insert, update
            result = repo.insertContent(
                    dto.getUserId(),
                    dto.getUserName(),
                    dto.getAge(),
                    dto.getPassword()  // ← 요거 추가!
            );

            log.info("service result : {}", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // 로그인 메서드
    public MemberDTO login(String userId, String password) {
        MemberEntity entity = repo.login(userId, password);
        log.info("login service entity : {}", entity);
        if (entity != null)
            return new MemberDTO(entity);
        return null;
    }

    // Page<MemberDTO> 형태로 페이징 처리
    public Page<MemberDTO> getPagedList(Pageable pageable) {
        Page<MemberEntity> pageEntity = repo.findAll(pageable);

        Page<MemberDTO> pageDTO = pageEntity.map(entity -> new MemberDTO(entity));
        return pageDTO;
    }

    // 키워드로 검색
    public List<MemberDTO> searchByName(String keyword) {
        List<MemberEntity> list = repo.findByUserNameContaining(keyword);
        return list.stream().map(MemberDTO::new).toList();
    }

    // nativeQuery 수정
    public int updateData(String userId, MemberDTO dto){
        return repo.updateMemberNative(userId, dto.getUserName(), dto.getAge());
    }



}