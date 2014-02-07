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

package pserver.pservlets;


import PServer.util.VectorMap;
import com.mongodb.DB;
 
public interface PService {    
    public abstract String getMimeType();
    //this method will be called when the PServlet will be loaded
    public abstract void init( String[] params ) throws Exception;
    //the method is the job that the servlet must do
    public abstract int service( String clientName, VectorMap parameters, String[][] response, DB dbAccess );
}
