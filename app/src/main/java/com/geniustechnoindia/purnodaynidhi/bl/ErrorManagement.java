package com.geniustechnoindia.purnodaynidhi.bl;

import com.geniustechnoindia.purnodaynidhi.bean.ErrorData;

/**
 * Created by workstation1 on 11/8/2017.
 */

public class ErrorManagement<T extends ErrorData> {
    Class<T> rClass;
    public ErrorManagement(Class<T> refClass){
        this.rClass = refClass;
    }
    public T setErrorCode(int code, String name) throws IllegalAccessException, InstantiationException {
        T cl = rClass.newInstance();
        cl.setErrorCode(code);
        cl.setErrorString(name);
        return cl;
    }
}
