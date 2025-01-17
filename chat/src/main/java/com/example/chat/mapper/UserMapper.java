package com.example.chat.mapper;




import com.example.chat.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    User findById(int id);
    User findByUsername(String username);
    int insertUser(User user);

        List<User> searchUsers(String keyword);
    List<User> getUsersByIds(List<Integer> ids);


}