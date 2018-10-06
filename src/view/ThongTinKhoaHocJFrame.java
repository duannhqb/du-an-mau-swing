/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import DAO.ChuyenDeDAO;
import DAO.KhoaHocDAO;
import helper.DialogHelper;
import helper.ShareHelper;
import helper.XDate;
import java.awt.HeadlessException;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
import model.ChuyenDe;
import model.KhoaHoc;

/**
 *
 * @author duann
 */
public class ThongTinKhoaHocJFrame extends javax.swing.JFrame {

    /**
     * Creates new form ThongTinKhoaHocJFrame
     */
    int index = 0;
    KhoaHocDAO dao = new KhoaHocDAO();
    ChuyenDeDAO cddao = new ChuyenDeDAO();

    public ThongTinKhoaHocJFrame() {
        initComponents();
        init();
        try {
            this.clear();
        } catch (Exception e) {
        }
    }

    public ThongTinKhoaHocJFrame(int index) {
        initComponents();
        this.index = index;
        init();
    }

    void init() {
        setIconImage(ShareHelper.APP_ICON);
        setLocationRelativeTo(null);
        lblMSG.setVisible(false);
        this.fillComboBox();
        this.selectComboBox();
        this.setStatus(true);
        txtNguoiTao.setEditable(false);
        txtHocPhi.setEditable(false);
        txtThoiLuong.setEditable(false);
        txtNgayTao.setEditable(false);
        this.fillToForm();
    }

