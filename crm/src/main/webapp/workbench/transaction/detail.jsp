<%@ page import="java.util.Map" %>
<%@ page import="com.bjpowernode.crm.settings.domain.DicValue" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%@ page import="com.bjpowernode.crm.workbench.domain.Tran" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";

//取得数据字典类型为stage的所有字典值(List<DicValue>)
   List<DicValue> dicValueList= (List<DicValue>) application.getAttribute("stage");
//取得pmap 阶段和可能性的对应关系
	Map<String,String> pmap= (Map<String, String>) application.getAttribute("pmap");
//根据pmap准备其中的key集合
	Set<String> keys = pmap.keySet();



//取得 正常阶段 和 丢失阶段  的分界点下标(可能性为0代表到分界线)

int fenjiedian=0; //提前准备数据库表中的分界点，这个是为了动态得到分界点 ,在更变图标的changeIcon方法用到
    for (int i = 0; i <dicValueList.size() ; i++) {
        DicValue dicValue = dicValueList.get(i);
        String stages = dicValue.getValue(); //得到每一个stage,为了拿到可能性
        String currtkenengxing = pmap.get(stages); //得到所有每一组stage的可能性
        if ("0%".equals(currtkenengxing)){ //可能性为0时，算作分界点
            fenjiedian = i;
            break;//必须break终止循环
        }
    }

		/*for (DicValue dicValue:dicValueList) {
			String stages = dicValue.getValue();//循环得到数据字典stages

				String kenengxing = pmap.get(stages);//用stages得到可能性
				fenjiedian++;
				if ("0".equals(kenengxing)){
					break;
				}
		}*/



%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<base href="<%=basePath%>">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />

<style type="text/css">
.mystage{
	font-size: 20px;
	vertical-align: middle;
	cursor: pointer;
}
.closingDate{
	font-size : 15px;
	cursor: pointer;
	vertical-align: middle;
}
</style>
	
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

<script type="text/javascript">

	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;
	
	$(function(){
		$("#remark").focus(function(){
			if(cancelAndSaveBtnDefault){
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","130px");
				//显示
				$("#cancelAndSaveBtn").show("2000");
				cancelAndSaveBtnDefault = false;
			}
		});
		
		$("#cancelBtn").click(function(){
			//显示
			$("#cancelAndSaveBtn").hide();
			//设置remarkDiv的高度为130px
			$("#remarkDiv").css("height","90px");
			cancelAndSaveBtnDefault = true;
		});
		
		$(".remarkDiv").mouseover(function(){
			$(this).children("div").children("div").show();
		});
		
		$(".remarkDiv").mouseout(function(){
			$(this).children("div").children("div").hide();
		});
		
		$(".myHref").mouseover(function(){
			$(this).children("span").css("color","red");
		});
		
		$(".myHref").mouseout(function(){
			$(this).children("span").css("color","#E6E6E6");
		});
		
		
		//阶段提示框
		$(".mystage").popover({
            trigger:'manual',
            placement : 'bottom',
            html: 'true',
            animation: false
        }).on("mouseenter", function () {
                    var _this = this;
                    $(this).popover("show");
                    $(this).siblings(".popover").on("mouseleave", function () {
                        $(_this).popover('hide');
                    });
                }).on("mouseleave", function () {
                    var _this = this;
                    setTimeout(function () {
                        if (!$(".popover:hover").length) {
                            $(_this).popover("hide")
                        }
                    }, 100);
                });

		showTranHistory();
	});
	
	function showTranHistory() {

		$.ajax({
			url:"workbench/transaction/showTranHistory.do",
			type:"get",
			dataType:"json",
			data:{
				"tranid":"${tran.id}"
			},
			success:function (date) {
				var html="";
				$.each(date,function (i,n) {
				    html+='<tr>';
					html+='<td>'+n.stage+'</td>';
					html+='<td>'+n.money+'</td>';
					html+='<td>'+n.kenengxing+'</td>';
					html+='<td>'+n.expectedDate+'</td>';
					html+='<td>'+n.createTime+'</td>';
					html+='<td>'+n.createBy+'</td>';
					html+='</tr>';
				})
				$("#TranTbody").html(html);
			}
		})

	}


	function changeStage(stage,i) { //stage需要改变的阶段，i需要改变的阶段对应的下标
        $.ajax({
            url:"workbench/transaction/changeStage.do", //改变阶段
            type:"get",
            dataType:"json",
            data:{
                "stage":stage,
                "id":"${tran.id}",
                "money":"${tran.money}",
                "expectedDate":"${tran.expectedDate}"
            },
            success:function (date) {

//{"success":true/false , "t":{stage,kenengxing,editBy,editTime} }
                if(date.success){
                $("#stage").html(date.t.stage);
                $("#editBy").html(date.t.editBy);
                $("#editTime").html(date.t.editTime);
                $("#kenengxing").html(date.t.kenengxing);
                    showTranHistory();
                    changeIcon(stage,i);
                }else{

                        alert("修改阶段失败")

                }

            }
        })
    }

