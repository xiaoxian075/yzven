/**
 * Created by Administrator on 2016/11/30.
 */
Ext.define('Ext.ux.E.Tree',{
    extend : 'Ext.tree.Panel',
    xtype : 'ETree',
    requires: [
        'Ext.data.TreeStore'
    ],
    //-----S属性---------------------start
    rootVisible : false,//是否显示树根
    region : 'center',//默认布局
    autoHeight : true,
    layout : 'fit',
    width : 300,
    split : true,
    collapsible : true,//设置为true是panel具有可折叠功能并且有一个展开/折叠的切换按钮被添加到panel的标题头区域。设置为false则该panel的 大小是静态不可变的，后者被其自身的布局所管理，并且没有切换按钮。
    //-----S属性---------------------end

    //-----E属性---------------------start
    /*
    * 数据来源,string/Array
    * 如果为Array认为为树的数据，自动化创建数据源，否则会自动认为为异步加载Url地址，自动加载该地址的数据生成数据源
    * 数据格式：(异步请求加载返回或者Array均要遵循以下要求）
    * [{
    *   left : boolean,是否叶子，true为叶子，则会无视子节点的数据，false则该参数可要可不要
    *   text : string,节点的文本
    *   children : Array 子节点,子节点的数据格式也要求遵循以上规则
    * },....]
    * 生效条件： 不为undefined或者''   且    未设置store
    * */
    _store : undefined,
    /**
     * 数据源扩展： 反射配置  boolean/Object 为true时，使用默认的字段名称
     * 该配置是用来处理后端直接返回未组装成树所需要格式数据时，自动化反射的配置
     * 参数：
     * _text    : 数据中要显示树节点文本的字段名称,default : text
     * _id      : 数据中用来判断数据父子级关系的字段名称,default : id
     * _parent  : 数据中用来判断数据父子级关系的字段名称,default : parent
     * _expanded : 用来判断是否展开的字段名称，
     *                      值为1/0 1表示展表,default:expanded
     *                      true/false
     * 判断依据如下：
     * _parent = 0 或 undefined 则为最高父节点
     * 否则，判断会根据该字段取得的值，将该数据设置到对应_id的children中
     * 该树解释支持无限级树
     * 生效条件：
     *      当配置了_store并生效时
     * 注意：
     *      使用了该配置，则认为数据不是Ext的树的数据格式，将根据配置的参数进行解释
     */
    _storeReflect : undefined,
    _rootText : 'Tree Root',//树根的默认文件
    _expanded : true,//树根是否默认展开
    _defaultExpanded : false,//默认节点展开
    /**
     * 过滤功能 string/object 数据中要过滤的字段
     * string 表示要过滤的数据中的字段
     * object ｛
     *              key   :  要过滤的字段
     *              text :  显示的标题
     *          ｝
     */
    _filter : undefined,//过滤
    _click : undefined,//自定义点击事件
    __childItems:[],
    //-----E属性---------------------start



    __storeReflectParse : function(store_){
        var _self = this;
        if(!_self.id) _self.id = E._randomId('tree_');
        _self.__childItems.push(_self.id);
        var reflect = _self._storeReflect;//反射的配置
        if(reflect){
            var text,id,parentId;
            if(reflect == true){
                text = "text";
                id = "id";
                parentId = "parentId";
            }else{
                //对反射参数进行验证
                if((text = reflect.text) == undefined
                    || (id = reflect.id) == undefined
                    || (parentId = reflect.parent) == undefined)
                    throw new Error("反射参数配置错误");
            }
            return parseInfiniteTree(store_ || _self._store,text,id,parentId,reflect.expanded || 'expanded',_self);
        }
        return [];

        function parseInfiniteTree(data,textField,idField,parentField,expanded,_self){
            //无限树解释
            if(E._dataWrap && data[E._dataWrap]){
                data = data[E._dataWrap];
            }
            var tree = [];
            var len = data.length;
            for(var i=0;i<len;i++){
                var row = data[i];
                var rowId = row[idField];
                var rowParentId = row[parentField];
                var rowText = row[textField];
                var exp = row[expanded];
                if(exp == 1 || exp == true || 'true' == exp){
                    exp = true;
                }else if(_self._defaultExpanded){
                    exp = true;
                }
                if(rowParentId){
                    //子节点,需要查找父
                    var parent = findParent(tree,rowParentId);
                    if(!parent){//创建子节点时未发现父节点，则先创建父节点的基础属性
                        parent = {_id : rowParentId,leaf : false};
                        tree.push(parent);
                    }
                    if(!parent.children){parent.children = [];}
                    var child = {
                        _id : rowId,
                        _data : row,
                        text : rowText,
                        leaf : true
                    };
                    if(exp) child._expanded = true;
                    parent.children.push(child);
                }else{
                    //父节点
                    var node = findParent(tree,rowId);
                    if(!node){
                        node = {
                            _id : rowId,
                            _data : row,
                            text : rowText,
                            leaf : true
                        };
                        tree.push(node);
                    }else{
                        if(!node.text)
                            node.text = rowText;
                        if(!node._data)
                            node._data = row;
                    }
                    if(exp) node._expanded = true;
                }
            }
            return tree;

            function findParent(array,id){
                for(var i=0,len=array.length;i<len;i++){
                    var row = array[i];
                    if(row._id == id){
                        if(!row.children)
                            row.children = [];//初始化子节点
                        row.leaf = false;//有子节点，则不会是叶子
                        return row;
                    }
                    if(row.children){
                        row = findParent(row.children,id);
                        if(row) return row;
                    }
                }
                return null;
            }
        }
    },

    __expand : function(nodes){
        var exp = nodes.data._expanded,len;
        var text = nodes.data.text;
        if(exp == true){
            nodes.expand();
        }
        if(nodes.childNodes && (len = nodes.childNodes.length) > 0){
            for(var i=0;i<len;i++){
                this.__expand(nodes.childNodes[i]);
            }
        }
    },
    /**
     * 创建数据源
     * @returns {Ext.data.TreeStore}
     * @private
     */
    __createStore : function(){
        var _self = this;
        var _store = _self._store;
        var storeCfg = {
            root : {
                text : _self._rootText,
                expanded : _self._expanded
            }
        };
        if(_store instanceof  Array){
            if(_self._storeReflect){//配置了反射的参数，认为该数据为后台直接返回的数据，并转化成树的数据结构
                _store = _self.__storeReflectParse();
            }
            storeCfg.root.children = _store;
        }else{
        	if(_store && _store.indexOf('http://') == -1){
                var url = _self._store;
                url = E._rootReqModel[url] ? E._rootReqModel[url] : E.basePath + url;
        		_self._store = url;
        	}
            if(!_self._storeReflect){
                storeCfg.proxy = {
                    type : E._request,
                    url : _self._store
                };//这边的异步加载，要求root节点不可以有children初始化，否则会加载失败
            }else{
                storeCfg.root.children = [];
                //做完成的监听
                _self.oldAfter = _self.listeners.afterrender;
                _self.listeners={
                    afterrender : function(thisTree,opts){
                        thisTree.setLoading('Loading...');
                        if(thisTree.oldAfter)
                            thisTree.oldAfter(thisTree,opts);
                        //开始异常加载数据
                        Ext.Ajax.request({
                            url : thisTree._store,
                            success : function(response){
                                thisTree.setLoading(false);
                                var res = response.responseText;
                                if(E._postAspect && !E._postAspect(res)) return ;
                                if(E._getAspect && !E._getAspect(res)) return ;
                                if(res != ''){
                                    res = eval('('+res+')');
                                    var data = thisTree.__storeReflectParse(res);
                                    var root = thisTree.getStore().getRoot();
                                    root.removeAll();
                                    root.appendChild(data);
                                    root.eachChild(function(child){
                                        thisTree.__expand(child);
                                    });
                                }
                            }
                        });//end ajax
                    }
                }
            }
        }
        return Ext.create('Ext.data.TreeStore',storeCfg);
    },
    constructor : function(){
        this.callParent(arguments);
    },
    initComponent : function(){
        var _self = this;
        if(!_self.listeners)
            _self.listeners = {};
        if(_self._store && !_self.store){//数据源的解析
            _self.store = _self.__createStore();
        }

        //过滤
        if(_self._filter){

            var filterCfg = {
                //设置过滤栏目
                flex:1,
                xtype: 'triggerfield',
                triggerCls: 'x-form-clear-trigger',
                onTriggerClick: function() {
                    this.reset();
                },
                listeners : {
                    change : function(f,val){
                        var tree = this.up('treepanel'),v;//取得树
                        tree.store.clearFilter();
                        if(!val || '' == val){
                            return ;
                        }
                        try {
                            v = new RegExp(val, 'i');
                            Ext.suspendLayouts();
                            tree.store.filter({
                                filterFn : function(node){
                                    var children = node.childNodes,
                                        len = children && children.length,
                                        i,visible=false;
                                    if(_self._storeReflect){
                                        //使用的是反射，即查找节点下data中的_data的字段
                                        if(!node.data._data) return false;
                                        if(node.isLeaf()){
                                            var val = node.data._data[_self._filter];
                                            visible = v.test(val);
                                        }
                                    }else{
                                        //数组
                                        visible = v.test(node.data[_self._filter]);
                                    }
                                    for (i = 0; i < len && !(visible = children[i].get('visible')); i++);
                                    return visible;
                                }
                            });
                            Ext.resumeLayouts(true);
                        } catch (e) {
                            this.markInvalid('Invalid regular expression');
                        }
                    }
                }
            };
            if(typeof _self._filter == 'object'){
                filterCfg.fieldLabel = _self._filter.text || '';
                filterCfg.labelWidth = E._labelWidth(filterCfg.fieldLabel);
                _self._filter = _self._filter.key;
            }
            _self.tbar = [filterCfg]
        }

        if(_self._click){
            //如果已配置了源生的itemClick事件，则再次的配置该事件
            //调用顺序如下：
            //1.调用原来的配置
            //2.调用_click配置
            //即会调用二次事件
            _self.oldItemClick = _self.listeners.itemclick;
            _self.listeners.itemclick = function(thisView,record,item,index,e,opts){
                _self._click(record.data._data);
                if(_self.oldItemClick)
                    _self.oldItemClick();
            };
        }
        _self.callParent();
    }

});

