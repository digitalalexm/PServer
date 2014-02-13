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
 * This class contains the global parameters that are needed to parameterize the algorithms and the API
 */
package pserver.parameters;

import pserver.tools.BasicPServerLoger;

/**
 *
 * @author alexm
 */
public class Parameters {
    /**
     * This number defines how much features will be stored in every preference document
     */    
    public static int NUM_OF_FEATURES_PER_PROFILE = 10000;
    /**
     * This is the number of times that the system will try to write something into the database
     * if an error occures
     */
    public static int WRITE_TRIES = 10;
    public static BasicPServerLoger logger = new BasicPServerLoger();    
}
