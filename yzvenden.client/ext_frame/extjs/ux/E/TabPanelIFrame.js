/**
 * Created by Administrator on 2016/11/28.
 */
Ext.define('Ext.ux.E.TabPanelIFrame',{
    extend : 'Ext.tab.Panel',
    xtype : 'EIFrameTabPanel',

    requires: [
        'Ext.ux.TabReorderer'
    ],
    items : [],
    border : true,
    frame : true,
    split : true,
    region : 'center',//默认布局

    defaults: {
        bodyPadding: 2,
        scrollable: true,
        closable: true
    },
    /**
     * 设置面版的初始打开页面
     * 值设置：[..]
     * 值1 : {title : 标题 , url : 完整的请求地址},...
     * 值2 : '标题','url','标题2','url2',....
     */
    _init : undefined,//初始打开的地址



    __childItems:[],
    __urlList: [],
    /**
     * 返回一个即将打开的面版的地址
     * @param title
     * @param url
     * @private
     */
    __openCfg : function(title,url){
    	if(url && url.indexOf('http://') == -1){
    		url = E.basePath + url;
    	}
        return {
            title : title || '',
            html : "<iframe src='" + (url || '') + "' frameborder='0' width='100%' height='100%' />"
        };
    },

    /**
     * 打开一个链接地址
     * @param title
     * @param url
     */
    open : function(title,url){
        if(!url || '' == url) return ;
        this.add(this.__openCfg(title,url));
        this.setActiveTab(this.items.length-1);
    },
    initComponent : function(){
        var _self = this;
        if(!_self.id) _self.id = E._randomId('iframe_');
        _self.__childItems.push(_self.id);
        if(_self._init){
            var isObj = typeof _self._init[0] == 'string';
            for(var i=0,len=_self._init.length;i<len;i++){
                if(isObj){
                    _self.items.push(_self.__openCfg(_self._init[i],_self._init[i+1]));
                    ++i;
                }else{
                    var o = _self._init[i];
                    _self.items.push(_self.__openCfg(o.title,o.url));
                    _self.activeTab = _self.items.length - 1;
                }
            }
        }
        _self.callParent();
    }
});
