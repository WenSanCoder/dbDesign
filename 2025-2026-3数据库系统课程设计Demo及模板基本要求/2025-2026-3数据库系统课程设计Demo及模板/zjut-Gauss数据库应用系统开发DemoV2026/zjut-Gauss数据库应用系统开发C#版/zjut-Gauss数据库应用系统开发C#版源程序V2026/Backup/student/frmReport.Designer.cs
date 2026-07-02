namespace student
{
    partial class frmReport
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
            this.btnCancel = new System.Windows.Forms.Button();
            this.btnChange = new System.Windows.Forms.Button();
            this.btnDelete = new System.Windows.Forms.Button();
            this.btnInsert = new System.Windows.Forms.Button();
            this.lCreit = new System.Windows.Forms.Label();
            this.txtgrad = new System.Windows.Forms.TextBox();
            this.lname = new System.Windows.Forms.Label();
            this.dataGView = new System.Windows.Forms.DataGridView();
            this.lCname = new System.Windows.Forms.Label();
            this.cmbS = new System.Windows.Forms.ComboBox();
            this.cmbC = new System.Windows.Forms.ComboBox();
            this.panel1 = new System.Windows.Forms.Panel();
            ((System.ComponentModel.ISupportInitialize)(this.dataGView)).BeginInit();
            this.panel1.SuspendLayout();
            this.SuspendLayout();
            // 
            // btnCancel
            // 
            this.btnCancel.Location = new System.Drawing.Point(328, 251);
            this.btnCancel.Name = "btnCancel";
            this.btnCancel.Size = new System.Drawing.Size(74, 26);
            this.btnCancel.TabIndex = 21;
            this.btnCancel.Text = "退  出";
            this.btnCancel.UseVisualStyleBackColor = true;
            this.btnCancel.Click += new System.EventHandler(this.btnCancel_Click);
            // 
            // btnChange
            // 
            this.btnChange.Location = new System.Drawing.Point(250, 251);
            this.btnChange.Name = "btnChange";
            this.btnChange.Size = new System.Drawing.Size(74, 26);
            this.btnChange.TabIndex = 20;
            this.btnChange.Text = "保  存";
            this.btnChange.UseVisualStyleBackColor = true;
            this.btnChange.Click += new System.EventHandler(this.btnChange_Click);
            // 
            // btnDelete
            // 
            this.btnDelete.Location = new System.Drawing.Point(170, 251);
            this.btnDelete.Name = "btnDelete";
            this.btnDelete.Size = new System.Drawing.Size(74, 26);
            this.btnDelete.TabIndex = 19;
            this.btnDelete.Text = "删  除";
            this.btnDelete.UseVisualStyleBackColor = true;
            this.btnDelete.Click += new System.EventHandler(this.btnDelete_Click);
            // 
            // btnInsert
            // 
            this.btnInsert.Location = new System.Drawing.Point(276, 39);
            this.btnInsert.Name = "btnInsert";
            this.btnInsert.Size = new System.Drawing.Size(84, 26);
            this.btnInsert.TabIndex = 18;
            this.btnInsert.Text = "插  入";
            this.btnInsert.UseVisualStyleBackColor = true;
            this.btnInsert.Click += new System.EventHandler(this.btnInsert_Click);
            // 
            // lCreit
            // 
            this.lCreit.AutoSize = true;
            this.lCreit.Location = new System.Drawing.Point(10, 53);
            this.lCreit.Name = "lCreit";
            this.lCreit.Size = new System.Drawing.Size(53, 12);
            this.lCreit.TabIndex = 17;
            this.lCreit.Text = "课  程：";
            // 
            // txtgrad
            // 
            this.txtgrad.Location = new System.Drawing.Point(277, 4);
            this.txtgrad.Name = "txtgrad";
            this.txtgrad.Size = new System.Drawing.Size(83, 21);
            this.txtgrad.TabIndex = 16;
            // 
            // lname
            // 
            this.lname.AutoSize = true;
            this.lname.Location = new System.Drawing.Point(10, 13);
            this.lname.Name = "lname";
            this.lname.Size = new System.Drawing.Size(53, 12);
            this.lname.TabIndex = 14;
            this.lname.Text = "姓  名：";
            // 
            // dataGView
            // 
            this.dataGView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dataGView.Location = new System.Drawing.Point(31, 101);
            this.dataGView.Name = "dataGView";
            this.dataGView.RowTemplate.Height = 23;
            this.dataGView.Size = new System.Drawing.Size(372, 135);
            this.dataGView.TabIndex = 12;
            // 
            // lCname
            // 
            this.lCname.AutoSize = true;
            this.lCname.Location = new System.Drawing.Point(207, 13);
            this.lCname.Name = "lCname";
            this.lCname.Size = new System.Drawing.Size(53, 12);
            this.lCname.TabIndex = 11;
            this.lCname.Text = "成  绩：";
            // 
            // cmbS
            // 
            this.cmbS.FormattingEnabled = true;
            this.cmbS.Location = new System.Drawing.Point(82, 5);
            this.cmbS.Name = "cmbS";
            this.cmbS.Size = new System.Drawing.Size(90, 20);
            this.cmbS.TabIndex = 22;
            // 
            // cmbC
            // 
            this.cmbC.FormattingEnabled = true;
            this.cmbC.Location = new System.Drawing.Point(83, 45);
            this.cmbC.Name = "cmbC";
            this.cmbC.Size = new System.Drawing.Size(89, 20);
            this.cmbC.TabIndex = 23;
            // 
            // panel1
            // 
            this.panel1.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.panel1.Controls.Add(this.btnInsert);
            this.panel1.Controls.Add(this.cmbC);
            this.panel1.Controls.Add(this.lCname);
            this.panel1.Controls.Add(this.cmbS);
            this.panel1.Controls.Add(this.lname);
            this.panel1.Controls.Add(this.txtgrad);
            this.panel1.Controls.Add(this.lCreit);
            this.panel1.Location = new System.Drawing.Point(27, 12);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(375, 76);
            this.panel1.TabIndex = 24;
            // 
            // frmReport
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(435, 295);
            this.Controls.Add(this.panel1);
            this.Controls.Add(this.btnCancel);
            this.Controls.Add(this.btnChange);
            this.Controls.Add(this.btnDelete);
            this.Controls.Add(this.dataGView);
            this.Name = "frmReport";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "成绩信息录入窗体";
            ((System.ComponentModel.ISupportInitialize)(this.dataGView)).EndInit();
            this.panel1.ResumeLayout(false);
            this.panel1.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button btnCancel;
        private System.Windows.Forms.Button btnChange;
        private System.Windows.Forms.Button btnDelete;
        private System.Windows.Forms.Button btnInsert;
        private System.Windows.Forms.Label lCreit;
        private System.Windows.Forms.TextBox txtgrad;
        private System.Windows.Forms.Label lname;
        private System.Windows.Forms.DataGridView dataGView;
        private System.Windows.Forms.Label lCname;
        private System.Windows.Forms.ComboBox cmbS;
        private System.Windows.Forms.ComboBox cmbC;
        private System.Windows.Forms.Panel panel1;
    }
}