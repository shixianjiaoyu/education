package cn.sjjy.edu.log.service;

import cn.sjjy.edu.common.bean.Operator;
import cn.sjjy.edu.log.dto.OperationType;

/** 
 * @author Captain
 * @date 2017年1月21日
 */
public interface ILogService {
    public void insertLog(String tableName, OperationType operationType, Operator operator, Object value);
}
