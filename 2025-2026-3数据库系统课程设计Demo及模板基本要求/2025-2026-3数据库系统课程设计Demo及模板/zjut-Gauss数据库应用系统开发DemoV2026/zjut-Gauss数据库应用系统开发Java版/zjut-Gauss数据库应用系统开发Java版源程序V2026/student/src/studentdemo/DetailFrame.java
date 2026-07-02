package studentdemo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
public class DetailFrame  implements MouseListener {
	DefaultTableModel maintableModel,detailtableModel;		// 默认显示的表格
	    JButton btnDelete,btnCancel;		// 各处理按钮
	JTable mainTable,detailTable;		// 表格
	JFrame f= new JFrame("主细表演示");
	JScrollPane mainJpane,detailJpane;
	gsqlCon con=null;
	ResultSet mainrs,detailrs ;
	Vector maincolumnNames,mainrowData, decolumnNames, derowData;
	ArrayList detaillist= new ArrayList();
	String friststunber,desql;
	int countd=0;
	String ttsql;
	String ffstunber;
	private int selNo = 0;
	// 构造函数
	public DetailFrame(){
		   f.setBounds(300, 200, 642, 462);		// 设置窗体大小
		   f.setTitle("主细表演示");		// 设置窗体名称
		   f.setLayout(null);		
		// 新建各按钮组件
		   btnDelete = new JButton("删除");
		   btnDelete.setBounds(303,338,66,26);
		   btnCancel = new JButton("退出");
		   btnCancel.setBounds(401,338,66,26);
		   f.add(btnDelete);
		   f.add(btnCancel);
	       mainrs = null;	
		   maincolumnNames = new Vector();
		   mainrowData = new Vector();
		   maintables();
		// 新建表格
	  	   maintableModel = new DefaultTableModel(mainrowData,maincolumnNames);	
		   mainTable = new JTable(maintableModel) {
			   @Override
			   public boolean isCellEditable(int row, int column) {
                   return false;
			   }
			};
		   mainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		   mainTable.addMouseListener(new MouseAdapter()
	        {
	            public void mouseClicked(MouseEvent event)
	            {   selNo = 0;
	                System.out.print("开始点击主表"+countd+"次");
	                detailtableModel.setRowCount(0);      
		            int ffrowcount = mainTable.getSelectedRow();
	             	try {
					    ffstunber=(String) mainTable.getValueAt(ffrowcount, 0);
					  ttsql="select Reports.Sno,Courses.Cname,Reports.Grade,Courses.Cno from Reports,Courses  where Reports.Cno=Courses.Cno and Reports.Sno='" + ffstunber + "';";
	            	    } catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	        detailtable(ttsql);
	        	        countd++;
	               }
	           });
	 	  mainJpane = new JScrollPane(mainTable);
		  mainJpane.setBounds(72,23, 486, 133);
		  f.add(mainJpane);
		  derowData =new Vector();
	      decolumnNames = new Vector();
		  decolumnNames.add("学号");
		  decolumnNames.add("课程名");
		  decolumnNames.add("成绩");
		  detailtableModel = new DefaultTableModel(derowData,decolumnNames);	
		  detailTable = new JTable(detailtableModel) {
			   @Override
			   public boolean isCellEditable(int row, int column) {
          	  return false;
			   }
			};
		  detailTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		  detailTable.addMouseListener(this);
		  detailJpane = new JScrollPane(detailTable);
		  detailJpane.setBounds(72, 185, 486, 133);
		  f.add(detailJpane);
		// 事件处理
		  MyEvent();
		  f.setVisible(true);		// 显示窗体
	}
	public void maintables() {	
		//设置列名
	       maincolumnNames.add("学号");
	       maincolumnNames.add("姓名");
	       maincolumnNames.add("系名");
	       maincolumnNames.add("入学时间");
	       maincolumnNames.add("学分");
	       try {
		      mainrs= con.gsqlquery(con.GetCon(), "select Students.Sno,Students.Sname,Depts.Dname,Students.StartDate,Students.creditHours from Students,Depts where Students.Dno = Depts.Dno;");
		 while(mainrs.next()){
			//rowData可以存放多行
		  //  System.out.print(" asdfasdfasdfsdfasd数据 ！！！！！！");
			 Vector mhang=new Vector();
			 mhang.add(mainrs.getString(1));
			 mhang.add(mainrs.getString(2));
			 mhang.add(mainrs.getString(3));
			 mhang.add(mainrs.getString(4).subSequence(0, 10));
			 mhang.add(mainrs.getString(5));
			//加入到rowData
			mainrowData.add(mhang);
			}
	} catch (SQLException e1) {
		// TODO Auto-generated catch block
		    System.out.print("gsstu_cour-------------结束查询操作！");
		 JOptionPane.showMessageDialog(null, "无记录!","提示消息",JOptionPane.WARNING_MESSAGE);
		   e1.printStackTrace();
	}			
};
public void detailtable(String sql) {
System.out.print("进入detailtable查询数据 ！！！！！！");
try {
		     detailrs= con.gsqlquery(con.GetCon(), sql);
			 while(detailrs.next()){ 
		         detaillist.add(new detailobject(detailrs.getString(1),detailrs.getString(2),detailrs.getString(3),detailrs.getString(4)));
              detailtableModel.addRow(new Object[] {detailrs.getString(1),detailrs.getString(2),detailrs.getString(3)});
	             System.out.print(detailrs.getString(1)+detailrs.getString(2)+detailrs.getString(3));
	}
} catch (Exception e) {
		// TODO Auto-generated catch block
		 JOptionPane.showMessageDialog(null, "无记录!","提示消息",JOptionPane.WARNING_MESSAGE);
		 e.printStackTrace();
	}	
};
	// 事件处理
	     public void MyEvent(){
	  	    btnDelete.addActionListener(new ActionListener(){
			@Override
			     public void actionPerformed(ActionEvent arg0) {
			
				    int mrowcount = mainTable.getSelectedRow();
				    int detailrowcount = detailTable.getSelectedRow();
				    if(selNo==0) {	
				        if(mrowcount==-1)
					     JOptionPane.showMessageDialog(null, "请选中数据!","提示消息",JOptionPane.WARNING_MESSAGE);
					   else
			               maintabledel();	
				    }else{
					    if(detailrowcount==-1)
						   JOptionPane.showMessageDialog(null, "请选中数据!","提示消息",JOptionPane.WARNING_MESSAGE);
					    else
					        detailtabledel();
				}    
			}
		});
		
	     	btnCancel.addActionListener(new ActionListener(){
			@Override
			    public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			    f.dispose();
			    }	
		    });
	}
	// 主函数
	public static void main(String[] args){
		    DetailFrame df=new DetailFrame();
	}
