package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingapi.model.MoneyLaunderingException;
import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface MoneyLaunderingService {
    void updateAccountStatus(SuspectAccount suspectAccount)throws MoneyLaunderingException;
    SuspectAccount getAccountStatus(String accountId) throws MoneyLaunderingException;
    List<SuspectAccount> getSuspectAccounts();
    public void addSuspectAccounts(SuspectAccount cuenta) throws MoneyLaunderingException;
}
