namespace student
{
    partial class frmCourse
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.lCname = new System.Windows.Forms.Label();
            this.dataGView = new System.Windows.Forms.DataGridView();
            this.txtCno = new System.Windows.Forms.TextBox();
            this.lCno = new System.Windows.Forms.Label();
            this.txtCredit = new System.Windows.Forms.TextBox();
            this.txtName = new System.Windows.Forms.TextBox();
            this.lCreit = new System.Windows.Forms.Label();
            this.btnCancel = new System.Windows.Forms.Button();
            this.btnChange = new System.Windows.Forms.Button();
            this.btnDelete = new System.Windows.Forms.Button();
            this.btnInsert = new System.Windows.Forms.Button();
            this.panel1 = new System.Windows.Forms.Panel();
            ((System.ComponentModel.ISupportInitialize)(this.dataGView)).BeginInit();
            this.panel1.SuspendLayout();
            this.SuspendLayout();
            // 
            // lCname
            // 
            this.lCname.AutoSize = true;
            this.lCname.Location = new System.Drawing.Point(194, 17);
            this.lCname.Name = "lCname";
            this.lCname.Size = new System.Drawing.Size(53, 12);
            this.lCname.TabIndex = 0;
            this.lCname.Text = "课程名：";
            // 
            // dataGView
            // 
            this.dataGView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dataGView.Location = new System.Drawing.Point(24, 101);
            this.dataGView.Name = "dataGView";
            this.dataGView.RowTemplate.Height = 23;
            this.dataGView.Size = new System.Drawing.Size(372, 135);
            this.dataGView.TabIndex = 1;
            // 
            // txtCno
            // 
            this.txtCno.Location = new System.Drawing.Point(80, 8);
            this.txtCno.Name = "txtCno";
            this.txtCno.Size = new System.Drawing.Size(79, 21);
            this.txtCno.TabIndex = 2;
            // 
            // lCno
            // 
            this.lCno.AutoSize = true;
            this.lCno.Location = new System.Drawing.Point(16, 17);
            this.lCno.Name = "lCno";
            this.lCno.Size = new System.Drawing.Size(53, 12);
            this.lCno.TabIndex = 3;
            this.lCno.Text = "课程号：";
            // 
            // txtCredit
            // 
            this.txtCredit.Location = new System.Drawing.Point(80, 44);
            this.txtCredit.Name = "txtCredit";
            this.txtCredit.Size = new System.Drawing.Size(79, 21);
            this.txtCredit.TabIndex = 4;
            // 
            // txtName
            // 
            this.txtName.Location = new System.Drawing.Point(259, 8);
            this.txtName.Name = "txtName";
            this.txtName.Size = new System.Drawing.Size(93, 21);
            this.txtName.TabIndex = 5;
            // 
            // lCreit
            // 
            this.lCreit.AutoSize = true;
            this.lCreit.Location = new System.Drawing.Point(16, 53);
            this.lCreit.Name = "lCreit";
            this.lCreit.Size = new System.Drawing.Size(53, 12);
            this.lCreit.TabIndex = 6;
            this.lCreit.Text = "学  分：";
            // 
            // btnCancel
            // 
            this.btnCancel.Location = new System.Drawing.Point(319, 248);
            this.btnCancel.Name = "btnCancel";
            this.btnCancel.Size = new System.Drawing.Size(74, 26);
            this.btnCancel.TabIndex = 10;
            this.btnCancel.Text = "退  出";
            this.btnCancel.UseVisualStyleBackColor = true;
            this.btnCancel.Click += new System.EventHandler(this.btnCancel_Click);
            // 
            // btnChange
            // 
            this.btnChange.Location = new System.Drawing.Point(241, 248);
            this.btnChange.Name = "btnChange";
            this.btnChange.Size = new System.Drawing.Size(72, 26);
            this.btnChange.TabIndex = 9;
            this.btnChange.Text = "保  存";
            this.btnChange.UseVisualStyleBackColor = true;
            this.btnChange.Click += new System.EventHandler(this.btnChange_Click);
            // 
            // btnDelete
            // 
            this.btnDelete.Location = new System.Drawing.Point(167, 248);
            this.btnDelete.Name = "btnDelete";
            this.btnDelete.Size = new System.Drawing.Size(68, 26);
            this.btnDelete.TabIndex = 8;
            this.btnDelete.Text = "删  除";
            this.btnDelete.UseVisualStyleBackColor = true;
            this.btnDelete.Click += new System.EventHandler(this.btnDelete_Click);
            // 
            // btnInsert
            // 
            this.btnInsert.Location = new System.Drawing.Point(259, 39);
            this.btnInsert.Name = "btnInsert";
            this.btnInsert.Size = new System.Drawing.Size(93, 26);
            this.btnInsert.TabIndex = 7;
            this.btnInsert.Text = "插  入";
            this.btnInsert.UseVisualStyleBackColor = true;
            this.btnInsert.Click += new System.EventHandler(this.btnInsert_Click);
            // 
            // panel1
            // 
            this.panel1.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.panel1.Controls.Add(this.txtCno);
            this.panel1.Controls.Add(this.lCname);
            this.panel1.Controls.Add(this.lCno);
            this.panel1.Controls.Add(this.txtCredit);
            this.panel1.Controls.Add(this.btnInsert);
            this.panel1.Controls.Add(this.txtName);
            this.panel1.Controls.Add(this.lCreit);
            this.panel1.Location = new System.Drawing.Point(24, 13);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(372, 77);
            this.panel1.TabIndex = 11;
            // 
            // frmCourse
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(424, 284);
            this.Controls.Add(this.panel1);
            this.Controls.Add(this.btnCancel);
            this.Controls.Add(this.btnChange);
            this.Controls.Add(this.btnDelete);
            this.Controls.Add(this.dataGView);
            this.Name = "frmCourse";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "课程信息输入窗体";
            ((System.ComponentModel.ISupportInitialize)(this.dataGView)).EndInit();
            this.panel1.ResumeLayout(false);
            this.panel1.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Label lCname;
        private System.Windows.Forms.DataGridView dataGView;
        private System.Windows.Forms.TextBox txtCno;
        private System.Windows.Forms.Label lCno;
        private System.Windows.Forms.TextBox txtCredit;
        private System.Windows.Forms.TextBox txtName;
        private System.Windows.Forms.Label lCreit;
        private System.Windows.Forms.Button btnCancel;
        private System.Windows.Forms.Button btnChange;
        private System.Windows.Forms.Button btnDelete;
        private System.Windows.Forms.Button btnInsert;
        private System.Windows.Forms.Panel panel1;
    }
}