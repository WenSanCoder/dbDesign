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
    public partial class frmDetail : Form
    {
        sqlConnect con = new sqlConnect();
        public DataSet ds = new DataSet();
        private string sql;
        private string SelSno="";
        private int selNo = 0;

        public frmDetail(){
          InitializeComponent();
          BindS();
          BindP();
        }


        //----绑定DataGivdView数据
        protected void BindS(){          
          try {
                sql = "select Sno as 学号,Sname as 姓名,dname as 系名,StartDate as 入学时间  ,CreditHours 学分 from Students,Depts where Students.dno=Depts.dno";
                ds = con.BindDataGridView(dgvS, sql);               
                if(ds.Tables[0].Rows.Count!=0){                
                 dgvS.Columns[0].ReadOnly = true;             
                 dgvS.AllowUserToAddRows = false;                 
                 SelSno = dgvS.Rows[0].Cells[0].Value.ToString();
               } 
            }catch{
                MessageBox.Show("不能做该操作！", "提示", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
        }
        protected void BindP()
        {
            sql = "select Sno as 学号,Cname  as 课程名称,Grade as 成绩 ,Courses.Cno from Courses,Reports  where  Reports.Cno=Courses.Cno and Sno='"+SelSno+"'";
            try{
                ds = con.BindDataGridView(DgvP, sql);            
                DgvP.Columns[0].ReadOnly = true;
                DgvP.Columns[1].ReadOnly = true;
                DgvP.Columns[3].Visible = false;
                DgvP.AllowUserToAddRows = false;
            }catch{
                MessageBox.Show("不能做该操作！", "提示", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
        }
        //---------删除记录
        private void btnDelete_Click(object sender, EventArgs e)
        {
            if (MessageBox.Show("确定要删除该条信息吗？", "提示", MessageBoxButtons.OKCancel, MessageBoxIcon.Question) == DialogResult.OK){
                if (selNo== 0) delS();
                else delP();               
            }else return; 
        }

        private void btnCancel_Click(object sender, EventArgs e){
          this.Dispose();          
        }

        private void dgvS_SelectionChanged(object sender, EventArgs e){
            selNo = 0;
            SelSno = dgvS.CurrentRow.Cells[0].Value.ToString();
            BindP();
        }

        //-------------删除学生
        private void delS() {         
          try{
               sql = "delete from Reports where Sno='" + dgvS.CurrentRow.Cells[0].Value.ToString() + "'";
               con.OperateData(sql);
               sql = "delete from Students where Sno='" + dgvS.CurrentRow.Cells[0].Value.ToString() + "'";
               con.OperateData(sql);
               BindS();
               BindP();
              }catch {
                MessageBox.Show("不能做该操作！", "提示", MessageBoxButtons.OK, MessageBoxIcon.Information);                      
              }
         }
        //------------删除成绩
        private void delP()
        {
          try {
                int rowIndex = dgvS.CurrentCell.RowIndex;                
                string Sno = DgvP.CurrentRow.Cells[0].Value.ToString();
                string Cno = DgvP.CurrentRow.Cells[3].Value.ToString();
                sql = "delete from Reports where Sno='" + Sno + "' and Cno='"+Cno+"'";
                con.OperateData(sql);                
                BindS();              
                dgvS.CurrentCell = this.dgvS[0,rowIndex];
                SelSno = Sno;                
                BindP();
            } catch{
                MessageBox.Show("不能做该操作！", "提示", MessageBoxButtons.OK, MessageBoxIcon.Information);              
            }
        }

        private void DgvP_CellClick(object sender, DataGridViewCellEventArgs e){
            selNo = 1;
        }
                
        
    }
}
