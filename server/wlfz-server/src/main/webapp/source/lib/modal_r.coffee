define [
    'jquery',
    'underscore'
], ($, _) ->

    tmp_modal = """
        
    <div class="modal_r" style="right:-715px;display:none">
        <div class="modal-header <%= i_class%> "><p class="title "><label class="close">×</label><i class="<%= i_class%>"></i><%= title %></p></div>

        <form class="form-y" onsubmit="return false;" id="form_send_msg">
            <div class="modal-body" style="height:<%= height%>px">
               
            </div>
        
         <div class="modal-footer">
            
            </div>
        </form>
        <!--[if lte IE 6]>
            <iframe style="position:absolute; visibility:inherit; top:0; left:0; width:100%; height:100%; z-index:-1; " frameborder="0"></iframe>   
        <![endif]-->
    </div>

        
        """

    class ModalR
        defaults:
            title: "侧滑弹出层"
            width: 500
            height: 'auto'
            button: "确定"
            content: ""
            target: null
            i_class:"",
            hasMask: true

        constructor: (options = {}) ->
            options = $.extend {}, @defaults, options
            options.height = @countHeight()

            @options = options
            @create()
            
        bindEvent: ->
            self = @
            $('.btn-cancel, label.close', @$modal).on 'click', ->
                self.destroy()

            _.each $('input[type=button]:not(.btn-cancel)', @$modal), (button, i) ->
                $(button).on 'click', _.debounce (event) ->
                    event.stopPropagation()
                    self.options.button[i]?.callback(self.$form.serialize(),event)
                    if self.options.button[i]?.autoremove
                        self.destroy()
                , 800, true
                
            $(window).on 'resize' , ->
                $('.modal-body',self.$modal).height(self.countHeight()+'px')

        countHeight:->
            h1 = if $('#header') then $('#header').height()  else 0
            h2 = if $('#footer') then $('#footer').height()  else 0
            h3 = if $('.nav-top') then $('.nav-top').height()  else 0
            height = $(window).height()-96

        create: ->
            """
                <% if (button) { %>
                <% _.each(button, function(b) { %>
                <input class="<%= b.class %>" style="<%= b.style %>" type="button" value="<%= b.value %>" />
                <% }); %>
                <% }; %>
                <input class="btn-small btn-cancel" type="button" value="取消" />
            """

            if @options.hasMask and $('.modal-wrapper').length == 0
                @$mask = $('<div class="modal-wrapper"></div>')
                $('body').append(@$mask)


            @$modal = $(_.template(tmp_modal,@options))
            footer = []
            _.each @options.button, (b) ->
                footer.push "<input class=\"#{ b.class }\" style=\"#{ b.style }\" type=\"button\" value=\"#{ b.value }\" />"
            $('.modal-footer', @$modal).html(footer.join('') + "<input class=\"btn-small btn-cancel\" type=\"button\" value=\"取消\" />")
            $('.modal-body', @$modal).html(@options.content)
            # form
            @$form = $('form', @$modal)
            $('body').append(@$modal)
            # bind events
            @bindEvent()
            @show()

            @options.cb?()
        show:->
            @$modal.show().stop().animate right:"0px", { easing: 'easeOutQuart', duration: 500 }
            $(document.body).css('overflow','hidden')
        destroy: ->
            self = @
            $(document.body).css('overflow','')

            @$modal.stop().animate right: "-715px", { 
                easing: 'easeOutExpo',
                duration: 400 ,
                complete: ->
                    self.$modal.remove()
                    self.$mask?.remove()
            }




