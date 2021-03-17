<!--
 * @Description: 登录组件
 * @Author: Aaron.Shen
 * @Date: 2020-02-19 20:55:17
 * @LastEditors: Aaron.Shen
 * @LastEditTime: 2020-03-01 15:34:08
 -->
<template>
  <div id="myLogin">
    <el-dialog
      title="登录"
      width="25%"
      center
      :visible.sync="isLogin"
      :before-close="handleClose"
    >
      <el-form
        :model="LoginUser"
        :rules="rules"
        status-icon
        ref="ruleForm"
        class="demo-ruleForm"
        v-if="!flagEmailLogin"
      >
        <el-form-item prop="name">
          <el-input
            prefix-icon="el-icon-user-solid"
            placeholder="请输入用户名或邮箱"
            v-model="LoginUser.name"
          ></el-input>
        </el-form-item>
        <el-form-item prop="pass">
          <el-input
            prefix-icon="el-icon-view"
            type="password"
            placeholder="请输入密码"
            v-model="LoginUser.pass"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            size="medium"
            type="primary"
            @click="Login"
            style="width: 100%"
            >登录</el-button
          >
        </el-form-item>
      </el-form>
      <el-form
        :model="sendEmailForm"
        status-icon
        :rules="sendEmailForm"
        ref="sendEmailForm"
        class="demo-ruleForm"
        v-if="flagEmailLogin"
      >
        <el-form-item
          label="邮箱"
          prop="email"
          :rules="[
            { required: true, message: '请输入邮箱地址', trigger: 'blur' },
            {
              type: 'email',
              message: '请输入正确的邮箱地址',
              trigger: ['blur', 'change'],
            },
          ]"
        >
          <el-input v-model="sendEmailForm.email" clearable autocomplete="off">
            <el-button
              slot="append"
              style="color: white; background-color: #3c8dbc"
              v-show="showTime"
              @click="sendEmail(sendEmailForm.email)"
              >发送验证码</el-button
            >
            <el-button
              slot="append"
              style="
                color: white;
                background-color: #3c8dbc;
                margin-left: -20px;
              "
              v-show="!showTime"
              >{{ sendTime }}秒</el-button
            >
          </el-input>
        </el-form-item>
      </el-form>
      <el-form
        :model="emailLoginForm"
        status-icon
        :rules="emailLoginForm"
        ref="emailLoginForm"
        class="demo-ruleForm"
        v-if="flagEmailLogin"
      >
        <el-form-item
          prop="code"
          label="验证码"
          :rules="[{ required: true, message: '验证码不能为空' }]"
        >
          <el-input
            prefix-icon="el-icon-bangzhu"
            placeholder="请输入验证码"
            v-model="emailLoginForm.code"
            maxlength="6"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            size="medium"
            type="primary"
            @click="loginForEmail()"
            style="width: 100%"
            >登录</el-button
          >
        </el-form-item>
      </el-form>
      <el-button plain size="mini" @click="emailLogin()" v-if="!flagEmailLogin"
        >邮箱验证码登录</el-button
      >
      <el-button plain size="mini" @click="emailLogin()" v-if="flagEmailLogin"
        >账号密码登录</el-button
      >
    </el-dialog>
  </div>
