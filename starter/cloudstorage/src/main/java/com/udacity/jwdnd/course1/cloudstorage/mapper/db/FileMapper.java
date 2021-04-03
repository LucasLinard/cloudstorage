package com.udacity.jwdnd.course1.cloudstorage.mapper.db;

import com.udacity.jwdnd.course1.cloudstorage.model.db.File;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FileMapper {
  @Select("SELECT * from files where userid=#{userid}")
  List<File> getFiles(Integer userId);

  @Select("SELECT * FROM files where fileName=#{fileName} and userid=#{userid}")
  File getFile(String fileName, Integer userid);

  @Options(useGeneratedKeys = true, keyProperty = "fileId")
  @Insert(
      "INSERT INTO files (filename,contenttype,filesize,userid,filedata) VALUES (#{fileName},#{contentType},#{fileSize},#{userid},#{fileData})")
  int insert(File file);

  @Delete("DELETE from FILES where fileName=#{fileName} and userid=#{userid}")
  int delete(String fileName, Integer userid);
}
