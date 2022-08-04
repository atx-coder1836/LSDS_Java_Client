package org.brewster.prototype;

public class Loan {
    private Integer id;
    private String amount;
    private String rate;
    private String term;
    private String payment;

    public Loan(){
        this.id = 0;
    }

    public Loan(Integer id, String amount, String rate, String term, String payment ){
        this.id = id;
        this.amount = amount;
        this.rate = rate;
        this.term = term;
        this.payment = payment;
    }

    public Loan(String databaseRecord){
        String[] tokens = databaseRecord.split("\\|");
        setId(Integer.parseInt(tokens[0]));
        setAmount(tokens[1]);
        setRate(tokens[2]);
        setTerm(tokens[3]);
        setPayment(tokens[4]);
    }

    @Override
    public String toString(){
        return "Loan { id = " + getId().toString() + "," +
                "amount = " + getAmount() + "," +
                "rate = " + getRate() + "," +
                "term = " + getTerm() + "," +
                "payment = " + getPayment() + "}";
    }

    public String asDBRecord(){
        return String.join("|",getId().toString(),
                getAmount(),
                getRate(),
                getTerm(),
                getPayment());
    }
    public Integer getId() {
        return id;
    }

    public String getAmount() {
        return amount;
    }

    public String getRate() {
        return rate;
    }

    public String getTerm() {
        return term;
    }

    public String getPayment() {
        return payment;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
}
