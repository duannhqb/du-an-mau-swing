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
import model.HocVien;

/**
 *
 * @author duann
 */
public class HocVienDAO {

    public void insert(HocVien hocVien) {
        String sql = "INSERT INTO HocVien(MaKH, MaNH, Diem) VALUES(?, ?, ?)";
        Jdbc.executeUpdate(sql,
                hocVien.getMaKH(),
                hocVien.getMaNH(),
                hocVien.getDiem()
        );
    }

    public void update(HocVien hocVien) {
        String sql = "UPDATE HocVien SET MaKH=?, MaNH=?, Diem=? WHERE MaHV=?";
        Jdbc.executeUpdate(sql,
                hocVien.getMaKH(),
                hocVien.getMaNH(),
                hocVien.getDiem(),
                hocVien.getMaHV()
        );
    }

    public void delete(Integer MaHV) {
        String sql = "DELETE FROM HocVien WHERE MaHV=?";
        Jdbc.executeUpdate(sql, MaHV);
    }

    private HocVien readFormResultSet(ResultSet rs) throws SQLException {
        HocVien hocVien = new HocVien();
        hocVien.setMaHV(rs.getInt(1));
        hocVien.setMaKH(rs.getInt(2));
        hocVien.setMaNH(rs.getString(3));
        hocVien.setDiem(rs.getDouble(4));
        return hocVien;
    }

    private List<HocVien> select(String sql, Object... args) {
        List<HocVien> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = Jdbc.executeQuery(sql, args);
                while (rs.next()) {
                    HocVien hocVien = readFormResultSet(rs);
                    list.add(hocVien);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<HocVien> select() {
        String sql = "SELECT * FROM HocVien";
        return select(sql);
    }

    public HocVien findById(Integer mahv) {
        String sql = "SELECT * FROM HocVien WHERE MaHV = ?";
        List<HocVien> list = select(sql, mahv);
        return list.size() > 0 ? list.get(0) : null;
    }
}
