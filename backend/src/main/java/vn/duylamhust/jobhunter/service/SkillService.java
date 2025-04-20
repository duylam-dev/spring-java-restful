package vn.duylamhust.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.duylamhust.jobhunter.domain.Skill;
import vn.duylamhust.jobhunter.domain.response.ResPaginationDTO;
import vn.duylamhust.jobhunter.repository.SkillRepository;
import vn.duylamhust.jobhunter.util.error.IdInvalidException;

@Service
@RequiredArgsConstructor
public class SkillService {
    private final SkillRepository skillRepository;

    public Skill handleCreate(Skill skill) throws IdInvalidException {
        existByName(skill.getName());
        return skillRepository.save(skill);
    }

    public Skill handleUpdate(Skill skill) throws IdInvalidException {
        var skillOld = skillRepository.findById(skill.getId()).orElseThrow(
                () -> new IdInvalidException("skill not exist, please enter other skill."));
        existByName(skill.getName());

        skillOld.setName(skill.getName());
        return skillRepository.save(skillOld);
    }

    private void existByName(String name) throws IdInvalidException {
        var res = skillRepository.findByName(name);
        if (res.isPresent())
            throw new IdInvalidException("skill exist, please enter other skill.");
    }

    public ResPaginationDTO fetchAllSkill(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> skillPage = skillRepository.findAll(spec, pageable);
        var rp = new ResPaginationDTO();
        var mt = new ResPaginationDTO.Meta(
                pageable.getPageNumber() + 1,
                pageable.getPageSize(),
                skillPage.getTotalPages(),
                skillPage.getTotalElements());
        rp.setMeta(mt);
        rp.setResult(skillPage.getContent());
        return rp;
    }

    public void delete(long id) throws IdInvalidException {
        var skill = skillRepository.findById(id).orElseThrow(
                () -> new IdInvalidException("skill not exist, please enter other skill."));
        skill.getJobs().forEach(job -> job.getSkills().remove(skill));
        skill.getSubscribers().forEach(sub -> sub.getSkills().remove(skill));

        skillRepository.delete(skill);
    }
}
