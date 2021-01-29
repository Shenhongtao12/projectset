import request from "@/utils/request";

export function getGoods(data) {
  return request({
    url: "goods",
    method: "get",
    params: data,
  });
}

//轮播图数据
export function getCarousel() {
  return request({
    url: "carousel",
    method: "get",
  });
}

//特价商品
export function getCheapGoods(data) {
  return request({
    url: "goods/cheap-goods",
    method: "get",
    params: data,
  });
}
