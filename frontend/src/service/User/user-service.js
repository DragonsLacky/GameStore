import axios from '../server/AxiosInstance';
import authHeader from '../Auth/auth-header';

const UserService = {
  loadUser(username) {
    return axios.post(`/user/${username}`, {
      header: authHeader(),
      username: username,
      email: 'email',
    });
  },
};

export default UserService;
