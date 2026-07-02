using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace student
{
    public partial class frmCourse : Form
    {
        sqlConnect con = new sqlConnect();
        public DataSet ds = new DataSet();
        private string sql;

        public frmCourse(){
            InitializeComponent();
            SetBind();
        }

        //----绑定DataGivdView数据
        protected void SetBind()
        {    
            try {
                sql = "select Cno as 课程号,Cname as 课程名称,CreditHour as 学分 from Courses";
                ds = con.BindDataGridView(dataGView, sql);              
                dataGView.Columns[0].ReadOnly = true;                
                dataGView.AllowUserToAddRows = false;
            }catch{
                MessageBox.Show("不能做该操作！", "提示", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
        }


      //---插入数据
        private void btnInsert_Click(object sender, EventArgs e)
        {    
            try{                
                sql = "insert into Courses values( '" + txtCno.Text + "','" + txtName.Text + "','" + txtCredit.Text + "')" ;
                con.OperateData(sql);
                SetBind();
            }catch {
                MessageBox.Show("不能做该操作！", "提示", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }          
           
        }
        //--------修改记录
        private void btnChange_Click(object sender, EventArgs e)
        {         
            try
            {
                sql = "update Courses set Cname='" + dataGView.CurrentRow.Cells[1].Value.ToString() +
                    "',CreditHour='"+ dataGView.CurrentRow.Cells[2].Value.ToString() +
                    "' where Cno='" + dataGView.CurrentRow.Cells[0].Value.ToString() + "'";
                con.OperateData(sql);
                SetBind();
            }catch{
                MessageBox.Show("不能做该操作！", "提示", MessageBoxButtons.OK, MessageBoxIcon.Information);
              
            }          
        }
        //---删除记录
        private void btnDelete_Click(object sender, EventArgs e){
            if (MessageBox.Show("确定要删除该条信息吗？", "提示", MessageBoxButtons.OKCancel, MessageBoxIcon.Question) == DialogResult.OK)
            {
              try {                                
                    sql = "delete from Courses where Cno='" + dataGView.CurrentRow.Cells[0].Value.ToString() + "'";
                    con.OperateData(sql);
                    SetBind();
                    MessageBox.Show("删除成功！", "提示", MessageBoxButtons.OK, MessageBoxIcon.Information);                 
                   }catch{
                    MessageBox.Show("不能做该操作！", "提示", MessageBoxButtons.OK, MessageBoxIcon.Information);                
                }
            }               
        }
       //---关闭窗口
        private void btnCancel_Click(object sender, EventArgs e){
            this.Close();
        }
    }
}