import request  from "@/utils/request";

export const querySelfData = () => request.get('/user')
export const querySelfFan = () => request.get('/user/fan')
export const querySelfFollow = () => request.get('/user/follow')
export const userUpdate = (form) => request.put('/user', JSON.parse(JSON.stringify(form)))