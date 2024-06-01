package edu.kollozsolt.accounts.service;

import edu.kollozsolt.accounts.dto.CustomerDetailsDto;

public interface ICustomerService {

    CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId);
}
