/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pservertester;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;
import pserver.data.GeneralPreferenceDAO;
import pserver.data.PUserDao;
import pserver.domain.PFeature;
import pserver.domain.PUser;
import pserver.parameters.Parameters;

/**
 *
 * @author alexm
 */
public class PServerTester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnknownHostException {
        MongoClient mclient = new MongoClient();
        DB db = mclient.getDB( "mydb" );
        
        Parameters.NUM_OF_FEATURES_PER_PROFILE = 1000;
        PUser user = new PUser();
        user.setName("some_user");
        ArrayList<PFeature> prefernces = new ArrayList<PFeature>();
        for( int i = 0 ; i < 10000000; i ++ ) {
            prefernces.add( new PFeature( "ftr" + i, (float)Math.random(), 0.0f ));
        }
        user.setPreferences(prefernces);
        PUserDao.SetUserProfile(db, "some_client", user );
        
        DBCollection col = db.getCollection(PUserDao.COLLECTION_USER_PROFILES + "_" + "some_client");
        DBObject obj = col.findOne( new BasicDBObject("_id", new BasicDBObject("name","some_user").append("idx", 0)));
        
        System.out.println( obj.toString()  );
        
        //=====================update preferences
        ArrayList<PFeature> updatePrefernces = new ArrayList<PFeature>();
        for( int i = prefernces.size()/2 ; i < prefernces.size() + 5; i ++ ) {
            if( i % 2 == 0 ){
                continue;
            }
            prefernces.add( new PFeature( "ftr" + i, (float)Math.random(), 0.0f ));
        }
        
        PUserDao.updateUserProfile(db, "some_client", user.getName(), updatePrefernces, true );
        
        obj = col.findOne( new BasicDBObject("_id", new BasicDBObject("name","some_user").append("idx", 0)));        
        System.out.println( obj.toString()  );
    }
}
