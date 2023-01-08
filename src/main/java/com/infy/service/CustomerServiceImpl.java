package com.infy.service;

import com.infy.dto.AddressDTO;
import com.infy.dto.CustomerDTO;
import com.infy.entity.Address;
import com.infy.entity.Customer;
import com.infy.exception.InfyBankException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.infy.repository.CustomerRepository;

import java.util.Optional;

@Service(value = "customerService")
@Transactional
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public CustomerDTO getCustomer(Integer customerId) throws InfyBankException{
        Optional<Customer> optional = customerRepository.findById(customerId);
        Customer customer = optional.orElseThrow(() -> new InfyBankException("Service.INVALID_CUSTOMERID"));
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCustomerId(customer.getCustomerId());
        customerDTO.setName(customer.getName());
        customerDTO.setEmailId(customer.getEmailId());
        customerDTO.setDateOfBirth(customer.getDateOfBirth());

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setAddressId(customer.getAddress().getAddressId());
        addressDTO.setStreet(customer.getAddress().getStreet());
        addressDTO.setCity(customer.getAddress().getCity());
        customerDTO.setAddress(addressDTO);

        return customerDTO;
    }

    // adding addCustomer() method
    @Override
    public Integer addCustomer(CustomerDTO customerDTO){
        Customer customer = new Customer();
        customer.setCustomerId(customerDTO.getCustomerId());
        customer.setEmailId(customerDTO.getEmailId());
        customer.setName(customerDTO.getName());
        customer.setDateOfBirth(customerDTO.getDateOfBirth());

        Address address = new Address();
        address.setAddressId(customerDTO.getAddress().getAddressId());
        address.setStreet(customerDTO.getAddress().getStreet());
        address.setCity(customerDTO.getAddress().getCity());

        customer.setAddress(address);
        customerRepository.save(customer);
        return customer.getCustomerId();
    }

    // implementing an updateAddress() method
    @Override
    public void updateAddress(Integer customerId, AddressDTO addressDTO) throws InfyBankException{
        Optional<Customer>optional = customerRepository.findById(customerId);
        Customer customer = optional.orElseThrow(() -> new
                InfyBankException("Service.INVALID_CUSTOMERID"));
        Address address = customer.getAddress();
        address.setCity(addressDTO.getCity());
        address.setStreet(addressDTO.getStreet());
    }

    @Override
    public void deleteCustomer(Integer customerId) throws InfyBankException{
        Optional<Customer> optional = customerRepository.findById(customerId);
        Customer customer = optional.orElseThrow(() -> new
                InfyBankException("Service.INVALID.CUSTOMERID"));
        customerRepository.delete(customer);
    }

    @Override
    public void deleteCustomerOnly(Integer customerId) throws InfyBankException{
        Optional<Customer> optional = customerRepository.findById(customerId);
        Customer customer = optional.orElseThrow(() -> new
                InfyBankException("Service.INVALID_CUSTOMERID"));
        customer.setAddress(null);
        customerRepository.delete(customer);
    }


}
