/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import DAO.ChuyenDeDAO;
import DAO.KhoaHocDAO;
import DAO.NguoiHocDAO;
import DAO.ThongKeDAO;
import helper.DialogHelper;
import helper.ShareHelper;
import helper.XDate;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import model.ChuyenDe;
import model.KhoaHoc;
import model.NguoiHoc;

/**
 *
 * @author duann
 */
public class MainProJFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainProJFrame
     */
    int mouseX;
    int mouseY;
    int index;

    ThongKeDAO tkdao = new ThongKeDAO();
    KhoaHocDAO khdao = new KhoaHocDAO();
    ChuyenDeDAO cddao = new ChuyenDeDAO();
    NguoiHocDAO nhdao = new NguoiHocDAO();

    public MainProJFrame() {
        initComponents();
        init();
    }

    void init() {
        setIconImage(ShareHelper.APP_ICON);
        setLocationRelativeTo(null);
        new Timer(1000, new ActionListener() {
            SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss a");

            @Override
            public void actionPerformed(ActionEvent e) {
                lblDongHo.setText(format.format(new Date()));
            }
        }).start();

        ShareHelper.setCrollRight();
        this.openWelcome();
        this.openLogin();

//        chào
        lblChao.setText("Chào: " + ShareHelper.USER.getHoTen());

        playMusic();
        popMenu();

        ShareHelper.DrawTable(tblThongKeNguoiHoc);
        ShareHelper.DrawTable(tblBangDiem);
        ShareHelper.DrawTable(tblTongHop);
        ShareHelper.DrawTable(tblDoanhThu);
//        ShareHelper.DrawTable(tblNhanVien);
        ShareHelper.DrawTable(tblChuyenDe);
        ShareHelper.DrawTable(tblKhoaHoc);
        ShareHelper.DrawTable(tblNguoiHoc);

//        Thống kê
        fillTableNguoiHoc();
        fillComboBoxKhoaHoc();
        fillTableKhoaHoc();
        fillTableBangDiem();
        fillComboBoxNam();
        fillTableDoanhThu();

//        Chuyên đề
        this.loadChuyenDe();

//        Người học
        this.loadNguoiHoc();

//        Khóa học
        this.loadKhoaHoc();
    }

    void popMenu() {
        mniChuyenDe.setText("Chuyên đề");
        mniKhoaHoc.setText("Khóa học");
        mniNguoiHoc.setText("Người học");
        mniNhanVien.setText("Nhân viên");
        mniThongKe.setText("Thống kê");
        mniThongTin.setText("Thông tin");
        mniTrangChu.setText("Trang chủ");
    }

    void playMusic() {
    }

    void openLogin() {
        new DangNhapJDialog(this, true).setVisible(true);
        this.setVisible(true);
    }

    void openWelcome() {
        new ChaoJDialog(this, true).setVisible(true);
    }

    void Exit() {
        if (DialogHelper.confirm(this, "Bạn có chắc chắn muốn kết thúc chương trình")) {
            System.exit(0);
        }
    }

    void logOff() {
        ShareHelper.logOff();
        this.dispose();
        this.openLogin();
    }

    void openAbout() {
        new AboutJDialog(this, true).setVisible(true);
    }

    void openWebsite() throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(getClass().getResource("/helper/index.html").toURI());
    }

    private void showPopMenu(java.awt.event.MouseEvent evt) {
        if (evt.isPopupTrigger()) {
            pmn.show(this, evt.getX() + 275, evt.getY() + 32);
        }
    }

    private void cardLayout(JPanel pnl1, JPanel pnl2) {
//        ẩn hiện tab bên phải
        pnlTrangChu.setVisible(false);
        pnlTableChuyenDe.setVisible(false);
        pnlTableKhoaHoc.setVisible(false);
        pnlTableNguoiHoc.setVisible(false);
        pnlTableNhanVien.setVisible(false);
        pnlTableThongKe.setVisible(false);
        pnlThongTin.setVisible(false);
        pnl1.setVisible(true);

//        set màu nền khi nhấn vào menu bên trái
        pnlHome.setBackground(new Color(102, 176, 249));
        pnlChuyenDe.setBackground(new Color(102, 176, 249));
        pnlAbout.setBackground(new Color(102, 176, 249));
        pnlKhoaHoc.setBackground(new Color(102, 176, 249));
        pnlNguoiHoc.setBackground(new Color(102, 176, 249));
        pnlNhanVien.setBackground(new Color(102, 176, 249));
        pnlThongKe.setBackground(new Color(102, 176, 249));
        pnl2.setBackground(new Color(113, 208, 249));

//        tắt boder ở table
        setBoderForTable(scpChuyenDe);
        setBoderForTable(scpKhoaHoc);
        setBoderForTable(scpNguoiHoc);
        setBoderForTable(scpNhanVien);
        setBoderForTable(scpTongHopNH);
        setBoderForTable(scpTongHopDiem);
        setBoderForTable(scpBangDiem);
        setBoderForTable(scpDoanhThu);
    }

    void setBoderForTable(JScrollPane scp) {
        scp.setViewportBorder(null);
        scp.setBorder(null);
    }

