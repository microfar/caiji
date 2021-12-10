package com.coinnet.service.sys;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * *@Remarks  谷歌发送email实现类 
 */

public  class SendMail {

	/**
	 * *@Remarks  谷歌发送email实现方法
	 * @param sendnick  发信人昵称  比如：某某网
	 * @param title   信的标题
	 * @param body   信的内容
	 * @param tomails  收件人，支持多个收件人  用 # 隔开
	 * @param stmpuser  发件人的邮箱账号
	 * @param smtppassword  发件人的邮箱密码
	 * @return
	 */
	public static String send(String sendnick, String title, String body, String tomails,String stmpuser,String smtppassword) {
		String sendState = "false";
		// SMTP邮件服务器发送配置
		final String smtpserver = "smtp.gmail.com"; // 服务器
		// smtpserver
		 final String username = stmpuser; // 发件用户 username
		 final String password = smtppassword; // 发件用户密码corslqnqwgdlepov
		 final String mailtype = "html"; // 发送邮件内容
		 

		try {
			System.out.println("smtpserver====" + smtpserver);
			String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
			Properties props = System.getProperties();
			props.setProperty("mail.smtp.host", smtpserver);
			props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
			props.setProperty("mail.smtp.socketFactory.fallback", "false");
			props.setProperty("mail.smtp.port", "465");
			props.setProperty("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.auth", "true");

			Session sendMailSession;
			sendMailSession = Session.getInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});

			// -- Create a new message --
			Message msg = new MimeMessage(sendMailSession);

