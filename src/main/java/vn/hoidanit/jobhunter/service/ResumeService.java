package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.response.ResPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.ResumeRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.Mapper.ResumeMapper;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@Service
@RequiredArgsConstructor
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final ResumeMapper resumeMapper;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public ResCreateResumeDTO handleCreate(Resume resume) throws IdInvalidException {
        if (resume.getUser() == null || resume.getJob() == null)
            return null;
        var check_user = userRepository.existsById(resume.getUser().getId());
        var check_job = jobRepository.existsById(resume.getJob().getId());
        if (!check_user || !check_job)
            throw new IdInvalidException("user id or job id not exits!");
        return resumeMapper.toResCreateResumeDTO(resumeRepository.save(resume));
    }

    public ResUpdateResumeDTO handleUpdate(Resume resume) throws IdInvalidException {
        var resumeDb = resumeRepository.findById(resume.getId())
                .orElseThrow(() -> new IdInvalidException("resume not found!"));
        resumeDb.setStatus(resume.getStatus());
        return resumeMapper.toResUpdateJobDTO(resumeRepository.save(resumeDb));
    }

    public ResResumeDTO fetchById(long id) throws IdInvalidException {
        var resumeDb = resumeRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("resume not found!"));
        var res = resumeMapper.toResResumeDTO(resumeDb);
        var userResume = new ResResumeDTO.UserResume(resumeDb.getUser().getId(), resumeDb.getUser().getName());
        var jobResume = new ResResumeDTO.JobResume(resumeDb.getJob().getId(), resumeDb.getJob().getName());
        res.setUser(userResume);
        res.setJob(jobResume);
        return res;
    }

    public ResPaginationDTO fetchAll(Specification<Resume> spec, Pageable pageable) {
        Page<Resume> jobPage = resumeRepository.findAll(spec, pageable);
        var rp = new ResPaginationDTO();
        var mt = new ResPaginationDTO.Meta(
                pageable.getPageNumber() + 1,
                pageable.getPageSize(),
                jobPage.getTotalPages(),
                jobPage.getTotalElements());
        rp.setMeta(mt);
        var res = jobPage.getContent().stream().map(x -> {
            var resDTO = resumeMapper.toResResumeDTO(x);
            resDTO.setUser(
                    new ResResumeDTO.UserResume(x.getUser().getId(), x.getUser().getName()));
            resDTO.setJob(
                    new ResResumeDTO.JobResume(x.getJob().getId(), x.getJob().getName()));
            return resDTO;
        });

        rp.setResult(res);

        return rp;
    }

    public void handleDelete(long id) throws IdInvalidException {
        resumeRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("resume not found!"));
        resumeRepository.deleteById(id);
    }
}
