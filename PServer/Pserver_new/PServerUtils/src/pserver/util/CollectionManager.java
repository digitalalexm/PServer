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
public class CollectionManager {
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
    
    public static void mergeOldWithNewAttributes( Collection<PAttribute> oldAttributes, Collection<PAttribute> newAttributes ) {                
        for( PAttribute oldAttr : oldAttributes ) {
            boolean found = false;
            for( PAttribute newAttribute : newAttributes ) {
                if( oldAttr.getName().equals(newAttribute.getName()) == true ) {
                    found = true;
                    break;
                }
            }
            if( found == false ) {
                newAttributes.add(oldAttr);
            }
        }        
    }
    
    public static void mergeOldWithNewFeatures( Collection<PFeature> oldFeatures, Collection<PFeature> newFeatures ) {
        for( PFeature oldFeature : oldFeatures ) {
            boolean found = false;
            for( PFeature newFeature : newFeatures ) {
                if( oldFeature.getName().equals(newFeature.getName()) == true ) {
                    found = true;
                    break;
                }
            }
            if( found == false ) {
                newFeatures.add(oldFeature);
            }
        }        
    }
}