//   TAB chuyên đề
    void loadChuyenDe() {
        DefaultTableModel model = (DefaultTableModel) tblChuyenDe.getModel();
        model.setRowCount(0);
        try {
            String keyWord = txtFindChuyenDe.getText();
            List<ChuyenDe> list = cddao.selectByKeyword(keyWord);
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

//    TAB người học
    void loadNguoiHoc() {
        DefaultTableModel model = (DefaultTableModel) tblNguoiHoc.getModel();
        model.setRowCount(0);
        try {
            String keyword = txtFindNguoiHoc.getText();
            List<NguoiHoc> list = nhdao.selectByKeyword(keyword);
            for (NguoiHoc nh : list) {
                Object[] row = {
                    nh.getMaNH(),
                    nh.getHoTen(),
                    nh.isGioiTinh() ? "Nam" : "Nữ",
                    XDate.toString(nh.getNgaySinh()),
                    nh.getDienThoai(),
                    nh.getEmail(),
                    nh.getMaNV(),
                    XDate.toString(nh.getNgayDK())
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

//    TAB khóa học
    void loadKhoaHoc() {
        DefaultTableModel model = (DefaultTableModel) tblKhoaHoc.getModel();
        model.setRowCount(0);
        try {
            String keyWord = txtFindKhoaHoc.getText();
            List<KhoaHoc> list = khdao.selectByKeyword(keyWord);
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

// TAB tổng hợp    
//    combobox khoa hoc
    void fillComboBoxKhoaHoc() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboKhoaHoc.getModel();
        if (model != null) {
            model.removeAllElements();
        }
        List<KhoaHoc> list = khdao.select();
        for (KhoaHoc khoaHoc : list) {
            model.addElement(khoaHoc);
        }
        cboKhoaHoc.setSelectedIndex(0);
    }

//    combobox nam
    void fillComboBoxNam() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboNam.getModel();
        if (model != null) {
            model.removeAllElements();
        }
        List<KhoaHoc> list = khdao.select();
        for (KhoaHoc khoaHoc : list) {
            int nam = khoaHoc.getNgayKG().getYear() + 1900;
            if (model.getIndexOf(nam) < 0) {
                model.addElement(nam);
            }
        }
        cboNam.setSelectedIndex(0);
    }

//    table thong ke diem
    void fillTableBangDiem() {
        DefaultTableModel model = (DefaultTableModel) tblBangDiem.getModel();
        model.setRowCount(0);
        KhoaHoc kh = (KhoaHoc) cboKhoaHoc.getSelectedItem();
        List<Object[]> list = tkdao.getBangDiem(kh.getMaKH());
        for (Object[] objects : list) {
            model.addRow(objects);
        }
    }

//    table thong ke nguoi hoc
    void fillTableNguoiHoc() {
        DefaultTableModel model = (DefaultTableModel) tblThongKeNguoiHoc.getModel();
        model.setRowCount(0);
        List<Object[]> list = tkdao.getNguoiHoc();
        for (Object[] objects : list) {
            model.addRow(objects);
        }
    }

//    table thong ke khoa hoc
    void fillTableKhoaHoc() {
        DefaultTableModel model = (DefaultTableModel) tblTongHop.getModel();
        model.setRowCount(0);
        List<Object[]> list = tkdao.getDiemTheoChuyenDe();
        for (Object[] objects : list) {
            model.addRow(objects);
        }
    }

    void fillTableDoanhThu() {
        if (ShareHelper.USER.isVaiTro() == false) {
            pnlDoanhThu.setVisible(false);
            pnlDoanhThu.setToolTipText("Bạn không đủ quyền để xem doanh thu!");
            tblDoanhThu.setToolTipText("Bạn không đủ quyền để xem doanh thu!");
            cboNam.setToolTipText("Bạn không đủ quyền để xem doanh thu!");
        } else {
            pnlDoanhThu.setVisible(true);
            DefaultTableModel model = (DefaultTableModel) tblDoanhThu.getModel();
            model.setRowCount(0);
            int nam = Integer.parseInt(cboNam.getSelectedItem().toString());
            List<Object[]> list = tkdao.getDoanhThu(nam);
            for (Object[] objects : list) {
                model.addRow(objects);
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

        pmn = new javax.swing.JPopupMenu();
        mniTrangChu = new javax.swing.JMenuItem();
        mniChuyenDe = new javax.swing.JMenuItem();
        mniKhoaHoc = new javax.swing.JMenuItem();
        mniNguoiHoc = new javax.swing.JMenuItem();
        mniNhanVien = new javax.swing.JMenuItem();
        mniThongKe = new javax.swing.JMenuItem();
        mniThongTin = new javax.swing.JMenuItem();
        pnlWapper = new javax.swing.JPanel();
        pnlThanhDieuHuong = new javax.swing.JPanel();
        pnlThongTinNhanVien = new javax.swing.JPanel();
        lblDongHo = new javax.swing.JLabel();
        btnDangXuat = new javax.swing.JButton();
        lblChao = new javax.swing.JLabel();
        pnlDieuHuong = new javax.swing.JPanel();
        pnlHome = new javax.swing.JPanel();
        btnHome = new javax.swing.JButton();
        pnlChuyenDe = new javax.swing.JPanel();
        btnChuyenDe = new javax.swing.JButton();
        pnlKhoaHoc = new javax.swing.JPanel();
        btnKhoaHoc = new javax.swing.JButton();
        pnlNguoiHoc = new javax.swing.JPanel();
        btnNguoiHoc = new javax.swing.JButton();
        pnlNhanVien = new javax.swing.JPanel();
        btnNhanVien = new javax.swing.JButton();
        pnlThongKe = new javax.swing.JPanel();
        btnThongKe = new javax.swing.JButton();
        pnlAbout = new javax.swing.JPanel();
        btnAbout = new javax.swing.JButton();
        tabs = new javax.swing.JPanel();
        pnlTrangChu = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        pnlTableChuyenDe = new javax.swing.JPanel();
        pnlWapperChuyenDe = new javax.swing.JPanel();
        lblTitleChuyenDe = new javax.swing.JLabel();
        scpChuyenDe = new javax.swing.JScrollPane();
        tblChuyenDe = new javax.swing.JTable();
        pnlFindChuyenDe = new javax.swing.JPanel();
        txtFindChuyenDe = new javax.swing.JTextField();
        lblFindChuyenDe = new javax.swing.JLabel();
        pnlTableKhoaHoc = new javax.swing.JPanel();
        pnlWapperKhoaHoc = new javax.swing.JPanel();
        lblTitleKhoaHoc = new javax.swing.JLabel();
        scpKhoaHoc = new javax.swing.JScrollPane();
        tblKhoaHoc = new javax.swing.JTable();
        pnlFindKhoaHoc = new javax.swing.JPanel();
        txtFindKhoaHoc = new javax.swing.JTextField();
        lblFindKhoaHoc = new javax.swing.JLabel();
        pnlTableNguoiHoc = new javax.swing.JPanel();
        pnlWapperNguoiHoc = new javax.swing.JPanel();
        pnlFindNguoiHoc = new javax.swing.JPanel();
        txtFindNguoiHoc = new javax.swing.JTextField();
        btnFindNguoiHoc = new javax.swing.JButton();
        lblFindNguoiHoc = new javax.swing.JLabel();
        scpNguoiHoc = new javax.swing.JScrollPane();
        tblNguoiHoc = new javax.swing.JTable();
        pnlTableNhanVien = new javax.swing.JPanel();
        scpNhanVien = new javax.swing.JScrollPane();
        tblNhanVien = new javax.swing.JTable();
        pnlTableThongKe = new javax.swing.JPanel();
        pnlWapper1 = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        tabsThongKe = new javax.swing.JTabbedPane();
        pnlThongKeNguoiHoc = new javax.swing.JPanel();
        scpTongHopNH = new javax.swing.JScrollPane();
        tblThongKeNguoiHoc = new javax.swing.JTable();
        pnlBangDiem = new javax.swing.JPanel();
        lblKhoaHoc = new javax.swing.JLabel();
        cboKhoaHoc = new javax.swing.JComboBox<>();
        scpBangDiem = new javax.swing.JScrollPane();
        tblBangDiem = new javax.swing.JTable();
        pnlTongHopDiem = new javax.swing.JPanel();
        scpTongHopDiem = new javax.swing.JScrollPane();
        tblTongHop = new javax.swing.JTable();
        pnlDoanhThu = new javax.swing.JPanel();
        lblNam = new javax.swing.JLabel();
        cboNam = new javax.swing.JComboBox<>();
        scpDoanhThu = new javax.swing.JScrollPane();
        tblDoanhThu = new javax.swing.JTable();
        pnlThongTin = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        btnExit = new javax.swing.JButton();
        lblTopTitle = new javax.swing.JLabel();

        mniTrangChu.setText("jMenuItem1");
        mniTrangChu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniTrangChuActionPerformed(evt);
            }
        });
        pmn.add(mniTrangChu);

        mniChuyenDe.setText("jMenuItem1");
        mniChuyenDe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniChuyenDeActionPerformed(evt);
            }
        });
        pmn.add(mniChuyenDe);

        mniKhoaHoc.setText("jMenuItem1");
        mniKhoaHoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniKhoaHocActionPerformed(evt);
            }
        });
        pmn.add(mniKhoaHoc);

        mniNguoiHoc.setText("jMenuItem1");
        mniNguoiHoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniNguoiHocActionPerformed(evt);
            }
        });
        pmn.add(mniNguoiHoc);

        mniNhanVien.setText("jMenuItem1");
        mniNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniNhanVienActionPerformed(evt);
            }
        });
        pmn.add(mniNhanVien);

        mniThongKe.setText("jMenuItem1");
        mniThongKe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniThongKeActionPerformed(evt);
            }
        });
        pmn.add(mniThongKe);

        mniThongTin.setText("jMenuItem1");
        mniThongTin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniThongTinActionPerformed(evt);
            }
        });
        pmn.add(mniThongTin);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        pnlWapper.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pnlWapperMouseReleased(evt);
            }
        });

        pnlThanhDieuHuong.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pnlThanhDieuHuongMouseReleased(evt);
            }
        });

        pnlThongTinNhanVien.setBackground(new java.awt.Color(51, 153, 255));

        btnDangXuat.setForeground(new java.awt.Color(255, 255, 255));
        btnDangXuat.setText("Đăng xuất");
        btnDangXuat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        btnDangXuat.setContentAreaFilled(false);
        btnDangXuat.setDefaultCapable(false);
        btnDangXuat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangXuatActionPerformed(evt);
            }
        });

        lblChao.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblChao.setForeground(new java.awt.Color(255, 255, 255));
        lblChao.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblChao.setText("Chào");

        javax.swing.GroupLayout pnlThongTinNhanVienLayout = new javax.swing.GroupLayout(pnlThongTinNhanVien);
        pnlThongTinNhanVien.setLayout(pnlThongTinNhanVienLayout);
        pnlThongTinNhanVienLayout.setHorizontalGroup(
            pnlThongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThongTinNhanVienLayout.createSequentialGroup()
                .addGroup(pnlThongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThongTinNhanVienLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDangXuat, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlThongTinNhanVienLayout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(lblDongHo, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(pnlThongTinNhanVienLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(lblChao, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlThongTinNhanVienLayout.setVerticalGroup(
            pnlThongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThongTinNhanVienLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDongHo, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblChao)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(btnDangXuat, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlDieuHuong.setBackground(new java.awt.Color(51, 153, 255));

        pnlHome.setBackground(new java.awt.Color(102, 176, 249));

        btnHome.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnHome.setText("TRANG CHỦ");
        btnHome.setBorderPainted(false);
        btnHome.setContentAreaFilled(false);
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlHomeLayout = new javax.swing.GroupLayout(pnlHome);
        pnlHome.setLayout(pnlHomeLayout);
        pnlHomeLayout.setHorizontalGroup(
            pnlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnHome, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
        );
        pnlHomeLayout.setVerticalGroup(
            pnlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHomeLayout.createSequentialGroup()
                .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnlChuyenDe.setBackground(new java.awt.Color(102, 176, 249));

        btnChuyenDe.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnChuyenDe.setText("CHUYÊN ĐỀ");
        btnChuyenDe.setBorderPainted(false);
        btnChuyenDe.setContentAreaFilled(false);
        btnChuyenDe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChuyenDeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlChuyenDeLayout = new javax.swing.GroupLayout(pnlChuyenDe);
        pnlChuyenDe.setLayout(pnlChuyenDeLayout);
        pnlChuyenDeLayout.setHorizontalGroup(
            pnlChuyenDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(pnlChuyenDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(btnChuyenDe, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
        );
        pnlChuyenDeLayout.setVerticalGroup(
            pnlChuyenDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(pnlChuyenDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlChuyenDeLayout.createSequentialGroup()
                    .addComponent(btnChuyenDe, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pnlKhoaHoc.setBackground(new java.awt.Color(102, 176, 249));

        btnKhoaHoc.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnKhoaHoc.setText("KHÓA HỌC");
        btnKhoaHoc.setBorderPainted(false);
        btnKhoaHoc.setContentAreaFilled(false);
        btnKhoaHoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKhoaHocActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlKhoaHocLayout = new javax.swing.GroupLayout(pnlKhoaHoc);
        pnlKhoaHoc.setLayout(pnlKhoaHocLayout);
        pnlKhoaHocLayout.setHorizontalGroup(
            pnlKhoaHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(pnlKhoaHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(btnKhoaHoc, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))
        );
        pnlKhoaHocLayout.setVerticalGroup(
            pnlKhoaHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(pnlKhoaHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlKhoaHocLayout.createSequentialGroup()
                    .addComponent(btnKhoaHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pnlNguoiHoc.setBackground(new java.awt.Color(102, 176, 249));

        btnNguoiHoc.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnNguoiHoc.setText("NGƯỜI HỌC");
        btnNguoiHoc.setBorderPainted(false);
        btnNguoiHoc.setContentAreaFilled(false);
        btnNguoiHoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNguoiHocActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlNguoiHocLayout = new javax.swing.GroupLayout(pnlNguoiHoc);
        pnlNguoiHoc.setLayout(pnlNguoiHocLayout);
        pnlNguoiHocLayout.setHorizontalGroup(
            pnlNguoiHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(pnlNguoiHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(btnNguoiHoc, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))
        );
        pnlNguoiHocLayout.setVerticalGroup(
            pnlNguoiHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(pnlNguoiHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlNguoiHocLayout.createSequentialGroup()
                    .addComponent(btnNguoiHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pnlNhanVien.setBackground(new java.awt.Color(102, 176, 249));

        btnNhanVien.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnNhanVien.setText("NHÂN VIÊN");
        btnNhanVien.setBorderPainted(false);
        btnNhanVien.setContentAreaFilled(false);
        btnNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNhanVienActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlNhanVienLayout = new javax.swing.GroupLayout(pnlNhanVien);
        pnlNhanVien.setLayout(pnlNhanVienLayout);
        pnlNhanVienLayout.setHorizontalGroup(
            pnlNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(pnlNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(btnNhanVien, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
        );
        pnlNhanVienLayout.setVerticalGroup(
            pnlNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(pnlNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlNhanVienLayout.createSequentialGroup()
                    .addComponent(btnNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pnlThongKe.setBackground(new java.awt.Color(102, 176, 249));

        btnThongKe.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnThongKe.setText("THỐNG KÊ");
        btnThongKe.setBorderPainted(false);
        btnThongKe.setContentAreaFilled(false);
        btnThongKe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThongKeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlThongKeLayout = new javax.swing.GroupLayout(pnlThongKe);
        pnlThongKe.setLayout(pnlThongKeLayout);
        pnlThongKeLayout.setHorizontalGroup(
            pnlThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(pnlThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(btnThongKe, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
        );
        pnlThongKeLayout.setVerticalGroup(
            pnlThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(pnlThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlThongKeLayout.createSequentialGroup()
                    .addComponent(btnThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pnlAbout.setBackground(new java.awt.Color(102, 176, 249));

        btnAbout.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnAbout.setText("THÔNG TIN");
        btnAbout.setBorderPainted(false);
        btnAbout.setContentAreaFilled(false);
        btnAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAboutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlAboutLayout = new javax.swing.GroupLayout(pnlAbout);
        pnlAbout.setLayout(pnlAboutLayout);
        pnlAboutLayout.setHorizontalGroup(
            pnlAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(pnlAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(btnAbout, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
        );
        pnlAboutLayout.setVerticalGroup(
            pnlAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
            .addGroup(pnlAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlAboutLayout.createSequentialGroup()
                    .addComponent(btnAbout, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout pnlDieuHuongLayout = new javax.swing.GroupLayout(pnlDieuHuong);
        pnlDieuHuong.setLayout(pnlDieuHuongLayout);
        pnlDieuHuongLayout.setHorizontalGroup(
            pnlDieuHuongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlHome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlChuyenDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlKhoaHoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlThongKe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlAbout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlNguoiHoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlDieuHuongLayout.setVerticalGroup(
            pnlDieuHuongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDieuHuongLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(pnlHome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlChuyenDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlKhoaHoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlNguoiHoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlAbout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        javax.swing.GroupLayout pnlThanhDieuHuongLayout = new javax.swing.GroupLayout(pnlThanhDieuHuong);
        pnlThanhDieuHuong.setLayout(pnlThanhDieuHuongLayout);
        pnlThanhDieuHuongLayout.setHorizontalGroup(
            pnlThanhDieuHuongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlThongTinNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlDieuHuong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlThanhDieuHuongLayout.setVerticalGroup(
            pnlThanhDieuHuongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThanhDieuHuongLayout.createSequentialGroup()
                .addComponent(pnlThongTinNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlDieuHuong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tabsMouseReleased(evt);
            }
        });
        tabs.setLayout(new java.awt.CardLayout());

        pnlTrangChu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pnlTrangChuMouseReleased(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/logo.png"))); // NOI18N

        javax.swing.GroupLayout pnlTrangChuLayout = new javax.swing.GroupLayout(pnlTrangChu);
        pnlTrangChu.setLayout(pnlTrangChuLayout);
        pnlTrangChuLayout.setHorizontalGroup(
            pnlTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTrangChuLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 919, Short.MAX_VALUE))
        );
        pnlTrangChuLayout.setVerticalGroup(
            pnlTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 546, Short.MAX_VALUE)
        );

        tabs.add(pnlTrangChu, "card4");

        pnlTableChuyenDe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pnlTableChuyenDeMouseReleased(evt);
            }
        });

        pnlWapperChuyenDe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pnlWapperChuyenDeMouseReleased(evt);
            }
        });

        lblTitleChuyenDe.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTitleChuyenDe.setForeground(new java.awt.Color(0, 0, 255));
        lblTitleChuyenDe.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitleChuyenDe.setText("QUẢN LÝ CHUYÊN ĐỀ");

        tblChuyenDe.setAutoCreateRowSorter(true);
        tblChuyenDe.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MÃ CD", "TÊN CD", "HỌC PHÍ", "THỜI LƯỢNG", "HÌNH"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblChuyenDe.setGridColor(new java.awt.Color(255, 255, 255));
        tblChuyenDe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblChuyenDeMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblChuyenDeMouseReleased(evt);
            }
        });
        scpChuyenDe.setViewportView(tblChuyenDe);

        pnlFindChuyenDe.setBackground(new java.awt.Color(255, 255, 255));

        txtFindChuyenDe.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        txtFindChuyenDe.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtFindChuyenDeCaretUpdate(evt);
            }
        });

        javax.swing.GroupLayout pnlFindChuyenDeLayout = new javax.swing.GroupLayout(pnlFindChuyenDe);
        pnlFindChuyenDe.setLayout(pnlFindChuyenDeLayout);
        pnlFindChuyenDeLayout.setHorizontalGroup(
            pnlFindChuyenDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFindChuyenDeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtFindChuyenDe, javax.swing.GroupLayout.DEFAULT_SIZE, 711, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlFindChuyenDeLayout.setVerticalGroup(
            pnlFindChuyenDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFindChuyenDeLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(txtFindChuyenDe, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        lblFindChuyenDe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/icons8-search-property-30.png"))); // NOI18N

        javax.swing.GroupLayout pnlWapperChuyenDeLayout = new javax.swing.GroupLayout(pnlWapperChuyenDe);
        pnlWapperChuyenDe.setLayout(pnlWapperChuyenDeLayout);
        pnlWapperChuyenDeLayout.setHorizontalGroup(
            pnlWapperChuyenDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTitleChuyenDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlWapperChuyenDeLayout.createSequentialGroup()
                .addContainerGap(39, Short.MAX_VALUE)
                .addGroup(pnlWapperChuyenDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scpChuyenDe, javax.swing.GroupLayout.PREFERRED_SIZE, 840, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlWapperChuyenDeLayout.createSequentialGroup()
                        .addComponent(lblFindChuyenDe)
                        .addGap(43, 43, 43)
                        .addComponent(pnlFindChuyenDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)))
                .addGap(40, 40, 40))
        );
        pnlWapperChuyenDeLayout.setVerticalGroup(
            pnlWapperChuyenDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWapperChuyenDeLayout.createSequentialGroup()
                .addComponent(lblTitleChuyenDe, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlWapperChuyenDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlFindChuyenDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFindChuyenDe))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addComponent(scpChuyenDe, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
        );

        javax.swing.GroupLayout pnlTableChuyenDeLayout = new javax.swing.GroupLayout(pnlTableChuyenDe);
        pnlTableChuyenDe.setLayout(pnlTableChuyenDeLayout);
        pnlTableChuyenDeLayout.setHorizontalGroup(
            pnlTableChuyenDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlWapperChuyenDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlTableChuyenDeLayout.setVerticalGroup(
            pnlTableChuyenDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlWapperChuyenDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tabs.add(pnlTableChuyenDe, "card3");

        pnlTableKhoaHoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pnlTableKhoaHocMouseReleased(evt);
            }
        });

        pnlWapperKhoaHoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pnlWapperKhoaHocMouseReleased(evt);
            }
        });

        lblTitleKhoaHoc.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTitleKhoaHoc.setForeground(new java.awt.Color(0, 0, 204));
        lblTitleKhoaHoc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitleKhoaHoc.setText("QUẢN LÝ KHÓA HỌC");

        scpKhoaHoc.setBorder(null);

        tblKhoaHoc.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        tblKhoaHoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MÃ KH", "CHUYÊN ĐỀ", "THỜI LƯỢNG", "HỌC PHÍ", "KHAI GIẢNG", "TẠO BỞI", "NGÀY TẠO"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblKhoaHoc.setGridColor(new java.awt.Color(255, 255, 255));
        tblKhoaHoc.setSelectionBackground(new java.awt.Color(61, 156, 232));
        tblKhoaHoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKhoaHocMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblKhoaHocMouseReleased(evt);
            }
        });
        scpKhoaHoc.setViewportView(tblKhoaHoc);

        pnlFindKhoaHoc.setBackground(new java.awt.Color(255, 255, 255));

        txtFindKhoaHoc.setBorder(null);
        txtFindKhoaHoc.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtFindKhoaHocCaretUpdate(evt);
            }
        });

        javax.swing.GroupLayout pnlFindKhoaHocLayout = new javax.swing.GroupLayout(pnlFindKhoaHoc);
        pnlFindKhoaHoc.setLayout(pnlFindKhoaHocLayout);
        pnlFindKhoaHocLayout.setHorizontalGroup(
            pnlFindKhoaHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFindKhoaHocLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtFindKhoaHoc, javax.swing.GroupLayout.DEFAULT_SIZE, 658, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlFindKhoaHocLayout.setVerticalGroup(
            pnlFindKhoaHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFindKhoaHocLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(txtFindKhoaHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        lblFindKhoaHoc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/icons8-search-property-30.png"))); // NOI18N

        javax.swing.GroupLayout pnlWapperKhoaHocLayout = new javax.swing.GroupLayout(pnlWapperKhoaHoc);
        pnlWapperKhoaHoc.setLayout(pnlWapperKhoaHocLayout);
        pnlWapperKhoaHocLayout.setHorizontalGroup(
            pnlWapperKhoaHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWapperKhoaHocLayout.createSequentialGroup()
                .addGroup(pnlWapperKhoaHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlWapperKhoaHocLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblTitleKhoaHoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlWapperKhoaHocLayout.createSequentialGroup()
                        .addGap(92, 92, 92)
                        .addComponent(lblFindKhoaHoc)
                        .addGap(28, 28, 28)
                        .addComponent(pnlFindKhoaHoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 81, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(pnlWapperKhoaHocLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(scpKhoaHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 835, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlWapperKhoaHocLayout.setVerticalGroup(
            pnlWapperKhoaHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWapperKhoaHocLayout.createSequentialGroup()
                .addComponent(lblTitleKhoaHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(pnlWapperKhoaHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlWapperKhoaHocLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(pnlFindKhoaHoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlWapperKhoaHocLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(lblFindKhoaHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(scpKhoaHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );

        javax.swing.GroupLayout pnlTableKhoaHocLayout = new javax.swing.GroupLayout(pnlTableKhoaHoc);
        pnlTableKhoaHoc.setLayout(pnlTableKhoaHocLayout);
        pnlTableKhoaHocLayout.setHorizontalGroup(
            pnlTableKhoaHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlWapperKhoaHoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlTableKhoaHocLayout.setVerticalGroup(
            pnlTableKhoaHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlWapperKhoaHoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tabs.add(pnlTableKhoaHoc, "card4");

        pnlTableNguoiHoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pnlTableNguoiHocMouseReleased(evt);
            }
        });

        pnlWapperNguoiHoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlWapperNguoiHocMouseClicked(evt);
            }
        });

        pnlFindNguoiHoc.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        pnlFindNguoiHoc.setToolTipText("Tìm kiếm");

        txtFindNguoiHoc.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtFindNguoiHocCaretUpdate(evt);
            }
        });

        btnFindNguoiHoc.setText("Tìm");
        btnFindNguoiHoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindNguoiHocActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlFindNguoiHocLayout = new javax.swing.GroupLayout(pnlFindNguoiHoc);
        pnlFindNguoiHoc.setLayout(pnlFindNguoiHocLayout);
        pnlFindNguoiHocLayout.setHorizontalGroup(
            pnlFindNguoiHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFindNguoiHocLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtFindNguoiHoc)
                .addGap(18, 18, 18)
                .addComponent(btnFindNguoiHoc)
                .addContainerGap())
        );
        pnlFindNguoiHocLayout.setVerticalGroup(
            pnlFindNguoiHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFindNguoiHocLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFindNguoiHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFindNguoiHoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFindNguoiHoc))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblFindNguoiHoc.setText("TÌM KIẾM");

        tblNguoiHoc.setAutoCreateRowSorter(true);
        tblNguoiHoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MÃ NH", "HỌ VÀ TÊN", "GIỚI TÍNH", "NGÀY SINH", "ĐIỆN THOẠI", "EMAIL", "MÃ NV", "NGÀY ĐK"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblNguoiHoc.setGridColor(new java.awt.Color(255, 255, 255));
        tblNguoiHoc.setSelectionBackground(new java.awt.Color(94, 171, 248));
        tblNguoiHoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNguoiHocMouseClicked(evt);
            }
        });
        scpNguoiHoc.setViewportView(tblNguoiHoc);

        javax.swing.GroupLayout pnlWapperNguoiHocLayout = new javax.swing.GroupLayout(pnlWapperNguoiHoc);
        pnlWapperNguoiHoc.setLayout(pnlWapperNguoiHocLayout);
        pnlWapperNguoiHocLayout.setHorizontalGroup(
            pnlWapperNguoiHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWapperNguoiHocLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(pnlWapperNguoiHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblFindNguoiHoc)
                    .addGroup(pnlWapperNguoiHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(scpNguoiHoc, javax.swing.GroupLayout.DEFAULT_SIZE, 832, Short.MAX_VALUE)
                        .addComponent(pnlFindNguoiHoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(46, Short.MAX_VALUE))
        );
        pnlWapperNguoiHocLayout.setVerticalGroup(
            pnlWapperNguoiHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWapperNguoiHocLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(lblFindNguoiHoc)
                .addGap(18, 18, 18)
                .addComponent(pnlFindNguoiHoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(scpNguoiHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlTableNguoiHocLayout = new javax.swing.GroupLayout(pnlTableNguoiHoc);
        pnlTableNguoiHoc.setLayout(pnlTableNguoiHocLayout);
        pnlTableNguoiHocLayout.setHorizontalGroup(
            pnlTableNguoiHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlWapperNguoiHoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlTableNguoiHocLayout.setVerticalGroup(
            pnlTableNguoiHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlWapperNguoiHoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tabs.add(pnlTableNguoiHoc, "card5");

        pnlTableNhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pnlTableNhanVienMouseReleased(evt);
            }
        });

        tblNhanVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblNhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNhanVienMouseClicked(evt);
            }
        });
        scpNhanVien.setViewportView(tblNhanVien);

        javax.swing.GroupLayout pnlTableNhanVienLayout = new javax.swing.GroupLayout(pnlTableNhanVien);
        pnlTableNhanVien.setLayout(pnlTableNhanVienLayout);
        pnlTableNhanVienLayout.setHorizontalGroup(
            pnlTableNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTableNhanVienLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(scpNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 832, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );
        pnlTableNhanVienLayout.setVerticalGroup(
            pnlTableNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTableNhanVienLayout.createSequentialGroup()
                .addGap(190, 190, 190)
                .addComponent(scpNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        tabs.add(pnlTableNhanVien, "card6");

        pnlTableThongKe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pnlTableThongKeMouseReleased(evt);
            }
        });

        lblTitle.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(0, 0, 255));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("TỔNG HỢP THỐNG KÊ");

        tblThongKeNguoiHoc.setAutoCreateRowSorter(true);
        tblThongKeNguoiHoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NĂM", "SỐ NGƯỜI HỌC", "ĐẦU TIÊN", "SAU CÙNG"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblThongKeNguoiHoc.setGridColor(new java.awt.Color(255, 255, 255));
        tblThongKeNguoiHoc.setSelectionBackground(new java.awt.Color(81, 170, 240));
        scpTongHopNH.setViewportView(tblThongKeNguoiHoc);

        javax.swing.GroupLayout pnlThongKeNguoiHocLayout = new javax.swing.GroupLayout(pnlThongKeNguoiHoc);
        pnlThongKeNguoiHoc.setLayout(pnlThongKeNguoiHocLayout);
        pnlThongKeNguoiHocLayout.setHorizontalGroup(
            pnlThongKeNguoiHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThongKeNguoiHocLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(scpTongHopNH, javax.swing.GroupLayout.PREFERRED_SIZE, 797, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        pnlThongKeNguoiHocLayout.setVerticalGroup(
            pnlThongKeNguoiHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThongKeNguoiHocLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(scpTongHopNH, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        tabsThongKe.addTab("NGƯỜI HỌC", pnlThongKeNguoiHoc);

        lblKhoaHoc.setText("KHÓA HỌC:");

        cboKhoaHoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboKhoaHocActionPerformed(evt);
            }
        });

        tblBangDiem.setAutoCreateRowSorter(true);
        tblBangDiem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "BẢNG ĐIỂM", "HỌ VÀ TÊN", "ĐIỂM", "XẾP LOẠI"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblBangDiem.setGridColor(new java.awt.Color(255, 255, 255));
        tblBangDiem.setSelectionBackground(new java.awt.Color(100, 178, 239));
        scpBangDiem.setViewportView(tblBangDiem);

        javax.swing.GroupLayout pnlBangDiemLayout = new javax.swing.GroupLayout(pnlBangDiem);
        pnlBangDiem.setLayout(pnlBangDiemLayout);
        pnlBangDiemLayout.setHorizontalGroup(
            pnlBangDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBangDiemLayout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addComponent(lblKhoaHoc)
                .addGap(46, 46, 46)
                .addComponent(cboKhoaHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 576, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(98, Short.MAX_VALUE))
            .addGroup(pnlBangDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlBangDiemLayout.createSequentialGroup()
                    .addGap(28, 28, 28)
                    .addComponent(scpBangDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 797, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(30, Short.MAX_VALUE)))
        );
        pnlBangDiemLayout.setVerticalGroup(
            pnlBangDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBangDiemLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(pnlBangDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKhoaHoc)
                    .addComponent(cboKhoaHoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(385, Short.MAX_VALUE))
            .addGroup(pnlBangDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlBangDiemLayout.createSequentialGroup()
                    .addGap(68, 68, 68)
                    .addComponent(scpBangDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(21, Short.MAX_VALUE)))
        );

        tabsThongKe.addTab("BẢNG ĐIỂM", pnlBangDiem);

        tblTongHop.setAutoCreateRowSorter(true);
        tblTongHop.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CHUYÊN ĐỀ", "TỔNG SỐ HV", "THẤP NHẤT", "CAO NHẤT", "ĐIỂM TRUNG BÌNH"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTongHop.setGridColor(new java.awt.Color(255, 255, 255));
        tblTongHop.setSelectionBackground(new java.awt.Color(67, 160, 233));
        scpTongHopDiem.setViewportView(tblTongHop);

        javax.swing.GroupLayout pnlTongHopDiemLayout = new javax.swing.GroupLayout(pnlTongHopDiem);
        pnlTongHopDiem.setLayout(pnlTongHopDiemLayout);
        pnlTongHopDiemLayout.setHorizontalGroup(
            pnlTongHopDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 855, Short.MAX_VALUE)
            .addGroup(pnlTongHopDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlTongHopDiemLayout.createSequentialGroup()
                    .addGap(28, 28, 28)
                    .addComponent(scpTongHopDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 792, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(35, Short.MAX_VALUE)))
        );
        pnlTongHopDiemLayout.setVerticalGroup(
            pnlTongHopDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 433, Short.MAX_VALUE)
            .addGroup(pnlTongHopDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlTongHopDiemLayout.createSequentialGroup()
                    .addGap(27, 27, 27)
                    .addComponent(scpTongHopDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(26, Short.MAX_VALUE)))
        );

        tabsThongKe.addTab("TỔNG HỢP ĐIỂM", pnlTongHopDiem);

        lblNam.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNam.setText("NĂM:");

        cboNam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboNamActionPerformed(evt);
            }
        });

        tblDoanhThu.setAutoCreateRowSorter(true);
        tblDoanhThu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CHUYÊN ĐỀ", "SỐ KHÓA", "SỐ HV", "DOANH THU", "HP CAO NHẤT", "HP THẤP NHẤT", "HP TRUNG BÌNH"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDoanhThu.setGridColor(new java.awt.Color(255, 255, 255));
        tblDoanhThu.setSelectionBackground(new java.awt.Color(58, 156, 234));
        scpDoanhThu.setViewportView(tblDoanhThu);
        if (tblDoanhThu.getColumnModel().getColumnCount() > 0) {
            tblDoanhThu.getColumnModel().getColumn(0).setMinWidth(150);
            tblDoanhThu.getColumnModel().getColumn(0).setPreferredWidth(150);
            tblDoanhThu.getColumnModel().getColumn(0).setMaxWidth(200);
        }

        javax.swing.GroupLayout pnlDoanhThuLayout = new javax.swing.GroupLayout(pnlDoanhThu);
        pnlDoanhThu.setLayout(pnlDoanhThuLayout);
        pnlDoanhThuLayout.setHorizontalGroup(
            pnlDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDoanhThuLayout.createSequentialGroup()
                .addGroup(pnlDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDoanhThuLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(scpDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 789, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlDoanhThuLayout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addComponent(lblNam, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(cboNam, javax.swing.GroupLayout.PREFERRED_SIZE, 608, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        pnlDoanhThuLayout.setVerticalGroup(
            pnlDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDoanhThuLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(pnlDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNam)
                    .addComponent(cboNam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(scpDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        tabsThongKe.addTab("DOANH THU", pnlDoanhThu);

        javax.swing.GroupLayout pnlWapper1Layout = new javax.swing.GroupLayout(pnlWapper1);
        pnlWapper1.setLayout(pnlWapper1Layout);
        pnlWapper1Layout.setHorizontalGroup(
            pnlWapper1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWapper1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(pnlWapper1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tabsThongKe)
                    .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        pnlWapper1Layout.setVerticalGroup(
            pnlWapper1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWapper1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tabsThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 461, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlTableThongKeLayout = new javax.swing.GroupLayout(pnlTableThongKe);
        pnlTableThongKe.setLayout(pnlTableThongKeLayout);
        pnlTableThongKeLayout.setHorizontalGroup(
            pnlTableThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlWapper1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlTableThongKeLayout.setVerticalGroup(
            pnlTableThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlWapper1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tabs.add(pnlTableThongKe, "card7");

        pnlThongTin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pnlThongTinMouseReleased(evt);
            }
        });

        jLabel6.setText("THÔNG TIN");

        javax.swing.GroupLayout pnlThongTinLayout = new javax.swing.GroupLayout(pnlThongTin);
        pnlThongTin.setLayout(pnlThongTinLayout);
        pnlThongTinLayout.setHorizontalGroup(
            pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThongTinLayout.createSequentialGroup()
                .addGap(292, 292, 292)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(277, Short.MAX_VALUE))
        );
        pnlThongTinLayout.setVerticalGroup(
            pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThongTinLayout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(217, Short.MAX_VALUE))
        );

        tabs.add(pnlThongTin, "card8");

        btnExit.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/icons8-cancel-30 (1).png"))); // NOI18N
        btnExit.setBorder(null);
        btnExit.setBorderPainted(false);
        btnExit.setContentAreaFilled(false);
        btnExit.setDefaultCapable(false);
        btnExit.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/icons8-cancel-30.png"))); // NOI18N
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        lblTopTitle.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                lblTopTitleMouseDragged(evt);
            }
        });
        lblTopTitle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblTopTitleMousePressed(evt);
            }
        });

        javax.swing.GroupLayout pnlWapperLayout = new javax.swing.GroupLayout(pnlWapper);
        pnlWapper.setLayout(pnlWapperLayout);
        pnlWapperLayout.setHorizontalGroup(
            pnlWapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWapperLayout.createSequentialGroup()
                .addComponent(pnlThanhDieuHuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(pnlWapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlWapperLayout.createSequentialGroup()
                        .addComponent(lblTopTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 879, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExit))))
        );
        pnlWapperLayout.setVerticalGroup(
            pnlWapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlThanhDieuHuong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlWapperLayout.createSequentialGroup()
                .addGroup(pnlWapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlWapperLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnExit))
                    .addGroup(pnlWapperLayout.createSequentialGroup()
                        .addComponent(lblTopTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 546, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        // TODO add your handling code here:
        cardLayout(pnlTrangChu, pnlHome);
    }//GEN-LAST:event_btnHomeActionPerformed

    private void btnChuyenDeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChuyenDeActionPerformed
        // TODO add your handling code here:
        cardLayout(pnlTableChuyenDe, pnlChuyenDe);
    }//GEN-LAST:event_btnChuyenDeActionPerformed

    private void btnThongKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThongKeActionPerformed
        // TODO add your handling code here:
        cardLayout(pnlTableThongKe, pnlThongKe);
    }//GEN-LAST:event_btnThongKeActionPerformed

    private void btnAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAboutActionPerformed
        // TODO add your handling code here:
        cardLayout(pnlThongTin, pnlAbout);
    }//GEN-LAST:event_btnAboutActionPerformed

    private void btnKhoaHocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKhoaHocActionPerformed
        // TODO add your handling code here:
        cardLayout(pnlTableKhoaHoc, pnlKhoaHoc);
    }//GEN-LAST:event_btnKhoaHocActionPerformed

    private void btnNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNhanVienActionPerformed
        // TODO add your handling code here:
        cardLayout(pnlTableNhanVien, pnlNhanVien);
    }//GEN-LAST:event_btnNhanVienActionPerformed

    private void btnNguoiHocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNguoiHocActionPerformed
        // TODO add your handling code here:
        cardLayout(pnlTableNguoiHoc, pnlNguoiHoc);
    }//GEN-LAST:event_btnNguoiHocActionPerformed

    private void lblTopTitleMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTopTitleMouseDragged
        // TODO add your handling code here:
        if (!evt.isMetaDown()) {
            Point p = getLocation();
            this.setLocation(p.x + evt.getX() - mouseX, p.y + evt.getY() - mouseY);
        }
    }//GEN-LAST:event_lblTopTitleMouseDragged

    private void lblTopTitleMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTopTitleMousePressed
        // TODO add your handling code here:
        mouseX = evt.getX();
        mouseY = evt.getY();
    }//GEN-LAST:event_lblTopTitleMousePressed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        // TODO add your handling code here:
        this.Exit();
    }//GEN-LAST:event_btnExitActionPerformed

    private void tblChuyenDeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblChuyenDeMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.index = tblChuyenDe.rowAtPoint(evt.getPoint());
            if (this.index >= 0) {
                new ThongTinChuyenDeJFrame(this.index).setVisible(true);
            }
        }
    }//GEN-LAST:event_tblChuyenDeMouseClicked

    private void tblChuyenDeMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblChuyenDeMouseReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_tblChuyenDeMouseReleased

    private void txtFindChuyenDeCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtFindChuyenDeCaretUpdate
        // TODO add your handling code here:
