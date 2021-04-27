import authHeader from '../Auth/auth-header';
import axios from '../server/AxiosInstance';

const GameService = {
  fetchGames: () => {
    return axios.get('/game', {
      header: authHeader(),
    });
  },
  fetchGamesAlphabetical: () => {
    return axios.get('/game', {
      header: authHeader(),
    });
  },
  fetchGamesByGenre: (genre) => {
    return axios.get(`/game/genre/${genre}`, {
      header: authHeader(),
    });
  },
  fetchOwnedGames: (username, email) => {
    return axios.post('/game/owned', {
      header: authHeader(),
      username,
      email,
    });
  },
  fetchCreatedGames: (username, email) => {
    return axios.post('/game/created', {
      header: authHeader(),
      username,
      email,
    });
  },
  fetchDevGames: (devId) => {
    return axios.get(`/game/dev/${devId}`, {
      header: authHeader(),
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
    return axios.post('/game/add', {
      header: authHeader(),
      username,
      title,
      description,
      price,
      developerId,
      publisherId,
      genres,
    });
  },
  deleteGame: (gameId) => {
    return axios.delete(`/game/remove/${gameId}`, {
      header: authHeader(),
    });
  },
};

export default GameService;
