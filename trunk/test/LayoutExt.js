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
LayoutExt = function(){
    //�趨�����������
    //Ext1.1ΪExt.BorderLayout
    var Viewport = Ext.Viewport;
    //��������
    var root;
    var layout;
    //����LayoutExt���������onReady
    return {
        init: function(){
            root = this;
            //��ʼ��Ext״̬������������ɷ����û���Cookie�еĲ���״̬
            Ext.state.Manager.setProvider(new Ext.state.CookieProvider());

            layout = new Viewport({
                //���ַ�ʽ��'border'
                layout: 'border',
                items: [ //��
                {
                    region: 'north', //��ʾ����
                    contentEl: 'north', //�󶨵�content
                    title: 'North', //����
                    split: false,//�ָ���
                    collapsible: true, //�Ƿ���������
                    //��ie���޴���ᱨ��(firefox���¡���),Ĭ�Ϸֱ�Ϊ�˲������ϡ��ҡ��µı߾�,�Դ˷�ֹԽ����ɵı�����
                    //Ҳ��д�� 'Object:��ֵ'����ʽ���� margins��{top: 0, left: 0, right:0, bottom: 0}
                    margins: '0 0 0 0'
                }, //��
                {
                    region: 'west', //��ʾ����
                    contentEl: 'west', //�󶨵�content
                    title: 'West', //����
                    split: true,//�ָ���
                    width: 80, //��
                    margins: '0 0 0 0' //��ie���޴���ᱨ��
                }, //��
                {
                    region: 'east', //��ʾ����
                    contentEl: 'east', //�󶨵�content
                    title: 'East', //����
                    width: 80,
                    split: true,//�ָ���
                    margins: '0 0 0 0' //��ie���޴���ᱨ��
                }, //��
                {
                    region: 'south', //��ʾ����
                    contentEl: 'south', //�󶨵�content
                    title: 'South', //����
                    split: true,//�ָ���
                    margins: '0 0 0 0' //��ie���޴���ᱨ��
                }, //��
                 new Ext.TabPanel({
                                    region: 'center',
                                    deferredRender: false,
                                    activeTab: 0, //���tab����
                                    items: [{
                                        contentEl: 'center1',
                                        title: 'cen1',
                                        closable: true, //�ر���
                                        autoScroll: true //�Ƿ��Զ���ʾ������
                                    }, {
                                        contentEl: 'center2',
                                        title: '��������2',
                                        autoScroll: true
                                    }]
                 })
                ]
            });
        }
    };
}
();
 //����onReady
Ext.onReady(LayoutExt.init, LayoutExt, true); 