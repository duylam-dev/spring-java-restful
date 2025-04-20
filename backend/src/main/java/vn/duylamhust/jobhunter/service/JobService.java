package vn.duylamhust.jobhunter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.duylamhust.jobhunter.domain.Job;
import vn.duylamhust.jobhunter.domain.Skill;
import vn.duylamhust.jobhunter.domain.response.ResPaginationDTO;
import vn.duylamhust.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.duylamhust.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.duylamhust.jobhunter.repository.JobRepository;
import vn.duylamhust.jobhunter.repository.SkillRepository;
import vn.duylamhust.jobhunter.util.Mapper.JobMapper;
import vn.duylamhust.jobhunter.util.error.IdInvalidException;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final JobMapper jobMapper;

    public ResCreateJobDTO handleCreate(Job job) throws IdInvalidException {
        List<Long> skill_ids = job.getSkills().stream().map(Skill::getId).collect(Collectors.toList());
        // get All skill
        var skills = skillRepository.findAllById(skill_ids);
        job.setSkills(skills);

        List<String> skill_name = skills.stream().map(Skill::getName).collect(Collectors.toList());

        var res = jobMapper.toResCreateJobDTO(jobRepository.save(job));
        res.setSkills(skill_name);
        return res;
    }

    public ResUpdateJobDTO handleUpdate(Job job) throws IdInvalidException {

        var jobDB = jobRepository.findById(job.getId()).orElseThrow(() -> new IdInvalidException("job not found!"));

        List<Long> skill_ids = job.getSkills().stream().map(Skill::getId).collect(Collectors.toList());
        // get All skill
        var skills = skillRepository.findAllById(skill_ids);
        job.setSkills(skills);

        List<String> skill_name = skills.stream().map(Skill::getName).collect(Collectors.toList());

        jobMapper.update(jobDB, job);
        var res = jobMapper.toResUpdateJobDTO(jobRepository.save(jobDB));
        res.setSkills(skill_name);
        return res;
    }

    public ResPaginationDTO fetchAllJob(Specification<Job> spec, Pageable pageable) {
        Page<Job> jobPage = jobRepository.findAll(spec, pageable);
        var rp = new ResPaginationDTO();
        var mt = new ResPaginationDTO.Meta(
                pageable.getPageNumber() + 1,
                pageable.getPageSize(),
                jobPage.getTotalPages(),
                jobPage.getTotalElements());
        rp.setMeta(mt);
        rp.setResult(jobPage.getContent());

        return rp;
    }

    public void handleDelete(long id) {
        jobRepository.deleteById(id);
    }

    public Job fetchJobById(long id) throws IdInvalidException {
        return jobRepository.findById(id).orElseThrow(() -> new IdInvalidException("job not found!"));
    }
}
