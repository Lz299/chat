package com.example.chat.mapper;



import com.example.chat.pojo.FriendRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FriendshipMapper {
    int addFriendship(@Param("user1Id") int user1Id, @Param("user2Id") int user2Id);
    List<Integer> getFriendsByUserId(int userId);
    boolean isFriendshipAccepted(@Param("user1Id") int user1Id, @Param("user2Id") int user2Id);
    void acceptFriendship(@Param("user1Id") int user1Id, @Param("user2Id") int user2Id);


    int addFriendRequest(FriendRequest friendRequest);
    List<FriendRequest> getFriendRequestsByReceiverId(int receiverId);
    void updateFriendRequestStatus(int requestId, String status);

}