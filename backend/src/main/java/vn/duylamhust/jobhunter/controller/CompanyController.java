package vn.duylamhust.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.duylamhust.jobhunter.domain.Company;
import vn.duylamhust.jobhunter.domain.response.ResPaginationDTO;
import vn.duylamhust.jobhunter.domain.response.company.ResCompanyDTO;
import vn.duylamhust.jobhunter.service.CompanyService;
import vn.duylamhust.jobhunter.util.anotation.ApiMessage;
import vn.duylamhust.jobhunter.util.error.IdInvalidException;

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

@RestController
public class CompanyController extends BaseController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    @ApiMessage("create company")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.handleCreate(company));
    }

    @GetMapping("/companies")
    @ApiMessage("find all company")
    public ResponseEntity<ResPaginationDTO> findAllCompany(
            @Filter Specification<Company> spec, Pageable pageable) {

        return ResponseEntity.ok(companyService.handleFindAll(spec, pageable));
    }

    @PutMapping("/companies")
    @ApiMessage("update company")
    public ResponseEntity<Company> updateCompany(@RequestBody Company company) {
        return ResponseEntity.ok(companyService.handleUpdate(company));
    }

    @GetMapping("/companies/{id}")
    @ApiMessage("fetch company by id")
    public ResponseEntity<ResCompanyDTO> findById(@PathVariable(name = "id") long id) throws IdInvalidException {
        return ResponseEntity.ok(companyService.fetchById(id));
    }

    @DeleteMapping("/companies/{id}")
    @ApiMessage("delete company")
    public ResponseEntity<Void> deleteCompany(@PathVariable(name = "id") long id) throws IdInvalidException {
        companyService.handleDelete(id);
        return ResponseEntity.ok(null);
    }

}
