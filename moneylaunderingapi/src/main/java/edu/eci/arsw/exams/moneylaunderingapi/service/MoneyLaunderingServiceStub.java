package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingapi.model.MoneyLaunderingException;
import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Service;

@Service("MoneyLaunderingServiceStub")
public class MoneyLaunderingServiceStub implements MoneyLaunderingService {
    
    private List<SuspectAccount> suspectAccounts = new CopyOnWriteArrayList<>();

    public MoneyLaunderingServiceStub() {
        suspectAccounts.add(new SuspectAccount("1", 20));
        suspectAccounts.add(new SuspectAccount("2", 25));
        suspectAccounts.add(new SuspectAccount("3", 30));
    }
    
    @Override
    public void updateAccountStatus(SuspectAccount suspectAccount)throws MoneyLaunderingException{
        this.getAccountStatus(suspectAccount.getAccountId()).addTransactions(suspectAccount.getAmountOfSmallTransactions());
    }

    @Override
    public SuspectAccount getAccountStatus(String accountId)throws MoneyLaunderingException{
        for (SuspectAccount sa: suspectAccounts){
            if (sa.getAccountId().equals(accountId)){
                return sa;
            }
        }
        throw new MoneyLaunderingException("Account not found");
    }

    @Override
    public List<SuspectAccount> getSuspectAccounts(){
        return suspectAccounts;
    }

    @Override
    public synchronized void addSuspectAccounts(SuspectAccount account) throws MoneyLaunderingException{
        for (SuspectAccount sa: suspectAccounts){
            if (sa.getAccountId().equals(account.getAccountId())){
                throw new MoneyLaunderingException("This account already exists");
            }
        }
        suspectAccounts.add(account);
    }
}
