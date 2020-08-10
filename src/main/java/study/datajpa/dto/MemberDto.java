package study.datajpa.dto;

import lombok.Data;

@Data
public class MemberDto {
    private Long id;
    private String username;
    private String TeamName;

    public MemberDto(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        TeamName = teamName;
    }
}
