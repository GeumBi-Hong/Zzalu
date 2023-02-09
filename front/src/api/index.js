// local vue api axios instance
import axios from 'axios';
const token = window.localStorage.getItem('token')

function apiInstance() {
  const instance = axios.create({
    baseURL: process.env.VUE_APP_API_BASE_URL,
    headers: {
      'Content-Type': 'application/json;charset=utf-8',
    },
  });
  return instance;
}

function authApiInstance() {
  console.log(token)
  const instance = axios.create({
    baseURL: process.env.VUE_APP_API_BASE_URL,
    headers: {
      'Content-Type': 'application/json;charset=utf-8',
      'Authorization': `Bearer ${ token }`,
    },
  });
  return instance;
}

export { apiInstance, authApiInstance };
