/**
 * Created by Administrator on 2017/3/10.
 */
Ext.define('Ext.ux.E.CheckboxModel',{
    extend : 'Ext.selection.CheckboxModel',
    xtype : 'ECheckboxModel',

    onSelectChange: function() {
        this.callParent(arguments);
        if (!this.suspendChange) {
            this.updateHeaderState();
        }
        if(this._changeSelect){
            this._changeSelect(this.selected,this);
        }
    },


    _changeSelect : undefined,

    constructor : function(){
        this.callParent(arguments);
    }
});