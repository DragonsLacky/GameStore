import authHeader from '../Auth/auth-header';
import axios from '../server/AxiosInstance';

const CartService = {
  fetchCartGames: (username) => {
    return axios.get(`/cart/${username}`, {
      headers: authHeader(),
    });
  },
  addGameToCart: (username, gameId) => {
    return axios.put(
      '/cart/add',
      {
        username,
        gameId,
      },
      {
        headers: authHeader(),
      }
    );
  },
  removeGameFromCart: (username, gameId) => {
    console.log(username, gameId);
    return axios.post(
      '/cart/remove',
      {
        username,
        gameId,
      },
      {
        headers: authHeader(),
      }
    );
  },
  buyCart: (username) => {
    return axios.post(
      `/cart/buy/${username}`,
      {},
      {
        headers: authHeader(),
      }
    );
  },
  clearCart: (username) => {
    return axios.delete(`/cart/clear/${username}`, {
      headers: authHeader(),
    });
  },
};

export default CartService;
