
$(document).ready(function() {

    $("#importFileView").click(function() {
        var scmServer = $("#scmServer").val();
        var scmFilePath = $("#scmFilePath").val();
        var scmFileRevision = $("#scmFileRevision").val();
        $("#importFileView").attr('href', scmServer + scmFileRevision + "@" + scmFilePath);

    });

    //Class based selector 
    $(".href-select").click(function() {
        var propName = $(this).text();
        var propVal = $(this).attr('itemprop');
        $("#newProperty").attr('readonly', true);
        $("#newProperty").val(propName);
        $("#newValue").val(propVal);

        $("#savePropertyBtn").hide();
        $("#updatePropertyBtn").show();
    });

    //Id based selector
    $("#addPropertyBtn").click(function() {
        $("#newProperty").attr('readonly', false);
        $("#updatePropertyBtn").hide();
        $("#savePropertyBtn").show();
    });
    $("#addNodeModalForm").validate({
    	errorClass: "label-error", 
    });
    $("#addPropertyModalForm").validate({
    	errorClass: "label-error", 
    });
});

var app = new Vue({
    el: '.app',
    data: {
        leafNodes: [],
        rightDatas: [],
        keyValue: {
            key:'',
            value:''
        },
        newNode:'',
        currentNode:'/',
        nodeNames:[]
    },
    mounted: function () {
        this.getNodeList();
    },
    methods:{
    	getNodeList:function(){
    		var _self = this;
            var parentPath=_self.$refs.parentPath.value;
            axiosUtils.get("/home/getChilds?zkPath="+parentPath)
                .then(function (res) {
                    if (res.data.success) {
                        _self.leafNodes = res.data.data;
                    }
                }).catch(function (reason) {
                    console.log(reason);
            });
            axiosUtils.post('/home/getKVs',{zkPath:parentPath})
                .then(function (res) {
                    if(res.data.success){
                        _self.rightDatas=res.data.data;
                    }
                })
                .catch(function (reason) {
                    console.log(reason);
                });
    	},
        addNode:function(){
            var _self=this;
            var flag = $("#addNodeModalForm").valid();
            if(flag){
            	var parentPath=_self.$refs.parentPath.value;
                var postData={
                    parentNode:parentPath,
                    node:_self.newNode
                };
                axiosUtils.post('/home/createNode',postData)
                    .then(function (res) {
                        if(res.data.success){
                        	$('#addNodeModal').modal('hide');
                            // 刷新节点列表
                            _self.getNodeList();
                            // 初始化
                            _self.newNode = "";
                            layer.msg('新增节点成功！', {icon: 1});  
                        }
                    })
                    .catch(function (reason) {
                        console.log(reason);
                    });
            }
        },
        addProperty:function () {
            var _self=this;
            var flag = $("#addPropertyModalForm").valid();
            if(flag){
            	var parentPath=_self.$refs.parentPath.value;
                var postData={
                    parentNode:parentPath,
                    key:_self.keyValue.key,
                    value:_self.keyValue.value
                }
                axiosUtils.post('/home/addParameter',postData)
                    .then(function(res){
                        if(res.data.success){
                        	$('#addPropertyModal').modal('hide');
                        	// 刷新节点列表
                            _self.getNodeList();
                            //初始化
                            _self.keyValue.key = "";
                            _self.keyValue.value = "";
                            layer.msg('新增属性成功！', {icon: 1});
                        }else{
                        	layer.alert(res.data.msg);
                        }
                    })
                    .catch(function (reason) {
                        console.log(reason);
                    })
            }
        },
        updateProperty:function(){
            alert('updateProperty');
        },
        delNodeBtn:function () {
        	var _self = this;
        	var nodeNamesLength = _self.nodeNames.length;
        	if(nodeNamesLength <= 0){
        		layer.alert("请选择要删除节点！");
        		return false;
        	} 
        	if(nodeNamesLength > 1){
        		layer.alert("只能删除一个节点");
        		return false;
        	} 
        	var postData={
        			nodeName:_self.nodeNames[0],
   	            }
	   		 axiosUtils.post('/home/deleteNode',postData)
	            .then(function(res){
	                if(res.data.success){
	                	// 刷新节点列表
                        _self.getNodeList();
                        // 清空选择过的节点
                        _self.nodeNames =[];
	               	 	layer.msg('删除属性成功！', {icon: 1});
	                }else{
	                	layer.alert(res.data.msg);
	                }
	            })
	            .catch(function (reason) {
	                console.log(reason);
	            })
        }
    }
});