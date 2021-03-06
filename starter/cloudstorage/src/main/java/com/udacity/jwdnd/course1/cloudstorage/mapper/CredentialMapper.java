package com.udacity.jwdnd.course1.cloudstorage.Mapper;

import com.udacity.jwdnd.course1.cloudstorage.Model.db.Credential;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("SELECT * from CREDENTIALS where userid=#{userId}")
    List<Credential>getAllCredentials(Integer userId);

    @Insert("INSERT INTO CREDENTIALS (url,username,key,password,userid) VALUES(#{url},#{userName},#{key},#{password},#{userId}) ")
    @Options(useGeneratedKeys = true, keyProperty="credentialId")
    int insert (Credential credential);

    @Update("UPDATE CREDENTIALS SET url=#{url}, username=#{userName}, key=#{key}, password=#{password} WHERE credentialId = #{credentialId}")
    void updateCredential(Integer credentialId,String url, String userName,String key, String password);

    @Delete("DELETE from CREDENTIALS where credentialid=#{credentialId} AND userid=#{userId}")
    int deleteCredential(Integer credentialId,Integer userId);
}
