package com.simicart.core.base.networkcloud.request.error;

import com.simicart.core.config.Config;

/**
 * Created by MSI on 02/12/2015.
 */
public class SimiError {

    public interface Error {
        int NULL_ERROR = 0;
        int SOCKET_TIMEOUT_ERROR = 1;
        int MALFORMEDURL_ERROR = 2;
        int IOEXCEPTION_ERROR=3;
    }

    protected int statusCode;
    protected String message;
    protected String codeError;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCodeError() {
        return codeError;
    }

    public void setCodeError(String codeError) {
        this.codeError = codeError;
    }

    public SimiError() {
        message = Config.getInstance().getText(
                "Some errors occured. Please try again later");
        codeError = "fail";
    }
}
