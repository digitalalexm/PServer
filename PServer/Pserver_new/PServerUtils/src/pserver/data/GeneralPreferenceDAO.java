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
/**
 * This class manages all the documents that contain preferences about features
 */
package pserver.data;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoException;
import java.util.ArrayList;
import static pserver.data.PUserDao.PREFERENCE_PROFILE_ID_INDEX;
import static pserver.data.PUserDao.PREFERENCE_PROFILE_ID_NAME;
import static pserver.data.PUserDao.PREFERENCE_PROFILE_FEATURES;
import static pserver.data.PUserDao.PREFERENCE_PROFILE_FEATURE_NAME;
import static pserver.data.PUserDao.PREFERENCE_PROFILE_FEATURE_VALUE;
import pserver.domain.PFeature;
import pserver.parameters.Parameters;

/**
 *
 * @author alexm
 */
public class GeneralPreferenceDAO {

    public static void storeAllPreferences(DBCollection collection, String name, ArrayList<PFeature> featurePreferences) {
        int idx = 0;
        int counter = 0;
        removeAllPreferences(collection, name);
        ArrayList<BasicDBObject> ids = new ArrayList<>();
        do {             
            int limit = Math.min(counter + Parameters.NUM_OF_FEATURES_PER_PROFILE, featurePreferences.size());
            try{ 
                storePreferences(collection, name, featurePreferences, idx, counter, limit);
            } catch (MongoException e ) {
                Parameters.logger.error("Unable to save preferences" );
                Parameters.logger.error( "Exception Message: "+ e.getMessage());
                GeneralDAO.cleanUp(collection, ids);
                throw e;
            }
            counter += Parameters.NUM_OF_FEATURES_PER_PROFILE;            
            idx++;
        } while (counter < featurePreferences.size());

    }

    /**
     * 
     * @param collection
     * @param name
     * @param featurePreferences
     * @param docIdx
     * @param start
     * @param end
     * @return the Id of the new document
     */
    public static BasicDBObject storePreferences(DBCollection collection, String name, ArrayList<PFeature> featurePreferences, int docIdx, int start, int end) {
        /*
         * Create de json document based on the template
         * { _id:{ name:"", idx:1 },
         *  preferences: [ feature :{ name: "", score:"" } ]
         */
        BasicDBObject id = new BasicDBObject(PREFERENCE_PROFILE_ID_NAME, name).append(PREFERENCE_PROFILE_ID_INDEX, docIdx);
        BasicDBObject userJson = new BasicDBObject("_id", id);
        BasicDBList preferences = new BasicDBList();
        for (int i = start; i < end; i++) {
            PFeature feature = featurePreferences.get(i);
            BasicDBObject featureJson = new BasicDBObject();
            BasicDBObject featureNameValueJson = new BasicDBObject(PREFERENCE_PROFILE_FEATURE_NAME, feature.getName())
                    .append(PREFERENCE_PROFILE_FEATURE_VALUE, feature.getValue());
            featureJson.append(feature.getName(), feature.getValue());
            preferences.add(featureNameValueJson);
        }
        userJson.append(PREFERENCE_PROFILE_FEATURES, preferences);
        collection.save(userJson);
        return userJson;
    }

    
    public static void updatePreferences(DBCollection collection, String name, ArrayList<PFeature> featurePreferences, boolean inc) {
        int numOfDocuments = getNumberOfPreferenceDocuments(collection, name);
        for( int i = 0; i < numOfDocuments; i ++){
            
        }
    }
    
    public static void removeAllPreferences(DBCollection collection, String name ) {
        collection.remove( new BasicDBObject("_id."+PREFERENCE_PROFILE_ID_NAME, name));
    }
    
    public static void removePreferences(DBCollection collection, String name, int docIdx ) {
        collection.remove( new BasicDBObject("_id."+PREFERENCE_PROFILE_ID_NAME, name).append("_id."+PREFERENCE_PROFILE_ID_INDEX, docIdx));
    }
    
    public static int getNumberOfPreferenceDocuments(DBCollection collection, String name ) {
        BasicDBObject query = new BasicDBObject("_id."+PREFERENCE_PROFILE_ID_NAME, name);                
        int num = 0;
        DBCursor cursor =collection.find(query);
        try{            
            num = cursor.count();            
        } finally {
            cursor.close();
        }
        return num;
    }
        
}
