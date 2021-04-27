import authHeader from '../Auth/auth-header';
import axios from '../server/AxiosInstance';

const GenreService = {
  fetchGenres: () => {
    return axios.get('/genre', {
      headers: authHeader(),
    });
  },
};

export default GenreService;
