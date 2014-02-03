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

package pserver.domain;

import java.util.*;

public class PUser {

    private String name;
    private ArrayList<PAttribute> attributes;
    private ArrayList<PFeature> preferences;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<PAttribute> getAttributes() {
        return attributes;
    }
    public void setAttributes(ArrayList<PAttribute> attributes) {
        this.attributes = attributes;
    }
    public ArrayList<PFeature> getPreferences() {
        return preferences;
    }
    public void setPreferences(ArrayList<PFeature> preferences) {
        this.preferences = preferences;
    }
}
