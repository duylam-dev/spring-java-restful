package vn.duylamhust.jobhunter.controller;

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
import vn.duylamhust.jobhunter.domain.Job;
import vn.duylamhust.jobhunter.domain.response.ResPaginationDTO;
import vn.duylamhust.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.duylamhust.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.duylamhust.jobhunter.service.JobService;
import vn.duylamhust.jobhunter.util.error.IdInvalidException;

@RestController
@RequiredArgsConstructor
public class JobController extends BaseController {
    private final JobService jobService;

    @PostMapping("/jobs")
    public ResponseEntity<ResCreateJobDTO> create(@RequestBody Job job) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.handleCreate(job));
    }

    @PutMapping("/jobs")
    public ResponseEntity<ResUpdateJobDTO> update(@RequestBody Job job) throws IdInvalidException {
        return ResponseEntity.ok(jobService.handleUpdate(job));
    }

    @GetMapping("/jobs")
    public ResponseEntity<ResPaginationDTO> findAll(
            @Filter Specification<Job> spec, Pageable pageable) {
        return ResponseEntity.ok(jobService.fetchAllJob(spec, pageable));
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<Job> getById(@PathVariable(name = "id") long id) throws IdInvalidException {
        return ResponseEntity.ok(jobService.fetchJobById(id));
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") long id) {
        jobService.handleDelete(id);
        return ResponseEntity.ok(null);
    }
}
