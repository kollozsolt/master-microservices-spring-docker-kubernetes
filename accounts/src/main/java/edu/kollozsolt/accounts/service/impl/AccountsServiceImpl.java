package edu.kollozsolt.accounts.service.impl;

import edu.kollozsolt.accounts.constants.AccountsConstants;
import edu.kollozsolt.accounts.dto.CustomerDto;
import edu.kollozsolt.accounts.entity.Accounts;
import edu.kollozsolt.accounts.entity.Customer;
import edu.kollozsolt.accounts.exception.CustomerAlreadyExistsException;
import edu.kollozsolt.accounts.mapper.CustomerMapper;
import edu.kollozsolt.accounts.repository.AccountsRepository;
import edu.kollozsolt.accounts.repository.CustomerRepository;
import edu.kollozsolt.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());

        if(optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber: " + customerDto.getMobileNumber());
        }

        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("Anonymous");
        Customer savedCustomers = customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomers));
    }

    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccountNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccountNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setCreatedBy("Anonymous");

        return newAccount;
    }
}
