package studentdemo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import java.sql.CallableStatement;
public class gsqlCon{
  //创建数据库连接。
  public static Connection GetCon() {
	String driver = "org.postgresql.Driver";
    String sourceURL = "jdbc:postgresql://192.168.56.129:26000/student_mis";
    Connection conn = null;
    try {
      //加载数据库驱动。
      Class.forName(driver).newInstance();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    try {
      //创建数据库连接。
      System.out.println("Connection start!");
      conn = DriverManager.getConnection(sourceURL, "zjutuser", "Bigdata@123");
      System.out.println("Connection succeed!");
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return conn;
  };  
  //执行普通SQL语句。
  public static int gsqlexc(Connection conn,String sql) {
    Statement stmt = null;
    int rc = 0;
    try {
      stmt = conn.createStatement();
      //执行普通SQL语句。
      rc = stmt.executeUpdate(sql);
      stmt.close();
      conn.close();
    } catch (SQLException e) {
      if (stmt != null) {
        try {
          stmt.close();
          conn.close();
          JOptionPane.showMessageDialog(null, "不能做该操作！","提示消息",JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e1) {
          e1.printStackTrace();
        }
      }
      e.printStackTrace();
    }
    return rc;
  }
  //执行普通SQL查询语句。
  public static ResultSet gsqlquery(Connection conn,String sql) throws SQLException {
	    Statement stmt = null;
	    ResultSet rs = null;
	    try {
	      stmt = conn.createStatement();
	      //执行普通SQL语句。
	      System.out.print("开始启动查询操作！");
	      rs  = stmt.executeQuery(sql);
	      System.out.print("完成查询操作！");
	    } catch (SQLException e) {
	      System.out.print("未能完成查询操作！");
	      stmt.close();
	      conn.close();
	      JOptionPane.showMessageDialog(null, "不能做该操作！","提示消息",JOptionPane.WARNING_MESSAGE);
	    } 
	    conn.close();
	    return  rs ;
 };
//创建Depts表查询语句
public static ResultSet gsqldep(Connection conn) throws SQLException {
	   Statement stmt = null;
	   ResultSet rs = null;
	   try {
		  stmt = conn.createStatement();
		  System.out.print("开始启动查询操作！");
		  rs  = stmt.executeQuery("select * from Depts");
	  } catch (SQLException e) {
		      
		   System.out.print("未能完成查询操作！");
		   stmt.close();
		   conn.close();
		   JOptionPane.showMessageDialog(null, "不能做该操作！","提示消息",JOptionPane.WARNING_MESSAGE);
	} 
System.out.print("结束查询操作！");
	    if(rs==null) {System.out.print("无数据！");}
	    conn.close();
	    return  rs ;
 };	 
//创建Students表查询语句
public static ResultSet gsqlstu(Connection conn) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			//执行普通SQL语句。
			System.out.print("开始启动查询操作！");
			rs  = stmt.executeQuery("select * from Students");
		 } catch (SQLException e) {
			System.out.print("未能完成查询操作！");
			stmt.close();
			conn.close();
			JOptionPane.showMessageDialog(null, "不能做该操作！","提示消息",JOptionPane.WARNING_MESSAGE);
		 } 
		System.out.print("结束查询操作！");
		if(rs==null) {System.out.print("无数据！");}
		conn.close();
		return  rs ;
};	
//创建Courses表查询语句  
public static ResultSet gsqlcour(Connection conn) throws SQLException {
		Statement stmt = null;
	    ResultSet rs = null;
		try {
		    stmt = conn.createStatement();
		    System.out.print("开始启动查询操作！");
	        rs  = stmt.executeQuery("select * from Courses");
		} catch (SQLException e) {
		     System.out.print("未能完成查询操作！");
		     stmt.close();
		     conn.close();
		     JOptionPane.showMessageDialog(null, "不能做该操作！","提示消息",JOptionPane.WARNING_MESSAGE);
	    } 
		System.out.print("结束查询操作！");
	    if(rs==null) {System.out.print("无数据！");}
	    conn.close();
		return  rs ;
};	 
//创建Students、Courses表查询语句
public static ResultSet gsqlstu_cour(Connection conn) throws SQLException {
		Statement stmt = null;
	    ResultSet rs = null;
		try {
		    stmt = conn.createStatement();
			System.out.print("开始启动查询操作:---------------");
		    rs  = stmt.executeQuery("select Students.Sno,Students.Sname,Courses.Cno,Courses.Cname from Students,Courses;");	  
	    } catch (SQLException e) {
		    System.out.print("gsstu_cour查询异常！");
			stmt.close();
			conn.close();
			JOptionPane.showMessageDialog(null, "不能做该操作！","提示消息",JOptionPane.WARNING_MESSAGE);
	   } 
	   System.out.print("gsstu_cour结束查询操作！");
	   if(rs==null) {System.out.print("无数据！");}
	   conn.close();
	   return  rs ;
	};	  
//执行存储过程。
public static int ExecCallableSQL(Connection conn, String end_date,int grade) {
	   CallableStatement cstmt = null;
	   int cscount=1;
	   try {
	    	   cstmt=conn.prepareCall("{CALL sp_delete_graduate(?,?)}");
	      	cstmt.setString(1, end_date);
	        cstmt.setInt(2, grade);
	        cstmt.execute();
	        if(cstmt==null)
	    	       JOptionPane.showMessageDialog(null, "无记录！","提示消息",JOptionPane.WARNING_MESSAGE);
	        cstmt.close();
	        conn.close();
	    } catch (SQLException e) {
	        if (cstmt != null) {
	    	      try {
	    	          cstmt.close();
	    	          conn.close();
	    	      } catch (SQLException e1) {
	    	       e1.printStackTrace();
	    	      }
	      }
	     e.printStackTrace();
	     cscount=0;
	  }
	return cscount;
 }
  /**
   * 主程序，逐步调用各静态方法。
   * @param args
  */
  public static void main(String[] args) {
  }

}
