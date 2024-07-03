package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResPaginationDTO;
import vn.hoidanit.jobhunter.service.SkillService;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
public class SkillController extends BaseController {
    private final SkillService skillService;

    @PostMapping("/skills")
    public ResponseEntity<Skill> create(@RequestBody @Valid Skill skill) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(skillService.handleCreate(skill));
    }

    @PutMapping("/skills")
    public ResponseEntity<Skill> update(@RequestBody @Valid Skill skill) throws IdInvalidException {
        return ResponseEntity.ok(skillService.handleUpdate(skill));
    }

    @GetMapping("/skills")
    public ResponseEntity<ResPaginationDTO> getAll(
            @Filter Specification<Skill> spec, Pageable pageable) {
        return ResponseEntity.ok(skillService.fetchAllSkill(spec, pageable));
    }

}
