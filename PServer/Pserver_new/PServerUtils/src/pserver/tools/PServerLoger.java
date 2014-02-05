/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pserver.tools;

/**
 *
 * @author alexm
 */
public interface PServerLoger {
    public void log( String msg );
    public void warn( String msg );
    public void error( String msg );
}
