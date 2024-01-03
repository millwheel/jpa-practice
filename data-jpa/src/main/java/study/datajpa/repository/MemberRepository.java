package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;

// Spring Data JPA가 구현 클래스를 알아서 프록시로 자동으로 만들어서 붙여줌
// @Repository 생략 가능.
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 쿼리 메서드
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // 쿼리 정의하기, 문법은 JPQL
    @Query("select m from Member m where m.username= :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    // 쿼리로 값만 조회
    @Query("select m.username from Member m")
    List<String> findUsernameList();

    // 쿼리로 DTO로 가져오기
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) " +
            "from Member m join m.team t")
    List<MemberDto> findMemberDto();

    // 컬렉션으로 조회하기 (List 등)
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names")Collection<String> names);

    // 페이징
    // count query 자동 최적화
    Page<Member> findByAge(int age, Pageable pageable);

    List<Member> findTop3By();
}
