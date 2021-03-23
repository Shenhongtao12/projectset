import request from "@/utils/request";

// 登录方法
export function login(data) {
  return request({
    url: "user",
    method: "post",
    params: data,
  });
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: "/getInfo",
    method: "get",
  });
}

// 退出方法
export function logout() {
  return request({
    url: "/logout",
    method: "post",
  });
}

export function update(data) {
  return request({
    url: "/user",
    method: "put",
    data: data,
  });
}

export function updatePass(data) {
  return request({
    url: "/user/updatePas",
    method: "put",
    params: data,
  });
}

// 获取验证码
export function sendEmailCode(data) {
  return request({
    url: "user/emailCode",
    method: "get",
    params: data,
  });
}

export function regist(data) {
  return request({
    url: "user/save",
    method: "post",
    data: data,
  });
}

export function emailLogin(params) {
  return request({
    url: "user/email-login",
    method: "get",
    params: params,
  });
}
