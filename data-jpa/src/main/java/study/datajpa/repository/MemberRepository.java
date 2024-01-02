package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Member;

import java.util.List;

// Spring Data JPA가 구현 클래스를 알아서 프록시로 자동으로 만들어서 붙여줌
// @Repository 생략 가능.
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 쿼리 메서드
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
}
