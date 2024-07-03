package vn.hoidanit.jobhunter.util.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResUpdateJobDTO;

@Mapper(componentModel = "spring")
public interface JobMapper {
    @Mapping(target = "skills", ignore = true)
    ResCreateJobDTO toResCreateJobDTO(Job job);

    @Mapping(target = "skills", ignore = true)
    ResUpdateJobDTO toResUpdateJobDTO(Job job);

    @Mapping(target = "skills", ignore = true)
    ResJobDTO toResJobDTO(Job job);

    @Mapping(target = "skills", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void update(@MappingTarget Job jobDB, Job newJob);
}
