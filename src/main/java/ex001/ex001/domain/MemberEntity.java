package ex001.ex001.domain;

// 데이터베이스를 갖고 오거나 데이터베이스에 전송할 때는 이 파일로 전부 처리됨

import ex001.ex001.dto.MemberDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "member_test")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long number;

    @NotNull
    @Column( unique = true, length = 20 )
    private String userId;  // user_id

    @Column(nullable = false, length = 100)
    private String password;    // password

    @Column(name="user_name", nullable = false, length = 30)
    private String userName;    //user_name

    @Column( name ="age")
    private int age;    // age

    public MemberEntity(MemberDTO dto){
        this.userId = dto.getUserId();
        this.userName = dto.getUserName();
        this.age = dto.getAge();
        this.password = dto.getPassword();
    }
}