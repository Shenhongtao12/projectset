<!--
 * @Description: 全部商品页面组件(包括全部商品,商品分类,商品搜索)
 * @Author: Aaron.Shen
 * @Date: 2020-02-07 16:23:00
 * @LastEditors: Aaron.Shen
 * @LastEditTime: 2020-03-08 12:11:13
 -->
<template>
  <div class="goods" id="goods" name="goods">
    <!-- 面包屑 -->
    <div class="breadcrumb">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item to="/">首页</el-breadcrumb-item>
        <el-breadcrumb-item>全部商品</el-breadcrumb-item>
        <el-breadcrumb-item v-if="search">搜索</el-breadcrumb-item>
        <el-breadcrumb-item v-else>分类</el-breadcrumb-item>
        <el-breadcrumb-item v-if="search">{{ search }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <!-- 面包屑END -->

    <!-- 分类标签 -->
    <div class="nav">
      <div class="product-nav">
        <div class="title">分类</div>
        <el-tabs v-model="activeName" type="card">
          <el-tab-pane
            v-for="item in categoryList"
            :key="item.id"
            :label="item.name"
            :name="item.name"
          />
        </el-tabs>
      </div>
    </div>
    <!-- 分类标签END -->

    <!-- 主要内容区 -->
    <div class="main">
      <div class="list">
        <MyList :list="product" v-if="product.length > 0"></MyList>
        <div v-else class="none-product">
          抱歉没有找到相关的商品，请看看其他的商品
        </div>
      </div>
      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          background
          @size-change="handleSizeChange"
          @current-change="currentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 30, 50]"
          :page-size.sync="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total.sync="total"
        ></el-pagination>
      </div>
      <!-- 分页END -->
    </div>
    <!-- 主要内容区END -->
  </div>
</template>
<script>
import { getClassify, getGoods, getCheapGoods } from "@/api/GoodsService";

export default {
  data() {
    return {
      categoryList: "", //分类列表
      categoryName: null, // 分类
      product: "", // 商品列表
      productList: "",
      total: 0, // 商品总量
      pageSize: 20, // 每页显示的商品数量
      currentPage: 1, //当前页码
      activeName: "", // 分类列表当前选中的id
      search: "", // 搜索条件
    };
  },
  created() {
    // 获取分类列表
    this.getCategory();
  },
  activated() {
    this.activeName = ""; // 初始化分类列表当前选中的id为-1
    this.total = 0; // 初始化商品总量为0
    this.currentPage = 1; //初始化当前页码为1
    // 如果路由没有传递参数，默认为显示全部商品
    if (Object.keys(this.$route.query).length === 0) {
      this.categoryName = null;
      this.activeName = "";
      return;
    }
    // 如果路由传递了categoryID，则显示对应的分类商品
    if (this.$route.query.categoryName !== undefined) {
      this.categoryName = this.$route.query.categoryName;
      if (this.categoryName.length >= 1) {
        this.activeName = this.categoryName;
      }
      return;
    }
    // 如果路由传递了search，则为搜索，显示对应的分类商品
    if (this.$route.query.search !== undefined) {
      this.search = this.$route.query.search;
    }
    this.backtop();
  },
  watch: {
    // 监听点击了哪个分类标签，通过修改分类id，响应相应的商品
    activeName: function (val) {
      if (val === "全部") {
        this.categoryName = null;
      } else {
        this.categoryName = val;
        // 更新地址栏链接，方便刷新页面可以回到原来的页面
        this.$router.push({
          path: "/goods",
          query: { categoryId: this.categoryName },
        });
      }
      // 初始化商品总量和当前页码
      this.total = 0;
      this.currentPage = 1;
    },
    // 监听搜索条件，响应相应的商品
    search: function (val) {
      if (val != "") {
        this.search = val;
        this.getData(this.search);
      }
    },
    // 监听分类id，响应相应的商品
    categoryName: function () {
      this.search = "";
      if (this.categoryName === "限时抢购") {
        this.getCheapGoodsList();
      } else {
        this.getData(this.categoryName);
      }
    },
    // 监听路由变化，更新路由传递了搜索条件
    $route: function (val) {
      if (val.path == "/goods") {
        if (val.query.search != undefined) {
          this.activeName = null;
          this.currentPage = 1;
          this.total = 0;
          this.search = val.query.search;
        }
      }
    },
  },
  methods: {
    // 返回顶部
    backtop() {
      const timer = setInterval(function () {
        const top =
          document.documentElement.scrollTop || document.body.scrollTop;
        const speed = Math.floor(-top / 5);
        document.documentElement.scrollTop = document.body.scrollTop =
          top + speed;

        if (top === 0) {
          clearInterval(timer);
        }
      }, 20);
    },
    // 页码变化调用currentChange方法
    currentChange(currentPage) {
      this.currentPage = currentPage;
      if (this.search !== "") {
        this.getData(this.search);
      } else {
        this.getData(this.categoryName);
      }
      this.backtop();
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.currentPage = 1;
      if (this.search !== "") {
        this.getData(this.search);
      } else {
        this.getData(this.categoryName);
      }
      this.backtop();
    },
    // 向后端请求分类列表数据
    getCategory() {
      getClassify({
        page: 1,
        size: 15,
      })
        .then((res) => {
          const val = {
            id: 0,
            name: "全部",
          };
          const cate = res.data.data;
          cate.unshift(val);
          this.categoryList = cate;
        })
        .catch((err) => {
          return Promise.reject(err);
        });
    },
    // 向后端请求全部商品或分类商品数据
    getData(keyword) {
      // 如果分类列表为空则请求全部商品数据，否则请求分类商品数据
      let request = {
        keyword: keyword,
        page: this.currentPage,
        size: this.pageSize,
      };
      getGoods(request)
        .then((res) => {
          this.product = res.data.data;
          this.total = res.data.total;
        })
        .catch((err) => {
          return Promise.reject(err);
        });
    },
    //限时抢购
    getCheapGoodsList() {
      let request = {
        status: true,
        page: this.currentPage,
        size: this.pageSize,
      };
      getCheapGoods(request)
        .then((res) => {
          this.product = res.data.data;
          this.total = res.data.total;
        })
        .catch((err) => {
          return Promise.reject(err);
        });
    },
  },
};
</script>

<style scoped>
.goods {
  background-color: #f5f5f5;
}

/* 面包屑CSS */
.el-tabs--card .el-tabs__header {
  border-bottom: none;
}

.goods .breadcrumb {
  height: 50px;
  background-color: white;
}

.goods .breadcrumb .el-breadcrumb {
  width: 1225px;
  line-height: 30px;
  font-size: 16px;
  margin: 0 auto;
}

/* 面包屑CSS END */

/* 分类标签CSS */
.goods .nav {
  background-color: white;
}

.goods .nav .product-nav {
  width: 1225px;
  height: 40px;
  line-height: 40px;
  margin: 0 auto;
}

.nav .product-nav .title {
  width: 50px;
  font-size: 16px;
  font-weight: 700;
  float: left;
}

/* 分类标签CSS END */

/* 主要内容区CSS */
.goods .main {
  margin: 0 auto;
  max-width: 1225px;
}

.goods .main .list {
  min-height: 650px;
  padding-top: 14.5px;
  margin-left: -13.7px;
  overflow: auto;
}

.goods .main .pagination {
  height: 50px;
  text-align: center;
}

.goods .main .none-product {
  color: #333;
  margin-left: 13.7px;
}

/* 主要内容区CSS END */
</style>
