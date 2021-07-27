<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<base href="<%=basePath%>">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>
<script type="text/javascript">

	$(function(){


		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});

		$("#addBtn").click(function () {

			$("#create-Owner").empty();
			$.ajax({
				url:"workbench/Activity/getUserList.do",
				dataType:"json",
				success:function (date) {



					$.each(date,function (i,n) {

                        $("#create-Owner").append("<option value='"+n.id+"'>"+n.name+"</option>");

					})

                    var id="${user.id}";

                    $("#create-Owner").val(id);

                    $("#createActivityModal").modal("show"); //打开模态窗口，关闭为hide
				}
			})

		})

		//创建中的保存按钮
		$("#saveBtn").click(function () {

			$.ajax({
				url:"workbench/Activity/save.do",
				data:{
					"owner":$.trim($("#create-Owner").val()),//外键 所有者
					"name":$.trim($("#create-Name").val()), //市场活动名称
					"startDate": $.trim($("#create-startDate").val()), //开始时间
					"endDate":$.trim($("#create-endDate").val()),//结束时间
					"cost":$.trim($("#create-Cost").val()), //成本
					"description":$.trim($("#create-description").val())//描述
				},
				type:"post",
				dataType:"json",
				success:function (date) {

					if (date.success){//依然是用success属性的true作为成功登入

						//更新 页面列表信息
						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
						//更新后，回到第一面，但每页的页数保持指定状态

						$("#fromadd")[0].reset();//jq转为dom对象，使用reset方法清除 模态窗口 表单
						//别tm直接用reset() jq的reset()是个骗子，因为jq没有reset()方法，是个大坑  js的才有效果

						//关闭模态窗口
						$("#createActivityModal").modal("hide");
					}else {

						alert("请重试");

					}

				}

			})
		})


					//修改按钮 (展示数据库中的信息 )
		$("#editBtn").click(function () {

			var $xz=$("input[name=xz]:checked");

			if ($xz.length==0){
				alert("请选择需要修改的记录");
			} else if ($xz.length>1){
				alert("一次只能修改一条记录");
			}else {

				$.ajax({

					url:"workbench/Activity/edit.do",
					type:"get",
					data:{"id":$xz.val()},
					dataType:"json",
					success:function (data) {
						//需要拿到的  (所有者列表 User表的name)  and  所有者相关信息
					var html="";

					$.each(data.ulist,function (i,n) {
						html+="<option value='"+n.id+"'>"+n.name+"</option>";
					})
						$("#update-id").val(data.activity.id);
						//隐藏域 id 值设为 查询到的id,为了点击保存进行实际修改时，知道修改的是谁
						//但又不需要展现出来，所以放进隐藏域


						$("#update-owner").html(html);
						$("#update-owner").val(data.activity.owner);
						$("#update-name").val(data.activity.name);
						$("#update-startDate").val(data.activity.startDate);
						$("#update-endDate").val(data.activity.endDate);
						$("#update-cost").val(data.activity.cost);
						$("#update-description").val(data.activity.description);

						$("#editActivityModal").modal("show");
					}

				})

			}


		})


		//修改中的保存按钮  (直接复制添加按钮的部分  ，很相似 ，实际开发先写添加操作)
		$("#updateBtn").click(function () {

			$.ajax({
				url:"workbench/Activity/update.do",
				data:{
					"id":$.trim($("#update-id").val()), //从隐藏与中取出id,用来作为where条件 来修改对应id的记录
					"owner":$.trim($("#update-owner").val()),//外键 所有者
					"name":$.trim($("#update-name").val()), //市场活动名称
					"startDate": $.trim($("#update-startDate").val()), //开始时间
					"endDate":$.trim($("#update-endDate").val()),//结束时间
					"cost":$.trim($("#update-cost").val()), //成本
					"description":$.trim($("#update-description").val())//描述
				},
				type:"post",
				dataType:"json",
				success:function (date) {

					//修改成功后需要更新信息列表，和创建不一样

					if (date.success){//依然是用success属性的true作为成功登入
						//更新 页面列表信息
						//关闭模态窗口

		//有个bug,创建、修改、删除后，即使用户在第二页，指定了每页显示5页 后操作的，pageList依然会回到1,2 的状态
						//解决：

						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
								//操作后停留在当前页

								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
						 		//操作后维持已经设好的每页展现的记录数


						//修改后，保持不动默认当前页，每页的页数保持指定状态

						$("#editActivityModal").modal("hide");
					}else {

						alert("请重试");

					}

				}

			})
		})

