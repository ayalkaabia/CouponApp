package com.example.project;


import java.sql.SQLException;
import java.util.ArrayList;

public interface CompaniesDAO {
    boolean isCompanyExists(String email,String Password) ;
    void addCompany(Company company) throws SQLException, DataExists;
    void updateCompany(Company company) throws DataNotExists;
    void deleteCompany(int companyId) throws DataNotExists;
    ArrayList<Company> getAllCompanies() ;
    Company getOneCompany(int companyId);

    Company isCompanyExists2(String email, String password);
}
