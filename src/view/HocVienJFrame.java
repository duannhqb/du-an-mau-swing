/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import DAO.HocVienDAO;
import DAO.NguoiHocDAO;
import helper.DialogHelper;
import helper.Jdbc;
import helper.ShareHelper;
import java.sql.ResultSet;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import model.HocVien;
import model.NguoiHoc;

/**
 *
 * @author duann
 */
public class HocVienJFrame extends javax.swing.JFrame {

    /**
     * Creates new form HocVienJFrame
     */
    DefaultTableModel model;
    Integer MaKH;
    HocVienDAO dao = new HocVienDAO();
    NguoiHocDAO nhdao = new NguoiHocDAO();
    int diemIndex;

    public HocVienJFrame() {
        initComponents();
        init();
        pnlHVKhac.setVisible(false);
    }

    public HocVienJFrame(Integer maKH) {
        initComponents();
        this.MaKH = maKH;
        init();
        pnlHVKhac.setVisible(false);
    }

    void init() {
        setIconImage(ShareHelper.APP_ICON);
        setLocationRelativeTo(null);
        ShareHelper.DrawTable(tblGridView);
        setBoderForTable(scpTable);

        ButtonGroup selectBtn = new ButtonGroup();
        selectBtn.add(rdoTatCa);
        selectBtn.add(rdoChuaNhap);
        selectBtn.add(rdoDaNhap);

        rdoTatCa.setSelected(true);
        this.fillComboBox();
        this.load();
        txtDiem.setText("-1");
    }

    void setBoderForTable(JScrollPane scp) {
        scp.setViewportBorder(null);
        scp.setBorder(null);
    }

    void fillComboBox() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboNguoiHoc.getModel();
        if (model != null) {
            model.removeAllElements();
        }
        try {
            List<NguoiHoc> list = nhdao.selectByCourse(MaKH);
            for (NguoiHoc nguoiHoc : list) {
                model.addElement(nguoiHoc);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn học viên!");
        }
    }

