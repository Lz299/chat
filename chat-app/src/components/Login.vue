<template>
  <div>
    <h2>Login</h2>
    <form @submit.prevent="loginUser">
      <div>
        <label for="username">Username:</label>
        <input v-model="credentials.username" id="username" type="text" required>
      </div>
      <div>
        <label for="password">Password:</label>
        <input v-model="credentials.password" id="password" type="password" required>
      </div>
      <button type="submit">Login</button>
    </form>
  </div>
</template>

<script>
import api from '../service/api';
import router from '../router';

export default {
  data() {
    return {
      credentials: {
        username: '',
        password: ''
      }
    };
  },
  methods: {
    async loginUser() {
      try {
        api.login(this.credentials.username, this.credentials.password).then(respone=>{
          console.log('User logged in:', respone.data);
          if (respone.data!=0){
            localStorage.setItem("loginId",respone.data)
            router.push('/home');
          }
        })

      } catch (error) {
        console.error('Error logging in:', error);
      }
    }
  }
};
</script>