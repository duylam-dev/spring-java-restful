package vn.hoidanit.jobhunter.util.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;

@Mapper(componentModel = "spring")
public interface ResumeMapper {
    ResCreateResumeDTO toResCreateResumeDTO(Resume resume);

    ResUpdateResumeDTO toResUpdateJobDTO(Resume resume);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "job", ignore = true)
    ResResumeDTO toResResumeDTO(Resume resume);
}
