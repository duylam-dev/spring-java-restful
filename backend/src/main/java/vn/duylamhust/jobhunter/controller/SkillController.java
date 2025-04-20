package vn.duylamhust.jobhunter.controller;

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
import vn.duylamhust.jobhunter.domain.Skill;
import vn.duylamhust.jobhunter.domain.response.ResPaginationDTO;
import vn.duylamhust.jobhunter.service.SkillService;
import vn.duylamhust.jobhunter.util.error.IdInvalidException;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @DeleteMapping("/skills/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") long id) throws IdInvalidException {
        skillService.delete(id);
        return ResponseEntity.ok(null);
    }

}
