package com.example.chat.mapper;



import com.example.chat.pojo.FriendRequest;
import com.example.chat.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FriendshipMapper {
    void addFriendship(@Param("user1Id") int user1Id, @Param("user2Id") int user2Id);
    List<Integer> getFriendsByUserId(int userId);
    boolean isFriendshipAccepted(@Param("user1Id") int user1Id, @Param("user2Id") int user2Id);




    void acceptFriendship(@Param("user1Id") int user1Id, @Param("user2Id") int user2Id);

    void rejectFriendship(@Param("user1Id") int user1Id, @Param("user2Id") int user2Id);

    void createFriendship(@Param("user1Id") int user1Id, @Param("user2Id") int user2Id);

    List<User> getFriendRequests(@Param("userId") int userId);
}

