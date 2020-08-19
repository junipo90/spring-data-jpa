package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    List<Member> findMemberByUsernameAndAgeGreaterThan(String username, int age);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findMember(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsername();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findMemberByNames(@Param("names") Collection<String> names);

    List<Member> findByUsername(String s);

    // Page 는 totalCount 쿼리가 자동으로 나간다
    // CountQuery 최적화 필요하면 count 는 join 안하고 쿼리를 날려서 memberCount 만 가져오게
//    @Query(value = "select m from Member m left join m.team t",
//            countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);
//    Slice<Member> findByAge(int age, Pageable pageable);

    // 벌크성 업데이트 쿼리에는 꼭 넣어 줘야 한다. 그리고 영속성 컨택스트를 클리어 해줘야
    // 같은 로직안에서 디비와 영속성 컨텍스트 사이의 오류를 없앨 수 있다.
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMembersFetchJoin();

    // spring data jpa 에서 fetch join 간편하게 해준다
    // 다른 쿼리 메소드 같은 곳에서도 사용 가능
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();


    // QueryHint 로 readOnly 넣어서 변경 감지 안되게 읽기 전용 (조회만하는데 데이터가 너무 엄청 많으면...)
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);
}
