package edu.kollozsolt.loans.serivce;

import edu.kollozsolt.loans.dto.LoansDto;

public interface ILonasService {

    void createLoan(String mobileNumber);

    LoansDto fetchLoan(String mobileNumber);

    boolean updateLoan(LoansDto loansDto);

    boolean deleteLoan(String mobileNumber);
}
