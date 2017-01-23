package cn.sjjy.edu.log.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.sjjy.edu.common.bean.Operator;
import cn.sjjy.edu.log.dao.LogOperationDao;
import cn.sjjy.edu.log.dto.OperationType;
import cn.sjjy.edu.log.po.LogOperationVo;

/** 
 * @author Captain
 * @date 2017年1月23日
 */
@Service
public class LogService implements ILogService {
    @Autowired
    private LogOperationDao dao;
    private final static Logger LOGGER = LoggerFactory.getLogger(LogService.class);

    @Override
    public void insertLog(String tableName, OperationType operationType, Operator operator, Object value) {
        if (null == tableName || null == operationType || null == value || Operator.validateParamIsNull(operator)) {
            return;
        }

        try {
            dao.insert(LogOperationVo.builder()
                    .operatorUsername(operator.getOperatorName())
                    .operatorType(operator.getOperatorType().getCode())
                    .tableName(tableName)
                    .type(operationType.getCode())
                    .value(JSONObject.toJSONString(value))
                    .build());
        } catch (Exception e) {
            LOGGER.error("insertLog.error", e);
        }
    }
}
