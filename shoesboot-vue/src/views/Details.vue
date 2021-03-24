<!--
 * @Description: 商品详情页面组件
 * @Author: Aaron.Shen
 * @Date: 2020-02-16 20:20:26
 * @LastEditors: Aaron.Shen
 * @LastEditTime: 2020-03-07 21:59:26
 -->
<template>
  <div id="details">
    <!-- 头部 -->
    <div class="page-header">
      <div class="title">
        <p>{{ productDetails.title }}</p>
        <div class="list">
          <ul>
            <li>
              <router-link to>概述</router-link>
            </li>
            <li>
              <router-link to>参数</router-link>
            </li>
            <li>
              <router-link to>用户评价</router-link>
            </li>
          </ul>
        </div>
      </div>
    </div>
    <!-- 头部END -->

    <!-- 主要内容 -->
    <div class="main">
      <!-- 左侧商品轮播图 -->
      <div class="block">
        <el-carousel height="560px" v-if="productPicture.length > 1">
          <el-carousel-item v-for="item in productPicture" :key="item.id">
            <img style="height: 560px" :src="item" />
          </el-carousel-item>
        </el-carousel>
        <div v-if="productPicture.length == 1">
          <img
            style="height: 560px"
            :src="productPicture[0]"
            :alt="productPicture[0]"
          />
        </div>
      </div>
      <!-- 左侧商品轮播图END -->

      <!-- 右侧内容区 -->
      <div class="content">
        <h1 class="name">{{ productDetails.title }}</h1>
        <p class="intro"></p>
        <p class="store">{{ productDetails.brand }}</p>
        <div class="price">
          <span>{{ productDetails.price }}元</span>
          <!-- <span
                            v-show="
              productDetails.product_price !=
              productDetails.product_selling_price
            "
                            class="del"
                    >{{ productDetails.price }}元</span
                    >-->
        </div>
        <div class="pro-list">
          <span class="pro-name">{{ productDetails.title }}</span>
          <span class="pro-price">
            <span>{{ productDetails.price }}元</span>
            <!--<span
                                v-show="
                            productDetails.product_price !=
                            productDetails.product_selling_price
                          "
                                class="pro-del"
                        >{{ productDetails.price }}元</span>-->
          </span>
          <p class="price-sum">总计 : {{ productDetails.price }}元</p>
        </div>
        <!-- 内容区底部按钮 -->
        <div class="button">
          <el-button class="shop-cart" :disabled="dis" @click="addShoppingCart"
            >加入购物车
          </el-button>
          <el-button class="like" @click="addCollect">收藏</el-button>
        </div>
        <!-- 内容区底部按钮END -->
        <div class="pro-policy">
          <ul>
            <li><i class="el-icon-circle-check"></i> 官方自营</li>
            <li><i class="el-icon-circle-check"></i> 平台发货</li>
            <li><i class="el-icon-circle-check"></i> 7天无理由退货</li>
            <li><i class="el-icon-circle-check"></i> 7天价格保护</li>
          </ul>
        </div>
      </div>
      <!-- 右侧内容区END -->
    </div>
    <!-- 主要内容END -->
  </div>
</template>
<script>
import { mapActions, mapGetters } from "vuex";
import { getOneGoods } from "@/api/GoodsService";
import { putShopCart } from "@/api/ShopCartService";
import { postFavorite } from "@/api/FavoriteService";

