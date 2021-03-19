<template>
  <div class="app-container">
    <el-form
      label-width="180px"
      status-icon
      :rules="formRules"
      ref="loginForm"
      :model="formValue"
      @submit.native.prevent
    >
      <el-form-item label="用户名" prop="username">
        <el-input
          name="name"
          :disabled="true"
          v-model="formValue.username"
          style="width: 280px"
        ></el-input>
      </el-form-item>
      <el-form-item label="设置密码" prop="password">
        <el-input
          v-model="formValue.password"
          style="width: 280px"
          type="password"
          :placeholder="passwordPlaceholder"
        ></el-input>
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="formValue.email" style="width: 280px"></el-input>
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="formValue.phone" style="width: 280px"></el-input>
      </el-form-item>
      <el-form-item label="注册时间" prop="inDate">
        <el-input
          v-model="formValue.inDate"
          style="width: 280px"
          :disabled="true"
        ></el-input>
      </el-form-item>
      <el-form-item label="是否为会员">
        <el-switch
          v-model="formValue.vip"
          active-color="#13ce66"
          inactive-color="#ff4949"
        >
        </el-switch>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit()">立即更新</el-button>
        <el-button @click="onSubmit()">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  name: "userInfo",
  created() {
    // 获取浏览器localStorage，判断用户是否已经登录
    // 如果已经登录，设置vuex登录状态
    //this.setUser(JSON.parse(localStorage.getItem("user")));
    //console.log(localStorage.getItem("user"))
    console.log(this.getUser);
    this.formValue = this.getUser;
  },
  computed: {
    ...mapGetters(["getUser"]),
  },
  data() {
    return {
      //表单双向绑定数据
      formValue: {},
      passwordPlaceholder: "",
      status: {},
      //表单提交规则
      formRules: {
        username: [
          { required: true, message: "必填项", trigger: "blur" },
          { min: 5, message: "最少为输入5位数", trigger: "blur" },
        ],
        password: [
          // {required: true, message: '必填项', trigger: 'blur'},
          { min: 6, message: "最少为输入6位数", trigger: "blur" },
        ],
        name: [
          { required: true, message: "必填项", trigger: "blur" },
          { min: 2, message: "最少为输入2位数", trigger: "blur" },
        ],
      },
    };
  },
  methods: {
    //提交表单
    onSubmit() {
      this.$refs.loginForm.validate((valid) => {
        if (!valid) {
          return false;
        }
        regAdmin(this.formValue).then((res) => {
          if (res.code == 200) {
            this.notifySucceed("添加成功");
            //this.$router.push("/admin/user");
          } else {
            this.notifyError(res.message);
          }
        });
      });
    },
  },
};
</script>

<style scoped></style>
