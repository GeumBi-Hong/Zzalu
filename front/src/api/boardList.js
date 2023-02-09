import { authApiInstance } from './index.js';
import { apiInstance } from './index.js';

// 로그인
const authapi = authApiInstance();
const api = apiInstance();

function getStoreList(params, data, res, err) {
    authapi.get(`/boards?gifId=${params}`)
        .then(res).catch(err)
}

function createBoard(params, datas, res, err) {
    // console.log(params,'dd',datas);
    authapi.post(`/boards`, datas)
        .then(res).catch(err)
}

function putBoardData(params, datas, res, err) {
    console.log(params, 'dd', datas);
    authapi.put(`/boards/gif/${datas[0]}`, datas[1])
        .then(res).catch(err)
}

function getUserBoard(params, res, err) {
    api.get(`/boards?username=${params}`)
        .then(res).catch(err)
}

function getBoard(params, res, err) {
    api.get(`/boards/${params}`)
        .then(res).catch(err)
}

function nameChange(params, res, err) {
    authapi.put(`/boards/gif/${params[0]}`, params[1])
        .then(res).catch(err)
}


export { getStoreList, createBoard, putBoardData, getUserBoard, getBoard,nameChange }