			String[] tosendES = tomails.split("#");
			System.out.println("邮件个数==:" + tosendES.length);
			for (int p = 0; p < tosendES.length; p++) {

				System.out.println("邮箱====:" + tosendES[p]);
				// -- Set the FROM and TO fields --设置表单和字段
				try {

					System.out.println("邮件名===:" + sendnick);
					sendnick = javax.mail.internet.MimeUtility.encodeText(sendnick);

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					System.out.println("发送邮件出错：" + e);
				}

				msg.setFrom(new InternetAddress(sendnick + "<" + username + ">"));

				msg.setRecipients(Message.RecipientType.TO, InternetAddress
						.parse(tosendES[p], false));

				msg.setSubject(title);

				if (mailtype.indexOf("txt") != -1) {
					msg.setText(body); // 发送纯文本
				} else {
					msg.setContent(body, "text/html;charset=gb2312"); // 发送HTML
					// 邮件
				}

				msg.setSentDate(new Date());

				Transport.send(msg);
				System.out.println("发送邮件成功：->" + tosendES[p]);
				sendState = "true";

			}

		} catch (MessagingException m) {
			System.out.println("发送邮件出错：-->" + m.toString());
			sendState = "false";

		}
		return sendState;

	}

	/**
	 * @测试类
	 */
	public static void main(String[] args) {

		 	String bodyString = "<div><div>   <div style=\'background-color:#d0d0d0;background-image:url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAMAAABHPGVmAAAAb1BMVEX29vYv4JIu4JL99/sd3o7y8vL79/lv5qzv7+8g343/9/0t3ZAp4JAA3Yc63JQT3ouf68aq7Mvo9O7i9OzF7drv9vN35rF/57aJ57lC4prX8uNd46TQ8OC77tXd8+dR46Bz3anh6eaz7dDz7PCV6sGsaPyGAAAEPElEQVRoge2Z6XqiMBSGIYGQkGAQoZWiMqNz/9c45yTWhTVB+2eGr8Wni+TlbFmOQbBq1apVq1at+i/EGH1NKWNzCLrLkzpaLBHVSb5LJzHpV5MJRV6SEllzoOMM+pkpEk5q5t9WSn+OUlijZ++X0gmjmxGP0UTM2ACEzcYJEopkkMLybBog5WYjXSFhlg9R2IGoEYQFIAFfXRCE6OMghB4EId334uvVAi+IPg6G/oSUJ1tICCGQBOOAF6LgywWCDFYMOEsUjJYPGQwDSxyThACw7gpl19RRRhoEbdl1GM0zWQFFXEcxIYChTchrNMjJhDuDNVne9RjdR4pAyWNcyHeMrX+8ADdGK0SvIlmiQqUAVhr/4NOjsxAEPnJMW8vILEMqWyoxf4BAJUY1Z5cyMu6pEfadX152lBgPIQlAYiDw+AaJf4FHNrKtwBZtHWXSyxNh8ypoIvCBSmiMhBgpcHF+MZCNbIqAlQJzynt881CiZKxqIhNQtAQI8M05msTjDxgXq66FTD5qn0A/MvTxwqsEp7hniPEbZ78wg2D6EEgp9QI7oIzk8RL8TuTmG2JdhZ7CHzj7kBv7OKKp0BZvCrqhhGdP8AdTAveJGA2BiyXRt9FXitvCcRdEurzEFTJquEIS3SEQEkwBrJNHSnr2tUXKknLeSDuJws3qEXIrxptEA7V/zrxKEOrjAr6KIKzXxFS9desJArYwP4pUuqRx3DzOPzMQoLTUi6IEMuqn9XsOAh5DyjYjbksUMhiro6cKnoeE6DHqGH2lgBHUnREcIEQkMCdvXSiGUdVR98/zENg7JbDuO1Aso+3tp5wgRH8YW0b2MHMMNwh4zFCyScqVEfVnbTcIeOwDPJZPeUyRETvcIWAL5BhQxjBKHGDvM8hwhkAmAyXN9fASRpQ+jNnhAwGPpUH6Z3ibTDI4iFT1yD7dAwI5BrX/Z8hjJPuCtbYeO9J4QJBibOkNRcQhnWB4QazHYIfZGUwojEd3LlkMgejTgB3D+6YfToY62bFJhi+E4BmQFvtQK2XPnro9w2G8aqeOmL6QUO8ZlGVxTmoo8bD9LCv4faQ+FkJA+jNgeFCqdqddEcDEHtBT2513X4XA+nKi5iZ7JwvOZOY4vgACh/P9jsH6B7FgNCib2bV5CQTqQjd5eSqKr/O+np6al0OMNRlKR05vHoBMR3GB+hA4zr0b0j/O0Xwy5xdBegdTdhhveixUduj3JJYcraZESNVjsP2b/SX2A42PInqvKbrf9gBTtm+NSrYd7ODMNNW8RHQy0iCkzZLz6JCkHm7codK9w5TkIJUNBf1GOTXZdfVb3LGF+a05peMM03zeJq0Sy9Um25nms+G82kefJaxatWrVP6k4nn/Pa+LxD0OuHfGfg8R38Z+i2OEDg+D8/RBsTZs4GEbA5+/wHB/b+IH9WAJNeLufYptI8fWjj1fD/RcIvEyv+nzwEwAAAABJRU5ErkJggg==);text-align:center;padding:40px;\'>                    <div class=\'mmsgLetter\' style=\'width: 580px; margin: 0px auto; padding: 10px; color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); border: 0px solid rgb(170, 170, 170); border-top-left-radius: 5px; border-top-right-radius: 5px; border-bottom-right-radius: 5px; border-bottom-left-radius: 5px; -webkit-box-shadow: rgb(153, 153, 153) 3px 3px 10px; box-shadow: rgb(153, 153, 153) 3px 3px 10px; font-family: Verdana, sans-serif; background-position: initial initial; background-repeat: initial initial;\'>                                        <div class=\'mmsgLetterHeader\' style=\'height: 23px; background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGoAAAALCAMAAACgV+haAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAALpQTFRF197HkK5Q5eXl+fn5lpaW9/j0bW1txMTEsMGLTk5OjL0hh7Esampqh7Eui61BR0dHibcm+vr4iLQpx9Kvobdxqqqqia441NzDmbJjZGRk6enpirojiYmJ7O/mi7wi1tbWTExMjcAgRUVFW1tbUFBQV1dXjq1Lt7e33NzcdXV18vTu9PT02uHL5urdwsLCtMSSh7Iry8vL+/v7v8ykjb8hiK8zq76E5+fnfn5+8vLyoaGhQ0NDj8Mf/f39+sYr0AAAAKxJREFUeNqc0FcOwjAQRVFjOgQIvbfQe6+x978tFNESW8p45n2PdeTLZGD1vQhfjklt86YbvsTNOwu+rJwBKRLXpWoBkGJRqVKbGgBlUjrUWgBQOvm59FG7NiHedgzF41KlllOBj7fquGbx/FS5RIjXH5nG+1PFxhCQsnq849Mxjvej1hdCPG4h4n2pa48QbzbAxHtT3bzAx3vcoS9ZXH3DTjYh3mGCjOftJcAAghgDufxW3jIAAAAASUVORK5CYII=); background-position: 0px 0px; background-repeat: repeat no-repeat;\'>  </div><div class=\'mmsgLetterContent\' style=\'text-align: left; padding: 30px; font-size: 14px; line-height: 1.5; background-image: url(https://docs.jiguang.cn/img/icon-jpush.svg); background-position: 100% 0%; background-repeat: no-repeat no-repeat;\'>   <div>  <p class=\'salutation\' style=\'font-weight: bold; margin: 20px 0px; padding: 0px;\'>                                    Hi,<span id=\'mailUserName\'>【名字】</span>：                                </p>                                 <p style=\'margin: 20px 0px; padding: 0px;\'>忘记密码了吗？别着急，请点击以下链接，我们协助您找回密码：<br><font color=\'#ff0000\'>注意：5分钟后失效！</font><br>                <a href=\'https://www.www.com/home/resetpassword.jsp?id=【随机码】&amp;email=china086@163.com\' style=\'word-break: break-all; word-wrap: break-word; display: block; color: rgb(64, 119, 0);\' target=\'_blank\'>https://www.com/home/resetpassword.jsp?id=【随机码】&amp;email=【邮箱】</a><br><br>如果这不是您的邮件请忽略，很抱歉打扰您，请原谅。</p>                            </div>	                                            <div class=\'mmsgLetterInscribe\' style=\'padding: 40px 0px 0px;\'>                                <img class=\'mmsgAvatar\' src=\'data:image/jpg;base64,/9j/4AAQSkZJRgABAQEASABIAAD/2wBDAAYEBAQFBAYFBQYJBgUGCQsIBgYICwwKCgsKCgwQDAwMDAwMEAwODxAPDgwTExQUExMcGxsbHCAgICAgICAgICD/2wBDAQcHBw0MDRgQEBgaFREVGiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICD/wAARCAA9ADIDAREAAhEBAxEB/8QAHAAAAQUBAQEAAAAAAAAAAAAACAMEBQYHAgAB/8QANBAAAQMDAgMFBgUFAAAAAAAAAQIDBAAFEQYSBxMhCBQiMTIjQUJRcXJSYYGRsTNigsHC/8QAGQEAAwEBAQAAAAAAAAAAAAAAAAECBAMF/8QAHREBAAIDAQADAAAAAAAAAAAAAAECAxExIRJRcf/aAAwDAQACEQMRAD8AxNpirI8QzTBOTLixcpcV7QDcWx54NKbHEPRLhFkK2DKF7tm1fQkj5UosNHhaHyqiIrZoBHlUAoyigHK1JZYW6rybSVH9KRrZwg4QO6yaVeLsCzZ1O5Cse1lOA9Qk/C2jyz76z2s00p98THF/gWqxxpV8siiq3A82dH81sE+TzZ/CD6hRE/Ymsa3DPLc8ZVvZfPrUnC8fiHQmtEM0u3EUyIbKQfWRTDqehlUB9Dyy2ytOxbiepSFdM1M8OvRMaetF2tsLTrNmYULKxEZCpLcgo2eHJ9h6VhRyTnrWTbf8YTV2tt6uF/U09GMiwutKafk94UhKEqThSEsDwqBB6k9aclWIC23bYVuL8GC6p6JGfdbacX54Cz0/StVOMV+uHE1STfbQCbNAc3LBhrQTjf5VFpVWBFcLtYvXThbElI5suXDT3Oe2xtLyXGzjdtUUj04NZbePQxR85jkfqcnayVbdJXa8y+dFjxG1ndJCE8xwjCeWlClfEQBRC8uOKT2J/AuWGY5Jjvc85lB1S3vq4d2a1Y3m36euVaDbFAN2DQZpdZzbDzIcb5jbnhV+QPyrld1x6aP2bZFxi6h1DGiLwHW2XkMr9C9qjnp88e+uGSeO2OOrl2gW7hM0ewbkeVFMxtIaZ9KSASFL+dRS3rpasa0wiyyubc3AnapKGQgrR1yQffWrGy5Ey4a6uJDNAS3D3RVz1nqBNoguJZCWy/Kkr6hplJAKsDzOTgCkFCnOuP3Rxpx9XdG5RaQdoCg2h0o3fdtGaWtnFtDC05was2jHTeLJNdkQilDgbkYKkp25WeYMZCvPGK45au+K65xtPW6+tFdyjolW51PgjODclST6c/vmopT3asl9eA84uzrHD4jXSNpSK1bbXayIQbj+h1xr+qtec58RIrTDNMpq66C1HbtHWfVckNPWu7stOh1nOWlvJ3JQ4k/yOlMlXzQG5dlG1o5Worsoe0UWYLavy2qdX/zSkw13iMWrrco56KRLkoP1DyxTIcPCa8xdY8J7QuV7QPQ+4T0Z+JkclY+pAzU2hVba9SWtr/A0Jw8uFxaGxm0xOXCbJ83MctlPXz8RFKIFrbncgEfcdcDrrytzzm5x1Z961eJR/c1aRj3S0pkdmqLFCMqYscWQgfJTCEO/6NIwv7qZCR7LjaUcOpTw9bt0e3H7G2wKAGbXzSGtc6kbQMJTdJWP8nCr+TQG6dj++zVjUNhWd0JjlTWB+FbmULA+7aKUg57X98mNWjT9iR0iTn3ZUn+4xgOWn6ZVmiAGBfiQv7VfxTA+1wmRw67jj2IsymgPy7rikYJ2ieWj6CqJ/9k=\' style=\'float: left;\'>                                <div class=\'mmsgSender\' style=\'margin: 0px 0px 0px 54px;\'>                                    <p class=\'mmsgName\' style=\'margin: 0px 0px 10px; padding: 0px;\'>Cindy</p>                                    <p class=\'mmsgInfo\' style=\'font-size: 12px; margin: 0px; line-height: 1.2; padding: 0px;\'>                                        客服经理<br>                                        <a href=\'mailto:coinnetok998@gmail.com\' style=\'color: rgb(64, 119, 0);\' target=\'_blank\'>coinnetok998@gmail.com</a>                                    </p>                                </div>                            </div>                                                    </div>                                            <div class=\'mmsgLetterFooter\' style=\'margin: 16px; text-align: center; font-size: 12px; color: rgb(136, 136, 136); text-shadow: rgb(238, 238, 238) 1px 0px 0px;\'>                                                                            </div>                    </div>                                                        </div></div></div>";
		 
		 System.out.println(send("账号激活", "账号激活码请在20分钟内激活", bodyString, "0001@protonmail.com","zhe@gmail.com","xd*****gl"));
 
	}

}
