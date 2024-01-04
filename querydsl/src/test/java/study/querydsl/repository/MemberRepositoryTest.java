package study.querydsl.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.querydsl.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;
    @Test
    public void basicTest() {
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);
        Member findMember = memberRepository.findById(member1.getId()).orElseThrow();
        assertThat(findMember).isEqualTo(member1);
        List<Member> result1 = memberRepository.findAll();
        assertThat(result1).contains(member1);
        List<Member> result2 = memberRepository.findByUsername("member1");
        assertThat(result2).containsExactly(member1);
    }
}