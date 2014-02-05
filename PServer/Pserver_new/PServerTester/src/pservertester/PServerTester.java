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
        for( int i = 0 ; i < 100000; i ++ ) {
            prefernces.add( new PFeature( "ftr" + i, (float)Math.random(), 0.0f ));
        }
        user.setPreferences(prefernces);
        //PUserDao.SetUserProfile(db, "some_client", user );
        
        DBCollection col = db.getCollection(PUserDao.COLLECTION_USER_PROFILES + "_" + "some_client");
        DBObject obj = col.findOne( );
        BasicDBObject id = new BasicDBObject((Map) col.findOne( null, new BasicDBObject("_id", 1) ));
        BasicDBObject idExt = id.append("features.name", "ftr1");
        BasicDBObject FeatureValue = new BasicDBObject("features", new BasicDBObject("name","ftr2"));
        //BasicDBObject incFeatureValue = new BasicDBObject("$inc", FeatureValue);
        //BasicDBObject pullFeatureValue = new BasicDBObject("$pull", FeatureValue);
        System.out.println( obj.toString());
        //System.out.println( id.toString());        
        GeneralPreferenceDAO.removePreferences(col, "some_user", 0);
        int numOfDocs = GeneralPreferenceDAO.getNumberOfPreferenceDocuments(col, "some_user");
        System.out.println( numOfDocs  );
        //col.update(idExt, pullFeatureValue);
        /*System.out.println( idExt.toString());
        System.out.println(pullFeatureValue.toString());        
        col.findOne(id);*/        
        //PUserDao.getUserProfile(db, "some_client", user.getName() );
    }
}
