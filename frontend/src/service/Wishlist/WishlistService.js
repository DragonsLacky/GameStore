import authHeader from '../Auth/auth-header';
import axios from '../server/AxiosInstance';

const WishlistService = {
  fetchWishlistGames: (username) => {
    return axios.get(`/wishlist/${username}`, {
      header: authHeader(),
    });
  },
  addGameToWishlist: (username, gameId) => {
    return axios.put('/wishlist/add', {
      header: authHeader(),
      username,
      gameId,
    });
  },
  removeGameFromWishlist: (username, gameId) => {
    console.log(username, gameId);
    return axios.post('/wishlist/remove', {
      header: authHeader(),
      username,
      gameId,
    });
  },
  clearWishlist: (username) => {
    return axios.delete(`/wishlist/${username}`, {
      header: authHeader(),
    });
  },
};

export default WishlistService;
