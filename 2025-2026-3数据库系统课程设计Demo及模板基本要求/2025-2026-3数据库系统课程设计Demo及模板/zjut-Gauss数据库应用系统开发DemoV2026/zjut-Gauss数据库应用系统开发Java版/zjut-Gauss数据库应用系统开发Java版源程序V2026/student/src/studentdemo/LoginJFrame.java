package studentdemo;	
import java.awt.Color;
	import java.awt.Font;
	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;
    import javax.swing.BorderFactory;
	import javax.swing.JButton;
	import javax.swing.JFrame;
	import javax.swing.JLabel;
	import javax.swing.JOptionPane;
	import javax.swing.JPasswordField;
	import javax.swing.JTextField;
	public class LoginJFrame extends JFrame  implements ActionListener{
		JLabel laName,laPwd;
		JTextField txtName;
		JPasswordField txtPwd;
		JButton btnLogin,btnExit;   
		public LoginJFrame() {
			laName = new JLabel("用 户 名:");
		//	laName.setText("yonghu");
			laName.setFont(new Font("微软雅黑", Font.PLAIN, 13));
			laPwd = new JLabel("密      码:");
			laPwd.setFont(new Font("微软雅黑", Font.PLAIN, 13));
			txtName = new JTextField();
			txtPwd = new JPasswordField();
			laName.setBounds(85, 95,80, 35);
			laPwd.setBounds(85, 135, 80, 35);
			txtName.setBounds(153, 98, 165, 30);
			txtPwd.setBounds(153, 138, 165, 30);
			btnLogin = new JButton("登录");
			btnLogin.setBounds(145, 205, 80, 40);
			setJButton(btnLogin);
			btnExit = new JButton("取消");		
			btnExit.setBounds(230, 205, 80, 40);
			setJButton(btnExit);
			btnLogin.addActionListener(this);
			btnExit.addActionListener(this);
			this.setTitle("欢迎登陆");
			this.setLayout(null);
			this.add(laName);
			this.add(laPwd);
			this.add(txtName);
			this.add(txtPwd);
			this.add(btnLogin);
			this.add(btnExit);
			this.setTitle("欢迎登陆");
			this.setSize(450,350);
			this.setLocationRelativeTo(null);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setVisible(true);
			this.setResizable(false);     
		}
		public static void main(String[] args) {
			// TODO Auto-generated method stub
			new LoginJFrame();	
		}
		//设置按钮风格：透明 
		private void setJButton(JButton btn) {  
			btn.setBackground(new Color(102, 0, 204));// 紫色 
			btn.setFont(new Font("Dialog", Font.BOLD, 18));  
			btn.setOpaque(false);  
			btn.setBorder(BorderFactory.createEmptyBorder());  
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand() == "取消") {
				this.dispose();
			}
			if(e.getActionCommand() == "登录") {
						if(txtName.getText()!=null && txtPwd.getText()!=null) {
							JOptionPane.showMessageDialog(null, "登录成功!","提示消息",JOptionPane.WARNING_MESSAGE);
							new MainJFrame();
							this.hide();
						}else if(txtName.getText().isEmpty()&&txtPwd.getText().isEmpty()) {
							JOptionPane.showMessageDialog(null, "请输入用户名和密码!","提示消息",JOptionPane.WARNING_MESSAGE);
							clear();
						}else if(txtName.getText().isEmpty()) {
							JOptionPane.showMessageDialog(null, "请输入用户名!","提示消息",JOptionPane.WARNING_MESSAGE);
							clear();
						}else if(txtPwd.getText().isEmpty()) {
							JOptionPane.showMessageDialog(null, "请输入密码!","提示消息",JOptionPane.WARNING_MESSAGE);
							clear();
						}else {
							JOptionPane.showMessageDialog(null, "用户名或者密码错误！\n请重新输入","提示消息",JOptionPane.ERROR_MESSAGE);
							clear();
						}
					}
				}
		private void clear() {
			txtName.setText("");
			txtPwd.setText("");
		}
	}