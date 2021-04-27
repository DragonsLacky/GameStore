import authHeader from '../Auth/auth-header';
import axios from '../server/AxiosInstance';

const GameService = {
  fetchGames: () => {
    return axios.get('/game', {
      headers: authHeader(),
    });
  },
  fetchGamesAlphabetical: () => {
    return axios.get('/game', {
      headers: authHeader(),
    });
  },
  fetchGamesByGenre: (genre) => {
    return axios.get(`/game/genre/${genre}`, {
      headers: authHeader(),
    });
  },
  fetchOwnedGames: (username, email) => {
    return axios.post(
      '/game/owned',
      {
        username,
        email,
      },
      {
        headers: authHeader(),
      }
    );
  },
  fetchCreatedGames: (username, email) => {
    return axios.post(
      '/game/created',
      {
        header: authHeader(),
        username,
        email,
      },
      { headers: authHeader() }
    );
  },
  fetchDevGames: (devId) => {
    return axios.get(`/game/dev/${devId}`, {
      headers: authHeader(),
    });
  },
  createGame: (
    username,
    title,
    description,
    price,
    developerId,
    publisherId,
    genres
  ) => {
    return axios.post(
      '/game/add',
      {
        username,
        title,
        description,
        price,
        developerId,
        publisherId,
        genres,
      },
      {
        headers: authHeader(),
      }
    );
  },
  deleteGame: (gameId) => {
    return axios.delete(`/game/remove/${gameId}`, {
      headers: authHeader(),
    });
  },
};

export default GameService;
