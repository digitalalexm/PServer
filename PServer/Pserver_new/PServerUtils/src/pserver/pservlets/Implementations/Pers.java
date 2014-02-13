/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pserver.pservlets.Implementations;

import pserver.util.VectorMap;
import com.mongodb.DB;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import pserver.data.FeatureAttributeDAO;
import pserver.data.UserDAO;
import pserver.data.DecayDAO;
import pserver.domain.PAttribute;
import pserver.domain.PDecayData;
import pserver.domain.PFeature;
import pserver.domain.PNumData;
import pserver.pservlets.PService;
import pserver.pservlets.PServiceResult;
import pserver.util.CollectionManager;
import pserver.util.PEntry;
import pserver.util.Validator;

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
        Object obj = parameters.getVal((Object) "com");
        if (obj == null) {
            PServiceResult retault = new PServiceResult();
            retault.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
            retault.setErrorMessage("'com' parameter is missing");
            return retault;
        }
        PServiceResult resault = null;
        String com = (String) obj;
        if (com.equalsIgnoreCase("addattr")) { //add new attribute(s)            
            resault = execPersAddAttr(clientName, parameters, db);
        } else if (com.equalsIgnoreCase("getattrdef")) {
            resault = execPersGetAttrDef(clientName, parameters, db);
        } else if (com.equalsIgnoreCase("remattr")) {  //remove attributes
        } else if (com.equalsIgnoreCase("addftr")) {       //add new feature(s)            
            resault = execPersAddFtr(clientName, parameters, db);
        } else if (com.equalsIgnoreCase("getdef") || com.equalsIgnoreCase("getftrdef")) {  //get ftr(s) AND def val(s)                    
            resault = execPersGetFtrDef(clientName, parameters, db);
        } else if (com.equalsIgnoreCase("remftr")) {  //remove feature(s)                    
        } else if (com.equalsIgnoreCase("setusr")) {  //add AND update user            
            resault = execPersSetUsr(clientName, parameters, db);
        } else if (com.equalsIgnoreCase("incval")) {  //increment numeric values                             
        } else if (com.equalsIgnoreCase("getusrs")) {  //get the names of the users that are installed in the database
            resault = execPersGetUsrs(clientName, parameters, db);
        } else if (com.equalsIgnoreCase("getusr") || com.equalsIgnoreCase("getusrftr")) {  //get feature values for a user
            resault = execPersGetUsrProfile(clientName, parameters, db);
        } else if (com.equalsIgnoreCase("getusrattr")) {  //get feature values for a user            
            resault = execPersGetUsrAttributes(clientName, parameters, db);
        } else if (com.equalsIgnoreCase("addddt")) {  //add new decay data
            resault = execPersAddDDT(clientName, parameters, db);
        } else if (com.equalsIgnoreCase("addndt")) {  //add new numeric data                 
            resault = execPersAddNDT(clientName, parameters, db);
        } else if (com.equalsIgnoreCase("remddt")) {  //remove decay data
        } else if (com.equalsIgnoreCase("sqlusr") || com.equalsIgnoreCase("sqlftrusr")) {  //specify conditions AND select users                    
        } else if (com.equalsIgnoreCase("remusr")) {  //remove user(s)            
        } else if (com.equalsIgnoreCase("setdcy")) {  //add AND update decay feature groups            
        } else if (com.equalsIgnoreCase("getdrt")) {  //get decay rate for a group            
        } else if (com.equalsIgnoreCase("remdcy")) {  //remove decay feature groups                    
        } else if (com.equalsIgnoreCase("sqlddt")) {  //retrieve decay data under conditions                    
        } else if (com.equalsIgnoreCase("caldcy")) {  //calculate decay values for a user                    
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

    private PServiceResult execPersAddAttr(String clientName, VectorMap queryParam, DB db) {
        PServiceResult results = new PServiceResult();
        ArrayList< PAttribute> updateAttributes = new ArrayList<>();
        for (int i = 0; i < queryParam.size(); i++) {
            PAttribute updateAttribute = new PAttribute();
            String key = (String) queryParam.getKey(i);
            if (key.equalsIgnoreCase("com")) {
                continue;
            }
            if (Validator.isAValidAttributeName(key) == false) {
                results.setReturnCode(PServiceResult.STATUS_SYNTAX_ERROR);
                results.setErrorMessage("Attribute names must not contain " + Validator.getAttributeInvalidCharachters());
                return results;
            }
            String val = (String) queryParam.getVal(i);
            String[] parts = val.split(":");
            if (parts.length != 2) {
                results.setReturnCode(PServiceResult.STATUS_SYNTAX_ERROR);
                results.setErrorMessage("After the attribute " + key + " there is not a pair of attribute type and value separeted with ':'");
                return results;
            }
            if (parts[0].equalsIgnoreCase("num") == true) {
                updateAttribute.setType(PAttribute.ATTRIBUTE_TYPE.TYPE_NUMBER);
                if (Validator.isReal(parts[1]) == false) {
                    results.setReturnCode(PServiceResult.STATUS_SYNTAX_ERROR);
                    results.setErrorMessage("The type of attribute " + key + " is a number but the default value is not");
                    return results;
                }
            } else if (parts[0].equalsIgnoreCase("str") == true) {
                updateAttribute.setType(PAttribute.ATTRIBUTE_TYPE.TYPE_STRING);
            } else {
                results.setReturnCode(PServiceResult.STATUS_SYNTAX_ERROR);
                results.setErrorMessage("The type of attribute " + key + " is neither 'num' nor 'int'");
                return results;
            }
            updateAttribute.setName(key);
            updateAttribute.setDefValue(parts[1]);
            updateAttribute.setValue(parts[1]);
            updateAttributes.add(updateAttribute);
        }
        FeatureAttributeDAO.setAttributes(db, clientName, updateAttributes);
        results.setReturnCode(PServiceResult.STATUS_OK);
        return results;
    }

    private PServiceResult execPersGetAttrDef(String clientName, VectorMap queryParam, DB db) {
        PServiceResult results = new PServiceResult();
        ArrayList< String> resultHeaders = new ArrayList<>();
        resultHeaders.add(("Attribute Name"));
        resultHeaders.add(("Default Value"));
        ArrayList<ArrayList<String>> defValues = new ArrayList<>();
        for (int i = 0; i < queryParam.size(); i++) {
            String key = (String) queryParam.getKey(i);
            if (key.equalsIgnoreCase("com")) {
                continue;
            } else if (key.equalsIgnoreCase("attr")) {
                String query = (String) queryParam.getVal(i);
                if (query.contains("*") == false) {
                    ArrayList<String> defValue = new ArrayList<>();
                    PAttribute value = FeatureAttributeDAO.getAttributeDefValue(db, clientName, query);
                    if (value == null) {
                        continue;
                    }
                    defValue.add(query);
                    if (value.getType() == PAttribute.ATTRIBUTE_TYPE.TYPE_NUMBER) {
                        defValue.add("num");
                    } else {
                        defValue.add("str");
                    }
                    defValue.add(value.getDefValue());
                    defValues.add(defValue);
                } else {
                    ArrayList<PAttribute> values = FeatureAttributeDAO.getAttributeDefValues(db, clientName, query);
                    if (values == null) {
                        continue;
                    }
                    for (PAttribute value : values) {
                        ArrayList<String> tmpArray = new ArrayList<>();
                        tmpArray.add(value.getName());
                        if (value.getType() == PAttribute.ATTRIBUTE_TYPE.TYPE_NUMBER) {
                            tmpArray.add("num");
                        } else {
                            tmpArray.add("str");
                        }
                        tmpArray.add(value.getValue());
                        defValues.add(tmpArray);
                    }
                }
            } else {
                results.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
                results.setErrorMessage("The key " + key + " is not a valid parameter");
                return results;
            }

        }
        results.setResultHeaders(resultHeaders);
        results.setResultData(defValues);
        return results;
    }

    private PServiceResult execPersAddFtr(String clientName, VectorMap queryParam, DB db) {
        PServiceResult results = new PServiceResult();
        ArrayList< PFeature> updatePeatures = new ArrayList< PFeature>();
        for (int i = 0; i < queryParam.size(); i++) {
            PFeature updateFeature = new PFeature();
            String key = (String) queryParam.getKey(i);
            if (key.equalsIgnoreCase("com")) {
                continue;
            }
            updateFeature.setName(key);
            String strVal = (String) queryParam.getVal(i);
            try {
                double val = Double.parseDouble(strVal);
                updateFeature.setDefValue(val);
                updateFeature.setValue(val);
                updatePeatures.add(updateFeature);
            } catch (NumberFormatException e) {
                results.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
                results.setErrorMessage("A feature value parameter is not a number");
                return results;
            }

        }
        FeatureAttributeDAO.setFeatures(db, clientName, updatePeatures);
        results.setReturnCode(PServiceResult.STATUS_OK);
        return results;
    }

    private PServiceResult execPersGetFtrDef(String clientName, VectorMap queryParam, DB db) {
        PServiceResult results = new PServiceResult();
        ArrayList< String> resultHeaders = new ArrayList< String>();
        resultHeaders.add(("Feature Name"));
        resultHeaders.add(("Default Value"));
        ArrayList<ArrayList<String>> defValues = new ArrayList< ArrayList<String>>();
        for (int i = 0; i < queryParam.size(); i++) {
            String key = (String) queryParam.getKey(i);
            if (key.equalsIgnoreCase("com")) {
                continue;
            } else if (key.equalsIgnoreCase("ftr")) {
                String query = (String) queryParam.getVal(i);
                if (query.contains("*") == false) {
                    ArrayList<String> defValue = new ArrayList<String>();
                    Double dValue = FeatureAttributeDAO.getFeatureDefValue(db, clientName, query);
                    String value = (dValue != null ? "" + dValue : null);
                    if (value == null) {
                        continue;
                    }
                    defValue.add(query);
                    defValue.add(value);
                    defValues.add(defValue);
                } else {
                    ArrayList<PEntry<String, Double>> values = FeatureAttributeDAO.getFeatureDefValues(db, clientName, query);
                    if (values == null) {
                        continue;
                    }
                    for (PEntry<String, Double> value : values) {
                        ArrayList<String> tmpArray = new ArrayList<String>();
                        tmpArray.add(value.getKey());
                        tmpArray.add(value.getValue() + "");
                        defValues.add(tmpArray);
                    }
                }
            } else {
                results.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
                results.setErrorMessage("The key " + key + " is not a valid parameter");
                return results;
            }

        }
        results.setResultHeaders(resultHeaders);
        results.setResultData(defValues);
        return results;
    }

    private PServiceResult execPersSetUsr(String clientName, VectorMap queryParam, DB db) {
        PServiceResult results = new PServiceResult();
        String userName = null;
        ArrayList<PAttribute> attributes = new ArrayList<PAttribute>();
        ArrayList<PFeature> features = new ArrayList<PFeature>();
        for (int i = 0; i < queryParam.size(); i++) {
            String key = (String) queryParam.getKey(i);
            if (key.equalsIgnoreCase("com")) {
                continue;
            } else if (key.equalsIgnoreCase("usr")) {
                userName = (String) queryParam.getVal(i);
            } else if (key.startsWith("attr_")) {
                PAttribute attr = new PAttribute();
                key = key.substring(5);
                String sValue = (String) queryParam.getVal(i);
                attr.setValue(sValue);
                if (key.contains("*") == false) {
                    PAttribute tmpSValue = FeatureAttributeDAO.getAttributeDefValue(db, clientName, key);
                    if (tmpSValue == null) {
                        results.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
                        results.setErrorMessage("The attribute " + key + " does not exists");
                        return results;
                    } else {
                        attr.setName(key);
                        attr.setType(tmpSValue.getType());
                        if( tmpSValue.getType() == PAttribute.ATTRIBUTE_TYPE.TYPE_NUMBER  == true && Validator.isReal(attr.getValue()) == false) {
                            results.setReturnCode(PServiceResult.STATUS_SYNTAX_ERROR);
                            results.setErrorMessage("The attribute " + key + " get only number values");
                            return results;
                        }
                        attr.setDefValue(tmpSValue.getDefValue());
                    }
                    attributes.add(attr);
                } else {
                    if (key.trim().equals("*") == true) {
                        key = "";
                    }
                    ArrayList<PAttribute> attributesEntries = FeatureAttributeDAO.getAttributeDefValues(db, clientName, key);
                    if (attributesEntries.isEmpty()) {
                        results.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
                        results.setErrorMessage("The attribute pattern " + key + " returns no attributes");
                        return results;
                    }
                    for (PAttribute value : attributesEntries) {
                        PAttribute attrToAdd = new PAttribute();
                        attrToAdd.setName(value.getName());
                        if( attrToAdd.getType() == PAttribute.ATTRIBUTE_TYPE.TYPE_NUMBER  == true && Validator.isReal(attr.getValue()) == false) {
                            results.setReturnCode(PServiceResult.STATUS_SYNTAX_ERROR);
                            results.setErrorMessage("The attribute " + attrToAdd.getName() + " get only number values");
                            return results;
                        }
                        attrToAdd.setValue(attr.getValue());
                        attrToAdd.setType(value.getType());
                        attrToAdd.setDefValue(value.getValue());
                        attributes.add(attrToAdd);
                    }
                }
            } else {
                PFeature ftr = new PFeature();
                if (key.startsWith("ftr_")) {
                    key = key.substring(4);
                }
                try {
                    Double dValue = Double.parseDouble((String) queryParam.getVal(i));
                    ftr.setValue(dValue);
                } catch (NumberFormatException e) {
                    results.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
                    results.setErrorMessage("The feature " + key + " is set to have a value that is not a number");
                    return results;
                }
                if (key.contains("*") == false) {
                    if (key.trim().equals("*") == true) {
                        key = "";
                    }
                    Double dValue = FeatureAttributeDAO.getFeatureDefValue(db, clientName, key);
                    if (dValue == null) {
                        results.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
                        results.setErrorMessage("The feature " + key + " does not exists");
                        return results;
                    } else {
                        ftr.setName(key);
                        ftr.setDefValue(dValue);
                    }
                    features.add(ftr);
                } else {
                    ArrayList<PEntry<String, Double>> featureEntries = FeatureAttributeDAO.getFeatureDefValues(db, clientName, key);
                    if (featureEntries.isEmpty()) {
                        results.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
                        results.setErrorMessage("The feature pattern " + key + " returns no features");
                        return results;
                    }
                    for (PEntry<String, Double> value : featureEntries) {
                        PFeature ftrToAdd = new PFeature();
                        ftrToAdd.setName(value.getKey());
                        ftrToAdd.setValue(ftr.getValue());
                        ftrToAdd.setDefValue(value.getValue());
                        features.add(ftrToAdd);
                    }
                }
            }
        }
        if (userName == null) {
            results.setReturnCode(PServiceResult.STATUS_SYNTAX_ERROR);
            results.setErrorMessage("There is no 'usr' parameter");
            return results;
        }
        CollectionManager.cleanAttributesCollection(attributes);
        CollectionManager.cleanFeatureCollection(features);
        UserDAO.updateUserAttributes(db, clientName, userName, attributes);
        UserDAO.updateUserProfile(db, clientName, userName, features, false);
        return results;
    }

    private PServiceResult execPersGetUsrs(String clientName, VectorMap queryParam, DB db) {
        PServiceResult results = new PServiceResult();
        String userName = "";
        for (int i = 0; i < queryParam.size(); i++) {
            String key = (String) queryParam.getKey(i);
            if (key.equalsIgnoreCase("com") == true) {
                continue;
            } else if (key.equalsIgnoreCase("whr") == true) {
                userName = (String) queryParam.getVal(i);
            } else {
                results.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
                results.setErrorMessage("The key " + key + " is not a valid parameter");
                return results;
            }
        }
        if (userName.trim().equals("*") == true) {
            userName = "";
        }
        List<String> userNames = UserDAO.getUsers(db, clientName, userName);
        ArrayList<ArrayList<String>> resultRedords = new ArrayList<>();
        for (String uName : userNames) {
            ArrayList<String> newArray = new ArrayList<>();
            newArray.add(uName);
            resultRedords.add(newArray);
        }
        ArrayList<String> headers = new ArrayList<>();
        headers.add("User Name");
        results.setResultHeaders(headers);
        results.setResultData(resultRedords);
        return results;
    }

    private PServiceResult execPersGetUsrProfile(String clientName, VectorMap queryParam, DB db) {
        PServiceResult results = new PServiceResult();
        String userName = null;
        String ftr = "";
        Integer num = null;
        boolean sortAsc = true;
        for (int i = 0; i < queryParam.size(); i++) {
            String key = (String) queryParam.getKey(i);
            if (key.equalsIgnoreCase("com") == true) {
                continue;
            } else if (key.equalsIgnoreCase("usr") == true) {
                userName = (String) queryParam.getVal(i);
            } else if (key.equalsIgnoreCase("ftr") == true) {
                ftr = (String) queryParam.getVal(i);
                if (key.trim().equals("*") == true) {
                    key = "";
                }
            } else if (key.equalsIgnoreCase("num") == true) {
                try {
                    num = Integer.parseInt((String) queryParam.getVal(i));
                } catch (NumberFormatException e) {
                    results.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
                    results.setErrorMessage("The 'num' value parameter is not an integer");
                    return results;
                }
            } else if (key.equalsIgnoreCase("srt") == true) {
                String srtParam = (String) queryParam.getVal(i);
                if (srtParam.equalsIgnoreCase("asc") == true) {
                    sortAsc = true;
                } else if (srtParam.equalsIgnoreCase("desc") == true) {
                    sortAsc = false;
                } else {
                    results.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
                    results.setErrorMessage("The 'srt' parameter takes only the values 'asc' and 'desc'");
                    return results;
                }
            } else {
                results.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
                results.setErrorMessage("The key " + key + " is not a valid parameter");
                return results;
            }
        }
        if (userName == null) {
            results.setReturnCode(PServiceResult.STATUS_SYNTAX_ERROR);
            results.setErrorMessage("There is no 'usr' parameter");
            return results;
        }
        if (num == null) {
            num = 0;
        }
        List<PFeature> profile = UserDAO.getUserProfile(db, clientName, userName, ftr, num, sortAsc);
        ArrayList<ArrayList<String>> resultData = new ArrayList<>();
        for (PFeature ftrp : profile) {
            ArrayList<String> row = new ArrayList<>(2);
            row.add(ftrp.getName());
            row.add(ftrp.getValue() + "");
            resultData.add(row);
        }
        results.setResultData(resultData);
        ArrayList<String> header = new ArrayList<>();
        header.add("Feature Name");
        header.add("Feature Value");
        results.setResultHeaders(header);
        return results;
    }

    private PServiceResult execPersGetUsrAttributes(String clientName, VectorMap queryParam, DB db) {
        PServiceResult results = new PServiceResult();
        String userName = null;
        String attr = "";
        for (int i = 0; i < queryParam.size(); i++) {
            String key = (String) queryParam.getKey(i);
            if (key.equalsIgnoreCase("com") == true) {
                continue;
            } else if (key.equalsIgnoreCase("usr") == true) {
                userName = (String) queryParam.getVal(i);
            } else if (key.equalsIgnoreCase("attr") == true) {
                attr = (String) queryParam.getVal(i);
            } else {
                results.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
                results.setErrorMessage("The key " + key + " is not a valid parameter");
                return results;
            }
        }
        if (userName == null) {
            results.setReturnCode(PServiceResult.STATUS_SYNTAX_ERROR);
            results.setErrorMessage("There is no 'usr' parameter");
            return results;
        }
        if (attr.equals("*") == true) {
            attr = "";
        }
        List<PAttribute> attributes = UserDAO.getUserAttributes(db, clientName, userName, attr);
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        for (PAttribute pattr : attributes) {
            ArrayList<String> row = new ArrayList<>();
            row.add(pattr.getName());
            row.add(pattr.getValue());
            data.add(row);
        }
        results.setResultData(data);
        ArrayList<String> header = new ArrayList<>();
        header.add("Attribute Name");
        header.add("Attribute Value");
        results.setResultHeaders(header);
        return results;
    }

    private PServiceResult execPersAddDDT(String clientName, VectorMap queryParam, DB db) {
        PServiceResult results = new PServiceResult();
        String userName = null;
        Long sessionId = null;
        long defaultTimestamp = System.currentTimeMillis();
        LinkedList<PDecayData> decayData = new LinkedList<>();
        for (int i = 0; i < queryParam.size(); i++) {
            String key = (String) queryParam.getKey(i);
            if (key.equalsIgnoreCase("com") == true) {
                continue;
            } else if (key.equalsIgnoreCase("ddt") == true) {
                String value = (String) queryParam.getVal(i);
                String values[] = value.split(":");
                String ftr = values[0];
                if (FeatureAttributeDAO.isAValicFeature(db, clientName, ftr) == false) {
                    results.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
                    results.setErrorMessage("An 'ndt' parameter contains a feature name that does not exists");
                    return results;
                }
                PDecayData ddt = new PDecayData();
                ddt.setFeature(ftr);
                if (values.length == 2) {
                    try {
                        long timeStamp = Long.parseLong(values[1]);
                        ddt.setTimeStamp(timeStamp);
                    } catch (NumberFormatException e) {
                        results.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
                        results.setErrorMessage("A timestamp parameter is not a long integer");
                        return results;
                    }
                } else {
                    ddt.setTimeStamp(defaultTimestamp);
                }
                decayData.add(ddt);
            } else if (key.equalsIgnoreCase("usr") == true) {
                userName = (String) queryParam.getVal(i);
                if (UserDAO.isAValidUser(db, clientName, userName) == false) {
                    results.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
                    results.setErrorMessage("The 'usr' parameter is not a valid user");
                    return results;
                }
            } else if (key.equalsIgnoreCase("sid") == true) {
                String value = (String) queryParam.getVal(i);
                try {
                    sessionId = Long.parseLong(value);
                } catch (NumberFormatException e) {
                    results.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
                    results.setErrorMessage("The 'sid' parameter is not an integer");
                    return results;
                }
            } else {
                results.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
                results.setErrorMessage("The key " + key + " is not a valid parameter");
                return results;
            }
            if (userName == null) {
                results.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
                results.setErrorMessage("There is no 'usr' parameter");
                return results;
            }
            if (sessionId == null) {
                sessionId = DecayDAO.getLastDDTSession(db, clientName, userName);
            }
            for (PDecayData ddt : decayData) {
                ddt.setUserName(userName);
                ddt.setSessionId(sessionId);
            }
            DecayDAO.addDecayData(db, clientName, userName, decayData);
        }
        return results;
    }

    private PServiceResult execPersAddNDT(String clientName, VectorMap queryParam, DB db) {
        PServiceResult results = new PServiceResult();
        String userName = null;
        Long sessionId = null;
        long defaultTimestamp = System.currentTimeMillis();
        LinkedList<PNumData> numData = new LinkedList<>();
        for (int i = 0; i < queryParam.size(); i++) {
            String key = (String) queryParam.getKey(i);
            if (key.equalsIgnoreCase("com") == true) {
                continue;
            } else if (key.equalsIgnoreCase("ndt") == true) {
                String value = (String) queryParam.getVal(i);
                String values[] = value.split(":");
                String ftr = values[0];
                if (FeatureAttributeDAO.isAValicFeature(db, clientName, ftr) == false) {
                    results.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
                    results.setErrorMessage("An 'ndt' parameter contains a feature name that does not exists");
                    return results;
                }
                PNumData nd = new PNumData();
                nd.setFeature(ftr);
                if (values.length == 3) {
                    try {
                        double dValue = Double.parseDouble(values[1]);
                        nd.setFeatureValue(dValue);
                    } catch (NumberFormatException e) {
                        results.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
                        results.setErrorMessage("An 'ndt' parameter has a feature value is not a real integer");
                        return results;
                    }
                    try {
                        long timeStamp = Long.parseLong(values[2]);
                        nd.setTimeStamp(timeStamp);
                    } catch (NumberFormatException e) {
                        results.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
                        results.setErrorMessage("A timestamp parameter is not a long integer");
                        return results;
                    }
                } else if (values.length == 2) {
                    try {
                        double dValue = Double.parseDouble(values[1]);
                        nd.setFeatureValue(dValue);
                    } catch (NumberFormatException e) {
                        results.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
                        results.setErrorMessage("A ndt parameter has a feature value is not a real integer");
                        return results;
                    }
                    nd.setTimeStamp(defaultTimestamp);
                } else {
                    results.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
                    results.setErrorMessage("A ndt parameter has no feature value");
                    return results;
                }
                numData.add(nd);
            } else if (key.equalsIgnoreCase("usr") == true) {
                userName = (String) queryParam.getVal(i);
                if (UserDAO.isAValidUser(db, clientName, userName) == false) {
                    results.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
                    results.setErrorMessage("The 'usr' parameter is not a valid user");
                    return results;
                }
            } else if (key.equalsIgnoreCase("sid") == true) {
                String value = (String) queryParam.getVal(i);
                try {
                    sessionId = Long.parseLong(value);
                } catch (NumberFormatException e) {
                    results.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
                    results.setErrorMessage("The 'sid' parameter is not an integer");
                    return results;
                }
            } else {
                results.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
                results.setErrorMessage("The key " + key + " is not a valid parameter");
                return results;
            }
            if (userName == null) {
                results.setReturnCode(PServiceResult.STATUS_PARAMETER_ERROR);
                results.setErrorMessage("There is no 'usr' parameter");
                return results;
            }
            if (sessionId == null) {
                sessionId = DecayDAO.getLastNumDataSession(db, clientName, userName);
            }
            for (PNumData nd : numData) {
                nd.setUser(userName);
                nd.setSessionId(sessionId);
            }
            DecayDAO.addNumData(db, clientName, userName, numData);
        }
        return results;
    }
}
