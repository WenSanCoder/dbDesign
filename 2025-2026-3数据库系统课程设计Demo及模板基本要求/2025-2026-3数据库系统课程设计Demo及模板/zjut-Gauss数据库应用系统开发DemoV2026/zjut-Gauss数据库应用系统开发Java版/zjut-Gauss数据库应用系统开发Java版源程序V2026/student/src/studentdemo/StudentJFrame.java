package studentdemo;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class StudentJFrame implements FocusListener{
	DefaultTableModel tableModel;		// 默认显示的表格
	JButton btnAdd,btnDelete,btnCancel,btnChange;		// 各处理按钮
	JTable table;		// 表格
	JFrame f= new JFrame("系别信息输入窗口");
	JPanel panelUP;	//增加信息的面板
	JLabel laSno,laName,laDept,laTime;
	JTextField txtSno,txtName,txtTime;
	JComboBox<depart> cmbDept;
	gsqlCon con=null;
	public StudentJFrame(){
		f.setBounds(300, 200, 642, 462);		// 设置窗体大小
		f.setTitle("学生信息输入窗口");		// 设置窗体名称
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
		laSno = new JLabel("学号:");
		laSno.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		laName = new JLabel("姓名:");
		laName.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		laDept = new JLabel("系别:");
		laDept.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		laTime = new JLabel("入学时间:");
		laTime.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		 cmbDept=new JComboBox<>();
	    ResultSet dp;
		try {
			dp = con.gsqldep(con.GetCon());
			while(dp.next()){
				 cmbDept.addItem(new depart(dp.getString(1),dp.getString(2)));
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		txtSno = new JTextField();
		txtName = new JTextField();
		txtTime = new JTextField("yyyy-mm-dd");
		txtTime.addFocusListener(this);
		laSno.setBounds(25, 25,40, 12);
		txtSno.setBounds(68, 18, 90, 25);
		laName.setBounds(168, 25, 40, 12);
		txtName.setBounds(236, 18, 90,25);
		laDept.setBounds(25, 68, 85, 25);
		cmbDept.setBounds(68, 68, 90, 25);
		laTime.setBounds(168, 68, 85,25);
		txtTime.setBounds(236, 68, 90,25);
		btnAdd.setBounds(341,66,66,26);
		panelUP.add(btnAdd);
		panelUP.add(laSno);
		panelUP.add(txtSno);
		panelUP.add(laName);
		panelUP.add(txtName);
		panelUP.add(laDept);
		panelUP.add(cmbDept);
		panelUP.add(laTime);
		panelUP.add(txtTime);
		ResultSet rs = null;
		Vector columnNames = new Vector();
			//设置列名
		columnNames.add("学号");
		columnNames.add("姓名");
		columnNames.add("系别");
		columnNames.add("入学时间");
		Vector rowData = new Vector();
		//rowData可以存放多行,开始从数据库里取
		try {
			rs= con.gsqlquery(con.GetCon(), "select Sno,Sname,Depts.Dname,StartDate from Students,Depts where Students.Dno = Depts.Dno;");
			if(rs==null) {System.out.print("查询无数据 ！！！！！！");}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    try {
			while(rs.next()){
					//rowData可以存放多行
				    System.out.print("数据 ！！！！！！");
					Vector hang=new Vector();
					hang.add(rs.getString(1));
					hang.add(rs.getString(2));
					hang.add(rs.getString(3));
					hang.add(rs.getString(4).subSequence(0, 10));
					//加入到rowData
					rowData.add(hang);
					}
		  } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 新建表格
		tableModel = new DefaultTableModel(rowData,columnNames);	
		table = new JTable(tableModel) {
			   @Override
			   public boolean isCellEditable(int row, int column) {
            if(column == 0)
			       return false;
            else
          	  return true;
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
				String sql=null;
				int a;
				depart de=(depart)cmbDept.getSelectedItem();
	            sql = "insert into Students values( '" + txtSno.getText() + "','" + txtName.getText() + "','" + de.toNber()+ "','" + txtTime.getText() + "',0);";            
	            a= con.gsqlexc(con.GetCon(), sql);
	            if(a>0) {
	               tableModel.addRow(new Object[] {txtSno.getText(), txtName.getText(),de.toString(),txtTime.getText()});
	           	   txtSno.setText("");
				   txtName.setText("");
				   txtTime.setText("");
				   cmbDept.setDefaultLocale(null);
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
				if(rowcount==-1) {	
				   JOptionPane.showMessageDialog(null, "请选中数据!","提示消息",JOptionPane.WARNING_MESSAGE);
				}
				String sql="delete from Students where Sno='" + table.getValueAt(rowcount, 0)+"';";
				rscount=con.gsqlexc(con.GetCon(), sql);
			  if(rscount>0) 
				  tableModel.removeRow(rowcount);
			}
		});
		btnChange.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {	
				int rowcount = table.getSelectedRow();
				int rscount;
				if(rowcount==-1) {	
				  JOptionPane.showMessageDialog(null, "请选中数据!","提示消息",JOptionPane.WARNING_MESSAGE);
				}
				ResultSet dep;
				String desql="";
				String dpo="";
				desql="select Dno from Depts where Dname='" +table.getValueAt(rowcount, 2)+"';";
				try {
					dep=con.gsqlquery(con.GetCon(), desql);
					if(dep.next()) {
					   dpo=dep.getString(1);
					   String sql="update Students set Sname='" + table.getValueAt(rowcount, 1)+"',Dno='"+ dpo +"',StartDate='"+table.getValueAt(rowcount, 3)+"'where Sno ='"+table.getValueAt(rowcount, 0) +"';";
				       con.gsqlexc(con.GetCon(), sql);
				     }
					else
						JOptionPane.showMessageDialog(null, "不能操作!","提示消息",JOptionPane.WARNING_MESSAGE);
						
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		// 退出
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
		new StudentJFrame();
	}
	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		txtTime.setText("");
		txtTime.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		txtTime.setForeground(Color.BLACK);
	}
	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}
}
