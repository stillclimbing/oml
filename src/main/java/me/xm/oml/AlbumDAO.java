package me.xm.oml;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AlbumDAO {

	private static final String INSERT = "INSERT INTO album"
			+ "(pathHash,folderName,name,artist,label,coverThumbFile,coverFile,discId,tags,description,totalDisc,lastModifiedTime)"
			+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
	private static final java.lang.String GET_ALL = "select * from album order by lastModifiedTime desc";


	private static final java.lang.String REMOVE_PATH = "delete from album where pathHash=?";

	public static long insertAlbum(Album item) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			if (conn == null) {
				return -1;
			}

			pstmt = conn.prepareStatement(INSERT);
			int i = 0;
			pstmt.setLong(++i, item.pathHash);
			pstmt.setString(++i, item.folderName);
			pstmt.setString(++i, item.name);
			pstmt.setString(++i, item.artist);
			pstmt.setString(++i, item.label);
			pstmt.setString(++i, item.coverThumbFile);
			pstmt.setString(++i, item.coverFile);
			pstmt.setString(++i, item.discId);
			pstmt.setString(++i, item.getTagsStr());
			pstmt.setString(++i, item.getDesc());
			pstmt.setInt(++i, item.getTotalDiscs());
			pstmt.setLong(++i, item.getLastModified());

			int intResult = pstmt.executeUpdate();
			//logger.info("record inserted in db: {}");
			return intResult;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} finally {
			cleanUp(null, pstmt, conn);
		}
	}

	public static List<Album> getAll() throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Album> albums = new ArrayList<Album>();
		try {
			conn = getConnection();
			if (conn == null) {
				return null;
			}

			pstmt = conn.prepareStatement(GET_ALL);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Album item = new Album();
				item.pathHash = rs.getLong("pathHash");
				item.folderName = rs.getString("folderName");
				item.name = rs.getString("name");
				item.artist = rs.getString("artist");
				item.label = rs.getString("label");
				item.coverThumbFile = rs.getString("coverThumbFile");
				item.coverFile = rs.getString("coverFile");
				item.discId = rs.getString("discId");
				item.setTagsStr(rs.getString("tags"));
				item.setDesc(rs.getString("description"));
				item.setTotalDiscs(rs.getInt("totalDisc"));
				albums.add(item);
			}
			return albums;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			cleanUp(rs, pstmt, conn);
		}
	}

	public static int deleteByPathHash(long pathHash) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			if (conn == null) {
				return -1;
			}

			pstmt = conn.prepareStatement(REMOVE_PATH);
			pstmt.setLong(1, pathHash);
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} finally {
			cleanUp(null, pstmt, conn);
		}
	}

	private static Connection getConnection() throws Exception {
		try {
			Class.forName("org.gjt.mm.mysql.Driver");
			return DriverManager.getConnection(
					"jdbc:mysql://192.168.1.2:3306/xm", "root", "xm801124");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void cleanUp(ResultSet rs, Statement stmt, Connection conn) {
		try {
			if (rs != null)
				rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
