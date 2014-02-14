/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pserver.pservlets.Implementations;

import com.mongodb.DB;
import pserver.pservlets.PService;
import pserver.pservlets.PServiceResult;
import pserver.util.Validator;
import pserver.util.VectorMap;

/**
 *
 * @author alexm
 */
public class Ster implements PService {

    @Override
    public void init(String[] params) throws Exception {
    }

    @Override
    public PServiceResult service(String clientName, VectorMap parameters, DB db) {
        Object obj = parameters.getVal((Object) "com");
        if (obj == null) {
            PServiceResult retault = new PServiceResult();
            retault.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
            retault.setErrorMessage("'com' parameter is missing");
            return retault;
        }
        PServiceResult resault = null;
        String com = (String) obj;

        if (com.equalsIgnoreCase("addstr")) {       //add stereotype
            resault = execSterAddStr(clientName, parameters, db);
        } else if (com.equalsIgnoreCase("lststr")) {  //list all stereotypes            
        } else if (com.equalsIgnoreCase("getstr")) {  //get feature values for a stereotype            
        } else if (com.equalsIgnoreCase("sqlstr")) {  //specify conditions and select stereotypes            
        } else if (com.equalsIgnoreCase("remstr")) {  //remove stereotype(s)            
        } else if (com.equalsIgnoreCase("addusr")) {  //assign user to stereotype(s)            
        } else if (com.equalsIgnoreCase("setdeg")) {  //update assignment degree            
        } else if (com.equalsIgnoreCase("incdeg")) {  //increment assignment degree
        } else if (com.equalsIgnoreCase("getusr")) {  //get assigned stereotypes for a user            
        } else if (com.equalsIgnoreCase("sqlusr")) {  //specify conditions and select assignments            
        } else if (com.equalsIgnoreCase("remusr")) {  //remove user assignments to stereotypes            
        } else if (com.equalsIgnoreCase("mkster")) {  //remove user assignments to stereotypes            
        } else if (com.equalsIgnoreCase("update")) {  //remove user assignments to stereotypes            
        } else if (com.equalsIgnoreCase("incval")) {
        } else {
            resault = new PServiceResult();
            resault.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
            resault.setErrorMessage("'com' parameter is invalid");
        }

        return resault;
    }

    private PServiceResult execSterAddStr(String clientName, VectorMap queryParam, DB db) {
        PServiceResult results = new PServiceResult();
        for (int i = 0; i < queryParam.size(); i++) {
            String key = (String) queryParam.getKey(i);
            if (key.equalsIgnoreCase("com") == true) {
                continue;
            } else if (key.equals("str") == true) {
                String stereotypeName = (String) queryParam.getVal(i);
            } else if (key.equals("rule") == true) {
                String rule = (String) queryParam.getVal(i);
                String subRules[] = rule.split(";");
                for (int j = 0; j < subRules.length; j++) {
                    String subRule = subRules[j];
                    String[] subRuleParts = subRule.split("$");
                    if (subRuleParts.length < 3 || subRuleParts.length % 2 != 1) {                        
                        results.setReturnCode(PServiceResult.STATUS_SYNTAX_ERROR);
                        results.setErrorMessage("'The 'rule' paraeter has not enouph data");
                        return results;
                    }
                }
                
            }
        }
        return results;
    }
}
