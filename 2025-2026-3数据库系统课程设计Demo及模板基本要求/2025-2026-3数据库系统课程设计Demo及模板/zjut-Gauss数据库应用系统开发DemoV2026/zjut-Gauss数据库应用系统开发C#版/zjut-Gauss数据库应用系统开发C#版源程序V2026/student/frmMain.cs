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
    public partial class frmMain : Form
    {
        public frmMain()
        {
            InitializeComponent();
        }

        private void 成绩表输入ToolStripMenuItem_Click(object sender, EventArgs e)
        {
            frmReport fReport = new frmReport();
            fReport.ShowDialog();
        }

        private void 退出ToolStripMenuItem_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }

        private void 学生表输入ToolStripMenuItem_Click(object sender, EventArgs e)
        {
            frmStudent fStudent = new frmStudent();
            fStudent.ShowDialog();
        }

        private void 系别表输入ToolStripMenuItem_Click(object sender, EventArgs e)
        {
            frmDept fDept = new frmDept();
            fDept.ShowDialog();
        }

        private void 课程表输入ToolStripMenuItem_Click(object sender, EventArgs e)
        {
            frmCourse fCourse = new frmCourse();
            fCourse.ShowDialog();
        }

        private void 主细表演示ToolStripMenuItem_Click(object sender, EventArgs e)
        {
            frmDetail fDetail = new frmDetail();
            fDetail.ShowDialog();
        }

        private void 调用存储过程演示ToolStripMenuItem_Click(object sender, EventArgs e)
        {
            frmProcdure fProcdure = new frmProcdure();
            fProcdure.ShowDialog();
        }
    }
}