export default {
  data() {
    return {
      dis: false, // 控制“加入购物车按钮是否可用”
      productID: "", // 商品id
      productDetails: "", // 商品详细信息
      productPicture: "", // 商品图片
    };
  },
  // 通过路由获取商品id
  activated() {
    if (this.$route.query.productID != undefined) {
      this.productID = this.$route.query.productID;
    }
  },
  watch: {
    // 监听商品id的变化，请求后端获取商品数据
    productID: function (val) {
      this.getDetails(val);
    },
  },
  computed: {
    ...mapGetters(["getShoppingCart"]),
  },
  methods: {
    ...mapActions(["unshiftShoppingCart", "addShoppingCartNum"]),
    // 获取商品详细信息
    getDetails(val) {
      getOneGoods(val)
        .then((res) => {
          if (res.code === 200) {
            this.productDetails = res.data;
            this.productPicture = res.data.images.split(",");
          } else if (res.code === 404) {
            this.notifyError("该商品已下架");
            this.$router.push("/collect");
          } else {
            this.notifyError(res.message);
            this.$router.push("/collect");
          }
        })
        .catch((err) => {
          return Promise.reject(err);
        });
    },
    // 加入购物车
    addShoppingCart() {
      // 判断是否登录,没有登录则显示登录组件
      if (!this.$store.getters.getUser) {
        this.$store.dispatch("setShowLogin", true);
        return;
      }
      let request = {
        id: null,
        goodsId: this.productID,
        amount: 1,
        userId: this.$store.getters.getUser.id,
      };
      if (this.getShoppingCart.length > 0) {
        this.getShoppingCart.forEach((goods) => {
          if (goods.goodsId === this.productID) {
            request.id = this.productID;
            request.amount = goods.amount + 1;
          }
        });
      }
      putShopCart(request)
        .then((res) => {
          if (res.code === 200) {
            if (request.id !== null) {
              // 该商品已经在购物车，数量+1
              this.addShoppingCartNum(this.productID);
            } else {
              // 新加入购物车成功
              let goods = {
                goodsId: this.productDetails.id,
                title: this.productDetails.title,
                image: this.productPicture[0],
                price: this.productDetails.price,

                cartId: res.data.cartId,
                amount: 1,
                inventory: res.data.inventory,
                check: false,
              };
              this.unshiftShoppingCart(goods);
            }
            this.notifySucceed("已成功加入购物车");
          } else {
            // 提示更新失败信息
            this.notifyError(res.message);
          }
        })
        .catch((err) => {
          return Promise.reject(err);
        });
    },
    addCollect() {
      // 判断是否登录,没有登录则显示登录组件
      if (!this.$store.getters.getUser) {
        this.$store.dispatch("setShowLogin", true);
        return;
      }
      console.log(this.$store.getters.getUser);
      postFavorite({
        userId: this.$store.getters.getUser.id,
        goodsId: this.productID,
      })
        .then((res) => {
          if (res.code === 200) {
            // 添加收藏成功
            this.notifySucceed(res.message);
          } else {
            // 添加收藏失败
            this.notifyError(res.message);
          }
        })
        .catch((err) => {
          return Promise.reject(err);
        });
    },
  },
};
</script>
<style>
/* 头部CSS */
#details .page-header {
  height: 64px;
  margin-top: -20px;
  z-index: 4;
  background: #fff;
  border-bottom: 1px solid #e0e0e0;
  -webkit-box-shadow: 0px 5px 5px rgba(0, 0, 0, 0.07);
  box-shadow: 0px 5px 5px rgba(0, 0, 0, 0.07);
}

#details .page-header .title {
  width: 1225px;
  height: 64px;
  line-height: 64px;
  font-size: 18px;
  font-weight: 400;
  color: #212121;
  margin: 0 auto;
}

#details .page-header .title p {
  float: left;
}

#details .page-header .title .list {
  height: 64px;
  float: right;
}

#details .page-header .title .list li {
  float: left;
  margin-left: 20px;
}

#details .page-header .title .list li a {
  font-size: 14px;
  color: #616161;
}

#details .page-header .title .list li a:hover {
  font-size: 14px;
  color: #ff6700;
}

/* 头部CSS END */

/* 主要内容CSS */
#details .main {
  width: 1225px;
  height: 560px;
  padding-top: 30px;
  margin: 0 auto;
}

#details .main .block {
  float: left;
  width: 560px;
  height: 560px;
}

#details .el-carousel .el-carousel__indicator .el-carousel__button {
  background-color: rgba(163, 163, 163, 0.8);
}

#details .main .content {
  float: left;
  margin-left: 25px;
  width: 640px;
}

#details .main .content .name {
  height: 30px;
  line-height: 30px;
  font-size: 24px;
  font-weight: normal;
  color: #212121;
}

#details .main .content .intro {
  color: #b0b0b0;
  padding-top: 10px;
}

#details .main .content .store {
  color: #ff6700;
  padding-top: 10px;
}

#details .main .content .price {
  display: block;
  font-size: 18px;
  color: #ff6700;
  border-bottom: 1px solid #e0e0e0;
  padding: 25px 0 25px;
}

#details .main .content .price .del {
  font-size: 14px;
  margin-left: 10px;
  color: #b0b0b0;
  text-decoration: line-through;
}

#details .main .content .pro-list {
  background: #f9f9fa;
  padding: 30px 60px;
  margin: 50px 0 50px;
}

#details .main .content .pro-list span {
  line-height: 30px;
  color: #616161;
}

#details .main .content .pro-list .pro-price {
  float: right;
}

#details .main .content .pro-list .pro-price .pro-del {
  margin-left: 10px;
  text-decoration: line-through;
}

#details .main .content .pro-list .price-sum {
  color: #ff6700;
  font-size: 24px;
  padding-top: 20px;
}

#details .main .content .button {
  height: 55px;
  margin: 10px 0 20px 0;
}

#details .main .content .button .el-button {
  float: left;
  height: 55px;
  font-size: 16px;
  color: #fff;
  border: none;
  text-align: center;
}

#details .main .content .button .shop-cart {
  width: 340px;
  background-color: #ff6700;
}

#details .main .content .button .shop-cart:hover {
  background-color: #f25807;
}

#details .main .content .button .like {
  width: 260px;
  margin-left: 40px;
  background-color: #b0b0b0;
}

#details .main .content .button .like:hover {
  background-color: #757575;
}

#details .main .content .pro-policy li {
  float: left;
  margin-right: 20px;
  color: #b0b0b0;
}

/* 主要内容CSS END */
</style>
