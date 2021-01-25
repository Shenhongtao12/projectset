import request from "@/utils/request";

export function getShopCart(data) {
  return request({
    url: "shopCart",
    method: "get",
    params: data,
  });
}

export function deleteShopCart(data) {
  return request({
    url: "shopCart",
    method: "delete",
    params: data,
  });
}

export function putShopCart(data) {
  return request({
    url: "shopCart",
    method: "put",
    data: data,
  });
}