    void load() {
        DefaultTableModel model = (DefaultTableModel) MainProJFrame.tblKhoaHoc.getModel();
        model.setRowCount(0);
        try {
            List<KhoaHoc> list = dao.select();
            for (KhoaHoc khoaHoc : list) {
                Object[] row = {
                    khoaHoc.getMaKH(),
                    khoaHoc.getMaCD(),
                    khoaHoc.getThoiLuong(),
                    khoaHoc.getHocPhi(),
                    XDate.toString(khoaHoc.getNgayKG()),
                    khoaHoc.getMaNV(),
                    XDate.toString(khoaHoc.getNgayTao())
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    void setModel(KhoaHoc model) {
        try {
            cboChuyenDe.setToolTipText(String.valueOf(model.getMaKH()));
            ChuyenDe cd = cddao.findById(model.getMaCD());
            cboChuyenDe.setSelectedItem(cd.getTenCD());
            txtNgayKhaiGiang.setText(XDate.toString(model.getNgayKG()));
            txtHocPhi.setText(String.valueOf(model.getHocPhi()));
            txtThoiLuong.setText(String.valueOf(model.getThoiLuong()));
            txtNguoiTao.setText(model.getMaNV());
            txtNgayTao.setText(XDate.toString(model.getNgayTao()));
            txtGhiChu.setText(model.getGhiChu());
        } catch (Exception e) {
        }
    }

    KhoaHoc getModel() {
        KhoaHoc model = new KhoaHoc();
        String tencd = (String) cboChuyenDe.getSelectedItem();
        ChuyenDe cd = cddao.findByName(tencd);
        model.setMaCD(cd.getMaCD());
        model.setNgayKG(XDate.toDate(txtNgayKhaiGiang.getText()));
        model.setHocPhi(Double.valueOf(txtHocPhi.getText()));
        model.setThoiLuong(Integer.parseInt(txtThoiLuong.getText()));
        model.setGhiChu(txtGhiChu.getText());
        model.setMaNV(txtNguoiTao.getText());
        model.setNgayTao(XDate.toDate(txtNgayTao.getText()));
        model.setMaKH(Integer.parseInt(cboChuyenDe.getToolTipText()));
        return model;
    }

    void setStatus(boolean insertTable) {
        try {
            btnInsert.setEnabled(insertTable);
            btnUpdate.setEnabled(!insertTable);
            btnDelete.setEnabled(!insertTable);

            boolean first = this.index > 0;
            boolean last = this.index < MainProJFrame.tblKhoaHoc.getRowCount() - 1;
            btnFirst.setEnabled(!insertTable && first);
            btnPrev.setEnabled(!insertTable && first);
            btnNext.setEnabled(!insertTable && last);
            btnLast.setEnabled(!insertTable && last);

            btnStudent.setVisible(!insertTable);
        } catch (Exception e) {
        }
    }

    void selectComboBox() {
        try {
            String tencd = (String) cboChuyenDe.getSelectedItem();
            ChuyenDe cd = cddao.findByName(tencd);
            txtThoiLuong.setText(String.valueOf(cd.getThoiLuong()));
            txtHocPhi.setText(String.valueOf(cd.getHocPhi()));
        } catch (Exception e) {
        }
    }

    void openHocVien() {
        new HocVienJFrame(Integer.valueOf(cboChuyenDe.getToolTipText())).setVisible(true);
    }

    void fillComboBox() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboChuyenDe.getModel();
        if (model != null) {
            model.removeAllElements();
        }
        try {
            List<ChuyenDe> list = cddao.select();
            for (ChuyenDe chuyenDe : list) {
                model.addElement(chuyenDe.getTenCD());
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    void fillToForm() {
        try {
            Integer maKH = (Integer) MainProJFrame.tblKhoaHoc.getValueAt(this.index, 0);
            KhoaHoc model = dao.findById(maKH);
            if (model != null) {
                this.setModel(model);
                this.setStatus(false);
            }
        } catch (Exception e) {
        }
    }

    void clear() {
        try {
            String tencd = (String) cboChuyenDe.getSelectedItem();
            ChuyenDe cd = cddao.findByName(tencd);

            KhoaHoc model = new KhoaHoc();
            model.setMaCD(cd.getMaCD());
            model.setMaNV(ShareHelper.USER.getMaNV());
            model.setNgayKG(XDate.add(30));
            model.setNgayTao(XDate.now());
            this.setModel(model);

            this.setStatus(true);
            this.selectComboBox();
            this.cboChuyenDe.setSelectedIndex(0);
        } catch (Exception e) {
        }
    }

    void insert() {
        KhoaHoc model = getModel();
        model.setNgayTao(new Date());
        try {
            dao.insert(model);
            this.load();
            this.clear();
            ShareHelper.setInfinity(lblMSG, "Thêm mới thành công!");
        } catch (HeadlessException e) {
            DialogHelper.alert(this, "Thêm mới thất bại!");
        }
    }

    void update() {
        KhoaHoc model = getModel();
        try {
            dao.update(model);
            this.load();
            ShareHelper.setInfinity(lblMSG, "Cập nhật thành công!");
        } catch (Exception e) {
            DialogHelper.alert(this, "Cập nhật thất bại!");
            System.out.println("error: " + e.toString());
        }
    }

    void delete() {
        if (DialogHelper.confirm(this, "Bạn có thực sự muốn xóa khóa học này?")) {
            Integer maKH = Integer.valueOf(cboChuyenDe.getToolTipText());
            try {
                dao.delete(maKH);
                this.load();
                this.clear();
                ShareHelper.setInfinity(lblMSG, "Xóa thành công!");
            } catch (Exception e) {
                DialogHelper.alert(this, "Xóa thất bại!");
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlWapper = new javax.swing.JPanel();
        btnInsert = new javax.swing.JButton();
        lblHocPhi = new javax.swing.JLabel();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        lblNguoiTao = new javax.swing.JLabel();
        btnClear = new javax.swing.JButton();
        btnFirst = new javax.swing.JButton();
        lblThoiLuong = new javax.swing.JLabel();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        lblChuyenDe = new javax.swing.JLabel();
        lblNgayTao = new javax.swing.JLabel();
        btnLast = new javax.swing.JButton();
        cboChuyenDe = new javax.swing.JComboBox<>();
        lblGhiChu = new javax.swing.JLabel();
        btnStudent = new javax.swing.JButton();
        lblNgayKhaiGiang = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtGhiChu = new javax.swing.JTextArea();
        lblMSG = new javax.swing.JLabel();
        txtNgayKhaiGiang = new javax.swing.JTextField();
        txtHocPhi = new javax.swing.JTextField();
        txtThoiLuong = new javax.swing.JTextField();
        txtNguoiTao = new javax.swing.JTextField();
        txtNgayTao = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Thông tin khóa học");

        btnInsert.setText("Thêm");
        btnInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertActionPerformed(evt);
            }
        });

        lblHocPhi.setText("Học phí");

        btnUpdate.setText("Sửa");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setText("Xóa");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        lblNguoiTao.setText("Người tạo");

        btnClear.setText("Mới");
        btnClear.setToolTipText("");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnFirst.setText("|<");
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        lblThoiLuong.setText("Thời lượng (giờ)");

        btnPrev.setText("<<");
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        btnNext.setText(">>");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        lblChuyenDe.setText("Chuyên đề");

        lblNgayTao.setText("Ngày tạo");

        btnLast.setText(">|");
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        cboChuyenDe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboChuyenDeActionPerformed(evt);
            }
        });

        lblGhiChu.setText("Ghi chú");

        btnStudent.setText("Học Viên");
        btnStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStudentActionPerformed(evt);
            }
        });

        lblNgayKhaiGiang.setText("Ngày khai giảng");

