package com.example.chat.mapper;




import com.example.chat.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User findById(int id);
    User findByUsername(String username);
    int insertUser(User user);
}