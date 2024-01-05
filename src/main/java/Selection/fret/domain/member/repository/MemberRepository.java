package Selection.fret.domain.member.repository;

import Selection.fret.domain.member.entity.Member;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,String> {
    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);
}
