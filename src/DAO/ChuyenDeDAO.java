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
import model.ChuyenDe;

/**
 *
 * @author duann
 */
public class ChuyenDeDAO {

    public void insert(ChuyenDe model) {
        String sql = "INSERT INTO ChuyenDe (MaCD, TenCD, HocPhi, ThoiLuong, Hinh, MoTa) VALUES (?, ?, ?, ?, ?, ?)";
        Jdbc.executeUpdate(sql,
                model.getMaCD(),
                model.getTenCD(),
                model.getHocPhi(),
                model.getThoiLuong(),
                model.getHinh(),
                model.getMoTa());
    }

    public void update(ChuyenDe chuyenDe) {
        String sql = "UPDATE ChuyenDe SET TenCD=?, HocPhi=?, ThoiLuong=?, Hinh=?, MoTa=? WHERE MaCD=?";
        Jdbc.executeUpdate(sql,
                chuyenDe.getTenCD(),
                chuyenDe.getHocPhi(),
                chuyenDe.getThoiLuong(),
                chuyenDe.getHinh(),
                chuyenDe.getMoTa(),
                chuyenDe.getMaCD()
        );
    }

    public void delete(String maCD) {
        String sql = "DELETE FROM ChuyenDe WHERE MaCD=?";
        Jdbc.executeUpdate(sql, maCD);
    }

    private ChuyenDe readFormResultSet(ResultSet rs) throws SQLException {
        ChuyenDe chuyenDe = new ChuyenDe();
        chuyenDe.setMaCD(rs.getString("MaCD"));
        chuyenDe.setHinh(rs.getString("Hinh"));
        chuyenDe.setHocPhi(rs.getDouble("HocPhi"));
        chuyenDe.setMoTa(rs.getString("MoTa"));
        chuyenDe.setTenCD(rs.getString("TenCD"));
        chuyenDe.setThoiLuong(rs.getInt("ThoiLuong"));
        return chuyenDe;
    }

    private List<ChuyenDe> select(String sql, Object... args) {
        List<ChuyenDe> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = Jdbc.executeQuery(sql, args);
                while (rs.next()) {
                    ChuyenDe chuyenDe = readFormResultSet(rs);
                    list.add(chuyenDe);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<ChuyenDe> select() {
        String sql = "SELECT * from ChuyenDe";
        return select(sql);
    }

    public ChuyenDe findById(String maCD) {
        String sql = "SELECT * FROM ChuyenDe WHERE MaCD=?";
        List<ChuyenDe> list = select(sql, maCD);
        return list.size() > 0 ? list.get(0) : null;
    }

    public ChuyenDe findByName(String tenCD) {
        String sql = "SELECT * FROM ChuyenDe WHERE TenCD=?";
        List<ChuyenDe> list = select(sql, tenCD);
        return list.size() > 0 ? list.get(0) : null;
    }

    public List<ChuyenDe> selectByKeyword(String keyword) {
        String sql = "SELECT * FROM ChuyenDe WHERE TenCD LIKE ?";
        return select(sql, "%" + keyword + "%");
    }
    
     public boolean checkCD(String MaCD) {
        String sql = "SELECT * FROM ChuyenDe WHERE MaCD = ?";
        List<ChuyenDe> list = select(sql, MaCD);
        return list.isEmpty();
    }
}
