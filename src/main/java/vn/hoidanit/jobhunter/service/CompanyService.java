package vn.hoidanit.jobhunter.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.ResPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {
    private CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleCreate(Company company) {
        return companyRepository.save(company);
    }

    public ResPaginationDTO handleFindAll(Specification<Company> spec, Pageable pageable) {
        Page<Company> companyPage = companyRepository.findAll(spec, pageable);
        var mt = new Meta();
        var rp = new ResPaginationDTO();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(companyPage.getTotalPages());
        mt.setTotal(companyPage.getTotalElements());

        rp.setMeta(mt);
        rp.setResult(companyPage.getContent());
        return rp;
    }

    public Company handleUpdate(Company reCompany) {
        var companyOld = companyRepository.findById(reCompany.getId()).orElse(null);
        if (companyOld != null) {
            companyOld.setName(reCompany.getName());
            companyOld.setDescription(reCompany.getDescription());
            companyOld.setAddress(reCompany.getAddress());
            companyOld.setLogo(reCompany.getLogo());
            return companyRepository.save(companyOld);
        }
        return null;
    }

    public void handleDelete(long id) {
        companyRepository.deleteById(id);
    }

}
