package studentdemo;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
public class ResultJFrame {
	DefaultTableModel tableModel;		// 默认显示的表格
	JButton btnAdd,btnDelete,btnCancel,btnChange;		// 各处理按钮
	JTable table;		// 表格
	JFrame f= new JFrame("成绩录入窗口");
	JPanel panelUP;	//增加信息的面板
	JLabel laName,laGrade,laCourse;
	JTextField txtGrade;
	JComboBox<stu_L> stuJcbox;
	JComboBox<courL> courJcbox;
	ArrayList stu_courlist= new ArrayList();
	gsqlCon con=null;
	// 构造函数
	public ResultJFrame(){
		f.setBounds(300, 200, 642, 462);		// 设置窗体大小
		f.setTitle("成绩信息输入窗口");		// 设置窗体名称
		f.setLayout(null);		
		// 新建各按钮组件
		btnAdd = new JButton("插入");
		btnDelete = new JButton("删除");
		btnDelete.setBounds(191,320,66,26);
		btnChange = new JButton("保存");
		btnChange.setBounds(268,320,66,26);
		btnCancel = new JButton("退出");
		btnCancel.setBounds(345,320,66,26);
		f.add(btnDelete);
		f.add(btnChange);
		f.add(btnCancel);
		panelUP = new JPanel();		// 新建按钮组件面板
	    panelUP.setLayout(null);
		panelUP.setBounds(92,30,427,120);
		panelUP.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		laName = new JLabel("姓名:");
		laName.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		laGrade = new JLabel("成   绩:");
		laGrade.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		laCourse = new JLabel("课程名:");
		laCourse.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		stuJcbox=new JComboBox<>();
		courJcbox=new JComboBox<>();
		ResultSet stu;
		ResultSet cour;
			try {
				stu = con.gsqlstu(con.GetCon());
				while(stu.next()){
					stuJcbox.addItem(new stu_L(stu.getString(1),stu.getString(2)));  
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			try {
				cour = con.gsqlcour(con.GetCon());
				while(cour.next()){
					courJcbox.addItem(new courL(cour.getString(1),cour.getString(2)));	    
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		   
		txtGrade = new JTextField();
		laName.setBounds(25, 25,40, 25);
		stuJcbox.setBounds(74, 25, 90, 25);
		laCourse.setBounds(185, 25, 40, 25);
		courJcbox.setBounds(236, 25, 90,25);
		laGrade.setBounds(25, 68, 85, 25);
		txtGrade.setBounds(74, 68, 90, 25);
		btnAdd.setBounds(236, 68, 90,25);
		panelUP.add(btnAdd);
		panelUP.add(laName);
		panelUP.add(laGrade);
		panelUP.add(laCourse);
		panelUP.add(stuJcbox);
		panelUP.add(txtGrade);
		panelUP.add(courJcbox);
		ResultSet rs = null;	
		Vector columnNames = new Vector();
			//设置列名
		columnNames.add("姓名");
		columnNames.add("课程名称名");
		columnNames.add("成绩");
		Vector rowData = new Vector();
		//rowData可以存放多行,开始从数据库里取
		try {
			rs= con.gsqlquery(con.GetCon(), "select Students.Sno,Students.Sname,Courses.CNo,Courses.Cname,Reports.Grade from Students,Courses,Reports where Students.Sno = Reports.Sno and Reports.Cno=Courses.Cno;");
			if(rs==null) {
System.out.print("查询无数据 ！！！！！！");
}
			while(rs.next()){
				//rowData可以存放多行
			    System.out.print("数据 ！！！！！！");
			    stu_courlist.add(new stu_courr(rs.getString(1),rs.getString(2),rs.getString(3),
rs.getString(4) ) );
			     Vector hang=new Vector();
			     hang.add(rs.getString(2));
			     hang.add(rs.getString(4));
			     hang.add(rs.getString(5));
			     rowData.add(hang);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		tableModel = new DefaultTableModel(rowData,columnNames);	
		table = new JTable(tableModel) {
				   @Override
				   public boolean isCellEditable(int row, int column) {
	               if(column == 2)
				       return true;
	               else
	             	  return false;
				   }
		};
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane s = new JScrollPane(table);
		s.setBounds(92, 170, 427, 120);
		f.add(panelUP);
		f.add(s);
		// 事件处理
		MyEvent();
		f.setVisible(true);		// 显示窗体
	}
	// 事件处理
	public void MyEvent(){	
	    // 增加
		btnAdd.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// 增加一行空白区域
				String sql=null;
				int a;	
				stu_L stu=(stu_L)stuJcbox.getSelectedItem();
		    	int grade=Integer.parseInt(txtGrade.getText().toString());
		    	if(grade>100|grade < 0) {	
					JOptionPane.showMessageDialog(null, "请输入成绩为0到100之间的整数!","提示消息",JOptionPane.WARNING_MESSAGE);
					txtGrade.setText("");
		    	}
		        if(grade>=0 & grade <= 100) {
			       courL cor=(courL)courJcbox.getSelectedItem();
			       System.out.print("courname cast");
	               sql = "insert into Reports values( '" + stu.tostunber() + "','" +cor.toNber() + "','"  + txtGrade.getText() + "');";            
	               a= con.gsqlexc(con.GetCon(), sql);
	               if(a>0) {
	                   tableModel.addRow(new Object[] {stu.toString(),cor.toString(),txtGrade.getText()});
	                    txtGrade.setText("");
	               }
			}
		
			}
		});
		
		// 删除
		btnDelete.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				// 删除指定行
				int rowcount = table.getSelectedRow();
			    int rscount;
                 stu_courr stu_cour_L=(stu_courr)stu_courlist.get(rowcount);
                 String stunber=stu_cour_L.tostunber();
                 String cournber=stu_cour_L.tocourner();
             	System.out.print(stunber+cournber);
				if(rowcount==-1) {	
				JOptionPane.showMessageDialog(null, "请选中数据!","提示消息",JOptionPane.WARNING_MESSAGE);
				}
				String sql="delete from Reports where Sno='" +stunber+"'and Cno='" + cournber + "'";
				rscount=con.gsqlexc(con.GetCon(), sql);
			    if(rscount>0) {
			    	tableModel.removeRow(rowcount);
			        stu_courlist.remove(rowcount);
			    }
			}
			
		});
		btnChange.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {	
				int rowcount = table.getSelectedRow();
				if(rowcount==-1) {	
				    JOptionPane.showMessageDialog(null, "请选中数据!","提示消息",JOptionPane.WARNING_MESSAGE);
				}
				int grade=Integer.parseInt((String) table.getValueAt(rowcount, 2));
				if(grade>100 | grade <0) {	
					JOptionPane.showMessageDialog(null, "请输入成绩为0到100之间的整数!","提示消息",JOptionPane.WARNING_MESSAGE);
				}
				
				if(grade>=0 & grade <= 100) {
                 stu_courr stu_cour_L=(stu_courr)stu_courlist.get(rowcount);
                 String stunber=stu_cour_L.tostunber();
                 String cournber=stu_cour_L.tocourner();
				 String sql="update Reports set Grade='" + table.getValueAt(rowcount, 2)+"'where Sno ='"+stunber+"'and Cno='" + cournber+ "'";
				 con.gsqlexc(con.GetCon(), sql);	
		        }}
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
		new ResultJFrame();
	}
}