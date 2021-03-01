<!--
 * @Description: 我的订单页面组件
 * @Author: Aaron.Shen
 * @Date: 2020-02-20 17:21:54
 * @LastEditors: Aaron.Shen
 * @LastEditTime: 2020-02-27 13:36:27
 -->
<template>
  <div class="order">
    <!-- 我的订单头部 -->
    <div class="order-header">
      <div class="order-header-content">
        <p>
          <i
            class="el-icon-s-order"
            style="font-size: 30px; color: #ff6700"
          ></i>
          我的订单
        </p>
      </div>
    </div>
    <!-- 我的订单头部END -->

    <!-- 我的订单主要内容 -->
    <div class="order-content" v-if="orders.length > 0">
      <div class="content" v-for="(item, index) in orders" :key="index">
        <ul>
          <!-- 我的订单表头 -->
          <li class="order-info">
            <div class="order-id">订单编号: {{ item.orderNumber }}</div>
            <div class="order-status">
              <span> 订单状态: {{ progressBar(item.status) }} </span>
            </div>
            <div class="order-time">
              订单时间: {{ item.inDate | dateFormat }}
            </div>
          </li>
          <li class="header">
            <div class="pro-img"></div>
            <div class="pro-name">商品名称</div>
            <div class="pro-price">单价</div>
            <div class="pro-num">数量</div>
            <div class="pro-total">小计</div>
          </li>
          <!-- 我的订单表头END -->

          <!-- 订单列表 -->
          <li
            class="product-list"
            v-for="(goods, i) in item.orderGoodsList"
            :key="i"
          >
            <div class="pro-img">
              <router-link
                :to="{
                  path: '/goods/details',
                  query: { productID: goods.goodsId },
                }"
              >
                <img :src="goods.image" />
              </router-link>
            </div>
            <div class="pro-name">
              <router-link
                :to="{
                  path: '/goods/details',
                  query: { productID: goods.goodsId },
                }"
              >
                <el-tooltip
                  class="item"
                  effect="light"
                  :content="goods.title"
                  placement="top"
                >
                  <p class="title">{{ goods.title }}</p>
                </el-tooltip>
              </router-link>
            </div>
            <div class="pro-price">{{ goods.price }}元</div>
            <div class="pro-num">{{ goods.amount }}</div>
            <div class="pro-total pro-total-in">
              {{ goods.price * goods.amount }}元
            </div>
          </li>
        </ul>
        <div class="order-bar">
          <div class="order-bar-left">
            <span class="order-total">
              共
              <span class="order-total-num">{{
                item.orderGoodsList.length
              }}</span>
              件商品
            </span>
          </div>
          <div class="order-bar-right">
            <span>
              <span class="total-price-title">合计：</span>
              <span class="total-price">{{ item.money }}元</span>
            </span>
          </div>
          <!-- 订单列表END -->
        </div>
        <!--快递信息-->
        <div
          class="order-bar"
          v-if="item.status === 'D' || item.status === 'E'"
        >
          <div class="order-bar-left">
            <span class="order-total">
              快递公司:
              <span class="order-total-num">
                {{ item.express }}
              </span>
            </span>
          </div>
          <div
            class="order-bar-right"
            style="margin-left: 5%"
            v-if="item.status === 'D'"
          >
            <el-button
              size="medium"
              type="warning"
              plain
              icon="el-icon-shopping-bag-1"
              @click="closedOrder(index, item.id)"
            >
              确认收货
            </el-button>
          </div>
          <div class="order-bar-right">
            快递单号:
            <span class="total-price-title">{{ item.expressNum }}</span>
          </div>
          <!-- 快递信息END -->
        </div>
      </div>
      <div style="margin-top: -20px"></div>
      <div class="block">
        <el-pagination
          :hide-on-single-page="page.total < 5"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="page.pageNum"
          :page-sizes="[5, 10, 20, 30]"
          :page-size.sync="page.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total.sync="page.total"
        >
        </el-pagination>
      </div>
    </div>

    <!-- 我的订单主要内容END -->

    <!-- 订单为空的时候显示的内容 -->
    <div v-else class="order-empty">
      <div class="empty">
        <h2>您的订单还是空的！</h2>
        <p>快去购物吧！</p>
      </div>
    </div>
    <!-- 订单为空的时候显示的内容END -->
  </div>
</template>
<script>
import { getOrderList, delivery } from "@/api/OrderService";

