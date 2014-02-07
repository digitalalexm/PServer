/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pserver.pservlets;

/**
 *
 * @author alexm
 */
public class PThread extends Thread {
    private float completionPercent;
    
    public PThread() {
        completionPercent = 0;
    }
    
    public PThread( Runnable runnable) {
        this();
    }
}
