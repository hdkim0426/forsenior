package forsenior;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class connFindFromDB {
	//useUnicode=yes&characterEncoding=UTF-8
	  private final static String driverName = "com.mysql.jdbc.Driver";
	  private final static String url = "jdbc:mysql://127.0.0.1:3306/new_schema?";
	  private final static String id = "root";
	  private final static String password = "0000";

	  
	   public static Connection conn = null;
	   public static PreparedStatement pstmt = null;
	   public static ResultSet rs=null;
	   
	   public connFindFromDB(){
		   try {
			   Class.forName(driverName); 
			   conn=DriverManager.getConnection(url, id, password);
		   } catch (Exception e) {
			   e.printStackTrace();
			   throw new RuntimeException("DB 연결 오류");
		   }
	   }
	   public void getConnection(){
		   try {
			   Class.forName(driverName); 
			   conn=DriverManager.getConnection(url, id, password);
		   } catch (Exception e) {
			   e.printStackTrace();
			   throw new RuntimeException("DB 연결 오류");
		   }
	   }
	   
	   public void insertNewQuestion(List<String> texts){
		   String sql = "insert into new_schema.testtable(question) values(?)";  
		   try {
			   pstmt = conn.prepareStatement(sql);
			   pstmt.setString(1,texts.get(0));
			   pstmt.execute();
			   
			   conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			   e.printStackTrace();
			}

		    
	   }
	   
	   public List<List<String>> findReply(){
		   System.out.println("start select DB");
		   List<List<String>> relation=new ArrayList();
		   try {
			   
			   Statement cnt_state = conn.createStatement();
			   String sql = "select * from new_schema.testtable;"; 
			   pstmt = conn.prepareStatement(sql);
			   rs=pstmt.executeQuery();
			   
			   // DB 데이터 한 행씩 가져오기
			   while(rs.next()){                                                        
				   List<String> temp=new ArrayList();
				   String qs = rs.getString("question");
				   String ca = rs.getString("categories");
				   String r1 = rs.getString("reply1");
				   String r2 = rs.getString("reply2");
				   temp.add(qs);
				   temp.add(ca);
				   temp.add(r1);
				   temp.add(r2);
				   relation.add(temp);
			   }
			   rs.close();
			   conn.close();
			   
			   
				
		   } catch (SQLException e) {
				// TODO Auto-generated catch block
			   e.printStackTrace();
		   }
		   
		   return relation;
	   }
	   
	   public List<List<String>> questionFirst(){
		   System.out.println("start select DB");
		   List<List<String>> relation=new ArrayList();
		   try {
			   
			   Statement cnt_state = conn.createStatement();
			   String sql = "select * from new_schema.question;"; 
			   pstmt = conn.prepareStatement(sql);
			   rs=pstmt.executeQuery();
			   
			   // DB 데이터 한 행씩 가져오기
			   while(rs.next()){                                                        
				   List<String> temp=new ArrayList();
				   String qs = rs.getString("question");
				   temp.add(qs);
				   relation.add(temp);
			   }
			   rs.close();
			   conn.close();
	 
		   } catch (SQLException e) {
				// TODO Auto-generated catch block
			   e.printStackTrace();
		   }
		   
		   return relation;
	   }
	   
}
