package com.feiyang.albumb.service.impl;

import com.feiyang.albumb.mapper.FriendRequestMapper;
import com.feiyang.albumb.service.FriendRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

@Service
public class FriendRequestServiceImpl implements FriendRequestService {

    private static final Logger logger = LoggerFactory.getLogger(FriendRequestServiceImpl.class);

    @Autowired
    private FriendRequestMapper friendRequestMapper;

    @Override
    @Transactional(rollbackFor = Exception.class) // 异常时回滚，保证数据一致性
    public void sendFriendRequest(Map<String, Object> paramMap) {
        try {
            // 1. 打印前端原始参数（便于排查）
            logger.info("接收好友请求前端参数：{}", paramMap);

            // 2. 解析前端参数（映射为表字段）
            Integer fromUserId = parseIntegerParam(paramMap, "user_id");  // 前端 user_id → 表 from_user_id
            Integer toUserId = parseIntegerParam(paramMap, "friend_id");// 前端 friend_id → 表 to_user_id
            String remark = parseStringParam(paramMap, "remark");        // 可选备注，兼容null

            logger.info("参数映射完成：from_user_id={}, to_user_id={}, remark={}",
                    fromUserId, toUserId, remark);

            // 3. 业务校验（基于表结构和业务规则）
            // （1）禁止添加自己为好友
            if (fromUserId.equals(toUserId)) {
                logger.warn("业务校验失败：用户{}尝试添加自己为好友", fromUserId);
                throw new RuntimeException("不能添加自己为好友");
            }

            // （2）校验是否已互为好友（修正 Mapper 方法名：isFriend → 原定义的 isFriend）
            int isFriendCount = friendRequestMapper.isFriend(fromUserId, toUserId);
            if (isFriendCount > 0) {
                logger.warn("业务校验失败：用户{}与{}已互为好友", fromUserId, toUserId);
                throw new RuntimeException("已互为好友，无需重复发送请求");
            }

            // （3）校验是否已发送过待处理请求（修正 Mapper 方法名：checkDuplicateRequest → hasPendingRequest）
            int duplicateCount = friendRequestMapper.hasPendingRequest(fromUserId, toUserId);
            if (duplicateCount > 0) {
                logger.warn("业务校验失败：用户{}已向{}发送过待处理请求", fromUserId, toUserId);
                throw new RuntimeException("已发送好友请求，对方未处理，请勿重复发送");
            }

            // 4. 执行数据库插入（修正 Mapper 方法名：sendFriendRequest → insertFriendRequest）
            int affectedRows = friendRequestMapper.insertFriendRequest(fromUserId, toUserId, remark);
            if (affectedRows == 0) {
                logger.error("数据库操作失败：好友请求插入失败，影响行数为0");
                throw new RuntimeException("好友请求发送失败");
            }

            logger.info("好友请求发送成功：from_user_id={}, to_user_id={}", fromUserId, toUserId);

        } catch (RuntimeException e) {
            // 业务异常直接抛出（前端接收具体提示）
            throw e;
        } catch (Exception e) {
            // 系统异常（如数据库连接、SQL错误）打印堆栈
            logger.error("发送好友请求系统异常", e);
            throw new RuntimeException("发送失败（系统异常）");
        }
    }

    /**
     * 工具方法：解析Integer类型参数（必传+格式校验）
     */
    private Integer parseIntegerParam(Map<String, Object> paramMap, String key) {
        Object value = paramMap.get(key);
        // 非空校验
        if (value == null || "".equals(value.toString().trim())) {
            logger.warn("参数校验失败：{}不能为空", key);
            throw new RuntimeException(key + "不能为空");
        }
        // 格式校验（兼容数字/字符串类型）
        try {
            return Integer.parseInt(value.toString().trim());
        } catch (NumberFormatException e) {
            logger.warn("参数校验失败：{}格式错误，值为{}", key, value);
            throw new RuntimeException(key + "格式错误，需为整数");
        }
    }

    /**
     * 工具方法：解析String类型参数（可选，允许为null）
     */
    private String parseStringParam(Map<String, Object> paramMap, String key) {
        Object value = paramMap.get(key);
        if (value == null || "null".equalsIgnoreCase(value.toString().trim())) {
            return null;
        }
        // 去除首尾空格，避免空字符串备注
        String remark = value.toString().trim();
        return remark.isEmpty() ? null : remark;
    }
}