<template>
  <div>
    <h2>Register</h2>
    <form @submit.prevent="registerUser">
      <div>
        <label for="username">Username:</label>
        <input v-model="user.username" id="username" type="text" required>
      </div>
      <div>
        <label for="password">Password:</label>
        <input v-model="user.password" id="password" type="password" required>
      </div>
      <div>
        <label for="email">Email:</label>
        <input v-model="user.email" id="email" type="email" required>
      </div>
      <button type="submit">Register</button>
    </form>
  </div>
</template>

<script>
import api from '../service/api';

export default {
  data() {
    return {
      user: {
        username: '',
        password: '',
        email: ''
      }
    };
  },
  methods: {
    async registerUser() {
      try {
        const response = await api.register(this.user);
        console.log('User registered:', response.data);
        // Redirect to login page or chat page
        this.$router.push('/login');
      } catch (error) {
        console.error('Error registering user:', error);
      }
    }
  }
};
</script>