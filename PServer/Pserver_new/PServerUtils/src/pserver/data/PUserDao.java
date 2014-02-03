/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pserver.data;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.awt.Cursor;
import java.util.ArrayList;
import java.util.Set;
import pserver.domain.PFeature;
import pserver.domain.PUser;

/**
 *
 * @author alexm
 */
public class PUserDao {
    /**
     * the name of the collection that stores all the user profiles
     */
    public static String COLLECTION_BASENAME_USER_PROFILES = "user_profiles";
    
    /**
     * 
     * @param db the mongo database
     * @param pclient the pserver client that wants to store profiles
     * @param user the profile that needs to be stored
     */
    public static void SetUserProfile( DB db, String pclient, PUser user ){
        /*
         * The name of all the collections are based on the name of the pserver client
         */
        DBCollection userProfiles = db.getCollection(COLLECTION_BASENAME_USER_PROFILES + "_" + pclient );
        DBCollection userProfilesSmalls = db.getCollection(COLLECTION_BASENAME_USER_PROFILES + "_" + pclient +"_smalls" );
        /*
         * check id there are no indeces and if not craate them 
         */
        if( userProfiles.getIndexInfo().size() == 0) {
            createUserProfileIndeces( userProfiles );
        }        
        
        BasicDBObject userJson = new BasicDBObject("_id", user.getName());
        
        long t1 = System.currentTimeMillis();
        ArrayList<PFeature> featurePreferences = user.getPreferences();
        for( PFeature feature : featurePreferences ) {
            userJson.append(feature.getName(), feature.getValue());
        }        
        userProfiles.update( new BasicDBObject( "_id", user.getName()), userJson, true, false);
        long t2 = System.currentTimeMillis();
        System.out.println("Time to update with big file: " + (t2-t1)/1000.0f);
        
        t1 = System.currentTimeMillis();
        featurePreferences = user.getPreferences();
        for( PFeature feature : featurePreferences ) {
            BasicDBObject idJson = new BasicDBObject("name", user.getName()).append("feature", feature.getName());
            BasicDBObject smallUserJson = new BasicDBObject( "_id", idJson );
            smallUserJson.append("value", feature.getValue());
            userProfilesSmalls.update( new BasicDBObject( "_id", idJson), smallUserJson, true, false);
        }                
        t2 = System.currentTimeMillis();
        System.out.println("Time to update with big file: " + (t2-t1)/1000.0f);
    }

    private static void createUserProfileIndeces( DBCollection userProfilesCollection ) {
        System.out.println("creating indeces");
    }
    
    public static PUser getUserProfile(  DB db, String pclient, String userName ) {
        PUser userProfile = new PUser();
        userProfile.setName(userName);
        DBCollection userProfiles = db.getCollection(COLLECTION_BASENAME_USER_PROFILES + "_" + pclient );
        DBCollection userProfilesSmalls = db.getCollection(COLLECTION_BASENAME_USER_PROFILES + "_" + pclient +"_smalls" );
        long t1 = System.currentTimeMillis();
        DBCursor cursor1 = userProfiles.find( new BasicDBObject( "_id", userName ) );
        while( cursor1.hasNext() ) {
            DBObject dobj = cursor1.next();
            userProfile.setName( (String)dobj.get("_id") );
            Set<String> keys = dobj.keySet();
            for( String key : keys) {
                if( key.equals(("_id"))) {
                    System.out.println("_id 1");
                    continue;
                }
                PFeature feature = new PFeature();
                feature.setName(key);
                feature.setValue((Double) dobj.get(key));                
            }
        }
        long t2 = System.currentTimeMillis();
        System.out.println("Time to retrieve with big file: " + (t2-t1)/1000.0f);
        
        t1 = System.currentTimeMillis();
        DBCursor cursor2 = userProfilesSmalls.find( new BasicDBObject( "_id.name", userName ) );
        while( cursor1.hasNext() ) {
            DBObject dobj = cursor1.next();
            PFeature feature = new PFeature();
            feature.setName((String)dobj.get("_id.feature"));
            feature.setValue((Double) dobj.get("value"));
        }
        t2 = System.currentTimeMillis();
        System.out.println("Time to retrieve with small file: " + (t2-t1)/1000.0f);
        //DBCursor cursor2 = userProfilesSmalls.findOne( new BasicDBObject( "_id.name", userName ) );
        return userProfile;                
    }
}
