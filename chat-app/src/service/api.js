import axios from 'axios';

const API_URL = 'http://localhost:8089/api';

const register = (user) => {
    return axios.post(`${API_URL}/users/register`, JSON.stringify(user),{headers:{
            "Content-Type":"application/json"
        }});
};

const login = (username, password) => {
    return axios.post(`${API_URL}/users/login`, null, { params: { username, password } });
};

const sendMessage = (message) => {
    return axios.post(`${API_URL}/messages/send`, JSON.stringify(message),{headers:{
            "Content-Type":"application/json"
        }});
};

const getUserById = (id) => {
    return axios.get(`${API_URL}/users/${id}`);
};

const getUserByUsername = (username) => {
    return axios.get(`${API_URL}/users/${username}`);
};

const addFriendship = (user1Id, user2Id) => {
    return axios.post(`${API_URL}/friendships/add`, null, { params: { user1Id, user2Id } });
};

const getFriendsByUserId = (userId) => {
    return axios.get(`${API_URL}/friendships/${userId}`);
};

const acceptFriendship = (user1Id, user2Id) => {
    return axios.post(`${API_URL}/friendships/accept`, null, { params: { user1Id, user2Id } });
};

const getAcceptedFriends = (userId) => {
    return axios.get(`${API_URL}/friendships/${userId}`);
};

const getMessagesBetweenUsers = (senderId, receiverId) => {
    return axios.get(`${API_URL}/messages/${senderId}/${receiverId}`);
};

export default {
    register,
    login,
    sendMessage,
    getUserById,
    getUserByUsername,
    addFriendship,
    getFriendsByUserId,
    acceptFriendship,
    getAcceptedFriends,
    getMessagesBetweenUsers
};