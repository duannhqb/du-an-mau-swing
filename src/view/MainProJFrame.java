/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import DAO.ChuyenDeDAO;
import DAO.KhoaHocDAO;
import DAO.NguoiHocDAO;
import DAO.NhanVienDAO;
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
import model.NhanVien;

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
    NhanVienDAO nvdao = new NhanVienDAO();

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
        this.cardLayout(pnlTrangChu, pnlHome);
        this.popMenu();

        ShareHelper.DrawTable(tblThongKeNguoiHoc);
        ShareHelper.DrawTable(tblBangDiem);
        ShareHelper.DrawTable(tblTongHop);
        ShareHelper.DrawTable(tblDoanhThu);
        ShareHelper.DrawTable(tblNhanVien);
        ShareHelper.DrawTable(tblChuyenDe);
        ShareHelper.DrawTable(tblKhoaHoc);
        ShareHelper.DrawTable(tblNguoiHoc);
    }

    private void popMenu() {
        mniChuyenDe.setText("Chuyên đề");
        mniKhoaHoc.setText("Khóa học");
        mniNguoiHoc.setText("Người học");
        mniNhanVien.setText("Nhân viên");
        mniThongKe.setText("Thống kê");
        mniTrangChu.setText("Trang chủ");
    }

    private void openLogin() {
        new DangNhapJDialog(this, true).setVisible(true);
        this.setVisible(true);
    }

    private void openWelcome() {
        new ChaoJDialog(this, true).setVisible(true);
    }

    private void Exit() {
        if (DialogHelper.confirm(this, "Bạn có chắc chắn muốn kết thúc chương trình")) {
            System.exit(0);
        }
    }