    void load() {
        model = (DefaultTableModel) tblGridView.getModel();
        model.setRowCount(0);
        try {
            String sql = "SELECT HocVien.*, NguoiHoc.HoTen FROM HocVien JOIN NguoiHoc ON NguoiHoc.MaNH = HocVien.MaNH WHERE MaKH=?";
            ResultSet rs = Jdbc.executeQuery(sql, MaKH);
            while (rs.next()) {
                double diem = rs.getDouble("Diem");
                Object[] row = {
                    rs.getInt("MaHV"),
                    rs.getString("MaNH"),
                    rs.getString("HoTen"),
                    diem,
                    false
                };

                if (rdoTatCa.isSelected()) {
                    model.addRow(row);
                } else if (rdoDaNhap.isSelected() && diem >= 0) {
                    model.addRow(row);
                } else if (rdoChuaNhap.isSelected() && diem == -1) {
                    model.addRow(row);
                }
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn học viên!");
        }
    }

    boolean check() {
        if (Double.parseDouble(txtDiem.getText()) >= 0 && Double.parseDouble(txtDiem.getText()) <= 10 || Double.parseDouble(txtDiem.getText()) == -1) {
            return true;
        } else {
            DialogHelper.alert(this, "Điểm từ 0 đến 10 hoặc là -1 (chưa nhập)");
        }
        return false;
    }

    void insert() {
        if (check()) {
            NguoiHoc nh = (NguoiHoc) cboNguoiHoc.getSelectedItem();
            HocVien model = new HocVien();
            model.setMaKH(MaKH);
            model.setMaNH(nh.getMaNH());
            model.setDiem(Double.valueOf(txtDiem.getText()));

            try {
                dao.insert(model);
                this.fillComboBox();
                this.load();
                ShareHelper.setInfinity(lblMSG, "Thêm thành công!");
            } catch (Exception e) {
                DialogHelper.alert(this, "Lỗi thêm học viên vào khóa học!");
                System.out.println(e.toString());
            }
        }
    }

    void update() {
        for (int i = 0; i < tblGridView.getRowCount(); i++) {
            Integer maHV = (Integer) tblGridView.getValueAt(i, 0);
            String maNH = (String) tblGridView.getValueAt(i, 1);
            Double diem = (Double) tblGridView.getValueAt(i, 3);
            Boolean isDelete = (Boolean) tblGridView.getValueAt(i, 4);

            if (isDelete) {
                dao.delete(maHV);
            } else {
                txtDiem.setFocusable(true);
                if (diem >= 0 && diem <= 10 || diem == -1) {
                    HocVien model = new HocVien();
                    model.setMaHV(maHV);
                    model.setMaKH(MaKH);
                    model.setMaNH(maNH);
                    model.setDiem(diem);

                    dao.update(model);
                } else {
                    DialogHelper.alert(this, "Điểm từ 0 - 10 hoặc là -1 (chưa nhập)");
                    this.load();
                    return;
                }
            }
        }
        this.fillComboBox();
        this.load();
        ShareHelper.setInfinity(lblMSG, "Cập nhật thành công!");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pmn = new javax.swing.JPopupMenu();
        mniThemHV = new javax.swing.JMenuItem();
        mniAnThanhThem = new javax.swing.JMenuItem();
        pnlWapper = new javax.swing.JPanel();
        pnlHVKhac = new javax.swing.JPanel();
        cboNguoiHoc = new javax.swing.JComboBox<>();
        txtDiem = new javax.swing.JTextField();
        btnInsert = new javax.swing.JButton();
        pnlHVKH = new javax.swing.JPanel();
        scpTable = new javax.swing.JScrollPane();
        tblGridView = new javax.swing.JTable();
        rdoTatCa = new javax.swing.JRadioButton();
        rdoDaNhap = new javax.swing.JRadioButton();
        rdoChuaNhap = new javax.swing.JRadioButton();
        btnCapNhat = new javax.swing.JButton();
        lblTitleHVKH = new javax.swing.JLabel();
        lblMSG = new javax.swing.JLabel();

        mniThemHV.setText("Thêm học viên");
        mniThemHV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniThemHVActionPerformed(evt);
            }
        });
        pmn.add(mniThemHV);

        mniAnThanhThem.setText("Ẩn thanh thêm học viên");
        mniAnThanhThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniAnThanhThemActionPerformed(evt);
            }
        });
        pmn.add(mniAnThanhThem);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Quản lý học viên");
        setBackground(new java.awt.Color(255, 255, 255));

        pnlWapper.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pnlWapperMouseReleased(evt);
            }
        });

        txtDiem.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDiem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        btnInsert.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/icons8-ok-30 (1).png"))); // NOI18N
        btnInsert.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnInsert.setBorderPainted(false);
        btnInsert.setContentAreaFilled(false);
        btnInsert.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/icons8-ok-30.png"))); // NOI18N
        btnInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlHVKhacLayout = new javax.swing.GroupLayout(pnlHVKhac);
        pnlHVKhac.setLayout(pnlHVKhacLayout);
        pnlHVKhacLayout.setHorizontalGroup(
            pnlHVKhacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHVKhacLayout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(cboNguoiHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnInsert)
                .addGap(6, 6, 6))
        );
        pnlHVKhacLayout.setVerticalGroup(
            pnlHVKhacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlHVKhacLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlHVKhacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboNguoiHoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11))
            .addComponent(btnInsert, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        scpTable.setBackground(new java.awt.Color(255, 255, 255));
        scpTable.setBorder(null);

        tblGridView.setAutoCreateRowSorter(true);
        tblGridView.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MÃ HV", "MÃ NH", "HỌ TÊN", "ĐIỂM", "XÓA"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblGridView.setGridColor(new java.awt.Color(255, 255, 255));
        tblGridView.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGridViewMouseClicked(evt);
            }
        });
        scpTable.setViewportView(tblGridView);
        if (tblGridView.getColumnModel().getColumnCount() > 0) {
            tblGridView.getColumnModel().getColumn(0).setPreferredWidth(25);
            tblGridView.getColumnModel().getColumn(1).setPreferredWidth(25);
            tblGridView.getColumnModel().getColumn(2).setPreferredWidth(80);
            tblGridView.getColumnModel().getColumn(3).setPreferredWidth(10);
            tblGridView.getColumnModel().getColumn(4).setPreferredWidth(15);
        }

        rdoTatCa.setText("Tất cả");
        rdoTatCa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTatCaActionPerformed(evt);
            }
        });

        rdoDaNhap.setText("Đã nhập điểm");
        rdoDaNhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDaNhapActionPerformed(evt);
            }
        });

        rdoChuaNhap.setText("Chưa nhập điểm");
        rdoChuaNhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoChuaNhapActionPerformed(evt);
            }
        });

        btnCapNhat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/icons8-restart-30 (1).png"))); // NOI18N
        btnCapNhat.setBorderPainted(false);
        btnCapNhat.setContentAreaFilled(false);
        btnCapNhat.setDefaultCapable(false);
        btnCapNhat.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/icons8-restart-30.png"))); // NOI18N
        btnCapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlHVKHLayout = new javax.swing.GroupLayout(pnlHVKH);
        pnlHVKH.setLayout(pnlHVKHLayout);
        pnlHVKHLayout.setHorizontalGroup(
            pnlHVKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHVKHLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(pnlHVKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlHVKHLayout.createSequentialGroup()
                        .addComponent(rdoTatCa)
                        .addGap(35, 35, 35)
                        .addComponent(rdoDaNhap)
                        .addGap(29, 29, 29)
                        .addComponent(rdoChuaNhap)
                        .addGap(117, 117, 117)
                        .addComponent(btnCapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(scpTable, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        pnlHVKHLayout.setVerticalGroup(
            pnlHVKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHVKHLayout.createSequentialGroup()
                .addComponent(scpTable, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(pnlHVKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlHVKHLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(btnCapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlHVKHLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(pnlHVKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rdoTatCa)
                            .addComponent(rdoDaNhap)
                            .addComponent(rdoChuaNhap))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblTitleHVKH.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTitleHVKH.setForeground(new java.awt.Color(0, 0, 204));
        lblTitleHVKH.setText("HỌC VIÊN TRONG KHÓA");

        lblMSG.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout pnlWapperLayout = new javax.swing.GroupLayout(pnlWapper);
        pnlWapper.setLayout(pnlWapperLayout);
        pnlWapperLayout.setHorizontalGroup(
            pnlWapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlWapperLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(pnlWapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlWapperLayout.createSequentialGroup()
                        .addComponent(lblTitleHVKH, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(111, 111, 111))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlWapperLayout.createSequentialGroup()
                        .addComponent(lblMSG, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(170, 170, 170))))
            .addGroup(pnlWapperLayout.createSequentialGroup()
                .addGroup(pnlWapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlWapperLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(pnlHVKhac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlWapperLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(pnlHVKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        pnlWapperLayout.setVerticalGroup(
            pnlWapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWapperLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitleHVKH, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlHVKhac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(lblMSG, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(pnlHVKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlWapper, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlWapper, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertActionPerformed
        // TODO add your handling code here:
        try {
            this.insert();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_btnInsertActionPerformed

    private void rdoTatCaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTatCaActionPerformed
        // TODO add your handling code here:
        this.load();
    }//GEN-LAST:event_rdoTatCaActionPerformed

    private void rdoDaNhapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDaNhapActionPerformed
        // TODO add your handling code here:
        this.load();
    }//GEN-LAST:event_rdoDaNhapActionPerformed

    private void rdoChuaNhapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoChuaNhapActionPerformed
        // TODO add your handling code here:
        this.load();
    }//GEN-LAST:event_rdoChuaNhapActionPerformed

    private void btnCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatActionPerformed
        // TODO add your handling code here:
        this.update();
    }//GEN-LAST:event_btnCapNhatActionPerformed

    private void mniThemHVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniThemHVActionPerformed
        // TODO add your handling code here:
        pnlHVKhac.setVisible(true);
        lblMSG.setVisible(false);
    }//GEN-LAST:event_mniThemHVActionPerformed

    private void pnlWapperMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlWapperMouseReleased
        // TODO add your handling code here:
        if (evt.isPopupTrigger()) {
            pmn.show(this, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_pnlWapperMouseReleased

    private void mniAnThanhThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniAnThanhThemActionPerformed
        // TODO add your handling code here:
        pnlHVKhac.setVisible(false);
//        lblMSG.setVisible(true);
    }//GEN-LAST:event_mniAnThanhThemActionPerformed

    private void tblGridViewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridViewMouseClicked
        // TODO add your handling code here:
        diemIndex = tblGridView.rowAtPoint(evt.getPoint());
    }//GEN-LAST:event_tblGridViewMouseClicked

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
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HocVienJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HocVienJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HocVienJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HocVienJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HocVienJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCapNhat;
    private javax.swing.JButton btnInsert;
    private javax.swing.JComboBox<String> cboNguoiHoc;
    private javax.swing.JLabel lblMSG;
    private javax.swing.JLabel lblTitleHVKH;
    private javax.swing.JMenuItem mniAnThanhThem;
    private javax.swing.JMenuItem mniThemHV;
    private javax.swing.JPopupMenu pmn;
    private javax.swing.JPanel pnlHVKH;
    private javax.swing.JPanel pnlHVKhac;
    private javax.swing.JPanel pnlWapper;
    private javax.swing.JRadioButton rdoChuaNhap;
    private javax.swing.JRadioButton rdoDaNhap;
    private javax.swing.JRadioButton rdoTatCa;
    private javax.swing.JScrollPane scpTable;
    private javax.swing.JTable tblGridView;
    private javax.swing.JTextField txtDiem;
    // End of variables declaration//GEN-END:variables
}
