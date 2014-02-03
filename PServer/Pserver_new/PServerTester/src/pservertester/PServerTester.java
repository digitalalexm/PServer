/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pservertester;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.util.ArrayList;
import pserver.data.PUserDao;
import pserver.domain.PFeature;
import pserver.domain.PUser;

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
        
        PUser user = new PUser();
        user.setName("some_user");
        ArrayList<PFeature> prefernces = new ArrayList<PFeature>();
        for( int i = 0 ; i < 100000; i ++ ) {
            prefernces.add( new PFeature( "ftr" + i, (float)Math.random(), 0.0f ));
        }
        user.setPreferences(prefernces);
        //PUserDao.SetUserProfile(db, "some_client", user );
        PUserDao.getUserProfile(db, "some_client", user.getName() );
    }
}
