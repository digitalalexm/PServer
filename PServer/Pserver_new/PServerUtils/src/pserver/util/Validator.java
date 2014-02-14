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
        if( name.contains("$") || name.contains("*") || name.contains(":") || name.contains(";") ){
            return false;
        } else {
            return true;
        }
    }
    
    public static boolean isAValidFeatureName( String name )  {
        if( name.contains("$") || name.contains("*") || name.contains(":") || name.contains(";") ){
            return false;
        } else {
            return true;
        }
    }
    
    public static boolean isAValidStereotypeName( String name )  {
        if( name.contains("$") || name.contains("*") || name.contains(":") || name.contains(";") ){
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
        return "'$',':','*',';'";
    }

    public static boolean isAValidRule(String rule) {
        String subRules[] = rule.split(";");
        for( int i = 0 ; i < subRules.length; i ++){
            String subRule = subRules[i];
            String[] subRuleParts = subRule.split("$");
            if( subRuleParts.length < 3 || subRuleParts.length % 2 != 1 ) {
                return false;
            }
        }
        return true;
    }
}