//        this.loadChuyenDe();
    }//GEN-LAST:event_txtFindChuyenDeCaretUpdate

    private void pnlWapperChuyenDeMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlWapperChuyenDeMouseReleased
        // TODO add your handling code here:
        this.showPopMenu(evt);
    }//GEN-LAST:event_pnlWapperChuyenDeMouseReleased

    private void mniChuyenDeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniChuyenDeActionPerformed
        // TODO add your handling code here:
        cardLayout(pnlTableChuyenDe, pnlChuyenDe);
    }//GEN-LAST:event_mniChuyenDeActionPerformed

    private void mniKhoaHocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniKhoaHocActionPerformed
        // TODO add your handling code here:
        cardLayout(pnlTableKhoaHoc, pnlKhoaHoc);
    }//GEN-LAST:event_mniKhoaHocActionPerformed

    private void mniTrangChuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniTrangChuActionPerformed
        // TODO add your handling code here:
        cardLayout(pnlTrangChu, pnlHome);
    }//GEN-LAST:event_mniTrangChuActionPerformed

    private void mniNguoiHocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniNguoiHocActionPerformed
        // TODO add your handling code here:
        cardLayout(pnlTableNguoiHoc, pnlNguoiHoc);
    }//GEN-LAST:event_mniNguoiHocActionPerformed

    private void mniNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniNhanVienActionPerformed
        // TODO add your handling code here:
        cardLayout(pnlTableNhanVien, pnlNhanVien);
    }//GEN-LAST:event_mniNhanVienActionPerformed

    private void mniThongKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniThongKeActionPerformed
        // TODO add your handling code here:
        cardLayout(pnlTableThongKe, pnlThongKe);
    }//GEN-LAST:event_mniThongKeActionPerformed

    private void mniThongTinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniThongTinActionPerformed
        // TODO add your handling code here:
        cardLayout(pnlThongTin, pnlAbout);
    }//GEN-LAST:event_mniThongTinActionPerformed

    private void tabsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabsMouseReleased
        // TODO add your handling code here:
