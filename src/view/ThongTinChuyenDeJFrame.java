/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import DAO.ChuyenDeDAO;
import helper.DialogHelper;
import helper.ShareHelper;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import model.ChuyenDe;
import static view.BangChuyenDeJFrame.tblGridView;

/**
 *
 * @author duann
 */
public class ThongTinChuyenDeJFrame extends javax.swing.JFrame {

    /**
     * Creates new form ThongTinChuyenDeJFrame
     */
    DefaultTableModel model;
//    String header[] = {"MÃ CD", "TÊN CD", "HỌC PHÍ", "THỜI LƯỢNG", "HÌNH"};
    JFileChooser fileChooser = new JFileChooser(ShareHelper.PATH_IMAGE);
    int index = 0;
    ChuyenDeDAO dao = new ChuyenDeDAO();

    public ThongTinChuyenDeJFrame() {
        initComponents();
        init();
        this.fillToForm();
        try {
            this.clear();
        } catch (IOException ex) {
        }
    }

    public ThongTinChuyenDeJFrame(int index) {
        initComponents();
        this.index = index;
        init();
        this.fillToForm();
    }

    void init() {
        setIconImage(ShareHelper.APP_ICON);
        setLocationRelativeTo(null);
        this.load();
        this.setStatus(true);
    }

    void clear() throws IOException {
        this.setModel(new ChuyenDe());
        this.setStatus(true);
        lblHinh.setToolTipText("");
        lblHinh.setIcon(ShareHelper.readLogo(""));
        txtThoiLuong.setText("");
        txtHocPhi.setText("");
    }

