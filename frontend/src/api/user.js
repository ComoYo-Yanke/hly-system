import request  from "@/utils/request";

export const querySelfData = () => request.get('/user')