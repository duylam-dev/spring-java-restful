package vn.duylamhust.jobhunter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;

import lombok.RequiredArgsConstructor;
import vn.duylamhust.jobhunter.domain.Company;
import vn.duylamhust.jobhunter.domain.Resume;
import vn.duylamhust.jobhunter.domain.User;
import vn.duylamhust.jobhunter.domain.response.ResPaginationDTO;
import vn.duylamhust.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.duylamhust.jobhunter.domain.response.resume.ResResumeDTO;
import vn.duylamhust.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.duylamhust.jobhunter.repository.JobRepository;
import vn.duylamhust.jobhunter.repository.ResumeRepository;
import vn.duylamhust.jobhunter.repository.UserRepository;
import vn.duylamhust.jobhunter.util.SecurityUtil;
import vn.duylamhust.jobhunter.util.Mapper.ResumeMapper;
import vn.duylamhust.jobhunter.util.error.IdInvalidException;

@Service
@RequiredArgsConstructor
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final ResumeMapper resumeMapper;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final FilterParser filterParser;
    private final FilterSpecificationConverter filterSpecificationConverter;
    private final FilterBuilder filterBuilder;

    public ResCreateResumeDTO handleCreate(Resume resume) throws IdInvalidException {
        if (resume.getUser() == null || resume.getJob() == null)
            return null;
        var check_user = userRepository.existsById(resume.getUser().getId());
        var check_job = jobRepository.existsById(resume.getJob().getId());
        if (!check_user || !check_job)
            throw new IdInvalidException("user id or job id not exits!");
        return resumeMapper.toResCreateResumeDTO(resumeRepository.save(resume));
    }

    public ResPaginationDTO findByUser(Pageable pageable) {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        FilterNode node = filterParser.parse("email='" + email + "'");
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
        Page<Resume> pageResume = resumeRepository.findAll(spec, pageable);

        var rp = new ResPaginationDTO();

        var mt = new ResPaginationDTO.Meta(
                pageable.getPageNumber() + 1,
                pageable.getPageSize(),
                pageResume.getTotalPages(),
                pageResume.getTotalElements());
        rp.setMeta(mt);
        var res = pageResume.getContent().stream().map(x -> {
            var resDTO = resumeMapper.toResResumeDTO(x);
            resDTO.setUser(
                    new ResResumeDTO.UserResume(x.getUser().getId(), x.getUser().getName()));
            resDTO.setJob(
                    new ResResumeDTO.JobResume(x.getJob().getId(), x.getJob().getName()));
            resDTO.setCompanyName(x.getJob().getCompany().getName());
            return resDTO;
        });
        rp.setResult(res);
        return rp;
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
        // get user signed
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        User currentUser = userRepository.findByEmail(email).orElse(null);
        List<Long> jobIds = null;
        if (currentUser != null) {
            Company company = currentUser.getCompany();
            if (company != null) {
                jobIds = company.getJobs().stream().map(item -> {
                    return item.getId();
                }).collect(Collectors.toList());
            }
        }

        Specification<Resume> jobInSpec = filterSpecificationConverter.convert(filterBuilder.field("job")
                .in(filterBuilder.input(jobIds)).get());
        var finalSpec = jobIds == null ? spec : jobInSpec.and(spec);
        Page<Resume> jobPage = resumeRepository.findAll(finalSpec, pageable);
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
            resDTO.setCompanyName(x.getJob().getCompany().getName());

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
