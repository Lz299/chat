package com.example.chat.pojo;


import java.util.Date;

    public class Friendship {

        private int id;
        private int user1Id;
        private int user2Id;
        private Date created_at;
        private boolean isAccepted;

        public Friendship() {
        }

        // Getters and Setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUser1Id() {
            return user1Id;
        }

        public void setUser1Id(int user1Id) {
            this.user1Id = user1Id;
        }

        public int getUser2Id() {
            return user2Id;
        }

        public void setUser2Id(int user2Id) {
            this.user2Id = user2Id;
        }

        public Date getCreated_at() {
            return created_at;
        }

        public void setCreated_at(Date created_at) {
            this.created_at = created_at;
        }

        public boolean isAccepted() {
            return isAccepted;
        }

        public void setAccepted(boolean accepted) {
            isAccepted = accepted;
        }

        @Override
        public String toString() {
            return "Friendship{" +
                    "id=" + id +
                    ", user1Id=" + user1Id +
                    ", user2Id=" + user2Id +
                    ", created_at=" + created_at +
                    ", isAccepted=" + isAccepted +
                    '}';
        }
    }
