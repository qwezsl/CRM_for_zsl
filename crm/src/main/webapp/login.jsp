<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript" >
		$(function () {
			if (window.top!=window){
				window.top.location=window.location;
			}
			$("#loginAct").focus();/*页面加载完毕用户名获得焦点*/
			val();//刷新页面时清空文本框
			$("#but").click(function () {/*进入方式1 按钮绑定点击*/
				login();
			})

			$(window).keydown(function (event) { /*进入方式2窗口绑定回车*/
			if (event.keyCode==13){
				login();
		 	   }
			})
		})

		function val() {
			$("#loginAct").val(""); $("#loginPwd").val("");
		}

		function login() {
			//首先将客户的空白去了，如果还为空  那就真是空了就不让进
			var loginAct=$.trim($("#loginAct").val());
			var loginPwd=$.trim($("#loginPwd").val());
			if (loginAct=="" || loginPwd==""){
				$("#msg").html("账号密码不能为空");
				val(); //清空文本框
				return false;
			}

			$.ajax({
				url:"settings/user/save.do",
				data:{
					"loginAct":loginAct,
					"loginPwd":loginPwd
				},
				type:"post",
				dataType:"json",
				success:function (date) {

					if (date.success){//后端返回包括success成员，值为true或false,以它来判断账号或密码的正确
						window.location.href="workbench/index.jsp";//成功则跳转页面
					} else{
						$("#msg").html(date.msg); //后端返回包括msg,值为 哪错了的自定义异常信息
					}

					//也就是说，登录界面最终返回的只需要是一个json,包含了success和msg

				}
			})
		}



	</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2017&nbsp;动力节点</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.jsp" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" id="loginAct">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码" id="loginPwd">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						
							<span id="msg" style="color: red"></span>
						
					</div>
					<button type="button" id="but" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录123</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>