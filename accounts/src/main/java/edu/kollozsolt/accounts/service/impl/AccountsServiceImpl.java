package edu.kollozsolt.accounts.service.impl;

import edu.kollozsolt.accounts.constants.AccountsConstants;
import edu.kollozsolt.accounts.dto.AccountsDto;
import edu.kollozsolt.accounts.dto.AccountsMessageDto;
import edu.kollozsolt.accounts.dto.CustomerDto;
import edu.kollozsolt.accounts.entity.Accounts;
import edu.kollozsolt.accounts.entity.Customer;
import edu.kollozsolt.accounts.exception.CustomerAlreadyExistsException;
import edu.kollozsolt.accounts.exception.ResourceNotFoundException;
import edu.kollozsolt.accounts.mapper.AccountsMapper;
import edu.kollozsolt.accounts.mapper.CustomerMapper;
import edu.kollozsolt.accounts.repository.AccountsRepository;
import edu.kollozsolt.accounts.repository.CustomerRepository;
import edu.kollozsolt.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    private static final Logger logger = LoggerFactory.getLogger(AccountsServiceImpl.class);

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private final StreamBridge streamBridge;

    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());

        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber: " + customerDto.getMobileNumber());
        }

        Customer savedCustomers = customerRepository.save(customer);
        Accounts savedAccount =  accountsRepository.save(createNewAccount(savedCustomers));
        sendCommunication(savedAccount, savedCustomers);
    }

    private void sendCommunication(Accounts account, Customer customer) {
        var accountsMessageDto = new AccountsMessageDto(
                account.getAccountNumber(), customer.getName(), customer.getEmail(), customer.getMobileNumber());
        logger.info("Sending Communication request for the details: {}", accountsMessageDto);
        var result = streamBridge.send("sendCommunication-out-0", accountsMessageDto);
        logger.info("Is the Communication request successfully processed?: {}", result);
    }

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        return customerDto;
    }

    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();

        if (accountsDto != null) {
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "accountNumber", accountsDto.getAccountNumber().toString())
            );
            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "customer", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDto, customer);
            customerRepository.save(customer);
            isUpdated = true;
        }

        return isUpdated;
    }

    @Override
    public boolean deleteAccount(String mobilNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobilNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobilNumber)
        );

        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());

        return true;
    }

    @Override
    public boolean updateCommunicationStatus(Long accountNumber) {
        boolean isUpdated = false;
        if (accountNumber != null) {
            Accounts account = accountsRepository.findById(accountNumber).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountNumber.toString())
            );
            account.setCommunicationSw(true);
            accountsRepository.save(account);
            isUpdated = true;
        }

        return isUpdated;
    }

    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccountNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccountNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);

        return newAccount;
    }
}
