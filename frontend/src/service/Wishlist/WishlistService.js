import authHeader from '../Auth/auth-header';
import axios from '../server/AxiosInstance';

const WishlistService = {
  fetchWishlistGames: (username) => {
    return axios.get(`/wishlist/${username}`, {
      headers: authHeader(),
    });
  },
  addGameToWishlist: (username, gameId) => {
    return axios.put(
      '/wishlist/add',
      {
        username,
        gameId,
      },
      { headers: authHeader() }
    );
  },
  removeGameFromWishlist: (username, gameId) => {
    return axios.post(
      '/wishlist/remove',
      {
        username,
        gameId,
      },
      { headers: authHeader() }
    );
  },
  clearWishlist: (username) => {
    return axios.delete(`/wishlist/${username}`, {
      headers: authHeader(),
    });
  },
};

export default WishlistService;
