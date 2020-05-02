package com.seesawin.repository;

import com.seesawin.models.Orderdetail;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

public interface OrderdetailMapper {
    @Delete({
        "delete from orderDetail",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into orderDetail (orderNo, name, ",
        "count, price)",
        "values (#{orderno,jdbcType=INTEGER}, #{name,jdbcType=VARCHAR}, ",
        "#{count,jdbcType=INTEGER}, #{price,jdbcType=INTEGER})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Integer.class)
    int insert(Orderdetail record);

    @Select({
        "select",
        "id, orderNo, name, count, price",
        "from orderDetail",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="orderNo", property="orderno", jdbcType=JdbcType.INTEGER),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="count", property="count", jdbcType=JdbcType.INTEGER),
        @Result(column="price", property="price", jdbcType=JdbcType.INTEGER)
    })
    Orderdetail selectByPrimaryKey(Integer id);

    @Select({
        "select",
        "id, orderNo, name, count, price",
        "from orderDetail"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="orderNo", property="orderno", jdbcType=JdbcType.INTEGER),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="count", property="count", jdbcType=JdbcType.INTEGER),
        @Result(column="price", property="price", jdbcType=JdbcType.INTEGER)
    })
    List<Orderdetail> selectAll();

    @Update({
        "update orderDetail",
        "set orderNo = #{orderno,jdbcType=INTEGER},",
          "name = #{name,jdbcType=VARCHAR},",
          "count = #{count,jdbcType=INTEGER},",
          "price = #{price,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Orderdetail record);

    @Select({
            "select",
            "id, orderNo, name, count, price",
            "from orderDetail",
            "where orderNo = #{orderNo,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="orderNo", property="orderno", jdbcType=JdbcType.INTEGER),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="count", property="count", jdbcType=JdbcType.INTEGER),
            @Result(column="price", property="price", jdbcType=JdbcType.INTEGER)
    })
    List<Orderdetail> selectByOrderNo(Integer orderNo);
}