export default {
  methods: {
    closedOrder(index, id) {
      let request = {
        id: id,
        status: "E",
      };
      delivery(request).then((res) => {
        if (res.code === 200) {
          this.notifySucceed("收货成功");
          this.orders[index].status = "E";
        } else {
          this.notifyError(res.message);
        }
      });
    },
    progressBar(status) {
      let num;
      switch (status) {
        case "A":
          num = "取消";
          break;
        case "B":
          num = "未支付";
          break;
        case "C":
          num = "待发货";
          break;
        case "D":
          num = "待收货";
          break;
        case "E":
          num = "已完成";
          break;
      }
      return num;
    },
    handleSizeChange(val) {
      this.page.pageSize = val;
      this.page.pageNum = 1;
      this.getOrderList();
    },
    handleCurrentChange(val) {
      this.page.pageNum = val;
      this.getOrderList();
    },
    getOrderList() {
      // 获取订单数据
      let request = {
        userId: this.$store.getters.getUser.id,
        page: this.page.pageNum,
        rows: this.page.pageSize,
      };
      getOrderList(request)
        .then((res) => {
          if (res.code === 200) {
            this.orders = res.data.data;
            this.page.total = res.data.total;
          } else {
            this.notifyError(res.message);
          }
        })
        .catch((err) => {
          return Promise.reject(err);
        });
    },
  },
  data() {
    return {
      orders: [], // 订单列表
      total: [], // 每个订单的商品数量及总价列表
      page: {
        total: 0,
        pageNum: 1,
        pageSize: 5,
      },
    };
  },
  created() {
    this.getOrderList();
  },
  activated() {
    this.getOrderList();
  },

  watch: {
    // 通过订单信息，计算出每个订单的商品数量及总价
    orders: function (val) {
      let total = [];
      for (let i = 0; i < val.length; i++) {
        const element = val[i];

        let totalNum = 0;
        let totalPrice = 0;
        for (let j = 0; j < element.length; j++) {
          const temp = element[j];
          totalNum += temp.product_num;
          totalPrice += temp.product_price * temp.product_num;
        }
        total.push({ totalNum, totalPrice });
      }
      this.total = total;
    },
  },
};
</script>
<style scoped>
.order {
  background-color: #f5f5f5;
  padding-bottom: 20px;
}

/* 我的订单头部CSS */
.order .order-header {
  height: 64px;
  border-bottom: 2px solid #ff6700;
  background-color: #fff;
  margin-bottom: 20px;
}

.order .order-header .order-header-content {
  width: 1225px;
  margin: 0 auto;
}

.order .order-header p {
  font-size: 28px;
  line-height: 58px;
  float: left;
  font-weight: normal;
  color: #424242;
}

/* 我的订单头部CSS END */
.order .content {
  width: 1225px;
  margin: 0 auto;
  background-color: #fff;
  margin-bottom: 50px;
}

.order .content ul {
  background-color: #fff;
  color: #424242;
  line-height: 85px;
}

/* 我的订单表头CSS */
.order .content ul .order-info {
  height: 60px;
  line-height: 60px;
  padding: 0 26px;
  color: #424242;
  border-bottom: 1px solid #ff6700;
}

.order .content ul .order-info .order-id {
  float: left;
  color: #ff6700;
}

.order .content ul .order-info .order-status {
  float: left;
  margin-left: 30%;
  color: #424242;
}
.order .content ul .order-info .order-status span {
  color: #ff6700;
}

.order .content ul .order-info .order-time {
  float: right;
}

.order .content ul .header {
  height: 85px;
  padding-right: 26px;
  color: #424242;
}

/* 我的订单表头CSS END */

/* 订单列表CSS */
.order .content ul .product-list {
  height: 85px;
  padding: 15px 26px 15px 0;
  border-top: 1px solid #e0e0e0;
}

.order .content ul .pro-img {
  float: left;
  height: 85px;
  width: 120px;
  padding-left: 80px;
}

.order .content ul .pro-img img {
  height: 80px;
  width: 80px;
}

.order .content ul .pro-name {
  float: left;
  width: 380px;
}

.order .content ul .pro-name a {
  color: #424242;
}

.order .content ul .pro-name a:hover {
  color: #ff6700;
}

.order .content ul .pro-price {
  float: left;
  width: 160px;
  padding-right: 18px;
  text-align: center;
}

.order .content ul .pro-num {
  float: left;
  width: 190px;
  text-align: center;
}

.order .content ul .pro-total {
  float: left;
  width: 160px;
  padding-right: 81px;
  text-align: right;
}

.order .content ul .pro-total-in {
  color: #ff6700;
}

.order .order-bar {
  width: 1185px;
  padding: 0 20px;
  border-top: 1px solid #ff6700;
  height: 50px;
  line-height: 50px;
  background-color: #fff;
}

.order .order-bar .order-bar-left {
  float: left;
}

.order .order-bar .order-bar-left .order-total {
  color: #757575;
}

.order .order-bar .order-bar-left .order-total-num {
  color: #ff6700;
}

.order .order-bar .order-bar-right {
  color: #757575;
  float: right;
}

.order .order-bar .order-bar-right .total-price-title {
  color: #ff6700;
  font-size: 14px;
}

.order .order-bar .order-bar-right .total-price {
  color: #ff6700;
  font-size: 30px;
}

/* 订单列表CSS END */

/* 订单为空的时候显示的内容CSS */
.order .order-empty {
  width: 1225px;
  margin: 0 auto;
}

.order .order-empty .empty {
  height: 300px;
  padding: 0 0 130px 558px;
  margin: 65px 0 0;
  background: url(../assets/imgs/cart-empty.png) no-repeat 124px 0;
  color: #b0b0b0;
  overflow: hidden;
}

.order .order-empty .empty h2 {
  margin: 70px 0 15px;
  font-size: 36px;
}

.order .order-empty .empty p {
  margin: 0 0 20px;
  font-size: 20px;
}

.title {
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
}

.block {
  height: 50px;
  text-align: center;
}

/* 订单为空的时候显示的内容CSS END */
</style>
