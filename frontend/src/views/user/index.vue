<script setup>

import { ref, onMounted} from 'vue';
import { querySelfData } from '@/api/user';

const userData = ref([]);


const getData = async () => {
    const result = await querySelfData();
    userData.value = result.data;
}

onMounted(() => {
    getData();
});

</script>

<template>
    <div style="padding: 10px; height: 100%; width: 100%;" class="container">
        <h3 >
            <el-avatar>{{ userData.data.name }}</el-avatar>
        </h3>
        
    </div>
    
</template>

<style scoped></style>
