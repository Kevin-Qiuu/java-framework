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
 * 1. setNonNullParameter方法调用时机<br>
 * 场景: 当执行SQL插入或更新操作时<br>
 * 触发条件: 处理带有@Param注解或实体类中的Encrypt类型字段<br>
 * 具体过程: MyBatis构建PreparedStatement时，遇到Encrypt类型的参数会自动调用此方法<br>
 * <br>
 * 2. getNullableResult系列方法调用时机<br>
 * 场景: 当执行SQL查询操作时<br>
 * 触发条件: 从ResultSet中获取数据并映射到Java对象的Encrypt类型属性时<br>
 */

@MappedTypes(Encrypt.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class EncryptTypeHandler extends BaseTypeHandler<Encrypt> {

    /**
     * 加密字段
     *
     * @param ps 预编译的 SQL 语句对象
     * @param i 参数在 SQL 中的位置索引
     * @param parameter 待加密字段
     * @param jdbcType 加密后的字段
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
     * @param rs MyBatis 查询的结果
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
     * @param rs MyBatis 查询的结果
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
     * @param cs MyBatis 存储过程的结果
     * @param columnIndex 需要解密的列索引
     * @return Encrypt
     */
    @Override
    public Encrypt getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String decryptData = AESUtil.decryptHex(cs.getString(columnIndex));
        return new Encrypt(decryptData);
    }
}
