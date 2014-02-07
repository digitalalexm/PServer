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
 * The preferences are stored in documents  with the following template
 * 
 * { _id:{ name:<s0me_name>, idx:<index> },
 *  preferences: [ feature :{ name: "", score:"" } ]
 * }
 */
package pserver.data;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import java.util.ArrayList;
import java.util.Iterator;
import static pserver.data.PUserDAO.PREFERENCE_DOCUMENT_ID_INDEX;
import static pserver.data.PUserDAO.PREFERENCE_DOCUMENT_ID_NAME;
import static pserver.data.PUserDAO.PREFERENCE_DOCUMENT_FEATURES;
import static pserver.data.PUserDAO.PREFERENCE_DOCUMENT_FEATURE_NAME;
import static pserver.data.PUserDAO.PREFERENCE_DOCUMENT_FEATURE_VALUE;
import pserver.domain.PFeature;
import pserver.parameters.Parameters;

/**
 *
 * @author alexm
 */
public class GeneralPreferenceDAO {

    public static void storeAllPreferences(DBCollection collection, String name, ArrayList<PFeature> featurePreferences, int firstIdx) {
        int idx = firstIdx;
        int counter = 0;        
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
        BasicDBObject id = new BasicDBObject(PREFERENCE_DOCUMENT_ID_NAME, name).append(PREFERENCE_DOCUMENT_ID_INDEX, docIdx);
        BasicDBObject userJson = new BasicDBObject("_id", id);
        BasicDBList preferences = new BasicDBList();
        for (int i = start; i < end; i++) {
            PFeature feature = featurePreferences.get(i);
            BasicDBObject featureJson = new BasicDBObject();
            BasicDBObject featureNameValueJson = new BasicDBObject(PREFERENCE_DOCUMENT_FEATURE_NAME, feature.getName())
                    .append(PREFERENCE_DOCUMENT_FEATURE_VALUE, feature.getValue());
            featureJson.append(feature.getName(), feature.getValue());
            preferences.add(featureNameValueJson);
        }
        userJson.append(PREFERENCE_DOCUMENT_FEATURES, preferences);
        collection.save(userJson);
        return userJson;
    }

    
    public static void updatePreferences(DBCollection collection, String userName, ArrayList<PFeature> featurePreferences, boolean mustInc) {
        int numOfDocuments = getNumberOfPreferenceDocuments(collection, userName);
        ArrayList<PFeature> localFeaturePreferences = new ArrayList<>(featurePreferences);
        for( int i = 0; i < numOfDocuments; i ++){
            BasicDBObject id = new BasicDBObject(PREFERENCE_DOCUMENT_ID_NAME, userName).append(PREFERENCE_DOCUMENT_ID_INDEX, i);
            ArrayList<PFeature> preferences = getPreferenceDocument( collection, id );            
            updateArrayOfPreferences( preferences, localFeaturePreferences, mustInc );
            if( i < numOfDocuments - 1 ){                
                storePreferences(collection, userName, preferences, i, 0, preferences.size());
            } else {
                if( localFeaturePreferences.isEmpty() == true ) {
                    return;
                }
                int offset = Math.min( Parameters.NUM_OF_FEATURES_PER_PROFILE, localFeaturePreferences.size());
                for( int j = 0; j < offset; j ++ ){
                    PFeature moveFtr = localFeaturePreferences.remove(0);
                    if( mustInc ) {
                        moveFtr.setValue( moveFtr.getDefValue() + moveFtr.getValue() );
                    }
                    preferences.add( moveFtr );                                        
                }                
                storePreferences(collection, userName, preferences, i, 0, preferences.size());
                if( localFeaturePreferences.isEmpty() == false ){                    
                    storeAllPreferences( collection, userName, localFeaturePreferences, numOfDocuments );
                }
            }
        }
    }
    
    public static void removeAllPreferences(DBCollection collection, String name ) {
        collection.remove( new BasicDBObject("_id."+PREFERENCE_DOCUMENT_ID_NAME, name));
    }
    
    public static void removePreferences(DBCollection collection, String name, int docIdx ) {
        collection.remove( new BasicDBObject("_id."+PREFERENCE_DOCUMENT_ID_NAME, name).append("_id."+PREFERENCE_DOCUMENT_ID_INDEX, docIdx));
    }
    
    public static int getNumberOfPreferenceDocuments(DBCollection collection, String name ) {
        BasicDBObject query = new BasicDBObject("_id."+PREFERENCE_DOCUMENT_ID_NAME, name);                
        int num = 0;
        DBCursor cursor =collection.find(query);
        try{            
            num = cursor.count();            
        } finally {
            cursor.close();
        }
        return num;
    }

    private static ArrayList<PFeature> getPreferenceDocument(DBCollection collection, BasicDBObject id) {
        ArrayList<PFeature> storedFeatures = new ArrayList<>();        
        DBObject doc = collection.findOne( new BasicDBObject( "_id", id));        
        BasicDBList preferences = (BasicDBList) doc.get(PREFERENCE_DOCUMENT_FEATURES);
        for( Iterator< Object > it = preferences.iterator(); it.hasNext(); ) {
            BasicDBObject dbo = ( BasicDBObject ) it.next();
            PFeature feature = new PFeature();
            feature.setName(dbo.getString(PREFERENCE_DOCUMENT_FEATURE_NAME));
            feature.setValue(dbo.getDouble(PREFERENCE_DOCUMENT_FEATURE_VALUE));
            storedFeatures.add(feature);
        }
        return storedFeatures;
    }

    private static void updateArrayOfPreferences(ArrayList<PFeature> preferences, ArrayList<PFeature> newFeaturePreferences, boolean mustInc) {        
        for ( int i = 0 ; i < preferences.size(); i ++ ){            
            PFeature firstFeature = preferences.get(i);
            for( int j = 0; j < newFeaturePreferences.size(); j++ ) {
                PFeature secondFeature = newFeaturePreferences.get(j);                
                if( firstFeature.getName().equals(secondFeature.getName() ) ) {
                    if(mustInc == true ) {
                        firstFeature.setValue( firstFeature.getValue() + secondFeature.getValue() );
                    } else {
                        firstFeature.setValue( secondFeature.getValue() );
                    }
                    newFeaturePreferences.remove(j);                    
                    break;
                }
            }
        }
    }
        
}
