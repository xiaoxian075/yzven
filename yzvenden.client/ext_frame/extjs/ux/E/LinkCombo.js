/**
 * Created by Administrator on 2016/12/13.
 */


Ext.define('Ext.ux.E.LinkCombo', {
    extend: 'Ext.form.ComboBox',
    xtype: 'ELinkCombo',

    queryMode : 'local',
    forceSelection : true,//限制在列表中的值
    multiSelect : false,//多选

    value : undefined,//初始值

    __doDataConfig : function(store,records){
        if(!records) return ;
        if(store._storeCfg){
            if(store._storeCfg['_init']){
                store.insert(0,store._storeCfg['_init']);
            }
            if(store._storeCfg['_filter']){
                var k = store._self.valueField,v=store._self.displayField;
                for(var j=0;j<records.length;j++){
                    var data = records[j].data;
                    if(store._storeCfg['_filter'](data[k],data[v]) != true){
                        store.remove(records[j]);
                    }
                }
            }
            if(store._storeCfg['_append']){
                store.add(store._storeCfg['_append']);
            }
        }
        if(store.getCount()>0){
            var v = store._self.getValue();
            if(!v){
                var row = store.getAt(0);
                if(row){
                    v = row.data[0];
                    store._self.setValue(v);
                }
            }
        }
    },

    refreshData : function(datas,val){
        var _self = this;
        var store = _self.getStore();
        if(!datas){
            store.load();
        }else{
            store.loadData(datas);
        }
        if(typeof val != 'undefined'){
            _self.setValue(val);
        }
    },


    constructor : function(){
        var init = {
            /**
             * 数据源 Object[]
             * 数据格式1：
             * [[key,val],[key1,val1],...,{数据配置}]
             * 数据格式2：
             * 第0位表示要显示的key的字段名称
             * 第1位表示要显示的val的字段名称
             * ['keyname','valname',{key:val,key1:val1},....,{数据配置}]
             * 数据格式3：
             *['keyname','valname','ajax_request_url','post_param_name','可选，扩展请求的key',{数据配置}]
             * 第3位的post_param_name是必须的
             * 第4位表示下拉框的key值
             *
             *
             * 数据配置如下：
             * {
             *      _filter : function(key,val) 过滤，返回true表示要存在
             *      _init : Object[] 初始化数据，数据格式要与数据源一致，参与数据过滤
             *      _append : object[] 追加数据，数据格式要与数据源一致，不参与数据过滤
             * }
             */
            _store : [],
            _copyCombo : undefined,//从某个combo组件复制数据
            _config : undefined,//数据配置，后优先，即_store中的数据配置优先

            _linkLv : 1,//联动等级
            __childItems:[],

        };
        var _self = this;
        for(var k in init){
            _self[k] = init[k];
        }
        this.callParent(arguments);
    },

    initComponent : function () {
        var _self = this;
        if(_self.combo){
            for(var i in _self.combo){
                _self[i] = _self.combo[i];
            }
        }
        if(!_self.id) _self.id = E._randomId('combo_');
        _self.__childItems.push(_self.id);
        var store;
        if((store = _self._store)){
            if(!store instanceof Array) throw new Error('必须是个数组');
            var first = store[0],
                last,//最后的元素
                cfg;//数据源配置
            var len = store.length;
            if(typeof (last = store[len-1]) == 'object' && (last['_init'] || last['_append'] || last['_filter']))//最后一个元素是对象
                store.splice(--len,1);
            else
                last = _self._config;//最后的元素为非特定的元素

            var k,v;
            if(typeof first == 'string' && store[1] && typeof store[2] == 'string'){
                k = _self.valueField = store[0];
                v = _self.displayField = store[1];
                var url = store[2];
                if(url && url.indexOf('http://') == -1){
                    url = E._rootReqModel[url] ? E._rootReqModel[url] : E.basePath + url;
                }
                var query = last != store[3] && store[3] ? store[3] : undefined;
                if(query)
                    _self.queryParam = query;
                //异步加载
                _self.queryMode = 'local';//远程加载
                _self.minChars = _self.allowBlank == false ? 1 : 0;
                cfg = {
                    _self : _self,
                    xtype : 'store',
                    fields : [k,v],
                    _storeCfg : last,
                    _extParam : store[4],
                    _extParamVal : _self.value,
                    proxy : {
                        limitParam : undefined,
                        pageParam : undefined,
                        startParam : undefined,
                        type : E._request,
                        url : url
                    }
                };
                if(E._dataWrap){
                    cfg.proxy.reader = {
                        type : 'json',
                        root : 'data'
                    };
                };
            }else{
                //是个映射化的数组
                if(typeof first == 'string'){
                    k = _self.valueField = store[0];
                    v = _self.displayField = store[1];
                    store.splice(0,2);
                }else{//直接的数组
                    k = _self.valueField = '0';
                    v = _self.displayField = '1';
                }
                if(last){//最后的元素
                    if(last['_init']){//init参与filter事件
                        store = last['_init'].concat(store);
                    }
                    if(last['_filter']){
                        for(var j=0;j<store.length;){
                            if(last['_filter'](store[j][k],store[j][v]) != true){
                                store.splice(j,1);//删除元素
                                j=0;
                            }else{
                                j++;
                            }
                        }
                    }
                    if(last['_append']){//追加不参与过滤
                        store = store.concat(last['_append']);
                    }

                }
                cfg = {
                    _self : _self,
                    xtype : 'store',
                    fields : [k,v],
                    _storeCfg : last,
                    data : store
                };
            }
            cfg.listeners = {
                beforeload : function(store,opt){
                    if(store._extParam){
                        var proxy = store.getProxy();
                        proxy.setExtraParam(store._extParam,store._extParamVal);
                    }
                },
                load : function(store,records,opts){//远程数据加载时
                    //store._self.__doDataConfig(store,records);
                },
                refresh : function(store,opts){
                    store._self.__doDataConfig(store,store.getRange());
                }
            };
            _self.store = cfg;
        }
        if(!_self.listeners)
            _self.listeners ={};
        _self.listeners.change = function(field,newVal,oldVal,opts){
            var parent = field.up();
            var index = parent.linkLv || 1;
            if(index >= field._linkLv){
                return ;
            }
            parent.linkLv = index + 1;
            var id = field.id;
            var createId = id + "_lv" + index;
            var config = field.__self;
            config.id = createId;
            var com = Ext.create('Ext.ux.E.LinkCombo',config);
            parent.add(com);
        };

        _self.oldAfter = _self.listeners.afterrender;
        _self.listeners.afterrender = function(thisCom,opts){
            if(thisCom._copyCombo){//combo组件数据复制
                var combo = E[thisCom._copyCombo],cs;
                if(!combo){
                    combo = Ext.getCmp(thisCom._copyCombo);
                }
                if(combo && (cs = combo.getStore())){
                    thisCom.refreshData(cs.getRange());
                }
            }else{
                thisCom.getStore().load();
            }
            if(thisCom.oldAfter)
                thisCom.oldAfter(thisCom,opts);
        };
        _self.__self = _self;
        _self.callParent();
    }
});