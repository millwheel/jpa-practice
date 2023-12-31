package study.querydsl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.repository.MemberJpaRepository;
import study.querydsl.repository.MemberRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberJpaRepository memberJpaRepository;
    private final MemberRepository memberRepository;
    @GetMapping("/v1/members")
    public List<MemberTeamDto> searchMemberV1(MemberSearchCondition condition) {
        return memberJpaRepository.search(condition);
    }

    @GetMapping("/v2/members")
    public List<MemberTeamDto> searchMemberV2(MemberSearchCondition condition) {
        return memberRepository.search(condition);
    }

    @GetMapping("/v3/members")
    public Page<MemberTeamDto> searchMemberWithPage(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    MemberSearchCondition condition) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return memberRepository.searchWithPage(condition, pageRequest);
    }

    @GetMapping("/v4/members")
    public Page<MemberTeamDto> searchMemberWithPageAdvanced(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    MemberSearchCondition condition) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return memberRepository.searchWithPageAdvanced(condition, pageRequest);
    }

}
