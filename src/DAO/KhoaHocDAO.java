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
import model.KhoaHoc;

/**
 *
 * @author duann
 */
public class KhoaHocDAO {

    public void insert(KhoaHoc khoaHoc) {
        String sql = "INSERT INTO KhoaHoc (MaCD, HocPhi, ThoiLuong, NgayKG, GhiChu, MaNV) VALUES (?, ?, ?, ?, ?, ?)";
        Jdbc.executeUpdate(sql,
                khoaHoc.getMaCD(),
                khoaHoc.getHocPhi(),
                khoaHoc.getThoiLuong(),
                khoaHoc.getNgayKG(),
                khoaHoc.getGhiChu(),
                khoaHoc.getMaNV()
        );
    }

    public void update(KhoaHoc khoaHoc) {
        String sql = "UPDATE KhoaHoc SET MaCD=?, HocPhi=?, ThoiLuong=?, NgayKG=?, GhiChu=?, MaNV=? WHERE MaKH=?";
        Jdbc.executeUpdate(sql,
                khoaHoc.getMaCD(),
                khoaHoc.getHocPhi(),
                khoaHoc.getThoiLuong(),
                khoaHoc.getNgayKG(),
                khoaHoc.getGhiChu(),
                khoaHoc.getMaNV(),
                khoaHoc.getMaKH()
        );
    }

    public void delete(Integer MaKH) {
        String sql = "DELETE FROM KhoaHoc WHERE MaKH = ?";
        Jdbc.executeUpdate(sql, MaKH);
    }

    private KhoaHoc readFormResultSet(ResultSet rs) throws SQLException {
        KhoaHoc model = new KhoaHoc();
        model.setMaKH(rs.getInt("MaKH"));
        model.setHocPhi(rs.getDouble("HocPhi"));
        model.setThoiLuong(rs.getInt("ThoiLuong"));
        model.setNgayKG(rs.getDate("NgayKG"));
        model.setGhiChu(rs.getString("GhiChu"));
        model.setMaNV(rs.getString("MaNV"));
        model.setNgayTao(rs.getDate("NgayTao"));
        model.setMaCD(rs.getString("MaCD"));
        return model;
    }

    private List<KhoaHoc> select(String sql, Object... args) {
        List<KhoaHoc> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = Jdbc.executeQuery(sql, args);
                while (rs.next()) {
                    KhoaHoc khoaHoc = readFormResultSet(rs);
                    list.add(khoaHoc);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<KhoaHoc> select() {
        String sql = "SELECT * FROM KhoaHoc";
        return select(sql);
    }

    public KhoaHoc findById(Integer makh) {
        String sql = "SELECT * FROM KhoaHoc WHERE MaKH=?";
        List<KhoaHoc> list = select(sql, makh);
        return list.size() > 0 ? list.get(0) : null;
    }
    
    public List<KhoaHoc> selectByKeyword(String keyword) {
        String sql = "SELECT * FROM KhoaHoc WHERE MaCD LIKE ?";
        return select(sql, "%" + keyword + "%");
    }
}
