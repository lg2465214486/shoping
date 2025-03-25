$(function () {

    $("#jqGrid").jqGrid({
        url: '/admin/daili/list',
        datatype: "json",
        colModel: [
            {label: 'id', name: 'adminUserId', index: 'userId', width: 50, key: true, hidden: true},
            {label: '昵称', name: 'loginUserName', index: 'nickName', width: 180},
            {label: '登录名', name: 'loginUserName', index: 'loginName', width: 120},
            {label: '密码', name: 'showPassword', index: 'showPassword', width: 120},
            {label: '邀请码', name: 'shareCode', index: 'shareCode', width: 120},
            {label: '身份状态', name: 'locked', index: 'locked', width: 60, formatter: lockedFormatter},
        ],
        postData: { loginName: ""},
        height: 560,
        rowNum: 10,
        rowList: [10, 20, 50],
        styleUI: 'Bootstrap',
        loadtext: '信息读取中...',
        rownumbers: false,
        rownumWidth: 20,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "data.list",
            page: "data.currPage",
            total: "data.totalPage",
            records: "data.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order",
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });

    $(window).resize(function () {
        $("#jqGrid").setGridWidth($(".card-body").width());
    });

    function lockedFormatter(cellvalue) {
        if (cellvalue == 1) {
            return "<button type=\"button\" class=\"btn btn-block btn-secondary btn-sm\" style=\"width: 50%;\">锁定</button>";
        } else if (cellvalue == 0) {
            return "<button type=\"button\" class=\"btn btn-block btn-success btn-sm\" style=\"width: 50%;\">正常</button>";
        }
    }

    function deletedFormatter(cellvalue) {
        if (cellvalue == 1) {
            return "<button type=\"button\" class=\"btn btn-block btn-secondary btn-sm\" style=\"width: 50%;\">注销</button>";
        } else if (cellvalue == 0) {
            return "<button type=\"button\" class=\"btn btn-block btn-success btn-sm\" style=\"width: 50%;\">正常</button>";
        }
    }
});

/**
 * jqGrid重新加载
 */
function reload() {
    var page = $("#jqGrid").jqGrid('getGridParam', 'page');
    $("#jqGrid").jqGrid('setGridParam', {
        postData: {loginName: $("#searchUserId").val()},
        page: page
    }).trigger("reloadGrid");
}

function addUser() {
    reset();
    $('.modal-title').html('');
    $('#indexConfigModal').modal('show');
}
function reset() {
    $("#loginName").val('');
    $("#password").val('');
    $('#edit-error-msg').css("display", "none");
}

//绑定modal上的保存按钮
$('#saveButton').click(function () {
    var loginName = $("#loginName").val();
    var password = $("#password").val();
    if (loginName === "" || password === "") {
        $('#edit-error-msg').css("display", "block");
        $('#edit-error-msg').html("请输入符合规范的配置项名称！");
    } else {
        var data = {
            "loginName": loginName,
            "password": password
        };
        var url = '/admin/daili/add';
        $.ajax({
            type: 'POST',//方法类型
            url: url,
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (result) {
                if (result.resultCode == 200) {
                    $('#indexConfigModal').modal('hide');
                    Swal.fire({
                        text: "保存成功",
                        icon: "success",iconColor:"#1d953f",
                    });
                    reload();
                } else {
                    $('#indexConfigModal').modal('hide');
                    Swal.fire({
                        text: result.message,
                        icon: "error",iconColor:"#f05b72",
                    });
                }
                ;
            },
            error: function () {
                Swal.fire({
                    text: "操作失败",
                    icon: "error",iconColor:"#f05b72",
                });
            }
        });
    }
});


function lockUser(lockStatus) {
    var ids = getSelectedRows();
    if (ids == null) {
        return;
    }
    if (lockStatus != 0 && lockStatus != 1) {
        Swal.fire({
            text: '非法操作',
            icon: "error",iconColor:"#f05b72",
        });
    }
    Swal.fire({
        title: "确认弹框",
        text: "确认要修改账号状态吗?",
        icon: "warning",iconColor:"#dea32c",
        showCancelButton: true,
        confirmButtonText: '确认',
        cancelButtonText: '取消'
    }).then((flag) => {
            if (flag.value) {
                $.ajax({
                    type: "POST",
                    url: "/admin/daili/lock/" + lockStatus,
                    contentType: "application/json",
                    data: JSON.stringify(ids),
                    success: function (r) {
                        if (r.resultCode == 200) {
                            Swal.fire({
                                text: "操作成功",
                                icon: "success",iconColor:"#1d953f",
                            });
                            $("#jqGrid").trigger("reloadGrid");
                        } else {
                            Swal.fire({
                                text: r.message,
                                icon: "error",iconColor:"#f05b72",
                            });
                        }
                    }
                });
            }
        }
    )
    ;
}