    void load() {
        model = (DefaultTableModel) tblGridView.getModel();
        model.setRowCount(0);
        try {
            List<ChuyenDe> list = dao.select();
            for (ChuyenDe chuyenDe : list) {
                Object[] row = {
                    chuyenDe.getMaCD(),
                    chuyenDe.getTenCD(),
                    chuyenDe.getHocPhi(),
                    chuyenDe.getThoiLuong(),
                    chuyenDe.getHinh()
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    void setModel(ChuyenDe model) throws IOException {
        txtMaCD.setText(model.getMaCD());
        txtHocPhi.setText(String.valueOf(model.getHocPhi()));
        txtMoTa.setText(model.getMoTa());
        txtThoiLuong.setText(String.valueOf(model.getThoiLuong()));
        txtTenCD.setText(model.getTenCD());
        lblHinh.setToolTipText(model.getHinh());
        if (model.getHinh() != null) {
            BufferedImage image = ImageIO.read(new File("logos/" + model.getHinh()));
            ImageIcon icon = new ImageIcon(fitimage(image, lblHinh.getWidth(), lblHinh.getHeight()));
            lblHinh.setIcon(icon);
        }
    }

    ChuyenDe getModel() {
        ChuyenDe model = new ChuyenDe();
        model.setMaCD(txtMaCD.getText());
        model.setTenCD(txtTenCD.getText());
        model.setThoiLuong(Integer.valueOf(txtThoiLuong.getText()));
        model.setHocPhi(Double.valueOf(txtHocPhi.getText()));
        model.setHinh(lblHinh.getToolTipText());
        model.setMoTa(txtMoTa.getText());
        return model;
    }

    void setStatus(boolean insertTable) {
        txtMaCD.setEditable(insertTable);
        txtMaCD.setDisabledTextColor(Color.white);
        btnInsert.setEnabled(insertTable);
        btnUpdate.setEnabled(!insertTable);
        btnDelete.setEnabled(!insertTable);

        boolean first = this.index > 0;
        boolean last = this.index < tblGridView.getRowCount() - 1;
        btnFirst.setEnabled(!insertTable && first);
        btnPrev.setEnabled(!insertTable && first);
        btnNext.setEnabled(!insertTable && last);
        btnLast.setEnabled(!insertTable && last);
    }

    void fillToForm() {
        try {
            String maCD = (String) tblGridView.getValueAt(this.index, 0);
            ChuyenDe model = dao.findById(maCD);
            if (model != null) {
                this.setModel(model);
                this.setStatus(false);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    void insert() {
        ChuyenDe model = getModel();
        try {
            dao.insert(model);
            this.load();
            this.clear();
            ShareHelper.setInfinity(lblMSG, "Thêm mới thành công!");
        } catch (Exception e) {
            DialogHelper.alert(this, "Thêm mới thất bại!");
        }
    }

    void update() {
        ChuyenDe model = getModel();
        try {
            dao.update(model);
            this.load();
            ShareHelper.setInfinity(lblMSG, "Cập nhật thành công!");
        } catch (Exception e) {
            DialogHelper.alert(this, "Cập nhật thất bại!");
        }
    }

    void delete() {
        if (DialogHelper.confirm(this, "Bạn có thực sự muốn xóa hay không?")) {
            String maCD = txtMaCD.getText();
            try {
                dao.delete(maCD);
                this.load();
                this.clear();
                ShareHelper.setInfinity(lblMSG, "Xóa thành công!");
            } catch (Exception e) {
                DialogHelper.alert(this, "Xóa thất bại! Bạn không thể xóa chuyên đề này vì nó đã bị ràng buộc dữ liệu với Khóa học");
                System.out.println(e.toString());
            }
        }
    }

    private Image fitimage(Image img, int w, int h) {
        BufferedImage resizedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(img, 0, 0, w, h, null);
        g2.dispose();
        return resizedImage;
    }

    void selectImage() throws IOException {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (ShareHelper.saveLogo(file)) {
                BufferedImage image = ImageIO.read(file);
                ImageIcon icon = new ImageIcon((fitimage(image, lblHinh.getWidth(), lblHinh.getHeight())));
                lblHinh.setIcon(icon);
                lblHinh.setToolTipText(file.getName());
            }
//            lưu lịch sử vị trí path
            fileChooser.setCurrentDirectory(file);
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

        pnlWrapper = new javax.swing.JPanel();
        btnLast = new javax.swing.JButton();
        lblThoiLuong = new javax.swing.JLabel();
        btnFirst = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        lblTenCD = new javax.swing.JLabel();
        btnDelete = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        lblMaCD = new javax.swing.JLabel();
        btnInsert = new javax.swing.JButton();
        pnlHinh = new javax.swing.JPanel();
        lblHinh = new javax.swing.JLabel();
        lblMoTa = new javax.swing.JLabel();
        lblTitleHinh = new javax.swing.JLabel();
        lblHocPhi = new javax.swing.JLabel();
        btnNext = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        lblMSG = new javax.swing.JLabel();
        pnlMaCD = new javax.swing.JPanel();
        txtMaCD = new javax.swing.JTextField();
        pnlTenCD = new javax.swing.JPanel();
        txtTenCD = new javax.swing.JTextField();
        pnlThoiLuong = new javax.swing.JPanel();
        txtThoiLuong = new javax.swing.JTextField();
        pnlHocPhi = new javax.swing.JPanel();
        txtHocPhi = new javax.swing.JTextField();
        pnlMoTa = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtMoTa = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Thông tin chuyên đề");

        pnlWrapper.setBackground(new java.awt.Color(255, 255, 255));

        btnLast.setBackground(new java.awt.Color(255, 255, 255));
        btnLast.setText(">|");
        btnLast.setOpaque(true);
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        lblThoiLuong.setText("Thời lượng (giờ)");

        btnFirst.setBackground(new java.awt.Color(255, 255, 255));
        btnFirst.setText("|<");
        btnFirst.setOpaque(true);
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        btnClear.setBackground(new java.awt.Color(255, 255, 255));
        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/icons8-broom-30.png"))); // NOI18N
        btnClear.setBorder(null);
        btnClear.setContentAreaFilled(false);
        btnClear.setOpaque(true);
        btnClear.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/icons8-broom-30 (1).png"))); // NOI18N
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        lblTenCD.setText("Tên chuyên đề");

        btnDelete.setBackground(new java.awt.Color(255, 255, 255));
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/icons8-cancel-30 (1).png"))); // NOI18N
        btnDelete.setBorder(null);
        btnDelete.setContentAreaFilled(false);
        btnDelete.setOpaque(true);
        btnDelete.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/icons8-cancel-30.png"))); // NOI18N
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnUpdate.setBackground(new java.awt.Color(255, 255, 255));
        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/icons8-restart-30 (1).png"))); // NOI18N
        btnUpdate.setBorder(null);
        btnUpdate.setContentAreaFilled(false);
        btnUpdate.setOpaque(true);
        btnUpdate.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/icons8-restart-30.png"))); // NOI18N
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        lblMaCD.setText("Mã chuyên đề");

        btnInsert.setBackground(new java.awt.Color(255, 255, 255));
        btnInsert.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/icons8-ok-30 (1).png"))); // NOI18N
        btnInsert.setBorder(null);
        btnInsert.setContentAreaFilled(false);
        btnInsert.setOpaque(true);
        btnInsert.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/icons8-ok-30.png"))); // NOI18N
        btnInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertActionPerformed(evt);
            }
        });

        pnlHinh.setBackground(new java.awt.Color(255, 255, 255));
        pnlHinh.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 204, 51)));

        lblHinh.setBackground(new java.awt.Color(255, 255, 255));
        lblHinh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblHinhMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlHinhLayout = new javax.swing.GroupLayout(pnlHinh);
        pnlHinh.setLayout(pnlHinhLayout);
        pnlHinhLayout.setHorizontalGroup(
            pnlHinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblHinh, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
        );
        pnlHinhLayout.setVerticalGroup(
            pnlHinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHinhLayout.createSequentialGroup()
                .addComponent(lblHinh, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        lblMoTa.setText("Mô tả chuyên đề");

        lblTitleHinh.setText("Hình logo");

        lblHocPhi.setText("Học phí");

        btnNext.setBackground(new java.awt.Color(255, 255, 255));
        btnNext.setText(">>");
        btnNext.setOpaque(true);
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnPrev.setBackground(new java.awt.Color(255, 255, 255));
        btnPrev.setText("<<");
        btnPrev.setOpaque(true);
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        lblMSG.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        pnlMaCD.setBackground(new java.awt.Color(255, 255, 255));
        pnlMaCD.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 204, 51)));

        txtMaCD.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMaCD.setBorder(null);
        txtMaCD.setDisabledTextColor(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnlMaCDLayout = new javax.swing.GroupLayout(pnlMaCD);
        pnlMaCD.setLayout(pnlMaCDLayout);
        pnlMaCDLayout.setHorizontalGroup(
            pnlMaCDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtMaCD)
        );
        pnlMaCDLayout.setVerticalGroup(
            pnlMaCDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtMaCD, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );

        pnlTenCD.setBackground(new java.awt.Color(255, 255, 255));
        pnlTenCD.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 204, 51)));

        txtTenCD.setBorder(null);

        javax.swing.GroupLayout pnlTenCDLayout = new javax.swing.GroupLayout(pnlTenCD);
        pnlTenCD.setLayout(pnlTenCDLayout);
        pnlTenCDLayout.setHorizontalGroup(
            pnlTenCDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTenCDLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTenCD)
                .addContainerGap())
        );
        pnlTenCDLayout.setVerticalGroup(
            pnlTenCDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtTenCD, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );

        pnlThoiLuong.setBackground(new java.awt.Color(255, 255, 255));
        pnlThoiLuong.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 204, 51)));
        pnlThoiLuong.setPreferredSize(new java.awt.Dimension(208, 27));

        txtThoiLuong.setBorder(null);

        javax.swing.GroupLayout pnlThoiLuongLayout = new javax.swing.GroupLayout(pnlThoiLuong);
        pnlThoiLuong.setLayout(pnlThoiLuongLayout);
        pnlThoiLuongLayout.setHorizontalGroup(
            pnlThoiLuongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThoiLuongLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtThoiLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlThoiLuongLayout.setVerticalGroup(
            pnlThoiLuongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtThoiLuong, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );

        pnlHocPhi.setBackground(new java.awt.Color(255, 255, 255));
        pnlHocPhi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 204, 51)));
        pnlHocPhi.setPreferredSize(new java.awt.Dimension(208, 27));

        txtHocPhi.setBorder(null);

        javax.swing.GroupLayout pnlHocPhiLayout = new javax.swing.GroupLayout(pnlHocPhi);
        pnlHocPhi.setLayout(pnlHocPhiLayout);
        pnlHocPhiLayout.setHorizontalGroup(
            pnlHocPhiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHocPhiLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtHocPhi, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlHocPhiLayout.setVerticalGroup(
            pnlHocPhiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtHocPhi, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );

        pnlMoTa.setBackground(new java.awt.Color(255, 255, 255));
        pnlMoTa.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 204, 51)));
        pnlMoTa.setPreferredSize(new java.awt.Dimension(208, 27));

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setBorder(null);

        txtMoTa.setColumns(20);
        txtMoTa.setRows(5);
        jScrollPane2.setViewportView(txtMoTa);

        javax.swing.GroupLayout pnlMoTaLayout = new javax.swing.GroupLayout(pnlMoTa);
        pnlMoTa.setLayout(pnlMoTaLayout);
        pnlMoTaLayout.setHorizontalGroup(
            pnlMoTaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMoTaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        pnlMoTaLayout.setVerticalGroup(
            pnlMoTaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlWrapperLayout = new javax.swing.GroupLayout(pnlWrapper);
        pnlWrapper.setLayout(pnlWrapperLayout);
        pnlWrapperLayout.setHorizontalGroup(
            pnlWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWrapperLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblMSG, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(232, 232, 232))
            .addGroup(pnlWrapperLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(pnlWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlWrapperLayout.createSequentialGroup()
                        .addGroup(pnlWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlWrapperLayout.createSequentialGroup()
                                .addGroup(pnlWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblTitleHinh)
                                    .addComponent(pnlHinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27)
                                .addGroup(pnlWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblMaCD)
                                    .addComponent(lblTenCD)
                                    .addComponent(pnlMaCD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(pnlTenCD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(pnlWrapperLayout.createSequentialGroup()
                                        .addGroup(pnlWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblThoiLuong)
                                            .addComponent(pnlThoiLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(68, 68, 68)
                                        .addGroup(pnlWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(pnlHocPhi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblHocPhi)))))
                            .addComponent(lblMoTa))
                        .addGap(0, 24, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlWrapperLayout.createSequentialGroup()
                        .addGroup(pnlWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnlWrapperLayout.createSequentialGroup()
                                .addComponent(btnInsert, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(pnlMoTa, javax.swing.GroupLayout.DEFAULT_SIZE, 712, Short.MAX_VALUE))
                        .addGap(21, 21, 21))))
        );
        pnlWrapperLayout.setVerticalGroup(
            pnlWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWrapperLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(lblMSG, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(lblTitleHinh)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlWrapperLayout.createSequentialGroup()
                        .addComponent(lblMaCD)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pnlMaCD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblTenCD)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pnlTenCD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(pnlWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblThoiLuong)
                            .addComponent(lblHocPhi))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlThoiLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pnlHocPhi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(pnlHinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(lblMoTa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlMoTa, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(pnlWrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnNext, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnPrev, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnFirst, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnClear, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnInsert, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlWrapper, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlWrapper, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        // TODO add your handling code here:
        this.index = tblGridView.getRowCount() - 1;
        this.fillToForm();
    }//GEN-LAST:event_btnLastActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        try {
            // TODO add your handling code here:
            this.clear();
        } catch (IOException ex) {
        }
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        this.delete();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        this.update();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertActionPerformed
        // TODO add your handling code here:
        this.insert();
    }//GEN-LAST:event_btnInsertActionPerformed

    private void lblHinhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHinhMouseClicked
        try {
            // TODO add your handling code here:
            this.selectImage();
        } catch (IOException ex) {
        }
    }//GEN-LAST:event_lblHinhMouseClicked

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        this.index++;
        this.fillToForm();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        // TODO add your handling code here:
        this.index--;
        this.fillToForm();
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        // TODO add your handling code here:
        this.index = 0;
        this.fillToForm();
    }//GEN-LAST:event_btnFirstActionPerformed

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
            java.util.logging.Logger.getLogger(ThongTinChuyenDeJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ThongTinChuyenDeJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ThongTinChuyenDeJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ThongTinChuyenDeJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ThongTinChuyenDeJFrame().setVisible(true);
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
    private javax.swing.JButton btnUpdate;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblHinh;
    private javax.swing.JLabel lblHocPhi;
    private javax.swing.JLabel lblMSG;
    private javax.swing.JLabel lblMaCD;
    private javax.swing.JLabel lblMoTa;
    private javax.swing.JLabel lblTenCD;
    private javax.swing.JLabel lblThoiLuong;
    private javax.swing.JLabel lblTitleHinh;
    private javax.swing.JPanel pnlHinh;
    private javax.swing.JPanel pnlHocPhi;
    private javax.swing.JPanel pnlMaCD;
    private javax.swing.JPanel pnlMoTa;
    private javax.swing.JPanel pnlTenCD;
    private javax.swing.JPanel pnlThoiLuong;
    private javax.swing.JPanel pnlWrapper;
    private javax.swing.JTextField txtHocPhi;
    private javax.swing.JTextField txtMaCD;
    private javax.swing.JTextArea txtMoTa;
    private javax.swing.JTextField txtTenCD;
    private javax.swing.JTextField txtThoiLuong;
    // End of variables declaration//GEN-END:variables
}
