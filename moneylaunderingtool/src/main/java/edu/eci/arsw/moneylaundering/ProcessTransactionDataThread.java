/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.moneylaundering;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Angi
 */
public class ProcessTransactionDataThread extends Thread{
    
    private List<File> transactionFiles;
    private int a;
    private int b;
    private TransactionAnalyzer transactionAnalyzer;
    private AtomicInteger amountOfFilesProcessed;
    private TransactionReader transactionReader;
    private boolean pause;
    
    public ProcessTransactionDataThread(List<File> transactionFiles, int a, int b, TransactionAnalyzer transactionAnalyzer,
            AtomicInteger amountOfFilesProcessed, TransactionReader transactionReader){
        this.transactionFiles = transactionFiles;
        this.a = a;
        this.b = b;
        this.transactionAnalyzer = transactionAnalyzer;
        this.amountOfFilesProcessed = amountOfFilesProcessed;
        this.transactionReader = transactionReader;
        this.pause = false;
    }
    
    public void run(){
        for(File transactionFile : transactionFiles)
        {   synchronized(this){
                while(pause){
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ProcessTransactionDataThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }  
            List<Transaction> transactions = transactionReader.readTransactionsFromFile(transactionFile);
            for(Transaction transaction : transactions)
            {
                transactionAnalyzer.addTransaction(transaction);
            }
            amountOfFilesProcessed.incrementAndGet();
        }
    }
    
    public void pause(){
        pause = true;
    }
    
    public void resumee(){
        pause = false;
        synchronized(this){
            notifyAll();
        }
    }
}
