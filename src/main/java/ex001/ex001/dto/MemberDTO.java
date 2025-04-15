package ex001.ex001.dto;

import ex001.ex001.domain.MemberEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class MemberDTO {
    private Long number;
    private String userId;
    private String userName;
    private String password;
    private int age;
    public MemberDTO(MemberEntity entity){
        this.number = entity.getNumber();
        this.userId = entity.getUserId();
        this.userName = entity.getUserName();
        this.password = entity.getPassword();
        this.age = entity.getAge();
    }


}
