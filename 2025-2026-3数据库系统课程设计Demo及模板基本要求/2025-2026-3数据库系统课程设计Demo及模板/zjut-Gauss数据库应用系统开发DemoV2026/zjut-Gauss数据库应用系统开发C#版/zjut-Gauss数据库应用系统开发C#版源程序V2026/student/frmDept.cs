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
    public partial class frmDept : Form
    {
        sqlConnect con = new sqlConnect();
        public DataSet ds = new DataSet();     
        private string sql;

        public frmDept()
        {
            InitializeComponent();
            SetBind();
        }
        //----绑定DataGivdView数据
        protected void SetBind()
        {  
            try
            {
                sql = "select dno as 系号,dname as 系名 from depts";
                ds = con.BindDataGridView(dataGView, sql);                
                dataGView.Columns[0].ReadOnly = true;
                dataGView.AllowUserToAddRows = false;
            }catch {
                MessageBox.Show("不能做该操作！", "提示", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
        }
        //----插入数据
        private void btnInsert_Click(object sender, EventArgs e)
        {           
            try{                
                sql = "insert into depts values( '" + txtDno.Text + "','" + txtName.Text + "')";            
                con.OperateData(sql);
                SetBind();
            }catch{
                MessageBox.Show("不能做该操作！", "提示", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
            
           
        }
        //----修改数据
        private void btnChange_Click(object sender, EventArgs e)
        {           
            try{
                sql = "update depts set dname='" + dataGView.CurrentRow.Cells[1].Value.ToString() +
                    "' where dno='" + dataGView.CurrentRow.Cells[0].Value.ToString() + "'";
                con.OperateData(sql);
                SetBind();
            }catch{
                MessageBox.Show("不能做该操作！", "提示", MessageBoxButtons.OK, MessageBoxIcon.Information);              
            }          
        }
        //---------删除数据
        private void btnDelete_Click(object sender, EventArgs e)
        {
            if (MessageBox.Show("确定要删除该条信息吗？", "提示", MessageBoxButtons.OKCancel, MessageBoxIcon.Question) == DialogResult.OK){               
                try {
                    sql = "delete from depts where dno='" + dataGView.CurrentRow.Cells[0].Value.ToString() + "'";
                    con.OperateData(sql);
                    SetBind();
                }
                catch{
                    MessageBox.Show("不能做该操作！", "提示", MessageBoxButtons.OK, MessageBoxIcon.Information);          
                }
            }
        }

        private void btnCancel_Click(object sender, EventArgs e){
            this.Close();
        }
    }
}
