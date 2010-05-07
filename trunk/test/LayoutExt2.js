/*
 * <p>Title: LoonFramework</p>
 * <p>Description:Ext��Layout����</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: LoonFramework</p>
 * <p>License: http://www.apache.org/licenses/LICENSE-2.0</p>
 * @author chenpeng
 * @email��ceponline@yahoo.com.cn
 * @version 0.1
 */

Ext.onReady(function() {
    //�趨�����������   


    var layout;
    var Pane = Ext.Panel;
    var Border = Ext.Viewport;
    //��ʼ��Ext״̬������������ɷ����û���Cookie�еĲ���״̬   
    Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
    //�����ڲ�����   
    var item1 = new Pane({
        title: 'ѡ��1'

    });

    var item2 = new Pane({
        title: 'ѡ��2'
    });

    var item3 = new Pane({
        title: 'ѡ��3'
    });
    //���   
    var grid = new Ext.grid.PropertyGrid({
        title: '���Ƕ��',
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
                //�˲��ֲ����۵���ʽ
                layout: 'accordion',
                collapsible: true,
                width: 100,
                minWidth: 70,
                maxWidth: 150,
                split: true,
                //ע��itme1 to 3
                items: [item1, item2, item3]
            },
            {
                region: 'center',
                title: 'center',
                layout:'fit',
                collapsible: true,
                split:true,
                margins:'0 0 0 0',
                //ע����
                items:[grid]
            },
            {
                title: 'south',
                //�Ƿ�ͬ������
                collapsible: true,
                //������ʽ
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
