package app.Dao;

import Mybatis.annotation.Mapper;
import Mybatis.annotation.Param;


@Mapper
public interface StudentDao {


    void insertuser(@Param("name")String name, @Param("id")String id, @Param("age")String age);

}
