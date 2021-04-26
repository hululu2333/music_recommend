{
    let view={
        el:'#newSongs',
        init(){
            this.$el=$(this.el)
        },
        template:`
        <li>
            <a href="./song.html?id={{id}}">
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
            console.log(songs)
            songs.map((songData)=>{
                let need='id url name singer'.split(' ')
                let li=this.template
                need.map((string)=>{
                    return li=li.replace(`{{${string}}}`,songData[string])
                })
                return this.$el.append(li)
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
            query.limit(10)
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