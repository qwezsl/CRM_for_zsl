<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<base href="<%=basePath%>">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>


<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<script type="text/javascript">
	$(function(){

		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "top-left"
		});

		$("#isCreateTransaction").click(function(){
			if(this.checked){
				$("#create-transaction2").show(200);
			}else{
				$("#create-transaction2").hide(200);
			}
		});

		/*----------------------------查询的是所有市场活动，然后是单选框，选中一项--------------------*/
		$("#selectActivity").click(function () {

			var sname=$("#selectName").val();

			$.ajax({

				url:"workbench/clue/GetActivityByName.do",
				type:"get",
				dataType:"json",
				data:{
				"sname":sname
				},
				success:function (date) {

					var html="";
					$.each(date,function (i,n) {
						html+='<tr>';
						html+='<td><input type="radio"  value='+n.id+' name="activity"/></td>';
						html+='<td id="name">'+n.name+'</td>';
						html+='<td>'+n.startDate+'</td>';
						html+='<td>'+n.endDate+'</td>';
						html+='<td>'+n.owner+'</td>';
						html+='</tr>';
					})
					$("#Activitys").html(html);
				}
			})
		})

		$("#relationBtn").click(function () {
			var xz=$("input[name=activity]:checked");
			if (xz.length==0){
			alert("请搜索并选择市场活动");
			}else {
				var ii=$("#activityId").val($("input[name=activity]:checked").val()); //将所选的市场活动id存入隐藏域

                      /*activityId == 市场活动源 */
 				alert(ii.val())
				var name=$("#name").html();
				$("#activity").val(name);
				$("#searchActivityModal").modal("hide");
				$("#Activitys").empty(); //刷新
				$("#selectName").val(""); //刷新
			}

		})


		$("#converBtn").click(function () {
			//根据"为客户创建交易" 的复选框 的勾选状态，来判断是否创建交易,将这些内容转发至交易页面
			if($("#isCreateTransaction").prop("checked")){   //使用prop函数判断是否为选中状态

				//当选中状态,就是需要将小交易的内容 连同 ClueId传入
			//这里可以用window.location.herf="" 跳转的方式，然后后面拼接参数,但是很蠢
			//因为，如果客户中途需求变更,想要小交易模态窗口,变成完整的交易模态窗口
			//那么拼接的参数就长了,不仅变得非常麻烦,并且会有字符数量限制
		//所以这里选择其他的请求方式，既然拼接字符串不能用，那么只能用表单提交
		//表单提交的好处: 1.不用手动挂载参数，2.可指定post请求，不受字符数量限制
	//ClueId怎么传入，得跟着表单进入，所以需要创建隐藏域来存ClueId

				$("#formConvert").submit();//jq使用submit函数提交即可


			}else {
				// 跳转后台处理，将Clueid传入，因为要告诉后台要转换的是哪条线索
				window.location.href="workbench/clue/convert.do?ClueId=${param.id}&convertDate=没有勾选";
			}

		})


	});
</script>

</head>
<body>
	
	<!-- 搜索市场活动的模态窗口 -->
	<div class="modal fade" id="searchActivityModal" role="dialog" >
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">搜索市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" id="selectName" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>

							<input type="button" id="selectActivity" class="btn btn-primary" value="查询"/>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
								<td></td>
							</tr>
						</thead>
						<tbody id="Activitys">
							<%--<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="relationBtn">提交</button>
				</div>
			</div>
		</div>
	</div>

	<div id="title" class="page-header" style="position: relative; left: 20px;">
		<h4>转换线索 <small>${param.name}-${param.company}</small></h4>
	</div>
	<div id="create-customer" style="position: relative; left: 40px; height: 35px;">
		新建客户：${param.company}
	</div>
	<div id="create-contact" style="position: relative; left: 40px; height: 35px;">
		新建联系人：${param.name}
	</div>
	<div id="create-transaction1" style="position: relative; left: 40px; height: 35px; top: 25px;">
		<input type="checkbox" id="isCreateTransaction"/>
		为客户创建交易
	</div>
	<div id="create-transaction2" style="position: relative; left: 40px; top: 20px; width: 80%; background-color: #F7F7F7; display: none;" >
	
		<form action="workbench/clue/convert.do" id="formConvert" method="post">
			<input type="hidden" name="a" value="b"/><%--后台识别表单还是跳转的标记--%>
			<input type="hidden" name="ClueId" value="${param.id}" />
		  <div class="form-group" style="width: 400px; position: relative; left: 20px;">
		    <label for="amountOfMoney">金额</label>
		    <input type="text" class="form-control" id="amountOfMoney" name="money">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="tradeName">交易名称</label>
		    <input type="text" class="form-control" name="convertName" id="tradeName" value="${param.company}-">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="expectedClosingDate">预计成交日期</label>
		    <input type="text" class="form-control time" id="expectedClosingDate" name="convertDate">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="stage">阶段</label>
		    <select id="stage"  class="form-control" name="convertStage">
				<option></option>
				<c:forEach items="${stage}" var="a">
					<option value="${a.value}"> ${a.text}  </option>
				</c:forEach>


		    </select>
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="activity">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" data-toggle="modal" data-target="#searchActivityModal" style="text-decoration: none;"><span class="glyphicon glyphicon-search"></span></a></label>
		    <input type="text" class="form-control" id="activity"  placeholder="点击上面搜索" readonly>
			  <input  type="hidden" id="activityId" name="activityId"/><%--隐藏域--%>
		  </div>
		</form>
		
	</div>
	
	<div id="owner" style="position: relative; left: 40px; height: 35px; top: 50px;">
		记录的所有者：<br>
		<b>${param.owner}</b>
	</div>
	<div id="operation" style="position: relative; left: 40px; height: 35px; top: 100px;">
		<input class="btn btn-primary" id="converBtn" type="button" value="转换">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input class="btn btn-default" type="button" value="取消">
	</div>
</body>
</html>