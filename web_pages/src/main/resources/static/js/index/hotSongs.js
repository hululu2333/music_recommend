{
    let view={
        el:'#hotSongs',
        init(){
            this.$el=$(this.el)
        },
        template:`
        <li>
            <a href="./song.html?id={{id}}">
                <div class="number" data-number="{{number}}">{{number}}</div>
                <div class="songMessage">
                    <div class="songName">{{name}}</div>
                    <div class="songSinger">
                        <i class="sghot"></i><span>{{singer}}</span>
                    </div>
                </div>
                <div class="playButton"></div>
            </a>
        </li>
        `,
        render(data){
            let {songs}=data
            songs.map((songData,index)=>{
                let need='id url name singer number'.split(' ')
                let li=this.template
                need.map((string)=>{
                    if (string==='number'){
                        index=+index+1
                        if (index<10){
                            index='0'+index
                        }
                        return li=li.replace(/{{number}}/g,index)
                    } else {
                        return li=li.replace(`{{${string}}}`,songData[string])
                    }
                })
                this.$el.append(li)
                if (index<=2){
                    this.$el.find(`div[data-number="${index}"]`).css('color','#DF3436')
                }
                return li
            })
        }
    }
    let model={
        data:{
            songs:[],
            selectedId:null,
        },
        findSongs(){
            var query = new AV.Query('Song')
            query.limit(20)
            query.descending('amount');
            return query.find().then((response)=>{
                return this.data.songs=response.map((song)=>{
                    let {id,attributes}=song
                    return Object.assign({id},attributes)
                })
            })
        },

    }
    let controller={
        init(view,model){
            this.view=view
            this.model=model
            this.view.init()
            this.model.findSongs().then((response)=>{
                this.view.render(this.model.data)
            })
        }
        
    }
    controller.init(view,model)
}