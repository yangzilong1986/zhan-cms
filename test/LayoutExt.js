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
LayoutExt = function(){
    //设定布局器及面板
    //Ext1.1为Ext.BorderLayout
    var Viewport = Ext.Viewport;
    //变量设置
    var root;
    var layout;
    //返回LayoutExt操作结果到onReady
    return {
        init: function(){
            root = this;
            //初始化Ext状态管理器，此类可返回用户在Cookie中的操作状态
            Ext.state.Manager.setProvider(new Ext.state.CookieProvider());

            layout = new Viewport({
                //布局方式，'border'
                layout: 'border',
                items: [ //北
                {
                    region: 'north', //显示区域
                    contentEl: 'north', //绑定的content
                    title: 'North', //名称
                    split: false,//分割线
                    collapsible: true, //是否允许折起
                    //在ie下无此项会报错(firefox无事……),默认分别为此布局左、上、右、下的边距,以此防止越界造成的崩溃。
                    //也可写作 'Object:数值'的形式，如 margins：{top: 0, left: 0, right:0, bottom: 0}
                    margins: '0 0 0 0'
                }, //西
                {
                    region: 'west', //显示区域
                    contentEl: 'west', //绑定的content
                    title: 'West', //名称
                    split: true,//分割栏
                    width: 80, //宽
                    margins: '0 0 0 0' //在ie下无此项会报错
                }, //东
                {
                    region: 'east', //显示区域
                    contentEl: 'east', //绑定的content
                    title: 'East', //名称
                    width: 80,
                    split: true,//分割栏
                    margins: '0 0 0 0' //在ie下无此项会报错
                }, //南
                {
                    region: 'south', //显示区域
                    contentEl: 'south', //绑定的content
                    title: 'South', //名称
                    split: true,//分割栏
                    margins: '0 0 0 0' //在ie下无此项会报错
                }, //中
                 new Ext.TabPanel({
                                    region: 'center',
                                    deferredRender: false,
                                    activeTab: 0, //活动的tab索引
                                    items: [{
                                        contentEl: 'center1',
                                        title: 'cen1',
                                        closable: true, //关闭项
                                        autoScroll: true //是否自动显示滚动条
                                    }, {
                                        contentEl: 'center2',
                                        title: '中央区域2',
                                        autoScroll: true
                                    }]
                 })
                ]
            });
        }
    };
}
();
 //加载onReady
Ext.onReady(LayoutExt.init, LayoutExt, true); 