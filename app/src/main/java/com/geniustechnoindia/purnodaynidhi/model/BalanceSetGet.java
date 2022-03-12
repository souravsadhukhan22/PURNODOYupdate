package com.geniustechnoindia.purnodaynidhi.model;

import com.geniustechnoindia.purnodaynidhi.bean.ErrorData;

public class BalanceSetGet extends ErrorData {
    String balance;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
