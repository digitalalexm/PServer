/* 
 * Copyright 2011 NCSR "Demokritos"
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");   
 * you may not use this file except in compliance with the License.   
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *    
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
*/

package pserver.data;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Set;
import pserver.domain.PFeature;
import pserver.domain.PUser;
import pserver.parameters.Parameters;

/**
 *
 * @author alexm
 */
public class PUserDao {
    /*
     * the name of the collection that stores all the user profiles
     */
    public static String COLLECTION_USER_PROFILES = "UserProfiles";
    /*
     * the name of the fields of the documents that will store preferences
     */
    public static String PREFERENCE_PROFILE_ID_NAME = "name";
    public static String PREFERENCE_PROFILE_ID_INDEX = "idx";
    public static String PREFERENCE_PROFILE_FEATURES = "features";
    public static String PREFERENCE_PROFILE_FEATURE_NAME = "name";
    public static String PREFERENCE_PROFILE_FEATURE_VALUE = "value";
    
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
        DBCollection userProfiles = db.getCollection(COLLECTION_USER_PROFILES + "_" + pclient );        
        /*
         * check id there are no indeces and if not craate them 
         */
        if( userProfiles.getIndexInfo().isEmpty() == true ) {
            createUserProfileIndeces( userProfiles );
        }        
        
        GeneralPreferenceDAO.storeAllPreferences(userProfiles, user.getName(), user.getPreferences());
    }

    private static void createUserProfileIndeces( DBCollection userProfilesCollection ) {
        System.out.println("creating indeces");
    }
    
    public static void UpdateUserProfile( DB db, String pclient, String userName, ArrayList<PFeature> values, boolean mustInc ){
        
    }
    
    public static PUser getUserProfile(  DB db, String pclient, String userName ) {
        PUser userProfile = new PUser();
        userProfile.setName(userName);
        DBCollection userProfiles = db.getCollection(COLLECTION_USER_PROFILES + "_" + pclient );
        DBCollection userProfilesSmalls = db.getCollection(COLLECTION_USER_PROFILES + "_" + pclient +"_smalls" );
        long t1 = System.currentTimeMillis();
        DBCursor cursor1 = userProfiles.find( new BasicDBObject( "_id", userName ) );
        while( cursor1.hasNext() ) {
            DBObject dobj = cursor1.next();
            userProfile.setName( (String)dobj.get("_id") );
            Set<String> keys = dobj.keySet();
            for( String key : keys) {
                /*if( key.equals(("_id"))) {
                    System.out.println("_id 1");
                    continue;
                }
                PFeature feature = new PFeature();
                feature.setName(key);
                feature.setValue((Double) dobj.get(key));  */              
            }
        }
        long t2 = System.currentTimeMillis();
        System.out.println("Time to retrieve with big file: " + (t2-t1)/1000.0f);
        
        t1 = System.currentTimeMillis();
        DBCursor cursor2 = userProfilesSmalls.find( new BasicDBObject( "_id.name", userName ) );
        while( cursor2.hasNext() ) {            
            DBObject dobj = cursor2.next();
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
