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
 * This class contains method that are useful for basic DAO operations like
 * cleanup etc
 */
package pserver.data;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import java.util.ArrayList;
import static pserver.data.PUserDAO.COLLECTION_USER_PROFILES;
import static pserver.data.PUserDAO.PREFERENCE_DOCUMENT_FEATURES;
import static pserver.data.PUserDAO.PREFERENCE_DOCUMENT_FEATURE_NAME;
import static pserver.data.PUserDAO.PREFERENCE_DOCUMENT_FEATURE_VALUE;
import static pserver.data.PUserDAO.PREFERENCE_DOCUMENT_ID_NAME;

/**
 *
 * @author alexm
 */
public class GeneralDAO {

    public static void cleanUp(DBCollection collection, ArrayList<BasicDBObject> ids) {
        for (BasicDBObject doc : ids) {
            collection.remove(doc);
        }
    }

    public static DBCollection getCollection(DB db, String pclient, String collectionName) {
        DBCollection collection = db.getCollection(collectionName + "_" + pclient);
        if (collection.getIndexInfo().isEmpty() == true) {
            createCollectionIndeces(collectionName, collection);
        }
        return collection;
    }

    private static void createCollectionIndeces(String collectionName, DBCollection collection) {
        if (collectionName.equals(COLLECTION_USER_PROFILES)) {
            CreateUserCollectionIndeces(collection);
        } else {
            System.out.println("no indeces for " + collectionName);
        }
    }

    private static void CreateUserCollectionIndeces(DBCollection collection) {
        collection.createIndex(new BasicDBObject("_id." + PREFERENCE_DOCUMENT_ID_NAME, 1));
        collection.createIndex(new BasicDBObject(PREFERENCE_DOCUMENT_FEATURES + "." + PREFERENCE_DOCUMENT_FEATURE_NAME, 1));
        collection.createIndex(new BasicDBObject(PREFERENCE_DOCUMENT_FEATURES + "." + PREFERENCE_DOCUMENT_FEATURE_VALUE, 1));
    }
}
