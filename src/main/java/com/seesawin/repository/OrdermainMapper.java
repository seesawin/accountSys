package com.seesawin.repository;

import com.seesawin.models.Ordermain;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

public interface OrdermainMapper {
    @Delete({
        "delete from orderMain",
        "where orderNo = #{orderno,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer orderno);

    @Insert({
        "insert into orderMain (username, totalPrice)",
        "values (#{username,jdbcType=VARCHAR}, #{totalprice,jdbcType=INTEGER})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="orderno", before=false, resultType=Integer.class)
    int insert(Ordermain record);

    @Select({
        "select",
        "orderNo, username, totalPrice",
        "from orderMain",
        "where orderNo = #{orderno,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="orderNo", property="orderno", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="username", property="username", jdbcType=JdbcType.VARCHAR),
        @Result(column="totalPrice", property="totalprice", jdbcType=JdbcType.INTEGER)
    })
    Ordermain selectByPrimaryKey(Integer orderno);

    @Select({
        "select",
        "orderNo, username, totalPrice",
        "from orderMain"
    })
    @Results({
        @Result(column="orderNo", property="orderno", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="username", property="username", jdbcType=JdbcType.VARCHAR),
        @Result(column="totalPrice", property="totalprice", jdbcType=JdbcType.INTEGER)
    })
    List<Ordermain> selectAll();

    @Update({
        "update orderMain",
        "set username = #{username,jdbcType=VARCHAR},",
          "totalPrice = #{totalprice,jdbcType=INTEGER}",
        "where orderNo = #{orderno,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Ordermain record);

    @Select({
            "select",
            "orderNo, username, totalPrice",
            "from orderMain",
            "where username = #{username,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(column="orderNo", property="orderno", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="username", property="username", jdbcType=JdbcType.VARCHAR),
            @Result(column="totalPrice", property="totalprice", jdbcType=JdbcType.INTEGER)
    })
    List<Ordermain> selectByUsername(String username);
}