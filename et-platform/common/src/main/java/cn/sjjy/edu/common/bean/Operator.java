package cn.sjjy.edu.common.bean;

import cn.sjjy.edu.common.enums.OperatorType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 
 * @author Captain
 * @date 2017年1月23日
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Operator {

    private String operatorName;

    private OperatorType operatorType;

    public static final boolean validateParamIsNull(Operator operator) {
        if (null == operator) {
            return true;
        }
        if (null == operator.getOperatorName() || "".equals(operator.getOperatorName()) || null == operator.getOperatorType()) {
            return true;
        }
        return false;
    }

    public final static Operator SYSTEM = new Operator("SYSTEM", OperatorType.SYSTEM);
}
