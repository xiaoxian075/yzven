/**
 * Created by Administrator on 2016/11/28.
 */
Ext.define('Ext.ux.E.Grid',{
    extend : 'Ext.grid.Panel',
    xtype : 'EGrid',


    requires:[
        'Ext.grid.column.Action',
        'Ext.grid.feature.Grouping',
        'Ext.data.ArrayStore',
        'Ext.ux.E.Toolbar',
        'Ext.grid.filters.Filters',
        'Ext.toolbar.Paging',
        'Ext.ux.E.CheckboxModel',
        'Ext.selection.RowModel'
    ],

    //stateful: true,
    multiSelect: true,
    multiColumnSort : true,//多列排序
    split : true,
    frame : true,
    border : true,
    hideHeaders:false,
    region : 'center',//默认布局
    viewConfig: {
        enableTextSelection: true
    },
    /**
     * 取得搜索栏的数据
     * @param [key string] 单独的Key，如果没有设置，表示取得全部
     */
    getSearch : function(key){
        return this._searchBar.getValue(key);
    },
    getSearchBar : function(){
        return this._searchBar;
    },
    /**
     * 取得搜索栏的位置元素
     * @param key   number / string 可以是位置的索引，从1开始算
     *                              也可是元素的name
     * @returns {*}
     */
    searchItem : function(key){
        return this._searchBar.getItem(key);
    },
    /**
     * 数据查找过滤
     * 如果_store是本地的数据源，则会对本地的数据进行过滤
     * 如果是异步加载，则会发送异步加载请求到服务端
     */
    search : function(){
        var _self = this;
        var store = _self.store;
        if(!_self.__proxy && !_self._paging){//限制本地的过滤只能在未分页的情况下
            //因为如果分页过滤 ，则会使分页栏的数据有异常！！所以，直接本地数据分页时，不产生过滤。
            //本地数据源,对数据进行过滤
            var query = _self.getSearch();
            store.clearFilter();
            store.filter({
                filterFn : function(item){
                    var data = item.data;
                    for(var k in query){
                        var v = query[k];
                        if(v && data[k] && v != data[k]) return false;
                    }
                    return true;
                }
            });
        }else{
            store._search = true;//自定义属性，搜索重置页面
            store.load();
        }
    },
    //绑定选中事件
    select : function(key){
        var rows = this.getSelectionModel().getSelection();
        if(rows.length == 0) return null;
        return key ? rows[0]['data'][key] : rows[0];
    },
    selections : function(key){
        var rows = this.getSelectionModel().getSelection();
        if(rows.length == 0) return null;
        rows.getValues = function(key){
            var keys = [];
            for(var i=0,len=rows.length;i<len;i++){
                keys[i] = key ? rows[i]['data'][key] : rows[i];
            }
            return keys;
        };
        return key ? rows.getValues(key) : rows;
    },

    loadData : function(data){
        this.getStore().loadData(data);
    },

    /**
     * 解析字段配置
     * @param fields
     * @private
     */
    __parseFields : function(fields,_self){//该方法用来可以被别的组件调用，所以要求信息由调用方传入
        _self.__modelFields = [];//重置元素
        if(!(fields instanceof Array)){
            throw new Error('必须为数组');
        }
        var columns = [];
        for(var i=0,len=fields.length;i<len;i++){
            var field = fields[i];
            for(var fieldName in field){
                var fieldParam = field[fieldName];
                if(fieldName == '_action'){
                    //事件按钮
                    var cfg = {
                        sortable : false,//不可排序
                        menuDisabled : true,
                        xtype : 'actioncolumn',
                        items : []
                    };
                    for(var k in field){
                        cfg[k] = field[k];
                    }
                    for(var i=0,len=fieldParam.length;i<len;i++){
                        var fp = fieldParam[i];
                        if(fp.icon){
                            if(fp.icon.indexOf('http') == -1)
                                fp.icon = E._icon(fp.icon);
                            delete fp.iconCls;
                        }
                        fp._handler = fp.handler;
                        fp.handler = function(grid,rowIndex,colIndex,thisFp){
                            var rec = grid.getStore().getAt(rowIndex);
                            thisFp._handler(rec.data);
                        };
                        cfg.items[i] = fp;
                    }
                    columns.push(cfg);
                    break ;
                }
                var modelField = {name : fieldName};
                var column = {dataIndex : fieldName};
                if(typeof fieldParam == 'string'){
                    column.text = fieldParam;
                }else{//是一个object对象
                    if(fieldParam.dateFormat){
                        column._format = fieldParam.dateFormat;
                        column.renderer = function(val,col){
                            var format = col.column._format;
                            if(!(val instanceof Date)){
                                if(9999999999 > val){
                                    val = new Date(val * 1000);
                                }else{
                                    val = new Date(val);
                                }
                            }
                            return Ext.Date.format(val,format);
                        }
                    }
                    if(fieldParam.ipFormat){//IP格式化
                        column.renderer = function(val,col){
                            if(!val) return "*";
                            var ip = Number(val);
                            return (ip>>>24) + '.' + ((ip&0x00FFFFFF)>>>16) + '.' + ((ip&0x0000FFFF)>>>8) + '.' + (ip&0x000000FF);
                        };
                    }
                    for(var k in fieldParam)
                        column[k] = fieldParam[k];//兼容Ext的源生用法
                }
                columns.push(column);
                _self.__modelFields.push(modelField);//放置Model的Fields
            }
        }
        return columns;
    },

    constructor : function(){
        var init = {
            plugins:undefined,//默认拥有该控件，是用来配置filter的时的插件，如果重写plugins，必须拥有该配置，如果没有，则filter不作用
            /**
             * 条件搜索
             */
            _search:undefined,
            /**
             * 搜索栏绑定，
             * 配置_search时失效
             * string / dom
             * 当为string时表示为dom元素的Id
             */
            _searchBind : undefined,
            /**
             * 字段的配置
             * 如果未配置该字段且未配置 columns(源生的字段配置） 则会抛出异常
             * 配置格式：
             * fieldName : string/object
             * fieldName 为对应的Model的字段名称，比如id
             * 属性可为 string 表示表格中显示的标题
             * 为object
             * {
             *      text : 表格中显示的标题
             *      dateFormat : 时间格式化，表明该字段为日期(date)
             *      filter : string 过滤，number,string,boolean,date
             *      其他参数请参考Ext的源生配置
             * }
             * 特殊的fieldName :
             *          _action : Array 按钮事件
             *             [{
             *                  icon : 图标地址，如果不是http开头的完整地址，则会匹配框架下的images图片，该字段则为图片名称，比如add
             *                  需要注意的是，如果配置了该属性，则iconCls属性会失效。
             *                  handler : function '按钮事件',单击时会返回该行对应的数据
             *                  [iconCls] : string '按钮图标',
             *                  [tooltip] : string '按钮提示'
             *             }]
             */
            _fields : undefined,
            _store : undefined,
            /**
             * 分组
             * string 分组的字段名称
             *
             */
            _group : undefined,
            _rowSelect : undefined,//行选中事件
            _checkbox : undefined,//多选框
            _rowexpander : false,//行展开
            /**
             * 分页设置 boolean / object
             * 如果_store为本地数据，则无视该属性
             * 如果为 false 则不使用分页，如果为 true 则默认配置为：
             * {
             *      pageSize : 100,//分页大小
             *      datas  : 'datas',//数据的字段名称，其值为对应的数据数组
             *      totalCount : 'totalCount',//数据的总长度
             *      limit : 'limit',//发送请求的时候，分页的大小的参数名称，默认limit，如果不需要，可设置为false
             *      start : 'start',//发送请求的时候，开始条数的参数名称，默认start,如果不需要发送，可设置为 false
             *      page  : 'page',//发送请求的时候，当前页码的参数名称，默认page,如果不需要发送，可设置为false
             * }
             * 以上配置均可重写，比如：
             * _paging : {pageSize:1},重写pageSize，并且启用分页
             */
            _paging : false,
            _autoLoad : false,//是否自动加载
            _afterLoad : undefined,//加载完成事件

            /**
             * 私有属性
             * {
             *   name : 字段名称
             *   type : 字段类型，默认 auto (auto/string/int/float/boolean/date)
             * }
             */
            __modelFields : [],
            __proxy : false,//标题数据的来源是否为异步加载
            __pageSize : 100,//默认的分页参数

            __childItems:[]
        };
        var _self = this;
        for(var k in init){
            _self[k] = init[k];
        }
        this.callParent(arguments);
    },

    //初始化
    initComponent : function(){
        var _self = this;
        if(!_self.id) _self.id = E._randomId('grid_');
        _self.__childItems.push(_self.id);
        if(_self._checkbox){
            _self.selType = 'checkboxmodel';
        }
        if(!_self.columns){//解析字段配置
            var fields = _self._fields;
            if(!fields)
                throw new Error('错误的表格配置，未配置表格的字段信息');
            _self.columns = _self.__parseFields(fields,_self);
        }
        if(!_self.features)
            _self.features = [];
        if(_self._group){//分组
            _self.features.push({
                ftype: 'grouping',
                groupHeaderTpl: '{columnName}: {name} 共{rows.length}条',
                hideGroupedHeader: true,
                startCollapsed: true
            });
        }
        if(!_self.plugins){
            _self.plugins = ['gridfilters'];
        }
        if(_self._rowexpander){//展开行
            _self.plugins.push({
                ptype: 'rowexpander',
                rowBodyTpl : new Ext.XTemplate(_self._rowexpander)
            });
        }

        if(_self._search){//搜索条件
            if(_self._search instanceof Array){
                var search = Ext.create('Ext.ux.E.Toolbar',{
                    id : _self.id + '_toolbar',
                    _items : _self._search,
                    border: false,
                });
                _self._searchBar = _self.tbar = search;
            }else{
                _self._searchBar = Ext.create('Ext.ux.E.Toolbar',_self._search);
            }
        }else if(_self._searchBind){
            if(typeof _self._searchBind == 'string'){
                _self._searchBar = {__justId:_self._searchBind};
            }else{
                _self._searchBar = _self._searchBind;
            }
        }

        if(_self._store){//根据字段的配置，自动化创建model和store
            var storeCfg = {
                fields : _self.__modelFields
            };
            if(_self._group){
                storeCfg.groupField = _self._group;
            }
            if(_self._store instanceof Array){
                var data = [];
                for(var i=0,len=_self._store.length;i<len;i++){
                    var row = _self._store[i];
                    var rowData = [];
                    for(var key in row){
                        rowData.push(row[key]);
                    }
                    data.push(rowData);
                }
                storeCfg.data = data;
            }else{//认为是一个远程加载地址
                if(typeof _self._store == 'function'){
                    _self._fnstore = _self._store;
                    _self._store = '';
                }
                _self.__proxy = true;//标识为异步加载
                storeCfg.autoLoad = false;//系统初始化时，再决定是否加载
                if(_self._store && _self._store.indexOf('http://') == -1){
                    _self._store = E._rootReqModel[_self._store] ? E._rootReqModel[_self._store] : E.basePath + _self._store;
                }
                storeCfg.proxy = {
                    type : E._request,
                    url : _self._store,
                    reader : E._reader.grid,
                    actionMethods: {
                        create : 'POST',
                        read   : 'POST', // by default GET
                        update : 'POST',
                        destroy: 'POST'
                    }
                };
                var paging;
                if((paging = _self._paging)){
                    //分页
                    if(paging == true)
                        paging = {};
                    storeCfg.remoteSort = paging.remoteSort || false;//远程已经排序好了，本地不在排序
                    storeCfg.pageSize = paging.pageSize || _self.__pageSize;
                    storeCfg.proxy.limitParam = paging.limit == false ? undefined : (paging.limit || 'limit');
                    storeCfg.proxy.pageParam = paging.page == false ? undefined : (paging.page || 'page');
                    storeCfg.proxy.startParam = paging.start == false ? undefined : (paging.start || 'start');
                    if(paging.datas || paging.totalCount){
                        storeCfg.proxy.reader = {
                            rootProperty : paging.datas || 'data',
                            totalProperty : paging.totalCount || 'totalCount'
                        }
                    }else{
                        storeCfg.proxy.reader = E._reader.gridPaging;
                    }
                }
                storeCfg._self = _self;
                storeCfg.listeners={
                    beforeload : function(store,operation,opts){
                        var _paging = store._self._paging;
                        if(_paging && (store._search || store.data.length < 1)){
                            store.currentPage = 1;
                            operation.setPage(1);
                            operation.setLimit(store.pageSize);
                            operation.setStart(0);
                            delete store._search;
                        }
                        var proxy = store.getProxy();
                        if(store._self._searchBar){
                            var search = store._self._searchBar.getValue();
                            if(search){
                                for(var k in search){
                                    proxy.setExtraParam(k,search[k]);
                                }
                            }
                        }
                    },
                    load : function(store,records,successful,opts){
                        var res = opts._response.responseText;
                        if(E._postAspect && !E._postAspect(res)) return ;
                        if(E._getAspect && !E._getAspect(res)) return ;
                        var afterLoad = store._self._afterLoad;
                        if(afterLoad){
                            afterLoad(store,records,successful,opts);
                        }
                    }
                }
            }
            var store = Ext.create('Ext.data.Store',storeCfg);
            _self.store = store;
        }


        if(_self.__proxy && _self._paging){//为本地数据源时，无视分页配置
            _self.__pagingBar = _self.bbar = Ext.create('Ext.PagingToolbar', {
                store: _self.store,
                displayInfo: true,
                displayMsg: '当前 {0} - {1} 共 {2} 条',
                emptyMsg: "暂无数据",
            });
        }
        if(!_self.listeners) _self.listeners = {};
        _self.oldAfter = _self.listeners.afterrender;
        _self.listeners.afterrender = function(thisCom,opts){
            if(this._searchBar && this._searchBar.__justId){
                this._searchBar = Ext.getCmp(this._searchBar.__justId);
            }
            if(this._autoLoad) this.store.load();
            if(this.oldAfter)
                this.oldAfter(thisCom,opts);
        };
        _self.__afterrender = function(dom,doms){
            var __self = this;
            if(__self._fnstore) {
                var url = _self._fnstore(doms);
                if(url && url.indexOf('http://') == -1){
                    url = E._rootReqModel[url] ? E._rootReqModel[url] : E.basePath + url;
                }
                var proxy = __self.getStore().getProxy();
                proxy.url = url;
                __self.getStore().setProxy(proxy);
                __self.getStore().load();
            }

        };
        //行选中事件
        _self._allSelected = false;
        var model = _self._checkbox ? 'Ext.ux.E.CheckboxModel' : 'Ext.selection.RowModel';
        _self.selModel = Ext.create(model,{
            _grid : _self,
            _changeSelect : function(selected,dom){
                var grid = dom._grid;
                var boxchange = grid._checkbox;
                if(typeof boxchange == 'function'){
                    selected.getValues = function(fieldName){
                        var len = this.length;
                        var items = this.items;
                        var vals = [];
                        for(var i=0;i<len;i++){
                            vals[i] = items[i].data[fieldName];
                        }
                        return vals;
                    };
                    selected.getDatas = function () {
                        var len = this.length;
                        var items = this.items;
                        var datas = [];
                        for(var i=0;i<len;i++){
                            datas[i]=items[i].data;
                        }
                        return datas;
                    };
                    selected.allSelected = function(){
                        var len = this.length;//选中的行
                        dom._grid._allSelected = len == dom._grid.store.data.length;
                        return dom._grid._allSelected;
                    };
                    boxchange(selected);
                }
            },
            listeners : {
                select : function(row,record,index,opts){
                    var g = this._grid;
                    if(g._rowSelect) g._rowSelect(record.data||record,index,record,row,g);
                }
            }
        });
       /* if(_self._checkbox){
            _self.selModel._changeSelect = function(selected){
                alert(1);
            }
        }*/
        _self.callParent();
    }
});
