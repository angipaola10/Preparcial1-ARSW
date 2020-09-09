package edu.eci.arsw.exams.moneylaunderingapi.model;

import java.util.concurrent.atomic.AtomicInteger;

public class SuspectAccount {
    public String accountId;
    public AtomicInteger amountOfSmallTransactions;
    
    public SuspectAccount(String accountId, int amountOfSmallTransactions){
        this.accountId = accountId;
        this.amountOfSmallTransactions = new AtomicInteger(amountOfSmallTransactions);
    }
    public String getAccountId(){
        return accountId;
    }
    
    public int getAmountOfSmallTransactions(){
        return amountOfSmallTransactions.get();
    }
    
    public void addTransactions(int newTransactions){
        amountOfSmallTransactions.getAndAdd(newTransactions);
    }
}
