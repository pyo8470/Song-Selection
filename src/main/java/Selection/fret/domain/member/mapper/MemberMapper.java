package Selection.fret.domain.member.mapper;

import Selection.fret.domain.member.dto.MemberDto;
import Selection.fret.domain.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberPostDtoToMember(MemberDto.PostDto postDto);

    Member memberPacthDtoToMember(MemberDto.PatchDto patchDto);

    MemberDto.ResponseDto memberToResponseDto(Member member);
}
