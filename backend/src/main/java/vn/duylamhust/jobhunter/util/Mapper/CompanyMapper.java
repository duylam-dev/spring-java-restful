package vn.duylamhust.jobhunter.util.Mapper;

import org.mapstruct.Mapper;

import vn.duylamhust.jobhunter.domain.Company;
import vn.duylamhust.jobhunter.domain.response.company.ResCompanyDTO;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    ResCompanyDTO toResCompanyDTO(Company company);
}