//        if (evt.isPopupTrigger()) {
//            pmn.show(this, evt.getX(), evt.getY());
//        }
    }//GEN-LAST:event_tabsMouseReleased

    private void pnlWapperMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlWapperMouseReleased
        // TODO add your handling code here:
//        if (evt.isPopupTrigger()) {
//            pmn.show(this, evt.getX(), evt.getY());
//        }
    }//GEN-LAST:event_pnlWapperMouseReleased

    private void pnlThanhDieuHuongMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlThanhDieuHuongMouseReleased
        // TODO add your handling code here:
        this.showPopMenu(evt);
    }//GEN-LAST:event_pnlThanhDieuHuongMouseReleased

    private void pnlTrangChuMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlTrangChuMouseReleased
        // TODO add your handling code here:
        this.showPopMenu(evt);
    }//GEN-LAST:event_pnlTrangChuMouseReleased

    private void pnlTableChuyenDeMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlTableChuyenDeMouseReleased
        // TODO add your handling code here:
        this.showPopMenu(evt);
    }//GEN-LAST:event_pnlTableChuyenDeMouseReleased

    private void pnlTableKhoaHocMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlTableKhoaHocMouseReleased
        // TODO add your handling code here:
        this.showPopMenu(evt);
    }//GEN-LAST:event_pnlTableKhoaHocMouseReleased

    private void pnlTableNguoiHocMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlTableNguoiHocMouseReleased
        // TODO add your handling code here:
        this.showPopMenu(evt);
    }//GEN-LAST:event_pnlTableNguoiHocMouseReleased

    private void pnlTableNhanVienMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlTableNhanVienMouseReleased
        // TODO add your handling code here:
        this.showPopMenu(evt);
    }//GEN-LAST:event_pnlTableNhanVienMouseReleased

    private void pnlTableThongKeMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlTableThongKeMouseReleased
        // TODO add your handling code here:
        this.showPopMenu(evt);
    }//GEN-LAST:event_pnlTableThongKeMouseReleased

    private void pnlThongTinMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlThongTinMouseReleased
        // TODO add your handling code here:
        this.showPopMenu(evt);
    }//GEN-LAST:event_pnlThongTinMouseReleased

    private void tblKhoaHocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKhoaHocMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.index = tblKhoaHoc.rowAtPoint(evt.getPoint());
            if (this.index >= 0) {
                new ThongTinKhoaHocJFrame(this.index).setVisible(true);
            }
        }
    }//GEN-LAST:event_tblKhoaHocMouseClicked

    private void tblKhoaHocMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKhoaHocMouseReleased
        // TODO add your handling code here:
        this.showPopMenu(evt);
    }//GEN-LAST:event_tblKhoaHocMouseReleased

    private void txtFindKhoaHocCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtFindKhoaHocCaretUpdate
        // TODO add your handling code here:
        this.loadNguoiHoc();
    }//GEN-LAST:event_txtFindKhoaHocCaretUpdate

    private void pnlWapperKhoaHocMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlWapperKhoaHocMouseReleased
        // TODO add your handling code here:
        this.showPopMenu(evt);
    }//GEN-LAST:event_pnlWapperKhoaHocMouseReleased

    private void txtFindNguoiHocCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtFindNguoiHocCaretUpdate
        // TODO add your handling code here:
        this.loadNguoiHoc();
    }//GEN-LAST:event_txtFindNguoiHocCaretUpdate

    private void btnFindNguoiHocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindNguoiHocActionPerformed
        // TODO add your handling code here:
        this.loadNguoiHoc();
    }//GEN-LAST:event_btnFindNguoiHocActionPerformed

    private void tblNguoiHocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNguoiHocMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.index = tblNguoiHoc.rowAtPoint(evt.getPoint());
            if (this.index >= 0) {
                new BangNguoiHocFrame(index).setVisible(true);
            }
        }
        txtFindNguoiHoc.setText("");
    }//GEN-LAST:event_tblNguoiHocMouseClicked

    private void pnlWapperNguoiHocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlWapperNguoiHocMouseClicked
        // TODO add your handling code here:
        txtFindNguoiHoc.setText("");
    }//GEN-LAST:event_pnlWapperNguoiHocMouseClicked

    private void tblNhanVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNhanVienMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.index = tblNhanVien.rowAtPoint(evt.getPoint());
            if (this.index >= 0) {
//                this.fillToForm();
//                tabs.setSelectedIndex(0);
            }
        }
    }//GEN-LAST:event_tblNhanVienMouseClicked

    private void cboKhoaHocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboKhoaHocActionPerformed
        // TODO add your handling code here:
        this.fillTableBangDiem();
    }//GEN-LAST:event_cboKhoaHocActionPerformed

    private void cboNamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboNamActionPerformed
        // TODO add your handling code here:
        this.fillTableDoanhThu();
    }//GEN-LAST:event_cboNamActionPerformed

    private void btnDangXuatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDangXuatActionPerformed
        // TODO add your handling code here:
        this.logOff();
    }//GEN-LAST:event_btnDangXuatActionPerformed

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
            java.util.logging.Logger.getLogger(MainProJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainProJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainProJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainProJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainProJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAbout;
    private javax.swing.JButton btnChuyenDe;
    private javax.swing.JButton btnDangXuat;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnFindNguoiHoc;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnKhoaHoc;
    private javax.swing.JButton btnNguoiHoc;
    private javax.swing.JButton btnNhanVien;
    private javax.swing.JButton btnThongKe;
    private javax.swing.JComboBox<String> cboKhoaHoc;
    private javax.swing.JComboBox<String> cboNam;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel lblChao;
    private javax.swing.JLabel lblDongHo;
    private javax.swing.JLabel lblFindChuyenDe;
    private javax.swing.JLabel lblFindKhoaHoc;
    private javax.swing.JLabel lblFindNguoiHoc;
    private javax.swing.JLabel lblKhoaHoc;
    private javax.swing.JLabel lblNam;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTitleChuyenDe;
    private javax.swing.JLabel lblTitleKhoaHoc;
    private javax.swing.JLabel lblTopTitle;
    private javax.swing.JMenuItem mniChuyenDe;
    private javax.swing.JMenuItem mniKhoaHoc;
    private javax.swing.JMenuItem mniNguoiHoc;
    private javax.swing.JMenuItem mniNhanVien;
    private javax.swing.JMenuItem mniThongKe;
    private javax.swing.JMenuItem mniThongTin;
    private javax.swing.JMenuItem mniTrangChu;
    private javax.swing.JPopupMenu pmn;
    private javax.swing.JPanel pnlAbout;
    private javax.swing.JPanel pnlBangDiem;
    private javax.swing.JPanel pnlChuyenDe;
    private javax.swing.JPanel pnlDieuHuong;
    private javax.swing.JPanel pnlDoanhThu;
    private javax.swing.JPanel pnlFindChuyenDe;
    private javax.swing.JPanel pnlFindKhoaHoc;
    private javax.swing.JPanel pnlFindNguoiHoc;
    private javax.swing.JPanel pnlHome;
    private javax.swing.JPanel pnlKhoaHoc;
    private javax.swing.JPanel pnlNguoiHoc;
    private javax.swing.JPanel pnlNhanVien;
    private javax.swing.JPanel pnlTableChuyenDe;
    private javax.swing.JPanel pnlTableKhoaHoc;
    private javax.swing.JPanel pnlTableNguoiHoc;
    private javax.swing.JPanel pnlTableNhanVien;
    private javax.swing.JPanel pnlTableThongKe;
    private javax.swing.JPanel pnlThanhDieuHuong;
    private javax.swing.JPanel pnlThongKe;
    private javax.swing.JPanel pnlThongKeNguoiHoc;
    private javax.swing.JPanel pnlThongTin;
    private javax.swing.JPanel pnlThongTinNhanVien;
    private javax.swing.JPanel pnlTongHopDiem;
    private javax.swing.JPanel pnlTrangChu;
    private javax.swing.JPanel pnlWapper;
    private javax.swing.JPanel pnlWapper1;
    private javax.swing.JPanel pnlWapperChuyenDe;
    private javax.swing.JPanel pnlWapperKhoaHoc;
    private javax.swing.JPanel pnlWapperNguoiHoc;
    private javax.swing.JScrollPane scpBangDiem;
    private javax.swing.JScrollPane scpChuyenDe;
    private javax.swing.JScrollPane scpDoanhThu;
    private javax.swing.JScrollPane scpKhoaHoc;
    private javax.swing.JScrollPane scpNguoiHoc;
    private javax.swing.JScrollPane scpNhanVien;
    private javax.swing.JScrollPane scpTongHopDiem;
    private javax.swing.JScrollPane scpTongHopNH;
    private javax.swing.JPanel tabs;
    private javax.swing.JTabbedPane tabsThongKe;
    private javax.swing.JTable tblBangDiem;
    public static javax.swing.JTable tblChuyenDe;
    private javax.swing.JTable tblDoanhThu;
    public static javax.swing.JTable tblKhoaHoc;
    public static javax.swing.JTable tblNguoiHoc;
    private javax.swing.JTable tblNhanVien;
    private javax.swing.JTable tblThongKeNguoiHoc;
    private javax.swing.JTable tblTongHop;
    private javax.swing.JTextField txtFindChuyenDe;
    private javax.swing.JTextField txtFindKhoaHoc;
    public static javax.swing.JTextField txtFindNguoiHoc;
    // End of variables declaration//GEN-END:variables
}
