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
      <el-form-item label="管理员账号" prop="adminName">
        <el-input
          name="name"
          v-model="formValue.adminName"
          style="width: 280px"
          :disabled="user_name_readonly"
        ></el-input>
      </el-form-item>
      <el-form-item label="设置角色" prop="role">
        <el-select
          v-model="formValue.role"
          style="width: 280px"
          placeholder="请选择"
        >
          <el-option
            v-for="(item, i) in roles"
            :key="i"
            :label="item.role_name"
            :value="item.rule"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="设置密码" prop="password">
        <el-input
          v-model="formValue.password"
          style="width: 280px"
          type="password"
          :placeholder="passwordPlaceholder"
        ></el-input>
      </el-form-item>
      <el-form-item label="真实姓名" prop="name">
        <el-input v-model="formValue.name" style="width: 280px"></el-input>
      </el-form-item>
      <el-form-item label="备注说明" prop="description">
        <el-input
          v-model="formValue.description"
          style="width: 280px"
        ></el-input>
      </el-form-item>
      <el-form-item label="是否启用">
        <el-radio-group v-model="formValue.status">
          <el-radio border v-for="(la, i) in status" :key="i" :label="i">{{
            la
          }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit()">立即创建</el-button>
        <el-button @click="resetForm" v-if="!user_name_readonly"
          >重置</el-button
        >
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { regAdmin } from "@/api/adminUser";

export default {
  name: "adduserd",
  created() {
    this.$nextTick(() => {
      this._getRole();
    });
  },
  data() {
    return {
      //表单双向绑定数据
      formValue: {
        status: 1,
      },
      passwordPlaceholder: "",
      user_name_readonly: false,
      roles: {},
      status: {},
      //表单提交规则
      formRules: {
        adminName: [
          { required: true, message: "必填项", trigger: "blur" },
          { min: 5, message: "最少为输入5位数", trigger: "blur" },
        ],
        password: [
          // {required: true, message: '必填项', trigger: 'blur'},
          { min: 6, message: "最少为输入6位数", trigger: "blur" },
        ],
        role: [{ required: true, message: "必填项", trigger: "change" }],
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
            this.$message({
              message: "添加成功",
              type: "success",
            });
            this.$router.push("/admin/user");
          } else {
            this.$message({
              message: res.message,
              type: "warn",
            });
          }
        });
      });
    },
    //重置表单
    resetForm() {
      this.$refs.loginForm.resetFields();
    },
    _getRole() {
      if (Object.keys(this.editAdmin).length > 0) {
        this.formValue = this.editAdmin;
      }
      this.roles = [
        { id: 1, role_name: "总经理", rule: "admin" },
        { id: 2, role_name: "经理", rule: "经理" },
        { id: 3, role_name: "主管", rule: "主管" },
        { id: 4, role_name: "普通管理", rule: "普通管理" },
      ];
      this.status = ["停封", "正常"];
    },
  },
  computed: {
    ...mapGetters(["editAdmin"]),
  },
};
</script>

<style scoped></style>
