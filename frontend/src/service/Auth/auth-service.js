import axios from '../server/AxiosInstance';

const AuthService = {
  login(username, password) {
    return axios
      .post('/auth/login', {
        username,
        password,
      })
      .then((response) => {
        if (response.data.token) {
          localStorage.setItem('user', JSON.stringify(response.data));
        }
        return response.data;
      });
  },

  logout() {
    localStorage.removeItem('user');
  },

  register(username, email, password, roles) {
    return axios.post('/auth/register', {
      username,
      email,
      password,
      roles
    });
  },
  getCurrentUser() {
    return JSON.parse(localStorage.getItem('user'));
  },
};

export default AuthService;
