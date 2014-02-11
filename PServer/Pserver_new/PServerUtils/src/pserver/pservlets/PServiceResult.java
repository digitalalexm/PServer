/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pserver.pservlets;

import java.util.ArrayList;

/**
 *
 * @author alexm
 */
public class PServiceResult {    
    public static final int STATUS_OK = 0;
    public static final int STATUS_DATABASE_ERROR = 1;
    public static final int STATUS_SYNTAX_ERROR = 2;
    public static final int STATUS_PARAMETER_ERROR = 3;
    
    private int returnCode;
    private String errorMessage;    
    private PThread pThread;
    private ArrayList<String> resultHeaders;
    private ArrayList<ArrayList<String>> result;
    
    public PServiceResult(){
        returnCode = STATUS_OK;
        pThread = null;
        errorMessage = null;
        result = null;
    }

    /**
     * @return the returnCode
     */
    public int getReturnCode() {
        return returnCode;
    }

    /**
     * @param returnCode the returnCode to set
     */
    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return the pThread
     */
    public PThread getpThread() {
        return pThread;
    }

    /**
     * @param pThread the pThread to set
     */
    public void setpThread(PThread pThread) {
        this.pThread = pThread;
    }

    /**
     * @return the result
     */
    public ArrayList<ArrayList<String>> getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResultData(ArrayList<ArrayList<String>> result) {
        this.result = result;
    }

    /**
     * @return the resultHeaders
     */
    public ArrayList<String> getResultHeaders() {
        return resultHeaders;
    }

    /**
     * @param resultHeaders the resultHeaders to set
     */
    public void setResultHeaders(ArrayList<String> resultHeaders) {
        this.resultHeaders = resultHeaders;
    }
    
}
