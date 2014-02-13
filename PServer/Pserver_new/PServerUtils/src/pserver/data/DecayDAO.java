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

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import pserver.domain.PDecayData;
import pserver.domain.PNumData;

public class DecayDAO {
    public static final String COLLECTION_DDT = "ddt";    
    public static final String COLLECTION_NDT = "ndt";
    public static final String DECAY_DOCUMENT_ID_USER = "name";
    public static final String DECAY_DOCUMENT_ID_SID = "sid";
    public static final String DECAY_DOCUMENT_FEATURES = "features";
    public static final String DECAY_DOCUMENT_FEATURES_NAME = "name";
    public static final String DECAY_DOCUMENT_FEATURES_VALUE = "value";
    public static final String DECAY_DOCUMENT_FEATURES_TIMESTAMP = "timestamp";    
    
    public static long getLastDDTSession( DB db, String pclient, String userName) {
        DBCollection collection = GeneralDAO.getCollection(db, pclient, COLLECTION_DDT);
        BasicDBObject firstOp = new BasicDBObject("$group", new BasicDBObject("_id","$_id."+DECAY_DOCUMENT_ID_USER)
                .append("sid", new BasicDBObject("$max","$_id."+DECAY_DOCUMENT_ID_SID)));        
        AggregationOutput output = collection.aggregate(firstOp);
        Iterable<DBObject> it = output.results();
        Iterator<DBObject> iit = it.iterator();
        if (iit.hasNext() == true) {
            DBObject doc = iit.next();
            long lastSid = (Long) doc.get("sid");
            return lastSid;
        }
        return 0L;
    }
    
    public static void addDecayData(DB db, String pclient, String userName, final List<PDecayData> ddts ) {
        DBCollection collection = GeneralDAO.getCollection(db, pclient, COLLECTION_DDT);
                
        for( PDecayData dd : ddts ){
            BasicDBObject docID = new BasicDBObject("_id."+ DECAY_DOCUMENT_ID_USER, dd.getUserName()).append("_id."+DECAY_DOCUMENT_ID_SID, dd.getSessionId());
            BasicDBObject docData = new BasicDBObject(DECAY_DOCUMENT_FEATURES_NAME,dd.getFeature()).append(DECAY_DOCUMENT_FEATURES_TIMESTAMP, dd.getTimeStamp());
            BasicDBObject docUpdate = new BasicDBObject("$addToSet", new BasicDBObject(DECAY_DOCUMENT_FEATURES, docData ));
            GeneralDAO.update(collection, docID, docUpdate, true, false);
        }
    }

    public static Long getLastNumDataSession(DB db, String pclient, String userName) {
        DBCollection collection = GeneralDAO.getCollection(db, pclient, COLLECTION_NDT);
        BasicDBObject firstOp = new BasicDBObject("$group", new BasicDBObject("_id","$_id."+DECAY_DOCUMENT_ID_USER)
                .append("sid", new BasicDBObject("$max","$_id."+DECAY_DOCUMENT_ID_SID)));        
        AggregationOutput output = collection.aggregate(firstOp);
        Iterable<DBObject> it = output.results();
        Iterator<DBObject> iit = it.iterator();
        if (iit.hasNext() == true) {
            DBObject doc = iit.next();
            long lastSid = (Long) doc.get("sid");
            return lastSid;
        }
        return 0L;
    }
    
    
    public static void addNumData(DB db, String clientName, String pclient, LinkedList<PNumData> numData) {
        DBCollection collection = GeneralDAO.getCollection(db, pclient, COLLECTION_NDT);
                
        for( PNumData nd : numData ){
            BasicDBObject docID = new BasicDBObject("_id."+ DECAY_DOCUMENT_ID_USER, nd.getUser()).append("_id."+DECAY_DOCUMENT_ID_SID, nd.getSessionId());
            BasicDBObject docData = new BasicDBObject(DECAY_DOCUMENT_FEATURES_NAME,nd.getFeature())
                    .append(DECAY_DOCUMENT_FEATURES_VALUE, nd.getFeatureValue()).append(DECAY_DOCUMENT_FEATURES_TIMESTAMP, nd.getTimeStamp());
            BasicDBObject docUpdate = new BasicDBObject("$addToSet", new BasicDBObject(DECAY_DOCUMENT_FEATURES, docData ));
            GeneralDAO.update(collection, docID, docUpdate, true, false);
        }
    }

}
