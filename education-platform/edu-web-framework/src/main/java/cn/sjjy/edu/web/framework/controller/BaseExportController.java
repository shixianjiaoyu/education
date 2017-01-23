package cn.sjjy.edu.web.framework.controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import cn.sjjy.edu.common.exception.ServiceException;

import cn.sjjy.edu.web.framework.annotations.CsvHeader;
import cn.sjjy.edu.web.framework.exception.WebApiExceptionCode;

/** 
 * @author Captain
 * @date 2017年1月21日
 */
@Controller
public class BaseExportController extends BaseController {
    public void csvWrite(HttpServletResponse response, List data, String fileName) throws IOException, ServiceException {
        if (CollectionUtils.isEmpty(data)) {
            throw new ServiceException(WebApiExceptionCode.NOT_FOUND);
        }

        Object object = data.get(0);
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        String[] header = new String[fields.length];
        String[] showHeader = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            showHeader[i] = fields[i].getAnnotation(CsvHeader.class).value();
            header[i] = fields[i].getName();
        }

        response.setContentType("text/csv;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.EXCEL_PREFERENCE);
        csvWriter.writeHeader(showHeader);

        for (Object obj : data) {
            csvWriter.write(obj, header);
        }
        csvWriter.close();
    }
}
