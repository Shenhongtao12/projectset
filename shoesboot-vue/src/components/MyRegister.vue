<!--
 * @Description: 用户注册组件
 * @Author: Aaron.Shen
 * @Date: 2020-02-19 22:20:35
 * @LastEditors: Aaron.Shen
 * @LastEditTime: 2020-03-01 15:34:34
 -->
<template>
  <div id="register">
    <el-dialog
      title="注册"
      width="25%"
      center
      :visible.sync="isRegister"
      :before-close="handleClose"
    >
      <el-form
        :model="RegisterUser"
        :rules="rules"
        status-icon
        ref="ruleForm"
        class="demo-ruleForm"
      >
        <el-form-item prop="name">
          <el-input
            prefix-icon="el-icon-user-solid"
            placeholder="请输入账号"
            v-model="RegisterUser.name"
          ></el-input>
        </el-form-item>
        <el-form-item prop="pass">
          <el-input
            prefix-icon="el-icon-view"
            type="password"
            placeholder="请输入密码"
            v-model="RegisterUser.pass"
          ></el-input>
        </el-form-item>
        <el-form-item prop="confirmPass">
          <el-input
            prefix-icon="el-icon-view"
            type="password"
            placeholder="请再次输入密码"
            v-model="RegisterUser.confirmPass"
          ></el-input>
        </el-form-item>
        <el-form-item label="是否注册为会员: " prop="vip">
          <el-switch
            v-model="RegisterUser.vip"
            active-color="#13ce66"
            inactive-color="#ff4949"
          >
          </el-switch>
        </el-form-item>
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
          <el-input v-model="RegisterUser.email" autocomplete="off">
            <el-button
              slot="append"
              style="color: white; background-color: #3c8dbc"
              v-show="showTime"
              @click="sendEmail(RegisterUser.email)"
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
        <el-form-item prop="emailCode">
          <el-input
            prefix-icon="el-icon-bangzhu"
            placeholder="请输入验证码"
            v-model="RegisterUser.code"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            size="medium"
            type="primary"
            @click="Register"
            style="width: 100%"
            >注册</el-button
          >
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
import { sendEmailCode, regist } from "@/api/UserService";
export default {
  name: "MyRegister",
  props: ["register"],
  data() {
    // 用户名的校验方法
    let validateName = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("请输入用户名"));
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
    // 确认密码的校验方法
    let validateConfirmPass = (rule, value, callback) => {
      if (value === "") {
        return callback(new Error("请输入确认密码"));
      }
      // 校验是否以密码一致
      if (this.RegisterUser.pass != "" && value === this.RegisterUser.pass) {
        this.$refs.ruleForm.validateField("checkPass");
        return callback();
      } else {
        return callback(new Error("两次输入的密码不一致"));
      }
    };
    /* let validateEmail = (rule, value, callback) => {
      if (value === "") {
        return callback(new Error("请输入邮箱"));
      }
    }; */
    return {
      isRegister: false, // 控制注册组件是否显示
      RegisterUser: {
        name: "",
        pass: "",
        confirmPass: "",
        email: "",
        code: "",
        vip: true,
      },
      showTime: true /* 布尔值，通过v-show控制显示‘获取按钮’还是‘倒计时’ */,
      sendTime: null /* 倒计时 计数器 */,
      timer: null,
      // 用户信息校验规则,validator(校验方法),trigger(触发方式),blur为在组件 Input 失去焦点时触发
      rules: {
        name: [{ validator: validateName, trigger: "blur" }],
        pass: [{ validator: validatePass, trigger: "blur" }],
        confirmPass: [{ validator: validateConfirmPass, trigger: "blur" }],
        // email: [{ validator: validateEmail, trigger: "blur" }],
      },
    };
  },
  watch: {
    // 监听父组件传过来的register变量，设置this.isRegister的值
    register: function (val) {
      if (val) {
        this.isRegister = val;
      }
    },
    // 监听this.isRegister变量的值，更新父组件register变量的值
    isRegister: function (val) {
      if (!val) {
        this.$refs["ruleForm"].resetFields();
        this.$emit("fromChild", val);
      }
    },
  },
  methods: {
    handleClose(done) {
      this.$confirm("确认关闭？")
        .then((_) => {
          done();
        })
        .catch((_) => {});
    },
    sendEmail(email) {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          let request = {
            email: email,
            newUser: 0,
          };
          sendEmailCode(request).then((res) => {
            if (res.code == 200) {
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
    Register() {
      // 通过element自定义表单校验规则，校验用户输入的用户信息
      this.$refs["ruleForm"].validate((valid) => {
        //如果通过校验开始注册
        if (valid) {
          let request = {
            username: this.RegisterUser.name,
            password: this.RegisterUser.pass,
            email: this.RegisterUser.email,
            emailCode: this.RegisterUser.code,
          };
          regist(request)
            .then((res) => {
              // “001”代表注册成功，其他的均为失败
              if (res.code == 200) {
                // 隐藏注册组件
                this.isRegister = false;
                // 弹出通知框提示注册成功信息
                this.notifySucceed(res.msg);
              } else {
                // 弹出通知框提示注册失败信息
                this.notifyError(res.msg);
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
  },
};
</script>
<style></style>
