/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pservertester;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import pserver.data.FeatureAttributeDAO;
import pserver.data.UserDAO;
import pserver.domain.PFeature;
import pserver.domain.PAttribute;
import pserver.domain.PUser;
import pserver.parameters.Parameters;
import pserver.pservlets.Implementations.Pers;
import pserver.pservlets.Implementations.Ster;
import pserver.pservlets.PServiceResult;
import pserver.util.VectorMap;

/**
 *
 * @author alexm
 */
public class PServerTester {

    private static String clientname = "some_client";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnknownHostException {        
        //testPersAddAttr();
        //testpers();
        //testAttr();
        //testsetUser();
        //testgetUsers();
        //testgetUserProfile();
        //testgetUserAttributes();
        //testUserAddDDT();
        //testUserAddNDT();
        trstAddStr();
    }

    private static void testAddFeatures() throws UnknownHostException {
        MongoClient mclient = new MongoClient();
        DB db = mclient.getDB("mydb");
        ArrayList<PFeature> features = new ArrayList<PFeature>();
        for (int i = 0; i < 100; i++) {
            PFeature ph = new PFeature("ftr" + i, i, i);
            features.add(ph);
        }
        FeatureAttributeDAO.setFeatures(db, clientname, features);
        Parameters.NUM_OF_FEATURES_PER_PROFILE = 11;
    }

    private static void testAddAttributes() throws UnknownHostException {
        MongoClient mclient = new MongoClient();
        DB db = mclient.getDB("mydb");
        ArrayList<PAttribute> attributes = new ArrayList<PAttribute>();
        for (int i = 0; i < 100; i++) {
            PAttribute ph = new PAttribute("attr" + i, i + "", i + "");
            attributes.add(ph);
        }
        FeatureAttributeDAO.setAttributes(db, clientname, attributes);
        Parameters.NUM_OF_FEATURES_PER_PROFILE = 11;
    }

    private static void testAddUSerPreferences() throws UnknownHostException {
        MongoClient mclient = new MongoClient();
        DB db = mclient.getDB("mydb");

        Parameters.NUM_OF_FEATURES_PER_PROFILE = 11;
        PUser user = new PUser();
        user.setName("some_user");
        ArrayList<PFeature> prefernces = new ArrayList<PFeature>();
        for (int i = 0; i < 100; i++) {
            prefernces.add(new PFeature("ftr" + i, (float) Math.random(), 0.0f));
        }
        user.setPreferences(prefernces);
        UserDAO.setUserProfile(db, clientname, user);

        DBCollection col = db.getCollection(UserDAO.COLLECTION_USER_PROFILES + "_" + clientname);
        DBObject obj = col.findOne(new BasicDBObject("_id", new BasicDBObject("name", "some_user").append("idx", 0)));

        System.out.println(obj.toString());

        //=====================update preferences
        ArrayList<PFeature> updatePrefernces = new ArrayList<PFeature>();
        for (int i = prefernces.size() / 2; i < prefernces.size() + 5; i++) {
            if (i % 2 == 0) {
                continue;
            }
            updatePrefernces.add(new PFeature("ftr" + i, (float) Math.random(), 0.0f));
        }

        UserDAO.updateUserProfile(db, clientname, user.getName(), updatePrefernces, true);

        //obj = col.findOne( new BasicDBObject("_id", new BasicDBObject("name","some_user").append("idx", 0)));        
        //System.out.println( obj.toString()  );
    }