function changeIcon(stage,i){  //拿到当前需要的阶段信息
	var Nowstage=stage; //拿到当前阶段
	var NowKengxing = $("#kenengxing").html();  //拿到当前阶段对应的可能性
	var index=i; //拿到当前阶段的下标
	var point="<%=fenjiedian%>";


	if (NowKengxing=="0%"){  //1.0大if  如果为0，前面7个绝对全部黑圈 ，后面的肯定一个黑叉 一个红叉

		for (var i = 0; i <point; i++) {//前七个
			//黑圈
			$("#"+i).removeClass();
			$("#"+i).addClass("glyphicon glyphicon-record mystage");
			$("#"+i).css("color","black");
		}
		for (var i =point; i < <%=dicValueList.size()%> ; i++) { //后两个
			if (i==index){
				//红叉
				$("#"+i).removeClass();
				$("#"+i).addClass("glyphicon glyphicon-remove mystage");
				$("#"+i).css("color","red");

			}else {
				//黑叉
				$("#"+i).removeClass();
				$("#"+i).addClass("glyphicon glyphicon-remove mystage");
				$("#"+i).css("color","black");
			}
		}

	} else { //2.0  大else  如果不为0，前面可能为: 绿圈 绿坐标 黑圈 最后两个肯定是黑叉

		for (var i = 0; i <point; i++) {


			if (i==index){ //绿坐标

				$("#"+i).removeClass();
				$("#"+i).addClass("glyphicon glyphicon-map-marker mystage");
				$("#"+i).css("color","#90F790");

			} else if(i<index){ //绿圈
				$("#"+i).removeClass();
				$("#"+i).addClass("glyphicon glyphicon-ok-circle mystage");
				$("#"+i).css("color","#90F790");
			}else { //黑圈

				$("#"+i).removeClass();
				$("#"+i).addClass("glyphicon glyphicon-record mystage");
				$("#"+i).css("color","black");

			}
		}


	}

}


</script>

</head>
<body>
	
	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>
	
	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>${tran.customerId}-${tran.name}<small>￥${tran.money}</small></h3>
		</div>
		<div style="position: relative; height: 50px; width: 250px;  top: -72px; left: 700px;">
			<button type="button" class="btn btn-default" onclick="window.location.href='edit.html';"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
			<button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>

	<!-- 阶段状态 -->
	<div style="position: relative; left: 40px; top: -50px;">
		阶段&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

		<%
			//这里必须用jsp脚本取 stage及对应的可能性,动态赋给每个图标
		      //取得当前阶段stage，从request域中取tran  (EL可以直接${tran.stage},但jsp脚本不行)
		       Tran tran = (Tran) request.getAttribute("tran");
			   String currenteTranstage = tran.getStage();
			//准备当前阶段的可能性
			String currenteTrankenengxing = pmap.get(currenteTranstage);


			if ("0%".equals(currenteTrankenengxing)){
			    //1.0大if  如果为0，前面7个绝对全部黑圈 ，后面的肯定一个黑叉 一个红叉

				for (int i = 0; i <dicValueList.size() ; i++) {//根据当前环境下(前面7黑，后面黑/红)，遍历所有可能性 可以对其分离
					DicValue dicValue = dicValueList.get(i);//循环得到数据字典stages
					String stage = dicValue.getValue();
					String kenengxing = pmap.get(stage);//用stages得到可能性

                        if ("0%".equals(kenengxing)){//1.1 进一步产生分界点，对前后分离处理
                            //1.2后部分(可能黑，可能红，但当前阶段一定为红
                             if (stage.equals(currenteTranstage)){  //得出当前阶段的状态


                                 //红叉
                                 %> <%--产生分界线，写html代码--%>

               <span id="<%=i%>" onclick="changeStage('<%=stage%>','<%=i%>')" <%--这个方法是因为图标是可以点的，要传需要改变的阶段和对应下标，来改变交易阶段--%>
              class="glyphicon glyphicon-remove mystage"  data-toggle="popover" data-placement="bottom"
                     data-content="<%=dicValue.getText()%>" style="color: red;"></span>
                       -----------
                                <%


                             }else {
                                 //黑叉

                                %> <%--产生分界线，写html代码--%>
        <span id="<%=i%>" onclick="changeStage('<%=stage%>','<%=i%>')" <%--这个方法是因为图标是可以点的，要传需要改变的阶段和对应下标，来改变交易阶段--%>
              class="glyphicon glyphicon-remove mystage"  data-toggle="popover" data-placement="bottom"
              data-content="<%=dicValue.getText()%>" style="color:black;"></span>
        -----------
        <%

                             }

                        }else {
                            //1.3前部分(全部黑圈)

        %> <%--产生分界线，写html代码--%>
        <span id="<%=i%>" onclick="changeStage('<%=stage%>','<%=i%>')" <%--这个方法是因为图标是可以点的，要传需要改变的阶段和对应下标，来改变交易阶段--%>
              class="glyphicon glyphicon-record mystage"  data-toggle="popover" data-placement="bottom"
              data-content="<%=dicValue.getText()%>" style="color: black;"></span>
        -----------
        <%

                        }

                }


			}
