define [
    'jquery',
    'underscore'
], ($, _) ->

    tmp_modal = """
        
        <div class="modal-wrapper">
            <div class="modal" style="width:<%= width%>px;margin-left:-<%= width/2 %>px;">
                <div class="modal-header">
                    <i class="close" ></i>
                    <h2><%= title %></h2>
                </div>
                <form onsubmit="return false;" style="padding:0;">
                <div class="modal-body" style="max-height:400px;_height:<%= height %>px;"></div>
                <div class="modal-footer"></div>
                </form>
            </div>
            <!--[if lte IE 6]>
                <iframe style="position:absolute; visibility:inherit; top:0; left:0; width:100%; height:100%; z-index:-1; " frameborder="0"></iframe>   
            <![endif]-->
            
        </div>
        
        """

    class Modal
        defaults:
            title: "弹出层"
            width: 500
            height: 'auto'
            button: "确定"
            content: ""
            target: null

        constructor: (options = {}) ->
            options = $.extend {}, @defaults, options
            @options = options
            @create()
            
        bindEvent: ->
            self = @
            $('.btn-cancel, i.close', @$modal).on 'click', ->
                self.release()

            _.each $('input[type=button]:not(.btn-cancel)', @$modal), (button, i) ->
                $(button).on 'click', _.debounce (event) ->
                    event.stopPropagation()
                    self.options.button[i]?.callback(self.$form.serialize())
                    if self.options.button[i]?.autoremove
                        self.release()
                , 800, true
            


        create: ->
            """
                <% if (button) { %>
                <% _.each(button, function(b) { %>
                <input class="<%= b.class %>" style="<%= b.style %>" type="button" value="<%= b.value %>" />
                <% }); %>
                <% }; %>
                <input class="btn btn-xslarge btn-cancel" type="button" value="取消" />
            """
            @$modal = $(_.template(tmp_modal,@options))
            footer = []
            _.each @options.button, (b) ->
                footer.push "<input class=\"#{ b.class }\" style=\"#{ b.style }\" type=\"button\" value=\"#{ b.value }\" />"
            $('.modal-footer', @$modal).html( "<input class=\"btn btn-xslarge btn-cancel mr20\" type=\"button\" value=\"取消\" />"+footer.join('') )
            $('.modal-body', @$modal).html(@options.content)
            # form
            @$form = $('form', @$modal)
            $('body').append(@$modal)
            # bind events
            @bindEvent()
            $(document.body).css('overflow','hidden')

        release: ->
            @$modal.remove()
            @options.close?()
            $(document.body).css('overflow','')




