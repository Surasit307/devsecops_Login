package com.ttb.fleet.login.account.service.impl;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.ttb.fleet.login.account.dto.AccountIn;
import com.ttb.fleet.login.account.dto.NewpasswordIn;
import com.ttb.fleet.login.account.service.AccountService;
import com.ttb.fleet.login.entity.Account;
import com.ttb.fleet.login.entity.Login;
import com.ttb.fleet.login.login.dto.LoginIn;
import com.ttb.fleet.login.login.dto.LogoutIn;
import com.ttb.fleet.login.repository.AccountRepository;
import com.ttb.fleet.login.repository.LoginRepository;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private LoginRepository loginRepository;
	private final Logger logger = LoggerFactory
			.getLogger(com.ttb.fleet.login.account.controller.AccountController.class);

    @Override //Create Account
    public Map<String,Object> create(AccountIn accountIn) throws Exception {
    	Account account = new Account();
        account.setFirstname(accountIn.getFirstname());
        account.setLastname(accountIn.getLastname());
        account.setGender(accountIn.getGender());
        account.setAddress(accountIn.getAddress());
        account.setUsername(accountIn.getUsername());
        account.setPassword(accountIn.getPassword());
        account.setEmail(accountIn.getEmail());
        account.setEmail_validation(null);
        account.setNewpassword(null);
        account.setDate(new Timestamp(System.currentTimeMillis()));
        
        accountRepository.save(account);
        String json = new Gson().toJson(account);
        Map<String,Object> result = new ObjectMapper().readValue(json, HashMap.class);
        return result;
    }
    
    @Override //Read Account
    public Map<String,Object> read(int accountId) throws Exception {
    	Account account = accountRepository.findAccountByAccountId(accountId);
    	String json = new Gson().toJson(account);
    	Map<String,Object> result = new ObjectMapper().readValue(json, HashMap.class);
    	return result;
    }
    
    @Override //Update Account
    public Map<String,Object> update(AccountIn accountIn) throws Exception {
    	Account account = accountRepository.findAccountByAccountId(accountIn.getAccountId());
        if(account == null){
            return null;
        }
        account.setAccountId(accountIn.getAccountId());
        account.setFirstname(accountIn.getFirstname());
        account.setLastname(accountIn.getLastname());
        account.setGender(accountIn.getGender());
        account.setAddress(accountIn.getAddress());
        account.setUsername(accountIn.getUsername());
        account.setPassword(accountIn.getPassword());
        account.setEmail(accountIn.getEmail());
        account.setDate(new Timestamp(System.currentTimeMillis()));
        accountRepository.save(account);
        String json = new Gson().toJson(account);
        Map<String,Object> result = new ObjectMapper().readValue(json, HashMap.class);
        return result;
    }

    @Override //Delete Account
    public Map<String,Object> delete(int accountId) throws Exception {
        Account account = accountRepository.findAccountByAccountId(accountId);
        accountRepository.delete(account);
        String json = new Gson().toJson(account);
        Map<String,Object> result = new ObjectMapper().readValue(json, HashMap.class);
        return result;
    }

    @Override //Login
    public Map<String,Object> userlogin(LoginIn loginIn) throws Exception {
    		Account account = accountRepository.findByUsername(loginIn.username);
    		Login login = loginRepository.findByUsername(loginIn.username);
    	if(loginIn.username.equals(account.username) && loginIn.password.equals(account.password)) {	
    	if(login != null) {
    	//Update login
    	login.setLoginId(login.getLoginId()); 
        login.setUsername(loginIn.username);
        login.setStatus_login("online");
        login.setToken(null);
        login.setDate(new Timestamp(System.currentTimeMillis()));
        loginRepository.save(login);
        String json = new Gson().toJson(login);
        Map<String,Object> result = new ObjectMapper().readValue(json, HashMap.class);
        return result;
    		} else {
    			//Insert
    			login = new Login();
                login.setUsername(loginIn.username);
                login.setStatus_login("online");
                login.setToken(null);
                login.setDate(new Timestamp(System.currentTimeMillis()));
                loginRepository.save(login);
                String json = new Gson().toJson(login);
                Map<String,Object> result = new ObjectMapper().readValue(json, HashMap.class);
                return result;
    		}
    	}
    	return null;
    }

    @Override //Logout
    public Map<String,Object> userlogout(LogoutIn logoutIn) throws Exception {
        //Query findBy Function
        Login login = loginRepository.findLoginByLoginId(logoutIn.getLoginId());
        if(login == null) {
        	return null;
        }
        login.setLoginId(logoutIn.getLoginId());
        login.setStatus_login("offline");
        login.setToken(null);
        login.setDate(new Timestamp(System.currentTimeMillis()));
        loginRepository.save(login);
        String json = new Gson().toJson(login);
        Map<String,Object> result = new ObjectMapper().readValue(json, HashMap.class);
        return result;
   
    }
    
    @Override //New password
    public Map<String,Object> newpass(NewpasswordIn newpasswordIn) throws Exception {
        //Query findBy Function
        Account account = accountRepository.findByUsername(newpasswordIn.username);
        if(account == null) {
        	return null;
        }
        account.setPassword(newpasswordIn.newpassword);
        account.setNewpassword(newpasswordIn.newpassword);
        accountRepository.save(account);
        String json = new Gson().toJson(account);
        Map<String,Object> result = new ObjectMapper().readValue(json, HashMap.class);
        return result;
   
    }
    
}
