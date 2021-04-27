import authHeader from '../Auth/auth-header';
import axios from '../server/AxiosInstance';

const PublisherService = {
  fetchPublishers: () => {
    return axios.get('/publisher', {
      header: authHeader(),
    });
  },
  fetchOwnedPublishers: (username) => {
    return axios.get(`/publisher/${username}`, {
      header: authHeader(),
    });
  },
  createPublisher: (name, description, username) => {
    return axios.post('/publisher/add', {
      header: authHeader(),

      name,
      description,
      username,
    });
  },
};

export default PublisherService;
