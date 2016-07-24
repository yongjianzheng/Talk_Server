package com.yongjian.english_tranning_talk.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.yongjian.english_tranning_talk.bean.Asses;

public class AssesDao {
	
	private AssesDao(){
		
	}
	/*
	 * 根据教师id查找评价
	 */
	public static ArrayList<Asses> selectAssById(int id){
		ArrayList<Asses> list = new ArrayList<>();
		String sql0 = "use talk_db";
		String sql1 = "select * from asses where  t_id =?";
		Connection con = DBPool.getConnection();
		try {
			con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while(rs.next()){
				Asses asses = new Asses();
				asses.setT_id(rs.getInt("t_id"));
				asses.setT_name(rs.getString("t_name"));
				asses.setContent(rs.getString("content"));
				asses.setAssgrade(rs.getString("assgrade"));
				list.add(asses);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		DBPool.close(con);
		return list;
	}
	/*
	 * 生成评价
	 */
	public static void produceAsses(Asses asses){
		String sql0 = "use talk_db";
		String sql1 = "insert into asses(t_id,t_name,content,assgrade) value(?,?,?,?)";
		Connection con = DBPool.getConnection();
		try {
			con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		PreparedStatement ps;
		try {
			ps =con.prepareStatement(sql0);
			ps.execute();
			ps =con.prepareStatement(sql1);
			ps.setInt(1,asses.getT_id());
			ps.setString(2, asses.getT_name());
			System.out.println("name是"+asses.getT_name());
			ps.setString(3, asses.getContent());
			System.out.println("content是"+asses.getContent());
			ps.setString(4, asses.getAssgrade());
			System.out.println("grade是"+asses.getAssgrade());
			ps.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			try {
				System.out.println("插入数据库异常，正在进行回滚..");
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		DBPool.close(con);		
	}

}
