import request from "@/utils/request";

export function getGoods(data) {
  return request({
    url: "goods",
    method: "get",
    params: data,
  });
}