// maintable 删除函数
	public void maintabledel()  {
        int marowcount=mainTable.getSelectedRow();
		    System.out.print("进入detailtable查询数据 ！！！！！！");
	        int rscount;
		   String delsql = "delete from Reports where Sno='" + mainTable.getValueAt(marowcount, 0)+ "';";
		   con.gsqlexc(con.GetCon(), delsql);
		   delsql = "delete from Students where Sno='" + mainTable.getValueAt(marowcount, 0)+ "';";
		   rscount=con.gsqlexc(con.GetCon(), delsql);
		   if(rscount>0)
		       maintableModel.removeRow(marowcount);		
		};
//detailtable 删除函数
		public void detailtabledel() {
			int derowcount=detailTable.getSelectedRow();
			int rscount;
			detailobject det=(detailobject)detaillist.get(derowcount);
			System.out.print("进入detailtabledel ！！！！！！"+derowcount+"索引");
			String delsql = "delete from Reports where Sno='" + det.torepsno() + "' and Cno='"+det.tocornber()+"';";
			rscount=con.gsqlexc(con.GetCon(), delsql);               
		  if(rscount>0) {
			  detailtableModel.removeRow(derowcount);
		    System.out.print("进入detailtabledel ！！！！！！"+derowcount+"索引");
			}
		};
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		selNo=1;	
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub	
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub	
	}	
}