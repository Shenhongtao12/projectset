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

export function getOneGoods(data) {
  return request({
    url: "goods/" + data,
    method: "get",
  });
}

//分类
export function getClassify(data) {
  return request({
    url: "classify",
    method: "get",
    params: data,
  });
}
