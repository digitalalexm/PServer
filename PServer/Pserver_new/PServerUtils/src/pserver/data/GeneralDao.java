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
import com.mongodb.DBCollection;
import java.util.ArrayList;
import static pserver.data.PUserDao.PREFERENCE_PROFILE_ID_INDEX;
import static pserver.data.PUserDao.PREFERENCE_PROFILE_ID_NAME;
import pserver.domain.PFeature;
import pserver.parameters.Parameters;

/**
 *
 * @author alexm
 */
public class GeneralDao {
    public static void storePreferences( DBCollection collection, String name, ArrayList<PFeature> featurePreferences ){
        int idx = 0;
        int counter = 0;                
        do {            
            /*
             * Create de json document based on the template
             * { _id:{ name:"", idx:1 },
             *  preferences: [ feature :{ name: "", score:"" } ]
             */
            BasicDBObject id = new BasicDBObject(PREFERENCE_PROFILE_ID_NAME, name).append(PREFERENCE_PROFILE_ID_INDEX, idx);            
            BasicDBObject userJson = new BasicDBObject("_id", id);
                                
            int limit = Math.min( counter + Parameters.NUM_OF_FEATURES_PER_PROFILE, featurePreferences.size());            
            for( int i = counter; i < limit; i ++) {                
                PFeature feature = featurePreferences.get(i);
                BasicDBObject featureJson = new BasicDBObject();
                userJson.append(feature.getName(), feature.getValue());                
            }        
            counter += Parameters.NUM_OF_FEATURES_PER_PROFILE;
            collection.update( new BasicDBObject( "_id", id), userJson, true, false);
            idx ++;
        } while( counter < featurePreferences.size() );
                
    }
}
