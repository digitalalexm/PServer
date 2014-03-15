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

import java.util.ArrayList;

/**
 *
 * @author alexm
 */
public class PStereotype {

    private String name;
    private ArrayList<PStereotypeRule> rules;
    private ArrayList<PSterUser> users;
    private ArrayList<PFeature> features;

    public PStereotype() {
        rules = new ArrayList<>();
    }            
    
    public void setName(String stereotypeName) {
        this.name = stereotypeName;
    }

    public String getName() {
        return name;
    }

    public ArrayList<PStereotypeRule> getRules() {
        return rules;
    }

    public void setRules(ArrayList<PStereotypeRule> rules) {
        this.rules = rules;
    }

    public ArrayList<PSterUser> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<PSterUser> users) {
        this.users = users;
    }

    public ArrayList<PFeature> getFeatures() {
        return features;
    }

    public void setFeatures(ArrayList<PFeature> features) {
        this.features = features;
    }

}