    private static void testpers() throws UnknownHostException {
        MongoClient mclient = new MongoClient();
        DB db = mclient.getDB("mydb");
        Pers pers = new Pers();
        VectorMap parameters = new VectorMap(1);
        for (int i = 0; i < 100; i++) {
            parameters.add("pftr", "2");
        }
        System.out.println("Calling service");
        PServiceResult resault = pers.service(clientname, parameters, db);
        if (resault.getReturnCode() == PServiceResult.STATUS_OK) {
            System.out.println("Something is wrong");
            return;
        } else {
            System.out.println(resault.getErrorMessage());
        }
        parameters.add("com", "blablabla");
        resault = pers.service(clientname, parameters, db);
        if (resault.getReturnCode() == PServiceResult.STATUS_OK) {
            System.out.println("Something is wrong");
            return;
        } else {
            System.out.println(resault.getErrorMessage());
        }
        parameters = new VectorMap(1);
        for (int i = 0; i < 100; i++) {
            parameters.add("pftr" + i, "2");
        }
        parameters.add("Otherpftr" + 0, "2");
        parameters.add("Otherpftr" + 1, "2");
        parameters.add("Otherpftr" + 3, "2");
        parameters.add("Otherpftr" + 4, "2");
        parameters.add("com", "addftr");
        resault = pers.service(clientname, parameters, db);
        if (resault.getReturnCode() != PServiceResult.STATUS_OK) {
            System.out.println("wroooong");
            return;
        }

        parameters = new VectorMap(1);
        parameters.add("com", "getdef");
        parameters.add("ftr", "Other*");
        parameters.add("ftr", "pftr1");
        resault = pers.service(clientname, parameters, db);
        ArrayList<String> header = resault.getResultHeaders();
        ArrayList<ArrayList<String>> results = resault.getResult();
        System.out.println(header.get(0) + "---" + header.get(1));
        for (ArrayList<String> attribute : results) {
            System.out.println(attribute.get(0) + "---" + attribute.get(1));
        }

    }

    private static void testAttr() throws UnknownHostException {
        MongoClient mclient = new MongoClient();
        DB db = mclient.getDB("mydb");
        Pers pers = new Pers();
        VectorMap parameters = new VectorMap(1);
        for (int i = 0; i < 100; i++) {
            parameters.add("pAttr", "value " + i);
        }
        System.out.println("Calling service");
        PServiceResult resault = pers.service(clientname, parameters, db);
        if (resault.getReturnCode() == PServiceResult.STATUS_OK) {
            System.out.println("Something is wrong");
            return;
        } else {
            System.out.println(resault.getErrorMessage());
        }
        parameters.add("com", "blablabla");
        resault = pers.service(clientname, parameters, db);
        if (resault.getReturnCode() == PServiceResult.STATUS_OK) {
            System.out.println("Something is wrong");
            return;
        } else {
            System.out.println(resault.getErrorMessage());
        }
        parameters = new VectorMap(1);
        for (int i = 0; i < 100; i++) {
            parameters.add("pAttr" + i, "2");
        }
        parameters.add("other1", "a2");
        parameters.add("other2", "b2");
        parameters.add("com", "addAttr");
        System.out.println("============final===============");
        resault = pers.service(clientname, parameters, db);
        if (resault.getReturnCode() != PServiceResult.STATUS_OK) {
            System.out.println("wroooong");
            System.out.println( resault.getErrorMessage());
            return;
        } else {            
        }

        parameters = new VectorMap(1);
        parameters.add("com", "getattrdef");
        parameters.add("attr", "pAttr*");
        parameters.add("attr", "other2");
        resault = pers.service(clientname, parameters, db);
        ArrayList<String> header = resault.getResultHeaders();
        ArrayList<ArrayList<String>> results = resault.getResult();
        System.out.println(header.get(0) + "---" + header.get(1));
        for (ArrayList<String> attribute : results) {
            System.out.println(attribute.get(0) + "---" + attribute.get(1));
        }
    }

    private static void testsetUser() throws UnknownHostException {
        MongoClient mclient = new MongoClient();
        DB db = mclient.getDB("mydb");
        VectorMap parameters = new VectorMap(1);
        parameters.add("com", "setusr");
        parameters.add("attr_pAttr1", "" + 3);
        parameters.add("attr_pAttr2", "" + 4);
        parameters.add("ftr_pftr1", "" + 4);
        parameters.add("ftr_pftr3", "" + 4);
        parameters.add("Otherpftr*", "" + 10);
        parameters.add("usr", "Alex");
        //parameters.add("blabla*", ""+ 10);
        Pers pers = new Pers();
        PServiceResult resault = pers.service(clientname, parameters, db);
        parameters.add("attr_pAttr*", "" + 5);
        resault = pers.service(clientname, parameters, db);
        System.out.println(resault.getErrorMessage());
    }

    private static void testgetUsers() throws UnknownHostException {
        MongoClient mclient = new MongoClient();
        DB db = mclient.getDB("mydb");
        Pers pers = new Pers();
        VectorMap parameters = new VectorMap(1);
        parameters.add("com", "getusrs");
        parameters.add("whr", "*");
        PServiceResult resault = pers.service(clientname, parameters, db);
        System.out.println(resault.getResultHeaders().get(0));
        for (List<String> res : resault.getResult()) {
            System.out.println(res.get(0));
        }
    }

