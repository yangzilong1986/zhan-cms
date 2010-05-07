接口应用说明:
1. alipay_daikou : 支付宝代扣提供的样例代码.
   商户只需要配置完成信息，将其部署到web应用服务器的发布目录下即可演示
 
文件说明:
1. alipay_daikou----
  src-----------|
                |--com.alipay.config包
                   |--AlipayConfig：支付宝关键信息配置类，例如：收款账户、页面编码（GBK、UTF-8等）合作者ID和安全校验码
                |--com.alipay.util包:相关类的作用可以打开具体类，类头部都有说明
                |--filters包
                   |--SetCharacterEncodingFilter类:设置字符集
  WebRoot-------|
		|----/alipay/images:放置需要的图片信息
              	|----index.jsp:提供给商户测试代扣功能时流程选择界面
              	|----qianyue.jsp:客户签约代扣输入签约信息界面.                                    
                |----alipay_qianyue.jsp:接受商户提交的签约代扣信息后处理页面,商户点击提交后由com.alipay.util包中的           
                                    Payment.java类处理.
                                    
                |----daikou.jsp:客户输入代扣信息界面.                                    
                |----alipay_daikou.jsp:接受商户提交的代扣信息后处理页面,商户点击提交后由com.alipay.util包中的           
                                    Payment.java类处理.
                                    
                |----chaxun.jsp:客户查询代扣信息的提交界面.                                    
                |----alipay_chaxun.jsp:接受商户提交的查询代扣信息后处理页面,商户点击提交后由com.alipay.util包中的           
                                    Payment.java类处理.
                                    
                |----jieyue.jsp:客户解除签约代扣输入信息界面.                                    
                |----alipay_jieyue.jsp:接受商户提交的解除签约代扣信息后处理页面,商户点击提交后由com.alipay.util包中的           
                                    Payment.java类处理.
                                                                                             
              	|----alipay_return.jsp：负责支付宝处理完成商户提交的订单信息后的同步返回页面【需要配置参数return_url】
              	
                                       是一种"可视化"的返回，只有支付成功才会通过ie页面跳转通知。支付宝将支付的订单	

			                          部分信息通过get方式跳转到这个页面。商户可以在这个页面做数据获取，但是获取信息	

		                              受到买家操作的影响。如果买家支付完成后商户服务器响应比较慢，买家在显示支付宝	

		                              提示的“支付成功“时关闭页面，那么商户网站是获取不到信息，我们这边称为” 掉单	

	                                  而且这个返回处理是一次性调取，即支付成功后才调取同步返回处理。所以建议商户在  

                                      异步做数据返回处理
              	|----alipay_notify.jsp：负责支付宝处理完成商户提交的订单信息的异步返回处理【需要配置参数notify_url】
                                       它的数据交互是通过服务器间进行数据交互，服务器post消息到异步返回处理页面，需

                                      要商户技术在异步返回处理页面处理相关的数据处理，然后每一步操作都要返回给支付    

                                      宝success（不能包含其他的HTML脚本语言，不可以做页面跳转。），这个返回处理如果   

                                      集成没有问题，基本不会出现掉单，因为支付宝会在24小时之内分6~10次将订单信息返     

                                      回个给商户网站，直到支付宝捕获success为止。

2.备注：
  A.注意本地(非服务器)不可以测试异步返回。
  B.在测试异步或者同步返回时如果想让测试结果记录日志可以在src下com.alipay.util包下找到SignatureHelper_return(同步日志
  )和SignatureHelper(异步日志)两个类中有一个sign方法中配置日志记录的路径
  C.java程序要注意的中文乱码问题，一定要配置上去中文filter,
  注意：一定要在web.xml中配置过滤器。每个项目中都配置了这个过滤器，具体可以直接打开
  WebRoot文件夹下，web-inf文件夹下的web.xml文件。
  可以参考下面文章：
   http://blog.csdn.net/lixinye0123/archive/2006/03/26/639402.aspx
  例如：
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
		
			
		