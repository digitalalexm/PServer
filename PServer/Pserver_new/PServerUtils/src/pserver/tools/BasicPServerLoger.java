/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pserver.tools;

/**
 *
 * @author alexm
 */
public class BasicPServerLoger implements PServerLoger {

    @Override
    public void log(String msg) {
        System.out.println( msg );
    }

    @Override
    public void warn(String msg) {
        System.out.println( msg );
    }

    @Override
    public void error(String msg) {
        System.out.println( msg );
    }
    
}
