/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pserver.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import pserver.domain.PAttribute;
import pserver.domain.PFeature;

/**
 *
 * @author alexm
 */
public class CollectionCleaner {
    public static void cleanFeatureCollection( Collection<PFeature> features ) {
        Iterator<PFeature> iterator = features.iterator();
        LinkedList<String> featuresFound = new LinkedList<String>();
        while( iterator.hasNext() ) {
            PFeature ftr = iterator.next();
            if( featuresFound.contains(ftr.getName()) ) {
                iterator.remove();
            } else {
                featuresFound.add(ftr.getName());
            }
        }            
    }
    
    public static void cleanAttributesCollection( Collection<PAttribute> attributes ) {
        Iterator<PAttribute> iterator = attributes.iterator();
        LinkedList<String> attributesFound = new LinkedList<String>();
        while( iterator.hasNext() ) {
            PAttribute attr = iterator.next();
            if( attributesFound.contains(attr.getName()) ) {
                iterator.remove();
            } else {
                attributesFound.add(attr.getName());
            }
        }            
    }
}
