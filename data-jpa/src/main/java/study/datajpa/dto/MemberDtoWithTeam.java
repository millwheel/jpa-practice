package study.datajpa.dto;


import lombok.Data;

@Data
public class MemberDtoWithTeam {
    private Long id;
    private String username;
    private String teamName;
    public MemberDtoWithTeam(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }
}