/*----------------------------------信息列表操作--------------------------*/
        pageList(1,3);

        //查询按钮
		$("#selectBtn").click(function () {


			$("#hidden-name").val($.trim($("#select-name").val()));
			$("#hidden-owner").val($.trim($("#select-owner").val()))
			$("#hidden-startDate").val($.trim($("#select-startDate").val()))
			$("#hidden-endDate").val($.trim($("#select-endDate").val()))

		    pageList(1,3)

		})

		$("#qx").click(function () {

            // 选取 所有动态生成的复选框
            $("input[name=xz]").prop("checked",this.checked);

        })

		$("#tbody").on("click",$("input[name=xz]"),function () {
			$("#qx").prop("checked",  $("input[name=xz]").length    ==    $("input[name=xz]:checked").length);
		})


        $("#deleteBtn").click(function () {



            var xz=$("input[name=xz]:checked");
            var xzarr="";
            if (xz.length==0){
                alert("请选择删除的记录")
            } else {

            	if(confirm("确定删除所选中的记录吗？"))
				{

					for (var i=0;i<xz.length;i++){

						xzarr+="id="+xz[i].value

						//在每个元素后加一个&
						if (i<xz.length-1){
							xzarr+="&";
						}
					}

					$.ajax({

						url: "workbench/Activity/delete.do",
						type: "post",
						dataType: "json",
						data: xzarr,
						success:function (date) {

							if (date.success){
								pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
								//删除后，回到第一面，但每页的页数保持指定状态

							} else {
								alert("操作失败")
							}
						}
					});

				}
            }
        })



	}); //function页面加载后


	function pageList(pageNo,pageSize){

		$("#select-name").val($.trim($("#hidden-name").val()));
		$("#select-owner").val($.trim($("#hidden-owner").val()))
		$("#select-startDate").val($.trim($("#hidden-startDate").val()))
		$("#select-endDate").val($.trim($("#hidden-endDate").val()))

		$.ajax({

			url:"workbench/Activity/pageList.do",
			type:"get",
			dataType:"json",
			data:{
				"pageNo":pageNo,
				"pageSize":pageSize,
				"name":$.trim($("#select-name").val()),
				"owner":$.trim($("#select-owner").val()),
				"startDate":$.trim($("#select-startDate").val()),
				"endDate":$.trim($("#select-endDate").val()),
			},

			success: function (date) {
				/*需要得到的列表数据:查询总记录条数和activity  date{
                                                            "pageCount":pageCount,
                                                            "databases":{ xxx }    }*/
				//更新列表
				var html="";
				$.each(date.dataList,function (i,n) {

					html+="<tr class='active'>";
					html+="<td>&nbsp;&nbsp;<input type='checkbox'  name='xz' value='"+n.id+"'/></td>";
					html+='<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
					html+="<td>"+n.owner+"</td>";											        //使用传统请求，并传入每条记录对应的id，因为需要用id来查询详细信息页detail.jsp 的信息
					html+="<td>"+n.startDate+"</td>";
					html+="<td>"+n.endDate+"</td>";
					html+="</tr>";


				})

				$("#tbody").html(html);
				//数据放完 开始结合分页查询 展现分页信息
				//计算总页数
				var totalPages=date.count%pageSize==0?date.count/pageSize:parseInt(date.count/pageSize)+1;
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: date.count, // 总记录条数

					visiblePageLinks: 10, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});
			}
		})

	}

</script>
</head>
<body>


<input type="hidden" id="hidden-name">
<input type="hidden" id="hidden-owner">
<input type="hidden" id="hidden-startDate">
<input type="hidden" id="hidden-endDate">

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form  id="fromadd" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-Owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-Owner">

								</select>
							</div>
                            <label for="create-Name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-Name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate">
							</div>
							<label for="create-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-Cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-Cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">

						<input type="hidden" id="update-id"> <%--update中的隐藏id--%>

						<div class="form-group">
							<label for="edit-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="update-owner">


								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="update-name">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label" >开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="update-startDate" >
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="update-endDate">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="update-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="update-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表123</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="select-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="select-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="select-startDate" value="xxxx-xx-xx"/>
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="select-endDate" value="xxxx-xx-xx">
				    </div>
				  </div>
				  
				  <button type="button" id="selectBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td>&nbsp;&nbsp;<input type="checkbox"  id="qx"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="tbody">

					</tbody>
				</table>
			</div>
			
			<div id="activityPage" > </div>
			
		</div>
		
	</div>
</body>
</html>