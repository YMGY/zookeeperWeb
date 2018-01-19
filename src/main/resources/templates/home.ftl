<#import "layouts/main-layout.ftl" as main>
<@main.page>

<div class="container">
    <!--Bread crumb-->
    <div class="row-fluid">
        <div class="col-md-12">

            <ul class="breadcrumb">
                <li><a href="/home/index?zkPath=${zkPath}" class="breadcrumb-select">${zkPath}</a></li>
            </ul>

        </div>
    </div>
    <input ref="parentPath" value="${zkPath}" type="hidden"/>
    <div class="row-fluid">
        <!--左边树结构-->
        <div class="col-md-3 well pre-scrollable scroll-pane">
            <span class="glyphicon glyphicon-folder-close"></span>
            <a href="/home/index?zkPath=${parentNode}"><..></a>
            <br/>
            <template v-for="node in leafNodes">
                <input type="checkbox" name="nodeChkGroup" v-model="nodeNames" :value="node.zkPath"/>
                <span class="glyphicon glyphicon-folder-close"></span>
                <a :href="'/home/index?zkPath='+node.zkPath">{{node.name}}</a>
                <br/>
            </template>

        </div>
        <!--右边表格-->
        <div class="col-md-9 pre-scrollable scroll-pane">
            <table class="table table-striped table-bordered wrap-table">
                <thead>
                <tr>
                    <th style="width: 4%"></th>
                    <th>Key</th>
                    <th>Value</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <template>
                    <tr v-for="item in rightDatas">
                        <td>
                            <input type="checkbox" name="propChkGroup" :value="item.key"/>
                        </td>
                        <td>
                            {{item.key}}
                        </td>
                        <td>
                            {{item.value}}
                        </td>
                        <td>
                            <button type="button" data-toggle="modal" data-target="#addPropertyModal">修改属性</button>
                        </td>
                    </tr>
                </template>
                </tbody>
            </table>
        </div>
    </div>

    <!--Modal Dialog-->
    <div class="row-fluid">
        <div class="col-md-12">
            <input type="hidden" name="currentPath" value=""/>
            <input type="hidden" name="displayPath" value=""/>
            <input type="hidden" id="delNodeLst" name="delNodeLst"/>
            <input type="hidden" id="delPropLst" name="delPropLst"/>

            <!-- Search Property Modal -->
            <div class="modal fade" id="searchModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h4 class="modal-title" id="myModalLabel">Search</h4>
                        </div>
                        <div class="modal-body">
                            <div class="input-group input-group-lg">
                                <span class="input-group-addon">Search</span>
                                <input type="text" id="search" name="searchStr" class="form-control">
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            <input type="submit" id="searchBtn" name="action" value="Search" class="btn btn-primary"/>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Add Node Modal -->
            <div class="modal fade" id="addNodeModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog">
                	<form id="addNodeModalForm" action="" method="post">
	                    <div class="modal-content">
	                        <div class="modal-header">
	                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                            <h4 class="modal-title" id="myModalLabel">新增节点</h4>
	                        </div>
	                        <div class="modal-body">
	                            <div class="input-group input-group-lg">
	                                <span class="input-group-addon">名称</span>
	                                <input type="text" required name="newNode" class="form-control" placeholder="节点名" v-model="newNode">
	                            </div>
	
	                        </div>
	                        <div class="modal-footer">
	                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	                            <button type="button" id="createNode" name="action" v-on:click="addNode();" class="btn btn-primary">保存</button>
	                        </div>
	                    </div>
                     </form>
                </div>
            </div>

            <!-- Add Property Modal -->
            <div class="modal fade" id="addPropertyModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog">
               		<form id="addPropertyModalForm" action="#" method="post">
	                    <div class="modal-content">
	                        <div class="modal-header">
	                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                            <h4 class="modal-title" id="myModalLabel">添加属性</h4>
	                        </div>
	                        <div class="modal-body">
	                            <div class="input-group input-group-lg">
	                                <span class="input-group-addon">Key</span>
	                                <input type="text" required  id="newProperty" name="newProperty" v-model="keyValue.key" class="form-control"
	                                       placeholder="name">
	                            </div>
	                            <br/>
	                            <div class="input-group input-group-lg">
	                                <span class="input-group-addon">Value</span>
	                                <textarea id="newValue" required  name="newValue" class="form-control" v-model="keyValue.value" placeholder="value"
	                                          style="resize:vertical;"></textarea>
	                            </div>
	                        </div>
	                        <div class="modal-footer">
	                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	                            <input type="button" id="savePropertyBtn"  name="action" value="保存属性" @click="addProperty"
	                                   class="btn btn-primary"/>
	                            <input type="button" id="updatePropertyBtn" data-dismiss="modal" name="action" value="更新属性" @click="updateProperty"
	                                   class="btn btn-primary"/>
	                        </div>
	                    </div>
	            	</form>
                </div>
            </div>

        </div>


    </div>
    <div class="row-fluid">
        <div class="col-md-12">
            <!-- Import Modal -->
            <form id="importForm" method="post" action="/import" enctype="multipart/form-data">
                <div class="modal fade" id="importModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                     aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;
                                </button>
                                <h4 class="modal-title" id="myModalLabel">Import</h4>
                            </div>
                            <div class="modal-body">

                                <div class="form-group">
                                    <div class="input-group input-group-lg">
                                        <span class="input-group-addon">Upload File</span>
                                        <div class="fileinput fileinput-new" data-provides="fileinput">
                                            <span class="btn btn-default btn-file">
                                                <input class="form-control" type="file" name="scmFile" size="30">
                                            </span>
                                        </div>
                                    </div>


                                    <br/>
                                    <h4 class="text-center text-primary">OR</h4>
                                    <br/>

                                    <div class="input-group input-group-lg">
                                        <span class="input-group-addon">File Path</span>
                                        <input type="hidden" id="scmServer" name="scmServer" value=""/>
                                        <input type="text" id="scmFilePath" name="scmFilePath" class="form-control"
                                               placeholder="">
                                    </div>
                                    <br/>
                                    <div class="input-group input-group-lg">
                                        <span class="input-group-addon">Revision</span>
                                        <input type="text" id="scmFileRevision" name="scmFileRevision"
                                               class="form-control" placeholder="head">
                                    </div>
                                    <br/>
                                    <div class="text-center">
                                        <a href="#" id="importFileView" target="_blank">View</a>
                                    </div>
                                </div>

                            </div>
                            <div class="modal-footer">
                                <div class="checkbox text-left">
                                    <label><input type="checkbox" name="scmOverwrite" value="true"> Overwrite Existing
                                        Properties</label>
                                </div>

                                <div>
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                    <input type="submit" id="importBtn" name="action" value="Import"
                                           class="btn btn-primary"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form>

        </div>
    </div>
</div>

</@main.page>
