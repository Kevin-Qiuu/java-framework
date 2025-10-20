package com.bitejiuyeke.biteadminservice.user.mapper.handler;

import cn.hutool.crypto.SecureUtil;
import com.bitejiuyeke.biteadminservice.user.domain.entity.Encrypt;
import com.bitejiuyeke.bitecommoncore.utils.AESUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Encrypt → String（加密）：当 MyBatis 需要将 Encrypt 对象转换为数据库存储的字符串时，会调用加密算法<br>
 * String → Encrypt（解密）：当 MyBatis 需要将数据库中的加密字符串转换为 Encrypt 对象时，会调用解密算法<br>
 */

@MappedTypes(Encrypt.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class EncryptTypeHandler extends BaseTypeHandler<Encrypt> {

    /**
     * 加密字段
     *
     * @param ps        预编译的 SQL 语句对象
     * @param i         参数在 SQL 中的位置索引
     * @param parameter 待加密字段
     * @param jdbcType  加密后的字段
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Encrypt parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null || parameter.getValue() == null) {
            ps.setString(i, "");
            return;
        }

        String encryptData = AESUtil.encryptHex(parameter.getValue());
        ps.setString(i, encryptData);
    }

    /**
     * 从 ResultSet 中根据列名获取 Encrypt 对象（从数据库读取时调用）
     *
     * @param rs         MyBatis 查询的结果
     * @param columnName 需要解密的列名
     * @return Encrypt
     */
    @Override
    public Encrypt getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String decryptData = AESUtil.decryptHex(rs.getString(columnName));
        return new Encrypt(decryptData);
    }

    /**
     * 从 ResultSet 中根据列索引获取 Encrypt 对象（从数据库读取时调用）
     *
     * @param rs          MyBatis 查询的结果
     * @param columnIndex 需要解密的列索引
     * @return Encrypt
     */
    @Override
    public Encrypt getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String decryptData = AESUtil.decryptHex(rs.getString(columnIndex));
        return new Encrypt(decryptData);
    }

    /**
     * 从存储过程的结果中获取 Encrypt 对象
     *
     * @param cs          MyBatis 存储过程的结果
     * @param columnIndex 需要解密的列索引
     * @return Encrypt
     */
    @Override
    public Encrypt getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String decryptData = AESUtil.decryptHex(cs.getString(columnIndex));
        return new Encrypt(decryptData);
    }
}
