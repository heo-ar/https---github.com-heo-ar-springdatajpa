package ex001.ex001.config;

import ex001.ex001.repo.MemberRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DummyDataInit implements CommandLineRunner {

    private final MemberRepo memberRepo;

    @Override
    public void run(String... args) throws Exception { 
        if (memberRepo.findByUserId("user01") == null)
            memberRepo.insertContent("user01", "홍길동", 25, "user01");

        if (memberRepo.findByUserId("user02") == null)
            memberRepo.insertContent("user02", "김철수", 30, "user02");

        if (memberRepo.findByUserId("user03") == null)
            memberRepo.insertContent("user03", "이영희", 22, "user03");

        if (memberRepo.findByUserId("user04") == null)
            memberRepo.insertContent("user04", "박민수", 28, "user04");

        if (memberRepo.findByUserId("user05") == null)
            memberRepo.insertContent("user05", "최서연", 24, "user05");

        if (memberRepo.findByUserId("user06") == null)
            memberRepo.insertContent("user06", "정우진", 31, "user06");

        if (memberRepo.findByUserId("user07") == null)
            memberRepo.insertContent("user07", "한예린", 27, "user07");

        if (memberRepo.findByUserId("user08") == null)
            memberRepo.insertContent("user08", "오지훈", 26, "user08");

        if (memberRepo.findByUserId("user09") == null)
            memberRepo.insertContent("user09", "서하늘", 29, "user09");

        if (memberRepo.findByUserId("user10") == null)
            memberRepo.insertContent("user10", "장도윤", 33, "user10");

        if (memberRepo.findByUserId("user11") == null)
            memberRepo.insertContent("user11", "이슬아", 21, "user11");

        if (memberRepo.findByUserId("user12") == null)
            memberRepo.insertContent("user12", "김태현", 34, "user12");

    }


}

