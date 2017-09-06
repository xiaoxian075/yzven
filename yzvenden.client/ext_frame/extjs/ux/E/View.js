/**
 * Created by Administrator on 2016/11/28.
 */
Ext.define('Ext.ux.E.View',{
    extend : 'Ext.container.Viewport',
    xtype : 'EView',

    requires : [
        'Ext.ux.E.Tree',
        'Ext.ux.E.TabPanelIFrame',
        'Ext.ux.E.Grid',
        'Ext.ux.E.Form',
        'Ext.ux.E.LinkCombo'
    ],

    layout : 'border',
    items : [],
    _returnDoms : undefined,//如果配置了该方法，将会返回这个View所有的Items下面的所有元素
    
    __domIds : [],//创建的Id，转化成Dom对象
    __childItems:[],

    __domItems:{},
    __parseItems : function(items){
        if(!items) return ;
        var _self = this;

        for(var i=0,len=items.length;i<len;i++){
            var item = items[i];
            for(var key in item){
                var val = item[key];
                if(key == 'id' && typeof val == 'string'){
                    _self.__domIds.push(val);
                    continue;
                }else{
                    if(val && val['id'] && typeof val['id'] == 'string'){
                        _self.__domIds.push(val['id']);
                        continue;
                    }else if(val && val instanceof Array){
                        _self.__parseItems(val);
                    }
                }
            }//end for
            if(item.__childItems && item.__childItems instanceof Array && item.__childItems.length > 0){
            	_self.__domIds.concat(item.__childItems);
            }
        }
    },
    initComponent : function(){
        var _self = this;
        if(!_self.id) _self.id = E._randomId('view_');
        _self.__domIds.push(_self.id);
        _self.__parseItems(_self.items);
        if(!_self.listeners){
            _self.listeners = {};
        }
        _self.doAfter = _self.listeners.afterrender;
        _self.listeners.afterrender = function(thisCom,opts){
            for(var i=0,len=thisCom.__domIds.length;i<len;i++){
                var id = thisCom.__domIds[i];
                var dom = Ext.getCmp(id);
                thisCom.__domItems[id] = dom;
                if(dom && dom.__childItems && dom.__childItems.length > 0){
                    for(var j=0,jl=dom.__childItems.length;j<jl;j++){
                        var jid = dom.__childItems[j];
                        var jdom = Ext.getCmp(jid);
                        if(jdom){
                            thisCom.__domItems[jid] = jdom;
                        }
                    }
                }
            }
            if(thisCom._returnDoms)
                thisCom._returnDoms(thisCom.__domItems);
            if(thisCom.doAfter)
                thisCom.doAfter(thisCom,opts);
        };
        _self.callParent();
    },


});