        txtGhiChu.setColumns(20);
        txtGhiChu.setRows(5);
        jScrollPane1.setViewportView(txtGhiChu);

        lblMSG.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        txtHocPhi.setMinimumSize(new java.awt.Dimension(0, 20));

        javax.swing.GroupLayout pnlWapperLayout = new javax.swing.GroupLayout(pnlWapper);
        pnlWapper.setLayout(pnlWapperLayout);
        pnlWapperLayout.setHorizontalGroup(
            pnlWapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWapperLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(pnlWapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlWapperLayout.createSequentialGroup()
                        .addComponent(btnInsert)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnUpdate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnClear)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 99, Short.MAX_VALUE)
                        .addComponent(btnStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnPrev)
                        .addGap(10, 10, 10)
                        .addComponent(btnNext)
                        .addGap(10, 10, 10)
                        .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1)
                    .addComponent(lblChuyenDe)
                    .addComponent(lblGhiChu)
                    .addGroup(pnlWapperLayout.createSequentialGroup()
                        .addGroup(pnlWapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblNguoiTao)
                            .addComponent(lblHocPhi)
                            .addComponent(cboChuyenDe, 0, 311, Short.MAX_VALUE)
                            .addComponent(txtHocPhi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtNguoiTao))
                        .addGap(57, 57, 57)
                        .addGroup(pnlWapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtThoiLuong, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                            .addComponent(lblNgayKhaiGiang, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblThoiLuong, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNgayTao, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNgayTao, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNgayKhaiGiang)))
                    .addComponent(lblMSG, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        pnlWapperLayout.setVerticalGroup(
            pnlWapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWapperLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMSG, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(pnlWapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblChuyenDe)
                    .addComponent(lblNgayKhaiGiang))
                .addGap(11, 11, 11)
                .addGroup(pnlWapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cboChuyenDe)
                    .addComponent(txtNgayKhaiGiang))
                .addGap(19, 19, 19)
                .addGroup(pnlWapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHocPhi)
                    .addComponent(lblThoiLuong))
                .addGap(11, 11, 11)
                .addGroup(pnlWapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(txtHocPhi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtThoiLuong))
                .addGap(18, 18, 18)
                .addGroup(pnlWapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNguoiTao)
                    .addComponent(lblNgayTao))
                .addGap(11, 11, 11)
                .addGroup(pnlWapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtNguoiTao)
                    .addComponent(txtNgayTao))
                .addGap(18, 18, 18)
                .addComponent(lblGhiChu)
                .addGap(11, 11, 11)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlWapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnFirst, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPrev, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNext, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlWapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlWapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnInsert))
                        .addGroup(pnlWapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnStudent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(btnLast, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlWapper, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlWapper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertActionPerformed
        // TODO add your handling code here:
        this.insert();
    }//GEN-LAST:event_btnInsertActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        this.update();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        this.delete();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        this.clear();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        // TODO add your handling code here:
        this.index = 0;
        this.fillToForm();
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        // TODO add your handling code here:
        this.index--;
        this.fillToForm();
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        this.index++;
        this.fillToForm();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        // TODO add your handling code here:
        this.index = MainProJFrame.tblKhoaHoc.getRowCount() - 1;
        this.fillToForm();
    }//GEN-LAST:event_btnLastActionPerformed

    private void cboChuyenDeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboChuyenDeActionPerformed
        // TODO add your handling code here:
        this.selectComboBox();
    }//GEN-LAST:event_cboChuyenDeActionPerformed

    private void btnStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStudentActionPerformed
        // TODO add your handling code here:
        this.openHocVien();
    }//GEN-LAST:event_btnStudentActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ThongTinKhoaHocJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ThongTinKhoaHocJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ThongTinKhoaHocJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ThongTinKhoaHocJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ThongTinKhoaHocJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnInsert;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnStudent;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cboChuyenDe;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblChuyenDe;
    private javax.swing.JLabel lblGhiChu;
    private javax.swing.JLabel lblHocPhi;
    private javax.swing.JLabel lblMSG;
    private javax.swing.JLabel lblNgayKhaiGiang;
    private javax.swing.JLabel lblNgayTao;
    private javax.swing.JLabel lblNguoiTao;
    private javax.swing.JLabel lblThoiLuong;
    private javax.swing.JPanel pnlWapper;
    private javax.swing.JTextArea txtGhiChu;
    private javax.swing.JTextField txtHocPhi;
    private javax.swing.JTextField txtNgayKhaiGiang;
    private javax.swing.JTextField txtNgayTao;
    private javax.swing.JTextField txtNguoiTao;
    private javax.swing.JTextField txtThoiLuong;
    // End of variables declaration//GEN-END:variables
}
