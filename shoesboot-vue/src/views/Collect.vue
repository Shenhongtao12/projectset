<!--
 * @Description: 我的收藏页面组件
 * @Author: Aaron.Shen
 * @Date: 2020-02-20 17:22:56
 * @LastEditors: Aaron.Shen
 * @LastEditTime: 2020-03-12 19:34:00
 -->
<template>
  <div class="collect">
    <!-- Add a static page for my favorite module -->
    <div class="collect-header">
      <div class="collect-title">
        <i class="el-icon-collection-tag" style="color: #ff6700"></i>
        我的收藏
      </div>
    </div>
    <div class="content">
      <div class="goods-list" v-if="collectList.length > 0">
        <div>
          <MyList :list="collectList" :isDelete="true"></MyList>
        </div>
      </div>
      <!-- 收藏列表为空的时候显示的内容 -->
      <div v-else class="collect-empty">
        <div class="empty">
          <h2>您的收藏还是空的！</h2>
          <p>快去购物吧！</p>
        </div>
      </div>
      <!--  收藏列表为空的时候显示的内容END -->
    </div>
    <div class="block">
      <el-pagination
        :hide-on-single-page="page.total < 10"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="page.pageNum"
        :page-sizes="[10, 20, 30, 50]"
        :page-size.sync="page.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total.sync="page.total"
      >
      </el-pagination>
    </div>
  </div>
</template>
<script>
import { getFavorite } from "@/api/FavoriteService";

export default {
  created() {
    this.getFavorite();
  },
  activated() {
    this.getFavorite();
  },
  methods: {
    getFavorite() {
      // 获取收藏数据
      getFavorite({
        page: this.page.pageNum,
        size: this.page.pageSize,
      })
        .then((res) => {
          if (res.code === 200) {
            this.collectList = res.data.data;
            this.page.total = res.data.total;
          }
        })
        .catch((err) => {
          return Promise.reject(err);
        });
    },
    handleSizeChange(val) {
      this.page.pageSize = val;
      this.page.pageNum = 1;
      this.getFavorite();
    },
    handleCurrentChange(val) {
      this.page.pageNum = val;
      this.getFavorite();
    },
  },
  data() {
    return {
      collectList: [],
      page: {
        total: 0,
        pageNum: 1,
        pageSize: 10,
      },
    };
  },
};
</script>
<style>
.collect {
  background-color: #f5f5f5;
}

.collect .collect-header {
  height: 64px;
  background-color: #fff;
  border-bottom: 2px solid #ff6700;
}

.collect .collect-header .collect-title {
  width: 1225px;
  margin: 0 auto;
  height: 64px;
  line-height: 58px;
  font-size: 28px;
}

.collect .content {
  padding: 20px 0;
  width: 1225px;
  margin: 0 auto;
}

.collect .content .goods-list {
  margin-left: -13.7px;
  overflow: hidden;
}

/* 收藏列表为空的时候显示的内容CSS */
.collect .collect-empty {
  width: 1225px;
  margin: 0 auto;
}

.collect .collect-empty .empty {
  height: 300px;
  padding: 0 0 130px 558px;
  margin: 65px 0 0;
  background: url(../assets/imgs/cart-empty.png) no-repeat 124px 0;
  color: #b0b0b0;
  overflow: hidden;
}

.collect .collect-empty .empty h2 {
  margin: 70px 0 15px;
  font-size: 36px;
}

.collect .collect-empty .empty p {
  margin: 0 0 20px;
  font-size: 20px;
}

.collect .block {
  height: 50px;
  text-align: center;
}

/* 收藏列表为空的时候显示的内容CSS END */
</style>
