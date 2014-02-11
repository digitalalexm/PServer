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
import pserver.domain.PAttribute;
import pserver.domain.PFeature;
import pserver.util.PEntry;

/**
 *
 * the default document for the features is { _id:<feature_name>,
 * def_value:<default value> } the default document for the attributes is {
 * _id:<attribute_name>, def_value:<default value> }
 *
 * @author alexm
 */
public class FeatureAttributeDAO {

    public static final String COLLECTION_FEATURES = "features";
    public static final String FEATURE_DOCUMENT_DEFAULT_VALUE = "def_value";
    public static final String COLLECTION_ATTRIBUTES = "attributes";
    public static final String ATTRIBUTE_DOCUMENT_DEFAULT_VALUE = "def_value";

    public static void setFeatures(DB db, String pclient, ArrayList<PFeature> features) {
        DBCollection featureCollection = GeneralDAO.getCollection(db, pclient, COLLECTION_FEATURES );
        for (PFeature feature : features) {
            featureCollection.save(new BasicDBObject("_id", feature.getName()).append(FEATURE_DOCUMENT_DEFAULT_VALUE, feature.getDefValue()));
        }
    }

    public static Double getFeatureDefValue(DB db, String pclient, String featureName) {
        DBCollection featureCollection = GeneralDAO.getCollection(db, pclient, COLLECTION_FEATURES );
        DBObject dbobj = featureCollection.findOne(new BasicDBObject("_id", featureName));        
        if( dbobj != null ) {
            return (Double) dbobj.get(FEATURE_DOCUMENT_DEFAULT_VALUE);
        } else {
            return null;
        }        
    }
    
    public static ArrayList<PEntry<String, Double>> getFeatureDefValues(DB db, String pclient, String regEx) {
        if( regEx.trim().equals("*")) {
            regEx = "";
        }
        ArrayList<PEntry<String, Double>> defValues = new ArrayList<PEntry<String, Double>>();
        DBCollection featureCollection = GeneralDAO.getCollection(db, pclient, COLLECTION_FEATURES );
        DBCursor cursor = featureCollection.find(new BasicDBObject("_id", new BasicDBObject("$regex", regEx)));
        try {
            while (cursor.hasNext()) {
                DBObject dbobj = (BasicDBObject) cursor.next();
                PEntry<String, Double> value = new PEntry<String, Double>();
                value.setKey((String) dbobj.get("_id"));
                value.setValue((Double) dbobj.get(FEATURE_DOCUMENT_DEFAULT_VALUE));
                defValues.add(value);
            }
        } finally {
            cursor.close();
        }
        return defValues;
    }
    
    public static void setAttributes(DB db, String pclient, ArrayList<PAttribute> attributes) {
        DBCollection attributeCollection = GeneralDAO.getCollection(db, pclient, COLLECTION_ATTRIBUTES);
        for (PAttribute attribute : attributes) {
            attributeCollection.save(new BasicDBObject("_id", attribute.getName()).append(ATTRIBUTE_DOCUMENT_DEFAULT_VALUE, attribute.getDefValue()));
        }
    }

    public static String getAttributeDefValue(DB db, String pclient, String attributeName) {
        DBCollection attributeCollection = GeneralDAO.getCollection(db, pclient, COLLECTION_ATTRIBUTES);
        DBObject dbobj = attributeCollection.findOne(new BasicDBObject("_id", attributeName));        
        if( dbobj != null ) {
            return (String) dbobj.get(ATTRIBUTE_DOCUMENT_DEFAULT_VALUE);
        } else {
            return null;
        }
    }

    public static ArrayList<PEntry<String, String>> getAttributeDefValues(DB db, String pclient, String regEx) {
        if( regEx.trim().equals("*")) {
            regEx = "";
        }
        ArrayList<PEntry<String, String>> defValues = new ArrayList<PEntry<String, String>>();
        DBCollection attributeCollection = GeneralDAO.getCollection(db, pclient, COLLECTION_ATTRIBUTES);
        DBCursor cursor = attributeCollection.find(new BasicDBObject("_id", new BasicDBObject("$regex", regEx)));
        try {
            while (cursor.hasNext()) {
                DBObject dbobj = (BasicDBObject) cursor.next();
                PEntry<String, String> value = new PEntry<String, String>();
                value.setKey((String) dbobj.get("_id"));
                value.setValue((String) dbobj.get(ATTRIBUTE_DOCUMENT_DEFAULT_VALUE));
                defValues.add(value);
            }
        } finally {
            cursor.close();
        }
        return defValues;
    }
}
