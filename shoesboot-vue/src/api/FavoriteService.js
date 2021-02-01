import request from "@/utils/request";

export function postFavorite(data) {
  return request({
    url: "favorite",
    method: "post",
    data: data,
  });
}

export function getFavorite(data) {
  return request({
    url: "favorite",
    method: "get",
    params: data,
  });
}

export function deleteFavorite(data) {
  return request({
    url: "favorite",
    method: "delete",
    params: data,
  });
}
