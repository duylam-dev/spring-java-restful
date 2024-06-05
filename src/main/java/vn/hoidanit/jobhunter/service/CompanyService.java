package vn.hoidanit.jobhunter.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
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

    public List<Company> handleFindAll() {
        return companyRepository.findAll();
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
