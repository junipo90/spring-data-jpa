package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;


    @Test
    public void testMember() throws Exception {
        //given
        Member member = new Member("userA", 30);
        Member saveMember = memberRepository.save(member);

        //when
        Member findMember = memberRepository.findById(saveMember.getId()).get();

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(saveMember.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(saveMember.getUsername());
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1", 30);
        Member member2 = new Member("member2", 40);

        Member saveMember1 = memberRepository.save(member1);
        Member saveMember2 = memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(saveMember1.getId()).get();
        Member findMember2 = memberRepository.findById(saveMember2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen() {
        Member member1 = new Member("member1", 30);
        Member member2 = new Member("member1", 40);

        Member saveMember1 = memberRepository.save(member1);
        Member saveMember2 = memberRepository.save(member2);

        List<Member> findMemberList = memberRepository.findMemberByUsernameAndAgeGreaterThan("member1", 35);
        assertThat(findMemberList.size()).isEqualTo(1);
        assertThat(findMemberList.get(0).getUsername()).isEqualTo("member1");
        assertThat(findMemberList.get(0).getAge()).isEqualTo(40);
    }

    @Test
    public void findMember() {
        Member member1 = new Member("member1", 30);
        Member member2 = new Member("member1", 40);

        Member saveMember1 = memberRepository.save(member1);
        Member saveMember2 = memberRepository.save(member2);

        List<Member> findMemberList = memberRepository.findMember("member1", 30);
        assertThat(findMemberList.size()).isEqualTo(1);
        assertThat(findMemberList.get(0).getUsername()).isEqualTo("member1");
        assertThat(findMemberList.get(0).getAge()).isEqualTo(30);
    }

    @Test
    public void findMemberUsername() {
        Member member1 = new Member("member1", 30);
        Member member2 = new Member("member2", 40);

        Member saveMember1 = memberRepository.save(member1);
        Member saveMember2 = memberRepository.save(member2);

        List<String> findMemberUsernameList = memberRepository.findUsername();
        for (String s : findMemberUsernameList) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void findMemberDto() {
        Team team1 = new Team("Team1");
        teamRepository.save(team1);

        Member member1 = new Member("member1", 30, team1);
        memberRepository.save(member1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
    }

    @Test
    public void findMemberByNames() {
        Member member1 = new Member("member1", 30);
        Member member2 = new Member("member2", 40);

        Member saveMember1 = memberRepository.save(member1);
        Member saveMember2 = memberRepository.save(member2);

        List<Member> result = memberRepository.findMemberByNames(Arrays.asList("member1", "member2"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }


}