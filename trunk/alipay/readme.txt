�ӿ�Ӧ��˵��:
1. alipay_daikou : ֧���������ṩ����������.
   �̻�ֻ��Ҫ���������Ϣ�����䲿��webӦ�÷������ķ���Ŀ¼�¼�����ʾ
 
�ļ�˵��:
1. alipay_daikou----
  src-----------|
                |--com.alipay.config��
                   |--AlipayConfig��֧�����ؼ���Ϣ�����࣬���磺�տ��˻���ҳ����루GBK��UTF-8�ȣ�������ID�Ͱ�ȫУ����
                |--com.alipay.util��:���������ÿ��Դ򿪾����࣬��ͷ������˵��
                |--filters��
                   |--SetCharacterEncodingFilter��:�����ַ���
  WebRoot-------|
		|----/alipay/images:������Ҫ��ͼƬ��Ϣ
              	|----index.jsp:�ṩ���̻����Դ��۹���ʱ����ѡ�����
              	|----qianyue.jsp:�ͻ�ǩԼ��������ǩԼ��Ϣ����.                                    
                |----alipay_qianyue.jsp:�����̻��ύ��ǩԼ������Ϣ����ҳ��,�̻�����ύ����com.alipay.util���е�           
                                    Payment.java�ദ��.
                                    
                |----daikou.jsp:�ͻ����������Ϣ����.                                    
                |----alipay_daikou.jsp:�����̻��ύ�Ĵ�����Ϣ����ҳ��,�̻�����ύ����com.alipay.util���е�           
                                    Payment.java�ദ��.
                                    
                |----chaxun.jsp:�ͻ���ѯ������Ϣ���ύ����.                                    
                |----alipay_chaxun.jsp:�����̻��ύ�Ĳ�ѯ������Ϣ����ҳ��,�̻�����ύ����com.alipay.util���е�           
                                    Payment.java�ദ��.
                                    
                |----jieyue.jsp:�ͻ����ǩԼ����������Ϣ����.                                    
                |----alipay_jieyue.jsp:�����̻��ύ�Ľ��ǩԼ������Ϣ����ҳ��,�̻�����ύ����com.alipay.util���е�           
                                    Payment.java�ദ��.
                                                                                             
              	|----alipay_return.jsp������֧������������̻��ύ�Ķ�����Ϣ���ͬ������ҳ�桾��Ҫ���ò���return_url��
              	
                                       ��һ��"���ӻ�"�ķ��أ�ֻ��֧���ɹ��Ż�ͨ��ieҳ����ת֪ͨ��֧������֧���Ķ���	

			                          ������Ϣͨ��get��ʽ��ת�����ҳ�档�̻����������ҳ�������ݻ�ȡ�����ǻ�ȡ��Ϣ	

		                              �ܵ���Ҳ�����Ӱ�졣������֧����ɺ��̻���������Ӧ�Ƚ������������ʾ֧����	

		                              ��ʾ�ġ�֧���ɹ���ʱ�ر�ҳ�棬��ô�̻���վ�ǻ�ȡ������Ϣ��������߳�Ϊ�� ����	

	                                  ����������ش�����һ���Ե�ȡ����֧���ɹ���ŵ�ȡͬ�����ش������Խ����̻���  

                                      �첽�����ݷ��ش���
              	|----alipay_notify.jsp������֧������������̻��ύ�Ķ�����Ϣ���첽���ش�����Ҫ���ò���notify_url��
                                       �������ݽ�����ͨ����������������ݽ�����������post��Ϣ���첽���ش���ҳ�棬��

                                      Ҫ�̻��������첽���ش���ҳ�洦����ص����ݴ���Ȼ��ÿһ��������Ҫ���ظ�֧��    

                                      ��success�����ܰ���������HTML�ű����ԣ���������ҳ����ת������������ش������   

                                      ����û�����⣬����������ֵ�������Ϊ֧��������24Сʱ֮�ڷ�6~10�ν�������Ϣ��     

                                      �ظ����̻���վ��ֱ��֧��������successΪֹ��

2.��ע��
  A.ע�Ȿ��(�Ƿ�����)�����Բ����첽���ء�
  B.�ڲ����첽����ͬ������ʱ������ò��Խ����¼��־������src��com.alipay.util�����ҵ�SignatureHelper_return(ͬ����־
  )��SignatureHelper(�첽��־)����������һ��sign������������־��¼��·��
  C.java����Ҫע��������������⣬һ��Ҫ������ȥ����filter,
  ע�⣺һ��Ҫ��web.xml�����ù�������ÿ����Ŀ�ж�������������������������ֱ�Ӵ�
  WebRoot�ļ����£�web-inf�ļ����µ�web.xml�ļ���
  ���Բο��������£�
   http://blog.csdn.net/lixinye0123/archive/2006/03/26/639402.aspx
  ���磺
  <filter>
		<filter-name>Set Character Encoding</filter-name>
		<filter-class>filters.SetCharacterEncodingFilter</filter-class>
		<init-param>
			<param-name>encoding</param-name>
			<param-value>GBK</param-value>
		</init-param>
	</filter>
	<filter-mapping>
		<filter-name>Set Character Encoding</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>
		
			
		