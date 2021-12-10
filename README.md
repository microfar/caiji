# caiji
数据采集，改进https请求apache工具类原始对HTTPS SSL版本1.0报错的问题， //添加SSL1.2证书支持
	if (httpClient == null) {
			httpClient = HttpClients.createDefault();
		}
	 
		  //添加SSL1.2证书支持
		SSLContext ctx = null;
		try {
			ctx = SSLContexts.custom().useProtocol("TLSv1.2").build();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
