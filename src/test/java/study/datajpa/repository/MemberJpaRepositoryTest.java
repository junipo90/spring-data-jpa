package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberJpaRepositoryTest {

    @Autowired
    private MemberJpaRepository memberJpaRepository;
    
    @Test
    public void testMember() throws Exception {
        //given
        Member member = new Member("userA", 30);
        Member saveMember = memberJpaRepository.save(member);

        //when
        Member findMember = memberJpaRepository.find(saveMember.getId());

        //then
        assertThat(findMember.getId()).isEqualTo(saveMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(saveMember.getUsername());
    }

    @Test
    public void basicCRUD(){
        Member member1 = new Member("member1", 30);
        Member member2 = new Member("member2", 40);

        Member saveMember1 = memberJpaRepository.save(member1);
        Member saveMember2 = memberJpaRepository.save(member2);

        Member findMember1 = memberJpaRepository.findById(saveMember1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(saveMember2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        long deletedCount = memberJpaRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }
    
    @Test
    public void findByUsernameAndAgeGreaterThen(){
        Member member1 = new Member("member1", 30);
        Member member2 = new Member("member1", 40);

        Member saveMember1 = memberJpaRepository.save(member1);
        Member saveMember2 = memberJpaRepository.save(member2);

        List<Member> findMemberList = memberJpaRepository.findByUsernameAndAgeGreaterThen("member1", 35);
        assertThat(findMemberList.size()).isEqualTo(1);
        assertThat(findMemberList.get(0).getUsername()).isEqualTo("member1");
        assertThat(findMemberList.get(0).getAge()).isEqualTo(40);
    }

    @Test
    public void paging() throws Exception {
        //given
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 10));
        memberJpaRepository.save(new Member("member3", 10));
        memberJpaRepository.save(new Member("member4", 10));
        memberJpaRepository.save(new Member("member5", 10));

        //when
        int age = 10;
        int offset = 0;
        int limit = 3;
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        //then
        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(5);
    }



}