package studentdemo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
public class MainJFrame implements ActionListener {
	JMenu file_, basetable_,mainstable_,procdure_; 
	JMenuItem exit_, student_, course_, result_,depart_,detail_,procdure_display; 
    MainJFrame(){  
    JFrame f= new JFrame("学生成绩管理系统");
    JMenuBar mb=new JMenuBar();  
    file_=new JMenu("文件");  
    basetable_=new JMenu("基本表维护");  
    mainstable_=new JMenu("主细表");  
    procdure_=new JMenu("调用存储过程"); 
    exit_=new JMenuItem("退出");  
    exit_.addActionListener(this);
    student_=new JMenuItem("学生表输入");  
    student_.addActionListener(this);
    course_=new JMenuItem("课程表输入");  
    course_.addActionListener(this);
    depart_=new JMenuItem("系别表输入");  
    depart_.addActionListener(this);
    result_=new JMenuItem("成绩表输入");  
    result_.addActionListener(this);
    detail_=new JMenuItem("主细表演示");  
    detail_.addActionListener(this);
    procdure_display=new JMenuItem("调用存储过程演示"); 
    procdure_display.addActionListener(this);
    file_.add(exit_); 
basetable_.add(student_);
    basetable_.add(course_);
    basetable_.add(depart_);
    basetable_.add(result_);
    mainstable_.add(detail_);
    procdure_.add(procdure_display);
    mb.add(file_); 
    mb.add(basetable_); 
    mb.add(mainstable_); 
    mb.add(procdure_);  
    f.setJMenuBar(mb);  
    f.setSize(642, 462);  
    f.setLocationRelativeTo(null);
    f.setLayout(null);  
    f.setVisible(true);  
}
    public static void main(String args[])
    {
    new MainJFrame();
    }
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource().equals(exit_))
			System.exit(0);
		if(e.getSource().equals(student_))
			new StudentJFrame();
		if(e.getSource().equals(course_))
			new CourseJFrame();
		if(e.getSource().equals(depart_))
			new DepartJFrame();
		if(e.getSource().equals(result_))
			new ResultJFrame();
		if(e.getSource().equals(detail_))
			new DetailFrame();
		if(e.getSource().equals(procdure_display))
			new ProcdureFrame();		
	}
}