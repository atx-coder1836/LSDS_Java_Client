package org.brewster.prototype;

import java.util.ArrayList;
import java.util.Iterator;

public class ListDBSingleton {
    private static ListDBSingleton instance = new ListDBSingleton();
    private ArrayList<String> fauxDatabase;
    private Integer nextLoanID;

    // make constructor private
    private ListDBSingleton(){}

    public static ListDBSingleton getInstance() {
        if(instance.fauxDatabase == null) {
            instance.fauxDatabase = new ArrayList<String>();
            instance.nextLoanID = 100;
        }
        return instance;
    }

    public ArrayList<String> getFauxDatabase(){
        return instance.fauxDatabase;
    }
    public Integer getNextLoanID () {
        return instance.nextLoanID++;
    }

    public Loan addLoan(Loan aLoan){
        if(aLoan.getId() == 0)
            aLoan.setId(getNextLoanID());
        fauxDatabase.add(loanToRecord(aLoan));
        return aLoan;
    }

    public Loan findLoan(Loan aLoan){
        Iterator<String> iterator = this.fauxDatabase.iterator();

        while(iterator.hasNext()){
            String record = iterator.next();
            String[] tokens = record.split("\\|");
            if(Integer.parseInt(tokens[0]) == aLoan.getId())
                return recordToLoan(record);
        }
        return null;
    }

    public Loan updateLoan(Loan aLoan){
        Boolean found = false;
        String record;
        Iterator<String> iterator = this.fauxDatabase.iterator();
        while(iterator.hasNext() && !found) {
            record = iterator.next();
            String[] tokens = record.split("\\|");
            if (Integer.parseInt(tokens[0]) == aLoan.getId()) {
                found = true;
                int index = this.fauxDatabase.lastIndexOf(record);
                this.fauxDatabase.set(index, this.loanToRecord(aLoan));
            }
        }
        if (found)
            return aLoan;

        return null;
    }

    protected Loan recordToLoan(String record){
        return new Loan(record);
    }

    protected String loanToRecord(Loan aLoan){
        return aLoan.asDBRecord();
    }
}