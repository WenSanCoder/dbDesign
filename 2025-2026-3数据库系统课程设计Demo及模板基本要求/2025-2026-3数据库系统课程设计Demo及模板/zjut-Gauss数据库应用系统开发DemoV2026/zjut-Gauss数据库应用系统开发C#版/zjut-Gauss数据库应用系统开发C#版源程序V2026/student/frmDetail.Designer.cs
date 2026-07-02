namespace student
{
    partial class frmDetail
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
            this.dgvS = new System.Windows.Forms.DataGridView();
            this.DgvP = new System.Windows.Forms.DataGridView();
            this.btnCancel = new System.Windows.Forms.Button();
            this.btnDelete = new System.Windows.Forms.Button();
            ((System.ComponentModel.ISupportInitialize)(this.dgvS)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.DgvP)).BeginInit();
            this.SuspendLayout();
            // 
            // dgvS
            // 
            this.dgvS.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dgvS.Location = new System.Drawing.Point(25, 23);
            this.dgvS.Name = "dgvS";
            this.dgvS.RowTemplate.Height = 23;
            this.dgvS.Size = new System.Drawing.Size(486, 133);
            this.dgvS.TabIndex = 0;
            this.dgvS.SelectionChanged += new System.EventHandler(this.dgvS_SelectionChanged);
            // 
            // DgvP
            // 
            this.DgvP.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.DgvP.Location = new System.Drawing.Point(25, 185);
            this.DgvP.Name = "DgvP";
            this.DgvP.RowTemplate.Height = 23;
            this.DgvP.Size = new System.Drawing.Size(486, 133);
            this.DgvP.TabIndex = 1;
            this.DgvP.CellClick += new System.Windows.Forms.DataGridViewCellEventHandler(this.DgvP_CellClick);
            // 
            // btnCancel
            // 
            this.btnCancel.Location = new System.Drawing.Point(401, 338);
            this.btnCancel.Name = "btnCancel";
            this.btnCancel.Size = new System.Drawing.Size(70, 26);
            this.btnCancel.TabIndex = 12;
            this.btnCancel.Text = "退  出";
            this.btnCancel.UseVisualStyleBackColor = true;
            this.btnCancel.Click += new System.EventHandler(this.btnCancel_Click);
            // 
            // btnDelete
            // 
            this.btnDelete.Location = new System.Drawing.Point(303, 338);
            this.btnDelete.Name = "btnDelete";
            this.btnDelete.Size = new System.Drawing.Size(70, 26);
            this.btnDelete.TabIndex = 11;
            this.btnDelete.Text = "删  除";
            this.btnDelete.UseVisualStyleBackColor = true;
            this.btnDelete.Click += new System.EventHandler(this.btnDelete_Click);
            // 
            // frmDetail
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(537, 378);
            this.Controls.Add(this.btnCancel);
            this.Controls.Add(this.btnDelete);
            this.Controls.Add(this.DgvP);
            this.Controls.Add(this.dgvS);
            this.Name = "frmDetail";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "主细表演示";
            ((System.ComponentModel.ISupportInitialize)(this.dgvS)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.DgvP)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.DataGridView dgvS;
        private System.Windows.Forms.DataGridView DgvP;
        private System.Windows.Forms.Button btnCancel;
        private System.Windows.Forms.Button btnDelete;
    }
}