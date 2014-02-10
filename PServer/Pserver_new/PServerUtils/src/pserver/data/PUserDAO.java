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

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import pserver.domain.PAttribute;
import pserver.domain.PFeature;
import pserver.domain.PUser;
import pserver.util.CollectionManager;

/**
 *
 * @author alexm
 */
public class PUserDAO {
    /*
     * the name of the collection that stores all the user profiles
     */
    public static String COLLECTION_USER_PROFILES = "userProfiles";
    /*
     * the name of the collection that stores all the user attributes
     */
    public static String COLLECTION_USER_ATTRIBUTES = "userAttributes";
    /*
     * the name of the fields of the documents that will store preferences
     */
    public static String PREFERENCE_DOCUMENT_ID_NAME = "name";
    public static String PREFERENCE_DOCUMENT_ID_INDEX = "idx";
    public static String PREFERENCE_DOCUMENT_FEATURES = "features";
    public static String PREFERENCE_DOCUMENT_FEATURE_NAME = "name";
    public static String PREFERENCE_DOCUMENT_FEATURE_VALUE = "value";
    
    public static String ATTRIBUTES_DOCUMENT_ATTRIBUTES = "attributes";
    public static String ATTRIBUTES_DOCUMENT_ATTRIBUTE_NAME = "name";
    public static String ATTRIBUTES_DOCUMENT_ATTRIBUTE_value = "value";
    
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
        DBCollection userProfiles = GeneralDAO.getCollection(db, pclient, COLLECTION_USER_PROFILES);
        GeneralPreferenceDAO.removeAllPreferences(userProfiles,  user.getName());
        GeneralPreferenceDAO.storeAllPreferences(userProfiles, user.getName(), user.getPreferences(), 0);
    }
    
    public static void updateUserProfile( DB db, String pclient, String userName, ArrayList<PFeature> features, boolean mustInc ){
        DBCollection userProfiles = GeneralDAO.getCollection(db, pclient, COLLECTION_USER_PROFILES );        
        GeneralPreferenceDAO.updatePreferences(userProfiles, userName, features, mustInc);
    }
    
    public static void updateUserAttributes( DB db, String pclient, String userName, ArrayList<PAttribute> attributes ){
        DBCollection userAttributes = GeneralDAO.getCollection(db, pclient, COLLECTION_USER_ATTRIBUTES);
        BasicDBObject doc = (BasicDBObject) userAttributes.findOne(new BasicDBObject("_id",userName));        
        if( doc != null ) {
            BasicDBList attrList = (BasicDBList) doc.get(ATTRIBUTES_DOCUMENT_ATTRIBUTES);
            LinkedList<PAttribute> storedAttribures = new LinkedList<PAttribute>();
            for( Iterator< Object > it = attrList.iterator(); it.hasNext(); ) {
                BasicDBObject attrDoc = (BasicDBObject) it.next();
                PAttribute storedAttribure = new PAttribute();                
                storedAttribure.setName(attrDoc.getString(ATTRIBUTES_DOCUMENT_ATTRIBUTE_NAME));
                storedAttribure.setValue(attrDoc.getString(ATTRIBUTES_DOCUMENT_ATTRIBUTE_value));
                storedAttribures.add(storedAttribure);
            }            
            CollectionManager.mergeOldWithNewAttributes(storedAttribures, attributes);
        }                        
        BasicDBList attributeList = new BasicDBList();
        for( PAttribute attr : attributes ) {
            BasicDBObject adObj = new BasicDBObject(ATTRIBUTES_DOCUMENT_ATTRIBUTE_NAME,attr.getName())
                    .append(ATTRIBUTES_DOCUMENT_ATTRIBUTE_value, attr.getValue());
            attributeList.add(adObj);
        }
        BasicDBObject uAttrDoc = new BasicDBObject("_id",userName).append(ATTRIBUTES_DOCUMENT_ATTRIBUTES, attributeList);        
        userAttributes.save( uAttrDoc );
    }
    
    public static List<String> getUsers(DB db, String pclient, String regEx) {
        if( regEx.trim().equals("*")) {
            regEx = "";
        }
        LinkedList<String> names = new LinkedList<String>();
        DBCollection userAttributes = GeneralDAO.getCollection(db, pclient, COLLECTION_USER_ATTRIBUTES);
        BasicDBObject query = new BasicDBObject("_id", new BasicDBObject("$regex", regEx));        
        DBCursor cursor = userAttributes.find( query,new BasicDBObject("_id",1) );
        try{
            while( cursor.hasNext() ) {
                BasicDBObject doc = (BasicDBObject) cursor.next();
                String name = doc.getString("_id");
                names.add(name);
            }
        }finally{
            cursor.close();
        }
        return names;
    }

    public static List<PFeature> getUserProfile(DB db, String pclient, String userName, String ftrΡεγΕχ, int num, boolean sortAsc) {
        if( ftrΡεγΕχ.equals("*")) {
            ftrΡεγΕχ = "";
        }
        DBCollection userProfiles = GeneralDAO.getCollection(db, pclient, COLLECTION_USER_PROFILES);
        return GeneralPreferenceDAO.getProfile(userProfiles, userName, ftrΡεγΕχ, num, sortAsc);
    }
}
