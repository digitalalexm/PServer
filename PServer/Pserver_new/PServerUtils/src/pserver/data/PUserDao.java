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
        GeneralPreferenceDAO.removeAllPreferences(userProfiles,  user.getName());
        GeneralPreferenceDAO.storeAllPreferences(userProfiles, user.getName(), user.getPreferences());
    }

    private static void createUserProfileIndeces( DBCollection userProfilesCollection ) {
        userProfilesCollection.createIndex( new BasicDBObject("_id."+PREFERENCE_PROFILE_ID_NAME,1));
    }
    
    public static void updateUserProfile( DB db, String pclient, String userName, ArrayList<PFeature> values, boolean mustInc ){
        DBCollection userProfiles = db.getCollection(COLLECTION_USER_PROFILES + "_" + pclient );        
        GeneralPreferenceDAO.updatePreferences(userProfiles, userName, values, mustInc);
    }
    
    public static PUser getUserProfile(  DB db, String pclient, String userName ) {
        return null;     
    }   
}
