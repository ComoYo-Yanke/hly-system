<script setup>

import { ref, onMounted } from 'vue';
import { querySelfData, querySelfFan, querySelfFollow,userUpdate } from '@/api/user';

const userData = ref([]);
const userAvatar = ref('');
const gender = ref('');
const bio = ref('');
const roles = ref('');
const dialogVisible = ref(false);
const fan = ref();
const follow = ref();

const form = ref({
    avatar: '',
    name: '',
    gender: '',
    email: '',
    role: '',
    bio: '',
    createTime: '',
});

// form = userData

const getData = async () => {
    const result = await querySelfData();
    const resultFan = await querySelfFan();
    const resultFollow = await querySelfFollow();

    
    
    userData.value = JSON.parse(JSON.stringify(result.data));
    fan.value = resultFan.data;
    follow.value = resultFollow.data;

    form.value = JSON.parse(JSON.stringify(result.data)); // 创建拷贝引用 - 方便回显和修改

    if (userData.value.avatar == null) userAvatar.value = "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
    else userAvatar.value = userData.value.avatar;
    if (userData.gender = 1) gender.value = '男';
    else if (userData.gender = 2) gender.value = '女';
    if (userData.value.bio == null) bio.value = '还没有个性签名, 说点什么吧~';
    else bio.value = userData.value.bio;

    if (userData.value.role == 1) roles.value = '管理员';
    else roles.value = '普通用户';
    console.log("原始返回：" , result.data);
    console.log("===", userData.value.bio);
    
}

const updateUser = async (form) => {
    console.log("更新");
    
    dialogVisible.value = false;
    await userUpdate(form);
    getData();
}

const takeAd = () => {
    dialogVisible.value = true;
}

const cancelUpdate = () =>{
    dialogVisible.value = false
}

onMounted(() => {
    getData();
});

</script>

<template>
    <div style="padding: 10px; height: 100%; width: 100%;" class="container">
        <h2 style="display: flex; width: 200px; align-items: center;">
            <el-avatar :size="45" :src="userAvatar" style="margin-right: 10px;"></el-avatar>
            <span style="overflow: auto; width: 100%;">{{ userData.name }}</span>
            
        </h2>
        <h4 style="display: flex; justify-content: space-between;">
            <div class="box-left" style="width: 50%;">
                <div class="email" style="display: flex; justify-content: 
                start;align-items: center; user-select:text;">
                    <el-icon :size="20">
                        <Message />
                    </el-icon> {{ userData.email }}
                </div>

                <br>
                性别：{{ gender }}
                <br>
                个性签名： {{ bio }}
            </div>
            <div class="box-right" style="width: 50%; display: flex; flex-direction: column; align-items: start;
            padding: 0 50px; border-left: 1px solid; box-sizing: border-box;">
                <h4>关注数：{{ follow }}</h4>
                <h4>粉丝数：{{ fan }}</h4>
            </div>

        </h4>
        <h5 style="width: 95%; border-bottom: 1px solid; margin: 50px 10px;"></h5>
        创建时间：{{ userData.createdTime }}
        <br>
        role：{{ roles }}
        <br>
        <div class="footer" style="display: flex; justify-content: center; width: 50%; margin-top: 50px;">
            <el-button @click="takeAd" class="button"
                style="display: flex; padding: 20px; font-weight: 100; margin: 30px;">
                <el-icon :size="20">
                    <Setting />
                </el-icon>
                <span>个人设置</span>
            </el-button>
            <el-tooltip content="请谨慎操作！" placement="right">
                <el-button type="danger" style="display: flex; padding: 20px; font-weight: 100; margin: 30px;">
                    <el-icon :size="20">
                        <Delete />
                    </el-icon>
                    <span>注销账户</span>
                </el-button>
            </el-tooltip>

        </div>


    </div>

    <!-- 模态窗口 -->
    <el-dialog class="container" v-model="dialogVisible" title="" width="500px" @close="handleClose">
        <h2>修改基本信息</h2>
        <div class="main">
            <el-form-item label="头像">
                <el-upload action="" :show-file-list="false">
                    <el-avatar :src="form.avatar" size="1">
                    </el-avatar>
                </el-upload>
            </el-form-item>


            <el-form-item label="姓名" prop="name" style="width: 250px;">
                <el-input v-model="form.name" placeholder="请输入姓名" :maxlength="10" show-word-limit />
            </el-form-item>
            <el-form-item label="性别" prop="gender">
                <el-radio-group v-model="form.gender">
                    <el-radio :label="1">男</el-radio>
                    <el-radio :label="2">女</el-radio>
                </el-radio-group>
            </el-form-item>

            <el-form-item label="个性签名" prop="signature">
                <el-input style="width: 100%; height: 100px; overflow-y: auto; white-space: pre-wrap;"
                    v-model="form.bio" :rows="3" placeholder="请输入个性签名" :maxlength="30" show-word-limit
                    type="textarea" />
            </el-form-item>

            <el-form-item label="邮箱" prop="email"
                :rules="[{ required: true, message: '请输入邮箱', trigger: 'blur' }, { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }]">
                <el-input v-model="form.email" placeholder="请输入邮箱" style="width: 100%;" />
            </el-form-item>

            <br />
            这里是内容
        </div>

        <template #footer>
            <div class="dialog-footer">
                <el-button class="button" @click="cancelUpdate">取消</el-button>
                <el-button type="primary" @click="updateUser(form)">确认</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<style scoped></style>