</template>
<script>
import { mapActions } from "vuex";
import { login, sendEmailCode, emailLogin } from "@/api/UserService";
export default {
  name: "MyLogin",
  data() {
    // 用户名的校验方法
    let validateName = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("请输入用户名或邮箱"));
      }
      // 用户名以字母开头,长度在5-16之间,允许字母数字下划线
      const userNameRule = /^[a-zA-Z][a-zA-Z0-9_]{4,15}$/;
      if (userNameRule.test(value)) {
        this.$refs.ruleForm.validateField("checkPass");
        return callback();
      } else {
        return callback(new Error("字母开头,长度5-16之间,允许字母数字下划线"));
      }
    };
    // 密码的校验方法
    let validatePass = (rule, value, callback) => {
      if (value === "") {
        return callback(new Error("请输入密码"));
      }
      // 密码以字母开头,长度在6-18之间,允许字母数字和下划线
      const passwordRule = /^[a-zA-Z]\w{5,17}$/;
      if (passwordRule.test(value)) {
        this.$refs.ruleForm.validateField("checkPass");
        return callback();
      } else {
        return callback(
          new Error("字母开头,长度6-18之间,允许字母数字和下划线")
        );
      }
    };
    return {
      flagEmailLogin: false,
      sendEmailForm: {
        email: "shenhongtao12@aliyun.com",
      },
      emailLoginForm: {
        code: "",
      },
      showTime: true /* 布尔值，通过v-show控制显示‘获取按钮’还是‘倒计时’ */,
      sendTime: null /* 倒计时 计数器 */,
      timer: null,
      LoginUser: {
        name: "",
        pass: "",
      },
      // 用户信息校验规则,validator(校验方法),trigger(触发方式),blur为在组件 Input 失去焦点时触发
      rules: {
        name: [{ validator: validateName, trigger: "blur" }],
        pass: [{ validator: validatePass, trigger: "blur" }],
      },
    };
  },
  computed: {
    // 获取vuex中的showLogin，控制登录组件是否显示
    isLogin: {
      get() {
        return this.$store.getters.getShowLogin;
      },
      set(val) {
        //this.$refs["ruleForm"].resetFields();
        this.setShowLogin(val);
      },
    },
  },
  methods: {
    ...mapActions(["setUser", "setShowLogin"]),
    loginForEmail() {
      if (!this.send) {
        this.notifyError("请先发送验证码");
      } else {
        this.$refs["emailLoginForm"].validate((valid) => {
          if (valid) {
            let request = {
              email: this.sendEmailForm.email,
              code: this.emailLoginForm.code,
            };
            emailLogin(request).then((res) => {
              if (res.code === 200) {
                // 隐藏登录组件
                this.isLogin = false;
                // 登录信息存到本地
                let user = JSON.stringify(res.data.user);
                localStorage.setItem("user", user);
                localStorage.setItem("User-Token", res.data.token);
                // 登录信息存到vuex
                this.setUser(res.data.user);
                // 弹出通知框提示登录成功信息
                this.notifySucceed("登录成功");
              } else {
                // 弹出通知框提示登录失败信息
                this.notifyError(res.message);
              }
            });
          }
        });
      }
    },
    emailLogin() {
      this.flagEmailLogin = !this.flagEmailLogin;
    },
    sendEmail(email) {
      this.$refs["sendEmailForm"].validate((valid) => {
        if (email == null) {
          this.notifyError("请输入邮箱");
        }
        if (valid) {
          let request = {
            email: email,
            newUser: 1,
          };
          sendEmailCode(request).then((res) => {
            if (res.code == 200) {
              this.send = true;
              const TIME_COUNT = 90; //  更改倒计时时间
              this.notifySucceed(res.message);
              if (!this.timer) {
                this.sendTime = TIME_COUNT;
                this.showTime = false;
                this.timer = setInterval(() => {
                  if (this.sendTime > 0 && this.sendTime <= TIME_COUNT) {
                    this.sendTime--;
                  } else {
                    this.showTime = true;
                    clearInterval(this.timer); // 清除定时器
                    this.timer = null;
                  }
                }, 1000);
              }
            } else {
              this.notifyError(res.message);
            }
          });
        }
      });
    },
    Login() {
      // 通过element自定义表单校验规则，校验用户输入的用户信息
      this.$refs["ruleForm"].validate((valid) => {
        //如果通过校验开始登录
        if (valid) {
          let request = {
            username: this.LoginUser.name,
            password: this.LoginUser.pass,
          };
          login(request)
            .then((res) => {
              if (res.code === 200) {
                // 隐藏登录组件
                this.isLogin = false;
                // 登录信息存到本地
                let user = JSON.stringify(res.data.user);
                localStorage.setItem("user", user);
                localStorage.setItem("User-Token", res.data.token);
                // 登录信息存到vuex
                this.setUser(res.data.user);
                // 弹出通知框提示登录成功信息
                this.notifySucceed("登录成功");
              } else {
                // 清空输入框的校验状态
                this.$refs["ruleForm"].resetFields();
                // 弹出通知框提示登录失败信息
                this.notifyError(res.message);
              }
            })
            .catch((err) => {
              return Promise.reject(err);
            });
        } else {
          return false;
        }
      });
    },
    handleClose(done) {
      this.$confirm("确认关闭？")
        .then((_) => {
          done();
        })
        .catch((_) => {});
    },
  },
};
</script>
<style></style>
