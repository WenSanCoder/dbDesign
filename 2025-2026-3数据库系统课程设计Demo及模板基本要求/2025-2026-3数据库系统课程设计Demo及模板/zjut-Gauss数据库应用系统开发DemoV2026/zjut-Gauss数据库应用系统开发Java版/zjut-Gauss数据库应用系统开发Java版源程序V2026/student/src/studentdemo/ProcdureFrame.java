package studentdemo;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
public class  ProcdureFrame implements FocusListener {
	JLabel laDate,laCrdit;
	JTextField txtDate,txtCredit;
	JFrame f= new JFrame("调用存储过程窗口");
	JButton btnDelete,btnCancel;
	gsqlCon con=null;
	public ProcdureFrame () {
		laDate = new JLabel("日      期:");
		laDate.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		laCrdit = new JLabel("最小学分:");
		laCrdit.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		txtDate = new JTextField("yyyy-mm-dd");
		txtDate.addFocusListener(this);
		txtCredit = new JTextField();
		laDate.setBounds(95, 90,80, 35);
		laCrdit.setBounds(95, 130, 80, 35);
		txtDate.setBounds(163, 93, 165, 30);
		txtCredit.setBounds(163, 133, 165, 30);
		btnDelete = new JButton("删除");
		btnDelete.setBounds(155, 200, 80, 40);
		setJButton(btnDelete);
		btnCancel = new JButton("取消");		
		btnCancel.setBounds(240, 200, 80, 40);
		setJButton(btnCancel);
		f.setLayout(null);
		f.add(laDate);
		f.add(laCrdit);
		f.add(txtDate);
		f.add(txtCredit);
		f.add(btnDelete);
		f.add(btnCancel);
		f.setSize(450,350);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		f.setResizable(false);
		f.setFocusable(true);
		MyEvent(); 
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ProcdureFrame ();
		
	}
	/*
	 * 设置按钮风格：透明 
	 */
	private void setJButton(JButton btn) {  
		btn.setBackground(new Color(102, 0, 204));
		btn.setFont(new Font("Dialog", Font.BOLD, 20));  
		btn.setOpaque(false);  
		btn.setBorder(BorderFactory.createEmptyBorder());  
		
	}		
	public void MyEvent(){
		// 删除
		btnDelete.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				// 删除指定行
	             int cscount;
				int grade=Integer.parseInt(txtCredit.getText().toString());
				String datetime=txtDate.getText();
				if(grade>100|grade < 0) {	
					JOptionPane.showMessageDialog(null, "请输入为0到100之间的整数!","提示消息",JOptionPane.WARNING_MESSAGE);
					txtCredit.setText("");
				}
			    if(grade>=0 & grade <= 100) {    
					cscount=con.ExecCallableSQL(con.GetCon(), txtDate.getText(), grade);
					if(cscount>0) {
					    txtCredit.setText("");
					    txtDate.setText("");
					  JOptionPane.showMessageDialog(null, "删除成功!","提示消息",JOptionPane.WARNING_MESSAGE);
					}else {
					   JOptionPane.showMessageDialog(null, "不能这么操作!","提示消息",JOptionPane.WARNING_MESSAGE);
					}
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
	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		txtDate.setText("");
	}
	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
	}
}