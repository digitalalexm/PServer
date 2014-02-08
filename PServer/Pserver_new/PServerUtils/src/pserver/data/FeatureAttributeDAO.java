/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pserver.data;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.LinkedList;
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
        DBCollection featureCollection = db.getCollection(COLLECTION_FEATURES + "_" + pclient);
        for (PFeature feature : features) {
            featureCollection.save(new BasicDBObject("_id", feature.getName()).append(FEATURE_DOCUMENT_DEFAULT_VALUE, feature.getDefValue()));
        }
    }

    public static Double getFeatureDefValue(DB db, String pclient, String featureName) {
        DBCollection featureCollection = db.getCollection(COLLECTION_FEATURES + "_" + pclient);
        DBObject dbobj = featureCollection.findOne(new BasicDBObject("_id", featureName));        
        if( dbobj != null ) {
            return (Double) dbobj.get(FEATURE_DOCUMENT_DEFAULT_VALUE);
        } else {
            return null;
        }        
    }
    
    public static ArrayList<PEntry<String, Double>> getFeatureDefValues(DB db, String pclient, String regEx) {
        ArrayList<PEntry<String, Double>> defValues = new ArrayList<PEntry<String, Double>>();
        DBCollection featureCollection = db.getCollection(COLLECTION_FEATURES + "_" + pclient);
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
        DBCollection attributeCollection = db.getCollection(COLLECTION_ATTRIBUTES + "_" + pclient);
        for (PAttribute attribute : attributes) {
            attributeCollection.save(new BasicDBObject("_id", attribute.getName()).append(ATTRIBUTE_DOCUMENT_DEFAULT_VALUE, attribute.getDefValue()));
        }
    }

    public static String getAttributeDefValue(DB db, String pclient, String attributeName) {
        DBCollection attributeCollection = db.getCollection(COLLECTION_ATTRIBUTES + "_" + pclient);
        DBObject dbobj = attributeCollection.findOne(new BasicDBObject("_id", attributeName));        
        if( dbobj != null ) {
            return (String) dbobj.get(ATTRIBUTE_DOCUMENT_DEFAULT_VALUE);
        } else {
            return null;
        }
    }

    public static ArrayList<PEntry<String, String>> getAttributeDefValues(DB db, String pclient, String regEx) {
        ArrayList<PEntry<String, String>> defValues = new ArrayList<PEntry<String, String>>();
        DBCollection attributeCollection = db.getCollection(COLLECTION_ATTRIBUTES + "_" + pclient);
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
