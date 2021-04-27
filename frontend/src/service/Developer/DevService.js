import authHeader from '../Auth/auth-header';
import axios from '../server/AxiosInstance';

const DevService = {
  fetchDevs: () => {
    return axios.get('/dev', {
      header: authHeader(),
    });
  },
  fetchPublisherDevs: (publisherId) => {
    return axios.get(`/dev/${publisherId}`, {
      header: authHeader(),
    });
  },
  createDev: (name, publisherId, username) => {
    return axios.post('/dev/add', {
      header: authHeader(),
      name,
      publisherId,
      username,
    });
  },
};

export default DevService;
