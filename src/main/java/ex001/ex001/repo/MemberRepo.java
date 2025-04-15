package ex001.ex001.repo;

import ex001.ex001.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemberRepo extends JpaRepository<MemberEntity, Long> {
    MemberEntity findByUserId(String userId);   // 로그인용 처리

    List<MemberEntity> findByUserNameContaining(String keyword);

    @Query(value = "select * from member_test where number=:n", nativeQuery = true)
    MemberEntity findByContent(@Param("n") long num);

    @Modifying
    @Transactional
    @Query(value="insert into member_test(user_id, user_name, age, password) " +
            "values(:id, :name, :age, :pw)", nativeQuery = true)
    int insertContent(@Param("id")String userId,
                      @Param("name") String userName,
                      @Param("age") int age,
                      @Param("pw") String password);


    // 로그인(id, psw)용 쿼리
    @Query(value = "SELECT * FROM member_test WHERE user_id = :id AND password = :pw", nativeQuery = true)
    MemberEntity login(@Param("id") String userId, @Param("pw") String password);

    // nativeQuery로 수정
    @Modifying
    @Transactional
    @Query(value = "UPDATE member_test SET user_name = :name, age = :age WHERE user_id = :id", nativeQuery = true)
    int updateMemberNative(@Param("id") String userId,
                           @Param("name") String userName,
                           @Param("age") int age);


}
