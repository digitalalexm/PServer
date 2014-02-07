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
import pserver.data.FeatureAttributeDAO;
import pserver.data.PUserDAO;
import pserver.domain.PFeature;
import pserver.domain.PAttribute;
import pserver.domain.PUser;
import pserver.parameters.Parameters;
import pserver.pservlets.Implementations.Pers;
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
        testpers();
        testAttr();
    }
    
    private static void testAddFeatures() throws UnknownHostException{
        MongoClient mclient = new MongoClient();
        DB db = mclient.getDB( "mydb" );
        ArrayList<PFeature> features = new ArrayList<PFeature>();
        for( int i = 0 ;i < 100; i ++) {            
            PFeature ph = new PFeature("ftr"+i, i, i);
            features.add(ph);
        }
        FeatureAttributeDAO.setFeatures(db, clientname,features );
        Parameters.NUM_OF_FEATURES_PER_PROFILE = 11;
    }
    
    private static void testAddAttributes() throws UnknownHostException{
        MongoClient mclient = new MongoClient();
        DB db = mclient.getDB( "mydb" );
        ArrayList<PAttribute> attributes = new ArrayList<PAttribute>();
        for( int i = 0 ;i < 100; i ++) {            
            PAttribute ph = new PAttribute("attr"+i, i + "", i + "");
            attributes.add(ph);
        }
        FeatureAttributeDAO.setAttributes(db, clientname,attributes );
        Parameters.NUM_OF_FEATURES_PER_PROFILE = 11;
    }
    
    private static void testAddUSerPreferences() throws UnknownHostException{
        MongoClient mclient = new MongoClient();
        DB db = mclient.getDB( "mydb" );
        
        Parameters.NUM_OF_FEATURES_PER_PROFILE = 11;
        PUser user = new PUser();
        user.setName("some_user");
        ArrayList<PFeature> prefernces = new ArrayList<PFeature>();
        for( int i = 0 ; i < 100; i ++ ) {
            prefernces.add( new PFeature( "ftr" + i, (float)Math.random(), 0.0f ));
        }
        user.setPreferences(prefernces);
        PUserDAO.SetUserProfile(db, clientname, user );
        
        DBCollection col = db.getCollection(PUserDAO.COLLECTION_USER_PROFILES + "_" + clientname);
        DBObject obj = col.findOne( new BasicDBObject("_id", new BasicDBObject("name","some_user").append("idx", 0)));
        
        System.out.println( obj.toString()  );
        
        //=====================update preferences
        ArrayList<PFeature> updatePrefernces = new ArrayList<PFeature>();
        for( int i = prefernces.size()/2 ; i < prefernces.size() + 5; i ++ ) {
            if( i % 2 == 0 ){
                continue;
            }
            updatePrefernces.add( new PFeature( "ftr" + i, (float)Math.random(), 0.0f ));
        }
        
        PUserDAO.updateUserProfile(db, clientname, user.getName(), updatePrefernces, true );
        
        //obj = col.findOne( new BasicDBObject("_id", new BasicDBObject("name","some_user").append("idx", 0)));        
        //System.out.println( obj.toString()  );
    }

    private static void testpers() throws UnknownHostException {
        MongoClient mclient = new MongoClient();
        DB db = mclient.getDB( "mydb" );
        Pers pers = new Pers();
        VectorMap parameters = new VectorMap(1);
        for( int i = 0 ; i < 100; i ++) {
            parameters.add("pftr", "2");
        }
        System.out.println("Calling service");
        PServiceResult resault = pers.service(clientname, parameters, db);
        if( resault.getReturnCode() == PServiceResult.STATUS_OK) {
            System.out.println("Something is wrong");
            return;
        } else {
            System.out.println( resault.getErrorMessage() );
        }
        parameters.add("com", "blablabla");
        resault = pers.service(clientname, parameters, db);
        if( resault.getReturnCode() == PServiceResult.STATUS_OK) {
            System.out.println("Something is wrong");
            return;
        } else {
            System.out.println( resault.getErrorMessage() );
        }
        parameters = new VectorMap(1);
        for( int i = 0 ; i < 100; i ++) {
            parameters.add("pftr" + i, "2");
        }
        parameters.add("com", "addftr");
        resault = pers.service(clientname, parameters, db);
        if( resault.getReturnCode() != PServiceResult.STATUS_OK) {
            System.out.println("wroooong");
            return;
        }        
    }

    private static void testAttr() throws UnknownHostException {
        MongoClient mclient = new MongoClient();
        DB db = mclient.getDB( "mydb" );
        Pers pers = new Pers();
        VectorMap parameters = new VectorMap(1);
        for( int i = 0 ; i < 100; i ++) {
            parameters.add("pAttr", "value " +i);
        }
        System.out.println("Calling service");
        PServiceResult resault = pers.service(clientname, parameters, db);
        if( resault.getReturnCode() == PServiceResult.STATUS_OK) {
            System.out.println("Something is wrong");
            return;
        } else {
            System.out.println( resault.getErrorMessage() );
        }
        parameters.add("com", "blablabla");
        resault = pers.service(clientname, parameters, db);
        if( resault.getReturnCode() == PServiceResult.STATUS_OK) {
            System.out.println("Something is wrong");
            return;
        } else {
            System.out.println( resault.getErrorMessage() );
        }
        parameters = new VectorMap(1);
        for( int i = 0 ; i < 100; i ++) {
            parameters.add("pAttr" + i, "2");
        }
        parameters.add("com", "addAttr");
        resault = pers.service(clientname, parameters, db);
        if( resault.getReturnCode() != PServiceResult.STATUS_OK) {
            System.out.println("wroooong");
            return;
        }      
    }
}
