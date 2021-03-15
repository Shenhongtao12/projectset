<!--
 * @Description: 首页组件
 * @Author: Aaron.Shen
 * @Date: 2020-02-07 16:23:00
 * @LastEditors: Aaron.Shen
 * @LastEditTime: 2020-02-27 13:36:12
 -->
<template>
  <div class="home" id="home" name="home">
    <!-- 轮播图 -->
    <div class="block">
      <el-carousel height="460px">
        <el-carousel-item v-for="item in carousel" :key="item.id">
          <img style="height: 460px" :src="item.url" :alt="item.title" />
        </el-carousel-item>
      </el-carousel>
    </div>
    <!-- 轮播图END -->
    <div class="main-box">
      <div class="main">
        <!-- 手机商品展示区域 -->
        <div class="phone">
          <div class="box-hd">
            <div class="title">限时抢购</div>
          </div>
          <div class="box-bd">
            <div class="promo-list">
              <router-link to>
                <img
                  :src="'https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/cb1bd61ad71c45a4f67f09b075463944.jpeg?thumb=1&w=293&h=768&f=webp&q=90'"
                />
              </router-link>
            </div>
            <div class="list">
              <MyList :list="cheapGoodsList" :isMore="false"></MyList>
            </div>
          </div>
        </div>
        <!-- 手机商品展示区域END -->

        <!-- 家电商品展示区域 -->
        <div class="appliance" id="promo-menu">
          <div class="box-hd">
            <div class="title">Nike</div>
            <div class="more" id="more">
              <MyMenu :val="2" @fromChild="getChildMsg">
                <span slot="1">热门</span>
                <span slot="2">Air Jordan</span>
              </MyMenu>
            </div>
          </div>
          <div class="box-bd">
            <div class="promo-list">
              <ul>
                <li>
                  <img
                    :src="'https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/116fc43816b87192be4e67cf762e8da5.jpeg?thumb=1&w=293&h=375&f=webp&q=90'"
                  />
                </li>
                <li>
                  <img
                    :src="'https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/7e03c0fe4af4d613603f22aaa8e0ea00.jpg?thumb=1&w=293&h=375&f=webp&q=90'"
                  />
                </li>
              </ul>
            </div>
            <div class="list">
              <MyList :list="applianceList" :isMore="true"></MyList>
            </div>
          </div>
        </div>
        <!-- 家电商品展示区域END -->

        <!-- 配件商品展示区域 -->
        <div class="accessory" id="promo-menu">
          <div class="box-hd">
            <div class="title">Adidas</div>
            <div class="more" id="more">
              <MyMenu :val="3" @fromChild="getChildMsg2">
                <span slot="1">热门</span>
                <span slot="2">Yeezy</span>
                <span slot="3">三叶草</span>
              </MyMenu>
            </div>
          </div>
          <div class="box-bd">
            <div class="promo-list">
              <ul>
                <li>
                  <img
                    :src="'https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/ffe114f73fac3a45e5622c3eff56106b.jpg?thumb=1&w=293&h=375&f=webp&q=90'"
                    alt
                  />
                </li>
                <li>
                  <img
                    :src="'https://cdn.cnbj1.fds.api.mi-img.com/mi-mall/a660ce095e8f553a9ed1515265f4e9fc.jpg?thumb=1&w=293&h=375&f=webp&q=90'"
                    alt
                  />
                </li>
              </ul>
            </div>
            <div class="list">
              <MyList
                :list="accessoryList"
                :isMore="true"
                :category-name="categoryName"
              ></MyList>
            </div>
          </div>
        </div>
        <!-- 配件商品展示区域END -->
      </div>
    </div>
  </div>
</template>
<script>
import { getGoods, getCarousel, getCheapGoods } from "@/api/GoodsService";

export default {
  data() {
    return {
      carousel: "", // 轮播图数据
      cheapGoodsList: "", // 特价商品列表
      miTvList: "", // 小米电视商品列表
      applianceList: "", // 家电商品列表
      applianceHotList: "", //热门家电商品列表
      accessoryList: "", //配件商品列表
      accessoryHotList: "", //热门配件商品列表
      protectingShellList: "", // 保护套商品列表
      chargerList: "", //充电器商品列表
      applianceActive: 1, // 家电当前选中的商品分类
      accessoryActive: 1, // 配件当前选中的商品分类
      categoryName: "",
    };
  },
  watch: {
    // 家电当前选中的商品分类，响应不同的商品数据
    applianceActive: function (val) {
      // 页面初始化的时候把applianceHotList(热门家电商品列表)直接赋值给applianceList(家电商品列表)
      // 所以在切换商品列表时判断applianceHotList是否为空,为空则是第一次切换,把applianceList赋值给applianceHotList
      if (this.applianceHotList == "") {
        this.applianceHotList = this.applianceList;
      }
      if (val == 1) {
        this.categoryName = "NIKE";
        this.applianceList = this.applianceHotList;
        return;
      }
      if (val == 2) {
        this.categoryName = "Air%20Jordan";
        this.applianceList = this.miTvList;
        return;
      }
    },
    accessoryActive: function (val) {
      // 获取轮播图数据
      getCarousel()
        .then((res) => {
          this.carousel = res.data;
          console.log(this.carousel);
        })
        .catch((err) => {
          return Promise.reject(err);
        });
      // 页面初始化的时候把accessoryHotList(热门配件商品列表)直接赋值给accessoryList(配件商品列表)
      // 所以在切换商品列表时判断accessoryHotList是否为空,为空则是第一次切换,把accessoryList赋值给accessoryHotList
      if (this.accessoryHotList == "") {
        this.accessoryHotList = this.accessoryList;
      }
      if (val == 1) {
        // 1为热门商品
        this.categoryName = "adidas";
        this.accessoryList = this.accessoryHotList;
        return;
      }
      if (val == 2) {
        this.categoryName = "Yeezy";
        this.accessoryList = this.protectingShellList;
        return;
      }
      if (val == 3) {
        this.categoryName = "三叶草";
        this.accessoryList = this.chargerList;
        return;
      }
    },
  },
  created() {
    // 获取轮播图数据
    getCarousel()
      .then((res) => {
        this.carousel = res.data;
      })
      .catch((err) => {
        return Promise.reject(err);
      });
    // 获取各类商品数据
    this.getCheapGoodsList(); //特价
    this.getPromo("Air Jordan", "miTvList");
    this.getPromo("Yeezy", "protectingShellList");
    this.getPromo("三叶草", "chargerList");
    this.getPromo("NIKE休闲鞋", "applianceList");
    this.getPromo("adidas", "accessoryList");
  },
  methods: {
    // 获取家电模块子组件传过来的数据
    getChildMsg(val) {
      this.applianceActive = val;
    },
    // 获取配件模块子组件传过来的数据
    getChildMsg2(val) {
      this.accessoryActive = val;
    },
    // 获取各类商品数据方法封装
    getPromo(categoryName, val) {
      let request = {
        keyword: categoryName,
        page: 1,
        size: 7,
      };
      getGoods(request)
        .then((res) => {
          this[val] = res.data.data;
        })
        .catch((err) => {
          return Promise.reject(err);
        });
    },

    getCheapGoodsList() {
      let request = {
        status: true,
        page: 1,
        rows: 8,
      };
      getCheapGoods(request)
        .then((res) => {
          this.cheapGoodsList = res.data.data;
        })
        .catch((err) => {
          return Promise.reject(err);
        });
    },
  },
};
</script>
<style scoped>
@import "../assets/css/index.css";
</style>
