package vn.hoidanit.jobhunter.service;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.response.ResPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.company.ResCompanyDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.Mapper.CompanyMapper;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final CompanyMapper companyMapper;

    public Company handleCreate(Company company) {
        return companyRepository.save(company);
    }

    public ResPaginationDTO handleFindAll(Specification<Company> spec, Pageable pageable) {
        Page<Company> companyPage = companyRepository.findAll(spec, pageable);
        var rp = new ResPaginationDTO();
        var mt = new ResPaginationDTO.Meta(
                pageable.getPageNumber() + 1,
                pageable.getPageSize(),
                companyPage.getTotalPages(),
                companyPage.getTotalElements());
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

    public void handleDelete(long id) throws IdInvalidException {
        var company = companyRepository.findById(id).orElseThrow(() -> new IdInvalidException("company is not exist"));
        userRepository.deleteAll(company.getUsers());
        companyRepository.deleteById(id);
    }

    public ResCompanyDTO fetchById(long id) throws IdInvalidException {
        var companyDb = companyRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("company is not exist"));
        return companyMapper.toResCompanyDTO(companyDb);
    }

}
