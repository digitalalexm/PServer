/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pserver.data;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import java.util.ArrayList;
import java.util.LinkedList;
import pserver.domain.PAttribute;
import pserver.domain.PFeature;

/**
 *
 * the default document for the features is
 * { _id:<feature_name>, def_value:<default value> }
 * the default document for the attributes is
 * { _id:<attribute_name>, def_value:<default value> }
 * @author alexm
 */
public class FeatureAttributeDAO {    
    public static final String COLLECTION_FEATURES = "features";
    public static final String FEATURE_DOCUMENT_DEFAULT_VALUE = "def_value";
    public static final String COLLECTION_ATTRIBUTES = "attributes";
    public static final String FEATURE_Attribute_DEFAULT_VALUE = "def_value";
    
    public static void setFeatures( DB db, String pclient, ArrayList<PFeature> features) {
        DBCollection featureCollection = db.getCollection(COLLECTION_FEATURES + "_" + pclient );        
        for( PFeature feature : features ) {           
            featureCollection.save(new BasicDBObject("_id", feature.getName()).append(FEATURE_DOCUMENT_DEFAULT_VALUE, feature.getDefValue() ) );
        }                
    }    
    
    public static void setAttributes( DB db, String pclient, ArrayList<PAttribute> attributes) {
        DBCollection featureCollection = db.getCollection(COLLECTION_ATTRIBUTES + "_" + pclient );        
        for( PAttribute attribute : attributes ) {           
            featureCollection.save(new BasicDBObject("_id", attribute.getName()).append(FEATURE_Attribute_DEFAULT_VALUE, attribute.getDefValue() ) );
        }                
    }    
}
