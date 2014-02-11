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

import com.mongodb.DB;

/**
 *
 * the default document for the features is { _id:<feature_name>,
 * def_value:<default value> } the default document for the attributes is {
 * _id:<attribute_name>, def_value:<default value> }
 *
 * @author alexm
 */
public class SessionDAO {

    public static long getLastSession(DB db, String clientName, String userName) {        
        return 0;
    }
    
}
