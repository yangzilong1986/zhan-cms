package zt.cms.xf.newcms.domain.T201002;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import zt.cms.xf.newcms.domain.common.MsgHeader;

/**
 * 4.3.2.	���ڻ��������ѯ
    ҵ�����̣�
    ?	�����Ŵ�ϵͳ�������뵥�����Ŵ�ϵͳ�����ѯ����
    ?	�Ŵ�ϵͳ���ݵ�����ϵͳ�ڲ�ѯ���ظñ����뵥����Ϣ��
        1)	���δ�ҵ��ñʼ�¼����"���뵥�Ų�����"
        2)����ѷſ��
	        �ͻ���|�ͻ�����|֤������|֤������|��ͬ��|��ݺ�|�ſ���|�ſ�����|��������|������|�������|������̬
        3)����Ѿܾ����ء��ܾ�ԭ�򡱡�
        4)���򣬷���״̬�������С���
 * User: zhanrui
 * Date: 2010-8-27
 * Time: 17:11:06
 * To change this template use File | Settings | File Templates.
 */

@XStreamAlias("ROOT")
public class T201002Request extends MsgHeader {
    private String stdsqdh;


    public String getStdsqdh() {
        return stdsqdh;
    }

    public void setStdsqdh(String stdsqdh) {
        this.stdsqdh = stdsqdh;
    }
}