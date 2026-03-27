package com.hly.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hly.constant.DataConstant;
import com.hly.constant.UIXConstant;
import com.hly.dto.HealthEventPageAllDTO;
import com.hly.dto.HealthEventPageDTO;
import com.hly.dto.HealthEventPageMapperDTO;
import com.hly.entity.HealthEvent;
import com.hly.entity.User;
import com.hly.entity.UserFollow;
import com.hly.mapper.HealthEventMapper;
import com.hly.mapper.UIXMapper;
import com.hly.result.PageResult;
import com.hly.service.UIXService;
import com.hly.utils.ThreadLocalUtil;
import com.hly.vo.UserFollowQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class UIXServiceImpl implements UIXService {
    
    @Autowired
    UIXMapper uixMapper;
    @Autowired
    HealthEventMapper healthEventMapper;
    /**
     * 关注用户
     * @param followingId
     * @return
     */
    @Override
    public String follow(Integer followingId){
        if(followingId == ThreadLocalUtil.getCurrentIdS()) return UIXConstant.FOLLOW_SELF;
        else {
            UserFollow userFollow = new UserFollow();
            userFollow.setFollowerId(ThreadLocalUtil.getCurrentIdS());
            userFollow.setFollowingId(followingId);
            userFollow.setCreatedAT(LocalDateTime.now());
            uixMapper.insert(userFollow);
            return null;
        }
    }
    
    /**
     * 查询关注状态
     * @param followingId
     * @return
     */
    @Override
    public UserFollowQueryVO followQuery(Integer followingId){
        UserFollow userFollow = new UserFollow();
        userFollow.setFollowerId(ThreadLocalUtil.getCurrentIdS());
        userFollow.setFollowingId(followingId);
        
        UserFollowQueryVO userFollowQueryVO = uixMapper.query(userFollow);
        
        return userFollowQueryVO;
    }
    
    /**
     * 取消关注
     * @param followingId
     */
    @Override
    public void cancelFollow(Integer followingId){
        UserFollow userFollow = new UserFollow();
        userFollow.setFollowerId(ThreadLocalUtil.getCurrentIdS());
        userFollow.setFollowingId(followingId);
        uixMapper.deleteById(userFollow);
    }
    
    /**
     * 获取指定用户的粉丝数
     * @param id
     * @return
     */
    @Override
    public Integer getFollowCount(Integer id) {
        
        return uixMapper.queryFollowCountOnlyOne(id);
    }
    
    /**
     * 获取指定用户的关注数
     * @param id
     * @return
     */
    @Override
    public Integer getFansCount(Integer id) {
        return uixMapper.queryFansCountOnlyOne(id);
    }
    
    @Override
    public PageResult pageQuery(HealthEventPageAllDTO healthEventPageAllDTO) {
        // 使用多个DTO确保数据安全
        
        // 初始化数据处理
        if(healthEventPageAllDTO.getPage() == null)
            healthEventPageAllDTO.setPage(DataConstant.DEFAULT_PAGE);
        if(healthEventPageAllDTO.getPageSize() == null)
            healthEventPageAllDTO.setPageSize(DataConstant.DEFAULT_PAGE_SIZE);
        // 分页查询
        PageHelper.startPage(healthEventPageAllDTO.getPage(), healthEventPageAllDTO.getPageSize());
        log.info("分页查询：{}", healthEventPageAllDTO);
        Page<HealthEvent> page = healthEventMapper.pageAllQuery(healthEventPageAllDTO);
        long total = page.getTotal();
        List<HealthEvent> records = page.getResult();
        return new PageResult(total, records);
        
    }
}
