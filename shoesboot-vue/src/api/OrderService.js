import request from "@/utils/request";

export function getOrderList(data) {
  return request({
    url: "order",
    method: "get",
    params: data,
  });
}

export function createOrder(data) {
  return request({
    url: "order",
    method: "post",
    data: data,
  });
}

//收货
export function delivery(data) {
  return request({
    url: "order",
    method: "put",
    data: data,
  });
}
