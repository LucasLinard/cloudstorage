package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
  @Select("SELECT * FROM Users WHERE username=#{username}")
  User getUserByUsername(String username);

  @Insert(
      "INSERT INTO Users (username, salt, password,firstname,lastname) VALUES (#{username},#{salt},#{password},#{firstName},#{lastName})")
  @Options(useGeneratedKeys = true, keyProperty = "userId")
  int insert(User users);
}