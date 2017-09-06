Ext.define('Ext.ux.E.Month', {
    extend: 'Ext.form.field.Date',
    xtype: 'EMonth',
    requires: ['Ext.picker.Month'],
    alternateClassName: ['Ext.form.MonthField', 'Ext.form.Month'],
    selectMonth: null,

    format : 'Y-m',

    createPicker: function () {
        var me = this,
            format = Ext.String.format,
            pickerConfig;
        pickerConfig = {
            pickerField: me,
            ownerCmp: me,
            renderTo: document.body,
            floating: true,
            hidden: true,
            focusOnShow: true,
            minDate: me.minValue,
            maxDate: me.maxValue,
            disabledDatesRE: me.disabledDatesRE,
            disabledDatesText: me.disabledDatesText,
            disabledDays: me.disabledDays,
            disabledDaysText: me.disabledDaysText,
            format: me.format,
            showToday: me.showToday,
            startDay: me.startDay,
            minText: format(me.minText, me.formatDate(me.minValue)),
            maxText: format(me.maxText, me.formatDate(me.maxValue)),
            listeners: {
                select: { scope: me, fn: me.onSelect },
                monthdblclick: { scope: me, fn: me.onOKClick },
                yeardblclick: { scope: me, fn: me.onOKClick },
                OkClick: { scope: me, fn: me.onOKClick },
                CancelClick: { scope: me, fn: me.onCancelClick }
            },
            keyNavConfig: {
                esc: function () {
                    me.collapse();
                }
            }
        };
        if (Ext.isChrome) {
            me.originalCollapse = me.collapse;
            pickerConfig.listeners.boxready = {
                fn: function () {
                    this.picker.el.on({
                        mousedown: function () {
                            this.collapse = Ext.emptyFn;
                        },
                        mouseup: function () {
                            this.collapse = this.originalCollapse;
                        },
                        scope: this
                    });
                },
                scope: me,
                single: true
            }
        }
        return Ext.create('Ext.picker.Month', pickerConfig);
    },
    onCancelClick: function () {
        var me = this;
        me.selectMonth = null;
        me.collapse();
    },
    onOKClick: function () {
        var me = this;
        if(!me.selectMonth)
            me.selectMonth = new Date();

        me.setValue(me.selectMonth);
        me.fireEvent('select', me, me.selectMonth);

        me.collapse();
    },
    onSelect: function (m, d) {
        var me = this;
        me.selectMonth = new Date((d[0] + 1) + '/1/' + d[1]);
    }
});