import axios from '../server/AxiosInstance';
import authHeader from '../Auth/auth-header';

const UserService = {
  loadUser(username) {
    return axios.post(
      `/user/${username}`,
      {
        username: username,
        email: 'email',
      },
      {
        headers: authHeader(),
      }
    );
  },
};

export default UserService;