//    khi đăng suất thì xóa hết thông tin ở bảng doanh thu, tránh trường hợp nhân viên đăng nhập vào có thể xem doanh thu.
    private void logOff() {
        DefaultTableModel model = (DefaultTableModel) tblDoanhThu.getModel();
        model.setRowCount(0);
        ShareHelper.logOff();
        this.dispose();
        this.openLogin();
    }

    private void openAbout() {
        new AboutJDialog(this, true).setVisible(true);
    }

    private void openWebsite() throws URISyntaxException, IOException {
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
        pnl1.setVisible(true);

//        set màu nền khi nhấn vào menu bên trái
        pnlHome.setBackground(new Color(102, 176, 249));
        pnlChuyenDe.setBackground(new Color(102, 176, 249));
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

//********************* TAB chuyên đề *********************
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

//********************* TAB người học *********************
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

//********************* TAB nhân viên *********************
    void loadNhanVien() {
        DefaultTableModel model = (DefaultTableModel) tblNhanVien.getModel();
        model.setRowCount(0);
        try {
            String keyword = txtFindNhanVien.getText();
            List<NhanVien> list = nvdao.selectByKeyword(keyword);
            for (NhanVien nv : list) {
                Object[] row = {
                    nv.getMaNV(),
                    nv.getMatKhau(),
                    nv.getHoTen(),
                    nv.isVaiTro() ? "Trưởng phòng" : "Nhân viên"
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu");
        }
    }

// ******************* TAB khóa học *********************
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

// ******************* TAB tổng hợp *********************
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
        try {
            cboKhoaHoc.setSelectedIndex(0);
        } catch (Exception e) {
        }
    }

//    combobox nam
    void fillComboBoxNam() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboNam.getModel();
        if (model != null) {
            model.removeAllElements();
        }
        try {
            List<KhoaHoc> list = khdao.select();
            for (KhoaHoc khoaHoc : list) {
                int nam = khoaHoc.getNgayKG().getYear() + 1900;
                if (model.getIndexOf(nam) < 0) {
                    model.addElement(nam);
                }
            }
            cboNam.setSelectedIndex(0);
        } catch (Exception e) {
        }
    }

//    table thong ke diem
    void fillTableBangDiem() {
        DefaultTableModel model = (DefaultTableModel) tblBangDiem.getModel();
        model.setRowCount(0);
        try {
            KhoaHoc kh = (KhoaHoc) cboKhoaHoc.getSelectedItem();
            List<Object[]> list = tkdao.getBangDiem(kh.getMaKH());
            for (Object[] objects : list) {
                model.addRow(objects);
            }
        } catch (Exception e) {
        }
    }

//    table thong ke nguoi hoc
    void fillTableNguoiHoc() {
        DefaultTableModel model = (DefaultTableModel) tblThongKeNguoiHoc.getModel();
        model.setRowCount(0);
        try {
            List<Object[]> list = tkdao.getNguoiHoc();
            for (Object[] objects : list) {
                model.addRow(objects);
            }
        } catch (Exception e) {
        }
    }

//    table thong ke khoa hoc
    void fillTableKhoaHoc() {
        DefaultTableModel model = (DefaultTableModel) tblTongHop.getModel();
        model.setRowCount(0);
        try {
            List<Object[]> list = tkdao.getDiemTheoChuyenDe();
            for (Object[] objects : list) {
                model.addRow(objects);
            }
        } catch (Exception e) {
        }
    }

//    bảng doanh thu
//    với quyền truy cập là Trưởng phòng mới xem được doanh thu
//    quyền nhân viên không thể xem được doanh thu
    void fillTableDoanhThu() {
        if (ShareHelper.USER.isVaiTro() == false) {
            pnlDoanhThu.setVisible(false);
            DefaultTableModel model = (DefaultTableModel) tblDoanhThu.getModel();
            model.setRowCount(0);
            pnlDoanhThu.setToolTipText("Bạn không đủ quyền để xem doanh thu!");
            tblDoanhThu.setToolTipText("Bạn không đủ quyền để xem doanh thu!");
            cboNam.setToolTipText("Bạn không đủ quyền để xem doanh thu!");
        } else {
            pnlDoanhThu.setVisible(true);
            DefaultTableModel model = (DefaultTableModel) tblDoanhThu.getModel();
            model.setRowCount(0);
            try {
                int nam = Integer.parseInt(cboNam.getSelectedItem().toString());
                List<Object[]> list = tkdao.getDoanhThu(nam);
                for (Object[] objects : list) {
                    model.addRow(objects);
                }
            } catch (Exception e) {
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
        pnlWapper = new javax.swing.JPanel();
        pnlThanhDieuHuong = new javax.swing.JPanel();
        pnlThongTinNhanVien = new javax.swing.JPanel();
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
        btnAbout = new javax.swing.JButton();
        tabs = new javax.swing.JPanel();
        pnlTrangChu = new javax.swing.JPanel();
        lblMain = new javax.swing.JLabel();
        pnlTableChuyenDe = new javax.swing.JPanel();
        pnlWapperChuyenDe = new javax.swing.JPanel();
        scpChuyenDe = new javax.swing.JScrollPane();
        tblChuyenDe = new javax.swing.JTable();
        pnlFindChuyenDe = new javax.swing.JPanel();
        lblFindChuyenDe = new javax.swing.JLabel();
        txtFindChuyenDe = new javax.swing.JTextField();
        btnThemChuyenDe = new javax.swing.JButton();
        lblTitleChuyenDe = new javax.swing.JLabel();
        pnlTableKhoaHoc = new javax.swing.JPanel();
        pnlWapperKhoaHoc = new javax.swing.JPanel();
        scpKhoaHoc = new javax.swing.JScrollPane();
        tblKhoaHoc = new javax.swing.JTable();
        pnlFindKhoaHoc = new javax.swing.JPanel();
        lblFindKhoaHoc = new javax.swing.JLabel();
        txtFindKhoaHoc = new javax.swing.JTextField();
        btnThemKhoaHoc = new javax.swing.JButton();
        lblTitleKhoaHoc = new javax.swing.JLabel();
        pnlTableNguoiHoc = new javax.swing.JPanel();
        pnlWapperNguoiHoc = new javax.swing.JPanel();
        pnlFindNguoiHoc = new javax.swing.JPanel();
        txtFindNguoiHoc = new javax.swing.JTextField();
        lblFindNguoiHoc = new javax.swing.JLabel();
        btnThemNguoiHoc = new javax.swing.JButton();
        scpNguoiHoc = new javax.swing.JScrollPane();
        tblNguoiHoc = new javax.swing.JTable();
        lblTitleNhanVien1 = new javax.swing.JLabel();
        pnlTableNhanVien = new javax.swing.JPanel();
        pnlWapperNhanVien = new javax.swing.JPanel();
        pnlFindNhanVien = new javax.swing.JPanel();
        txtFindNhanVien = new javax.swing.JTextField();
        btnThemNhanVien = new javax.swing.JButton();
        lblFindNhanVien = new javax.swing.JLabel();
        scpNhanVien = new javax.swing.JScrollPane();
        tblNhanVien = new javax.swing.JTable();
        lblTitleNhanVien = new javax.swing.JLabel();
        pnlTableThongKe = new javax.swing.JPanel();
        pnlWapperThongKe = new javax.swing.JPanel();
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
        lblTitleThongKe = new javax.swing.JLabel();
        btnExit = new javax.swing.JButton();
        pnlThanhTrangThai = new javax.swing.JPanel();
        lblDongHo = new javax.swing.JLabel();
        pnlTrangThaiBottom = new javax.swing.JPanel();
        btnOpenBr = new javax.swing.JButton();

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
        btnDangXuat.setBorder(null);
        btnDangXuat.setContentAreaFilled(false);
        btnDangXuat.setDefaultCapable(false);
        btnDangXuat.setFocusPainted(false);
        btnDangXuat.setFocusable(false);
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
                .addGap(54, 54, 54)
                .addComponent(lblChao, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThongTinNhanVienLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDangXuat, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(87, 87, 87))
        );
        pnlThongTinNhanVienLayout.setVerticalGroup(
            pnlThongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThongTinNhanVienLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(lblChao)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addComponent(btnDangXuat, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlDieuHuong.setBackground(new java.awt.Color(51, 153, 255));

        pnlHome.setBackground(new java.awt.Color(102, 176, 249));

        btnHome.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/icons8-home-24 (1).png"))); // NOI18N
        btnHome.setText("TRANG CHỦ");
        btnHome.setBorderPainted(false);
        btnHome.setContentAreaFilled(false);
        btnHome.setFocusPainted(false);
        btnHome.setFocusable(false);
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
        btnChuyenDe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/icons8-read-24.png"))); // NOI18N
        btnChuyenDe.setText("CHUYÊN ĐỀ");
        btnChuyenDe.setBorderPainted(false);
        btnChuyenDe.setContentAreaFilled(false);
        btnChuyenDe.setFocusPainted(false);
        btnChuyenDe.setFocusable(false);
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
        btnKhoaHoc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/icons8-study-30.png"))); // NOI18N
        btnKhoaHoc.setText("KHÓA HỌC");
        btnKhoaHoc.setBorderPainted(false);
        btnKhoaHoc.setContentAreaFilled(false);
        btnKhoaHoc.setFocusPainted(false);
        btnKhoaHoc.setFocusable(false);
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
        btnNguoiHoc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/icons8-account-24.png"))); // NOI18N
        btnNguoiHoc.setText("NGƯỜI HỌC");
        btnNguoiHoc.setBorderPainted(false);
        btnNguoiHoc.setContentAreaFilled(false);
        btnNguoiHoc.setFocusPainted(false);
        btnNguoiHoc.setFocusable(false);
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
        btnNhanVien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/icons8-name-tag-24.png"))); // NOI18N
        btnNhanVien.setText("NHÂN VIÊN");
        btnNhanVien.setBorderPainted(false);
        btnNhanVien.setContentAreaFilled(false);
        btnNhanVien.setFocusPainted(false);
        btnNhanVien.setFocusable(false);
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
        btnThongKe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/icons8-statistics-24.png"))); // NOI18N
        btnThongKe.setText("THỐNG KÊ");
        btnThongKe.setBorderPainted(false);
        btnThongKe.setContentAreaFilled(false);
        btnThongKe.setFocusPainted(false);
        btnThongKe.setFocusable(false);
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

        btnAbout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/Info.png"))); // NOI18N
        btnAbout.setText("Thông tin ứng dụng");
        btnAbout.setBorderPainted(false);
        btnAbout.setContentAreaFilled(false);
        btnAbout.setDefaultCapable(false);
        btnAbout.setFocusPainted(false);
        btnAbout.setFocusable(false);
        btnAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAboutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlDieuHuongLayout = new javax.swing.GroupLayout(pnlDieuHuong);
        pnlDieuHuong.setLayout(pnlDieuHuongLayout);
        pnlDieuHuongLayout.setHorizontalGroup(
            pnlDieuHuongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlHome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlChuyenDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlKhoaHoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlThongKe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlNguoiHoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDieuHuongLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAbout)
                .addGap(54, 54, 54))
        );
        pnlDieuHuongLayout.setVerticalGroup(
            pnlDieuHuongLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDieuHuongLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(btnAbout, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        lblMain.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/logo.png"))); // NOI18N

        javax.swing.GroupLayout pnlTrangChuLayout = new javax.swing.GroupLayout(pnlTrangChu);
        pnlTrangChu.setLayout(pnlTrangChuLayout);
        pnlTrangChuLayout.setHorizontalGroup(
            pnlTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblMain, javax.swing.GroupLayout.DEFAULT_SIZE, 938, Short.MAX_VALUE)
        );
        pnlTrangChuLayout.setVerticalGroup(
            pnlTrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        pnlFindChuyenDe.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        pnlFindChuyenDe.setToolTipText("Tìm kiếm");

        lblFindChuyenDe.setText("TÌM KIẾM");

        txtFindChuyenDe.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtFindChuyenDeCaretUpdate(evt);
            }
        });

        btnThemChuyenDe.setText("Thêm");
        btnThemChuyenDe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemChuyenDeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlFindChuyenDeLayout = new javax.swing.GroupLayout(pnlFindChuyenDe);
        pnlFindChuyenDe.setLayout(pnlFindChuyenDeLayout);
        pnlFindChuyenDeLayout.setHorizontalGroup(
            pnlFindChuyenDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFindChuyenDeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFindChuyenDe)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtFindChuyenDe)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnThemChuyenDe)
                .addContainerGap())
        );
        pnlFindChuyenDeLayout.setVerticalGroup(
            pnlFindChuyenDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFindChuyenDeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFindChuyenDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFindChuyenDe)
                    .addComponent(txtFindChuyenDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThemChuyenDe))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblTitleChuyenDe.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTitleChuyenDe.setForeground(new java.awt.Color(0, 0, 204));
        lblTitleChuyenDe.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitleChuyenDe.setText("QUẢN LÝ CHUYÊN ĐỀ");

        javax.swing.GroupLayout pnlWapperChuyenDeLayout = new javax.swing.GroupLayout(pnlWapperChuyenDe);
        pnlWapperChuyenDe.setLayout(pnlWapperChuyenDeLayout);
        pnlWapperChuyenDeLayout.setHorizontalGroup(
            pnlWapperChuyenDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlWapperChuyenDeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitleChuyenDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlWapperChuyenDeLayout.createSequentialGroup()
                .addContainerGap(47, Short.MAX_VALUE)
                .addGroup(pnlWapperChuyenDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(scpChuyenDe, javax.swing.GroupLayout.DEFAULT_SIZE, 844, Short.MAX_VALUE)
                    .addComponent(pnlFindChuyenDe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(47, 47, 47))
        );
        pnlWapperChuyenDeLayout.setVerticalGroup(
            pnlWapperChuyenDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWapperChuyenDeLayout.createSequentialGroup()
                .addComponent(lblTitleChuyenDe, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnlFindChuyenDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(scpChuyenDe, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                .addContainerGap())
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

        pnlFindKhoaHoc.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        pnlFindKhoaHoc.setToolTipText("Tìm kiếm");

        lblFindKhoaHoc.setText("TÌM KIẾM");

        txtFindKhoaHoc.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtFindKhoaHocCaretUpdate(evt);
            }
        });

        btnThemKhoaHoc.setText("Thêm");
        btnThemKhoaHoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemKhoaHocActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlFindKhoaHocLayout = new javax.swing.GroupLayout(pnlFindKhoaHoc);
        pnlFindKhoaHoc.setLayout(pnlFindKhoaHocLayout);
        pnlFindKhoaHocLayout.setHorizontalGroup(
            pnlFindKhoaHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFindKhoaHocLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFindKhoaHoc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtFindKhoaHoc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnThemKhoaHoc)
                .addContainerGap())
        );
        pnlFindKhoaHocLayout.setVerticalGroup(
            pnlFindKhoaHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFindKhoaHocLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFindKhoaHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFindKhoaHoc)
                    .addComponent(txtFindKhoaHoc)
                    .addComponent(btnThemKhoaHoc))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblTitleKhoaHoc.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTitleKhoaHoc.setForeground(new java.awt.Color(0, 0, 204));
        lblTitleKhoaHoc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitleKhoaHoc.setText("QUẢN LÝ KHÓA HỌC");

        javax.swing.GroupLayout pnlWapperKhoaHocLayout = new javax.swing.GroupLayout(pnlWapperKhoaHoc);
        pnlWapperKhoaHoc.setLayout(pnlWapperKhoaHocLayout);
        pnlWapperKhoaHocLayout.setHorizontalGroup(
            pnlWapperKhoaHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWapperKhoaHocLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(pnlWapperKhoaHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlFindKhoaHoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scpKhoaHoc, javax.swing.GroupLayout.DEFAULT_SIZE, 844, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(pnlWapperKhoaHocLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitleKhoaHoc, javax.swing.GroupLayout.DEFAULT_SIZE, 918, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlWapperKhoaHocLayout.setVerticalGroup(
            pnlWapperKhoaHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWapperKhoaHocLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(lblTitleKhoaHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnlFindKhoaHoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(scpKhoaHoc, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                .addContainerGap())
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

        lblFindNguoiHoc.setText("TÌM KIẾM");

        btnThemNguoiHoc.setText("Thêm");
        btnThemNguoiHoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemNguoiHocActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlFindNguoiHocLayout = new javax.swing.GroupLayout(pnlFindNguoiHoc);
        pnlFindNguoiHoc.setLayout(pnlFindNguoiHocLayout);
        pnlFindNguoiHocLayout.setHorizontalGroup(
            pnlFindNguoiHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFindNguoiHocLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFindNguoiHoc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtFindNguoiHoc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnThemNguoiHoc)
                .addContainerGap())
        );
        pnlFindNguoiHocLayout.setVerticalGroup(
            pnlFindNguoiHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFindNguoiHocLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFindNguoiHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFindNguoiHoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFindNguoiHoc)
                    .addComponent(btnThemNguoiHoc))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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

        lblTitleNhanVien1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTitleNhanVien1.setForeground(new java.awt.Color(0, 0, 204));
        lblTitleNhanVien1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitleNhanVien1.setText("QUẢN LÝ NGƯỜI HỌC");

        javax.swing.GroupLayout pnlWapperNguoiHocLayout = new javax.swing.GroupLayout(pnlWapperNguoiHoc);
        pnlWapperNguoiHoc.setLayout(pnlWapperNguoiHocLayout);
        pnlWapperNguoiHocLayout.setHorizontalGroup(
            pnlWapperNguoiHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWapperNguoiHocLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(pnlWapperNguoiHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlFindNguoiHoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scpNguoiHoc, javax.swing.GroupLayout.DEFAULT_SIZE, 844, Short.MAX_VALUE))
                .addGap(47, 47, 47))
            .addGroup(pnlWapperNguoiHocLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitleNhanVien1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlWapperNguoiHocLayout.setVerticalGroup(
            pnlWapperNguoiHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWapperNguoiHocLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(lblTitleNhanVien1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnlFindNguoiHoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(scpNguoiHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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

        pnlWapperNhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlWapperNhanVienMouseClicked(evt);
            }
        });

        pnlFindNhanVien.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        pnlFindNhanVien.setToolTipText("Tìm kiếm");

        txtFindNhanVien.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtFindNhanVienCaretUpdate(evt);
            }
        });

        btnThemNhanVien.setText("Thêm");
        btnThemNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemNhanVienActionPerformed(evt);
            }
        });

        lblFindNhanVien.setText("TÌM KIẾM");

        javax.swing.GroupLayout pnlFindNhanVienLayout = new javax.swing.GroupLayout(pnlFindNhanVien);
        pnlFindNhanVien.setLayout(pnlFindNhanVienLayout);
        pnlFindNhanVienLayout.setHorizontalGroup(
            pnlFindNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFindNhanVienLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFindNhanVien)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtFindNhanVien)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnThemNhanVien)
                .addContainerGap())
        );
        pnlFindNhanVienLayout.setVerticalGroup(
            pnlFindNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFindNhanVienLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFindNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFindNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThemNhanVien)
                    .addComponent(lblFindNhanVien))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblNhanVien.setAutoCreateRowSorter(true);
        tblNhanVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MÃ NV", "MẬT KHẨU", "HỌ VÀ TÊN", "VAI TRÒ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblNhanVien.setGridColor(new java.awt.Color(255, 255, 255));
        tblNhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNhanVienMouseClicked(evt);
            }
        });
        scpNhanVien.setViewportView(tblNhanVien);

        lblTitleNhanVien.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTitleNhanVien.setForeground(new java.awt.Color(0, 0, 204));
        lblTitleNhanVien.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitleNhanVien.setText("QUẢN LÝ NHÂN VIÊN");

        javax.swing.GroupLayout pnlWapperNhanVienLayout = new javax.swing.GroupLayout(pnlWapperNhanVien);
        pnlWapperNhanVien.setLayout(pnlWapperNhanVienLayout);
        pnlWapperNhanVienLayout.setHorizontalGroup(
            pnlWapperNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWapperNhanVienLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(pnlWapperNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlFindNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scpNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, 844, Short.MAX_VALUE))
                .addGap(47, 47, 47))
            .addGroup(pnlWapperNhanVienLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitleNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlWapperNhanVienLayout.setVerticalGroup(
            pnlWapperNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWapperNhanVienLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(lblTitleNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnlFindNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(scpNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlTableNhanVienLayout = new javax.swing.GroupLayout(pnlTableNhanVien);
        pnlTableNhanVien.setLayout(pnlTableNhanVienLayout);
        pnlTableNhanVienLayout.setHorizontalGroup(
            pnlTableNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTableNhanVienLayout.createSequentialGroup()
                .addComponent(pnlWapperNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        pnlTableNhanVienLayout.setVerticalGroup(
            pnlTableNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTableNhanVienLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(pnlWapperNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        tabs.add(pnlTableNhanVien, "card6");

        pnlTableThongKe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pnlTableThongKeMouseReleased(evt);
            }
        });

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
                .addGap(48, 48, 48)
                .addComponent(scpTongHopNH, javax.swing.GroupLayout.PREFERRED_SIZE, 839, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );
        pnlThongKeNguoiHocLayout.setVerticalGroup(
            pnlThongKeNguoiHocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThongKeNguoiHocLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(scpTongHopNH, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                .addContainerGap())
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
                .addGroup(pnlBangDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlBangDiemLayout.createSequentialGroup()
                        .addGap(92, 92, 92)
                        .addComponent(lblKhoaHoc)
                        .addGap(18, 18, 18)
                        .addComponent(cboKhoaHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 618, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlBangDiemLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(scpBangDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 841, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(46, 46, 46))
        );
        pnlBangDiemLayout.setVerticalGroup(
            pnlBangDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBangDiemLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(pnlBangDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboKhoaHoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblKhoaHoc))
                .addGap(28, 28, 28)
                .addComponent(scpBangDiem, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                .addContainerGap())
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
        if (tblTongHop.getColumnModel().getColumnCount() > 0) {
            tblTongHop.getColumnModel().getColumn(0).setMinWidth(200);
            tblTongHop.getColumnModel().getColumn(0).setPreferredWidth(200);
            tblTongHop.getColumnModel().getColumn(0).setMaxWidth(250);
        }

        javax.swing.GroupLayout pnlTongHopDiemLayout = new javax.swing.GroupLayout(pnlTongHopDiem);
        pnlTongHopDiem.setLayout(pnlTongHopDiemLayout);
        pnlTongHopDiemLayout.setHorizontalGroup(
            pnlTongHopDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTongHopDiemLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(scpTongHopDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 839, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlTongHopDiemLayout.setVerticalGroup(
            pnlTongHopDiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTongHopDiemLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(scpTongHopDiem, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                .addContainerGap())
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
            tblDoanhThu.getColumnModel().getColumn(0).setMinWidth(200);
            tblDoanhThu.getColumnModel().getColumn(0).setPreferredWidth(250);
            tblDoanhThu.getColumnModel().getColumn(0).setMaxWidth(250);
        }

        javax.swing.GroupLayout pnlDoanhThuLayout = new javax.swing.GroupLayout(pnlDoanhThu);
        pnlDoanhThu.setLayout(pnlDoanhThuLayout);
        pnlDoanhThuLayout.setHorizontalGroup(
            pnlDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDoanhThuLayout.createSequentialGroup()
                .addGroup(pnlDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDoanhThuLayout.createSequentialGroup()
                        .addGap(115, 115, 115)
                        .addComponent(lblNam, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cboNam, javax.swing.GroupLayout.PREFERRED_SIZE, 615, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlDoanhThuLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(scpDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 832, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(46, 46, 46))
        );
        pnlDoanhThuLayout.setVerticalGroup(
            pnlDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDoanhThuLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(pnlDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNam)
                    .addComponent(cboNam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(scpDoanhThu, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabsThongKe.addTab("DOANH THU", pnlDoanhThu);

        lblTitleThongKe.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTitleThongKe.setForeground(new java.awt.Color(0, 0, 204));
        lblTitleThongKe.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitleThongKe.setText("TỔNG HỢP THỐNG KÊ");

        javax.swing.GroupLayout pnlWapperThongKeLayout = new javax.swing.GroupLayout(pnlWapperThongKe);
        pnlWapperThongKe.setLayout(pnlWapperThongKeLayout);
        pnlWapperThongKeLayout.setHorizontalGroup(
            pnlWapperThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabsThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 938, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(lblTitleThongKe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlWapperThongKeLayout.setVerticalGroup(
            pnlWapperThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWapperThongKeLayout.createSequentialGroup()
                .addComponent(lblTitleThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addComponent(tabsThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout pnlTableThongKeLayout = new javax.swing.GroupLayout(pnlTableThongKe);
        pnlTableThongKe.setLayout(pnlTableThongKeLayout);
        pnlTableThongKeLayout.setHorizontalGroup(
            pnlTableThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlWapperThongKe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlTableThongKeLayout.setVerticalGroup(
            pnlTableThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlWapperThongKe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tabs.add(pnlTableThongKe, "card7");

        btnExit.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        btnExit.setText("X");
        btnExit.setBorder(null);
        btnExit.setBorderPainted(false);
        btnExit.setContentAreaFilled(false);
        btnExit.setDefaultCapable(false);
        btnExit.setFocusPainted(false);
        btnExit.setFocusable(false);
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        pnlThanhTrangThai.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                pnlThanhTrangThaiMouseDragged(evt);
            }
        });
        pnlThanhTrangThai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pnlThanhTrangThaiMousePressed(evt);
            }
        });

        lblDongHo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout pnlThanhTrangThaiLayout = new javax.swing.GroupLayout(pnlThanhTrangThai);
        pnlThanhTrangThai.setLayout(pnlThanhTrangThaiLayout);
        pnlThanhTrangThaiLayout.setHorizontalGroup(
            pnlThanhTrangThaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThanhTrangThaiLayout.createSequentialGroup()
                .addContainerGap(46, Short.MAX_VALUE)
                .addComponent(lblDongHo, javax.swing.GroupLayout.PREFERRED_SIZE, 833, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlThanhTrangThaiLayout.setVerticalGroup(
            pnlThanhTrangThaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblDongHo, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
        );

        btnOpenBr.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/Globe.png"))); // NOI18N
        btnOpenBr.setBorderPainted(false);
        btnOpenBr.setContentAreaFilled(false);
        btnOpenBr.setDefaultCapable(false);
        btnOpenBr.setFocusPainted(false);
        btnOpenBr.setFocusable(false);
        btnOpenBr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenBrActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlTrangThaiBottomLayout = new javax.swing.GroupLayout(pnlTrangThaiBottom);
        pnlTrangThaiBottom.setLayout(pnlTrangThaiBottomLayout);
        pnlTrangThaiBottomLayout.setHorizontalGroup(
            pnlTrangThaiBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTrangThaiBottomLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnOpenBr, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlTrangThaiBottomLayout.setVerticalGroup(
            pnlTrangThaiBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTrangThaiBottomLayout.createSequentialGroup()
                .addComponent(btnOpenBr)
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout pnlWapperLayout = new javax.swing.GroupLayout(pnlWapper);
        pnlWapper.setLayout(pnlWapperLayout);
        pnlWapperLayout.setHorizontalGroup(
            pnlWapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(pnlWapperLayout.createSequentialGroup()
                .addComponent(pnlThanhDieuHuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlWapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlWapperLayout.createSequentialGroup()
                        .addComponent(pnlThanhTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlTrangThaiBottom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        pnlWapperLayout.setVerticalGroup(
            pnlWapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlThanhDieuHuong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlWapperLayout.createSequentialGroup()
                .addGroup(pnlWapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlWapperLayout.createSequentialGroup()
                        .addComponent(btnExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(1, 1, 1))
                    .addComponent(pnlThanhTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(pnlTrangThaiBottom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        this.loadChuyenDe();
    }//GEN-LAST:event_btnChuyenDeActionPerformed

    private void btnThongKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThongKeActionPerformed
        // TODO add your handling code here:
        cardLayout(pnlTableThongKe, pnlThongKe);
        this.fillTableNguoiHoc();
        this.fillComboBoxKhoaHoc();
        this.fillTableKhoaHoc();
        this.fillTableBangDiem();
        this.fillComboBoxNam();
        this.fillTableDoanhThu();
    }//GEN-LAST:event_btnThongKeActionPerformed

    private void btnKhoaHocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKhoaHocActionPerformed
        // TODO add your handling code here:
        cardLayout(pnlTableKhoaHoc, pnlKhoaHoc);
        this.loadKhoaHoc();
    }//GEN-LAST:event_btnKhoaHocActionPerformed

    private void btnNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNhanVienActionPerformed
        // TODO add your handling code here:
        cardLayout(pnlTableNhanVien, pnlNhanVien);
        this.loadNhanVien();
    }//GEN-LAST:event_btnNhanVienActionPerformed

    private void btnNguoiHocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNguoiHocActionPerformed
        // TODO add your handling code here:
        cardLayout(pnlTableNguoiHoc, pnlNguoiHoc);
        this.loadNguoiHoc();
    }//GEN-LAST:event_btnNguoiHocActionPerformed

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
        this.loadChuyenDe();
    }//GEN-LAST:event_txtFindChuyenDeCaretUpdate

    private void pnlWapperChuyenDeMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlWapperChuyenDeMouseReleased
        // TODO add your handling code here:
        this.showPopMenu(evt);
    }//GEN-LAST:event_pnlWapperChuyenDeMouseReleased

    private void mniChuyenDeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniChuyenDeActionPerformed
        // TODO add your handling code here:
        this.cardLayout(pnlTableChuyenDe, pnlChuyenDe);
    }//GEN-LAST:event_mniChuyenDeActionPerformed

    private void mniKhoaHocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniKhoaHocActionPerformed
        // TODO add your handling code here:
        this.cardLayout(pnlTableKhoaHoc, pnlKhoaHoc);
    }//GEN-LAST:event_mniKhoaHocActionPerformed

    private void mniTrangChuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniTrangChuActionPerformed
        // TODO add your handling code here:
        this.cardLayout(pnlTrangChu, pnlHome);
    }//GEN-LAST:event_mniTrangChuActionPerformed

    private void mniNguoiHocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniNguoiHocActionPerformed
        // TODO add your handling code here:
        this.cardLayout(pnlTableNguoiHoc, pnlNguoiHoc);
    }//GEN-LAST:event_mniNguoiHocActionPerformed

    private void mniNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniNhanVienActionPerformed
        // TODO add your handling code here:
        this.cardLayout(pnlTableNhanVien, pnlNhanVien);
    }//GEN-LAST:event_mniNhanVienActionPerformed

    private void mniThongKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniThongKeActionPerformed
        // TODO add your handling code here:
        this.cardLayout(pnlTableThongKe, pnlThongKe);
    }//GEN-LAST:event_mniThongKeActionPerformed

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
        this.loadKhoaHoc();
    }//GEN-LAST:event_txtFindKhoaHocCaretUpdate

    private void pnlWapperKhoaHocMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlWapperKhoaHocMouseReleased
        // TODO add your handling code here:
        this.showPopMenu(evt);
    }//GEN-LAST:event_pnlWapperKhoaHocMouseReleased

    private void txtFindNguoiHocCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtFindNguoiHocCaretUpdate
        // TODO add your handling code here:
        this.loadNguoiHoc();
    }//GEN-LAST:event_txtFindNguoiHocCaretUpdate

    private void tblNguoiHocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNguoiHocMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.index = tblNguoiHoc.rowAtPoint(evt.getPoint());
            if (this.index >= 0) {
                new ThongTinNguoiHocFrame(index).setVisible(true);
            }
        }
    }//GEN-LAST:event_tblNguoiHocMouseClicked

    private void pnlWapperNguoiHocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlWapperNguoiHocMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_pnlWapperNguoiHocMouseClicked

    private void tblNhanVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNhanVienMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.index = tblNhanVien.rowAtPoint(evt.getPoint());
            if (this.index >= 0) {
                new ThongTinNhanVienJFrame(index).setVisible(true);
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

    private void btnThemChuyenDeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemChuyenDeActionPerformed
        // TODO add your handling code here:
        new ThongTinChuyenDeJFrame().setVisible(true);
    }//GEN-LAST:event_btnThemChuyenDeActionPerformed

    private void btnThemKhoaHocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemKhoaHocActionPerformed
        // TODO add your handling code here:
        new ThongTinKhoaHocJFrame().setVisible(true);
    }//GEN-LAST:event_btnThemKhoaHocActionPerformed

    private void txtFindNhanVienCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtFindNhanVienCaretUpdate
        // TODO add your handling code here:
        this.loadNhanVien();
    }//GEN-LAST:event_txtFindNhanVienCaretUpdate

    private void pnlWapperNhanVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlWapperNhanVienMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_pnlWapperNhanVienMouseClicked

    private void btnThemNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemNhanVienActionPerformed
        // TODO add your handling code here:
        new ThongTinNhanVienJFrame().setVisible(true);
    }//GEN-LAST:event_btnThemNhanVienActionPerformed

    private void btnThemNguoiHocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemNguoiHocActionPerformed
        // TODO add your handling code here:
        new ThongTinNguoiHocFrame().setVisible(true);
    }//GEN-LAST:event_btnThemNguoiHocActionPerformed

    private void pnlThanhTrangThaiMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlThanhTrangThaiMousePressed
        // TODO add your handling code here:
        mouseX = evt.getX();
        mouseY = evt.getY();
    }//GEN-LAST:event_pnlThanhTrangThaiMousePressed

    private void pnlThanhTrangThaiMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlThanhTrangThaiMouseDragged
        // TODO add your handling code here:
        if (!evt.isMetaDown()) {
            Point p = getLocation();
            this.setLocation(p.x + evt.getX() - mouseX, p.y + evt.getY() - mouseY);
        }
    }//GEN-LAST:event_pnlThanhTrangThaiMouseDragged

    private void btnOpenBrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenBrActionPerformed
        try {
            // TODO add your handling code here:
            this.openWebsite();
        } catch (URISyntaxException | IOException ex) {
        }
    }//GEN-LAST:event_btnOpenBrActionPerformed

    private void btnAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAboutActionPerformed
        // TODO add your handling code here:
        this.openAbout();
    }//GEN-LAST:event_btnAboutActionPerformed

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
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnKhoaHoc;
    private javax.swing.JButton btnNguoiHoc;
    private javax.swing.JButton btnNhanVien;
    private javax.swing.JButton btnOpenBr;
    private javax.swing.JButton btnThemChuyenDe;
    private javax.swing.JButton btnThemKhoaHoc;
    private javax.swing.JButton btnThemNguoiHoc;
    private javax.swing.JButton btnThemNhanVien;
    private javax.swing.JButton btnThongKe;
    private javax.swing.JComboBox<String> cboKhoaHoc;
    private javax.swing.JComboBox<String> cboNam;
    public static javax.swing.JLabel lblChao;
    private javax.swing.JLabel lblDongHo;
    private javax.swing.JLabel lblFindChuyenDe;
    private javax.swing.JLabel lblFindKhoaHoc;
    private javax.swing.JLabel lblFindNguoiHoc;
    private javax.swing.JLabel lblFindNhanVien;
    private javax.swing.JLabel lblKhoaHoc;
    private javax.swing.JLabel lblMain;
    private javax.swing.JLabel lblNam;
    private javax.swing.JLabel lblTitleChuyenDe;
    private javax.swing.JLabel lblTitleKhoaHoc;
    private javax.swing.JLabel lblTitleNhanVien;
    private javax.swing.JLabel lblTitleNhanVien1;
    private javax.swing.JLabel lblTitleThongKe;
    private javax.swing.JMenuItem mniChuyenDe;
    private javax.swing.JMenuItem mniKhoaHoc;
    private javax.swing.JMenuItem mniNguoiHoc;
    private javax.swing.JMenuItem mniNhanVien;
    private javax.swing.JMenuItem mniThongKe;
    private javax.swing.JMenuItem mniTrangChu;
    private javax.swing.JPopupMenu pmn;
    private javax.swing.JPanel pnlBangDiem;
    private javax.swing.JPanel pnlChuyenDe;
    private javax.swing.JPanel pnlDieuHuong;
    private javax.swing.JPanel pnlDoanhThu;
    private javax.swing.JPanel pnlFindChuyenDe;
    private javax.swing.JPanel pnlFindKhoaHoc;
    private javax.swing.JPanel pnlFindNguoiHoc;
    private javax.swing.JPanel pnlFindNhanVien;
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
    private javax.swing.JPanel pnlThanhTrangThai;
    private javax.swing.JPanel pnlThongKe;
    private javax.swing.JPanel pnlThongKeNguoiHoc;
    private javax.swing.JPanel pnlThongTinNhanVien;
    private javax.swing.JPanel pnlTongHopDiem;
    private javax.swing.JPanel pnlTrangChu;
    private javax.swing.JPanel pnlTrangThaiBottom;
    private javax.swing.JPanel pnlWapper;
    private javax.swing.JPanel pnlWapperChuyenDe;
    private javax.swing.JPanel pnlWapperKhoaHoc;
    private javax.swing.JPanel pnlWapperNguoiHoc;
    private javax.swing.JPanel pnlWapperNhanVien;
    private javax.swing.JPanel pnlWapperThongKe;
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
    public static javax.swing.JTable tblNhanVien;
    private javax.swing.JTable tblThongKeNguoiHoc;
    private javax.swing.JTable tblTongHop;
    private javax.swing.JTextField txtFindChuyenDe;
    private javax.swing.JTextField txtFindKhoaHoc;
    public static javax.swing.JTextField txtFindNguoiHoc;
    public static javax.swing.JTextField txtFindNhanVien;
    // End of variables declaration//GEN-END:variables
}
