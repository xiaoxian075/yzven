/**
 * Created by Administrator on 2016/12/13.
 */


Ext.define('Ext.ux.E.Radio', {
    extend: 'Ext.form.RadioGroup',
    xtype: 'ERadio',

    /**
     * 多选框组
     * _group : object[]
     *      数据格式1：
     *          [['label','val'],...]
     *      数据格式2：
     *          ['labelfield','valfield',{labelfield:'label',valuefield:'val'},...]
     *
     * value : object
     *      为多选框的默认选中值
     */
    _group : undefined,
    _default : '',//当未选中时的默认值
    _width : 16,//多选框的框的大小
    
    __radiogroup : true,//标识

    __minWidth : 0,//该组件需要的最小宽度，用来计算


    __childItems:[],


    getGroupValue : function(){
        return this.getValue();
    },
    _setValue : function(val){
        var name = this.name;
        var value = {};
        value[name] = val;
        this.setValue(value);
    },
    __groupParse : function(datas,vals){
        var _self = this;
        if(!datas instanceof Array) throw new Error('必须为数组');
        if(vals && vals instanceof Array){
            var obj = {};
            for(var k in vals){
                obj[vals[k]] = true;
            }
            vals = obj;
        }
        var first = datas[0];
        var items = [];
        if(typeof first == 'string'){//数据格式2，解析成数据格式1
            var valfield = datas[1];
            datas.splice(0,2);
            var newDatas = [];
            for(var i=0,len=datas.length;i<len;i++){
                var obj = datas[i];
                newDatas[i] = [obj[first],obj[valfield]];
            }
            datas = newDatas;
        }
        for(var i=0,len=datas.length;i<len;i++){
            var data = datas[i],
                label = data[0],
                val = data[1];
            _self.__minWidth += E._labelWidth(label) + _self._width;
            var cfg = {
                boxLabel : label,
                name : _self.name,
                inputValue : val
            };
            if(vals && (vals == val))
                cfg.checked = true;
            items.push(cfg);
        }
        return items;
    },
    initComponent : function () {
        var _self = this;
        if(!_self.id) _self.id = E._randomId('radio_');
        _self.__childItems.push(_self.id);
        if(_self._group || _self._items)
            _self.value = _self.value ? _self.value : ''
            _self.items = _self.__groupParse(_self._group || _self._items,_self.value.toString());
        delete _self.value;
        _self.width = _self.__minWidth + E._labelWidth(_self.fieldLabel) + 20;
        _self.getSubmitData = function(){//使单选一个多选框时的值是数组而不是字符串的配置
            return this.getValue() || this._default;
        }
        _self.callParent();
    }
});