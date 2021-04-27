import axios from 'axios';

const Axios = axios.create({
  baseURL: 'http://localhost:5000/api',
  headers: {
    'Access-Control-Allow-Origin': '*',
  },
});

export default Axios;
