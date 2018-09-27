/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author duann
 */
public class DialogHelper {
//    hiển thị thông báo cho người dùng

    public static void alert(Component parent, String messager) {
        JOptionPane.showMessageDialog(parent, messager, "Hệ thống quản lý đào tạo", JOptionPane.INFORMATION_MESSAGE);
    }

//    Hiển thị thông báo và yêu cầu người dùng xác nhận
    public static boolean confirm(Component parent, String messager) {
        int result = JOptionPane.showConfirmDialog(parent, messager, "Hệ thống quản lý đào tạo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }

//    hiển thị thông báo yêu cầu nhập dữ liệu;
    public static String prompt(Component parent, String messager) {
        return JOptionPane.showInputDialog(parent, messager, "Hệ thống quản lý đào tạo", JOptionPane.INFORMATION_MESSAGE);
    }
}
