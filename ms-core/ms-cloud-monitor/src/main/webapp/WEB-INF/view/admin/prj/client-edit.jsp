<%@page import="com.module.admin.sys.enums.SysFileType"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/inc/sys.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑项目客户端</title>
<jsp:include page="/WEB-INF/view/inc/css.jsp"></jsp:include>
</head>
<body class="cld-body">
	<div class="enter-panel ep-xs">
		<input type="hidden" id="prjId" value="${param.prjId}">
		<div class="form-group">
			<label for="clientId" class="col-sm-4">客户端 <span class="text-danger">*</span></label>
			<div class="col-sm-8"><my:select id="clientId" items="${cliInfos}" value="${prjClient.clientId}" cssCls="form-control" /></div>
		</div>
		<div class="form-group">
			<label for="logPath" class="col-sm-4">日志路径 <span class="text-danger">*</span></label>
			<div class="col-sm-8"><input type="text" class="form-control" id="logPath" placeholder="查看的日志路径" value="${prjClient.logPath}"></div>
		</div>
		<div class="text-right"><small><a href="javascript:info.loadLogPath()">加载上次使用路径</a></small></div>
		<hr/>
		<div class="form-group text-right">
			<span id="saveMsg" class="label label-danger"></span>
 			<div class="btn-group">
				<button type="button" id="saveBtn" class="btn btn-success enter-fn">保存</button>
			</div>
		</div>
	</div>

	<jsp:include page="/WEB-INF/view/inc/js.jsp"></jsp:include>
	<script type="text/javascript">
	var info = {
			loadLogPath: function() {
				JUtil.ajax({
					url : '${webroot}/prjClient/f-json/getLastByPrjIdClientId.shtml',
					data : {
						prjId: $('#prjId').val(),
						clientId: $('#clientId').val()
					},
					success : function(json) {
						if (json.code === 0) {
							$('#logPath').val(json.body.logPath);
						}
						else if (json.code === -1)
							parent.message(JUtil.msg.ajaxErr);
						else
							parent.message(json.message);
					}
				});
			}
	};
	$(function() {
		$('#saveBtn').click(function() {
			var _saveMsg = $('#saveMsg').empty();
			
			var _saveBtn = $('#saveBtn');
			var _orgVal = _saveBtn.html();
			_saveBtn.attr('disabled', 'disabled').html('保存中...');
			JUtil.ajax({
				url : '${webroot}/prjClient/f-json/save.shtml',
				data : {
					prjId: $('#prjId').val(),
					clientId: $('#clientId').val(),
					version: '${param.version}',
					logPath: $('#logPath').val()
				},
				success : function(json) {
					if (json.code === 0) {
						_saveMsg.attr('class', 'label label-success').append('保存成功');
						setTimeout(function() {
							parent.client.loadInfo();
							parent.dialog.close();
						}, 800);
					}
					else if (json.code === -1)
						_saveMsg.append(JUtil.msg.ajaxErr);
					else
						_saveMsg.append(json.message);
					_saveBtn.removeAttr('disabled').html(_orgVal);
				}
			});
		});
		/* $('#queryBtn').click(function() {
			JUtil.ajax({
				url : '${webroot}/cliInfo/f-json/find.shtml',
				data : {
					searchString: $('#searchString').val(), size: 5
				},
				success : function(json) {
					if (json.code === 0) {
						var _panel = $('#listPanel').empty();
						if(json.body.length > 0) {
							var _cont = ['<ul class="list-group">'];
							$.each(json.body, function(i, obj) {
								_cont.push('<li class="list-group-item">',
							             '<a class="badge" href="javascript:;" onclick="info.save(this, \'',obj.clientId,'\');">确定</a>',obj.ip,':',obj.port,'</li>');
							});
							_cont.push('</ul>');
							_panel.append(_cont.join(''));
						} else {
							_panel.append('<small class="text-success">未搜索到客户端</small>');
						}
					}
					else if (json.code === -1)
						message(JUtil.msg.ajaxErr);
					else
						message(json.message);
				}
			});
		});
		
		$('#queryBtn').click(); */
	});
	</script>
</body>
</html>