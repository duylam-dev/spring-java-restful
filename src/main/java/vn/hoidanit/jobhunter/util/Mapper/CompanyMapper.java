package vn.hoidanit.jobhunter.util.Mapper;

import org.mapstruct.Mapper;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.response.ResCompanyDTO;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    ResCompanyDTO toResCompanyDTO(Company company);
}
