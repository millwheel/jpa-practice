package study.datajpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDtoWithTeam;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;

// Spring Data JPA가 구현 클래스를 알아서 프록시로 자동으로 만들어서 붙여줌
// @Repository 생략 가능.
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    // 쿼리 메서드
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // 쿼리 정의하기, 문법은 JPQL
    @Query("select m from Member m where m.username= :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    // 쿼리로 값만 조회
    @Query("select m.username from Member m")
    List<String> findUsernameList();

    // 쿼리로 DTO로 가져오기
    @Query("select new study.datajpa.dto.MemberDtoWithTeam(m.id, m.username, t.name) " +
            "from Member m join m.team t")
    List<MemberDtoWithTeam> findMemberDto();

    // 컬렉션으로 조회하기 (List 등)
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names")Collection<String> names);

    // 페이징
    // count query 자동 최적화
    Page<Member> findByAge(int age, Pageable pageable);

    List<Member> findTop3By();

    // Modifying 애노테이션이 있어야 jpa의 executeUpdate를 실행한다. 이게 없으면 getResultList 나 getSingleResult를 수행한다.
    // 벌크성 쿼리를 사용하면 JPA 영속성 컨텍스트를 무시하고 바로 DB에 쿼리를 날리게 된다.
    // 벌크 연산 사용 이후에는 영속성 컨텍스트를 모두 초기화 해줘야 한다.
    @Modifying
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    //JPQL 없이 Fetch join 사용하기 - 쿼리가 간단할 때 사용
//    @EntityGraph(attributePaths = {"team"})
//    List<Member> findByUsername(String username);

    //JPQL과 함께 Fetch join 사용하기 - 쿼리가 복잡할 때 사용
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    // readonly를 쓰면 변경 감지 체크를 하지 않는다. 성능이 살짝 좋아진다. 사실 큰 차이는 안 남.
    @QueryHints(value = { @QueryHint(name = "org.hibernate.readOnly", value = "true")}, forCounting = true)
    Page<Member> findReadOnlyByUsername(String name, Pageable pageable);

    // lock 사용 - 실시간 트래픽이 많으면 해당 락을 사용하면 안 된다.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findByUsername(String name);

}
