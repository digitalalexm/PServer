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
 * This class contains method that are useful for basic DAO operations like cleanup etc
 */
package pserver.data;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import java.util.ArrayList;

/**
 *
 * @author alexm
 */
public class GeneralDAO {
    
    public static void cleanUp( DBCollection collection, ArrayList<BasicDBObject> ids ) {
        for( BasicDBObject doc : ids ) {
            collection.remove(doc);
        }
    }
    
}
