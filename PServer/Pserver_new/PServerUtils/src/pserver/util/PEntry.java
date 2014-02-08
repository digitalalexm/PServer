/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pserver.util;

import java.util.Map;

/**
 *
 * @author alexm
 */
public class PEntry<K, V> implements Map.Entry<K, V> {
    private K key;
    private V value;

    public PEntry() {
        key = null;
        value = null;
    }
    
    public PEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    
    public void setKey( K key ) {
        this.key = key;
    }
    
    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        return old;
    }    
}
