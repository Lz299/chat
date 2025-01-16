import { createRouter, createWebHistory } from 'vue-router';
import Register from '../components/Register.vue';
import UserLogin from '../components/Login.vue';
import Chat from '../components/Chat.vue';
import Home from '../components/Home.vue';

const routes = [
    { path: '/register', component: Register },
    { path: '/login', component: UserLogin },
    { path: '/home', component: Home },
    { path: '/chat/:senderId/:receiverId', name: 'Chat', component: Chat }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;