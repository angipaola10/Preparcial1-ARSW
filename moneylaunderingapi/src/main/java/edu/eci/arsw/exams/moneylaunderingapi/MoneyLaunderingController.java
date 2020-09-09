package edu.eci.arsw.exams.moneylaunderingapi;


import edu.eci.arsw.exams.moneylaunderingapi.model.MoneyLaunderingException;
import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import edu.eci.arsw.exams.moneylaunderingapi.service.MoneyLaunderingService;
import java.lang.reflect.MalformedParametersException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
@RequestMapping(value = "/fraud-bank-accounts")
public class MoneyLaunderingController
{
    @Autowired
    @Qualifier("MoneyLaunderingServiceStub")
    MoneyLaunderingService moneyLaunderingService;

    @RequestMapping( method = RequestMethod.GET)
     public ResponseEntity<?> getSuspectAccounts()
    {
        try {
            return new ResponseEntity<>(moneyLaunderingService.getSuspectAccounts(), HttpStatus.ACCEPTED);
        } catch (MalformedParametersException e) {
            Logger.getLogger(MoneyLaunderingController.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
     
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addSuspectAccounts(@RequestBody SuspectAccount account)
    {
        try {
            moneyLaunderingService.addSuspectAccounts(account);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (MoneyLaunderingException e) {
            Logger.getLogger(MoneyLaunderingController.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
    
    @RequestMapping(value="/{accountId}",method = RequestMethod.GET)
    public ResponseEntity<?> getAccountByID(@PathVariable String accountId)
    {
        try {
            return new ResponseEntity<>(moneyLaunderingService.getAccountStatus(accountId), HttpStatus.ACCEPTED);
        } catch (MoneyLaunderingException e) {
            Logger.getLogger(MoneyLaunderingController.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @RequestMapping(value="/{accountId}", method = RequestMethod.PUT)
    public ResponseEntity<?> actualizarCuenta(@RequestBody SuspectAccount cuenta) {
        try {
            moneyLaunderingService.updateAccountStatus(cuenta);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (MoneyLaunderingException ex) {
            Logger.getLogger(MoneyLaunderingController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
