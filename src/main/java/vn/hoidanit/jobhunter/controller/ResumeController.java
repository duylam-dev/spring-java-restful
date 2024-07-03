package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import lombok.RequiredArgsConstructor;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.response.ResPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.service.ResumeService;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequiredArgsConstructor
public class ResumeController extends BaseController {
    private final ResumeService resumeService;

    @PostMapping("/resumes")
    public ResponseEntity<ResCreateResumeDTO> create(@RequestBody Resume resume) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(resumeService.handleCreate(resume));
    }

    @PutMapping("/resumes")
    public ResponseEntity<ResUpdateResumeDTO> update(@RequestBody Resume resume) throws IdInvalidException {
        return ResponseEntity.ok(resumeService.handleUpdate(resume));
    }

    @GetMapping("/resumes/{id}")
    public ResponseEntity<ResResumeDTO> getById(@PathVariable(name = "id") long id) throws IdInvalidException {
        return ResponseEntity.ok(resumeService.fetchById(id));
    }

    @GetMapping("/resumes")
    public ResponseEntity<ResPaginationDTO> getAll(
            @Filter Specification<Resume> spec, Pageable pageable) {
        return ResponseEntity.ok(resumeService.fetchAll(spec, pageable));
    }

    @DeleteMapping("/resumes/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") long id) throws IdInvalidException {
        resumeService.handleDelete(id);
        return ResponseEntity.ok(null);
    }
}
