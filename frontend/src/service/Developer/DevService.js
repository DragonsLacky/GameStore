import authHeader from '../Auth/auth-header';
import axios from '../server/AxiosInstance';

const DevService = {
  fetchDevs: () => {
    return axios.get('/dev', {
      headers: authHeader(),
    });
  },
  fetchPublisherDevs: (publisherId) => {
    return axios.get(`/dev/${publisherId}`, {
      headers: authHeader(),
    });
  },
  createDev: (name, publisherId, username) => {
    return axios.post(
      '/dev/add',
      {
        name,
        publisherId,
        username,
      },
      {
        headers: authHeader(),
      }
    );
  },
  editDev: (devId, name, publisherId, username) => {
    return axios.put(
      `/dev/edit/${devId}`,
      {
        name,
        publisherId,
        username,
      },
      {
        headers: authHeader(),
      }
    );
  },
  deleteDeveloper: (developerId) => {
    return axios.delete(`/dev/remove/${developerId}`, {
      headers: authHeader(),
    });
  },
};

export default DevService;
