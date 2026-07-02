using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
//using System.Data.SqlClient;


namespace student
{
    public partial class frmStudent : Form
    {
        sqlConnect con = new sqlConnect();
        public DataSet ds = new DataSet();        
        private string sql;
       

        public frmStudent()
        {
            InitializeComponent();
            ComBoxBind();
            SetBind();           
        }
       

      
        //----绑定DataGivdView数据
        protected void SetBind()
        {           
            sql = "select Sno as 学号,Sname as 姓名,deptName as 系名,StartDate as 入学时间,CreditHours as 学分  from Students,Depts where Students.DeptNo=Depts.DeptNo";
            try{
                ds = con.BindDataGridView(dataGView, sql);              
                dataGView.Columns[0].ReadOnly = true;
                dataGView.Columns[4].ReadOnly = true;
                dataGView.AllowUserToAddRows = false;
            }catch{
                MessageBox.Show("不能做该操作！", "提示", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }                        
        }





        //----绑定下拉框数据
        protected void ComBoxBind()
        {
            try
            {   //----对下拉框绑定学院信息           
                cmbDept.DropDownStyle = ComboBoxStyle.DropDownList;
                cmbDept.Items.Clear();
                ds = con.Getds("select  DeptNo ,deptName from Depts");
                cmbDept.DisplayMember = "deptName";
                cmbDept.ValueMember = "DeptNo";
                cmbDept.DataSource = ds.Tables[0];
                cmbDept.SelectedIndex = 0;                
            }
            catch (Exception)
            {
                MessageBox.Show("不能做该操作！", "提示", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }       
        }




        //----插入数据项
        private void btnInsert_Click(object sender, EventArgs e)
        {           
            try
            {
                sql = "insert into Students values( '" + txtSno.Text + "','" + txtName.Text + "','" + cmbDept.SelectedValue.ToString() + "','" + dtTime.Value.Date.ToString() +"',0)";
                con.OperateData(sql);
                SetBind();
            }
            catch {
                MessageBox.Show("不能做该操作！", "提示", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
           
        }



        //----关闭窗口
        private void btnCancel_Click(object sender, EventArgs e)
        {
            this.Close();

        }




        //----删除当前行
        private void btnDelete_Click(object sender, EventArgs e)
        {
            if (MessageBox.Show("确定要删除该条信息吗？", "提示", MessageBoxButtons.OKCancel, MessageBoxIcon.Question) == DialogResult.OK)
            {
                try
                {
                    string Sno = dataGView.CurrentRow.Cells[0].Value.ToString();                  
                    sql = "delete from Students where Sno= '" + Sno + "'";
                    con.OperateData(sql);
                    SetBind();
                    MessageBox.Show("删除成功！", "提示", MessageBoxButtons.OK, MessageBoxIcon.Information);                  
                }catch {
                    MessageBox.Show("不能做该操作！", "提示", MessageBoxButtons.OK, MessageBoxIcon.Information);
               
                }                                
            }
            else    return;
                  
        }



        //----修改当前行
        private void btnChange_Click(object sender, EventArgs e)
        {
            string Sno = dataGView.CurrentRow.Cells[0].Value.ToString();
            string Time = dataGView.CurrentRow.Cells[3].Value.ToString();
            try
            {
                sql = "select DeptNo from Depts where deptName='" + dataGView.CurrentRow.Cells[2].Value.ToString() + "'";
                ds = con.Getds(sql);
                string DeptNo = ds.Tables[0].Rows[0][0].ToString();
                sql = "update Students set Sname='" + dataGView.CurrentRow.Cells[1].Value.ToString() + "',deptNo='"
                     + DeptNo + "', StartDate='" + dataGView.CurrentRow.Cells[3].Value.ToString() +
                     "' where Sno='" + Sno + "'";
                con.OperateData(sql);
                SetBind();
            }
            catch{
                MessageBox.Show("不能做该操作！", "提示", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }  
                    
        }
       
      
    }
}
