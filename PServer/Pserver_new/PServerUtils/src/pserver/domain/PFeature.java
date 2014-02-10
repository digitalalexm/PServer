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
public final class PFeature implements Comparable<PFeature>{
    
    private String name;    
    private double value;
    private double defValue;

    public PFeature() {
        this.name = null;
        this.value = this.defValue = 0.0f;        
    }

    public PFeature( String name, double value, double defValue ) {        
        this.name = name;
        this.setValue( value );
        this.setDefValue( defValue );
    }
    
    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue( double value ) {
        this.value = value;        
    }

    public double getDefValue() {
        return defValue;
    }

    public void setDefValue( double defValue ) {
        this.defValue = defValue;
    }

    @Override
    public int compareTo(PFeature o) {
        if( o.getValue() > this.getValue()) {
            return -1;
        } else if( o.getValue() < this.getValue()) {
            return 1;
        } else {
            return 0;
        }
    }
}
