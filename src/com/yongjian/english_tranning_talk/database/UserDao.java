package com.yongjian.english_tranning_talk.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.yongjian.english_tranning_talk.bean.Asses;
import com.yongjian.english_tranning_talk.bean.OrderList;
import com.yongjian.english_tranning_talk.bean.User;
import com.yongjian.english_tranning_talk.database.DBPool;

public class UserDao {
	
	private UserDao(){
		
	}

	/*
	 * 查询手机号是否已被注册
	 */
	public static boolean isRegister(String phone){
		boolean result = false;
		String sql0 = "use talk_db";
		String sql1 = "select * from user where phonenum=?";
		Connection connection = DBPool.getConnection();
		try {
			connection.setAutoCommit(false);
			PreparedStatement ps;
			ps = connection.prepareStatement(sql0);
			ps.execute();
			ps =connection.prepareStatement(sql1);
			ps.setString(1,phone);
			ResultSet resultSet = ps.executeQuery();
			result= resultSet.first()?true:false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DBPool.close(connection);
		return result;
	}
	/*
	 * 向数据库添加用户
	 */
	public static int insertInfo(User user) {
		int id = -1;
		String type=user.getType();
		String sql0 = "use talk_db";
		String sql1;
		System.out.println(type);
		if("learner"==type){
			sql1 = "insert into user(name,phonenum,pwd,sex,type)"+
		          "values(?,?,?,?,?)";
			Connection connection=DBPool.getConnection();
			try {
				connection.setAutoCommit(false);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			PreparedStatement ps;
			try {
				ps = connection.prepareStatement(sql0);
				ps.execute();
				ps = connection.prepareStatement(sql1);
				ps.setString(1, user.getName());
				System.out.println(user.getName());
				ps.setString(2, user.getPhnum());
				ps.setString(3, user.getPwd());
				System.out.println(user.getPwd());
				ps.setString(4, user.getSex());
				ps.setString(5, user.getType());
				ps.executeUpdate();
				connection.commit();
				id = 0;
			} catch (SQLException e) {
				try {
					System.out.println("数据库异常，正在回滚");
					connection.rollback();
					id = -1;
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}else{
			sql1 ="insert into user(name,phonenum,pwd,sex,type,grade)"+
		    "value(?,?,?,?,?,?)";
			Connection connection=DBPool.getConnection();
		    try {
			connection.setAutoCommit(false);
		    }catch (SQLException e) {
			e.printStackTrace();
		    }
		    PreparedStatement ps;
		    try {
		    	ps = connection.prepareStatement(sql0);
		    	ps.execute();
		    	ps = connection.prepareStatement(sql1);
		    	ps.setString(1, user.getName());
		    	System.out.println(user.getName());
		    	ps.setString(2, user.getPhnum());
		    	ps.setString(3, user.getPwd());
		    	System.out.println(user.getPwd());
		    	ps.setString(4, user.getSex());
		    	ps.setString(5, user.getType());
		    	ps.setString(6, user.getGrade());
		    	ps.executeUpdate();
		    	connection.commit();
		    	id = 0;
		    } catch (SQLException e) {
		    	try {
				System.out.println("数据库异常，正在回滚");
				connection.rollback();
				id = -1;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		}
		return id;
	}
	/*
	 * 获得最后一次插入的值
	 */
	public static int getLastId(Connection con){
		String sql0 = "use talk_db";
		String sql1 = "select MAX(id) as ID from user";// 注意:使用MAX(ID) 必须加上 as
														// id 翻译
		PreparedStatement ps;
		ResultSet rs;
		int id = -1;
		try {
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			rs = ps.executeQuery();
			if (rs.first())
				id = rs.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DBPool.close(con);
		return id;
	}
	/*
	 * 进行登录的验证
	 */
	public static boolean login(User user){
		boolean isExisted = false;
		String sql0 = "use talk_db";
		String sql1 = "select * from user where phonenum=? and pwd=?";
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			System.out.println(user.getPhnum());
			System.out.println(user.getPwd());
			ps.setString(1, user.getPhnum());
			ps.setString(2, user.getPwd());
			rs =ps.executeQuery();
			if(rs.next()){
				isExisted = true;
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setSex(rs.getString("sex"));
				System.out.println(user.getSex());
				user.setWallet(rs.getString("wallet"));
				user.setType(rs.getString("type"));
				System.out.println(user.getType());
				if("teacher".equals(user.getType())){
					user.setGrade(rs.getString("grade"));
					user.setHourypay(rs.getString("hourypay"));
				}
			}
			System.out.println("用户名是"+user.getName());
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		DBPool.close(con);
		return isExisted;
	}
	/*
	 * 更新在线状态
	 */
	public static void updateIsOnline(int id, int isOnline){
		String sql0 = "use talk_db";
		String sql1 = "update user set isOnline=? where id=?";
		Connection con = DBPool.getConnection();
		try {
			con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, isOnline);
			ps.setInt(2, id);
			ps.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			try {
				System.out.println("数据库正在回滚....");
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		DBPool.close(con);
	}
	
	/*
	 * 查找不同等级的老师
	 */
	public static ArrayList<User> selectTeacherByType(String grade){
		ArrayList<User> list = new ArrayList<User>();
		String sql0 = "use talk_db";
		String sql1 = "select * from user where  isOnline = 1 and grade = ?";
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
			ps.setString(1, grade);
			rs = ps.executeQuery();
			while(rs.next()){
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setGrade(rs.getString("grade"));
				user.setName(rs.getString("name"));
				user.setPhnum(rs.getString("phonenum"));
				user.setSex(rs.getString("sex"));
				user.setType(rs.getString("type"));
				user.setPwd(rs.getString("pwd"));
				user.setHourypay(rs.getString("hourypay"));
				list.add(user);
			}
		}catch(SQLException e){
			System.out.println("查找不同老师失败");
			e.printStackTrace();
		}
		DBPool.close(con);
		return list;
	}
}
