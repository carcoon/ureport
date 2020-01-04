/**
 * Created by jacky on 2016/7/9.
 */
export function alert(msg){
    const dialog=buildDialog('消息提示',msg);
    dialog.modal('show');
};

export function confirm(msg,callback){
    const dialog=buildDialog('确认提示',msg,[{
        name:'确认',
        click:function(){
            callback.call(this);
        }
    }]);
    dialog.modal('show');
};

export function dialog(title,content,callback){
    const dialog=buildDialog(title,content,[{
        name:'确认',
        click:function(){
            callback.call(this);
        }
    }]);
    dialog.modal('show');
};

export function showDialog(title,dialogContent,buttons,events,large){
    const dialog=buildDialog(title,dialogContent,buttons,large);
    dialog.modal('show');
    if(events){
        for(let event of events){
            dialog.on(event.name,event.callback);
        }
    }
};
export function showPopDialog(title,url,width,height){
    let stylew='';
    let styleh='';
    if(width!==null && width!==''){
        stylew='width:'+width+'pt;'
    }
    if(height!==null && height!==''){
        styleh=' style=\"'+stylew+'height:'+height+'pt;\"'
    }
    let dialogContent=`<div ${styleh}><iframe  src=\"${url}\" style=\"width:100%;height:100%;border-width:0 \"></iframe></div>'`;
    const dialog=buildDialog(title,dialogContent,null,null,width,height);
    dialog.modal('show');

};
function buildDialog(title,dialogContent,buttons,large,width,height){
    const className='modal-dialog'+ (large ? ' modal-lg' : '');
    let modal=$(`<div class="modal fade" tabindex="-1" role="dialog" aria-hidden="true"></div>`);
    let dialog=$(`<div class="${className}"></div>`);
    let stylew='';
    let styleh='';
     if(width!==null && width!==''){
        stylew=' style=\"width:'+(width+30)+'pt;\"'
    }
    if(height!==null && height!==''){
        styleh=' style=\"height:'+(height+20)+'pt;\"'
    }
    modal.append(dialog);
    let content=$(`<div class="modal-content" ${stylew}>
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
               &times;
            </button>
            <h4 class="modal-title">
               ${title}
            </h4>
         </div>
         <div class="modal-body"  ${styleh}>
            ${typeof(dialogContent)==='string' ? dialogContent : ''}
         </div>`);
    if(typeof(dialogContent)==='object'){
        content.find('.modal-body').append(dialogContent);
    }
    dialog.append(content);
    let footer=$(`<div class="modal-footer"></div>`);
    content.append(footer);
    if(buttons){
        buttons.forEach((btn,index)=>{
            let button=$(`<button type="button" class="btn btn-default">${btn.name}</button>`);
            button.click(function(e){
                btn.click.call(this);
                if(!btn.holdDialog){
                    modal.modal('hide');
                }
            }.bind(this));
            footer.append(button);
        });
    }else{
        let okBtn=$(`<button type="button" class="btn btn-default" data-dismiss="modal">确定</button>`);
        footer.append(okBtn);
    }

    modal.on("show.bs.modal", function() {
        var index=1050;
        $(document).find('.modal').each(function (i,d) {
            var zIndex=$(d).css('z-index');
            if(zIndex && zIndex!=='' && !isNaN(zIndex)){
                zIndex=parseInt(zIndex);
                if(zIndex>index){
                    index=zIndex;
                }
            }
        });
        index++;
        modal.css({'z-index':index});
    });
    return modal;
};