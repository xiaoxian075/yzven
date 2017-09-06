/**
 * Created by Administrator on 2016/12/13.
 */


Ext.define('Ext.ux.E.Form1', {
    extend: 'Ext.panel.Panel',
    xtype: 'EForm1',
    requires: [
        'Ext.data.TreeStore',
        'Ext.layout.container.Accordion',
    ],
    titleCollapse: true,
    animate: true,
    activeOnTop: false,
    _expanded: true,
    initComponent : function () {
        var _self = this;
        var cfg = {
            layout: {
                type: 'accordion',
                titleCollapse: _self.titleCollapse,
                animate: _self.animate,
                activeOnTop: _self.activeOnTop
            },
            items: []
        }
        var _items = []
        // if(_self._store && _self._store.indexOf('http://') == -1){
        //     var url = _self._store;
        //     url = E._rootReqModel[url] ? E._rootReqModel[url] : E.basePath + url;
        //     _self._store = url;
        // }
        if(_self._store && _self._store instanceof  Array) {
           
            if(_self._storeReflect) {
                _self._store.forEach(function(val,index) {
                    if(val.parentId == 0) {
                        var indexId = val.id;
                        var childArr = []
                        function findChild(indexId) {
                            _self._store.forEach(function(childVal) {
                                if(childVal.parentId == indexId) {
                                    childArr.push(childVal);
                                    findChild(childVal.id)
                                }
                            })
                        }
                        findChild(indexId)
                        _items[index] = {}
                        _items[index].title =  val.text;
                        _items[index].layout = 'border';
                        _items[index].items = [{
                            xtype: 'ETree',
                            _store : childArr,
                            _expanded: _self._expanded,
                            _defaultExpanded: true,
                            collapsible: false,
                            _storeReflect: _self._storeReflect,
                            _click: _self._click
                        }]
                        
                        cfg.items.push(_items[index])
                    }
                })
            }
        }
        Object.assign(_self,cfg);
        _self.callParent();
    },

});