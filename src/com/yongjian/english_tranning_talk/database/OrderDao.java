package com.yongjian.english_tranning_talk.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.yongjian.english_tranning_talk.bean.OrderList;
import com.yongjian.english_tranning_talk.bean.User;

public class OrderDao {
	
	private OrderDao(){
		
	}
	/*
	 * 生成订单
	 */
	public static void produceOrder(OrderList order){
		String sql0 = "use talk_db";
		String sql1 = "insert into orderlist(l_id,t_id,cost,date,duration) value(?,?,?,?,?)";
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
			ps.setInt(1, order.getL_id());
			ps.setInt(2, order.getT_id());
			ps.setString(3, order.getCost());
			System.out.println("cost 是"+order.getCost());
			ps.setString(4, order.getDate());
			System.out.println("date是"+order.getDate());
			ps.setString(5, order.getDuration());
			System.out.println("date 是"+order.getDuration());
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
	/*
	 * 查找订单
	 */
	public static ArrayList<OrderList> selectMyOrder(User user){
		ArrayList<OrderList> list = new ArrayList<>();
		String sql0 = "use talk_db";
		String sql1;
		if ("teacher".equals(user.getType())) {
			sql1 = "select * from orderlist  where  t_id = ?";
		} else
			sql1 = "select * from orderlist  where  l_id = ?";
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
			ps.setInt(1, user.getId());
			rs = ps.executeQuery();
			while(rs.next()){
				OrderList order = new OrderList();
				order.setT_id(rs.getInt("t_id"));
				order.setL_id(rs.getInt("l_id"));
				order.setCost(rs.getString("cost"));
				order.setDate(rs.getString("date"));
				order.setDuration(rs.getString("duration"));
				list.add(order);
			}
		}catch(SQLException e){
			System.out.println("查找失败");
			e.printStackTrace();
	    }
		DBPool.close(con);
		return list;
	}

}
