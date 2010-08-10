function CharMode(ch){
       if (ch>=48 && ch <=57) //����
        return 1;
       if (ch>=65 && ch <=90) //��д��ĸ
        return 2;
       if (ch>=97 && ch <=122) //Сд
        return 4;
       else
        return 8; //�����ַ�
    }

    function bitTotal(num){
       var modes=0;
       for (i=0;i<4;i++){
        if (num & 1) modes++;
        num>>>=1;
       }
       return modes;
    }

    function checkStrong(pwd){ //���������ǿ�ȼ���

       if(pwd.length < 6)
        return 0;
       var Modes=0;
       for (i=0;i<pwd.length;i++){
        //����ÿһ���ַ������ͳ��һ���ж�����ģʽ.
        Modes |= CharMode(pwd.charCodeAt(i));
       }
       return bitTotal(Modes);
    }

    function pwStrength(pwd){
       var O_color="#eeeeee";
       var L_color="#FF0000";
       var M_color="#FF9900";
       var H_color="#33CC00";
       if (pwd==null||pwd==''){
           document.getElementById("pwdStrenTab").style.display = "none";
           Lcolor=Mcolor=Hcolor=O_color;
       }else {
           document.getElementById("pwdStrenTab").style.display = "inline";
        var S_level=checkStrong(pwd);
        switch(S_level) {
         case 0:
          Lcolor=Mcolor=Hcolor=O_color;
         case 1:
          Lcolor=L_color;
          Mcolor=Hcolor=O_color;
          break;
         case 2:
          Lcolor=Mcolor=M_color;
          Hcolor=O_color;
          break;
         default:
          Lcolor=Mcolor=Hcolor=H_color;
        }
       }
       document.getElementById("strength_L").style.background=Lcolor;
       document.getElementById("strength_M").style.background=Mcolor;
       document.getElementById("strength_H").style.background=Hcolor;
       return;
    }