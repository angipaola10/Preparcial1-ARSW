package edu.eci.arsw.moneylaundering;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MoneyLaundering implements Runnable
{
    private TransactionAnalyzer transactionAnalyzer;
    private TransactionReader transactionReader;
    private int amountOfFilesTotal;
    private AtomicInteger amountOfFilesProcessed;
    private final int NTHREADS = 5;
    private ConcurrentLinkedDeque<ProcessTransactionDataThread> threads;
    private boolean pause;

    public MoneyLaundering()
    {
        transactionAnalyzer = new TransactionAnalyzer();
        transactionReader = new TransactionReader();
        amountOfFilesProcessed = new AtomicInteger();
        threads = new ConcurrentLinkedDeque<>();
        pause = false;
        amountOfFilesTotal = -1;
    }

    public void processTransactionData()
    {
        amountOfFilesProcessed.set(0);
        List<File> transactionFiles = getTransactionFileList();
        amountOfFilesTotal = transactionFiles.size();
        int d = amountOfFilesTotal/NTHREADS;
        for(int i=0; i<NTHREADS; i++){
            if (i == NTHREADS-1){
                threads.addLast(new ProcessTransactionDataThread(transactionFiles, i*d, amountOfFilesTotal-1, 
                        transactionAnalyzer, amountOfFilesProcessed, transactionReader));
            }else{
                threads.addLast(new ProcessTransactionDataThread(transactionFiles, i*d, (i*d)+d-1, transactionAnalyzer, 
                        amountOfFilesProcessed, transactionReader));
            }
            threads.getLast().start();
        }
    }

    public List<String> getOffendingAccounts()
    {
        return transactionAnalyzer.listOffendingAccounts();
    }

    private List<File> getTransactionFileList()
    {
        List<File> csvFiles = new ArrayList<>();
        try (Stream<Path> csvFilePaths = Files.walk(Paths.get("src/main/resources/")).filter(path -> path.getFileName().toString().endsWith(".csv"))) {
            csvFiles = csvFilePaths.map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFiles;
    }

    public static void main(String[] args)
    {
        Thread moneyLaundering = new Thread(new MoneyLaundering());
        moneyLaundering.start();
    }
    
    private void pause(){
        System.out.println("----------------------------------PAUSE----------------------------------");
        pause = true;
        for (ProcessTransactionDataThread t: threads){
            t.pause();
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(MoneyLaundering.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void resumee(){
        System.out.println("----------------------------------RESUME----------------------------------");
        pause = false;
        for (ProcessTransactionDataThread t: threads){
            t.resumee();
        }
    }

    @Override
    public void run() {
        Thread processingThread = new Thread(() -> processTransactionData());
        processingThread.start();
        while(amountOfFilesTotal==-1 || amountOfFilesProcessed.get() < amountOfFilesTotal)
        {          
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if(line.contains("exit")){
                break;
            }else if(line.isEmpty()){
                if (pause){
                    resumee();
                }else{
                    pause();
                    showReport();
                }
            }else if (!pause && !line.isEmpty()){
                showReport();
            }
        }
    }
    
    private void showReport(){
        String message = "Processed %d out of %d files.\nFound %d suspect accounts:\n%s";
        List<String> offendingAccounts = getOffendingAccounts();
        String suspectAccounts = offendingAccounts.stream().reduce("", (s1, s2)-> s1 + "\n"+s2);
        message = String.format(message, amountOfFilesProcessed.get(), amountOfFilesTotal, offendingAccounts.size(), suspectAccounts);
        System.out.println(message);
    }


}
