function refselect(name, value, name_d, value_d) {
    name.value = value;
    name_d.value = value_d;
    if (typeof(name) != "undefined")
    {
        if (typeof(name.onchange) != "undefined" && name.onchange != null)
        {
            name.onchange();
        }
    }
    window.close();
}
function refselect_brhid(name, value, brhjb, value_brhjb, brhname, value_brhname) {
    name.value = value;
    brhjb.value = value_brhjb;
    brhname.value = value_brhname;
    window.close();
}

function buttonClick(pageno, pagecount) {
    winform.Plat_Form_Request_List_Pageno.value = pageno;
    winform.Plat_Form_Request_List_PageCount.value = pagecount;
    winform.submit();
}