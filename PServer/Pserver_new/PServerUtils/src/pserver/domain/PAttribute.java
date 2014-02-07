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

/**
 *
 * @author alexm
 */
public class PAttribute {

    private String name;
    private String value;
    private String defValue;

    public PAttribute() {
        name = null;
        value = defValue = null;        
    }

    public PAttribute( String name, String defValue, String value ) {
        this.name = name;
        this.value = value;
        this.defValue = defValue;
    }
    
    public PAttribute( String name, String value ) {
        this.name = name;
        this.value = defValue = value;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue( String value ) {
        this.value = value;
    }

    public String getDefValue() {
        return defValue;
    }

    public void setDefValue( String defValue ) {
        this.defValue = defValue;
    }
}
