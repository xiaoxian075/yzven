/**
 * Created by Administrator on 2016/12/15.
 */
Ext.define('Ext.ux.E.Img', {
    extend: 'Ext.panel.Panel',
    xtype: 'EImg',

    requires : ['Ext.button.Button','Ext.form.field.File'],

    _url : undefined,//初始地址
    _defalutIsNoImg : true,//默认显示图片为：暂无图片,设置为false时，会显示 + 号图片
    _handler : undefined,//图片单击事件
    _filefield : undefined,//绑定图片上传功能

    setSrc : function(url){
        this.__img.getEl().dom.src = url;
    },
    padding : 0,
    margin : 5,
    border : 1,
    width : 100,//默认宽
    height : 120,//默认高
    layout : 'fit',

    __childItems:[],
    __img  : undefined,//实例化的图片Dom
    initComponent : function(){
        var _self = this,items=[],defaultSrc;
        if(!_self.id) _self.id = E._randomId('img_');
        _self.__childItems.push(_self.id);
        _self.width = _self._width || 100;
        if(_self._filefield){
            _self._defalutIsNoImg = false;
            defaultSrc = _self._url || E._icon('img_upload_add');
            _self.handler = function(thisBtn){
                var file = thisBtn._self.__file;
                file.button.getEl().dom.childNodes[1].click();
            };
            _self.__file = Ext.create('Ext.form.field.File',{
                _self : _self,
                hidden : true,
                name : _self._filefield,
                listeners : {
                    change : function(thisField,newVal,oldVal){
                        var img = thisField._self;
                        var file = thisField.fileInputEl.dom.files.item(0);
                        var src = window.URL.createObjectURL(file);
                        img.setSrc(src);
                    }
                }
            });
            items.push(_self.__file);
        }else{
            defaultSrc = _self._url || E._icon(_self._defalutIsNoImg ? 'img_default' : 'img_upload_add');
        }
        var cfg = {
            width : _self.width - 6,
            minHeight : _self.height - 6,
            border : 0,padding:0,margin:0,
            style : {
                backgroundColor : '#fff'
            },
            autoEl : {
                tag: 'img',
                src: defaultSrc
            }
        };
        cfg.listeners = {
            mouseover : function(thisBtn){
                thisBtn.getEl().dom.style.cursor = 'default';
            }
        };
        if(_self.handler){
            cfg.handler = _self.handler;
            delete cfg.listeners.mouseover;
        }
        cfg._self = _self;
        _self.__img = Ext.create('Ext.button.Button',cfg);
        items.push({
            xtype : 'panel',
            padding : 2,margin:0,
            items : [_self.__img]
        });
        _self.items = items;

        _self.callParent();
    }

});