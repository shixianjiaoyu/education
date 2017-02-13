package cn.sjjy.edu.auth.springmvc;


import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SolarAuthFailHandler {

    /**
     * ����SolarȨ����֤ʧ��
     * @param request       http request
     * @param response      http response
     * @param solarInfo     Solar�û���Ϣ, ��δ��¼���Solar�û�����Ϊnull
     * @param pointsAlters  ��ѡ����Ȩ�޵�
     * @return  �Ƿ������������
     */
    boolean handle(HttpServletRequest request, HttpServletResponse response, SolarInfo solarInfo, List<Set<String>> pointsAlters);
}
