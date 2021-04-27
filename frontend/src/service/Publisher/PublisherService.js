import authHeader from '../Auth/auth-header';
import axios from '../server/AxiosInstance';

const PublisherService = {
  fetchPublishers: () => {
    return axios.get('/publisher', {
      headers: authHeader(),
    });
  },
  fetchOwnedPublishers: (username) => {
    return axios.get(`/publisher/${username}`, {
      headers: authHeader(),
    });
  },
  createPublisher: (name, description, username) => {
    return axios.post(
      '/publisher/add',
      {
        name,
        description,
        username,
      },
      { headers: authHeader() }
    );
  },
  editPublisher: (publisherId, name, description, username) => {
    return axios.put(
      `/publisher/edit/${publisherId}`,
      {
        name,
        description,
        username,
      },
      { headers: authHeader() }
    );
  },
  deletePublisher: (publisherId) => {
    return axios.delete(`/publisher/remove/${publisherId}`, {
      headers: authHeader(),
    });
  },
};

export default PublisherService;
