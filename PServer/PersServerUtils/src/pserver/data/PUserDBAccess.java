/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pserver.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import pserver.domain.PUser;

/**
 *
 * @author alexm
 */
public class PUserDBAccess {

    DBAccess dbAccess;

    public PUserDBAccess(DBAccess db) {
        dbAccess = db;
    }

    public PUser getUserProfile(String userName, String[] featureTypes, String clientName) throws SQLException {
        PUser user = new PUser(userName);
        String sql = "SELECT * FROM " + DBAccess.UPROFILE_TABLE + " WHERE " + DBAccess.UPROFILE_TABLE_FIELD_USER + "=? AND " + DBAccess.FIELD_PSCLIENT + "='" + clientName + "'";
        if (featureTypes != null) {
            for (int i = 0; i < featureTypes.length; i++) {
                sql += " AND " + DBAccess.UPROFILE_TABLE_FIELD_FEATURE + " LIKE ?";
            }
        }
        PreparedStatement usrStmt = dbAccess.getConnection().prepareStatement(sql);
        usrStmt.setString(1, userName);
        if (featureTypes != null) {
            for (int i = 0; i < featureTypes.length; i++) {
                usrStmt.setString(i + 2, featureTypes[i]);
            }
        }

        ResultSet rs = usrStmt.executeQuery();
        while (rs.next()) {
            user.setFeature(rs.getString(DBAccess.UPROFILE_TABLE_FIELD_FEATURE), rs.getFloat(DBAccess.UPROFILE_TABLE_FIELD_VALUE));
        }
        rs.close();
        usrStmt.close();

        sql = "SELECT * FROM " + DBAccess.FEATURE_STATISTICS_TABLE + " WHERE " + DBAccess.FIELD_PSCLIENT + "='" + clientName + "' AND " + DBAccess.FEATURE_STATISTICS_TABLE_FIELD_USER + "=? AND " + DBAccess.FEATURE_STATISTICS_TABLE_FIELD_TYPE + "=" + DBAccess.STATISTICS_FREQUENCY;
        PreparedStatement selectFtrFreq = dbAccess.getConnection().prepareStatement(sql);

        Hashtable<String, Float> freqs = new Hashtable<String, Float>();
        selectFtrFreq.setString(1, userName );
        rs = selectFtrFreq.executeQuery();
        while (rs.next()) {
            freqs.put( rs.getString( DBAccess.FEATURE_STATISTICS_TABLE_FIELD_FEATURE ), rs.getFloat(DBAccess.FEATURE_STATISTICS_TABLE_FIELD_VALUE) );
        }
        user.setFtrReqs(freqs);       
        rs.close();
        selectFtrFreq.close();

        sql = "SELECT * FROM " + DBAccess.UFTRASSOCIATIONS_TABLE + " WHERE " + DBAccess.FIELD_PSCLIENT + "='" + clientName + "' AND " + DBAccess.UFTRASSOCIATIONS_TABLE_FIELD_USR + "=? AND " + DBAccess.UFTRASSOCIATIONS_TABLE_FIELD_TYPE + "=" + DBAccess.RELATION_SIMILARITY;
        PreparedStatement assocsStmt = dbAccess.getConnection().prepareStatement(sql);

        Hashtable<Set<String>, Float> assoces = new Hashtable<Set<String>, Float>();
        assocsStmt.setString(1, userName );
        rs = assocsStmt.executeQuery();
        while (rs.next()) {
            String ftr1 = rs.getString(DBAccess.UFTRASSOCIATIONS_TABLE_FIELD_DST);
            String ftr2 = rs.getString(DBAccess.UFTRASSOCIATIONS_TABLE_FIELD_SRC);
            float val = rs.getFloat(DBAccess.UFTRASSOCIATIONS_TABLE_FIELD_WEIGHT );
            Set<String> ftrSet = new HashSet<String>(2);
            ftrSet.add(ftr1);
            ftrSet.add(ftr2);
            assoces.put( ftrSet, val);
        }
        user.setFtrAssocs(assoces);
        rs.close();
        assocsStmt.close();
        return user;
    }

    VectorResultSet getVectorResultSetOfUsers(String user1, String user2, String clientName) throws SQLException {
        String sql1 = "SELECT " + DBAccess.UPROFILE_TABLE_FIELD_FEATURE + ", " + DBAccess.UPROFILE_TABLE_FIELD_NUMVALUE + " FROM " + DBAccess.UPROFILE_TABLE + " WHERE up_user = '"
                + user1 + "' AND " + DBAccess.UPROFILE_TABLE_FIELD_FEATURE + " in " + "(SELECT " + DBAccess.UPROFILE_TABLE_FIELD_FEATURE + " FROM "
                + DBAccess.UPROFILE_TABLE + " WHERE " + DBAccess.FIELD_PSCLIENT + " = '" + clientName + "' ) AND " + DBAccess.FIELD_PSCLIENT + "='" + clientName + "'";

        String sql2 = "SELECT uf_feature AS up_feature, uf_numdefvalue AS up_numvalue FROM up_features WHERE uf_feature NOT IN " + " ( SELECT up_feature FROM user_profiles WHERE up_user = '" + user1 + "' AND FK_psclient = '" + clientName + "') AND FK_psclient = '" + clientName + "'";

        String queryProfile1 = "(" + sql1 + ") UNION (" + sql2 + ") ORDER BY " + DBAccess.UPROFILE_TABLE_FIELD_FEATURE;

        sql1 = "SELECT " + DBAccess.UPROFILE_TABLE_FIELD_FEATURE + ", " + DBAccess.UPROFILE_TABLE_FIELD_NUMVALUE + " FROM " + DBAccess.UPROFILE_TABLE + " WHERE up_user = '"
                + user2 + "' AND " + DBAccess.UPROFILE_TABLE_FIELD_FEATURE + " in " + "(SELECT " + DBAccess.UPROFILE_TABLE_FIELD_FEATURE + " FROM "
                + DBAccess.UPROFILE_TABLE + " WHERE " + DBAccess.FIELD_PSCLIENT + " = '" + clientName + "' ) AND " + DBAccess.FIELD_PSCLIENT + "='" + clientName + "'";

        sql2 = "SELECT uf_feature AS up_feature, uf_numdefvalue AS up_numvalue FROM up_features WHERE uf_feature NOT IN " + " ( SELECT up_feature FROM user_profiles WHERE up_user = '" + user2 + "' AND FK_psclient = '" + clientName + "') AND FK_psclient = '" + clientName + "'";

        String queryProfile2 = "(" + sql1 + ") UNION (" + sql2 + ") ORDER BY " + DBAccess.UPROFILE_TABLE_FIELD_FEATURE;

        String sql = "SELECT u1." + DBAccess.UPROFILE_TABLE_FIELD_NUMVALUE + ", u2." + DBAccess.UPROFILE_TABLE_FIELD_NUMVALUE
                + " FROM ( " + queryProfile1 + " ) AS u1, (" + queryProfile2 + " ) AS u2 WHERE u1." + DBAccess.UPROFILE_TABLE_FIELD_FEATURE + " = u2." + DBAccess.UPROFILE_TABLE_FIELD_FEATURE + " AND ( u1.up_numvalue > 0 OR u2.up_numvalue > 0 )";

        //System.out.println( "Sql = " + sql );

        PServerResultSet rs = dbAccess.executeQuery(sql);

        return new VectorResultSet(rs);
    }
}