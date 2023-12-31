package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // readOnly를 사용하면 좀 더 쿼리가 최적화된다. 읽기 성능이 좋아짐
public class MemberService {
    
    @Autowired
    private MemberRepository memberRepository;

    @Transactional // 데이터 변경하는 로직에서는 transactional 애노테이션이 꼭 있어야 한다.
    // 기본적으로 readOnly 는 false로 적용. 매서드 레벨 애노테이션이 우선적으로 적용된다.
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}
