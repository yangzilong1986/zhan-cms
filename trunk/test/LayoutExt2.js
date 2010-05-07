/*
 * <p>Title: LoonFramework</p>
 * <p>Description:Ext的Layout用例</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: LoonFramework</p>
 * <p>License: http://www.apache.org/licenses/LICENSE-2.0</p>
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */

Ext.onReady(function() {
    //设定布局器及面板   


    var layout;
    var Pane = Ext.Panel;
    var Border = Ext.Viewport;
    //初始化Ext状态管理器，此类可返回用户在Cookie中的操作状态   
    Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
    //生成内部布局   
    var item1 = new Pane({
        title: '选项1'

    });

    var item2 = new Pane({
        title: '选项2'
    });

    var item3 = new Pane({
        title: '选项3'
    });
    //表格   
    var grid = new Ext.grid.PropertyGrid({
        title: '表格嵌套',
        closable: true,
        source:  {
            "(name)": "grid",
            "grouping": false,
            "autoFitColumns": true,
            "productionQuality": false,
            "created": new Date(Date.parse('03/18/2008')),
            "tested": false,
            "version": .01,
            "borderWidth": 1
        }
    });
    layout = new Border({
        layout: 'border',
        items: [
            {
                region: 'west',
                title: 'west',
                //此布局采用折叠样式
                layout: 'accordion',
                collapsible: true,
                width: 100,
                minWidth: 70,
                maxWidth: 150,
                split: true,
                //注入itme1 to 3
                items: [item1, item2, item3]
            },
            {
                region: 'center',
                title: 'center',
                layout:'fit',
                collapsible: true,
                split:true,
                margins:'0 0 0 0',
                //注入表格
                items:[grid]
            },
            {
                title: 'south',
                //是否同步收缩
                collapsible: true,
                //收缩方式
                collapseMode: 'mini',
                region: 'south',
                margins: '0 5 10 5',
                height: 50,
                split: true
            }
        ]
    });
            window.alert("aaa");

}


        );
