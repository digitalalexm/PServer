/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pserver.pservlets.Implementations;

import pserver.util.VectorMap;
import com.mongodb.DB;
import java.util.ArrayList;
import pserver.data.FeatureAttributeDAO;
import pserver.domain.PAttribute;
import pserver.domain.PFeature;
import pserver.pservlets.PService;
import pserver.pservlets.PServiceResult;

/**
 *
 * @author alexm
 */
public class Pers implements PService {

    @Override
    public void init(String[] params) throws Exception {        
    }

    @Override
    public PServiceResult service(String clientName, VectorMap parameters, DB db) {        
        Object obj = parameters.getVal((Object)"com");
        if( obj == null ){
            PServiceResult retault = new PServiceResult();
            retault.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);            
            retault.setErrorMessage("'com' parameter is missing");
            return retault;
        }
        PServiceResult resault = null;        
        String com = (String)obj;
        if (com.equalsIgnoreCase("addftr")) {       //add new feature(s)            
            resault = execPersAddFtr(clientName, parameters, db);
        } else if (com.equalsIgnoreCase("addattr")) { //add new attribute(s)            
            resault = execPersAddAttr(clientName, parameters, db);
        } else if (com.equalsIgnoreCase("remftr")) {  //remove feature(s)            
        } else if (com.equalsIgnoreCase("remattr")) {  //remove attributes           
        } else if (com.equalsIgnoreCase("setdef") || com.equalsIgnoreCase("setftrdef")) {  //update the def value of ftr            
        } else if (com.equalsIgnoreCase("setattrdef")) {  //update the def value of ftr           
        } else if (com.equalsIgnoreCase("getdef") || com.equalsIgnoreCase("getftrdef")) {  //get ftr(s) AND def val(s)            
        } else if (com.equalsIgnoreCase("getattrdef")) {            
        } else if (com.equalsIgnoreCase("setusr")) {  //add AND update user            
        } else if (com.equalsIgnoreCase("incval")) {  //increment numeric values                             
        } else if (com.equalsIgnoreCase("getusrs")) {  //get feature values for a user            
        } else if (com.equalsIgnoreCase("getusr") || com.equalsIgnoreCase("getusrftr")) {  //get feature values for a user
        } else if (com.equalsIgnoreCase("getusrattr")) {  //get feature values for a user            
        } else if (com.equalsIgnoreCase("sqlusr") || com.equalsIgnoreCase("sqlftrusr")) {  //specify conditions AND select users            
        } else if (com.equalsIgnoreCase("sqlattrusr")) {            
        } else if (com.equalsIgnoreCase("remusr")) {  //remove user(s)            
        } else if (com.equalsIgnoreCase("setdcy")) {  //add AND update decay feature groups            
        } else if (com.equalsIgnoreCase("getdrt")) {  //get decay rate for a group            
        } else if (com.equalsIgnoreCase("remdcy")) {  //remove decay feature groups            
        } else if (com.equalsIgnoreCase("addddt")) {  //add new decay data            
        } else if (com.equalsIgnoreCase("sqlddt")) {  //retrieve decay data under conditions            
        } else if (com.equalsIgnoreCase("remddt")) {  //remove decay data            
        } else if (com.equalsIgnoreCase("caldcy")) {  //calculate decay values for a user            
        } else if (com.equalsIgnoreCase("addndt")) {  //add new numeric data            
        } else if (com.equalsIgnoreCase("sqlndt")) {  //retrieve numeric data under conditions            
        } else if (com.equalsIgnoreCase("remndt")) {  //remove numeric data            
        } else if (com.equalsIgnoreCase("getavg")) {  //calculate average values for a user            
        } else {   
            resault = new PServiceResult();
            resault.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);            
            resault.setErrorMessage("'com' parameter is invalid");            
        }

        return resault;
    }
    
    private PServiceResult execPersAddFtr( String clientName, VectorMap queryParam, DB db) {
        PServiceResult results = new PServiceResult();
        ArrayList< PFeature > updatePeatures = new ArrayList< PFeature >();
        for( int i = 0; i < queryParam.size(); i ++ ){
            PFeature updateFeature = new PFeature();
            String key = (String)queryParam.getKey(i);
            if( key.equals("com")) {
                continue;
            }
            updateFeature.setName(key);
            String strVal = (String)queryParam.getVal(i);            
            try{
                double val = Double.parseDouble(strVal);
                updateFeature.setDefValue(val);
                updateFeature.setValue(val);
                updatePeatures.add(updateFeature);
            }catch( NumberFormatException e) {                
                results.setReturnCode(PServiceResult.STATUS_SYNTAX_ERROR);
                results.setErrorMessage("Systax Error: A feature value parameter is not a number");
            }                  
            
        }        
        FeatureAttributeDAO.setFeatures(db, clientName, updatePeatures);
        results.setReturnCode(PServiceResult.STATUS_OK);
        return results;
    }
    
    private PServiceResult execPersAddAttr( String clientName, VectorMap queryParam, DB db) {
        PServiceResult results = new PServiceResult();
        ArrayList< PAttribute > updateAttributes = new ArrayList< PAttribute >();
        for( int i = 0; i < queryParam.size(); i ++ ){
            PAttribute updateAttribute = new PAttribute();
            String key = (String)queryParam.getKey(i);
            if( key.equals("com")) {
                continue;
            }
            updateAttribute.setName(key);
            String strVal = (String)queryParam.getVal(i);            
            updateAttribute.setDefValue(strVal);
            updateAttribute.setValue(strVal);
            updateAttributes.add(updateAttribute);            
            
        }        
        FeatureAttributeDAO.setAttributes(db, clientName, updateAttributes);
        results.setReturnCode(PServiceResult.STATUS_OK);
        return results;
    }
    
}
