/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pserver.util;

/**
 *
 * @author alexm
 */
public class Validator {
    public static boolean isAValidAttributeName( String name )  {
        if( name.contains("$") || name.contains("*") || name.contains(":") ){
            return false;
        } else {
            return true;
        }
    }
    
    public static boolean isAValidFeatureName( String name )  {
        if( name.contains("$") || name.contains("*") || name.contains(":") ){
            return false;
        } else {
            return true;
        }
    }
    
    public static boolean isAValidStereotypeName( String name )  {
        if( name.contains("$") || name.contains("*") || name.contains(":") ){
            return false;
        } else {
            return true;
        }
    }
    
    public static boolean isInteger( String name ) {
        try{
            Long.parseLong(name);
            return true;
        } catch( NumberFormatException e) {
            return false;
        }
    }
    
    public static boolean isReal( String name ) {
        try{
            Double.parseDouble(name);
            return true;
        } catch( NumberFormatException e) {
            return false;
        }
    }

    public static String getAttributeInvalidCharachters() {
        return "'$',':',''*";
    }
}
