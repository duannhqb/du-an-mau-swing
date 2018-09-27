/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import model.NhanVien;

/**
 *
 * @author duann
 */
public class ShareHelper {
//    ảnh biểu tượng ứng dụng xuất hiện trên mọi cửa sổ

    public static final Image APP_ICON;

    static {
//        tải biểu tượng ứng dụng
        String file = "/imgs/fpt.png";
        APP_ICON = new ImageIcon(ShareHelper.class.getResource(file)).getImage();
    }

//    public static final File PATH_IMAGE = new File("logos");
    public static final String PATH_IMAGE = "C:\\Users\\duann\\Pictures\\";

//    sao chép file logo chuyên đề vào thư mục logo;
    public static boolean saveLogo(File file) {
        File dir = new File("logos");
//        tạo thư mục nếu chưa tồn tại
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File newFile = new File(dir, file.getName());
        try {
//            copy vào thư mục logos (sẽ ghi đè nếu đã tồn tại)
            Path sourse = Paths.get(file.getAbsolutePath());
            Path destination = Paths.get(newFile.getAbsolutePath());
            Files.copy(sourse, destination, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

//  đọc hình ảnh logo chuyên đề
    public static ImageIcon readLogo(String fileName) {
        File path = new File("logos", fileName);
        return new ImageIcon(path.getAbsolutePath());
    }

//    đối tượng chứa thông tin của người sử dụng sau khi đăng nhập
    public static NhanVien USER = null;

//    Xóa thông tin của người sử dụng khi có yêu cầu đăng suất
    public static void logOff() {
        ShareHelper.USER = null;
    }

//    Kiểm tra xem đăng nhập hay chưa
    public static boolean authenticated() {
        return ShareHelper.USER != null;
    }

//    Set Table 
    public static void DrawTable(JTable tblGridView) {
        tblGridView.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tblGridView.getTableHeader().setForeground(Color.blue);

        // Column Width
        tblGridView.getColumnModel().getColumn(0).setPreferredWidth(30);
        tblGridView.setForeground(Color.black);

        // Row Height
        tblGridView.setRowHeight(20);

        // Column Center
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblGridView.setDefaultRenderer(Object.class, centerRenderer);
    }

    public static void setInfinity(JLabel label, String text) {
        label.setForeground(Color.red);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setText(text);

        new Timer(4000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label.setText("");
            }
        }).start();
    }

    public static void setCrollRight() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
        }
    }
    
//    format point
    public static NumberFormat FORMATTER = new DecimalFormat("#0.00");
}
