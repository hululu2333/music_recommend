{
    let view={
        el:'#tabs',
        init(){
            this.$el=$(this.el)
        },
        highLight($li){
            $li.addClass('active').siblings().removeClass('active')
        }
    }
    let model={
        data:{
        },
        select:null
    }
    let controller={
        init(view,model){
            this.view=view
            this.view.init()
            this.model=model
            this.bindEvents()
        },
        bindEvents(){
            this.view.$el.on('click','li',(e)=>{
                this.model.select=e.currentTarget.getAttribute('data-page-list')
                this.view.highLight($(e.currentTarget))
                let copy=JSON.parse(JSON.stringify(this.model.select))
                window.eventHub.emit('tabSwitch',copy)
            })
        }
    }
    controller.init(view,model)
}