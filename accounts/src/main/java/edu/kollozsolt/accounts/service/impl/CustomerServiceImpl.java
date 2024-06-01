package edu.kollozsolt.accounts.service.impl;

import edu.kollozsolt.accounts.dto.*;
import edu.kollozsolt.accounts.entity.Accounts;
import edu.kollozsolt.accounts.entity.Customer;
import edu.kollozsolt.accounts.exception.ResourceNotFoundException;
import edu.kollozsolt.accounts.mapper.AccountsMapper;
import edu.kollozsolt.accounts.mapper.CustomerMapper;
import edu.kollozsolt.accounts.repository.AccountsRepository;
import edu.kollozsolt.accounts.repository.CustomerRepository;
import edu.kollozsolt.accounts.service.ICustomerService;
import edu.kollozsolt.accounts.service.client.CardsFeignClient;
import edu.kollozsolt.accounts.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomerServiceImpl implements ICustomerService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;

    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );


        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(correlationId, mobileNumber);
        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(correlationId, mobileNumber);

        customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());
        customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());

        return customerDetailsDto;
    }
}
