/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import helper.Jdbc;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.NhanVien;

/**
 *
 * @author duann
 */
public class NhanVienDAO {
//    thêm mới

    public void insert(NhanVien nhanVien) {
        String sql = "INSERT INTO NhanVien (MaNV, MatKhau, HoTen, VaiTro) VALUES (?, ?, ?, ?)";
        Jdbc.executeUpdate(sql,
                nhanVien.getMaNV(),
                nhanVien.getMatKhau(),
                nhanVien.getHoTen(),
                nhanVien.isVaiTro()
        );
    }

//    cập nhật
    public void update(NhanVien nhanVien) {
        String sql = "UPDATE NhanVien SET MatKhau=?, HoTen=?, VaiTro=? WHERE MaNV=?";
        Jdbc.executeUpdate(sql,
                nhanVien.getMatKhau(),
                nhanVien.getHoTen(),
                nhanVien.isVaiTro(),
                nhanVien.getMaNV()
        );
    }

//    delete
    public void delete(String MaNV) {
        String sql = "DELETE FROM NhanVien WHERE MaNV=?";
        Jdbc.executeUpdate(sql, MaNV);
    }

    private NhanVien readFormResultSet(ResultSet rs) throws SQLException {
        NhanVien nhanVien = new NhanVien();
//        nhanVien.setMaNV(rs.getString("MaNV"));
//        nhanVien.setMatKhau(rs.getString("MatKhau"));
//        nhanVien.setHoTen(rs.getString("HoTen"));
//        nhanVien.setVaiTro(rs.getBoolean("VaiTro"));
        nhanVien.setMaNV(rs.getString(1));
        nhanVien.setMatKhau(rs.getString(2));
        nhanVien.setHoTen(rs.getString(3));
        nhanVien.setVaiTro(rs.getBoolean(4));
        return nhanVien;
    }

    private List<NhanVien> select(String sql, Object... args) {
        List<NhanVien> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = Jdbc.executeQuery(sql, args);
                while (rs.next()) {
                    NhanVien nhanVien = readFormResultSet(rs);
                    list.add(nhanVien);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<NhanVien> select() {
        String sql = "SELECT * FROM NhanVien";
        return select(sql);
    }

    public List<NhanVien> selectByKeyword(String keyword) {
        String sql = "SELECT * FROM NhanVien WHERE HoTen LIKE ?";
        return select(sql, "%" + keyword + "%");
    }
    
    public NhanVien findById(String maNV) {
        String sql = "SELECT * FROM NhanVien WHERE MaNV=?";
        List<NhanVien> list = select(sql, maNV);
//      list được lưu giá trị từ select theo mã nhân viên
//      nếu list.size lớn hơn 0 là đúng thì sẽ trả về vị trí đầu tiên trong list đó ( list.get(0) ) còn là sai thì sẽ return về null
        return list.size() > 0 ? list.get(0) : null;
    }
}