    private static void testgetUserProfile() throws UnknownHostException {
        MongoClient mclient = new MongoClient();
        DB db = mclient.getDB("mydb");
        Pers pers = new Pers();
        VectorMap parameters = new VectorMap(1);
        parameters.add("com", "getusrftr");
        parameters.add("usr", "Alex");
        //parameters.add("ftr", "Otherpftr3");
        parameters.add("num", "3");
        parameters.add("srt", "asc");
        PServiceResult resault = pers.service(clientname, parameters, db);
        if (resault.getErrorMessage() == null) {
            ArrayList<ArrayList<String>> ret = resault.getResult();
            for (ArrayList<String> row : ret) {
                String ftrName = row.get(0);
                String ftrVal = row.get(1);
                System.out.println(ftrName + "---" + ftrVal);
            }
        } else {
            System.out.println(resault.getErrorMessage());
        }
        /*System.out.println( resault.getResultHeaders().get(0) );
         for( List<String> res : resault.getResult()){
         System.out.println( res.get(0) );
         }*/
    }

    private static void testgetUserAttributes() throws UnknownHostException {
        MongoClient mclient = new MongoClient();
        DB db = mclient.getDB("mydb");
        Pers pers = new Pers();
        VectorMap parameters = new VectorMap(1);
        parameters.add("com", "getattrdef");        
        parameters.add("attr", "pAttr*");
        PServiceResult resault = pers.service(clientname, parameters, db);
        if (resault.getErrorMessage() == null) {
            ArrayList<ArrayList<String>> ret = resault.getResult();
            for (ArrayList<String> row : ret) {                
                String ftrName = row.get(0);
                String ftrtype = row.get(1);
                String ftrVal = row.get(2);
                System.out.println(ftrName + "---"+ ftrtype + "---" + ftrVal);
            }
        } else {
            System.out.println(resault.getErrorMessage());
        }
    }

    private static void testUserAddDDT() throws UnknownHostException {
        MongoClient mclient = new MongoClient();
        DB db = mclient.getDB("mydb");
        Pers pers = new Pers();
        VectorMap parameters = new VectorMap(1);
        parameters.add("com", "addddt");
        parameters.add("usr", "Alex");
        //parameters.add("sid", "1");        
        parameters.add("ddt", "new");
        PServiceResult resault = pers.service(clientname, parameters, db);
        if (resault.getErrorMessage() == null) {
           
        } else {
            System.out.println(resault.getErrorMessage());
        }
    }
    
    private static void testUserAddNDT() throws UnknownHostException {
        MongoClient mclient = new MongoClient();
        DB db = mclient.getDB("mydb");
        Pers pers = new Pers();
        VectorMap parameters = new VectorMap(1);
        parameters.add("com", "addndt");
        parameters.add("usr", "Alex");
        //parameters.add("sid", "1");        
        parameters.add("ndt", "new");
        PServiceResult resault = pers.service(clientname, parameters, db);
        if (resault.getErrorMessage() == null) {
           
        } else {
            System.out.println(resault.getErrorMessage());
        }
    }

    private static void testPersAddAttr() throws UnknownHostException {
        MongoClient mclient = new MongoClient();
        DB db = mclient.getDB("mydb");
        Pers pers = new Pers();        
        
        VectorMap parameters = new VectorMap(1);
        parameters.add("com", "addattr");
        for (int i = 0; i < 100; i++) {
            parameters.add("pAttr", "str:value " + i);
        }        
        PServiceResult resault = pers.service(clientname, parameters, db);
        if (resault.getReturnCode() == PServiceResult.STATUS_OK) {            
            return;
        } else {
            System.out.println(resault.getErrorMessage());
        }        
    }

    private static void trstAddStr() throws UnknownHostException {
        MongoClient mclient = new MongoClient();
        DB db = mclient.getDB("mydb");
        Ster ster = new Ster();        
        
        VectorMap parameters = new VectorMap(1);
        parameters.add("str", "stereotype1");
        parameters.add("com", "addstr");      
        parameters.add("rule", "attr1$:$val1;attr2$:$val2");
        PServiceResult resault = ster.service(clientname, parameters, db);
        if (resault.getReturnCode() == PServiceResult.STATUS_OK) {            
            return;
        } else {
            System.out.println(resault.getErrorMessage());
        }        
    }
}
