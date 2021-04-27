import authHeader from '../Auth/auth-header';
import axios from '../server/AxiosInstance';

const CartService = {
  fetchCartGames: (username) => {
    return axios.get(`/cart/${username}`);
  },
  addGameToCart: (username, gameId) => {
    return axios.put('/cart/add', {
      header: authHeader(),
      username,
      gameId,
    });
  },
  removeGameFromCart: (username, gameId) => {
    console.log(username, gameId);
    return axios.post('/cart/remove', {
      header: authHeader(),
      username,
      gameId,
    });
  },
  buyCart: (username) => {
    return axios.post(`/cart/buy/${username}`, {
      header: authHeader(),
    });
  },
  clearCart: (username) => {
    return axios.delete(`/cart/clear/${username}`, {
      header: authHeader(),
    });
  },
};

export default CartService;
