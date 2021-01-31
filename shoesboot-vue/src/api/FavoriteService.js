import request from "@/utils/request";

export function postFavorite(data) {
  return request({
    url: "favorite",
    method: "post",
    data: data,
  });
}
