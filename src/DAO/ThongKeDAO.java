/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import helper.Jdbc;
import helper.ShareHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author duann
 */
public class ThongKeDAO {

    public List<Object[]> getNguoiHoc() {
        List<Object[]> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                String sql = "{call sp_ThongKeNguoiHoc}";
                rs = Jdbc.executeQuery(sql);
                while (rs.next()) {
                    Object[] model = {
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getDate(3),
                        rs.getDate(4)
                    };
//                    add
                    list.add(model);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<Object[]> getBangDiem(Integer makh) {
        List<Object[]> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                String sql = "{call sp_BangDiem(?)}";
                rs = Jdbc.executeQuery(sql, makh);
                while (rs.next()) {
                    double diem = rs.getDouble(3);
                    String xepLoai = "Xuất sắc";
                    if (diem < 0) {
                        xepLoai = "Chưa nhập";
                    } else if (diem < 3) {
                        xepLoai = "Kém";
                    } else if (diem < 5) {
                        xepLoai = "Yếu";
                    } else if (diem < 6.5) {
                        xepLoai = "Trung Bình";
                    } else if (diem < 7.5) {
                        xepLoai = "Khá";
                    } else if (diem < 9) {
                        xepLoai = "Giỏi";
                    }
                    Object[] model = {
                        rs.getString("MaNH"),
                        rs.getString("HoTen"),
                        diem,
                        xepLoai
                    };
                    list.add(model);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<Object[]> getDiemTheoChuyenDe() {
        List<Object[]> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                String sql = "{call sp_ThongKeDiem}";
                rs = Jdbc.executeQuery(sql);
                while (rs.next()) {
                    Object[] model = {
                        rs.getString("ChuyenDe"),
                        rs.getInt("SoKH"),
                        rs.getDouble("ThapNhat"),
                        rs.getDouble("CaoNhat"),
                        ShareHelper.FORMATTER.format(rs.getDouble("TrungBinh"))
                    };
//                    add
                    list.add(model);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<Object[]> getDoanhThu(int nam) {
        List<Object[]> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                String sql = "{call sp_ThongKeDoanhThu(?)}";
                rs = Jdbc.executeQuery(sql, nam);
                while (rs.next()) {
                    Object[] model = {
                        rs.getString(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getDouble(4),
                        rs.getDouble(5),
                        rs.getDouble(6),
                        rs.getDouble(7)
                    };
//                    add
                    list.add(model);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