//-----------------------------------------------------------------------------------------------------------------------
			else {//2.0  大else  如果不为0，前面可能为: 绿圈 绿坐标 黑圈 最后两个肯定是黑叉

                int index=0; //当前阶段的下标，用于区分前半部分，因为有三个分支
							for (int i = 0; i < dicValueList.size(); i++) {
								DicValue dicValue = dicValueList.get(i);
								String stage=dicValue.getValue();
								if (stage.equals(currenteTranstage))  //获取当前stage的下标
                    {
                        index=i;
                        break;
                    }


                }
//根据当前环境，遍历所有可能性 可以对其分离
				for (int i = 0; i < dicValueList.size(); i++) {
					DicValue dicValue = dicValueList.get(i);
					String stage=dicValue.getValue();
				    String kenengxing=pmap.get(stage);

                    if ("0%".equals(kenengxing)){ //2.1 进一步产生分界点，对前后分离处理  两个一定黑叉

                        //2.2黑叉
        %> <%--产生分界线，写html代码--%>
        <span id="<%=i%>" onclick="changeStage('<%=stage%>','<%=i%>')" <%--这个方法是因为图标是可以点的，要传需要改变的阶段和对应下标，来改变交易阶段--%>
              class="glyphicon glyphicon-remove mystage"  data-toggle="popover" data-placement="bottom"
              data-content="<%=dicValue.getText()%>" style="color: black;"></span>
        -----------
        <%
                        //黑叉

                    }else {
                        //2.3可能绿圈 /绿坐标 /黑圈
                        if (i==index){ // 2.3.0如果i遍历到当前阶段，一定是绿坐标

                            //绿坐标

        %> <%--产生分界线，写html代码--%>
        <span id="<%=i%>" onclick="changeStage('<%=stage%>','<%=i%>')" <%--这个方法是因为图标是可以点的，要传需要改变的阶段和对应下标，来改变交易阶段--%>
              class="glyphicon glyphicon-map-marker mystage"  data-toggle="popover" data-placement="bottom"
              data-content="<%=dicValue.getText()%>" style="color: #90F790;"></span>
        -----------
        <%

                        }else if(i<index){//2.3.1如果i小于当前阶段，说明是左边，绿圈

                            //绿圈
        %> <%--产生分界线，写html代码--%>
        <span id="<%=i%>" onclick="changeStage('<%=stage%>','<%=i%>')" <%--这个方法是因为图标是可以点的，要传需要改变的阶段和对应下标，来改变交易阶段--%>
              class="glyphicon glyphicon-ok-circle mystage"  data-toggle="popover" data-placement="bottom"
              data-content="<%=dicValue.getText()%>" style="color: #90F790;"></span>
        -----------
        <%

                        }else { //2.3.2如果i大于当前阶段，说明在右边 黑圈

                            //黑圈
        %> <%--产生分界线，写html代码--%>
        <span id="<%=i%>" onclick="changeStage('<%=stage%>','<%=i%>')" <%--这个方法是因为图标是可以点的，要传需要改变的阶段和对应下标，来改变交易阶段--%>
              class="glyphicon glyphicon-record mystage"  data-toggle="popover" data-placement="bottom"
              data-content="<%=dicValue.getText()%>" style="color: black;"></span>
        -----------

        <%

                        }

                    }

                }
			}


		%>
		<%--<span class="glyphicon glyphicon-ok-circle mystage" data-toggle="popover" data-placement="bottom" data-content="资质审查" style="color: #90F790;"></span>
		-----------
		<span class="glyphicon glyphicon-ok-circle mystage" data-toggle="popover" data-placement="bottom" data-content="需求分析" style="color: #90F790;"></span>
		-----------
		<span class="glyphicon glyphicon-ok-circle mystage" data-toggle="popover" data-placement="bottom" data-content="价值建议" style="color: #90F790;"></span>
		-----------
		<span class="glyphicon glyphicon-ok-circle mystage" data-toggle="popover" data-placement="bottom" data-content="确定决策者" style="color: #90F790;"></span>
		-----------
		<span class="glyphicon glyphicon-map-marker mystage" data-toggle="popover" data-placement="bottom" data-content="提案/报价" style="color: #90F790;"></span>
		-----------
		<span class="glyphicon glyphicon-record mystage" data-toggle="popover" data-placement="bottom" data-content="谈判/复审"></span>
		-----------
		<span class="glyphicon glyphicon-record mystage" data-toggle="popover" data-placement="bottom" data-content="成交"></span>
		-----------
		<span class="glyphicon glyphicon-record mystage" data-toggle="popover" data-placement="bottom" data-content="丢失的线索"></span>
		-----------
		<span class="glyphicon glyphicon-record mystage" data-toggle="popover" data-placement="bottom" data-content="因竞争丢失关闭"></span>
		-------------%>
		<span class="closingDate">${tran.expectedDate}</span>
		 							<%--预计成交日期！！！--%>
	</div>
	
	<!-- 详细信息 -->
	<div style="position: relative; top: 0px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.owner}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">金额</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${tran.money}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.customerId}-${tran.name}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">预计成交日期</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${tran.expectedDate}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">客户名称</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.customerId}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">阶段</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b id="stage">${tran.stage}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">类型</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.type}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">可能性</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b id="kenengxing">${tran.kenengxing}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">来源</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.source}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">市场活动源</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${tran.activityId}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">联系人名称</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${tran.contactsId}</b></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 60px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${tran.createBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${tran.createTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 70px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b id="editBy">${tran.editBy}&nbsp;&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;" id="editTime">${tran.editTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 80px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${tran.description}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 90px;">
			<div style="width: 300px; color: gray;">联系纪要</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${tran.contactSummary}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 100px;">
			<div style="width: 300px; color: gray;">下次联系时间</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${tran.nextContactTime}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
	</div>
	
	<!-- 备注 -->
	<div style="position: relative; top: 100px; left: 40px;">
		<div class="page-header">
			<h4>备注</h4>
		</div>
		
		<!-- 备注1 -->
		<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>哎呦！</h5>
				<font color="gray">交易</font> <font color="gray">-</font> <b>动力节点-交易01</b> <small style="color: gray;"> 2017-01-22 10:10:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>
		
		<!-- 备注2 -->
		<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>呵呵！</h5>
				<font color="gray">交易</font> <font color="gray">-</font> <b>动力节点-交易01</b> <small style="color: gray;"> 2017-01-22 10:20:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>
		
		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button type="button" class="btn btn-primary">保存</button>
				</p>
			</form>
		</div>
	</div>
	
	<!-- 阶段历史 -->
	<div>
		<div style="position: relative; top: 100px; left: 40px;">
			<div class="page-header">
				<h4>阶段历史</h4>
			</div>
			<div style="position: relative;top: 0px;">
				<table id="activityTable" class="table table-hover" style="width: 900px;">
					<thead>
						<tr style="color: #B3B3B3;">
							<td>阶段</td>
							<td>金额</td>
							<td>可能性</td>
							<td>预计成交日期</td>
							<td>创建时间</td>
							<td>创建人</td>
						</tr>
					</thead>
					<tbody id="TranTbody">
						<%--<tr>
							<td>资质审查</td>
							<td>5,000</td>
							<td>10</td>
							<td>2017-02-07</td>
							<td>2016-10-10 10:10:10</td>
							<td>zhangsan</td>
						</tr>
						<tr>
							<td>需求分析</td>
							<td>5,000</td>
							<td>20</td>
							<td>2017-02-07</td>
							<td>2016-10-20 10:10:10</td>
							<td>zhangsan</td>
						</tr>
						<tr>
							<td>谈判/复审</td>
							<td>5,000</td>
							<td>90</td>
							<td>2017-02-07</td>
							<td>2017-02-09 10:10:10</td>
							<td>zhangsan</td>
						</tr>--%>
					</tbody>
				</table>
			</div>
			
		</div>
	</div>
	
	<div style="height: 200px;"></div>
	
</body>
</html>