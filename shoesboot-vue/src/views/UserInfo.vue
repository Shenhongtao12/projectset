<template>
  <div class="app-container" style="display: flex">
    <div>
      <el-form
        label-width="250px"
        status-icon
        :rules="formRules"
        ref="loginForm"
        :model="formValue"
        @submit.native.prevent
        v-if="!flag"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
            name="name"
            :disabled="true"
            v-model="formValue.username"
            style="width: 280px"
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
          <el-button type="primary" @click="changePassword()"
            >修改密码</el-button
          >
        </el-form-item>
      </el-form>

      <el-form
        :model="ruleForm"
        status-icon
        :rules="rules"
        ref="ruleForm"
        label-width="250px"
        class="demo-ruleForm"
        v-if="flag"
      >
        <el-form-item label="原密码" prop="oldPassword">
          <el-input
            type="password"
            v-model="ruleForm.oldPassword"
            autocomplete="off"
            style="width: 280px"
            :placeholder="passwordPlaceholder"
          ></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="password">
          <el-input
            type="password"
            v-model="ruleForm.password"
            autocomplete="off"
            :placeholder="passwordPlaceholder"
            style="width: 280px"
          ></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPass">
          <el-input
            type="password"
            v-model="ruleForm.checkPass"
            autocomplete="off"
            style="width: 280px"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')"
            >立即修改</el-button
          >
          <el-button @click="changePassword()">返回</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div style="width: 200px"></div>
    <div>
      <el-upload
        class="avatar-uploader"
        action="http://47.98.128.88:8080/api/upload/"
        :show-file-list="false"
        :on-success="handleAvatarSuccess"
        :before-upload="beforeAvatarUpload"
      >
        <img v-if="formValue.url" :src="formValue.url" class="avatar" />
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { update, updatePass } from "@/api/UserService";

export default {
  name: "userInfo",
  created() {
    // 获取浏览器localStorage，判断用户是否已经登录
    // 如果已经登录，设置vuex登录状态
    //this.setUser(JSON.parse(localStorage.getItem("user")));
    //console.log(localStorage.getItem("user"))
    this.info = JSON.parse(JSON.stringify(this.getUser));
    console.log(this.info);
    this.info.inDate = this.$moment(this.info.inDate).format(
      "YYYY-MM-DD HH:mm:ss"
    );
    this.formValue = this.info;
  },
  computed: {
    ...mapGetters(["getUser"]),
  },
  data() {
    var validateOldPass = (rule, value, callback) => {
      if (value == null || value === "") {
        callback(new Error("请输入原密码"));
      }
      setTimeout(() => {
        callback();
      }, 1000);
    };
    var validatePass = (rule, value, callback) => {
      if (value == null || value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (this.ruleForm.checkPass !== "") {
          this.$refs.ruleForm.validateField("checkPass");
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value == null || value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.ruleForm.password) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    return {
      //表单双向绑定数据
      formValue: {
        url: "",
      },
      passwordPlaceholder: "",
      flag: false,
      status: {},
      info: {},
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
      },
      ruleForm: {
        oldPassword: "",
        password: "",
        checkPass: "",
      },
      rules: {
        oldPassword: [{ validator: validateOldPass, trigger: "blur" }],
        password: [{ validator: validatePass, trigger: "blur" }],
        checkPass: [{ validator: validatePass2, trigger: "blur" }],
      },
    };
  },
  methods: {
    handleAvatarSuccess(res, file) {
      if (res.code == 200) {
        this.formValue.url = res.data.thumbnailUrl;
        this.$message.success(res.message);
      } else {
        this.$message.warn(res.message);
      }
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg" || file.type === "image/png";
      const isLt3M = file.size / 1024 / 1024 < 3;

      if (!isJPG) {
        this.$message.warn("上传图片只能是 JPG PNG格式!");
      }
      if (!isLt3M) {
        this.$message.warn("上传图片大小不能超过 2MB!");
      }
      return isJPG && isLt3M;
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let request = {
            id: this.formValue.id,
            oldPassword: this.ruleForm.oldPassword,
            password: this.ruleForm.password,
          };
          updatePass(request).then((res) => {
            if (res.code == 200) {
              this.notifySucceed("更新成功");
              //this.$router.push("/admin/user");
            } else {
              this.notifyError(res.message);
            }
          });
        } else {
          return false;
        }
      });
    },
    changePassword() {
      console.log(this.ruleForm);
      this.flag = !this.flag;
      this.$refs["ruleForm"].resetFields();
      //this.$refs['loginForm'].resetFields();
      this.ruleForm = {
        oldPassword: "",
        password: "",
        checkPass: "",
      };
      this.formValue = this.info;
    },
    //提交表单
    onSubmit() {
      this.$refs.loginForm.validate((valid) => {
        if (!valid) {
          return false;
        }
        let request = {
          id: this.formValue.id,
          username: this.formValue.username,
          password: this.formValue.password,
          email: this.formValue.email,
          phone: this.formValue.phone,
          vip: this.formValue.vip,
          url: this.formValue.url,
        };
        update(request).then((res) => {
          if (res.code == 200) {
            this.notifySucceed("更新成功");
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

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
